/**
 * 设备预约统计列表
 */
	//异步分页查询充电桩预约记录
	var userDevicePileAppointmentUrl="userjson/transaction_userDevicePileAppointment.action";
	//异步分页查询充电点预约记录
	var userDeviceStationAppointmentUrl = "userjson/transaction_userDeviceStationAppointment.action";
	var param={};
	var str;
	var team;
	var limit;
	var item;
	initExportBtn($("#exportBtn"),null,userDeviceStationAppointmentUrl,null,"设备预约统计");
	//初始化充电桩Table start
	function initPileBody(){
		var item = $("#userDevicePileBody");
		param={};
		param.currentPage=$("#currentPage3").val();
		param.stationId = $("#stationId").val();
		param.searchName = $("#searchName3").val().trim();
		//param.searchTime=$("#datetimeInput").val().trim();
		$.ajax({  
		      url:userDevicePileAppointmentUrl,// 跳转到 action  
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
					$(data.page.root).each(function(index,value){
						str = "<tr>"
							+"<td>"+value.pileName+"</td>"
							+"<td><span>"+value.username+"</span></td>"
							+"<td><span>"
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
					stationTotalPage3 = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
					setPagingToolbar3Params(data.page.totalProperty,stationTotalPage3,$("#currentPage3").val());
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
		+"<p id='appLen' class='text-center'>"
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
function searchPileAppointment(obj){
	stationId = $(obj).attr("stationId");
	stationName = $(obj).attr("stationName");
	if(stationId && stationId > 0){
		$("#stationId").val(stationId);
		$("#stationName").text(stationName);
		$("#searchName3").val("");
		$("#currentPage3").val("1");
		$("#isAppOrCha").val("app");
		$("#userDevicePileTable").removeClass("hide");
		$("#userDeviceChargeTable").addClass("hide");
		$("#userDeviceAppointmentTable").addClass("hide");
		initPileBody();
	}
}
$(function(){
	
	//初始化搜索条件函数
	function initInput(){
		$("#searchName").val("");
	}
	

//	初始化充电点Table函数 start
	function initStationBody(){
		param={};
		param.currentPage=$("#currentPage").val();
		param.searchName = $("#searchName").val().trim();
		var item = $("#userDeviceAppointmentBody");
		$.ajax({  
		      url:userDeviceStationAppointmentUrl,// 跳转到 action  
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
							+"<td title='"+value.address+"'>"
							+mySubString(value.address,13)
							+"</td>"
							+"<td>"+value.pileNum+"</td>";
						if(value.appNum > 0){
							str = str
									+"<td>"+value.appNum+"<a stationId='"+value.stationId+"' stationName='"+value.stationName+"' href='javascript:' onclick='searchPileAppointment(this);'> >></a></td>";
						}else{
							str = str
									+"<td>"+value.appNum+"<a stationId='"+value.stationId+"' href='javascript:'> >></a></td>";
						}
							str = str
							        +"<td><span style='color:green;font-size:17px;'>+"+value.appFeeDesc+"</span></td>"
							        +"<td>"
									+"<img class='startImg' style='background-position:-"+(50-parseInt(value.score))+"px;background-image:url("+IMG_SRC+"res/img/bussiness/starBackImg.png);'"+" src="+IMG_SRC+"res/img/bussiness/starImg.gif"+">"
									+"</td>"
							        +"</tr>"; 
						item.append(str);//封装table
					});
					 $("#pagingToolbar").show();
					}else{
						 $("#pagingToolbar").hide();
						 if(!$("#help-block").attr("id")){
							 item.parent().parent().append("<p id='help-block' class='help-block text-center' style='margin-top:5%;'>暂无记录</p>");
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
		initStationBody();
	});
	//初始化充电点Table函数 end
	//initPagingToolbar2(initPileBody);
	initInput();
	initPagingToolbar(initStationBody);
	initStationBody();
	//initOneBootStrapDate2($("#datetime"));
	//initInput();
	//stationAndPileBtn(initStationBody,initPileBody,initInput);
	//publicSearchFun(initStationBody,initPileBody);
	//initExportBtn($("#exportBtn"),searchAppointmentsUrl,searchStationAppointmentsUrl);
})

