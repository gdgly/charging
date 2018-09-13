var param={};
$(document).ready(function(){
	initParams();
	queryList();
});

function initParams(){
	initStartEndDate($('#startDateDiv'),$('#endDateDiv'));
	initPagingToolbar(queryList);
	$("#queryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryList();
	});
}

function queryList(){
	param={};
	param.startdate =  $.trim($("#startDate").val());
	param.enddate =  $.trim($("#endDate").val());
	param.keyword = $.trim($("#keyword").val());
	param.validstatus = $('#validStatus option:selected').val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	param.tm = new Date().getTime();
	
	var tbody = $("#applyTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'person/userPileApply_queryList.action',
		data:param,
		dataType:'json',
        cache: false,
        success: function(data,options){
             if(data.success){
            	 if(data.page){
            		 tbody.empty();
            		 var dataList = data.page.root;
            		 $(dataList).each(function(index,item){
            			 html += '<tr>';
            			 html += '<td>'+item.id+'</td>';
            			 html += '<td>'+item.realName+'</td>';
            			 html += '<td>'+item.phone+'</td>';
            			 html += getTdHtml(item.address, 10);
            			 html += getTdHtml(item.remark, 10);
            			 html += getTdHtml(item.username, 10);
            			 html += '<td>'+item.userphone+'</td>';
            			 html += '<td>'+item.addTimeDesc+'</td>';
            			 html += '<td>'+item.validStatusDesc+'</td>';
            			 if(item.validStatus > 1){
                			 html += '<td><a class="a-blue" href="person/userPileApplyVerify.action?id='+item.id+'&requestype=isdetail">详细</a></td>';
                		 }else{
                			 html += '<td><a class="a-green" href="person/userPileApplyVerify.action?id='+item.id+'&requestype=isverify">处理</a></td>';
                		 }
                		 html += '</tr>';
            		 });
            		 tbody.html(html);
            		 
            		 var currentPage = data.page.startRow/(data.page.endRow-data.page.startRow)+1;
            		 var totalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
            		 setPagingToolbarParams(data.page.totalProperty, totalPage,currentPage);
            	 }
             }else{
            	 showMsg(data.message, data.errormsg);
             }
         }
     });
}

