var param={};
$(document).ready(function(){
	initParams();
	queryList();
});

function initParams(){
	initStartEndDate($('#startDateDiv'),$('#endDateDiv'));
	initPagingToolbar(queryList);
	//初始化查询按钮
	$("#queryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryList();
	});
	//初始化导出按钮
	initExportBtn($("#exportBtn"),$("#conditionFrom"),$("#fileName").val());
}

function queryList(){
	param={};
	param.startdate =  $.trim($("#startDate").val());
	param.enddate =  $.trim($("#endDate").val());
	param.keyword = $.trim($("#keyword").val());
	param.status = $('#status option:selected').val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	param.tm = new Date().getTime();
	
	var tbody = $("#userReceiptTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'person/userReceipt_queryList.action',
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
            		 html += '<td>'+item.billTypeDesc+'</td>';
            		 html += getTdHtml(item.billHead, 15);
            		 html += getTdHtml(item.time, 15);
            		 html += '<td class="text-rignt">'+item.moneyDesc+'</td>';
            		 html += '<td>'+item.statusDesc+'</td>';
            		 html += '<td>'+item.recipient+'</td>';
            		 html += '<td>'+item.phone+'</td>';
            		 html += getTdHtml(item.address, 15);
            		 html += '<td>'+item.username+'</td>';
            		 html += '<td>'+item.addTimeDesc+'</td>';
            		 if(item.status > 1){
            			 html += '<td><a class="a-blue" href="person/userReceiptVerify.action?id='+item.id+'&requestype=isdetail">详细</a></td>';
            		 }else{
            			 html += '<td><a class="a-green" href="person/userReceiptVerify.action?id='+item.id+'&requestype=isverify">审核</a></td>';
            		 }
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

