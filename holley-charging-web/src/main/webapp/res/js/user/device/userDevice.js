/**
 * 个人设备
 */
	var param={};
	var map;
	var temLng;
	var tempSearchName;
	var userValidStationUrl = "userjson/device_userValidStation.action";//查找已有点
	var userValidPileUrl = "userjson/device_userValidPile.action";//查找已有桩
	var userUnValidStationUrl = "userjson/device_userUnValidStation.action";//查找未审核设备
	goUserMenu("userDevice");
	initExportBtn($("#exportBtn"),userValidPileUrl,userValidStationUrl,"已有充电桩","已有充电点");
	//初始化充电桩
	function initUserValidPileBody(){
		param={};
		param.currentPage=$("#currentPage2").val();
		param.stationId = $("#stationId").val();
		param.searchName = $("#searchName").val().trim();
		var item=$("#userValidPileBody");
		
		if(!param.stationId || param.stationId <= 0){
			showWarning("请选择充电点！！");
			return false;
		}
		$.ajax( {  
		      url:userValidPileUrl,// 跳转到 action  
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
		  		if(data.page.root.length > 0){
					$("#help-block2").remove();
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
						+"<td>"+value.comTypeDesc+"</td>"
						+"<td><span title='"+value.comAddr+"'>"+mySubString(value.comAddr, 5)+"</span></td>"
						+"<td>"+value.isAppDesc+"</td>"
						+"<td>"+value.payWayDesc+"</td>";
					
					str=str +"<td>"
					+'<div class="dropdown">'
					+'<button type="button" class="btn btn-default  btn-sm dropdown-toggle" id="dropdownMenu1"' 
					+'data-toggle="dropdown"><span style="color:#337ab7;">详情</span>'
					+'<span class="caret"></span>'
					+'</button>'
					+'<ul class="dropdown-menu" role="menu"' 
					+'aria-labelledby="dropdownMenu1">'
					+'<li role="presentation">'
					+'<a role="menuitem" tabindex="-1" href="userDevice/editValidPile.action?validPileId='
					+value.id+"&backStationId="+value.stationId
					+'">修改</a>'
					+'</li>'
					+'<li role="presentation">'
					+'<a role="menuitem" tabindex="-1" href="userDevice/showUserVlaidDeviceDetail.action?pileId='
					+value.id+"&backStationId="+value.stationId
					+'&detailType=detailPile">详细</a>'
					+'</li>'
					+'</ul>'
					+'</div>'
					+"</td>"
					+"</tr>";
					item.append(str);
				});
				 $("#pagingToolbar2").show();
		  		}else{
		  			 $("#pagingToolbar2").hide();
					 if(!$("#help-block2").attr("id")){
						 item.parent().parent().append("<p id='help-block2' class='help-block text-center' style='margin-top:10%;'>暂无记录</p>");
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
	}
	/**
	 * 跳转已审核桩设备
	 */
	function goUserValidPile(obj,init){
		$("#stationId").val($(obj).attr("stationId"));
		$("#searchName").attr("placeholder","请输入充电桩名称");
		tempSearchName = $("#searchName").val();
		$("#searchName").val("");
		$("#goBackBtn").removeClass("hide");
		$("#userValidStationTable").addClass("hide");
		$("#pageTool1").addClass("hide");
		$("#userValidPileTable").removeClass("hide");
		$("#pageTool2").removeClass("hide");
		$("#currentPage2").val("1");
		init();
	}
	////////////////////////////////添加STATION--START////////////////////////////////////////////////////
	function cheack(station){
		if(isEmpty(station.stationName)){
			return  "请输入充电点名称！！";
		}
		else if(isEmpty(station.lng)){
			return  "请输入经度！！";
		}
		else if(isEmpty(station.lat)){
			return  "请输入纬度！！";
		}
		else if(station.province == 0){
			return  "请选择省份！！";
		}
		else if(station.city == 0){
			return  "请选择市区！！";
		}else if(station.openDay == 0){
			return  "请选择开放日！！";
		}else if(station.openTime == 0){
			return  "请选择开放时间！！";
		}
		else if(!regBox.regMobile.test(station.linkPhone) && !regBox.regTel.test(station.linkPhone)){
			return  "请输入正确的座机号码电话或手机号码！！";
		}
		else if(isEmpty(station.address)){
			return  "请输入详细地址！！";
		}
		else if(isEmpty($("#addStationImg").val())){
			return  "请上传图片！！";
		}
		return "success";
	}

	// 百度地图API功能
	function myyFun(result){
		var cityName = result.name;
		if(cityName){
			map.centerAndZoom(cityName, 16);
		}
		else{
			map.centerAndZoom("北京市", 16);	
		}
		
	}
	//地图智能搜索
	function setPlace(myValue){
		map.clearOverlays();    //清除地图上所有覆盖物
		function myFun(){
			var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
			map.centerAndZoom(pp, 19);
			//map.addOverlay(new BMap.Marker(pp));    //添加标注
		}
		var local = new BMap.LocalSearch(map, { //智能搜索
		  onSearchComplete: myFun
		});
		local.search(myValue);
	}

	function initMap(obj){
		map = new BMap.Map(obj, {
			enableMapClick : false
		}); // 创建地图实例
		 map.enableScrollWheelZoom();
		var myCity = new BMap.LocalCity();//定位当前城市
		myCity.get(myyFun);                  // 初始化地图,设置城市和地图级别。
		//单击获取点击的经纬度
		map.addEventListener("click",function(e){
			map.clearOverlays(); 
			var lng = e.point.lng;
			var lat = e.point.lat;
			$("#lngMap").text(lng);
			$("#latMap").text(lat);
			var point = new BMap.Point(lng, lat);
			//map.centerAndZoom(point, 19);
			var marker = new BMap.Marker(point);
			map.addOverlay(marker);  
			marker.setAnimation(BMAP_ANIMATION_BOUNCE);
		});
	}
	function resetInput(){
		$("#lngMap").text($("#lng").val());
		$("#latMap").text($("#lat").val());
		$("#searchMapName").val("");
	}
	////////////////////////////////添加STATION--END/////////////////////////////////////////////////////
	$(function(){
		commomChangeImg("addStationImg","showAddStationImg");//初始化图片
		function initUserValidStationBody(){
			param={};
			param.currentPage=$("#currentPage").val();
			param.searchName = $("#searchName").val().trim();
			var item = $("#userValidStationBody");
			$.ajax({
			      url:userValidStationUrl,// 跳转到 action  
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
							var totalPileNum = parseInt(value.fastNum)+parseInt(value.slowNum);
							if(!totalPileNum){
								totalPileNum = 0;
							}
							str = "<tr>"
									+"<td>"+value.stationName+"</td>"
									+"<td title='"+value.address+"'>地址：<span>"
									+mySubString(value.address,15)
									+"</span></td>";
							temp = value.isShow;
							if(1 == temp){
								str=str+"<td>"+"显示"+"</td>";
							}else{
								str=str+"<td>"+"隐藏"+"</td>";
							}
							 	str = str+"<td>"+value.openDayDesc+"</td>"
										 +"<td>"+value.openTimeDesc+"</td>";
							if(totalPileNum > 0){
								str=str	
									+"<td>"
									+totalPileNum+"个桩<a stationId='"+value.id+"' href='javascript:;' onclick='goUserValidPile(this,initUserValidPileBody);'>>></a>"
									+"</td>";
							}else{
								str=str	
									+"<td>"
									+totalPileNum+"个桩<a stationId='"+value.id+"' href='javascript:;'>>></a>"
									+"</td>";
							}
							str=str	
									+"<td>"
									+"<img class='startImg' style='background-position:-"+(50-parseInt(value.score))+"px;background-image:url("+IMG_SRC+"res/img/bussiness/starBackImg.png);'"+" src="+IMG_SRC+"res/img/bussiness/starImg.gif"+">"
									+"</td>"
									+"<td>"
									+'<div class="dropdown">'
									+'<button type="button" class="btn btn-default  btn-sm dropdown-toggle" id="dropdownMenu1"' 
									+'data-toggle="dropdown"><span style="color:#337ab7;">详情</span>'
									+'<span class="caret"></span>'
									+'</button>'
									+'<ul class="dropdown-menu" role="menu"' 
									+'aria-labelledby="dropdownMenu1">'
									+'<li role="presentation">'
									+'<a role="menuitem" tabindex="-1" href="userDevice/editValidStation.action?validStationId='
									+value.id
									+'">修改</a>'
									+'</li>'
									+'<li role="presentation">'
									+'<a role="menuitem" tabindex="-1" href="userDevice/addUserValidPile.action?validStationId='
									+value.id+"&isStation=1"
									+'">新增</a>'
									+'</li>'
									+'<li role="presentation">'
									+'<a role="menuitem" tabindex="-1" href="userDevice/showUserVlaidDeviceDetail.action?stationId='
									+value.id
									+'&detailType=detailStation">详细</a>'
									+'</li>'
									+'</ul>'
									+'</div>'
									/*+"<a href='Manager/editValidStation.action?validStationId=" 
									+value.id
									+"'><span title='修改' style='color:#337ab7;' class='glyphicon glyphicon-pencil'></span></a>&nbsp;&nbsp;&nbsp;"
									+"<a href='Manager/addValidStationPile.action?validStationId="
									+value.id
									+"'><span title='添加' style='color:#337ab7;' class='glyphicon glyphicon-plus'></span></a>&nbsp;&nbsp;&nbsp;"
									+"<a href='Manager/editValidStation.action?validStationId=" 
									+value.id
									+"'><span title='详细' style='color:#337ab7;' class='glyphicon glyphicon-list'></span></a>"*/
									+"</td>"
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
			tempStationId = $("#stationId").val();
			if(tempStationId && tempStationId > 0){
				initUserValidPileBody();
			}else{
				initUserValidStationBody();
			}
		});
		$("#addDeviceBtn").on("click",function(){
			stationId = $("#stationId").val();
			if(stationId > 0){
				href("userDevice/addUserValidPile.action?validStationId="+stationId);
			}else{
				resetForm($("#userStationForm"))
				$("#userValidStationPage").addClass("hide");
				$("#addUserStationPage").removeClass("hide");	
			}
			
		});
		$("#goBackStationBtn").on("click",function(){
			$("#userValidStationPage").removeClass("hide");
			$("#addUserStationPage").addClass("hide");
		});
		$("#showUnVlaidDeviceBtn").on("click",function(){
				href("userDevice/initUserUnDevice.action");
		});
		$("#goBackBtn").on("click",function(){
			$(this).addClass("hide");
			$("#searchName").attr("placeholder","请输入充电点名称");
			if(tempSearchName){
				$("#searchName").val(tempSearchName);
			}else{
				$("#searchName").val("");
			}
			$("#stationId").val(0);
			$("#userValidStationTable").removeClass("hide");
			$("#userValidPileTable").addClass("hide");
			initUserValidStationBody();
		});
		//initOneBootStrapDate2($('#datepicker'));
		initPagingToolbar(initUserValidStationBody);
		if(gobackStationId > 0){
			$("#stationId").val(gobackStationId);
			$("#searchName").attr("placeholder","请输入充电桩名称");
			//tempSearchName = $("#searchName").val();
			$("#searchName").val("");
			$("#goBackBtn").removeClass("hide");
			$("#userValidStationTable").addClass("hide");
			$("#userValidPileTable").removeClass("hide");
			$("#currentPage2").val("1");
			initUserValidPileBody();
		}else{
			initUserValidStationBody();
		}
		
		
////////////////////////////////添加STATION--START/////////////////////////////////////////////////////
		
		initMap("baiduMap");//初始化地图
		//禁止修改经度
		$("#lng").on("focus",function(){
			temLng = $("#lng").val();
		});
		$("#lng").on("change",function(){
			$("#lng").val(temLng);
		});
		
		//禁止修改纬度
		$("#lat").on("focus",function(){
			temLng = $("#lat").val();
		});
		$("#lat").on("change",function(){
			$("#lat").val(temLng);
		});
		//打开地图事件
		$("#pointBtn").on("click",function(){
			$("#pointMapModal").modal();
	 	}); 
		//提交打点信息事件
		$("#editLngLatBtn").on("click",function(){
	 		$("#lng").val($("#lngMap").text());
			$("#lat").val($("#latMap").text());
			$('#pointMapModal').modal("hide"); 
		});
		//绑定地图搜索事件
		$("#searchMapBtn").on("click",function(){
			searchName = $("#searchMapName").val();
			setPlace(searchName);
		});
		//地图框打开事件
		$('#pointMapModal').on('show.bs.modal', function () {
			resetInput();
			})
		//地图框关闭事件
		$('#pointMapModal').on('hidden.bs.modal', function () {
			resetInput();
			})
		var msg;
		var station;
		var addStationByAjaxUrl = "device_addStationByAjax.action";
		var addUserPileUrl = "userDevice/addUserNewPile.action";
		var newStationId = 0;
		initArea($("#province"),$("#city"));//初始化省市
		resetForm($("#userStationForm"));//重置表单

		$("#addUserPilePageBtn").on("click",function(){
			if(newStationId > 0){
			href(addUserPileUrl+"?newStationId="+newStationId);
			}else{
				showWarning("当前无法添加！！");
			}
		});
		$("#saveUserStationBtn").on("click",function(){
			station= getFormJson($("#userStationForm"));
			msg = cheack(station);
			if("success" != msg){
				showWarning(msg);
			}
			else{
			param.pobTempStation=formDataToJsonString($("#userStationForm"));
	 		$('#userStationForm').ajaxSubmit({
				url:addStationByAjaxUrl,
				type:'post',
				dataType:'json',
				data:param,
				beforeSubmit:function(){$("#loading").removeClass("hide")},
				success:function(data){
					$("#loading").addClass("hide");
					 if(data.userLoginStatus){
			    		  checkLoginStatus(data,true);
			    		  return;
			    	  }
					if("success" == data.map.msg){
						newStationId = data.map.newStationId;
						$("#addUserPilePageBtn").attr("disabled",false); 
						$("#saveUserStationBtn").attr("disabled",true); 
							showWarning("添加成功！！");
					}
					else{
						showWarning(data.map.msg);
					}
				
						}
				});  
		}
		});
////////////////////////////////添加STATION--END//////////////////////////////////////////////////////
	})
	
