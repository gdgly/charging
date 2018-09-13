var param={};
var dataa=[];
var tempObj={};
var SINGLE_PHASE='1';//单相
var THREE_PHASE='2';//三相
$(document).ready(function(){
	initParams();
	queryStationList();
});

function initParams(){
	initArea($("#province"), $("#city"));//初始化省市
	initPagingToolbar(queryStationList);
	//图表刷新按钮
	
	$("#refreshPileChartBtn").on('click',function(){
		tempPileId = $("#pileIDForChart").val();
		tempPileName = $("#pileNameForChart").val();
		pileType = $("#pileTypeForChart").val();
		if(tempPileId > 0){
			queryPileChart(tempPileId,tempPileName,pileType);
		}else{
			showInfo("请选择充电桩！！");
		}
	});
	
	$("#addDay").on('change',function(){
		tempPileId = $("#pileIDForChart").val();
		tempPileName = $("#pileNameForChart").val();
		pileType = $("#pileTypeForChart").val();
		if(tempPileId > 0){
			queryPileChart(tempPileId,tempPileName,pileType);
		}else{
			showInfo("请选择充电桩！！");
		}
	});
	//
	//充电点列表查询按钮
	$("#queryStationBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryStationList();
	});
	//充电桩列表查询按钮
	$("#queryPileBtn").on("click",function(){
		queryPileList();
	});
	//充电桩列表返回按钮
	$("#backStationListBtn").on("click",function(){
		showResult("showstationlist");
		queryStationList();
	});
	//充电桩状态刷新按钮
	$("#refreshPileStatusBtn").on("click",function(){
		queryPileStatus();
	});
	//充电桩状态返回按钮
	$("#backPileListBtn").on("click",function(){
		showResult("showpilelist");
		queryPileList();
	});
	//充电桩图表返回按钮
	$("#backPileListForChartBtn").on("click",function(){
		showResult("showpilelist");
		queryPileList();
	});
	//初始化充电点列表导出按钮
	initExportBtn($("#exportStationBtn"),$("#sconditionFrom"),$("#sfileName").val(),$("#stationTable"));
	//初始化充电桩列表导出按钮
	initExportBtn($("#exportPileBtn"),$("#pconditionFrom"),$("#pfileName").val(),$("#pileTable"));
	bindKey13([$("#keyword")],$("#queryStationBtn"));//回车自动搜索
}

