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
	param.keyword = $.trim($("#keyword").val());
	param.status = $('#verifyStatus option:selected').val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	param.tm = new Date().getTime();
	
	var tbody = $("#userRealTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'person/userRealVerify_queryRealVerifyList.action',
		data:param,
		dataType:'json',
        cache: false,
        success: function(data,options){
             if(data.page){
            	 tbody.empty();
            	 var dataList = data.page.root;
            	 $(dataList).each(function(index,item){
            		 html += '<tr>';
            		 html += '<td>'+item.userId+'</td>';
            		 html += getTdHtml(item.username, 15);
            		 html += '<td>'+item.phone+'</td>';
            		 html += '<td>'+item.addTimeStr+'</td>';
            		 html += '<td>'+item.cardNum+'</td>';
            		 html += '<td>'+item.realName+'</td>';
            		 html += '<td>'+item.statusDesc+'</td>';
            		 if(item.status > 1){
            			 html += '<td><a class="a-blue" href="person/userRealVerifyDetail.action?userid='+item.userId+'&addTimeStr='+item.addTimeStr+'">详细</a></td>';
//            			 html += '<td><a class="a-disable" disabled="disabled" href="javascript:;">审核</a></td>';
            		 }else{
            			 html += '<td><a class="a-green" href="person/userRealVerify.action?userid='+item.userId+'&addTimeStr='+item.addTimeStr+'">审核</a></td>';
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

