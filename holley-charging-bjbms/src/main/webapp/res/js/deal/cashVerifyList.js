var param={};
$(document).ready(function(){
//	setTabelDivHeight();
	initParams();
	queryCashVerifyList();
});

function initParams(){
	initStartEndDate($('#startDateDiv'),$('#endDateDiv'));
	initPagingToolbar(queryCashVerifyList);
	//初始化查询按钮
	$("#queryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryCashVerifyList();
	});
	//初始化导出按钮
	initExportBtn($("#exportBtn"),$("#conditionFrom"),$("#fileName").val());
}

function queryCashVerifyList(){
	param={};
	param.startdate =  $.trim($("#startDate").val());
	param.enddate =  $.trim($("#endDate").val());
	param.userinfo = $.trim($("#userInfo").val());
	param.verifystatus = $('#verifyStatus option:selected').val();
	param.cashstatus = $('#cashStatus option:selected').val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	param.tm = new Date().getTime();
	
	var tbody = $("#cashTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'deal/cashVerify_queryCashList.action',
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
            		 html += '<td>'+getNotNullData(item.realName)+'</td>';
            		 html += '<td>'+item.phone+'</td>';
            		 html += '<td>'+item.userTypeDesc+'</td>';
            		 html += '<td>'+item.moneyDesc+'</td>';
            		 html += '<td>'+getNotNullData(item.cashWayDesc)+'</td>';
//            		 html += '<td>'+getNotNullData(item.accountInfo)+'</td>';
            		 html += '<td>'+getNotNullData(item.cashStatusDesc)+'</td>';
            		 html += getTdHtml(item.remark, 15);
            		 html += '<td>'+item.addTimeDesc+'</td>';
            		 html += '<td>'+item.validStatusDesc+'</td>';
            		 html += '<td>'+getNotNullData(item.validTimeDesc)+'</td>';
            		 html += getTdHtml(item.validRemark, 15);
            		 html += '<td>';
            		 if(item.validStatus > CASH_VERIFY_STATUS_VERIFYING){//已审核
            			 html += '<a class="a-blue" href="deal/cashVerifyDetail.action?id='+item.id+'&usertype='+item.userType+'">详细</a>';
            		 }else{//未审核
            			 html += '<a class="a-green" href="deal/cashVerify.action?id='+item.id+'&usertype='+item.userType+'">审核</a>';
            		 }
            		 html += '</td>';
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