function queryStationList(){
	param={};
	param.keyword = $.trim($("#keyword").val());
	param.province = $("#province option:selected").val();
    param.city = $("#city option:selected").val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	
	var tbody = $("#stationTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'run/monitor_queryStationList.action',
		data:param,
		dataType:'json',
        cache: false,
        beforeSend:function(){$("#loading").removeClass("hide");},
        complete:function(){
	    	  $("#loading").addClass("hide"); 
	      },
        success: function(data,options){
        	 if(data.page){
        		 tbody.empty();
        		 var dataList = data.page.root;
        		 var stationparams;
        		 $(dataList).each(function(index,item){
        			 html += '<tr>';
        			 html += '<td>'+item.stationid+'</td>';
        			 html += getTdHtml(item.stationname, 15);
        			 html += '<td>'+item.citydesc+'</td>';
        			 html += getTdHtml(item.busmecname, 15);
        			 html += '<td>'+item.bustypedesc+'</td>';
//        			 html += getTdHtml(item.address, 15);
        			 //电桩数
        			 html += '<td class="text-center">';
        			 html += '<a href="javascript:;" onclick="queryStationDetail(this)"'+
        				 ' stationid='+item.stationid+
        				 ' stationname='+item.stationname+
        				 ' busmecname='+item.busmecname+
        				 ' address='+item.address+
        				 ' pilestatus=-1'+
        				 '>'+getDefaultData(item.pilenum,"0")+'</a>';
        			 html += '</td>';
        			//空闲数
        			 html += '<td class="text-center">'
        			 html += '<a style="color:'+getPileStatusColor(PILE_RUNSTATUS_IDLE)+';" href="javascript:;" onclick="queryStationDetail(this)"'+
        				 ' stationid='+item.stationid+
        				 ' stationname='+item.stationname+
        				 ' busmecname='+item.busmecname+
        				 ' address='+item.address+
        				 ' pilestatus='+PILE_RUNSTATUS_IDLE+
        				 '>'+getDefaultData(item.idlenum,"0")+'</a>';
        			 html += '</td>';
        			//充电数
        			 html += '<td class="text-center">'
        			 html += '<a style="color:'+getPileStatusColor(PILE_RUNSTATUS_CHARGING)+';" href="javascript:;" onclick="queryStationDetail(this)"'+
        				 ' stationid='+item.stationid+
        				 ' stationname='+item.stationname+
        				 ' busmecname='+item.busmecname+
        				 ' address='+item.address+
        				 ' pilestatus='+PILE_RUNSTATUS_CHARGING+
        				 '>'+getDefaultData(item.chargenum,"0")+'</a>';
        			 html += '</td>';
        			//预约数
        			/* html += '<td class="text-center">'
        			 html += '<a style="color:'+getPileStatusColor(PILE_RUNSTATUS_APPOINTMENT)+';" href="javascript:;" onclick="queryStationDetail(this)"'+
        				 ' stationid='+item.stationid+
        				 ' stationname='+item.stationname+
        				 ' busmecname='+item.busmecname+
        				 ' address='+item.address+
        				 ' pilestatus='+PILE_RUNSTATUS_APPOINTMENT+
        				 '>'+getDefaultData(item.appnum,"0")+'</a>';
        			 html += '</td>';*/
        			//离线数
        			 html += '<td class="text-center">'
        			 html += '<a style="color:'+getPileStatusColor(PILE_RUNSTATUS_OFFLINE)+';" href="javascript:;" onclick="queryStationDetail(this)"'+
        				 ' stationid='+item.stationid+
        				 ' stationname='+item.stationname+
        				 ' busmecname='+item.busmecname+
        				 ' address='+item.address+
        				 ' pilestatus='+PILE_RUNSTATUS_OFFLINE+
        				 '>'+getDefaultData(item.offlinenum,"0")+'</a>';
        			 html += '</td>';
        			//故障数
        			 html += '<td class="text-center">'
        			 html += '<a style="color:'+getPileStatusColor(PILE_RUNSTATUS_FAULT)+';" href="javascript:;" onclick="queryStationDetail(this)"'+
        				 ' stationid='+item.stationid+
        				 ' stationname='+item.stationname+
        				 ' busmecname='+item.busmecname+
        				 ' address='+item.address+
        				 ' pilestatus='+PILE_RUNSTATUS_FAULT+
        				 '>'+getDefaultData(item.faultnum,"0")+'</a>';
        			 html += '</td>';
        			//忙碌数
        			 html += '<td class="text-center">'
        			 html += '<a style="color:'+getPileStatusColor(PILE_RUNSTATUS_UNKNOW)+';" href="javascript:;" onclick="queryStationDetail(this)"'+
        				 ' stationid='+item.stationid+
        				 ' stationname='+item.stationname+
        				 ' busmecname='+item.busmecname+
        				 ' address='+item.address+
        				 ' pilestatus='+PILE_RUNSTATUS_UNKNOW+
        				 '>'+getDefaultData(item.unknownum,"0")+'</a>';
        			 html += '</td>';
//        			 html += '<td class="text-right" style="color:'+getPileStatusColor(PILE_RUNSTATUS_IDLE)+';">'+getNotNullData(item.idlenum)+'</td>';//空闲
//        			 html += '<td class="text-right" style="color:'+getPileStatusColor(PILE_RUNSTATUS_CHARGING)+';">'+getNotNullData(item.chargenum)+'</td>';//充电
//        			 html += '<td class="text-right" style="color:'+getPileStatusColor(PILE_RUNSTATUS_APPOINTMENT)+';">'+getNotNullData(item.appnum)+'</td>';//预约
//        			 html += '<td class="text-right" style="color:'+getPileStatusColor(PILE_RUNSTATUS_OFFLINE)+';">'+getNotNullData(item.offlinenum)+'</td>';//离线
//        			 html += '<td class="text-right" style="color:'+getPileStatusColor(PILE_RUNSTATUS_FAULT)+';">'+getNotNullData(item.faultnum)+'</td>';//故障
//        			 html += '<td class="text-right" style="color:'+getPileStatusColor(PILE_RUNSTATUS_UNKNOW)+';">'+getNotNullData(item.unknownum)+'</td>';//忙绿
        			 html += '</tr>';
        		 });
        		 tbody.html(html);
        		 
        		 var currentPage = data.page.startRow/(data.page.endRow-data.page.startRow)+1;
        		 var totalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
        		 setPagingToolbarParams(data.page.totalProperty, totalPage,currentPage);
        	 }else{
            	 showMsg(data.message, data.errormsg);
             }
         }
     });
}

