/**
 * 个人设备告警
 */
	var param={};
	var userDeviceAlarmUrl = "userjson/device_userDeviceAlarm.action";
	goUserMenu("userDeviceAlarm");
	initExportBtn($("#exportBtn"),null,userDeviceAlarmUrl,null,"设备告警","searchDate");
	$(function(){
		function initBody(){
			param.currentPage=$("#currentPage").val();
			param.searchDate = $("#searchDate").val();
			var item = $("#userDeviceAlarmBody");
			$.ajax({
			      url:userDeviceAlarmUrl,// 跳转到 action  
			      data:param,  
			      type:'post',  
			      cache:false,  
			      dataType:'json', 
			      beforeSend:function(){$("#loading").removeClass("hide");},
			      success:function(data){
			    	  if(data.userLoginStatus){
			    		  checkLoginStatus(data,false);
			    		  return;
			    	  }
						item.empty();
						if(data.page.root.length > 0){
							$("#help-block").remove();
						$(data.page.root).each(function(index,value){
							str = "<tr>"
								+"<td>"+value.stationName+"</td>"
								+"<td>"+value.pileName+"</td>"
								+"<td><span title='"+value.address+"'>"+mySubString(value.address,13)+"</span></td>" 
								+"<td>"+value.described+"</td>"
								+"<td>"+value.eventTimeDesc+"</td>";
							item.append(str);//封装table
						});
						$("#pagingToolbar").show();
						}else{
							 $("#pagingToolbar").hide();
							 if(!$("#help-block").attr("id")){
								 item.parent().parent().append("<p id='help-block' class='help-block text-center' style='margin-top:10%;'>暂无记录</p>");
							 }
				    		
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
		$("#searchBtn").on("click",function(){
			$("#currentPage").val(1);
			initBody();
		});
		initOneBootStrapDate2($('#datepicker'));
		initPagingToolbar(initBody);
		initBody();
	})
	
