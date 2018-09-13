/**
 * 设备列表
 */
setBowHeight1();

$(function(){
	var searchUnValidStationsUrl="device_searchUnValidStationsByAjax.action";
	var searchUnValidPilesByAjaxUrl="device_searchUnValidPilesByAjax.action";
	var param={};
	var str;
	var item;
	/**
	 * 初始化搜索条件
	 */
	function initInput(){
		//$("#datetime").find("input").val("");
		$("#searchKeyName").val("");
		$("#validStatus").val(0);
	}
	//初始化充电桩
	function initPileBody(){
		param={};
		param.currentPage=$("#currentPage2").val();
		param.searchPileName = $("#searchKeyName").val();
		param.validStatus = $("#validStatus").val();
		item= $("#unValidPileTable").find("tbody");
		$.ajax({  
		      url:searchUnValidPilesByAjaxUrl,// 跳转到 action  
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
						/* $("#searchKeyNameBtn").attr("disabled",false);
			    		 $("#exportBtn").attr("disabled",false);*/
					$(data.page.root).each(function(index,value){
						var totalPileNum = parseInt(value.fastNum)+parseInt(value.slowNum);
						var chaWay = parseInt(value.chaWay);//1.交流2.直流
						var pileType = parseInt(value.pileType);  //1.快充2.慢充3.超速
						var requestType = parseInt(value.requestType);//1.点桩新增2.单个装新增4.桩修改
						var validStatus = parseInt(value.validStatus);//1.待审核2.审核中3.审核通过4.审核失败
						str = "<tr>"
								+"<td>"+value.pileName+"</td>"
								+"<td>"+value.pileTypeDesc2+"</td>"
								+"<td>"+value.chaWayDesc2+"</td>"
								+"<td>通讯协议：<span>"+value.comTypeDesc2+"</span></td>"
								+"<td>通讯地址：<span title='"+value.comAddr+"'>"+mySubString(value.comAddr, 3)+"</span></td>"
								+"<td>支持预约：<span>"+value.isAppDesc2+"</span></td>"
								+"<td>支付方式：<span>"+value.payWayDesc2+"</span></td>"
								+"<td>"+value.requestTypeDesc2+"</td>";
						if(validStatus == 1){
							str=str+" <td style='color:"+"red;'>"+value.validStatusDesc2+"</td>";
						}else if(validStatus == 2){
							str=str+" <td style='color:"+"green;'>"+value.validStatusDesc2+"</td>";
						}else if(validStatus == 4){
							str=str+" <td style='color:"+"#ccc;'>"+value.validStatusDesc2+"</td>";
						}else{
							str=str+" <td style='color:"+"#ccc;'>"+value.validStatusDesc2+"</td>";
						}
						if(requestType == 1){
							if(validStatus == 1 || validStatus == 4){
								str=str+"<td>"
								+'<div class="dropdown text-right">'
								+'<button type="button" class="btn btn-default  btn-sm dropdown-toggle" id="dropdownMenu1"' 
								+'data-toggle="dropdown"><span style="color:#337ab7;">详情</span>'
								+'<span class="caret"></span>'
								+'</button>'
								+'<ul class="dropdown-menu" role="menu"' 
								+'aria-labelledby="dropdownMenu1">'
								+'<li role="presentation">'
								+'<a role="menuitem" tabindex="-1" href="deviceManager/editNewPile.action?type=1&newPileId='
								+value.id
								+'">修改</a>'
								+'</li>'
								+'<li role="presentation">'
								+'<a role="menuitem" tabindex="-1" href="deviceManager/showUnValidDetail.action?pileId='
								+value.id
								+'&detailType=detailPile">详细</a>'
								+'</li>'
								+'</ul>'
								+'</div>'
								+"</td>";
							}
							else{
								str=str+"<td>"
								+'<div class="dropdown text-right">'
								+'<button type="button" class="btn btn-default  btn-sm dropdown-toggle" id="dropdownMenu1"' 
								+'data-toggle="dropdown"><span style="color:#337ab7;">详情</span>'
								+'<span class="caret"></span>'
								+'</button>'
								+'<ul class="dropdown-menu" role="menu"' 
								+'aria-labelledby="dropdownMenu1">'
								+'<li role="presentation">'
								+'<a role="menuitem" tabindex="-1" href="deviceManager/showUnValidDetail.action?pileId='
								+value.id
								+'&detailType=detailPile">详细</a>'
								+'</li>'
								+'</ul>'
								+'</div>'
								+"</td>";
							}
						}
						else if(requestType == 2){
							if(validStatus == 1 || validStatus == 4){
								str=str	+"<td>"
								+'<div class="dropdown text-right">'
								+'<button type="button" class="btn btn-default  btn-sm dropdown-toggle" id="dropdownMenu1"' 
								+'data-toggle="dropdown"><span style="color:#337ab7;">详情</span>'
								+'<span class="caret"></span>'
								+'</button>'
								+'<ul class="dropdown-menu" role="menu"' 
								+'aria-labelledby="dropdownMenu1">'
								+'<li role="presentation">'
								+'<a role="menuitem" tabindex="-1" href="deviceManager/editNewPile.action?type=2&newPileId='
								+value.id
								+'">修改</a>'
								+'</li>'
								+'<li role="presentation">'
								+'<a role="menuitem" tabindex="-1" href="deviceManager/showUnValidDetail.action?pileId='
								+value.id
								+'&detailType=detailPile">详细</a>'
								+'</li>'
								+'</ul>'
								+'</div>'
		/*						+"<a href='deviceManager/editNewPile.action?type=2&newPileId="+value.id
								+"'><span title='修改' style='color:#337ab7;' class='glyphicon glyphicon-pencil'></span>"
								+"</a>"*/
								+"</td>";
							}
							else{
								str=str+"<td>"
								+'<div class="dropdown text-right">'
								+'<button type="button" class="btn btn-default  btn-sm dropdown-toggle" id="dropdownMenu1"' 
								+'data-toggle="dropdown"><span style="color:#337ab7;">详情</span>'
								+'<span class="caret"></span>'
								+'</button>'
								+'<ul class="dropdown-menu" role="menu"' 
								+'aria-labelledby="dropdownMenu1">'
								+'<li role="presentation">'
								+'<a role="menuitem" tabindex="-1" href="deviceManager/showUnValidDetail.action?pileId='
								+value.id
								+'&detailType=detailPile">详细</a>'
								+'</li>'
								+'</ul>'
								+'</div>'
								+"</td>";
							}
						}
						else{
							str=str+"<td>"
							+'<div class="dropdown text-right">'
							+'<button type="button" class="btn btn-default  btn-sm dropdown-toggle" id="dropdownMenu1"' 
							+'data-toggle="dropdown"><span style="color:#337ab7;">详情</span>'
							+'<span class="caret"></span>'
							+'</button>'
							+'<ul class="dropdown-menu" role="menu"' 
							+'aria-labelledby="dropdownMenu1">'
							+'<li role="presentation">'
							+'<a role="menuitem" tabindex="-1" href="deviceManager/showUnValidDetail.action?pileId='
							+value.id
							+'&detailType=detailPile">详细</a>'
							+'</li>'
							+'</ul>'
							+'</div>'
							+"</td>";
						}
						str=str+"</tr>";
						item.append(str);//封装table

					});
					$("#pagingToolbar2").show();
		      }else{
		    	  		  $("#pagingToolbar2").hide();
						  /*$("#searchKeyNameBtn").attr("disabled",true);
			    		  $("#exportBtn").attr("disabled",true);*/
			    		  item.append("<p class='help-block text-center' style='margin-top:10%;'>暂无记录</p>"); 
					}
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
		
	};
	//初始化充电点
	function initStationBody(){
		param={};
		param.currentPage=$("#currentPage").val();
		param.searchStationName = $("#searchKeyName").val();
		param.validStatus = $("#validStatus").val();
		item=$("#unValidStationTable").find("tbody");
		$.ajax({  
		      url:searchUnValidStationsUrl,// 跳转到 action  
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
						 /*$("#searchKeyNameBtn").attr("disabled",false);
			    		  $("#exportBtn").attr("disabled",false);*/
					$(data.page.root).each(function(index,value){
						var totalPileNum = parseInt(value.fastNum)+parseInt(value.slowNum);
						str = "<tr>"
							+"<td>"+value.stationName+"</td>"
							+"<td title='"+value.address+"'>地址：<span>"
							+mySubString(value.address,10)
							+"</span></td>"
							+"<td>开放日：<span>"+value.openDayDesc2+"</span></td>"
							+"<td>开放时间：<span>"+value.openTimeDesc2+"</span></td>"
							+"<td>"+value.requestTypeDesc2+"</td>";
						var requestType = parseInt(value.requestType);
						var validStatus = parseInt(value.validStatus);
						if(validStatus == 1){
							str=str+" <td style='color:"+"red;'>"+value.validStatusDesc2+"</td>";
						}else if(validStatus == 2){
							str=str+" <td style='color:"+"green;'>"+value.validStatusDesc2+"</td>";
						}else if(validStatus == 4){
							str=str+" <td style='color:"+"#ccc;'>"+value.validStatusDesc2+"</td>";
						}else{
							str=str+" <td style='color:"+"#ccc;'>"+value.validStatusDesc2+"</td>";
						}
						
						if(requestType == 1){
							if(validStatus == 1 || validStatus == 4){
								str = str 
								+"<td>"
								+'<div class="dropdown text-right">'
								+'<button type="button" class="btn btn-default  btn-sm dropdown-toggle" id="dropdownMenu1"' 
								+'data-toggle="dropdown"><span style="color:#337ab7;">详情</span>'
								+'<span class="caret"></span>'
								+'</button>'
								+'<ul class="dropdown-menu" role="menu"' 
								+'aria-labelledby="dropdownMenu1">'
								+'<li role="presentation">'
								+'<a role="menuitem" tabindex="-1" href="deviceManager/editNewStation.action?newStationId='
								+value.id
								+'">修改</a>'
								+'</li>'
								+'<li role="presentation">'
								+'<a role="menuitem" tabindex="-1" href="deviceManager/addNewStationPile.action?newStationId='
								+value.id
								+'">新增</a>'
								+'</li>'
								+'<li role="presentation">'
								+'<a role="menuitem" tabindex="-1" href="deviceManager/showUnValidDetail.action?stationId='
								+value.id
								+'&detailType=detailStation">详细</a>'
								+'</li>'
								+'</ul>'
								+'</div>'
								+"</td>";
							}
							else{
								str = str 
								+"<td>"
								+'<div class="dropdown text-right">'
								+'<button type="button" class="btn btn-default  btn-sm dropdown-toggle" id="dropdownMenu1"' 
								+'data-toggle="dropdown"><span style="color:#337ab7;">详情</span>'
								+'<span class="caret"></span>'
								+'</button>'
								+'<ul class="dropdown-menu" role="menu"' 
								+'aria-labelledby="dropdownMenu1">'
								+'<li role="presentation">'
								+'<a role="menuitem" tabindex="-1" href="deviceManager/showUnValidDetail.action?stationId='
								+value.id
								+'&detailType=detailStation">详细</a>'
								+'</li>'
								+'</ul>'
								+'</div>'
								+"</td>";
							}

						}
						else if(requestType == 3){
							str=str
							+"<td>"
							+'<div class="dropdown text-right">'
							+'<button type="button" class="btn btn-default  btn-sm dropdown-toggle" id="dropdownMenu1"' 
							+'data-toggle="dropdown"><span style="color:#337ab7;">详情</span>'
							+'<span class="caret"></span>'
							+'</button>'
							+'<ul class="dropdown-menu" role="menu"' 
							+'aria-labelledby="dropdownMenu1">'
							+'<li role="presentation">'
							+'<a role="menuitem" tabindex="-1" href="deviceManager/showUnValidDetail.action?stationId='
							+value.id
							+'&detailType=detailStation">详细</a>'
							+'</li>'
							+'</ul>'
							+'</div>'
							+"</td>";

						}

						str = str
						+"</tr>"; 
						item.append(str);//封装table

					});
					 $("#pagingToolbar").show();
		      }else{
		    	  		  $("#pagingToolbar").hide();
						/*  $("#searchKeyNameBtn").attr("disabled",true);
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
	initStationBody();
	//initOneBootStrapDate2($("#datetime"));
	initInput();
	stationAndPileBtn(initStationBody,initPileBody,initInput);
	$("#exportBtn").on("click",function(){
		searchType = $("#searchType").val();
		opt={};
		opt.onOk = function(){
			if('pile' == searchType){
				href(searchUnValidPilesByAjaxUrl+"?isExport=1"+"&fileName="+"待审核充电桩");
			}else if('station' == searchType){
				href(searchUnValidStationsUrl+"?isExport=1"+"&fileName="+"待审核充电点");
			}else{
				showWarning("导出失败！！");
			}
		}
		showConfirm("确定要导出Excel？",opt);
		
		
	});
	$("#searchKeyNameBtn").on("click",function(){
		$("#currentPage").val("1");
		$("#currentPage2").val("1");
		searchType = $("#searchType").val();
		/*if(!$("#searchKeyName").val().trim()){
			showWarning("请输入搜索关键字！！");
			return false;
		}*/
		if('pile' == searchType){
			initPileBody();
		}
		else if('station' == searchType){
			initStationBody();
		}
	});

})

