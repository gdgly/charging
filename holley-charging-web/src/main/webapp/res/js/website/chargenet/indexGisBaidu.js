
	var map = new BMap.Map("mapContainer",{
			enableMapClick : false
	}); // 创建地图实例
	map.addControl(new BMap.NavigationControl());//添加缩放平移控件
// map.addControl(new BMap.ScaleControl());//添加比例尺控件
	map.addControl(new BMap.OverviewMapControl());//添加缩略图控件
//	map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
	map.enableScrollWheelZoom();//允许使用鼠标滚轮进行地图缩放
	
	//控制地图的最大和最小缩放级别
	map.setMinZoom(11);
//	map.setMaxZoom(19);
	
	var stationIcon = new BMap.Icon(IMG_SRC+"res/img/mark/map_sign_1.png", new BMap.Size(32,46));
	var stationOverIcon = new BMap.Icon(IMG_SRC+"res/img/mark/map_sign_2.png", new BMap.Size(32,46));
	var stationSelectIcon = new BMap.Icon(IMG_SRC+"res/img/mark/map_sign_2.png", new BMap.Size(32,46));
	
	var size = new BMap.Size(10, 20);
	map.addControl(new BMap.CityListControl({
	    anchor: BMAP_ANCHOR_TOP_RIGHT,
	    offset: size,
	    // 切换城市之间事件
	     onChangeBefore: onCityChangeBefore,
	    // 切换城市之后事件
	     onChangeAfter:onCityChangeAfter
	}));
	var myCity = new BMap.LocalCity();
	myCity.get(setCityCenter);
	var flag = true;
	map.addEventListener("tilesloaded",function(){
		queryStationByRange();
		if(flag){
			operateflag = "byrange";
			pageIndex = 1;
			queryStationByRangeByPage();
			flag = false;
		}
	});

function onCityChangeBefore(){
	//return false;
//	alert('before');
}

function onCityChangeAfter(){
}

//根据定位城市，设置地图中心
function setCityCenter(result){
    var cityName = result.name;
    if(cityName == null){
    	var point = new BMap.Point(120.054161, 30.250309);
    	map.centerAndZoom(point, 13); // 初始化地图，设置中心点坐标和地图级别
    }else{
    	map.centerAndZoom(cityName, 13);
  //    map.setCenter(cityName);   //关于setCenter()可参考API文档---”传送门“
    }
}

//创建点
var markerArray = new Array();
var tempArray;
function createMarkers(list){
	if(list == null || list.length == 0)return;
	var point;
	tempArray = new Array();
	for ( var i = 0; i < list.length; i++) {
		point = new BMap.Point(list[i].lng, list[i].lat);
		
		var marker = new BMap.Marker(point);
		marker.id = list[i].id;
		marker.setIcon(stationIcon);
		marker.setTitle(list[i].stationName);
		marker.stationname = list[i].stationName;
		marker.address = list[i].address;
		marker.telephone = list[i].linkPhone;
		marker.score = list[i].score;
		marker.idlenum = isEmpty(list[i].idleNum)?"0":list[i].idleNum;
		marker.fastnum = isEmpty(list[i].fastNum)?"0":list[i].fastNum;
		marker.slownum = isEmpty(list[i].slowNum)?"0":list[i].slowNum;
		marker.pilenum = parseInt(marker.fastnum)+parseInt(marker.slownum);
		
		
		//在图标上设置文本数字
		var offsetX = LABEL_OFFSET_X1;
		var offsetY = LABEL_OFFSET_Y;
		if(marker.pilenum < 10){
			offsetX = LABEL_OFFSET_X1;
		}else if(marker.pilenum < 100){
			offsetX = LABEL_OFFSET_X2;
		}else{
			offsetX = LABEL_OFFSET_X3;
		}
		var label = new BMap.Label(marker.pilenum,{offset:new BMap.Size(offsetX,offsetY)});
		label.setStyle(LABEL_STYLE);
		marker.setLabel(label);
		
		tempArray.push(marker);
		//添加地图可视区域内新增的点
		if(findMarker(markerArray,marker.id) == false){
			markerArray.push(marker);
			map.addOverlay(marker);
		}
		
		marker.addEventListener("click", function() {
			queryStationDetail(this);
		});
		
		if(selectMarkerid != null && selectMarkerid == marker.id){
//			marker.setTop(true);
//			marker.setIcon(stationSelectIcon);
//			clearMarkerIcon();
			queryStationDetail(marker);
		}
	}
	
	//删除已经不在地图可是区域的点
	removeMarker();
	
	//最简单的用法，生成一个marker数组，然后调用markerClusterer类即可。
//	var markerClusterer = new BMapLib.MarkerClusterer(map, {markers:markerArray});
//	
//	for ( var i = 0; i < markerArray.length; i++) {
//		var marker = markerArray[i];
//		var label = new BMap.Label(marker.pilenum,{offset:new BMap.Size(offsetX,offsetY)});
//		label.setStyle(LABEL_STYLE);
//		marker.setLabel(label);
//	}
}

