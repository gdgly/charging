<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<%@include file="../../common/global/meta.jsp"%>
<%@include file="../../common/global/js.jsp"%>
<%@include file="../head.jsp"%>
<html lang="zh-CN">
<head>
<script type="text/javascript" src="//api.map.baidu.com/api?v=2.0&ak=Gf39Vxv6N9I9pfdb8tyg6GIA"></script>
<style type="text/css">
h5{
color:black;
}
h6{
color:#666666;
}
h5>span{
color:#0a94f2;
}
ul, li {
	margin: 0;
	padding: 0
}

#scrollDiv {
	z-index: 999px;
	width: 550PX;
	height: 152px;
	border-radius: 10px;
	min-height: 30px;
	line-height: 30px;
	border: #ccc 1px solid;
	overflow: hidden;
	position: absolute;
	background-color: #F5F5F5;
	top: 140px;
	left: 88px;
}

#scrollDiv li {
	height: 30px;
	list-style-type: none;
	border-bottom: 1px solid #ccc;
}

#scrollDiv li span {
	padding-left: 10px;
}

.float {
	z-index: 999px;
	width: 350px;
	height: 90px;
	position: absolute;
	top: 188px;
	right: 80px;
	background-color: #F5F5F5;
	border-radius: 5px;
	overflow: hidden;
	opacity:0.9;
}

.floatSearch {
	z-index: 999px;
	border-radius: 10px;
	position: absolute;
	top: 100px;
	right: 38px;
}

.floatClock {
	z-index: 999px;
	width: 50px;
	border-radius: 10px;
	position: absolute;
	top: 190px;
	left: 27px;
	height: 50px;
	background-color: white;
	opacity:0.9;
}
</style>
</head>
<body>
	<div class="container" id="mapDiv"
		style="width: 100%; height: 720px; padding-left: 0; padding-right: 0; margin-top: 70px;">
		<div id="allmap" style="width: 100%; height: 100%;"></div>
	</div>
	<div class="btn-group btn-group-sm floatSearch box-shadow">
		<button title="刷新" id="refreshMapBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-refresh"></span></button>
		<button title="设备监控" id="shoWMonitorListBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-eye-open"></span></button>
		<button title="设备告警" onclick="href('alarmEvent/searchEvents.action');" type="button" class="btn btn-default"><span class="glyphicon glyphicon-warning-sign"></span></button>
	</div>
	<!-- 事件滚动 start-->
	<div id="scrollDiv" class="box-shadow text-center hide">
		<ul>
		<s:iterator id="item" value="#request.alarmEventList">
			<li>
				<div class="col-sm-5" title="<s:property value="allStationAndPileNameDesc"/>"><s:property value="shortStationAndPileNameDesc"/></div>
				<div class="col-sm-3">
					<span style="color: red;"><s:property value="described"/></span>
				</div>
				<div class="col-sm-4">
					<span style="color: #6E6E6E;"><s:property value="eventTimeDesc"/></span>
				</div>
			</li>
		</s:iterator>
		</ul>

	</div>
	<!-- 事件滚动 end-->
	<!-- 小闹钟 start-->
	<div class="floatClock box-shadow text-center " id="clock">
		<img id="clockImg" src="${imgUrl}res/img/bussiness/alert_1.png"  style="padding-top: 17px;" /> <span id="eventNum" class="badge pull-right hide" style="position: absolute; top: 8px; left: 25px; background-color: red;">0</span>
	</div>
	<!-- 小闹钟 end-->
	<div class="float box-shadow" id="detailPanel">
		<div style="height: 90px;padding-top: 26px;border-bottom: 1px solid #ccc;">
			<div class="col-sm-offset-1 col-sm-5">
				空闲：<span style="color: #1ED538;">${freePile}</span>个
			</div>
			<div class="col-sm-6">
				充电中：<span style="color: #3EA2CC;">${chaPile}</span>个
			</div>
			<div class="col-sm-offset-1  col-sm-5">
				离线：<span style="color: #6E6E6E;">${offPile}</span>个
			</div>
			<div class="col-sm-6">
				预约中：<span style="color: #FF8D18;">${appPile}</span>个
			</div>
		</div>
		
		<div id="info" style="height: 81%;">
	 	<div id="stationResult" style="overflow-y: auto;height: 100%;">
		<s:iterator id="item" status="statu" value="#request.stationList">
		<a href="javascript:;" onclick="clickListStation(this);" class="list-group-item" stationId=<s:property value='id'/> lng=<s:property value='lng'/> lat=<s:property value='lat'/> >
		<h5><s:property value="#statu.index+1"/>.
		<span><s:property value="stationName"/> </span>
		</h5>
		<h6><s:property value="address"/> </h6>
		</a>
		</s:iterator>
		</div> 
		<!-- 详情 start -->
		<form class="form-horizontal hide" role="form" id="pileResult">
		</form>
		<!-- 详情 end -->
		</div>
	</div>
