/**
 * 设备充电统计列表
 */
	var userDevicePileChargeUrl="userjson/transaction_userDevicePileCharge.action";
	var userDeviceStationChargeUrl = "userjson/transaction_userDeviceStationCharge.action";
	var param={};
	var team;
	var item;
	var str;
//初始化充电桩start
	function initPileBody(){
		item = $("#pileChaTable").find("tbody");
		param={};
		param.currentPage=$("#currentPage2").val();
		param.stationId = $("#stationId").val();
		param.searchKeyName = $("#searchKeyName").val().trim();
		param.searchTime=$("#datetimeInput").val().trim();
		$.ajax({  
		      url:userDevicePileChargeUrl,// 跳转到 action  
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
					$("#chaCurrentPage").val(data.page.startRow/(data.page.endRow-data.page.startRow)+1);
					item.empty();
					$(data.page.root).each(function(index,value){
						str = "<tr>"
							+"<td>"+value.pileName+"</td>"
							+"<td>用户名：<span>"+value.username+"</span></td>"
							+"<td>手机号码：<span>"+value.phone+"</span></td>"
							+"<td>"+value.payStatusDesc+"</td>";
						str = str+"<td>"
						+"<div style='width:85px;'><span style='color:green;font-size:17px;'>+"
						+value.totalFeeDesc
						+"</span>&nbsp;&nbsp;<img style='float:right;padding-top:5px;' src='"
						+IMG_SRC
						+"res/img/bussiness/detail.png'"
						+" chaFee="+value.chaFeeDesc
						+" serviceFee="+value.serviceFeeDesc
						+" parkFee="+value.parkFeeDesc
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
								+"<td class='text-right'>"+value.updateTimeDesc+"</td>"
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
	//初始化充电桩end
//显示详细信息
function showDetail(obj){
	var html1 = "<div class='form-group' style='width:160px;'>"
		+"<label for='chaFee' class='col-sm-8 control-label'>充电费用：</label>"
		+"<div class='col-sm-4'>"
		+"<p id='chaFee' style='margin-left:-35px;'>"
		+parseFloat($(obj).attr("chaFee")).toFixed(2)
		+"</p>"
		+"</div>";
	var html2 = "<div class='form-group' style='width:160px;'>"
		+"<label for='serviceFee' class='col-sm-8 control-label'>服务费用：</label>"
		+"<div class='col-sm-4'>"
		+"<p id='serviceFee' style='margin-left:-35px;'>"
		+parseFloat($(obj).attr("serviceFee")).toFixed(2)
		+"</p>"
		+"</div>";
	var html3 = "<div class='form-group' style='width:160px;'>"
		+"<label for='parkFee' class='col-sm-8 control-label'>停车费用：</label>"
		+"<div class='col-sm-4'>"
		+"<p id='parkFee' style='margin-left:-35px;'>"
		+parseFloat($(obj).attr("parkFee")).toFixed(2)
		+"</p>"
		+"</div>";
	$(obj).popover({
		html : true,
		title: function() {
			return "<p class='text-center' style='margin-bottom:1px;'>费用明细</p>";
		},
		content: function() {
			return html1+html2+html3;
		}
	});
	$(obj).popover("show");
}
//关闭详细信息
function closeDetail(obj){
	$(obj).popover("hide");
}


$(function(){
	function initInput(){
		$("#searchName").val("");
	}
	
	//初始化充电点start
	function initStationBody(){
		item = $("#userDeviceChargeBody");
		param={};
		param.currentPage = $("#currentPage").val();
		param.searchName = $("#searchName").val().trim();
		$.ajax({
		      url:userDeviceStationChargeUrl,// 跳转到 action  
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
						var serviceFee = getDefaultData(parseFloat(value.serviceFee),0);
						var parkFee	= getDefaultData(parseFloat(value.parkFee),0);
						var chaFee = getDefaultData(parseFloat(value.chaFee),0);
						str = "<tr>"
							+"<td>"+value.stationName+"</td>"
							+"<td title='"+value.address+"'>"
							+mySubString(value.address,13)
							+"</td>"
							+"<td>"+value.pileNum+"</td>";
						if(value.chaNum > 0){
							str = str
									+"<td>充电："+value.chaNum+"<a stationId='"+value.stationId+"' href='javascript:' onclick='searchPileByStationId(this,initPileBody);'>>></a></td>";
						}else{
							str = str
							+"<td>"+value.chaNum+"<a stationId='"+value.stationId+"' href='javascript:'>>></a></td>";
						}
						str = str 
							+"<td>"
						 +"<div style='width:85px;'><span style='color:green;font-size:17px;'>+"
						 +value.totalFeeDesc
						 +"</span>&nbsp;&nbsp;<img style='float:right;padding-top:5px;' src='"
						 +IMG_SRC
						 +"res/img/bussiness/detail.png'"
						 +" chaFee="+chaFee
						 +" serviceFee="+serviceFee
						 +" parkFee="+parkFee
						 +" onmouseleave='closeDetail(this);' onmouseenter='showDetail(this);' name='detail'/>"
						 +"</div>"
						 +"</td>"
						 +"<td>"
						 +"<img class='startImg' style='background-position:-"+(50-parseInt(value.score))+"px;background-image:url("+IMG_SRC+"res/img/bussiness/starBackImg.png);'"+" src="+IMG_SRC+"res/img/bussiness/starImg.gif"+">"
						 +"</td>";
						 +"</tr>"; 
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
	initStationBody();
});
	initInput();//初始化搜索条件
	initPagingToolbar(initStationBody);
	//initPagingToolbar2(initPileBody);
	initStationBody();//刷新页面加载
	//publicSearchFun(initStationBody,initPileBody);
	//initOneBootStrapDate2($("#datetime"));
	//initExportBtn($("#exportBtn"),searchChargesUrl,searchStationChargesUrl);
	//stationAndPileBtn(initStationBody,initPileBody,initInput);
	//初始化数据end
})

