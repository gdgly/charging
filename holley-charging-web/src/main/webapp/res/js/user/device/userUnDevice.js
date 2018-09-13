	var searchUnValidStationsUrl="device_searchUnValidStationsByAjax.action";
	var searchUnValidPilesByAjaxUrl="device_searchUnValidPilesByAjax.action";
	//初始化充电桩
	function initPileBody(){
		param={};
		param.currentPage=$("#currentPage2").val();
		param.searchPileName = $("#searchName2").val();
		param.validStatus = $("#validStatus").val();
		param.limit = 5;
		var item= $("#userUnValidPileBody");
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
						$("#help-block2").remove();
					$(data.page.root).each(function(index,value){
						var chaWay = parseInt(value.chaWay);//1.交流2.直流
						var pileType = parseInt(value.pileType);  //1.快充2.慢充3.超速
						var requestType = parseInt(value.requestType);//1.点桩新增2.单个装新增4.桩修改
						var validStatus = parseInt(value.validStatus);//1.待审核2.审核中3.审核通过4.审核失败
						str = "<tr>"
								+"<td>"+value.pileName+"</td>"
								+"<td>"+value.pileCode+"</td>"
								+"<td>"+value.pileTypeDesc2+"</td>"
								+"<td>"+value.chaWayDesc2+"</td>"
								+"<td>"+value.comTypeDesc2+"</td>"
								+"<td><span title='"+value.comAddr+"'>"+mySubString(value.comAddr, 3)+"</span></td>"
								+"<td>"+value.isAppDesc2+"</td>"
								+"<td>"+value.payWayDesc2+"</td>"
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
								+'<div class="dropdown">'
								+'<button type="button" class="btn btn-default  btn-sm dropdown-toggle" id="dropdownMenu1"' 
								+'data-toggle="dropdown"><span style="color:#337ab7;">详情</span>'
								+'<span class="caret"></span>'
								+'</button>'
								+'<ul class="dropdown-menu" role="menu"' 
								+'aria-labelledby="dropdownMenu1">'
								+'<li role="presentation">'
								+'<a role="menuitem" tabindex="-1" href="userDevice/editNewPile.action?type=1&newPileId='
								+value.id
								+'">修改</a>'
								+'</li>'
								+'<li role="presentation">'
								+'<a role="menuitem" tabindex="-1" href="userDevice/showUserUnVlaidDeviceDetail.action?pileId='
								+value.id
								+'&detailType=detailPile">详细</a>'
								+'</li>'
								+'</ul>'
								+'</div>'
								+"</td>";
							}
							else{
								str=str+"<td>"
								+'<div class="dropdown">'
								+'<button type="button" class="btn btn-default  btn-sm dropdown-toggle" id="dropdownMenu1"' 
								+'data-toggle="dropdown"><span style="color:#337ab7;">详情</span>'
								+'<span class="caret"></span>'
								+'</button>'
								+'<ul class="dropdown-menu" role="menu"' 
								+'aria-labelledby="dropdownMenu1">'
								+'<li role="presentation">'
								+'<a role="menuitem" tabindex="-1" href="userDevice/showUserUnVlaidDeviceDetail.action?pileId='
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
								+'<div class="dropdown">'
								+'<button type="button" class="btn btn-default  btn-sm dropdown-toggle" id="dropdownMenu1"' 
								+'data-toggle="dropdown"><span style="color:#337ab7;">详情</span>'
								+'<span class="caret"></span>'
								+'</button>'
								+'<ul class="dropdown-menu" role="menu"' 
								+'aria-labelledby="dropdownMenu1">'
								+'<li role="presentation">'
								+'<a role="menuitem" tabindex="-1" href="userDevice/editNewPile.action?type=2&newPileId='
								+value.id
								+'">修改</a>'
								+'</li>'
								+'<li role="presentation">'
								+'<a role="menuitem" tabindex="-1" href="userDevice/showUserUnVlaidDeviceDetail.action?pileId='
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
								+'<div class="dropdown">'
								+'<button type="button" class="btn btn-default  btn-sm dropdown-toggle" id="dropdownMenu1"' 
								+'data-toggle="dropdown"><span style="color:#337ab7;">详情</span>'
								+'<span class="caret"></span>'
								+'</button>'
								+'<ul class="dropdown-menu" role="menu"' 
								+'aria-labelledby="dropdownMenu1">'
								+'<li role="presentation">'
								+'<a role="menuitem" tabindex="-1" href="userDevice/showUserUnVlaidDeviceDetail.action?pileId='
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
							+'<div class="dropdown">'
							+'<button type="button" class="btn btn-default  btn-sm dropdown-toggle" id="dropdownMenu1"' 
							+'data-toggle="dropdown"><span style="color:#337ab7;">详情</span>'
							+'<span class="caret"></span>'
							+'</button>'
							+'<ul class="dropdown-menu" role="menu"' 
							+'aria-labelledby="dropdownMenu1">'
							+'<li role="presentation">'
							+'<a role="menuitem" tabindex="-1" href="userDevice/showUserUnVlaidDeviceDetail.action?pileId='
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
		    	  		 if(!$("#help-block2").attr("id")){
							 item.parent().parent().append("<p id='help-block2' class='help-block text-center' style='margin-top:5%;'>暂无记录</p>");
						 }
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
$(function(){
	//var item;
	/**
	 * 初始化搜索条件
	 */
	function initInput(){
		//$("#datetime").find("input").val("");
		$("#searchKeyName").val("");
		$("#validStatus").val(0);
	}
	
	//初始化充电点
	function initStationBody(){
		param2={};
		param2.currentPage=$("#currentPage").val();
		param2.searchStationName = $("#searchName").val();
		param2.validStatus = $("#validStatus2").val();
		param2.limit = 5;
		var item2=$("#userUnValidStationBody");
		$.ajax({  
		      url:searchUnValidStationsUrl,// 跳转到 action  
		      data:param2,  
		      type:'post',  
		      cache:false,  
		      dataType:'json',  
		      beforeSend:function(){$("#loading").removeClass("hide");},
		      success:function(data){
		    	  if(data.userLoginStatus){
		    		  checkLoginStatus(data,true);
		    		  return;
		    	  }
					item2.empty();
					if(data.page.root.length > 0){
						$("#help-block").remove();
					$(data.page.root).each(function(index,value){
						//var totalPileNum = parseInt(value.fastNum)+parseInt(value.slowNum);
						str2 = "<tr>"
							+"<td>"+value.stationName+"</td>"
							+"<td title='"+value.address+"'><span>"
							+mySubString(value.address,10)
							+"</span></td>"
							+"<td>"+value.openDayDesc2+"</td>"
							+"<td>"+value.openTimeDesc2+"</td>"
							+"<td>"+value.requestTypeDesc2+"</td>";
						var requestType = parseInt(value.requestType);
						var validStatus = parseInt(value.validStatus);
						if(validStatus == 1){
							str2=str2+" <td style='color:"+"red;'>"+value.validStatusDesc2+"</td>";
						}else if(validStatus == 2){
							str2=str2+" <td style='color:"+"green;'>"+value.validStatusDesc2+"</td>";
						}else if(validStatus == 4){
							str2=str2+" <td style='color:"+"#ccc;'>"+value.validStatusDesc2+"</td>";
						}else{
							str2=str2+" <td style='color:"+"#ccc;'>"+value.validStatusDesc2+"</td>";
						}
						
						if(requestType == 1){
							if(validStatus == 1 || validStatus == 4){
								str2 = str2 
								+"<td>"
								+'<div class="dropdown">'
								+'<button type="button" class="btn btn-default  btn-sm dropdown-toggle" id="dropdownMenu1"' 
								+'data-toggle="dropdown"><span style="color:#337ab7;">详情</span>'
								+'<span class="caret"></span>'
								+'</button>'
								+'<ul class="dropdown-menu" role="menu"' 
								+'aria-labelledby="dropdownMenu1">'
								+'<li role="presentation">'
								+'<a role="menuitem" tabindex="-1" href="userDevice/editNewStation.action?newStationId='
								+value.id
								+'">修改</a>'
								+'</li>'
								+'<li role="presentation">'
								+'<a role="menuitem" tabindex="-1" href="userDevice/addNewStationPile.action?newStationId='
								+value.id
								+'">新增</a>'
								+'</li>'
								+'<li role="presentation">'
								+'<a role="menuitem" tabindex="-1" href="userDevice/showUserUnVlaidDeviceDetail.action?stationId='
								+value.id
								+'&detailType=detailStation">详细</a>'
								+'</li>'
								+'</ul>'
								+'</div>'
								+"</td>";
							}
							else{
								str2 = str2 
								+"<td>"
								+'<div class="dropdown">'
								+'<button type="button" class="btn btn-default  btn-sm dropdown-toggle" id="dropdownMenu1"' 
								+'data-toggle="dropdown"><span style="color:#337ab7;">详情</span>'
								+'<span class="caret"></span>'
								+'</button>'
								+'<ul class="dropdown-menu" role="menu"' 
								+'aria-labelledby="dropdownMenu1">'
								+'<li role="presentation">'
								+'<a role="menuitem" tabindex="-1" href="userDevice/showUserUnVlaidDeviceDetail.action?stationId='
								+value.id
								+'&detailType=detailStation">详细</a>'
								+'</li>'
								+'</ul>'
								+'</div>'
								+"</td>";
							}

						}
						else if(requestType == 3){
							str2=str2
							+"<td>"
							+'<div class="dropdown">'
							+'<button type="button" class="btn btn-default  btn-sm dropdown-toggle" id="dropdownMenu1"' 
							+'data-toggle="dropdown"><span style="color:#337ab7;">详情</span>'
							+'<span class="caret"></span>'
							+'</button>'
							+'<ul class="dropdown-menu" role="menu"' 
							+'aria-labelledby="dropdownMenu1">'
							+'<li role="presentation">'
							+'<a role="menuitem" tabindex="-1" href="userDevice/showUserUnVlaidDeviceDetail.action?stationId='
							+value.id
							+'&detailType=detailStation">详细</a>'
							+'</li>'
							+'</ul>'
							+'</div>'
							+"</td>";

						}

						str2 = str2
						+"</tr>"; 
						item2.append(str2);//封装table

					});
					 $("#pagingToolbar").show();
					}else{
		    	  		  $("#pagingToolbar").hide();
		    	  		if(!$("#help-block").attr("id")){
							 item2.parent().parent().append("<p id='help-block' class='help-block text-center' style='margin-top:5%;'>暂无记录</p>");
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
	//initInput();
	initPagingToolbar(initStationBody);
	initPagingToolbar2(initPileBody);

	initStationBody();
	//initOneBootStrapDate2($("#datetime"));
	initPileBody();
	//stationAndPileBtn(initStationBody,initPileBody,initInput);
	initExportBtn($("#exportBtn1"),null,searchUnValidStationsUrl,null,"待审核充电点");
	initExportBtn($("#exportBtn2"),null,searchUnValidPilesByAjaxUrl,null,"待审核充电桩");
	
	
	$("#searchBtn").on("click",function(){
		$("#currentPage").val("1");
		initStationBody();
		
	});
	$("#searchBtn2").on("click",function(){
		$("#currentPage2").val("1");
		initPileBody();
	});
	
})