function queryStationDetail(obj){
	$("#stationId").val($(obj).attr("stationid"));
	$("#busMecName").text($(obj).attr("busmecname"));
	$("#stationName").text($(obj).attr("stationname"));
	$("#address").text($(obj).attr("address"));
	$("#pileStatus").val($(obj).attr("pilestatus"));
	showResult("showpilelist");
	queryPileList();
}

function queryPileList(){
	param = {};
	param.stationid = $("#stationId").val();
    param.pilestatus = $('#pileStatus option:selected').val();
	var tbody = $("#pileTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'run/monitor_queryPileList.action',
		data:param,
		dataType:'json',
        cache: false,
        beforeSend:function(){$("#loading").removeClass("hide");},
        complete:function(){
	    	  $("#loading").addClass("hide"); 
	      },
        success: function(data,options){
        	 if(data.pileStatusList){
        		 tbody.empty();
        		 var dataList = data.pileStatusList;
        		 var color;
        		 $(dataList).each(function(index,item){
        		/*	 html += '<tr style="cursor:pointer;" onclick="showDetail(this);" >';*/
        			 html += '<tr>';
        			 html += '<td id="pileid">'+item.pileid+'</td>';
        			 html += '<td id="pilename">'+getNotNullData(item.pilename)+'</td>';
        			 html += '<td id="pilecode">'+getNotNullData(item.pilecode)+'</td>';
        			 html += '<td id="comtype">'+getNotNullData(item.comtypeDesc)+'</td>';
        			 html += '<td id="comaddr">'+getNotNullData(item.comaddr)+'</td>';
        			 html += '<td id="powertype">'+getNotNullData(item.powertypedesc)+'</td>';
        			 html += '<td id="currenttype">'+getNotNullData(item.currenttypedesc)+'</td>';
        			 html += '<td id="payway">'+getNotNullData(item.paywaydesc)+'</td>';
        			 html += '<td class="text-right">'+getNotNullData(item.outv)+' V</td>';
        			 html += '<td class="text-right">'+getNotNullData(item.outi)+' A</td>';
        			 color = getPileStatusColor(item.status);
        			 if(color){
        				 html += '<td style="color:'+color+';">'+getNotNullData(item.statusdisc)+'</td>';
        			 }else{
        				 html += '<td>'+getNotNullData(item.statusdisc)+'</td>';
        			 }
        			 html += '<td><a href="javascript:;" pileId="'+item.pileid+'" pileName="'+getNotNullData(item.pilename)+'" pileType="'+SINGLE_PHASE+'" onclick="showPileChart(this);">'+'查看'+'</a></td>';
        			 html += '</tr>';
        		 });
        		 tbody.html(html);
        	 }else{
            	 showMsg(data.message, data.errormsg);
             }
         }
     });
}

