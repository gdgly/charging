/**
 * 个人设备地图监视
 */
	var markers=[];
	var map;
	var myIcon = new BMap.Icon(IMG_SRC+"res/img/mark/map_sign_1.png", new BMap.Size(32,46));
	var myIcon2 = new BMap.Icon(IMG_SRC+"res/img/mark/map_sign_2.png", new BMap.Size(40,45));
	var myIcon3 = new BMap.Icon(IMG_SRC+"res/img/mark/map_sign_3.png", new BMap.Size(32,46));
	var levelSize =15;
	var param={};
	var html;
	var timerID;
	var searchPileListByStationIdUrl = "run/monitor_searchPileListByStationId.action";
	var opts = {
			//width : 250,     // 信息窗口宽度
			//height: 80,     // 信息窗口高度
			title : "信息窗口" , // 信息窗口标题
			enableMessage:true//设置允许信息窗发送短息
		   };
	function getOnlinePileList(point){
			param={};
			param.stationId = point.stationBean.id;
			$.post(searchPileListByStationIdUrl,param,function(data){
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
		}else if(5 == onlineStatus){
			html = '<div class="form-group">'
				+'<label class="col-sm-5 control-label">桩名称：</label>'
				+'<div class="col-sm-6 text-center" style="padding-top: 8px;">'
				+pileName
				+'</div>'
				+'</div>';
			html = html+'<div class="form-group">'
				+'<label class="col-sm-5 control-label">桩状态：</label>'
				+'<div class="col-sm-6 text-center" style="padding-top: 8px;">'
				+'<span style="color: #FF8D18;">故障</span>'
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
		temp = "<h4 style='font-family: Microsoft YaHei ! important;'>"
				+"名称："+stationBean.stationName
				+"</h4>"
				+"<p  style='font-family: Microsoft YaHei ! important;' title='"
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
				   +"chaPower='"+return2Num(getDefaultData(pileList[i].chapower,"0"))+"' "
				   +"chaMoney='"+return2Num(getDefaultData(pileList[i].money,"0"))+"' "
				   +"outV='"+return2Num(getDefaultData(pileList[i].outv,"0"))+"' "
				   +"outI='"+return2Num(getDefaultData(pileList[i].outi,"0"))+"' "
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
/**
 * 初始化标签图标
 * @param markers
 */
function initIcon(markers){
	for(var y=0;y<markers.length;y++){
		if(markers[y].stationBean.stationToType == 1){
			markers[y].setIcon(myIcon);
		}else if(markers[y].stationBean.stationToType == 2){
			markers[y].setIcon(myIcon3);
		}
		
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
			$.post(searchPileListByStationIdUrl,param,function(data){
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


$(function(){
	setContentHeight($("#allmap"));//设置高度
	map = new BMap.Map("allmap",{
	  	enableMapClick : false
	}); 
	map.addEventListener('click', function(){
		   $("#pileResult").addClass("hide");
		   $("#stationResult").removeClass("hide");
		});
	if(jsonList.length>0){
			map.centerAndZoom(new BMap.Point(jsonList[0].lng,jsonList[0].lat) , 15);
		}
		  else{
			map.centerAndZoom("杭州",levelSize);
		}  
	map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放 
	function addClickHandler(marker,point){
			marker.addEventListener("click",function(e){
				initIcon(markers);
				marker.setIcon(myIcon2);
				getOnlinePileList(point);
				
				});
		}
		

	/**
	 * 添加标签
	 * @param point
	 */
	function addMarker(point){
		var marker;
		if(point.stationBean.stationToType == 1){
			marker = new BMap.Marker(point,{icon:myIcon});
		}else if(point.stationBean.stationToType == 2){
			marker = new BMap.Marker(point,{icon:myIcon3});
		}
			//var marker = new BMap.Marker(point,{icon:myIcon});
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
		/**
		 * 初始化充电点
		 */
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
      //跳转设备实时状态列表
	  $("#shoWMonitorListBtn").on("click",function(){
		href("run/monitorList.action");
	  });
      $("#refreshMapBtn").on("click",function(){
    	  reload();
      });
      
})
	

