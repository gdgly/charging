/**
 * 预约列表
 */
setBowHeight1();
//异步分页查询充电桩预约记录
	var searchAppointmentsUrl="appointment_searchAppointmentsByAjax.action";
	//异步分页查询充电点预约记录
	var searchStationAppointmentsUrl = "appointment_searchStationAppointmentsByAjax.action";
	var param={};
	var str;
	var team;
	var limit;
	var item;
//初始化充电桩Table start
	function initPileBody(){
		item = $("#pileAppTable").find("tbody");
		param={};
		param.currentPage=$("#currentPage2").val();
		param.stationId = $("#stationId").val();
		param.searchKeyName = $("#searchKeyName").val().trim();
		param.datetimeInput=$("#datetimeInput").val().trim();
		$.ajax({  
		      url:searchAppointmentsUrl,// 跳转到 action  
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
					$(data.page.root).each(function(index,value){
						str = "<tr>"
							+"<td>"+value.pileName+"</td>"
							+"<td>用户名：<span>"+value.username+"</span></td>"
							+"<td>手机号码：<span>"
							+value.phone
							+"</span></td>"
							+"<td>"+value.appStatusDesc+"</td>";
						
						str=str
							+"<td>"
							+"<div style='width:85px;'><span style='color:green;font-size:17px;'>+"
							+value.appFeeDesc
							+"</span>&nbsp;&nbsp;<img style='float:right;padding-top:5px;' src='"
							+IMG_SRC
							+"res/img/bussiness/detail.png'"
							+" appLen="+ value.appLen
							+" onmouseleave='closeDetail(this);' onmouseenter='showDetail(this);' name='detail'/>"
							+"</div>"
							+"</td>";
						temp = value.isBill;
						if(temp == 1){
							str = str+"<td style='color:green;'>"+value.isBillDesc+"</td>";
						}else{
							str = str+"<td style='color:coral;'>"+value.isBillDesc+"</td>";
						}
						str = str
							+"<td class='text-right'>"+value.addTimeDesc+"</td>"
							+"</tr>";
						item.append(str);
					});
					var stationTotalPage2 = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
					setPagingToolbar2Params(data.page.totalProperty,stationTotalPage2,$("#currentPage2").val());
				},  
		      complete:function(){
		    	  $("#loading").addClass("hide"); 
		      },
		      error : function() {  
		           showWarning("异常！");
		      }  
		 });
	}
	//初始化充电桩Table end

//显示详细信息
function showDetail(obj){
	var str = "<div class='form-group' style='width:160px;'>"
		+"<label class='col-sm-8 control-label'>预约时长：</label>"
		+"<div class='col-sm-4'>"
		+"<p id='appLen' style='margin-left:-35px;'>"
		+$(obj).attr("appLen")
		+"Min</p>"
		+"</div>";
	$(obj).popover({ 
		html : true,
		title: function() {
			return "<p class='text-center' style='margin-bottom:1px;'>费用明细</p>";
		},
		content: function() {
			return str;
		}
	});
	$(obj).popover("show");
}
//关闭详细信息
function closeDetail(obj){
	$(obj).popover("hide");
}
$(function(){
	
	//初始化搜索条件函数
	function initInput(){
		$("#datetime").find("input").val("");
	}
	

//	初始化充电点Table函数 start
	function initStationBody(){
		param={};
		param.currentPage=$("#currentPage").val();
		param.searchKeyName = $("#searchKeyName").val().trim();
		item = $("#stationAppTable").find("tbody");
		$.ajax({  
		      url:searchStationAppointmentsUrl,// 跳转到 action  
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
							+"<td title='"+value.address+"'>"
							+mySubString(value.address,13)
							+"</td>"
							+"<td>"+value.pileNum+"个桩</td>";
						if(value.appNum > 0){
							str = str
									+"<td>预约："+value.appNum+"次<a stationId='"+value.stationId+"' href='javascript:' onclick='searchPileByStationId(this,initPileBody);'>>></a></td>";
							
						}else{
							str = str
									+"<td>预约："+value.appNum+"次<a stationId='"+value.stationId+"' href='javascript:'>>></a></td>";
						}
							str = str
									+"<td>评分："
									+"<img class='startImg' style='background-position:-"+(50-parseInt(value.score))+"px;background-image:url("+IMG_SRC+"res/img/bussiness/starBackImg.png);'"+" src="+IMG_SRC+"res/img/bussiness/starImg.gif"+">"
									+"</td>"
							        +"<td><span style='color:green;font-size:17px;'>+"+value.appFeeDesc+"</span></td>"
							        +"</tr>"; 
						item.append(str);//封装table
					});
					 $("#pagingToolbar").show();
					}else{
						 $("#pagingToolbar").hide();
					/*	 $("#exportBtn").attr("disabled",true);
						 $("#searchKeyNameBtn").attr("disabled",true);*/
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
	//初始化充电点Table函数 end
	initPagingToolbar2(initPileBody);
	initPagingToolbar(initStationBody);
	initStationBody();
	initOneBootStrapDate2($("#datetime"));
	initInput();
	//stationAndPileBtn(initStationBody,initPileBody,initInput);
	publicSearchFun(initStationBody,initPileBody);
	initExportBtn($("#exportBtn"),searchAppointmentsUrl,searchStationAppointmentsUrl,"设备预约记录","设备预约统计","searchKeyName","datetimeInput");
})

