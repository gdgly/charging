var querySingleStaPileUrl = "run/monitor_querySingleStaPile.action";
var params={};
var CHARGING = 2;//充电中
var FAULT = 5;//故障
var IDLE = 1;//空闲
var BUSYING = 0;//忙碌中
var OFFLINE = 4;//离线中
var FAST = 1;
var SLOW =2;
var DC=2;//直流
var AC=1;//交流
function querySingleStation(stationId){
	params={};
	if(stationId && stationId > 0){
		params.stationId = 	stationId;
		params.pileSta = $("#pileSta").val(-1);
		params.pileCode = $("#pileCode").val("");
	}else{
		params.stationId = 	$("#selectStationId").val();	
	}
	params.pileSta = $("#pileSta").val();
	params.pileCode = $("#pileCode").val();
	$.ajax({
		type:"POST",
		url:querySingleStaPileUrl,
		data:params,
		dataType:'json',
        cache: false,
        beforeSend:function(){$("#loading").removeClass("hide");},
        complete:function(){
	    	  $("#loading").addClass("hide"); 
	      },
        success: function(data){
        	if(data.selectStationId > 0){
        		$("#staName").text(data.stationName);
        		$("#selectStationId").val(data.selectStationId);
        		$("#fastPileCount").text(data.fastCount);
        		$("#slowPileCount").text(data.slowCount);
        		$("#pileSum").text(data.fastCount+data.slowCount);
        		$("#idleCount").text(data.idleCount);
        		$("#chargingCount").text(data.chargingCount);
        		$("#unLineCount").text(data.unLineCount);
        		$("#faultCount").text(data.faultCount);
        		$("#busyCount").text(data.busyCount);
        		createData(data.pileStatusList);
        	}
         }
     });

}
function createData(dataList){
	$("#pillBox").empty()
	
	if(dataList && dataList.length > 0){
		$(dataList).each(function(index,item){
			html='';
			temp = item.status;
			html += '<div class="pilldiv">';
			html += '<div class="pilldivColor ';
			//状态封装start
			if(temp == IDLE){//空闲
				html += 'statecolorH"></div>';
				html += '<div class="nameCount">空闲</div>';
			}else if(temp == CHARGING){//充电中
				html += 'statecolorM"></div>';
				html += '<div class="nameCount">充电中</div>';
			}else if(temp == BUSYING){//忙碌中
				html += 'statecolorY"></div>';
				html += '<div class="nameCount">忙碌中</div>';
			}else if(temp == OFFLINE){//离线
				html += 'statecolorZ"></div>';
				html += '<div class="nameCount">离线</div>';
			}else if(temp == FAULT){//故障
				html += 'statecolorL"></div>';
				html += '<div class="nameCount">故障</div>';
			}
			//状态封装end
			
			//实时数据封装start
			html += '<div class="pilldivS">';
			html += '<div class="pilldivInfo">';
			html += '<span>桩名称:'+getDefaultData(item.pilename,'--')+'</span>';
			html += '</div>';
			//1
			html += '<div class="pilldivInfo">';
			html += '<div class="redname" title="'+getNotNullData(item.pilecode)+'">编号:'+mySubString(getDefaultData(item.pilecode,'--'),32)+'</div>';
			html += '</div>';
			html += '<div class="pilldivInfo">';
			html += '<div class="redname" title="'+getNotNullData(item.pilemodel)+'">终端型号:'+mySubString(getDefaultData(item.pilemodel,'--'),32)+'</div>';
			html += '</div>';
			//2	
			html += '<div class="pilldivInfo">';
			html += '<div class="redname">终端类型:'
				if(FAST == item.piletype){
					html += '快充';
				}else if(SLOW == item.piletype){
					html += '慢充';
				}else{
					html += '--';
				}
				html +='</div>';
			html += '<div class="rednameRight">充电方式:';
			if(DC == item.chaway){
				html += '直流';
			}else if(AC == item.chaway){
				html += '交流';
			}else{
				html += '--';
			}
			html += '</div>';
			html += '</div>';
			//3
			html += '<div class="pilldivInfo">';
			html += '<div class="redname">电量:'+ getDefaultData(item.chapower,'--')+'</div>';
			html += '<div class="rednameRight">功率(W):'+getRate(item.outi,item.outv)+'</div>';
			html += '</div>';
			//4
			html += '<div class="pilldivInfo">';
			html += '<div class="redname">电压(V):'+getDefaultData(item.outv,'--')+'</div>';
			html += '<div class="rednameRight">电流(A):'+getDefaultData(item.outi,'--')+'</div>';
			html += '</div>';
			//5
			html += '<div class="pilldivInfo">';
			html += '<div class="redname" title="'+getNotNullData(item.starttimeStr)+'">充电时间:'+mySubString(getDefaultData(item.starttimeStr,'--'),5)+'</div>';
			html += '<div class="rednameRight">充电时长(分):'+getDefaultData(item.chalen,'--')+'</div>';
			//6
			if(item.pileToType == 1){
				html += '<div class="pilldivInfo">';
				html += '<div class="redname">当前SOC(%):'+getDefaultData(item.soc,'--')+'</div>';
				html += '<div class="rednameRight">车牌号:'+getDefaultData(item.plateNum,'--')+'</div>';
				html += '</div>';	
				html += '<div class="pilldivInfo">';
				html += '<div class="redname">已充金额(元):'+getDefaultData(item.money,'--')+'</div>';
				html += '</div>';	
			}
			//实时数据封装end
			html += '</div>';
			html += '</div>';
			html += '<button class="pilldivButton" disabled="disabled"><span class="glyphicon glyphicon-flash"></span></button>';
			html += '</div>';
			$("#pillBox").append(html);
		});
		
	}else{
		$("#pillBox").append("<p class='text-center' style='font-size:30px;'>无数据</p>");
	}
}

$(function(){
	querySingleStation();
	$("#singleStaSearchBtn").on("click",function(){
		querySingleStation();
	});
	$("#singleStaResetBtn").on("click",function(){
		params.pileSta = $("#pileSta").val(-1);
		params.pileCode = $("#pileCode").val("");
		querySingleStation();
	});
	$("#singleStaFreshBtn").on("click",function(){
		querySingleStation();
	});
	$("#pileSta").on("change",function(){
		querySingleStation();
	});
})
