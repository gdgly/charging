var param={};
$(document).ready(function(){
	setContentHeight($("#chargeTableDiv"),155)
	initParams();
	queryList();
});

function initParams(){
	initStartEndDate2($('#startDateDiv'),$('#endDateDiv'));
	initStartEndDate3($('#startUpdateDateDiv'),$('#endUpdateDateDiv'));
	initPagingToolbar(queryList);
	//初始化充电记录列表查询按钮
	$("#queryBtn").on("click",function(){
		$("#stationIdForSelectModal").val("");
		setPagingToolbarParams(0, 0, 1);
		queryList();
	});
	$("#isUser").on("change",function(){
		var isuser = $(this).val();
		$("#searchUser").val("");
		$("#stationToType").val(1);
		if(isuser == 1){
			$("#searchUser").attr("disabled",false); 
			$("#stationToType").attr("disabled",false); 
		}else{
			$("#searchUser").attr("disabled",true); 
			$("#stationToType").attr("disabled",true); 
		}
		$("#stationIdForSelectModal").val("");
		setPagingToolbarParams(0, 0, 1);
		queryList();
	});
	$("#stationToType").on("change",function(){
		$("#stationIdForSelectModal").val("");
		setPagingToolbarParams(0, 0, 1);
		queryList();
	});
	$("#chargeResetBtn").on("click",function(){
		reload();
		/*$("#startDate").val("");
		$("#endDate").val("");
		$("#searchTradeno").val("");
		$("#searchStation").val("");
		$("#queryBtn").click();*/
	});
	/*$("#isUser").on("change",function(){
		temp = $(this).val();
		if(temp == 1){
			$("#searchUser").removeClass('hide');
		}else{
			$("#searchUser").val("").addClass('hide');
			
		}
	});*/
	/*$("#isUser").on("change",function(){
		if($(this).val() == 1){
			$("#phoneTh").removeClass("hide");
		}else{
			$("#phoneTh").addClass("hide");
		}
	});*/
	//初始化充电记录列表导出按钮
	initExportBtn($("#exportBtn"),$("#conditionFrom"),$("#fileName").val(),$("#chargeTable"));
 
	bindKey13([$("#searchTradeno"),$("#searchStation"),$("#searchUser")],$("#queryBtn"));//回车自动搜索
}

function queryList(stationId,staName){
	param={};
	if(!isEmpty(stationId)){
		//$("#searchStation").val(staName);
		setPagingToolbarParams(0, 0, 1);
		param.startdate = $("#startDate").val("").val();
		param.enddate = $("#endDate").val("").val();
		param.searchtradeno =$("#searchTradeno").val("").val();
		param.searchuser = $("#searchUser").val("").val();
		param.searchstation = $("#searchStation").val("").val();
		param.dealstatus = $('#dealStatus').val(0).val();
		param.paystatus = $('#payStatus').val(0).val();
		param.billstatus = $('#billStatus').val(0).val();
		param.stationIdForSelectModal = stationId.toString();
		param.startupdatedate = $("#startUpdateDate").val();
		param.endupdatedate = $("#endUpdateDate").val();
		$("#stationIdForSelectModal").val(stationId.toString());
	}else{
		param.startdate =  $.trim($("#startDate").val());
		param.enddate =  $.trim($("#endDate").val());
		param.searchtradeno = $.trim($("#searchTradeno").val());
		param.searchuser = $.trim($("#searchUser").val());
		param.searchstation = $.trim($("#searchStation").val());
		param.dealstatus = $('#dealStatus').val();
		param.paystatus = $('#payStatus').val();
		param.billstatus = $('#billStatus').val();
		param.startupdatedate =  $.trim($("#startUpdateDate").val());
		param.endupdatedate =  $.trim($("#endUpdateDate").val());
		param.stationIdForSelectModal = $("#stationIdForSelectModal").val();
	}
	param.stationToType = $("#stationToType").val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	param.tm = new Date().getTime();
	param.isUser = $('#isUser').val();
	var tbody = $("#chargeTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'deal/charge_queryList.action',
		data:param,
		dataType:'json',
        cache: false,
        beforeSend:function(){$("#loading").removeClass("hide");},
        complete:function(){
	    	  $("#loading").addClass("hide"); 
	      },
        success: function(data,options){
        	if(data.payMent){
        		$("#allChaPowerData").text(getDefaultData(data.payMent.chaPower, 0));
        		$("#allChaFeeData").text(getDefaultData(data.payMent.chaFee,0));
        		$("#allServiceFeeData").text(getDefaultData(data.payMent.serviceFee,0));
        		$("#allShouldFeeData").text(getDefaultData(data.payMent.shouldMoney,0));
        		$("#allChaLenData").text(getDefaultData(data.payMent.chaLen,0));
        	}else{
        		$("#allChaPowerData").text("0");
        		$("#allChaFeeData").text("0");
        		$("#allServiceFeeData").text("0");
        		$("#allShouldFeeData").text("0");
        		$("#allChaLenData").text("0");
        	}
             if(data.page){
            	 tbody.empty();
            	 var dataList = data.page.root;
            	 if($("#isUser").val() != 1){
            		 $("#phoneTh").addClass("hide");
            		 $("#realNameTh").addClass("hide");
            	 }else{
            		 $("#phoneTh").removeClass("hide");
            		 $("#realNameTh").removeClass("hide");
            	 }
            	 $(dataList).each(function(index,item){
            		 html += '<tr>';
            		 html += '<td class="text-center">'+item.stationPileName+'</td>';
            		 html += '<td class="text-center">'+item.tradeNo+'</td>';
            		/* html += '<td>'+item.tradeNo+'</td>';*/
            		 html += '<td class="text-center">'+getNotNullData(item.accountInfo)+'</td>';
            		 html += '<td class="text-center">'+getNotNullData(item.plateNum)+'</td>';
            		 html += '<td class="text-center">'+item.dealStatusDesc+'</td>';		
            		 html += '<td class="text-center">'+item.payStatusDesc+'</td>';	
            		 if($("#isUser").val() == 1){
            			 html += '<td class="text-center">'+getNotNullData(item.realName)+'</td>';	 
            			 html += '<td class="text-center">'+getNotNullData(item.phone)+'</td>';	 
            		 }
            		 html += '<td class="text-center">'+getNotNullData(item.startTimeDesc)+'</td>';
            		 html += '<td class="text-center">'+getDefaultData(item.endTimeDesc,"--")+'</td>';
            		 html += '<td class="text-center">'+getNotNullData(item.chaPower)+'</td>';
            		 html += '<td class="text-center">'+getNotNullData(item.chaFee)+'</td>';
            		 html += '<td class="text-center">'+getNotNullData(item.serviceFee)+'</td>';
            	/*	 html += '<td class="text-center">'+getNotNullData(item.parkFee)+'</td>';*/
            		/* onmouseenter="openDetail(this)" onmouseleave="closeDetail(this)"*/
            		 html += '<td class="text-center"><div'+
            		 ' startTime='+item.startTime+' endTime='+item.endTime+
            		 ' chaLen='+item.chaLen+' chaPower='+item.chaPower+
            		 ' chaFee='+item.chaFee+' parkFee='+item.parkFee+' serviceFee='+item.serviceFee+
            		 ' shouldMoney='+getNotNullData(item.shouldMoney)+'>'+getNotNullData(item.shouldMoney)+'</div></td>';		
            		 html += '<td class="text-center">'+getNotNullData(item.chaLen)+'</td>';
            		/* html += '<td>'+item.isBillDesc+'</td>';*/
            		/* html += '<td>'+item.updateTimeDesc+'</td>';*/
            		 html += '<td class="text-center">'+item.updateTimeDesc+'</td>';
            		 html += '<td class="text-center">'
            		 html += '<a href="deal/chargeDetailInit.action?paymentid='+item.id+'&isUser='+param.isUser+'">详细</a>';
            		 html += '</td>'
            	 });
            	 tbody.html(html);
            	 var currentPage = data.page.startRow/(data.page.endRow-data.page.startRow)+1;
            	 var totalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
            	 setPagingToolbarParams(data.page.totalProperty, totalPage, currentPage);
            	 $("#allCountData").text(data.page.totalProperty);
             }else{
            	 showMsg(data.message, data.errormsg);
             }
         }
     });
}

