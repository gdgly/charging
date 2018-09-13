/**
 * 设备充电统计列表
 */
	var userDevicePileChargeUrl="userjson/transaction_userDevicePileCharge.action";
	var userDeviceStationChargeUrl = "userjson/transaction_userDeviceStationCharge.action";
	var param2={};
	var team2;
	var item2;
	var str2;
	initExportBtn($("#exportBtn2"),null,userDeviceStationChargeUrl,null,"设备充电统计");
	$("#exportBtn3").on("click",function(){
		tempStationId = $("#stationId").val();
		isAppOrCha = $("#isAppOrCha").val();
		opt={};
		opt.onOk = function(){
			if("cha" == isAppOrCha && tempStationId > 0){
				temp = userDevicePileChargeUrl+"?isExport=1&stationId="+tempStationId+"&fileName="+"设备充电记录";
				if(!isEmpty($("#searchName3").val())){
					temp = temp + "&searchName="+$("#searchName3").val();
				}
				href(temp);
			}else if("app" == isAppOrCha && tempStationId > 0){
				temp = userDevicePileAppointmentUrl+"?isExport=1&stationId="+tempStationId+"&fileName="+"设备预约记录";
				if(!isEmpty($("#searchName3").val())){
					temp = temp + "&searchName="+$("#searchName3").val();
				}
				href(temp);
			}else{
				showWarning("导出失败！！");
			}
		}
		showConfirm("确定要导出Excel？",opt);
	})
	function searchPileCharge2(obj){
		stationId = $(obj).attr("stationId");
		stationName = $(obj).attr("stationName");
		if(stationId && stationId > 0){
			$("#stationId").val(stationId);
			$("#stationName").text(stationName);
			$("#searchName3").val("");
			$("#currentPage3").val("1");
			$("#isAppOrCha").val("cha");
			$("#userDevicePileTable").removeClass("hide");
			$("#userDeviceChargeTable").addClass("hide");
			$("#userDeviceAppointmentTable").addClass("hide");
			initPileBody2();
		}
	}
