var param={};
$(document).ready(function(){
//	setTabelDivHeight();
	initParams();
	queryRealVerifyList();
});

function initParams(){
	initStartEndDate($('#startDateDiv'),$('#endDateDiv'));
	initPagingToolbar(queryRealVerifyList);
	//查询
	$("#queryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryRealVerifyList();
	});
}

function queryRealVerifyList(){
	param={};
	param.startdate =  $.trim($("#startDate").val());
	param.enddate =  $.trim($("#endDate").val());
	param.busname = $.trim($("#busName").val());
	param.verifystatus = $('#verifyStatus option:selected').val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	param.tm = new Date().getTime();
	
	var tbody = $("#busRealTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'business/busRealTransfer_queryRealVerifyList.action',
		data:param,
		dataType:'json',
        cache: false,
        success: function(data,options){
             if(data.page){
            	 tbody.empty();
            	 var dataList = data.page.root;
            	 $(dataList).each(function(index,item){
            		 html += '<tr>';
            		 html += '<td>'+item.busInfoId+'</td>';
            		 html += '<td>'+item.busName+'</td>';
            		 html += '<td>'+item.addTimeStr+'</td>';
            		 html += '<td>'+item.accRealName+'</td>';
            		 html += '<td>'+item.bankName+'</td>';
            		 html += '<td>'+item.bankAccount+'</td>';
            		 html += '<td>'+item.userId+'</td>';
            		 html += '<td>'+item.validStatusDesc+'</td>';
            		 html += '<td>'+getNotNullData(item.validMoney)+'</td>';
            		 html += '<td>'+getNotNullData(item.remark)+'</td>';
            		 if(item.validStatus > BUS_REAL_VERIFY_STATUS_TRANSFERING){
            			 html += '<td><a class="a-blue" href="business/busRealTransferDetail.action?busInfoId='+item.busInfoId+'&addTimeStr='+item.addTimeStr+'">详细</a></td>';
//            			 html += '<td><a class="a-disable" disabled="disabled" href="javascript:;">审核</a></td>';
            		 }else{
            			 html += '<td><a class="a-green" href="business/busRealTransfer.action?busInfoId='+item.busInfoId+'&addTimeStr='+item.addTimeStr+'">审核</a></td>';
            		 }
            		 
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

function setTabelDivHeight(){
	var height = $(window).height()-235;
	$("#tableDiv").css("height", height);
   
}

