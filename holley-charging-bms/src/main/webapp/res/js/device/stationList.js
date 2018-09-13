var param={};
$(document).ready(function(){
	initStationParams();
	queryStationList();
});

function initStationParams(){
	initArea($("#province"), $("#city"));//初始化省市
	initPagingToolbar(queryStationList);
	$("#addStationBtn").on("click",function(){
		href("device/addStation.action");
	});
	//初始化充电点列表查询按钮
	$("#stationQueryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryStationList();
	});
	//初始化充电点列表导出按钮
	initExportBtn($("#stationExportBtn"),$("#stationForm"),$("#stationFileName").val());
	//初始化充电桩列表查询按钮
	$("#pileQueryBtn").on("click",function(){
		queryPileList();
	});
	//初始化充电桩列表导出按钮
	initExportBtn($("#pileExportBtn"),$("#pileForm"),$("#pileFileName").val());
}

function queryStationList(){
	param={};
	param.keyword = $.trim($("#keyword").val());
	param.province = $("#province option:selected").val();
    param.city = $("#city option:selected").val();
	param.bustype = $("#busType option:selected").val();
	param.isshow = $("#isShow option:selected").val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	param.tm = new Date().getTime();
	
	var tbody = $("#stationTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'device/station_queryStationList.action',
		data:param,
		dataType:'json',
        cache: false,
        success: function(data,options){
             if(data.page){
            	 tbody.empty();
            	 var dataList = data.page.root;
            	 $(dataList).each(function(index,item){
            		 temp = item.busType;
            		 html += '<tr>';
            		 html += '<td>'+item.id+'</td>';
            		 html += getTdHtml(item.stationName, 15);
            		 html += '<td>'+item.provinceDesc+(isEmpty(item.cityDesc)?"":'，'+item.cityDesc)+'</td>'; 
            		 html += getTdHtml(item.address, 15);
            		 html += '<td>'+item.busTypeDesc+'</td>';
            		 html += getTdHtml(item.busMecName, 15);
            		 html += '<td>'+item.isShowDesc+'</td>';
            		 html += '<td class="text-right"><a href="javascript:;" onclick="showPileList(this)"'+
            		 ' stationid='+item.id+
            		 ' stationname='+item.stationName+
    				 ' busmecname='+item.busMecName+
    				 ' address='+item.address+
            		 ' piletype='+CHARGEPOWER_TYPE_FAST+'>'+getNotNullData(item.fastNum)+'</a></td>';
            		 html += '<td class="text-right"><a href="javascript:;" onclick="showPileList(this)"'+
            		 ' stationid='+item.id+
            		 ' piletype='+CHARGEPOWER_TYPE_SLOW+'>'+getNotNullData(item.slowNum)+'</a></td>';
            		 html += '<td class="text-right">'+getNotNullData(item.score)+'</td>';
            		 html += '<td>'+item.updateTimeStr+'</td>'
            		 html += '<td>'
            	     html += '<a class="a-blue" href="device/stationDetail.action?id='+item.id+'">详细</a> | ';
            		 if(temp == 1){
            			 html += '<a class="a-blue" href="device/editStation.action?stationId='+item.id+'">修改</a> | ';
                		 html += '<a class="a-blue" href="device/addPile.action?stationId='+item.id+'">添加</a>';	 
            		 }else{
            			 html += '<span  style="color:gray">修改</span> | ';
                		 html += '<span  style="color:gray">添加</span>';	  
            		 }
            		 html +='</td>';
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

var stationid;
function showPileList(obj){
	stationid = $(obj).attr("stationid");
	$("#stationId").val(stationid);
	$("#busMecName").text($(obj).attr("busmecname"));
	$("#stationName").text($(obj).attr("stationname"));
	$("#address").text($(obj).attr("address"));
	$("#pileType").val($(obj).attr("piletype"));
	queryPileList();
}

function queryPileList(){
	param = {};
	param.stationid = stationid;
    param.piletype = $('#pileType option:selected').val();
	var tbody = $("#pileTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'device/station_queryPileList.action',
		data:param,
		dataType:'json',
        cache: false,
        success: function(data,options){
        	 if(data.pileList){
        		 showResult("showpilelist")
        		 tbody.empty();
        		 var dataList = data.pileList;
        		 $(dataList).each(function(index,item){
        			 html += '<tr style="cursor:pointer;" onclick="showPileDetail(this);" >';
        			 html += '<td id="pileid">'+item.id+'</td>';
        			 html += '<td id="pilename">'+getNotNullData(item.pileName)+'</td>';
        			 html += '<td id="pilecode">'+getNotNullData(item.pileCode)+'</td>';
        			 html += '<td id="comtypedesc">'+getNotNullData(item.comTypeDesc)+'</td>';
        			 html += '<td id="comaddr">'+getNotNullData(item.comAddr)+'</td>';
        			 html += '<td id="powertypedesc">'+getNotNullData(item.pileTypeDesc)+'</td>';
        			 html += '<td id="currenttypedesc">'+getNotNullData(item.chaWayDesc)+'</td>';
        			 html += '<td>'+getNotNullData(item.isAppDesc)+'</td>';
        			 html += '<td>'+getNotNullData(item.payWayDesc)+'</td>';
        			 html += '<td>'+getNotNullData(item.statusDesc)+'</td>';
        		 });
        		 tbody.html(html);
        	 }else{
            	 showMsg(data.message, data.errormsg);
             }
         }
     });
}

function showPileDetail(obj){
	pileid = $(obj).find("[id=pileid]").text();
	queryPileDetail();
}

var pileid;
function queryPileDetail(){
	param = {};
	param.pileid = pileid;
	$.ajax({
		type:"POST",
		url:'device/station_queryPileDetail.action',
		data:param,
		dataType:'json',
        cache: false,
        success: function(data,options){
        	 if(data.dataMap){
        		 showResult("showpiledetail");
        		 setPileDetail(data.dataMap);
        	 }else{
            	 showMsg(data.message, data.errormsg);
             }
         }
     });
}

function setPileDetail(data){
	var chargePile = data.chargePile;
	 var activeRule = data.activeRule;
	 var unactiveRule = data.unactiveRule;
	 if(chargePile){
		 $("#pileDetail_pileName").text(chargePile.pileName);
		 $("#pileDetail_pileType").text(chargePile.pileTypeDesc);
		 $("#pileDetail_pileCode").text(chargePile.pileCode);
		 $("#pileDetail_pileModel").text(chargePile.pileModelDesc);
		 $("#pileDetail_chaWay").text(chargePile.chaWayDesc);
		 $("#pileDetail_chaMode").text(chargePile.chaModelDesc);
		 $("#pileDetail_payWay").text(chargePile.payWayDesc);
		 $("#pileDetail_stationName").text(chargePile.stationName);
		 $("#pileDetail_isControl").text(chargePile.isControlDesc);
		 $("#pileDetail_isChaLoad").text(chargePile.isChaLoadDesc);
		 $("#pileDetail_isApp").text(chargePile.isAppDesc);
		 $("#pileDetail_status").text(chargePile.statusDesc);
		 $("#pileDetail_buildTime").text(chargePile.buildTimeStr);
		 $("#pileDetail_updateTime").text(chargePile.updateTimeStr);
		 $("#pileDetail_address").text(chargePile.address);
		 $("#pileDetail_comType").text(chargePile.comTypeDesc);
		 $("#pileDetail_comAddr").text(chargePile.comAddr);
		 $("#pileDetail_comSubAddr").text(chargePile.comSubAddr);
	 }
	 if(activeRule){
		 $("#activeRuleDiv").removeClass("hide");
		 $("#activeRule_feeRule").text(activeRule.feeRuleDesc);
		 if(activeRule.chargeruleId == "1"){//单一电价
			 $("#achargeFeeDiv").removeClass("hide");
			 $("#afeeRuleDetailDiv").addClass("hide");
			 $("#activeRule_chargeFee").text(activeRule.chargeFee+" 元/度");
		 }else{//多费率电价
			 $("#achargeFeeDiv").addClass("hide");
			 $("#afeeRuleDetailDiv").removeClass("hide");
			 $("#activeRule_feeRuleDetail").text(activeRule.feeRuleDetail);
		 }
		 $("#activeRule_parkFee").text(activeRule.parkFee + " 元/时");
		 $("#activeRule_serviceFee").text(activeRule.serviceFee+" 元/度");
		 $("#activeRule_activeTime").text(activeRule.activeTimeStr);
	 }
	 if(unactiveRule){
		 $("#unactiveRuleDiv").removeClass("hide");
		 $("#unactiveRule_feeRule").text(unactiveRule.feeRuleDesc);
		 if(unactiveRule.chargeruleId == "1"){//单一电价
			 $("#uchargeFeeDiv").removeClass("hide");
			 $("#ufeeRuleDetailDiv").addClass("hide");
			 $("#unactiveRule_chargeFee").text(unactiveRule.chargeFee+" 元/度");
		 }else{//多费率电价
			 $("#uchargeFeeDiv").addClass("hide");
			 $("#ufeeRuleDetailDiv").removeClass("hide");
			 $("#unactiveRule_feeRuleDetail").text(unactiveRule.feeRuleDetail);
		 }
		 $("#unactiveRule_parkFee").text(unactiveRule.parkFee + " 元/时");
		 $("#unactiveRule_serviceFee").text(unactiveRule.serviceFee+" 元/度");
		 $("#unactiveRule_activeTime").text(unactiveRule.activeTimeStr);
	 }
}



function showResult(flag){
	if("showpilelist" == flag){//显示充电桩列表
		$("#stationListResult").addClass("hide");
		$("#pileListResult").removeClass("hide");
		$("#pileDetailResult").addClass("hide");
	}else if("showpiledetail" == flag){//显示充电桩详细
		$("#stationListResult").addClass("hide");
		$("#pileListResult").addClass("hide");
		$("#pileDetailResult").removeClass("hide");
	}else{//显示充电点列表
		$("#stationListResult").removeClass("hide");
		$("#pileListResult").addClass("hide");
		$("#pileDetailResult").addClass("hide");
	}
}