//判断地图可视区域内是否已经存在点
function findMarker(list,id){
	var marker;
	for(var i=0;i<list.length;i++){
		marker = list[i];
		if(marker.id == id)return true;
	}
	return false;
}

//删除地图上已经不在可视区域的点
function removeMarker(){
	if(markerArray == null || markerArray.length == 0)return;
	var marker;
	for(var i=markerArray.length-1;i>=0;i--){
		marker = markerArray[i];
		if(findMarker(tempArray, marker.id) == false){
			markerArray.splice(i,1);
			map.removeOverlay(marker); 
		}
	}
}

//清除覆盖物
function removeOverlay(){
	map.clearOverlays(); 
}

//鼠标滑过事件
function mouseover(item) {
	var itemArray = item.split(",");
	var marker = null;
	for ( var i = 0; i < markerArray.length; i++) {
		if (markerArray[i].id == itemArray[2]) {
			marker = markerArray[i];
		}
	}
	if (marker != null) {
		marker.setIcon(stationOverIcon);
	}
}

//鼠标划出事件
function mouseout(item) {
	var itemArray = item.split(",");
	var marker = null;
	for ( var i = 0; i < markerArray.length; i++) {
		if (markerArray[i].id == itemArray[2]) {
			marker = markerArray[i];
		}
	}
	if (marker != null && marker.id != selectMarkerid) {
		marker.setIcon(stationIcon);
	}
}

//鼠标点击事件
var selectMarkerid;
function mouseclick(item) {
	var itemArray = item.split(",");
	var marker = null;
	var premarker;
	for ( var i = 0; i < markerArray.length; i++) {
		if (markerArray[i].id == itemArray[2]) {
			marker = markerArray[i];
			break;
		}
	}
	if (marker != null) {
//		selectMarkerid = marker.id;
//		marker.setTop(true);
//		marker.setIcon(stationSelectIcon);
//		clearMarkerIcon();
		queryStationDetail(marker);
	}else{
		var point = new BMap.Point(itemArray[0], itemArray[1]);
    	map.centerAndZoom(point, 13);
    	selectMarkerid = itemArray[2];
	}
}

//恢复默认图标
function clearMarkerIcon(){
	for ( var i = 0; i < markerArray.length; i++) {
		if(selectMarkerid == null || (selectMarkerid != null && selectMarkerid != markerArray[i].id)){
			markerArray[i].setIcon(stationIcon);
		}
		
	}
}

//创建信息窗口对象
function createInfoWindow(marker) {
	selectMarkerid = marker.id;
	marker.setTop(true);
	marker.setIcon(stationSelectIcon);
	clearMarkerIcon();
	var html='';
	html += "<h4 style='color:#0a94f2;margin-top:20px;'>"+marker.stationname+"</h4>";
	html += "<p><img class='startImg' style='background-position:-"+(50-parseInt(marker.score))+"px;background-image:url("+IMG_SRC+"res/img/bussiness/starBackImg.png);height:15px;'"+" src=http://img.bbs.csdn.net/upload/201406/27/1403856447_26132.gif>";
	html +="<small style='float:right; position:relative;cursor: hand' ";
	html +=	"onclick='onDetailClick(\""+marker.id+"\");'>查看桩详情</small>";
	html +="</p>";
	html += "<p>"+marker.address+"</p>";
	html += "<p>";
	if(marker.idlenum > 0){
		html += "空闲充电桩<span style='color:red;'> "+marker.idlenum+" </span>个 ";
	}else{
		html += "当前无空闲桩 ";
	}
	if(marker.fastnum > 0){
		html +=	", 快充<span style='color:red;'> "+marker.fastnum+" </span>个 ";
	}
	if(marker.slownum > 0){
		html +=	", 慢充<span style='color:red;'> "+marker.slownum+" </span>个";
	}
	html += "</p>";
	var infoWindow = new BMap.InfoWindow(html);
	infoWindow.setWidth(300);
	return infoWindow;
}

function t(){
	$("a[name='全国']").addClass("hide");
}
$(function(){
	setTimeout("t()", 500);
})