function showDetail(obj){
	$("#detail_pilename").text($(obj).find("[id=pilename]").text());
	$("#detail_pilecode").text($(obj).find("[id=pilecode]").text());
	$("#detail_busmecname").text($("#busMecName").text());
	$("#detail_stationname").text($("#stationName").text());
	$("#detail_powertype").text($(obj).find("[id=powertype]").text());
	$("#detail_currenttype").text($(obj).find("[id=currenttype]").text());
	$("#detail_payway").text($(obj).find("[id=payway]").text());
	$("#detail_comtype").text($(obj).find("[id=comtype]").text());
	$("#detail_comaddr").text($(obj).find("[id=comaddr]").text());
	pileid = $(obj).find("[id=pileid]").text();
	showResult("showpiledetail");
	queryPileStatus();
}
function showPileChart(obj){
	pileId = $(obj).attr("pileId");
	pileName = $(obj).attr("pileName");
	pileType = $(obj).attr("pileType");//1单相2三相
	$("#pileIDForChart").val(pileId);
	$("#pileNameForChart").val(pileName);
	$("#pileTypeForChart").val(pileType);
	$("#addDay").val(0);
	queryPileChart(pileId,pileName,pileType);
}


var tempSeries = {
    	name:'输出电压',
        type: 'line',
        data:[],
        hoverAnimation: false,
        symbolSize: 6,
        itemStyle: {
            normal: {
                color: '#c23531'
            }
        },
        markLine: {
            data: [
                {type: 'average', name: '平均值'}
            ]
        },
        showSymbol: false,
    }
var tempSeries1 = {
    	name:'输出电压',
        type: 'line',
        data:[],
        hoverAnimation: false,
        symbolSize: 6,
        itemStyle: {
            normal: {
                color: '#c23531'
            }
        },
      /*  markLine: {
            data: [
                {type: 'average', name: '平均值'}
            ]
        },*/
        showSymbol: false,
    }
var tempSeries2 = {
    	name:'输出电压',
        type: 'line',
        data:[],
        hoverAnimation: false,
        symbolSize: 6,
        itemStyle: {
            normal: {
                color: '#c23531'
            }
        },
      /*  markLine: {
            data: [
                {type: 'average', name: '平均值'}
            ]
        },*/
        showSymbol: false,
    }
var tempSeries3 = {
    	name:'输出电压',
        type: 'line',
        data:[],
        hoverAnimation: false,
        symbolSize: 6,
        itemStyle: {
            normal: {
                color: '#c23531'
            }
        },
        /*markLine: {
            data: [
                {type: 'average', name: '平均值'}
            ]
        },*/
        showSymbol: false,
    }

