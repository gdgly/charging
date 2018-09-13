/**
 * 告警事件列表
 */
setBowHeight1();
$(function(){
	var searchEventsUrl="event_searchEventsByAjax.action";
	var param={};
	var str;
	var item;
	function initStationBody(){
		param={};
		param.currentPage=$("#currentPage").val();
		item =$("#alarmTable").find("tbody");
		$.ajax({  
		      url:searchEventsUrl,// 跳转到 action  
		      data:param,  
		      type:'post',  
		      cache:false,  
		      dataType:'json',  
		      beforeSend:function(){$("#loading").removeClass("hide");},
		      success:function(data){
		    	  if(data.userLoginStatus){
		    		  checkLoginStatus(data,true);
		    		  return;
		    	  }
					item.empty();
					if(data.page.root.length > 0){
					$(data.page.root).each(function(index,value){
						str = "<tr>"
							+"<td>"+value.stationName+"</td>"
							+"<td>"+value.pileName+"</td>" 
							+"<td>地址：<span title='"+value.address+"'>"+mySubString(value.address,13)+"</span></td>" 
							+"<td>事件：<span>"+value.described+"</span></td>" 
							+"<td><span style='color:red;'>"+value.levelDesc+"</span></td>" 
							+"<td>"+value.eventTimeDesc+"</td>" 
							+"</tr>"; 
						item.append(str);//封装tableBody

					});
	
					}else{
					     $("#pagingToolbar").hide();
						 $("#exportBtn").attr("disabled",true);
			    		 item.append("<p class='help-block text-center' style='margin-top:10%;'>暂无记录</p>"); 
					}
					var stationTotalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
					setPagingToolbarParams(data.page.totalProperty,stationTotalPage,$("#currentPage").val());
				},
		      complete:function(){
		    	  $("#loading").addClass("hide"); 
		      },
		      error : function() {  
		           showWarning("异常！");
		      }
		 });
	};
	initPagingToolbar(initStationBody);
	initStationBody();
	initExportBtn($("#exportBtn"),null,searchEventsUrl,null,"告警记录");
})