//初始化充电桩start
	function initPileBody2(){
		var item2 = $("#userDevicePileBody");
		param2={};
		param2.currentPage=$("#currentPage3").val();
		param2.stationId = $("#stationId").val();
		param2.searchName = $("#searchName3").val().trim();
		//param2.searchTime=$("#datetimeInput").val().trim();
		$.ajax({  
		      url:userDevicePileChargeUrl,// 跳转到 action  
		      data:param2,  
		      type:'post',  
		      cache:false,  
		      dataType:'json',  
		      beforeSend:function(){$("#loading").removeClass("hide");},
		      success:function(data){
		    	  if(data.userLoginStatus){
		    		  checkLoginStatus(data,false);
		    		  return;
		    	  }
					//$("#chaCurrentPage").val(data.page.startRow/(data.page.endRow-data.page.startRow)+1);
					item2.empty();
					$(data.page.root).each(function(index,value){
						str2 = "<tr>"
							+"<td>"+value.pileName+"</td>"
							+"<td><span>"+value.username+"</span></td>"
							+"<td><span>"+value.phone+"</span></td>"
							+"<td>"+value.payStatusDesc+"</td>";
						str2 = str2+"<td>"
						+"<div style='width:85px;'><span style='color:green;font-size:17px;'>+"
						+value.totalFeeDesc
						+"</span>&nbsp;&nbsp;<img style='float:right;padding-top:5px;' src='"
						+IMG_SRC
						+"res/img/bussiness/detail.png'"
						+" chaFee="+value.chaFeeDesc
						+" serviceFee="+value.serviceFeeDesc
						+" parkFee="+value.parkFeeDesc
						+" onmouseleave='closeDetail2(this);' onmouseenter='showDetail2(this);' name='detail'/>"
						+"</div>"
						+"</td>";
						temp = value.isBill;
						if(temp == 1){
							str2 = str2+"<td style='color:green;'>"+value.isBillDesc+"</td>";
						}else{
							str2 = str2+"<td style='color:coral;'>"+value.isBillDesc+"</td>";
						}
						str2 = str2
								+"<td class='text-right'>"+value.updateTimeDesc+"</td>"
						 		+"</tr>";
						item2.append(str2);
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
	//初始化充电桩end
//显示详细信息
function showDetail2(obj){
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
function closeDetail2(obj){
	$(obj).popover("hide");
}


$(function(){
	function initInput2(){
		$("#searchName2").val("");
	}
	
	//初始化充电点start
	function initStationBody2(){
		var item2 = $("#userDeviceChargeBody");
		param2={};
		param2.currentPage = $("#currentPage2").val();
		param2.searchName = $("#searchName2").val().trim();
		$.ajax({
		      url:userDeviceStationChargeUrl,// 跳转到 action  
		      data:param2,  
		      type:'post',  
		      cache:false,  
		      dataType:'json',  
		      beforeSend:function(){$("#loading").removeClass("hide");},
		      success:function(data){
		    	  if(data.userLoginStatus){
		    		  checkLoginStatus(data,false);
		    		  return;
		    	  }
					item2.empty();
					if(data.page.root.length > 0){
						$("#help-block2").remove();
					$(data.page.root).each(function(index,value){
						var serviceFee = getDefaultData(parseFloat(value.serviceFee),0);
						var parkFee	= getDefaultData(parseFloat(value.parkFee),0);
						var chaFee = getDefaultData(parseFloat(value.chaFee),0);
						str2 = "<tr>"
							+"<td>"+value.stationName+"</td>"
							+"<td title='"+value.address+"'>"
							+mySubString(value.address,13)
							+"</td>"
							+"<td>"+value.pileNum+"</td>";
						if(value.chaNum > 0){
							str2 = str2
									+"<td>"+value.chaNum+"<a stationId='"+value.stationId+"' stationName='"+value.stationName+"' href='javascript:' onclick='searchPileCharge2(this);'> >></a></td>";
						}else{
							str2 = str2
							+"<td>"+value.chaNum+"<a stationId='"+value.stationId+"' href='javascript:'> >></a></td>";
						}
						str2 = str2 
							+"<td>"
						 +"<div style='width:85px;'><span style='color:green;font-size:17px;'>+"
						 +value.totalFeeDesc
						 +"</span>&nbsp;&nbsp;<img style='float:right;padding-top:5px;' src='"
						 +IMG_SRC
						 +"res/img/bussiness/detail.png'"
						 +" chaFee="+chaFee
						 +" serviceFee="+serviceFee
						 +" parkFee="+parkFee
						 +" onmouseleave='closeDetail2(this);' onmouseenter='showDetail2(this);' name='detail'/>"
						 +"</div>"
						 +"</td>"
						 +"<td>"
						 +"<img class='startImg' style='background-position:-"+(50-parseInt(value.score))+"px;background-image:url("+IMG_SRC+"res/img/bussiness/starBackImg.png);'"+" src="+IMG_SRC+"res/img/bussiness/starImg.gif"+">"
						 +"</td>";
						 +"</tr>"; 
						item2.append(str2);//封装table
					});	
					$("#pagingToolbar2").show();
					}else{
						$("#pagingToolbar2").hide();
						if(!$("#help-block2").attr("id")){
							 item2.parent().parent().append("<p id='help-block2' class='help-block text-center' style='margin-top:5%;'>暂无记录</p>");
						 }
					}
					var stationTotalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
					setPagingToolbar2Params(data.page.totalProperty,stationTotalPage,$("#currentPage2").val());
				},  
		      complete:function(){
		    	  $("#loading").addClass("hide"); 
		      },
		      error : function() {
		           showWarning("异常！");
		      }  
		 });
	};
$("#searchBtn2").on("click",function(){
	initStationBody2();
});
	initInput2();//初始化搜索条件
	initPagingToolbar2(initStationBody2);
	//initPagingToolbar2(initPileBody);
	initStationBody2();//刷新页面加载
	//publicSearchFun(initStationBody,initPileBody);
	//initOneBootStrapDate2($("#datetime"));
	//initExportBtn($("#exportBtn"),searchChargesUrl,searchStationChargesUrl);
	//stationAndPileBtn(initStationBody,initPileBody,initInput);
	//初始化数据end
})