function queryPileChart(pileId,pileName,pileType){
	if(pileId > 0){
		param.pileId = pileId;
		param.pileType = pileType;
		param.addDay = $("#addDay").val();
		$.ajax({
			type:"POST",
			url:'run/monitor_queryPileChart.action',
			data:param,
			dataType:'json',
	        cache: false,
	        beforeSend:function(){$("#loading").removeClass("hide");},
	        complete:function(){
		    	  $("#loading").addClass("hide"); 
		      },
		      error : function() {  
		           showWarning("系统异常！！");
		      },
	        success: function(data){
	        	$("#main1").empty();
        		$("#main2").empty();
	        	showResult("showpilechart");
	        	if("success" == data.result.msg){
	        		option.title.text = pileName;
	        		if(pileType == SINGLE_PHASE){//单相
	        	 		dataTimeV = data.result.dataTimeV;//电压时间横轴
		        		dataV = data.result.dataV;//电压
		        		dataTimeA = data.result.dataTimeA;//电流时间横轴
		        		dataA = data.result.dataA;//电流
		        			if(dataV && dataV.length > 0 && dataTimeV && dataTimeV.length > 0){
		        				option.title.subtext='电压实时监控';
		        				option.yAxis.axisLabel.formatter='{value} V';
		        				option.legend.data[0]='输出电压';
		        				tempSeries.name='输出电压';
		        				tempSeries.itemStyle.normal.color='#c23531';
		        				tempSeries.data = dataV;
		        				
		        				option.series[0] = tempSeries;
		        				option.tooltip.formatter=function (params) {
		        				    params = params[0];
		        				    return " 输出电压：" + params.value+" V"+"</br>"+"时间："+params.name;
		        				};
		        				option.xAxis.data = dataTimeV;
		        				echarts.init(document.getElementById('main1')).setOption(option);
		        			}else{
		        				$("#main1").append("<p class='text-center' style='margin-top:10%;'>暂无输出电压数据</p>");
		        			}
		        			if(dataA && dataA.length > 0 && dataTimeA && dataTimeA.length > 0){
		        				option.title.subtext='电流实时监控';
		        				option.yAxis.axisLabel.formatter='{value} A'
		        				option.legend.data[0]='输出电流';
		        				tempSeries.name='输出电流';
		        				tempSeries.itemStyle.normal.color='#6B8E23';
		        				tempSeries.data = dataA;
		        				option.series[0] = tempSeries;
		        				option.tooltip.formatter=function (params) {
		        				    params = params[0];
		        				    return " 输出电流：" + params.value+" A"+"</br>"+"时间："+params.name;
		        				};
		        				option.xAxis.data = dataTimeA;
		    	        		echarts.init(document.getElementById('main2')).setOption(option);
		        			}else{
		        				$("#main2").append("<p class='text-center' style='margin-top:10%;'>暂无输出电流数据</p>");
		        			}	
	        		}
	        		else if(pileType == THREE_PHASE){//三相
	        			dataTimeV = data.result.dataTimeV;//电压时间横轴
		        		dataV1 = data.result.dataV1;//电压1
		        		dataV2 = data.result.dataV2;//电压2
		        		dataV3 = data.result.dataV3;//电压3
		        		dataTimeA = data.result.dataTimeA;//电流时间横轴
		        		dataA1 = data.result.dataA1;//电流1
		        		dataA2 = data.result.dataA2;//电流2
		        		dataA3 = data.result.dataA3;//电流3
		        		if(dataTimeV && dataTimeV.length > 0){
		        			option.title.subtext='电压实时监控';
	        				option.yAxis.axisLabel.formatter='{value} V';
	        				option.legend.data[0]='A相电压';
	        				tempSeries1.name='A相电压';
	        				tempSeries1.itemStyle.normal.color='#c23531';
	        				tempSeries1.data = dataV1;
	        				option.series[0] = tempSeries1;
	        				
	        				option.legend.data[1]='B相电压';
	        				tempSeries2.name='B相电压';
	        				tempSeries2.itemStyle.normal.color='#CD950C';
	        				tempSeries2.data = dataV2;
	        				option.series[1] = tempSeries2;
	        				
	        				option.legend.data[2]='C相电压';
	        				tempSeries3.name='C相电压';
	        				tempSeries3.itemStyle.normal.color='#FF4500';
	        				tempSeries3.data = dataV3;
	        				option.series[2] = tempSeries3;
	        				option.tooltip.formatter=function (params) {
	        				    params1 = params[0];
	        				    params2 = params[1];
	        				    params3 = params[2];
	        				    
	        				    temp="未知";
	        				    temp2="无数据";
	        				    temp3="无数据";
	        				    temp4="无数据";
	        				    if(params1.name){
	        				    	temp=params1.name;
	        				    }else if(params2.name){
	        				    	temp=params2.name;
	        				    }else if(params3.name){
	        				    	temp=params3.name;
	        				    }
	        				    if(params1.value >= 0){
	        				    	temp2 = params1.value+" A";
	        				    }
	        				    if(params2.value >= 0){
	        				    	temp3 = params2.value+" A";
	        				    }
	        				    if(params3.value >= 0){
	        				    	temp4 = params3.value+" A";
	        				    }
	        				    
	        				    return 	 " A相电压：" + temp2+"</br>"
	        				    		+" B相电压：" + temp3+"</br>"
	        				    		+" C相电压：" + temp4+"</br>"
	        				    		+"时间："+temp;
	        				};
	        				option.xAxis.data = dataTimeV;
	        				echarts.init(document.getElementById('main1')).setOption(option);
		        		}else{
		        			$("#main1").append("<p class='text-center' style='margin-top:10%;'>暂无输出电压数据</p>");
		        		}
		        		if(dataTimeA && dataTimeA.length > 0){
		        			option.title.subtext='电流实时监控';
	        				option.yAxis.axisLabel.formatter='{value} A'
	        					
	        				option.legend.data[0]='A相电流';
	        				tempSeries1.name='A相电流';
	        				tempSeries1.itemStyle.normal.color='#6B8E23';
	        				tempSeries1.data = dataA1;
	        				option.series[0] = tempSeries1;
	        				
	        				option.legend.data[1]='B相电流';
	        				tempSeries2.name='B相电流';
	        				tempSeries2.itemStyle.normal.color='#006400';
	        				tempSeries2.data = dataA2;
	        				option.series[1] = tempSeries2;
	        				
	        				option.legend.data[2]='C相电流';
	        				tempSeries3.name='C相电流';
	        				tempSeries3.itemStyle.normal.color='#00FFFF	';
	        				tempSeries3.data = dataA3;
	        				option.series[2] = tempSeries3;
	        				
	        				
	        				option.tooltip.formatter=function (params) {
	        				    params1 = params[0];
	        				    params2 = params[1];
	        				    params3 = params[2];
	        				    temp="未知";
	        				    temp2="无数据";
	        				    temp3="无数据";
	        				    temp4="无数据";
	        				    if(params1.name){
	        				    	temp=params1.name;
	        				    }else if(params2.name){
	        				    	temp=params2.name;
	        				    }else if(params3.name){
	        				    	temp=params3.name;
	        				    }
	        				    if(params1.value >= 0){
	        				    	temp2 = params1.value+" A";
	        				    }
	        				    if(params2.value >= 0){
	        				    	temp3 = params2.value+" A";
	        				    }
	        				    if(params3.value >= 0){
	        				    	temp4 = params3.value+" A";
	        				    }
	        				    return   " A相电流：" + temp2+"</br>"
	        				    		+" B相电流：" + temp3+"</br>"
	        				    		+" C相电流：" + temp4+"</br>"
	        				    		+"时间："+temp;
	        				};
	        				option.xAxis.data = dataTimeA;
	    	        		echarts.init(document.getElementById('main2')).setOption(option);
		        		}else{
		        			$("#main2").append("<p class='text-center' style='margin-top:10%;'>暂无输出电流数据</p>");
		        		}
	        		}
	       
	        	}else{
	        		$("#main1").append("<p class='text-center' style='margin-top:10%;'>暂无输出电压数据</p>");
	        		$("#main2").append("<p class='text-center' style='margin-top:10%;'>暂无输出电流数据</p>");
	    			showInfo(data.result.msg)
	        	}
	         }
	     });
	}else{
		showInfo("请选择充电桩！！")
	}
}

