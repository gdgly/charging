var param={};
$(document).ready(function(){
	initParams();
	queryStationList();
});

function initParams(){
	initArea($("#province"), $("#city"));//初始化省市
	initPagingToolbar(queryStationList);
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
	//初始化充电点列表导出按钮
	initExportBtn($("#exportStationBtn"),$("#sconditionFrom"),$("#sfileName").val());
	//初始化充电桩列表导出按钮
	initExportBtn($("#exportPileBtn"),$("#pconditionFrom"),$("#pfileName").val());
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
        			 html += '<td class="text-right">';
        			 html += '<a href="javascript:;" onclick="queryStationDetail(this)"'+
        				 ' stationid='+item.stationid+
        				 ' stationname='+item.stationname+
        				 ' busmecname='+item.busmecname+
        				 ' address='+item.address+
        				 ' pilestatus=-1'+
        				 '>'+getNotNullData(item.pilenum)+'</a>';
        			 html += '</td>';
        			//空闲数
        			 html += '<td class="text-right">'
        			 html += '<a style="color:'+getPileStatusColor(PILE_RUNSTATUS_IDLE)+';" href="javascript:;" onclick="queryStationDetail(this)"'+
        				 ' stationid='+item.stationid+
        				 ' stationname='+item.stationname+
        				 ' busmecname='+item.busmecname+
        				 ' address='+item.address+
        				 ' pilestatus='+PILE_RUNSTATUS_IDLE+
        				 '>'+getNotNullData(item.idlenum)+'</a>';
        			 html += '</td>';
        			//充电数
        			 html += '<td class="text-right">'
        			 html += '<a style="color:'+getPileStatusColor(PILE_RUNSTATUS_CHARGING)+';" href="javascript:;" onclick="queryStationDetail(this)"'+
        				 ' stationid='+item.stationid+
        				 ' stationname='+item.stationname+
        				 ' busmecname='+item.busmecname+
        				 ' address='+item.address+
        				 ' pilestatus='+PILE_RUNSTATUS_CHARGING+
        				 '>'+getNotNullData(item.chargenum)+'</a>';
        			 html += '</td>';
        			//预约数
        			 html += '<td class="text-right">'
        			 html += '<a style="color:'+getPileStatusColor(PILE_RUNSTATUS_APPOINTMENT)+';" href="javascript:;" onclick="queryStationDetail(this)"'+
        				 ' stationid='+item.stationid+
        				 ' stationname='+item.stationname+
        				 ' busmecname='+item.busmecname+
        				 ' address='+item.address+
        				 ' pilestatus='+PILE_RUNSTATUS_APPOINTMENT+
        				 '>'+getNotNullData(item.appnum)+'</a>';
        			 html += '</td>';
        			//离线数
        			 html += '<td class="text-right">'
        			 html += '<a style="color:'+getPileStatusColor(PILE_RUNSTATUS_OFFLINE)+';" href="javascript:;" onclick="queryStationDetail(this)"'+
        				 ' stationid='+item.stationid+
        				 ' stationname='+item.stationname+
        				 ' busmecname='+item.busmecname+
        				 ' address='+item.address+
        				 ' pilestatus='+PILE_RUNSTATUS_OFFLINE+
        				 '>'+getNotNullData(item.offlinenum)+'</a>';
        			 html += '</td>';
        			//故障数
        			 html += '<td class="text-right">'
        			 html += '<a style="color:'+getPileStatusColor(PILE_RUNSTATUS_FAULT)+';" href="javascript:;" onclick="queryStationDetail(this)"'+
        				 ' stationid='+item.stationid+
        				 ' stationname='+item.stationname+
        				 ' busmecname='+item.busmecname+
        				 ' address='+item.address+
        				 ' pilestatus='+PILE_RUNSTATUS_FAULT+
        				 '>'+getNotNullData(item.faultnum)+'</a>';
        			 html += '</td>';
        			//忙碌数
        			 html += '<td class="text-right">'
        			 html += '<a style="color:'+getPileStatusColor(PILE_RUNSTATUS_UNKNOW)+';" href="javascript:;" onclick="queryStationDetail(this)"'+
        				 ' stationid='+item.stationid+
        				 ' stationname='+item.stationname+
        				 ' busmecname='+item.busmecname+
        				 ' address='+item.address+
        				 ' pilestatus='+PILE_RUNSTATUS_UNKNOW+
        				 '>'+getNotNullData(item.unknownum)+'</a>';
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
        success: function(data,options){
        	 if(data.pileStatusList){
        		 tbody.empty();
        		 var dataList = data.pileStatusList;
        		 var color;
        		 $(dataList).each(function(index,item){
        			 html += '<tr style="cursor:pointer;" onclick="showDetail(this);" >';
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
		$("#pileResult").removeClass("hide");
		$("#pileDetailResult").addClass("hide");
	}else if("showpiledetail" == flag){//显示充电桩详细
		$("#stationResult").addClass("hide");
		$("#pileResult").addClass("hide");
		$("#pileDetailResult").removeClass("hide");
	}else{//显示充电点列表
		$("#stationResult").removeClass("hide");
		$("#pileResult").addClass("hide");
		$("#pileDetailResult").addClass("hide");
	}
}