</body>
<script type="text/javascript">
setBowHeight2();
$("#allmap").css("height",$(document.body).height());
	var jsonList = ${jsonList};
	var markers=[];
	var map;
	var myIcon = new BMap.Icon(IMG_SRC+"res/img/mark/map_sign_1.png", new BMap.Size(32,46));
	var myIcon2 = new BMap.Icon(IMG_SRC+"res/img/mark/map_sign_2.png", new BMap.Size(40,45));
	var levelSize =15;
	var param={};
	var html;
	var timerID;
	var opts = {
			//width : 250,     // 信息窗口宽度
			//height: 80,     // 信息窗口高度
			title : "信息窗口" , // 信息窗口标题
			enableMessage:true//设置允许信息窗发送短息
		   };

	
	function getOnlinePileList(point){
			param={};
			param.stationId = point.stationBean.id;
			$.post("monitor_searchPileListByStationId.action",param,function(data){
				 if(data.userLoginStatus){
		    		  checkLoginStatus(data,true);
		    		  return;
		    	  }
			 var content = getContent(data.result.pileList);
				openInfo(content,point); 
		});
	}
	function showInfo(obj){
		var onlineStatus = $(obj).attr("onlineStatus");
		var pileName = getNotNullData($(obj).attr("pileName"));
		var chaLen = getNotNullData($(obj).attr("chaLen"));
		var chaPower = getNotNullData($(obj).attr("chaPower"));
		var chaMoney = getNotNullData($(obj).attr("chaMoney"));
		var outV = getNotNullData($(obj).attr("outV"));
		var outI = getNotNullData($(obj).attr("outI"));
		var username = getNotNullData($(obj).attr("username"));
		var phone = getNotNullData($(obj).attr("phone"));
		var appEndTimeDesc = $(obj).attr("appEndTimeDesc");
		html = '<div class="form-group">'
			+'<label class="col-sm-5 control-label">桩名称：</label>'
			+'<div class="col-sm-6 text-center" style="padding-top: 8px;">'
			+'<span>'+pileName+'</span>'
			+'</div>'
			+'</div>';
		$(obj).parent().find("tr").each(function(index,data){
			$(data).css("backgroundColor","white");
		});
		$(obj).css("backgroundColor","#EBEBEB");
		$("#stationResult").addClass("hide");
			if(1 == onlineStatus){//空闲
				html = '<div class="form-group">'
					+'<label class="col-sm-5 control-label">桩名称：</label>'
					+'<div class="col-sm-6 text-center" style="padding-top: 8px;">'
					+pileName
					+'</div>'
					+'</div>';
				html = html+ '<div class="form-group">'
					+'<label class="col-sm-5 control-label">桩状态：</label>'
					+'<div class="col-sm-6 text-center" style="padding-top: 8px;">'
					+'<span style="color: #1ED538;">空闲</span>'
					+'</div>'
					+'</div>';
			}
			else if(2 == onlineStatus){//充电
				html = '<div class="form-group">'
					+'<label class="col-sm-5 control-label">桩名称：</label>'
					+'<div class="col-sm-6 text-center" style="padding-top: 8px;">'
					+pileName
					+'</div>'
					+'</div>';
				html = html+'<div class="form-group">'
					+'<label class="col-sm-5 control-label">桩状态：</label>'
					+'<div class="col-sm-6 text-center" style="padding-top: 8px;">'
					+'<span style="color: #3EA2CC;">充电中</span>'
					+'</div>'
					+'</div>';
				html = html+'<div class="form-group">'
					+'<label class="col-sm-5 control-label">用户名：</label>'
					+'<div class="col-sm-6 text-center" style="padding-top: 8px;">'
					+username
					+'</div>'
					+'</div>';
				html = html+'<div class="form-group">'
					+'<label class="col-sm-5 control-label">手机：</label>'
					+'<div class="col-sm-6 text-center" style="padding-top: 8px;">'
					+phone
					+'</div>'
					+'</div>';	
				html = html+'<div class="form-group">'
					+'<label class="col-sm-5 control-label">充电时长：</label>'
					+'<div class="col-sm-6 text-center" style="padding-top: 8px;">'
					+'<span>'+chaLen+'</span>'
					+'</div>'
					+'</div>';
				html = html+'<div class="form-group">'
					+'<label class="col-sm-5 control-label">充电电量：</label>'
					+'<div class="col-sm-6 text-center" style="padding-top: 8px;">'
					+'<span>'+chaPower+'</span>'
					+'</div>'
					+'</div>';
				html = html+'<div class="form-group">'
					+'<label class="col-sm-5 control-label">充电金额：</label>'
					+'<div class="col-sm-6 text-center" style="padding-top: 8px;">'
					+'<span>'+chaMoney+'</span>'
					+'</div>'
					+'</div>';
				html = html+'<div class="form-group">'
					+'<label class="col-sm-5 control-label">输出电压：</label>'
					+'<div class="col-sm-6 text-center" style="padding-top: 8px;">'
					+'<p><span style="color:red;">'+outV+'</span> V</p>'
					+'</div>'
					+'</div>';
				html = html+'<div class="form-group">'
					+'<label class="col-sm-5 control-label">输出电流：</label>'
					+'<div class="col-sm-6 text-center" style="padding-top: 8px;">'
					+'<p><span style="color:red;">'+outI+'</span> A</p>'
					+'</div>'
					+'</div>';
			}
			else if(3 == onlineStatus){//预约
				html = '<div class="form-group">'
					+'<label class="col-sm-5 control-label">桩名称：</label>'
					+'<div class="col-sm-6 text-center" style="padding-top: 8px;">'
					+pileName
					+'</div>'
					+'</div>';
				html = html+'<div class="form-group">'
					+'<label class="col-sm-5 control-label">桩状态：</label>'
					+'<div class="col-sm-6 text-center" style="padding-top: 8px;">'
					+'<span style="color: #FF8D18;">预约中</span>'
					+'</div>'
					+'</div>';
				html = html+'<div class="form-group">'
					+'<label class="col-sm-5 control-label">用户名：</label>'
					+'<div class="col-sm-6 text-center" style="padding-top: 8px;">'
					+username
					+'</div>'
					+'</div>';
				html = html+'<div class="form-group">'
					+'<label class="col-sm-5 control-label">手机：</label>'
					+'<div class="col-sm-6 text-center" style="padding-top: 8px;">'
					+phone
					+'</div>'
					+'</div>';
				html = html+'<div class="form-group">'
					+'<label class="col-sm-5 control-label">预约截止时间：</label>'
					+'<div class="col-sm-6 text-center" style="padding-top: 8px;">'
					+appEndTimeDesc
					+'</div>'
					+'</div>';
					
			}else if(4 == onlineStatus){//离线
				html = '<div class="form-group">'
					+'<label class="col-sm-5 control-label">桩名称：</label>'
					+'<div class="col-sm-6 text-center" style="padding-top: 8px;">'
					+pileName
					+'</div>'
					+'</div>';
				html = html+'<div class="form-group">'
					+'<label class="col-sm-5 control-label">桩状态：</label>'
					+'<div class="col-sm-6 text-center" style="padding-top: 8px;">'
					+'<span style="color: #6E6E6E;">离线</span>'
					+'</div>'
					+'</div>';
			}
			else{//未知
				html = '<div class="form-group">'
					+'<label class="col-sm-5 control-label">桩名称：</label>'
					+'<div class="col-sm-6 text-center" style="padding-top: 8px;">'
					+pileName
					+'</div>'
					+'</div>';
				html = html+ '<div class="form-group">'
					+'<label class="col-sm-5 control-label">桩状态：</label>'
					+'<div class="col-sm-6 text-center" style="padding-top: 8px;">'
					+'<span style="color: #6E6E6E;">未知</span>'
					+'</div>'
					+'</div>';
			}
			$("#pileResult").html(html).removeClass("hide");
		}
	function getTile(stationBean){
			var temp = "信息窗口";
			if(stationBean){
				temp = "<h4>"
						+"名称："+stationBean.stationName
						+"</h4>"
						+"<p title='"
						+stationBean.address+"' class='help-block'>地址："+mySubString(stationBean.address,15)+"</p>"
						;
			}
			return temp;
		}
		function getContent(pileList){
			var str="无信息";
			if(pileList && pileList.length > 0){
				str="<div style='width:300px;height:100px;overflow: auto;'>"
				     +"<table class='table table-hover'>";
				for(var i=0;i<pileList.length;i++){
					str=str+"<tr pileName='"
						   +pileList[i].pilename+"' "
						   +"chaLen='"+getDefaultData(pileList[i].chalen,0)+"' "
						   +"chaPower='"+return2Num(getDefaultData(pileList[i].chapower,0))+"' "
						   +"chaMoney='"+return2Num(getDefaultData(pileList[i].money,0))+"' "
						   +"outV='"+return2Num(getDefaultData(pileList[i].outv,0))+"' "
						   +"outI='"+return2Num(getDefaultData(pileList[i].outi,0))+"' "
						   +"username='"+getDefaultData(pileList[i].username,"")+"' "
						   +"phone='"+getDefaultData(pileList[i].phone,"")+"' "  
						   +"appEndTimeDesc='"+pileList[i].appEndTimeDesc+"' "
						   +"onclick='showInfo(this)' onlineStatus='"
						   +pileList[i].status+"'>"
						   +"<td>"
					       +pileList[i].pilename
					       +"</td>"
					       +"<td>"
		    	    	   +pileList[i].powertypedesc
		    	    	   +"</td>";
		    	    	   temp = pileList[i].status
		    	    	   if(temp == 1){//空闲
		    	    		  str = str +  "<td style='color: #1ED538;'>"+getNotNullData(pileList[i].statusdisc)+"</td>";
		    	    	   }else if(temp == 2){//充电
		    	    		   str = str +  "<td style='color: #3EA2CC;'>"+getNotNullData(pileList[i].statusdisc)+"</td>";
		    	    	   }else if(temp == 3){//预约
		    	    		   str = str +  "<td style='color: #FF8D18;'>"+getNotNullData(pileList[i].statusdisc)+"</td>";
		    	    	   }else if(temp == 4){//离线
		    	    		   str = str +  "<td style='color: #6E6E6E;'>"+getNotNullData(pileList[i].statusdisc)+"</td>";
		    	    	   }else{//未知
		    	    		   str = str +  "<td style='color: #6E6E6E;'>"+getNotNullData(pileList[i].statusdisc)+"</td>";
		    	    	   }
		    	    	  str = str + "</tr>";
				}
				str=str+"</table></div>";
			}
			else{
				str =	"<div style='width:300px;height:50px;'>"
						+"<p class='text-center help-block'>"+"无信息"+"</p>"
						+"</div>";
			}
		return str;
		}
	function initIcon(markers){
			for(var y=0;y<markers.length;y++){
				markers[y].setIcon(myIcon);
			}
			
		}
	function openInfo(content,point){
			opts.title=getTile(point.stationBean);
			var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
			map.openInfoWindow(infoWindow,point); //开启信息窗口
		}
