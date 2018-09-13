var param={};
$(document).ready(function(){
	initParams();
	queryList();
});

function initParams(){
	initStartEndDate($('#startDateDiv'),$('#endDateDiv'));
	initPagingToolbar(queryList);
	//初始化资金日志列表查询按钮
	$("#queryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryList();
	});
	$("#userType").on("change",function(){
		$("#queryBtn").click();
	});
	$("#logType").on("change",function(){
		$("#queryBtn").click();
	});
	$("#direction").on("change",function(){
		$("#queryBtn").click();
	});
	$("#resetBtn").on("click",function(){
		resetForm($("#conditionFrom"));
		$("#queryBtn").click();
	});
	//初始化资金日志列表导出按钮
	initExportBtn($("#exportBtn"),$("#conditionFrom"),$("#fileName").val(),$("#accountLogTable"));
	bindKey13([$("#keyword")],$("#queryBtn"));//回车自动搜索
}

function queryList(){
	param={};
	param.startdate =  $.trim($("#startDate").val());
	param.enddate =  $.trim($("#endDate").val());
	param.keyword = $.trim($("#keyword").val());
	param.usertype = $('#userType option:selected').val();
	param.logtype = $('#logType option:selected').val();
	param.direction = $('#direction option:selected').val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	param.tm = new Date().getTime();
	
	var tbody = $("#accountLogTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'deal/accountLog_queryList.action',
		data:param,
		dataType:'json',
        cache: false,
        beforeSend:function(){$("#loading").removeClass("hide");},
        complete:function(){
	    	  $("#loading").addClass("hide"); 
	      },
        success: function(data){
        	 if(data.page){
        		 tbody.empty();
        		 var dataList = data.page.root;
        		 $(dataList).each(function(index,item){
        			 html += '<tr>';
        			/* html += '<td>'+item.userId+'</td>';
        			 html += getTdHtml(item.username, 15);*/
        			 html += '<td>'+getNotNullData(item.phone)+'</td>';
        			 html += '<td>'+getNotNullData(item.userTypeDesc)+'</td>';
//        			 html += '<td class="text-right">'+item.recordId+'</td>';
        			 html += '<td class="text-center">';
        			 html += '<a href="javascript:;" onclick="queryRecoreDetail('+item.type+','+item.recordId+','+item.usertype+')">'+item.recordId+'</a>';
        			 html += '</td>';
        			 html += '<td>'+getNotNullData(item.typeDesc)+'</td>';
        			 html += '<td>'+getNotNullData(item.directionDesc)+'</td>';
        			 html += '<td class="text-center">'+item.operateMoneyDesc+'</td>';
        			 html += '<td class="text-center">'+item.totalMoneyDesc+'</td>';
        			 html += '<td class="text-center">'+item.usableMoneyDesc+'</td>';
        			 html += '<td class="text-center">'+item.freezeMoneyDesc+'</td>';
        			 html += '<td>'+getNotNullData(item.addTimeStr)+'</td>';
        			 html += '</tr>';
        		 });
        		 tbody.html(html);
        		 
        		 var currentPage = data.page.startRow/(data.page.endRow-data.page.startRow)+1;
        		 var totalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
        		 setPagingToolbarParams(data.page.totalProperty, totalPage,currentPage);
        	 }else{
            	 showMsg(data.message, data.errormsg);
             }
         }
     });
}
function queryRecoreDetail(type,recordid,usertype){
	if(ACCOUNTLOG_TYPE_RECHARGE == type){//充值
		href("deal/rechargeDetailInit.action?rechargeid="+recordid);
	}else if(ACCOUNTLOG_TYPE_CARD_RECHARGE == type){
		href("deal/cardRechargeDetailInit.action?id="+recordid);
	}else if(ACCOUNTLOG_TYPE_UNACC_CHARGING == type || ACCOUNTLOG_TYPE_CARDACC_CHARGING == type || ACCOUNTLOG_TYPE_BADCARDACC_CHARGING == type){//平台账户充电 或 非平台账户充电
		href("deal/chargeDetailInit.action?paymentid="+recordid);
	}else if(ACCOUNTLOG_TYPE_ACC_CHARGING == type ){
		href("deal/chargeDetailInit.action?paymentid="+recordid+"&isUser=1");	
	}else if(ACCOUNTLOG_TYPE_ACC_APPOINTMENT == type || ACCOUNTLOG_TYPE_UNACC_APPOINTMENT == type){//平台账户预约 或 非平台账户预约
		href("deal/appointmentDetailInit.action?appointmentid="+recordid);
	}else if(ACCOUNTLOG_TYPE_APPLY_CASH == type || ACCOUNTLOG_TYPE_VALID_CASH == type){//申请提现 或 审核提现
		href("deal/cashVerifyDetail.action?id="+recordid+"&usertype="+usertype);
	}else if(ACCOUNTLOG_TYPE_BILL == type){//结算
		queryBills(type,recordid);
	}
}

function queryBills(type,recordid){
	param={};
	param.type = type;
	param.recordid = recordid;
	$.ajax({
		type:'POST',
		url:'deal/accountLog_queryRecordDetail.action',
		data:param,
		dataType:'json',
		cache:false,
		success:function(data){
			if(data.billsInfo){
				showResult("showbills");
				var billsInfo = data.billsInfo;
				$("#bills_userid").text(billsInfo.userId);
				$("#bills_usertype").text(billsInfo.usertypeDesc);
				$("#bills_username").text(billsInfo.username);
				$("#bills_phone").text(billsInfo.phone);
				$("#bills_checkcycle").text(billsInfo.checkCycle);
				$("#bills_totalfeein").text(billsInfo.totalFeeInDesc);
				$("#bills_appfeein").text(billsInfo.appFeeInDesc);
				$("#bills_chafeein").text(billsInfo.chaFeeInDesc);
				$("#bills_servicefeein").text(billsInfo.serviceFeeInDesc);
				$("#bills_parkfeein").text(billsInfo.parkFeeInDesc);
			}else{
				showMsg(data.message, data.errormsg);
			}
		}
	});
	
}

function showResult(flag){
	if("showbills" == flag){
		$("#accountLogResult").addClass("hide");
		$("#billsResult").removeClass("hide");
	}else{
		$("#accountLogResult").removeClass("hide");
		$("#billsResult").addClass("hide");
	}
}


