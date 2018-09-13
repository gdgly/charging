/**
 * 设备列表
 */
setBowHeight1();
var searchStationsUrl="device_searchStationsByAjax.action";
var searchPilesUrl="device_searchPilesByAjax.action";
var param={};
var str;
var team;
var item;

//初始化充电桩
function initPileBody(){
	param={};
	param.currentPage=$("#currentPage2").val();
	param.stationId = $("#stationId").val();
	param.searchKeyName = $("#searchKeyName").val().trim();
	item=$("#validPile").find("tbody");
	if(!param.stationId || param.stationId <= 0){
		showWarning("请选择充电点！！");
		return false;
	}
	$.ajax( {  
	      url:searchPilesUrl,// 跳转到 action  
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
	    	  if(data.message){
	    		showWarning(data.message);
	    		return false;
	    	  }
	  		item.empty();
			$(data.page.root).each(function(index,value){
				str = "<tr>"
					+"<td>"+value.pileName+"</td>"
					+"<td>桩编号：<span>"+value.pileCode+"</span></td>";
				team = parseInt(value.status);
				if(team == 1){
					str=str+"<td style='color:green;'>"+value.statusDesc+"</td>";
				}
				else if(team == 2){
					str=str+"<td style='color:red;'>"+value.statusDesc+"</td>";
				}
				else{
					str=str+"<td style='color:#ccc;'>"+value.statusDesc+"</td>";
				}
				str=str
					+"<td>"+value.pileTypeDesc+"</td>"
					+"<td>"+value.chaWayDesc+"</td>"
					+"<td>通讯协议：<span>"+value.comTypeDesc+"</span></td>"
					+"<td>通讯地址：<span title='"+value.comAddr+"'>"+mySubString(value.comAddr, 3)+"</span></td>"
					+"<td>支持预约：<span>"+value.isAppDesc+"</span></td>"
					+"<td>支付方式：<span>"+value.payWayDesc+"</span></td>";
				
				str=str +"<td>"
				+'<div class="dropdown text-right">'
				+'<button type="button" class="btn btn-default  btn-sm dropdown-toggle" id="dropdownMenu1"' 
				+'data-toggle="dropdown"><span style="color:#337ab7;">详情</span>'
				+'<span class="caret"></span>'
				+'</button>'
				+'<ul class="dropdown-menu" role="menu"' 
				+'aria-labelledby="dropdownMenu1">'
				+'<li role="presentation">'
				+'<a role="menuitem" tabindex="-1" href="deviceManager/editValidPile.action?validPileId='
				+value.id+"&backStationId="+value.stationId
				+'">修改</a>'
				+'</li>'
				+'<li role="presentation">'
				+'<a role="menuitem" tabindex="-1" href="deviceManager/showValidDetail.action?pileId='
				+value.id+"&backStationId="+value.stationId
				+'&detailType=detailPile">详细</a>'
				+'</li>'
				+'</ul>'
				+'</div>'
				+"</td>"
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
$(function(){
	$("#addStationBtn").on("click",function(){
		stationId = $(this).attr("stationId");
		if(stationId > 0){
			href("deviceManager/addValidStationPile.action?validStationId="+stationId);
		}else{
			href('deviceManager/addStation.action');
		}
	});
	/**
	 * 初始化搜索条件
	 */
	function initInput(){
		$("#stationId").val("");
		$("#searchKeyName").val("");
	}
	//	初始化充电点
	function initStationBody(){
		param={};
		param.currentPage=$("#currentPage").val();
		param.searchKeyName = $("#searchKeyName").val().trim();
		item=$("#validStation").find("tbody");
		$.ajax({
		      url:searchStationsUrl,// 跳转到 action  
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
						var totalPileNum = parseInt(value.fastNum)+parseInt(value.slowNum);
						if(!totalPileNum){
							totalPileNum = 0;
						}
						str = "<tr>"
								+"<td>"+value.stationName+"</td>"
								+"<td title='"+value.address+"'>地址：<span>"
								+mySubString(value.address,10)
								+"</span></td>"
								+"<td>开放日：<span>"+value.openDayDesc+"</span></td>"
								+"<td>开放时间：<span>"+value.openTimeDesc+"</span></td>";
						if(totalPileNum > 0){
							str=str	
								+"<td>"
								+totalPileNum+"个桩<a stationId='"+value.id+"' href='javascript:;' onclick='searchPileByStationId(this,initPileBody);'>>></a>"
								+"</td>";
						}else{
							str=str	
								+"<td>"
								+totalPileNum+"个桩<a stationId='"+value.id+"' href='javascript:;'>>></a>"
								+"</td>";
						}
						str=str	
								+"<td>评分："
								+"<img class='startImg' style='background-position:-"+(50-parseInt(value.score))+"px;background-image:url("+IMG_SRC+"res/img/bussiness/starBackImg.png);'"+" src="+IMG_SRC+"res/img/bussiness/starImg.gif"+">"
								+"</td>"
								+"<td>"
								+'<div class="dropdown text-right">'
								+'<button type="button" class="btn btn-default  btn-sm dropdown-toggle" id="dropdownMenu1"' 
								+'data-toggle="dropdown"><span style="color:#337ab7;">详情</span>'
								+'<span class="caret"></span>'
								+'</button>'
								+'<ul class="dropdown-menu" role="menu"' 
								+'aria-labelledby="dropdownMenu1">'
								+'<li role="presentation">'
								+'<a role="menuitem" tabindex="-1" href="deviceManager/editValidStation.action?validStationId='
								+value.id
								+'">修改</a>'
								+'</li>'
								+'<li role="presentation">'
								+'<a role="menuitem" tabindex="-1" href="deviceManager/addValidStationPile.action?validStationId='
								+value.id+"&isStation=1"
								+'">新增</a>'
								+'</li>'
								+'<li role="presentation">'
								+'<a role="menuitem" tabindex="-1" href="deviceManager/showValidDetail.action?stationId='
								+value.id
								+'&detailType=detailStation">详细</a>'
								+'</li>'
								+'</ul>'
								+'</div>'
								/*+"<a href='deviceManager/editValidStation.action?validStationId=" 
								+value.id
								+"'><span title='修改' style='color:#337ab7;' class='glyphicon glyphicon-pencil'></span></a>&nbsp;&nbsp;&nbsp;"
								+"<a href='deviceManager/addValidStationPile.action?validStationId="
								+value.id
								+"'><span title='添加' style='color:#337ab7;' class='glyphicon glyphicon-plus'></span></a>&nbsp;&nbsp;&nbsp;"
								+"<a href='deviceManager/editValidStation.action?validStationId=" 
								+value.id
								+"'><span title='详细' style='color:#337ab7;' class='glyphicon glyphicon-list'></span></a>"*/
								+"</td>"
								+"</tr>"; 
						item.append(str);//封装table
					});
					$("#pagingToolbar").show();
					}else{
						  $("#pagingToolbar").hide();
						 /* $("#searchKeyNameBtn").attr("disabled",true);
			    		  $("#exportBtn").attr("disabled",true);*/
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
	initPagingToolbar2(initPileBody);
	initPagingToolbar(initStationBody);
	initInput();
	if(backStationId > 0){
		backPileByStationId(backStationId,initPileBody);
	}else{
		initStationBody();
	}
	$("#comeBack").on("click",function(){
	//	href("deviceManager/searchValidDevice.action");
		if(searchKeyName){
			$("#searchKeyName").val(searchKeyName);
		}else{
			$("#searchKeyName").val("");
		}
		$("#addStationBtn").attr("stationId","0");
		//$("#searchKeyNameDiv").addClass("col-sm-offset-4");
		//$("#datetime").addClass("hide");
		$("#stationId").val("");
		$("#comeBack").addClass("hide");
		$("#pileDiv").addClass("hide");
		initStationBody();
		$("#stationDiv").removeClass("hide");
	});
	//stationAndPileBtn(initStationBody,initPileBody,initInput);
	publicSearchFun(initStationBody,initPileBody);
	initExportBtn($("#exportBtn"),searchPilesUrl,searchStationsUrl,"已有充电桩","已有充电点");
})

