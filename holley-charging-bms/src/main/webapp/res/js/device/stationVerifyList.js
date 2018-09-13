var param={};
$(document).ready(function(){
	initStationParams();
	queryStationList();
});

function initStationParams(){
	initStartEndDate($('#stationStartDateDiv'),$('#stationEndDateDiv'));
	initPagingToolbar(queryStationList);
	$("#stationQueryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryStationList();
	});
}

function queryStationList(){
	param={};
	param.startdate =  $.trim($("#stationStartDate").val());
	param.enddate =  $.trim($("#stationEndDate").val());
	param.stationname = $.trim($("#stationName").val());
	param.verifystatus = $('#stationVerifyStatus option:selected').val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	param.tm = new Date().getTime();
	
	var tbody = $("#stationTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'device/deviceVerify_queryStationList.action',
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
            		 html += '<td>'+item.stationName+'</td>';
            		 html += '<td>'+getNotNullData(item.realStationId)+'</td>';
            		 html += '<td>'+getNotNullData(item.updateTimeStr)+'</td>';
            		 html += '<td>'+getNotNullData(item.busTypeDesc)+'</td>';
            		 html += '<td>'+getNotNullData(item.requestTypeDesc)+'</td>';
            		 html += '<td>'+getNotNullData(item.validStatusDesc)+'</td>';
            		 html += '<td>'+getNotNullData(item.validTimeStr)+'</td>';
            		 html += '<td>'+getNotNullData(item.validRemark)+'</td>';
            		 if(item.validStatus > 2){
            			 html += '<td><a class="a-blue" href="device/stationVerifyDetail.action?id='+item.id+'">详细</a></td>';
//            			 html += '<td><a class="a-disable" disabled="disabled" href="javascript:;">审核</a></td>';
            		 }else{
            			 html += '<td><a class="a-green" href="device/stationVerify.action?id='+item.id+'">审核</a></td>';
            		 }
            		 
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