var pileid;
function queryPileStatus(){
	param = {};
	param.pileid = pileid;
	$.ajax({
		type:"POST",
		url:'run/monitor_queryPileStatus.action',
		data:param,
		dataType:'json',
        cache: false,
        beforeSend:function(){$("#loading").removeClass("hide");},
        complete:function(){
	    	  $("#loading").addClass("hide"); 
	      },
        success: function(data,options){
        	 if(data.pileStatus){
        		 $("#detail_outv").text(getNotNullData(data.pileStatus.outv) +" V");
        		 $("#detail_outi").text(getNotNullData(data.pileStatus.outi) + " A");
        		 $("#detail_status").text(data.pileStatus.statusdisc);
        		 $("#detail_userid").text(getNotNullData(data.pileStatus.userid));
        		 $("#detail_username").text(getNotNullData(data.pileStatus.username));
        		 $("#detail_phone").text(getNotNullData(data.pileStatus.phone));
        		 $("#detail_apprecordid").text(getNotNullData(data.pileStatus.apprecordid));
        		 $("#detail_appendtime").text(getFormatDate(data.pileStatus.appendtime));
        		 $("#detail_chalen").text(getNotNullData(data.pileStatus.chalen) + " 分钟");
        		 $("#detail_chapower").text(getNotNullData(data.pileStatus.chapower) + " 度");
        		 $("#detail_money").text(getNotNullData(data.pileStatus.money) +" 元");
        		 $("#detail_tradeno").text(getNotNullData(data.pileStatus.tradeno));
        		 if(data.pileStatus.status == PILE_RUNSTATUS_APPOINTMENT || data.pileStatus.status == PILE_RUNSTATUS_CHARGING){
        			 $("#userInfo").removeClass("hide"); 
        			 if(data.pileStatus.status == PILE_RUNSTATUS_APPOINTMENT){
        				 $("#appInfo").removeClass("hide");
        				 $("#chargeInfo").addClass("hide");
        			 }else{
        				 $("#appInfo").addClass("hide");
        				 $("#chargeInfo").removeClass("hide");
        			 }
        		 }else{
        			 $("#userInfo").addClass("hide");
        			 $("#appInfo").addClass("hide");
        			 $("#chargeInfo").addClass("hide");
        		 }
        	 }else{
            	 showMsg(data.message, data.errormsg);
             }
         }
     });
}

