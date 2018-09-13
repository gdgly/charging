commomChangeImg("editStationImg","showEditStationImg");
	var map;
	var point;
	var defaultSize = 15;
	var marker;
/////////////////////////////////
	function removeDisabled(){
		$("#saveStationBtn").attr("disabled",false); 
	}
	
	function actionFun(obj,action){
		obj.on(action,removeDisabled);
	}
	function initChangeSaveBtn(){
		actionFun($("input[name='stationName']"),"keydown");
		actionFun($("input[name='linkPhone']"),"keydown");
		actionFun($("textarea[name='address']"),"keydown");
		actionFun($("textarea[name='remark']"),"keydown");
		actionFun($("input[name='img']"),"change");
		actionFun($("input[name='parkType']"),"change");
		actionFun($("select[name='province']"),"change");
		actionFun($("select[name='city']"),"change");
		actionFun($("select[name='openDay']"),"change");
		actionFun($("select[name='openTime']"),"change");
	}
///////////////////////////////////
	function cheack(station) {
		if (isEmpty(station.stationName)) {
			return "请输入充电点名称！！";
		} else if (isEmpty(station.lng)) {
			return "请输入经度！！";
		} else if (isEmpty(station.lat)) {
			return "请输入纬度！！";
		} else if (station.province == 0) {
			return "请选择省份！！";
		} else if (station.city == 0) {
			return "请选择市区！！";
		} else if(station.openDay <= 0){
			return "请选择开放日！！";
		}else if(station.openTime <= 0){
			return "请选择开放时间！！";
		}else if(!regBox.regMobile.test(station.linkPhone) && !regBox.regTel.test(station.linkPhone)){
			return  "请输入正确的座机号码电话或手机号码！！";
		} else if (isEmpty(station.address)) {
			return "请输入详细地址！！";
		}
		return "success";
	}

	// 百度地图API功能
	function myyFun(result) {
		var cityName = result.name;
		if (cityName) {
			map.centerAndZoom(cityName, defaultSize);
		} else {
			map.centerAndZoom("北京市", defaultSize);
		}

	}
	//地图智能搜索
	function setPlace(myValue) {
		map.clearOverlays(); //清除地图上所有覆盖物
		function myFun() {
			var pp = local.getResults().getPoi(0).point; //获取第一个智能搜索的结果
			map.centerAndZoom(pp, defaultSize);
			//map.addOverlay(new BMap.Marker(pp));    //添加标注
		}
		var local = new BMap.LocalSearch(map, { //智能搜索
			onSearchComplete : myFun
		});
		local.search(myValue);
	}

	function initMap(obj, lng, lat) {
		map = new BMap.Map(obj, {
			enableMapClick : false
		}); // 创建地图实例
		map.enableScrollWheelZoom();
		if (lng && lat) {
			point = new BMap.Point(lng, lat);
			map.centerAndZoom(point, defaultSize);
			marker = new BMap.Marker(point); // 创建标注
			map.addOverlay(marker); // 将标注添加到地图中
			marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
		} else {
			var myCity = new BMap.LocalCity();//定位当前城市
			myCity.get(myyFun); // 初始化地图,设置城市和地图级别。
		}

		//单击获取点击的经纬度
			map.addEventListener("click", function(e) {
			map.clearOverlays();
			var lng = e.point.lng;
			var lat = e.point.lat;
			$("#lngMap").text(lng);
			$("#latMap").text(lat);
			point = new BMap.Point(lng, lat);
			marker = new BMap.Marker(point);
			map.addOverlay(marker);
			marker.setAnimation(BMAP_ANIMATION_BOUNCE);
		});
	}
	function resetInput() {
		$("#lngMap").text($("#lng").val());
		$("#latMap").text($("#lat").val());
		$("#searchName").val("");
	}

	$(function() {
		var temLng;
		 initMap("baiduMap", $("#lng").val(), $("#lat").val());//初始化地图 
		//禁止修改经度
		$("#lng").on("focus", function() {
			temLng = $("#lng").val();
		});
		$("#lng").on("change", function() {
			$("#lng").val(temLng);
		});

		//禁止修改纬度
		$("#lat").on("focus", function() {
			temLng = $("#lat").val();
		});
		$("#lat").on("change", function() {
			$("#lat").val(temLng);
		});
		//打开地图事件
		$("#pointBtn").on("click", function() {
			map.clearOverlays();
			point = new BMap.Point($("#lng").val(), $("#lat").val());
			marker = new BMap.Marker(point);
			map.addOverlay(marker);
			marker.setAnimation(BMAP_ANIMATION_BOUNCE);
		 	window.setTimeout(function(){    
				  map.panTo(point);    
				 }, 500);  
			$("#pointMapModal").modal();
		});
		//提交打点信息事件
		$("#editLngLatBtn").on("click", function() {
			$("#lng").val($("#lngMap").text());
			$("#lat").val($("#latMap").text());
			$('#pointMapModal').modal("hide");
			removeDisabled();
		});
		//绑定地图搜索事件
		$("#searchBtn").on("click", function() {
			var searchName = $("#searchName").val();
			setPlace(searchName);
		});
		//地图框打开事件
		$('#pointMapModal').on('show.bs.modal', function() {
			resetInput();
		})
		//地图框关闭事件
		$('#pointMapModal').on('hidden.bs.modal', function() {
			resetInput();
		})
		
		var msg;
		var param = {};
		var editStationByAjaxUrl = "device_editStationByAjax.action";
		var finishUrlForValid = "deviceManager/searchValidDevice.action";
		var finishUrlForUnValid = "deviceManager/searchUnValidDevice.action";
		initArea($("#province"), $("#city"));//初始化省市
		resetForm($("#stationForm"));//重置表单
		initChangeSaveBtn();
		$("#saveStationBtn").on("click", function() {
			var station = getFormJson($("#stationForm"));
			msg = cheack(station);
			if ("success" != msg) {
				showWarning(msg)
			}
			else{
			param.pobStation = formDataToJsonString($("#stationForm"));
			param.actionType = $("#actionType").val();
			param.stationId = $("#stationId").val();
			$('#stationForm').ajaxSubmit({
				url : editStationByAjaxUrl,
				type : 'post',
				dataType : 'json',
				data : param,
				beforeSubmit:function(){$("#loading").removeClass("hide")},
				success : function(data) {
					$("#loading").addClass("hide");
					 if(data.userLoginStatus){
			    		  checkLoginStatus(data,true);
			    		  return;
			    	  }
					if ("success" == data.map.msg) {
						/* var opt={};
						opt.onOk = function(){
							if (param.actionType == "editValidStation") {
								href(finishUrlForValid);
							} else if (param.actionType == "editNewStation") {
								href(finishUrlForUnValid);
							} 
						}
						opt.onClose = opt.onOk; */
						showSuccess("修改成功！！")
					} else {
						showWarning(data.map.msg)
					}

				}
			});
			}
		});

	})