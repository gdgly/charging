var operateflag = "byrange";//是否边界查询
var pageIndex = 1;//当前页
var pageCount = 0;//总页数
$(document).ready(function(){
	fixHeight();
});

//设置content的高度
function fixHeight() {
	var contentHeight = getContentHeight();
	$("#body").css("height", contentHeight);
    $("#content").css("height", contentHeight);
    $("#stationListGrop").css("height",contentHeight-89);
   
}

//根据地图可视范围查询桩
var stationList;
function queryStationByRange(){
	var bs = map.getBounds();   //获取可视区域
	var bssw = bs.getSouthWest();   //可视区域左下角
	var bsne = bs.getNorthEast();   //可视区域右上角
	var keyword =$("#keyword").val();
//	alert("当前地图可视范围是：" + bssw.lng + "," + bssw.lat + "到" + bsne.lng + "," + bsne.lat);
	$.ajax({
        type: "POST",
        url: 'chargenet/chargenet_queryStationByRange.action',
        data: {keyword:keyword,swlng:bssw.lng,swlat:bssw.lat,nelng:bsne.lng,nelat:bsne.lat,tm:new Date().getTime()},
        dataType:'json',
        cache: false,
        success: function(data,options){
            if(data.success){
            	stationList = data.stationList;
            	createMarkers(stationList);
//            	createStationListGroup(stationList);
            }else{
            	
            }
        }
    });
}

function queryStationByRangeByPage(){
	var bs = map.getBounds();   //获取可视区域
	var bssw = bs.getSouthWest();   //可视区域左下角
	var bsne = bs.getNorthEast();   //可视区域右上角
	var keyword =$("#keyword").val();
	var param = {};
    param.keyword = keyword;
    param.swlng = bssw.lng;
    param.swlat = bssw.lat;
    param.nelng = bsne.lng;
    param.nelat = bsne.lat;
    param.pageIndex = pageIndex;
    param.pageLimit = PAGE_LIMIT;
	$.ajax({
        type: "POST",
        url: 'chargenet/chargenet_queryStationByRangeByPage.action',
        data: param,
        dataType:'json',
        cache: false,
        success: function(data,options){
            if(data.success){
//            	createStationListGroup(data.stationList);
            	var page = data.page;
            	var list = page.root;
            	pageCount = Math.floor(page.totalProperty/PAGE_LIMIT) + (page.totalProperty%PAGE_LIMIT>0?1:0);
            	if(page.totalProperty == 0){
            		$("#stationListGrop").html('');
            	}
            	if(list == null || list.length == 0){
            		hideLoadMore();
            	}else{
            		createStationListGroup(list);
            		if(pageIndex == pageCount && list.length <= PAGE_LIMIT){
            			hideLoadMore();
            		}
            	}
            }else{
            	
            }
        }
    });
}

//根据关键字搜索桩
//$("#queryStationBtn").bind('click',queryStation);
$("#queryStationBtn").bind('click',function(){
	operateflag = "byparam";
	pageIndex = 1;
	queryStation();
	queryStationByPage();
});
function queryStation(){
	var bs = map.getBounds();   //获取可视区域
	var bssw = bs.getSouthWest();   //可视区域左下角
	var bsne = bs.getNorthEast();   //可视区域右上角
    var keyword =$("#keyword").val();
    $.ajax({
        type: "POST",
        url: 'chargenet/chargenet_queryStation.action',
        data: {keyword:keyword,swlng:bssw.lng,swlat:bssw.lat,nelng:bsne.lng,nelat:bsne.lat,tm:new Date().getTime()},
        dataType:'json',
        cache: false,
        success: function(data,options){
            if(data.success){
//            	createStationListGroup(data.stationList);
            	createMarkers(data.stationList);
            	if(data.stationList != null && data.stationList.length > 0){
            		//设查询结果的第一个充电点为中心坐标
            		var record = data.stationList[0];
            		var point = new BMap.Point(record.lng, record.lat);
                	map.centerAndZoom(point, 13); // 初始化地图，设置中心点坐标和地图级别
            	}
            	
            }else{
            }
        }
    });
}

function queryStationByPage(){
	var bs = map.getBounds();   //获取可视区域
	var bssw = bs.getSouthWest();   //可视区域左下角
	var bsne = bs.getNorthEast();   //可视区域右上角
    var keyword =$("#keyword").val();
    var param = {};
    param.keyword = keyword;
    param.swlng = bssw.lng;
    param.swlat = bssw.lat;
    param.nelng = bsne.lng;
    param.nelat = bsne.lat;
    param.pageIndex = pageIndex;
    param.pageLimit = PAGE_LIMIT;
    $.ajax({
        type: "POST",
        url: 'chargenet/chargenet_queryStationByPage.action',
        data: param,
        dataType:'json',
        cache: false,
        success: function(data,options){
            if(data.success){
            	var page = data.page;
            	var list = page.root;
            	pageCount = Math.floor(page.totalProperty/PAGE_LIMIT) + (page.totalProperty%PAGE_LIMIT>0?1:0);
            	if(page.totalProperty == 0){
            		$("#stationListGrop").html('');
            	}
            	if(list == null || list.length == 0){
            		hideLoadMore();
            	}else{
            		createStationListGroup(list);
            		if(pageIndex == pageCount && list.length <= PAGE_LIMIT){
            			hideLoadMore();
            		}
            	}
            }
            
        }
    });
}