//显示详细信息
function openDetail(obj){
	$(obj).popover({ 
		html : true,
		title: function() {
			return "<p class='text-center' style='margin-bottom:1px;'>"+'费用明细'+"</p>";
		},
		content: function() {
			var html = '';
			html += '<form role="form" style="width:265px;margin-top:-10px;>';
			html += '<div class="form-group">';
			html += '<label class="col-sm-4 control-label">开始时间</label>';
			html += '<div class="col-sm-8 text-right"><p>'+getFormatDate($(obj).attr("startTime"))+'</p></div>';
			html += '</div>';
			html += '<div class="form-group">';
			html += '<label class="col-sm-4 control-label">结束时间</label>';
			html += '<div class="col-sm-8 text-right"><p>'+getFormatDate($(obj).attr("endTime"))+'</p></div>';
			html += '</div>';
			html += '<div class="form-group">';
			html += '<label class="col-sm-4 control-label">充电时长</label>';
			html += '<div class="col-sm-8 text-right"><p>'+$(obj).attr("chaLen")+' 分钟</p></div>';
			html += '</div>';
			html += '<div class="form-group">';
			html += '<label class="col-sm-4 control-label">充电电量</label>';
			html += '<div class="col-sm-8 text-right"><p>'+$(obj).attr("chaPower")+' 度</p></div>';
			html += '</div>';
			html += '<div class="form-group">';
			html += '<label class="col-sm-4 control-label">充电费用</label>';
			html += '<div class="col-sm-8 text-right"><p>'+$(obj).attr("chaFee")+' 元</p></div>';
			html += '</div>';
			html += '<div class="form-group">';
			html += '<label class="col-sm-4 control-label">停车费用</label>';
			html += '<div class="col-sm-8 text-right"><p>'+$(obj).attr("parkFee")+' 元</p></div>';
			html += '</div>';
			html += '<div class="form-group">';
			html += '<label class="col-sm-4 control-label">服务费用</label>';
			html += '<div class="col-sm-8 text-right"><p>'+$(obj).attr("serviceFee")+' 元</p></div>';
			html += '</div>';
			html += '<div class="form-group">';
			html += '<label class="col-sm-4 control-label">应缴费用</label>';
			html += '<div class="col-sm-8 text-right"><p>'+$(obj).attr("shouldMoney")+' 元</p></div>';
			html += '</div>';
			html += '</form>';
			return html;
		}
	});
	$(obj).popover("show");
}

//关闭详细信息
function closeDetail(obj){
	$(obj).popover("hide");
}