function clickListStation(obj){
	$("#stationResult").find("a").removeClass("active");
	$(obj).addClass("active");
	//map.centerAndZoom(new BMap.Point(jsonList[1].lng,jsonList[1].lat) , 15);
	lng = $(obj).attr("lng");
	lat = $(obj).attr("lat");
	stationId = $(obj).attr("stationId");
	 for(var i in markers){
		if(markers[i].point.lng == lng && markers[i].point.lat == lat){
			initIcon(markers);
			markers[i].setIcon(myIcon2); 
			map.centerAndZoom(markers[i].point, 15);
			opts.title=getTile(markers[i].stationBean);
			param={};
			param.stationId = markers[i].stationBean.id;
			$.post("monitor_searchPileListByStationId.action",param,function(data){
				  if(data.userLoginStatus){
		    		  checkLoginStatus(data,true);
		    		  return;
		    	  }
				 	var content = getContent(data.result.pileList);
					var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
					map.openInfoWindow(infoWindow,markers[i].point); //开启信息窗口
			});
			
			
			//getOnlinePileList(markers[i].point);
			break;
		}
	}
	
}

(function($){ 
	$.fn.extend({ 
	Scroll:function(opt,callback){ 
	//参数初始化 
	if(!opt) var opt={};
	var _this=this.eq(0).find("ul:first"); 
	var lineH=_this.find("li:first").height(), //获取行高 
	line=opt.line?parseInt(opt.line,10):parseInt(this.height()/lineH,10), //每次滚动的行数，默认为一屏，即父容器高度 		
	speed=opt.speed?parseInt(opt.speed,10):500, //卷动速度，数值越大，速度越慢（毫秒） 
	timer=opt.timer?parseInt(opt.timer,10):3000; //滚动的时间间隔（毫秒） 
	if(line==0) line=1; 
	var upHeight=0-line*lineH; 
	//滚动函数 
	scrollUp=function(){ 
	_this.animate({ 
	marginTop:upHeight 
	},speed,function(){ 
	for(i=1;i<=line;i++){ 
	_this.find("li:first").appendTo(_this); 
	} 
	_this.css({marginTop:0}); 
	}); 
	} 
	//鼠标事件绑定 
	_this.hover(function(){ 
	clearInterval(timerID); 
	},function(){ 
	timerID=setInterval("scrollUp()",timer); 
	}).mouseout(); 
	} 
	}) 
	})(jQuery); 
	$(document).ready(function(){ 
		$("#clock").on("click",function(){
			if(eventNum > 0){
			if($("#scrollDiv").hasClass("hide")){
				$("#clockImg").attr("src",IMG_SRC+"res/img/bussiness/alert.png");
				if(!$("#eventNum").hasClass("hide")){
					$("#eventNum").addClass("hide");
				}
				$("#scrollDiv").removeClass("hide");
				
			}
			else{
				$("#clockImg").attr("src",IMG_SRC+"res/img/bussiness/alert_1.png");
				$("#scrollDiv").addClass("hide");
			}
			}
		});
		
		var eventNum = $("#scrollDiv").find("ul").find("li").size();
            if( eventNum > 5){
            	$("#scrollDiv").Scroll({line:1,speed:500,timer:2000});
            }
            if(eventNum > 0){
            	$("#eventNum").text(eventNum).removeClass("hide");
            }
	}); 


 	$(function(){
 		var jsonPobChargingStation = ${jsonPobChargingStation};
 		var jsonPileList = ${jsonPileList};
 		// 百度地图API功能
  	    map = new BMap.Map("allmap",{
  			enableMapClick : false
  		});    // 创建Map实例
  		map.addEventListener('click', function(){
  		   $("#pileResult").addClass("hide");
  		   $("#stationResult").removeClass("hide");
  		});
  		
  	//地图智能搜索
  		function setPlace(myValue){
  			function myFun(){
  				var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
  			
  				map.centerAndZoom(pp, levelSize);
  				//map.addOverlay(new BMap.Marker(pp));    //添加标注
  			}
  			var local = new BMap.LocalSearch(map, { //智能搜索
  			  onSearchComplete: myFun
  			});
  			local.search(myValue);
  		}
 	
 	// 百度地图API功能
 		/* function myyFun(result){
 			var cityName = result.name;
 			if(cityName){
 				map.centerAndZoom(cityName, 16);
 			}
 			else{
 				map.centerAndZoom("北京市", 16);	
 			}
 			
 		}
  		
  		
  		var myCity = new BMap.LocalCity();//定位当前城市
  		myCity.get(myyFun);   */
  		if(jsonList.length>0){
  			map.centerAndZoom(new BMap.Point(jsonList[0].lng,jsonList[0].lat) , 15);
  		}
  		  else{
  			map.centerAndZoom("杭州",levelSize);
  		}  
  		
  		
 		//map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
 		map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放 
 

 		function addClickHandler(marker,point){
 			marker.addEventListener("click",function(e){
 				initIcon(markers);
 				marker.setIcon(myIcon2);
 				getOnlinePileList(point);
 				
 				});
 		}
 		
 		
 		function addMarker(point){
 			var marker = new BMap.Marker(point,{icon:myIcon});
 			marker.stationBean = point.stationBean;
 			var num = parseInt(point.stationBean.fastNum)+parseInt(point.stationBean.slowNum);
 			if(!num){
 				num=0;
 			}
 			
 			var leftRight = 7;
 			if(num < 10){
 				leftRight = LABEL_OFFSET_X1;
 			}
 			else if(num >= 10 && num < 100){
 				leftRight = LABEL_OFFSET_X2;
 			}
 			else if(num >= 100 && num < 1000){
 				leftRight = LABEL_OFFSET_X3;
 			}
 			var label = new BMap.Label(num,{offset:new BMap.Size(leftRight,LABEL_OFFSET_Y)});
 			label.setStyle(LABEL_STYLE);
 			marker.setLabel(label);
 			//marker.point.stationBean = point.stationBean;
 			markers.push(marker);
 			addClickHandler(marker,point);
 			map.addOverlay(marker);
 			}
 		
          function initStations(){
        	  markers=[];
        	  if(jsonList && jsonList.length > 0){
        		  $("#detailPanel").css("height","515px");
        		for(var i=0;i<jsonList.length;i++){
        			var point = new BMap.Point(jsonList[i].lng,jsonList[i].lat);
        			point.stationBean=jsonList[i];
        			addMarker(point);
        		} 
         		if(jsonPobChargingStation && jsonPobChargingStation.id > 0){
        	 		for(var i in markers){
        				if((markers[i].point.lng == jsonPobChargingStation.lng)&&(markers[i].point.lat == jsonPobChargingStation.lat)){
        					 map.panTo(markers[i].point);   
        					 initIcon(markers[i]);
        					 markers[i].setIcon(myIcon2);
        					 openInfo(getContent(jsonPileList),markers[i].point);
        					 break;
        				}
        			} 
        	 		  	$("#stationResult").find("a").removeClass("active");
        	 		 	$("#stationResult").find("a").each(function(index,data){
        	 			 tempStationId = parseInt($(data).attr("stationid"));
        	 			  if(jsonPobChargingStation.id == tempStationId){
        	 				$(data).addClass("active"); 
        	 			 } 
        	 			
        	 		 });
        		} 
          }
          }

          function initSearch(){
        	  $("#searchName").val("");
        	 // $('#info form').addClass("hide");
        	  $("#stationResult").find("a").removeClass("active");
          }
          initSearch();//初始化搜索框
          initStations();//初始化标签
        
          //事件绑定
          $("#searchBtn").on("click",function(){
        	  setPlace($("#searchName").val());
          });
         //跳转设备实时状态列表
		  $("#shoWMonitorListBtn").on("click",function(){
			href("monitor/searchAllOnlinePileList.action");
		  });
          $("#refreshMapBtn").on("click",function(){
        	 href("monitor/monitorMap.action");
          });
	}); 
</script>
</html>