function createStationListGroup(list){
    var html='';
    $.each(list, function(i, item){
    	if(selectedStationid != null && selectedStationid == item.id){
    		html+='<li id="id_'+item.id+'" class="list-group-item active" onclick="onListGroupItemClick(this);">';
    	}else{
    		html+='<li id="id_'+item.id+'" class="list-group-item" onclick="onListGroupItemClick(this);">';
    	}
        html+='<span style="cursor:pointer"';
    	html +='onmouseover="mouseover(\''+item.lng+','+item.lat+','+item.id+'\');"';
    	html +='onmouseout="mouseout(\''+item.lng+','+item.lat+','+item.id+'\');"';
    	html +='onclick="mouseclick(\''+item.lng+','+item.lat+','+item.id+'\');">'
    	html +='<h5>'+item.rownum+'. <span style="color:#0a94f2">'+item.stationName+'<span></h5>';
    	html +='<h6>'+item.address+'</h6>';
        html+='</span>';
        html+='</li>';
    });
    if(pageIndex == 1){
		$("#stationListGrop").html('');
		$("#stationListGrop").html(html); 
    }else{
    	$("#stationListGrop li:last-child").remove();
		$("#stationListGrop").append(html); 
	}
    var moreHtml = '<li><div style="text-align: center;"><button id="loadMoreBtn" class="loadMoreBtn" onclick="loadMore()">加载更多</button><p id="noMoreText" class="noMoreText hidden">没有更多内容</p></div></li>';
    $("#stationListGrop").append(moreHtml); 
}

var selectedStationid;
function onListGroupItemClick(item){
	$('ul.list-group > li').removeClass('active');
	$(item).addClass('active');
	selectedStationid = $(item).attr("id").split("_")[1];//记住选中的点id,以为地图移动，页面也重新加载
}

function queryStationDetail(marker){
	if(marker == null)return;
	 $.ajax({
	        type: "POST",
	        url: 'chargenet/chargenet_queryStationIdleNum.action',
	        data: {stationid:marker.id,tm:new Date().getTime()},
	        dataType:'json',
	        cache: false,
	        success: function(data,options){
	            if(data.success){
	            	marker.idlenum = data.stationIdleNum;
	            	marker.openInfoWindow(createInfoWindow(marker));
	            	$("#stationResult").show();
	                $("#pileResult").hide();
	            }else{
	            }
	        }
	    });
}

//点击查看桩详情
function onDetailClick(id){
	if(id == null || id == "")return;
	 $.ajax({
	        type: "POST",
	        url: 'chargenet/chargenet_queryPileStatus.action',
	        data: {stationid:id,tm:new Date().getTime()},
	        dataType:'json',
	        cache: false,
	        success: function(data,options){
	            if(data.success){
	            	 $("#pileListGrop").css("height",getContentHeight()-60);
	            	createPileListGroup(data.pileStatusList);
	                $("#stationResult").hide();
	                $("#pileResult").show();
	            }else{
	            }
	        }
	    });
}

function createPileListGroup(list){
	$("#pileListGrop").html('');
    var html='';
    if(list == null || list.length == 0){
    	html+='<li class="list-group-item text-center">暂无充电桩数据.</li>';
    	$("#pileListGrop").html(html);
    	return;
    }
    $.each(list, function(i, item){
    	html+='<li class="list-group-item">';
    	html +='<h5><strong>'+item.pilename+'</strong>';
    	html +='<div style="float:right;background-color:#87c5e2;color:#fff">'+item.paywaydesc+'</div></h5>';
    	html +='<div style="float:right">'
    	html +='<span>'+item.currenttypedesc+'</span> , ';
    	html +='<span>'+item.powertypedesc+'</span>';
    	html +='</div>';
    	html += '<h6>目前状态 : ';
    	if(item.status == PILE_RUNSTATUS_IDLE){//空闲
    		html +='<span style="color:green;">'+item.statusdisc+'</span>';
    	}else if(item.status == PILE_RUNSTATUS_APPOINTMENT){//预约中
    		html +='<span style="color:#ff8c00;">'+item.statusdisc+'</span>';
    	}else if(item.status == PILE_RUNSTATUS_CHARGING){//充电中
    		html +='<span style="color:#7ec0ee;">'+item.statusdisc+'</span>';
    	}else if(item.status == PILE_RUNSTATUS_OFFLINE){//离线
    		html +='<span style="color:#6e6e6e;">'+item.statusdisc+'</span>';
    	}else if(item.status == PILE_RUNSTATUS_FAULT){//故障
    		html +='<span style="color:red;">'+item.statusdisc+'</span>';
    	}else if(item.status == PILE_RUNSTATUS_UNKNOW){//忙碌中
    		html +='<span>'+item.statusdisc+'</span>';
    	}else{
    		html +='<span>未知</span>';
    	}
        html+='</h6>';
        html+='</li>';
    });
    $("#pileListGrop").html(html);
}

function back(){
	 $("#stationResult").show();
     $("#pileResult").hide();
}

//点击加载更多
function loadMore(){
	pageIndex ++;
	if(pageIndex < 1) pageIndex = 1;
	if(pageIndex > pageCount) {
		pageIndex = pageCount;
		hideLoadMore();
		return;
	}
	if(operateflag == "byrange"){
		queryStationByRangeByPage();
	}else{
		queryStationByPage();
	}
}

//显示“加载更多"
function showLoadMore(){
	$("#loadMoreBtn").removeClass("hidden");
	$("#noMoreText").addClass("hidden");
}
//隐藏"加载更多"
function hideLoadMore(){
	$("#loadMoreBtn").addClass("hidden");
	$("#noMoreText").removeClass("hidden");
}

