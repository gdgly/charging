var param={};
var jsonobj;
var jsonstr;
$(document).ready(function(){
	initParams();
	queryList();
});

function initParams(){
	initStartEndMonth($('#startDateDiv'),$('#endDateDiv'));
	initPagingToolbar(queryList);
	//初始化结算账单查询按钮
	$("#queryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryList();
	});
	//初始化结算账单导出按钮
	initExportBtn($("#exportBtn"),$("#conditionFrom"),$("#fileName").val());
	
}

function queryList(){
	param={};
	param.startmonth =  $.trim($("#startDate").val());
	param.endmonth =  $.trim($("#endDate").val());
	param.keyword = $.trim($("#keyword").val());
	param.usertype = $('#userType option:selected').val();
	param.isreceipt = $('#isReceipt option:selected').val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	param.tm = new Date().getTime();
	
	var tbody = $("#billsTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'deal/bills_queryBillsList.action',
		data:param,
		dataType:'json',
        cache: false,
        success: function(data,options){
             if(data.page){
            	 tbody.empty();
            	 var dataList = data.page.root;
            	 $(dataList).each(function(index,item){
            		 html += '<tr>';
            		 html += '<td>'+item.id+'</td>';
            		 html += getTdHtml(item.username, 10);
            		 html += '<td>'+item.phone+'</td>';
            		 html += '<td>'+item.checkCycle+'</td>';
            		 html += '<td class="text-right">'+item.appFeeInDesc+'</td>';
            		 html += '<td class="text-right">'+item.chaFeeInDesc+'</td>';
            		 html += '<td class="text-right">'+item.serviceFeeInDesc+'</td>';
            		 html += '<td class="text-right">'+item.parkFeeInDesc+'</td>';
            		 html += '<td class="text-right"><a href="deal/billsDetail.action?userid='+item.userId+'&datatime='+item.checkCycle+'&detailtype=indetail">'+item.totalFeeInDesc+'</a></td>';
            		 html += '<td class="text-right"><a href="deal/billsDetail.action?userid='+item.userId+'&datatime='+item.checkCycle+'&detailtype=rechargedetail">'+item.rechargeFeeDesc+'</a></td>';
            		 html += '<td class="text-right"><a href="deal/billsDetail.action?userid='+item.userId+'&datatime='+item.checkCycle+'&detailtype=cashdetail">'+item.cashFeeDesc+'</a></td>';
            		 html += '<td class="text-right">'+item.appFeeOutDesc+'</td>';
            		 html += '<td class="text-right">'+item.chaFeeOutDesc+'</td>';
            		 html += '<td class="text-right">'+item.serviceFeeOutDesc+'</td>';
            		 html += '<td class="text-right">'+item.parkFeeOutDesc+'</td>';
            		 html += '<td class="text-right"><a href="deal/billsDetail.action?userid='+item.userId+'&datatime='+item.checkCycle+'&detailtype=outdetail">'+item.totalFeeOutDesc+'</a></td>';
            		 html += '<td class="text-right">'+getNotNullData(item.receiptId)+'</td>';
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






