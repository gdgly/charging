var param={};
var jsonobj;
var jsonstr;
$(document).ready(function(){
	initParams();
});

function initParams(){
	var date = new Date(datatime+"-01");
	initMonth($('#detailDateDiv'),date);
	if(detailtype == 'indetail'){
		//查询账单收入明细
		initPagingToolbar(queryInDetail);
		$("#queryBtn").on("click",function(){
			setPagingToolbarParams(0, 0, 1);
			queryInDetail();
		});
		queryInDetail();
	}else if(detailtype == 'outdetail'){
		initPagingToolbar(queryOutDetail);
		//查询账单支出明细
		$("#queryBtn").on("click",function(){
			setPagingToolbarParams(0, 0, 1);
			queryOutDetail();
		});
		queryOutDetail();
	}else if(detailtype == 'rechargedetail'){
		initPagingToolbar(queryRechargeDetail);
		//查询账单支出明细
		$("#queryBtn").on("click",function(){
			setPagingToolbarParams(0, 0, 1);
			queryRechargeDetail();
		});
		queryRechargeDetail();
	}else if(detailtype == 'cashdetail'){
		initPagingToolbar(queryCashDetail);
		//查询账单支出明细
		$("#queryBtn").on("click",function(){
			setPagingToolbarParams(0, 0, 1);
			queryCashDetail();
		});
		queryCashDetail();
	}
	//初始化明细列表导出按钮
	initExportBtn($("#exportBtn"),$("#conditionFrom"),$("#fileName").val());
	
}

function queryInDetail(){
	param = {};
	param.userid = userid;
	param.checkcycle = $.trim($("#detailDate").val());
	param.checkmark = $("#billMarkin option:selected").val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	
	var tbody = $("#detailInTable").find("tbody");
	var html = '';
	$.ajax({
		type:'POST',
		url:'deal/bills_queryInDetail.action',
		data:param,
		dataType:'json',
		cache:false,
		success:function(data){
			if(data.page){
				 var totalObj = data.page.obj;
				 if(totalObj){
					 $("#appFeeIn").text("预约收入："+(isEmpty(totalObj.appFeeInDesc)?"0.00":totalObj.appFeeInDesc));
	            	 $("#chaFeeIn").text("充电收入："+(isEmpty(totalObj.chaFeeInDesc)?"0.00":totalObj.chaFeeInDesc));
	            	 $("#serviceFeeIn").text("服务收入："+(isEmpty(totalObj.serviceFeeInDesc)?"0.00":totalObj.serviceFeeInDesc));
	            	 $("#parkFeeIn").text("停车收入："+(isEmpty(totalObj.parkFeeInDesc)?"0.00":totalObj.parkFeeInDesc));
	            	 $("#totalFeeIn").text("总收入："+(isEmpty(totalObj.totalFeeInDesc)?"0.00":totalObj.totalFeeInDesc));
				 }else{
					 $("#appFeeIn").text("预约收入：0.00");
	            	 $("#chaFeeIn").text("充电收入：0.00");
	            	 $("#serviceFeeIn").text("服务收入：0.00");
	            	 $("#parkFeeIn").text("停车收入：0.00");
	            	 $("#totalFeeIn").text("总收入：0.00");
				 }
            	
				 tbody.empty();
            	 var dataList = data.page.root;
            	 $(dataList).each(function(index,item){
            		 html += '<tr>';
//            		 html += '<tr style="cursor:pointer;" onclick="showRecordDetail(this);" recordid="'+item.recordId+'" checkmark="'+item.checkMark+'">';
//            		 html += '<td>'+item.id+'</td>';
            		 html += '<td>'+item.addTimeDesc+'</td>';
            		 html += getTdHtml(item.stationPileName, 10);
            		 html += '<td>'+item.checkMarkDesc+'</td>';
            		 html += '<td class="text-right">'+getNotNullData(item.appFeeInDesc)+'</td>';
            		 html += '<td class="text-right">'+getNotNullData(item.chaFeeInDesc)+'</td>';
            		 html += '<td class="text-right">'+getNotNullData(item.serviceFeeInDesc)+'</td>';
            		 html += '<td class="text-right">'+getNotNullData(item.parkFeeInDesc)+'</td>';
            		 html += '<td class="text-right">'+getNotNullData(item.totalFeeInDesc)+'</td>';
            		 html += '<td class="text-right">'+getNotNullData(item.totalFeeDesc)+'</td>';
            		 html += '<td class="text-right">'+item.recordId+'</td>';
            		 html += '</tr>';
            	 });
            	 tbody.html(html);
            	 var currentPage = data.page.startRow/(data.page.endRow-data.page.startRow)+1;
            	 var totalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
            	 setPagingToolbarParams(data.page.totalProperty, totalPage, currentPage);
			}else{
				showMsg(data.message, data.errormsg);
			}
		}
	});
}