function showResult(flag){
	if("showpilelist" == flag){//显示充电桩列表
		$("#stationResult").addClass("hide");
		$("#pileDetailResult").addClass("hide");
		$("#pileChartResult").addClass("hide");
		$("#pileResult").removeClass("hide");
	}else if("showpiledetail" == flag){//显示充电桩详细
		$("#stationResult").addClass("hide");
		$("#pileResult").addClass("hide");
		$("#pileChartResult").addClass("hide");
		$("#pileDetailResult").removeClass("hide");
	}else if("showpilechart" == flag){
		$("#stationResult").addClass("hide");
		$("#pileResult").addClass("hide");
		$("#pileDetailResult").addClass("hide");
		$("#pileChartResult").removeClass("hide");
	}else{//显示充电点列表
		$("#pileResult").addClass("hide");
		$("#pileChartResult").addClass("hide");
		$("#pileDetailResult").addClass("hide");
		$("#stationResult").removeClass("hide");
	}
}
//折线图表
   option = {
        title: {
            text: '华立科技元 1#充电桩',
            subtext: '电压实时监控',
        },
        tooltip: {
            trigger: 'axis',
            formatter: function (params) {
                params = params[0];
                return " 输出电压：" + params.value+" V"+"</br>"+"时间："+params.name;
            },
            axisPointer: {
                animation: false
            }
        },
        legend: {
            data:['输出电压']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis : 
            {
                type : 'category',
                axisLabel: {
                    formatter: function (value, idx) {
                       // var date = new Date(value);
                        temp = value.split(" ");
                       // return temp[2] == 'F'?temp[0]+" "+temp[1]:temp[1]
                        return idx === 0 ? value : temp[1];
                    }
                },
                boundaryGap : false,
                splitLine: {
                    show: false
                },
                data : ['2016/01/01 08:00','2016-01-01 09:40','2016/01/01 10:00','2016/01/01 11:00','2016/01/01 12:00']
            }
        ,
        yAxis: {
        	 axisLabel: {
                 formatter: '{value} V'
             },
           
            splitNumber: 3,
            splitLine: {
                show: false
            }
        },
        series: [ {
        	name:'输出电压',
            type: 'line',
            data:[1,2,3],
            hoverAnimation: false,
            symbolSize: 6,
            itemStyle: {
                normal: {
                    color: '#c23531'
                }
            },
            markLine: {
                data: [
                    {type: 'average', name: '平均值'}
                ]
            },
            showSymbol: false,
        }
        ]
    };
   
   
   /* markPoint: {
	symbolSize:70,
   data: [
       {type: 'max', name: '最大值'},
       {type: 'min', name: '最小值'}
   ]
},*/
