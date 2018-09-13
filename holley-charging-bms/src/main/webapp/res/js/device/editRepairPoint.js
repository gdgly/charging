var param={};
var msg;
var map;
var defaultSize = 15;
$(document).ready(function(){
	initParam();
});

function initParam(){
	var temLng;
	//初始化地图 
	 initMap("baiduMap", $("#lng").val(), $("#lat").val());
//	//禁止修改经度
//	$("#lng").on("focus", function() {
//		temLng = $("#lng").val();
//	});
//	$("#lng").on("change", function() {
//		$("#lng").val(temLng);
//	});
//	//禁止修改纬度
//	$("#lat").on("focus", function() {
//		temLng = $("#lat").val();
//	});
//	$("#lat").on("change", function() {
//		$("#lat").val(temLng);
//	});
	//打开地图事件
	$("#markPointBtn").on("click", function() {
		map.clearOverlays();
		point = new BMap.Point($("#lng").val(), $("#lat").val());
		marker = new BMap.Marker(point);
		map.addOverlay(marker);
		marker.setAnimation(BMAP_ANIMATION_BOUNCE);
		if(point.lng != 0 && point.lat != 0){
			window.setTimeout(function(){    
				map.panTo(point);    
			}, 500);  
		}
	 	$("#pointMapModal").modal();
	});
	//提交打点信息事件
	$("#editLngLatBtn").on("click", function() {
		$("#lng").val($("#lngMap").val());
		$("#lat").val($("#latMap").val());
		$('#pointMapModal').modal("hide");
		removeDisabled();
	});
	//绑定地图搜索事件
	$("#searchBtn").on("click", function() {
		var searchName = $.trim($("#searchName").val());
		setPlace(searchName);
	});
	//地图框打开事件
	$('#pointMapModal').on('show.bs.modal', function() {
		resetInput();
	});
	//地图框关闭事件
	$('#pointMapModal').on('hidden.bs.modal', function() {
		resetInput();
	});
	//查询保存按钮点击事件
	$("#saveBtn").on("click",function(){
		if(requestType == REQUEST_TYPE_ADD){
			addRepairPoint();
		}else{
			editRepairPoint();
		}
	});
}

//百度地图API功能
function myyFun(result) {
	var cityName = result.name;
	if (cityName) {
		map.centerAndZoom(cityName, defaultSize);
	} else {
		map.centerAndZoom("北京市", defaultSize);
	}

}

function removeDisabled(){
	$("#saveBtn").attr("disabled",false); 
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
	if (lng && lat && "/" != lng && "/" != lat) {
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
		$("#lngMap").val(lng);
		$("#latMap").val(lat);
		point = new BMap.Point(lng, lat);
		marker = new BMap.Marker(point);
		map.addOverlay(marker);
		marker.setAnimation(BMAP_ANIMATION_BOUNCE);
    });
}
function resetInput() {
	if("/" == $("#lng").val() || "/" == $("#lat").val()){
		return;
	}
	$("#lngMap").val($("#lng").val());
	$("#latMap").val($("#lat").val());
	$("#searchName").val("");
}

function addRepairPoint(){
	param={};
	var repairPoint = getFormJson($("#repairPointForm"));
	msg = validForm(repairPoint);
	if ("success" != msg) {
		showInfo(msg);
		return false;
	}
	param.repairPoint = objToJsonString(repairPoint);
	$('#repairPointForm').ajaxSubmit({
		url:'device/repairPoint_addRepairPoint.action',
		type:'post',
		dataType:'json',
		data:param,
		success:function(data){
			if(data.success){
				gobackAndReload();
				showSuccess("新增成功");
			}else{
				showMsg(data.message, data.errormsg);
			}
		}
	});
}

function editRepairPoint(){
	param={};
	var repairPoint = getFormJson($("#repairPointForm"));
	msg = validForm(repairPoint);
	if ("success" != msg) {
		showInfo(msg)
		return false;
	}
	repairPoint.id = repairpointid;
	param.repairPoint = objToJsonString(repairPoint);
	$('#repairPointForm').ajaxSubmit({
		url:'device/repairPoint_editRepairPoint.action',
		type:'post',
		dataType:'json',
		data:param,
		success:function(data){
			if(data.success){
				gobackAndReload();
				showSuccess("修改成功");
			}else{
				showInfo(data.message);
			}
		}
	});
}

//校验表单信息
function validForm(obj) {
	if (isEmpty(obj.name)) {
		return "请输入服务点名称.";
	} else if (isEmpty(obj.address)) {
		return "请输入服务点地址.";
	}else if (isEmpty(obj.isShow)) {
		return "请选择是否显示服务点.";
	}else if(isEmpty(obj.lat) || isEmpty(obj.lng)){
		return "请输入经纬度.";
	}else if(isEmpty(obj.tel) || !REGBOX.regTel.test(obj.tel)){
		return "请正确输入座机号码.";
	}else if(!isEmpty(obj.phone) && !REGBOX.regMobile.test(obj.phone)){
		return "请正确输入手机号码.";
	}
	return "success";
}