function queryOutDetail(){
	param = {};
	param.userid = userid;
	param.checkcycle = $.trim($("#detailDate").val());
	param.checkmark = $("#billMarkout option:selected").val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	
	var tbody = $("#detailOutTable").find("tbody");
	var html = '';
	$.ajax({
		type:'POST',
		url:'deal/bills_queryOutDetail.action',
		data:param,
		dataType:'json',
		cache:false,
		success:function(data){
			if(data.page){
				 var totalObj = data.page.obj;
				 if(totalObj){
					 $("#appFeeOut").text("预约支出："+(isEmpty(totalObj.appFeeDesc)?"0.00":totalObj.appFeeDesc));
	            	 $("#chaFeeOut").text("充电支出："+(isEmpty(totalObj.chaFeeDesc)?"0.00":totalObj.chaFeeDesc));
	            	 $("#serviceFeeOut").text("服务支出："+(isEmpty(totalObj.serviceFeeDesc)?"0.00":totalObj.serviceFeeDesc));
	            	 $("#parkFeeOut").text("停车支出："+(isEmpty(totalObj.parkFeeDesc)?"0.00":totalObj.parkFeeDesc));
	            	 $("#totalFeeOut").text("总支出："+(isEmpty(totalObj.totalFeeDesc)?"0.00":totalObj.totalFeeDesc));
				 }else{
					 $("#appFeeOut").text("预约支出：0.00");
	            	 $("#chaFeeOut").text("充电支出：0.00");
	            	 $("#serviceFeeOut").text("服务支出：0.00");
	            	 $("#parkFeeOut").text("停车支出：0.00");
	            	 $("#totalFeeOut").text("总支出：0.00");
				 }
            	
				 tbody.empty();
            	 var dataList = data.page.root;
            	 $(dataList).each(function(index,item){
            		 html += '<tr>';
//            		 html += '<tr style="cursor:pointer;" onclick="showRecordDetail(this);" recordid="'+item.recordId+'" checkmark="'+item.checkMark+'">';
            		 html += '<td>'+item.addTimeDesc+'</td>';
            		 html += getTdHtml(item.stationPileName, 10);
            		 html += '<td>'+item.checkMarkDesc+'</td>';
            		 html += '<td class="text-right">'+item.appFeeDesc+'</td>';
            		 html += '<td class="text-right">'+item.chaFeeDesc+'</td>';
            		 html += '<td class="text-right">'+item.serviceFeeDesc+'</td>';
            		 html += '<td class="text-right">'+item.parkFeeDesc+'</td>';
            		 html += '<td class="text-right">'+item.totalFeeDesc+'</td>';
            		 html += '<td class="text-right">'+item.recordId+'</td>';
            		 html += '</tr>';
            	 });
            	 tbody.html(html);
            	 var currentPage = data.page.startRow/(data.page.endRow-data.page.startRow)+1;
            	 var totalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
            	 setPagingToolbarParams(data.page.totalProperty, totalPage, currentPage);
			}else{
				showMsg(data.message, data.errormsg);
			}
		}
	});
}

