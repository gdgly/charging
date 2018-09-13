var map;
var point;
var param;
$(document).ready(function(){
	initMap();
	$("#showMapBtn").on("click",function(){
		$("#pointMapModal").modal();
		setTimeout(function(){    
			  map.panTo(point);    
		}, 500); 
	});
	
	$("#saveBtn").on("click",function(){
		save();
	});
	
});

function initMap(){
//	 百度地图API功能
	map = new BMap.Map("mapContainer", {enableMapClick : false});
	map.addControl(new BMap.NavigationControl());//添加缩放平移控件
	map.addControl(new BMap.OverviewMapControl());//添加缩略图控件
	map.enableScrollWheelZoom();//允许使用鼠标滚轮进行地图缩放
	
	point = new BMap.Point(lng , lat); // 创建点坐标
	map.centerAndZoom(point, 19);
	var marker = new BMap.Marker(point);  // 创建标注
	map.addOverlay(marker);               // 将标注添加到地图中
	marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
	setTimeout(function(){    
		  map.panTo(point);    
	}, 500);
}

function save(){
	param={};
	param.id = id;
	param.validstatus = $('#validStatus option:selected').val();
	param.validremark = $.trim($("#validRemark").val());
	param.noticetypes = getNoticeTypes();
	param.tm = new Date().getTime();
	var msg = validParam(param);
	if ("success" != msg) {
		showInfo(msg);
		return;
	}
	$.ajax({
		type:"POST",
		url:'device/deviceVerify_stationVerify.action',
		data:param,
		dataType:'json',
        cache: false,
        success:function(data,options){
        	if(data.success){
        		gobackAndReload();
        	}else{
        		showMsg(data.message, data.errormsg);
        	}
        }
	});
}

//审核结果切换事件
function onStatusChange(){
	var validStatus = $('#validStatus option:selected').val();
	if(validStatus == DEVICE_VERIFY_STATUS_FAILED){
		$('#validRemarkDiv').removeClass('hide');
	}else{
		$('#validRemarkDiv').addClass('hide');
		$('#validRemark').val('');
	}
}

function validParam(obj){
	if(isEmpty(param.id)){
		return "id不能为空";
	}else if(isEmpty(obj.validstatus)){
		return "审核结果不能为空";
	}else if(obj.validstatus == DEVICE_VERIFY_STATUS_FAILED && isEmpty(param.validremark)){
		return "审核失败原因不能为空";
	}
	return "success";
}












