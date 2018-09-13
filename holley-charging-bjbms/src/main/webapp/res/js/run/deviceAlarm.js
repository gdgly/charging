/**
 * 设备告警信息
 */
var param={};
var temp;
var queryDeviceAlarmUrl = "run/monitor_queryDeviceAlarm.action";

$(document).ready(function(){
	initParams();
	queryDevicelAlarm();
});


function initParams(){
	initStartEndDate($("#startTimeDate"),$("#endTimeDate"));
	$("#queryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryDevicelAlarm();
	});
	initPagingToolbar(queryDevicelAlarm);
	bindKey13([$("#keyword")],$("#queryBtn"));//回车自动搜索
}


function queryDevicelAlarm(){
	param={};
	param.startTime = $.trim($("#startTime").val());
	param.endTime = $.trim($("#endTime").val());
	param.pageindex = $.trim($("#currentPage").val());
	param.keyword = $.trim($("#keyword").val());
	var tbody = $("#deviceAlarmTbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:queryDeviceAlarmUrl,
		data:param,
		dataType:'json',
        cache: false,
        beforeSend:function(){$("#loading").removeClass("hide");},
        complete:function(){
	    	  $("#loading").addClass("hide"); 
	      },
        success: function(data){
        	if(data.page){
        		 	tbody.empty();
        		 	 if(data.page.root.length > 0){
        			 $(data.page.root).each(function(index,item){
        				 html += '<tr>';
        				 html += '<td>'+getNotNullData(item.stationName)+'</td>';
        				 html += '<td>'+getNotNullData(item.pileName)+'</td>';
        				 html += '<td>'+getNotNullData(item.address)+'</td>';
        				 html += '<td>'+getNotNullData(item.described)+'</td>';
        				 html += '<td>'+getNotNullData(item.eventTimeDesc)+'</td>';
        				 html += '</tr>';
        			 });
        		 	 $("#noListMsg").remove();
        			 tbody.html(html);
        			 var currentPage = data.page.startRow/(data.page.endRow-data.page.startRow)+1;
                	 var totalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
                	 	 setPagingToolbarParams(data.page.totalProperty, totalPage,currentPage);
        		 	 }else{
            			 if(!$("#noListMsg").attr("id")){
             				$("#userTableDiv").append('<p id="noListMsg" class="text-center" style="margin-top: 50px;">暂告警信息</p>'); 
             			 }
             		 }
				
        	}else{
        		showMsg(data.message, data.errormsg);
        	}
         },
         error:function(){
        	 showInfo("系统异常！！");
         }
     });
}