function queryRechargeDetail(){
	param = {};
	param.userid = userid;
	param.checkcycle = $.trim($("#detailDate").val());
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	
	var tbody = $("#detailRechargeTable").find("tbody");
	var html = '';
	$.ajax({
		type:'POST',
		url:'deal/bills_queryRechargeDetail.action',
		data:param,
		dataType:'json',
		cache:false,
		success:function(data){
			if(data.page){
				 var totalObj = data.page.obj;
				 if(totalObj){
					 $("#totalMoney").text("充值总额："+totalObj);
				 }else{
					 $("#totalMoney").text("充值总额：0.00");
				 }
            	
				 tbody.empty();
            	 var dataList = data.page.root;
            	 $(dataList).each(function(index,item){
            		 html += '<tr>';
//            		 html += '<tr style="cursor:pointer;" onclick="showRecordDetail(this);" recordid="'+item.id+'" checkmark="3">';
            		 html += '<td>'+item.addTimeDesc+'</td>';
            		 html += '<td class="text-right">'+item.moneyDesc+'</td>';
            		 html += '<td>'+item.payWayDesc+'</td>';
            		 html += getTdHtml(item.accountInfo,20);
            		 html += '<td>'+item.tradeNo+'</td>';
            		 html += '<td class="text-right">'+item.id+'</td>';
            		 html += '</tr>';
            	 });
            	 tbody.html(html);
            	 var currentPage = data.page.startRow/(data.page.endRow-data.page.startRow)+1;
            	 var totalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
            	 setPagingToolbarParams(data.page.totalProperty, totalPage, currentPage);
			}else{
				showMsg(data.message, data.errormsg);
			}
		}
	});
}

function queryCashDetail(){
	param = {};
	param.userid = userid;
	param.checkcycle = $.trim($("#detailDate").val());
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	
	var tbody = $("#detailCashTable").find("tbody");
	var html = '';
	$.ajax({
		type:'POST',
		url:'deal/bills_queryCashDetail.action',
		data:param,
		dataType:'json',
		cache:false,
		success:function(data){
			if(data.page){
				 var totalObj = data.page.obj;
				 if(totalObj){
					 $("#totalMoney").text("提现总额："+totalObj);
				 }else{
					 $("#totalMoney").text("提现总额：0.00");
				 }
            	
				 tbody.empty();
            	 var dataList = data.page.root;
            	 $(dataList).each(function(index,item){
            		 html += '<tr>';
//            		 html += '<tr style="cursor:pointer;" onclick="showRecordDetail(this);" recordid="'+item.id+'" checkmark="4">';
            		 html += '<td>'+item.addTimeDesc+'</td>';
            		 html += '<td>'+item.moneyDesc+'</td>';
            		 html += '<td>'+getNotNullData(item.cashWayDesc)+'</td>';
            		 html += '<td>'+getNotNullData(item.cashStatusDesc)+'</td>';
            		 html += '<td>'+item.id+'</td>';
            		 html += '</tr>';
            	 });
            	 tbody.html(html);
            	 var currentPage = data.page.startRow/(data.page.endRow-data.page.startRow)+1;
            	 var totalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
            	 setPagingToolbarParams(data.page.totalProperty, totalPage, currentPage);
			}else{
				showMsg(data.message, data.errormsg);
			}
		}
	});
}

function showRecordDetail(obj){
	var recordid = $(obj).attr("recordid");
	var checkmark = $(obj).attr("checkmark");
	if(checkmark == 1){
		href("deal/chargeDetailInit.action?paymentid="+recordid);
	}else if(checkmark == 2){
		href("deal/appointmentDetailInit.action?appointmentid="+recordid);
	}else if(checkmark == 3){
		href("deal/rechargeDetailInit.action?rechargeid="+recordid);
	}else if(checkmark == 4){
		href("deal/cashVerifyDetail.action?id="+recordid);
	}
}

function showList(showobj){
	if("showBillsList" == showobj){
		$("#billsListResult").removeClass("hide");
		$("#inDetailResult").addClass("hide");
		$("#outDetailResult").addClass("hide");
	}else if("showDetailIn" == showobj){
		$("#billsListResult").addClass("hide");
		$("#inDetailResult").removeClass("hide");
		$("#outDetailResult").addClass("hide");
	}else if("showDetailOut" == showobj){
		$("#billsListResult").addClass("hide");
		$("#inDetailResult").addClass("hide");
		$("#outDetailResult").removeClass("hide");
	}
}




