var param={};
$(document).ready(function(){
	initParams();
	queryAppointmentList();
});

function initParams(){
	initStartEndDate($('#startDateDiv'),$('#endDateDiv'));
	initPagingToolbar(queryAppointmentList);
	//初始化预约记录列表查询按钮
	$("#queryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryAppointmentList();
	});
	//初始化预约记录列表导出按钮
	initExportBtn($("#exportBtn"),$("#conditionFrom"),$("#fileName").val());
}

function queryAppointmentList(){
	param={};
	param.startdate =  $.trim($("#startDate").val());
	param.enddate =  $.trim($("#endDate").val());
	param.searchappno = $.trim($("#searchAppno").val());
	param.searchuser = $.trim($("#searchUser").val());
	param.searchstation = $.trim($("#searchStation").val());
	param.appstatus = $('#appStatus option:selected').val();
	param.paystatus = $('#payStatus option:selected').val();
	param.billstatus = $('#billStatus option:selected').val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	param.tm = new Date().getTime();
	
	var tbody = $("#appTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'deal/appointment_queryList.action',
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
            		 html += '<td>'+item.appNo+'</td>';
            		 html += '<td>'+item.phone+'</td>';
            		 html += '<td>'+item.stationPileName+'</td>';
            		 html += '<td>'+item.startTimeDesc+'</td>';
            		 html += '<td class="text-right">'+item.appLen+'</td>';
            		 html += '<td class="text-right">'+item.appFeeDesc+'</td>';
            		 html += '<td>'+item.payStatusDesc+'</td>';
            		 html += '<td>'+item.appStatusDesc+'</td>';	
            		 html += '<td>'+item.isBillDesc+'</td>';
            		 html += '<td>'+item.addTimeDesc+'</td>';
            		 html += '<td>'
            		 html += '<a href="deal/appointmentDetailInit.action?appointmentid='+item.id+'">详细</a>';
            		 html += '</td>'
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



