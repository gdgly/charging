var param={};
var tempIssuedObj={};
var issuedByStationUrl="device/station_issuedByStation.action";
$(document).ready(function(){
	initStationParams();
	queryStationList();
});
function refreshChargeRuleDesc(){
	var tem = $("#feeRule").val();
	$(jsonstrToObj(chargeRuleJsonObj)).each(function(index,data){
	if(tem == data.id){
		if(tem == 1){
			$("#chargeRuleDesc").text(data.name);
		}else{
			$("#chargeRuleDesc").text("尖:"+data.jianFee+"元 ；峰:"+data.fengFee+"元；平:"+data.pingFee+"元；谷:"+data.guFee+"元");
		}
	}
	});
}
function initStationParams(){
//	$('#chargeFeeRuleCheckModal').on('hidden.bs.modal', function () {
//		})
	
	initMinBootStrapDate($("#activeTimeDiv"),new Date(currentTime));//初始化时间插件
	$("#chargeFeeRuleCheckModalForm").find("input").on("change",function(){
		var tempval=$(this).val();
		if(isNaN(tempval)){
			$(this).val("").focus();
		}else{
			$(this).val(parseFloat(Math.abs(tempval)).toFixed(4));
		}
	});
	$("#doIssuedBtn").on("click",function(){
		param={};
		var serviceFee=$("#seviceFee").val();
		var parkFee=$("#parkFee").val();
		var chargeruleId=$("#feeRule").val();
		var chargeFee=$("#chargeFee").val();
		var activeTime=$("#activeTime").val();
	if(tempIssuedObj.ids.length <= 0){
		showInfo("参数非法");
		return;
	}else if(isEmpty(serviceFee)){
		showInfo("服务费不能为空");
		return;
	}else if(isEmpty(parkFee)){
		showInfo("停车费不能为空");
		return;
	}else if(chargeruleId == 1 && isEmpty(chargeFee)){
		showInfo("单一电费不能为空");
		return;
	}else if (isEmpty(activeTime)) {
		showInfo("规则启用时间不能为空");
		return;
	}else{
		temp = new Date(activeTime);
	    dd = new Date(currentTime);
	    if(temp < dd){
	    	showInfo("规则启用时间不能小于当前日");
	    	return;
	    }
}
	param.ids=tempIssuedObj.ids.join(",");
	param.chargeruleId=chargeruleId;
	param.serviceFee=serviceFee;
	param.parkFee=parkFee;
	param.chargeFee=chargeFee;
	param.activeTime=activeTime;
		var option = {
				title: "系统提示",
				btn: window.wxc.xcConfirm.btnEnum.okcancel,
				onOk: function(){
					$.ajax({
						type:"POST",
						url:issuedByStationUrl,
						data:param,
						dataType:'json',
				        cache: false,
				       // beforeSend:showLoading,
				      //  complete:hideLoading,
				        success: function(data){
				        	if(data.success){
//				        		var opt={
//				        				onOk:reload,
//				        				onClose:reload
//				        		}
				        		showInfo("下发成功")
							}else{
								showMsg(data.message, data.errormsg);
							}
				         }
				     });
				}
			}
			showConfirm("确定现在下发计费模型吗?",option);
	});
	$("#feeRule").on("change",function(){
		refreshChargeRuleDesc();
	});
	$("#issuedChargeRuleBtn").on("click",function(){
		resetForm($("#chargeFeeRuleCheckModalForm"));
		wrapCheckedData();
		refreshChargeRuleDesc();
		if(tempIssuedObj.ids.length > 0){
			$("#issuedStation").text(tempIssuedObj.stationNames);
			$('#chargeFeeRuleCheckModal').modal({
				keyboard: false
				//backdrop:false
			});
		}else{
			showInfo("请选择要下发的充电站")
		}
	});
	$("#allcheck").on("change",function(){
		if($(this).is(':checked')){
			//$("input[name='subcheck']").attr("checked",true);
			$("input[name='subcheck']").each(function(){
				$(this).get(0).checked =true;
			});
		}else{
			$("input[name='subcheck']").each(function(){
				$(this).get(0).checked =false;
			});
		}
	});
	initArea($("#province"), $("#city"));//初始化省市
	initPagingToolbar(queryStationList);
	$("#addStationBtn").on("click",function(){
		href("device/addStation.action");
	});
	//初始化充电点列表查询按钮
	$("#stationQueryBtn").on("click",function(){
		$("#stationIdForSelectModal").val(0);
		setPagingToolbarParams(0, 0, 1);
		queryStationList();
	});
	//初始化充电点列表导出按钮
	initExportBtn($("#stationExportBtn"),$("#stationForm"),$("#stationFileName").val(),$("#stationTable"));
	//初始化充电桩列表查询按钮
	$("#pileQueryBtn").on("click",function(){
		queryPileList();
	});
	//初始化充电桩列表导出按钮
	initExportBtn($("#pileExportBtn"),$("#pileForm"),$("#pileFileName").val(),$("#pileTable"));
	bindKey13([$("#keyword")],$("#stationQueryBtn"));//回车自动搜索
}

function queryStationList(stationId){
	$("#allcheck").get(0).checked =false;
	param={};
	if(!isEmpty(stationId)){
		setPagingToolbarParams(0, 0, 1);
		param.keyword = $("#keyword").val("").val();
		param.province = $("#province").val(0).val();
	    param.city = $("#city").val(0).val();
		param.bustype = $("#busType").val(0).val();
		param.isshow = $("#isShow").val(0).val();
		param.stationToType=$("#stationToType").val(0).val();
		param.tm = new Date().getTime();
		param.stationIdForSelectModal = stationId;
		$("#stationIdForSelectModal").val(stationId);
	}else{
		param.keyword = $.trim($("#keyword").val());
		param.province = $("#province").val();
	    param.city = $("#city").val();
		param.bustype = $("#busType").val();
		param.isshow = $("#isShow").val();
		param.stationToType=$("#stationToType").val();
		param.tm = new Date().getTime();
		param.stationIdForSelectModal = $("#stationIdForSelectModal").val();
	}
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	var tbody = $("#stationTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'device/station_queryStationList.action',
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
            	 $(dataList).each(function(index,item){
            		 temp = item.busType;
            		 html += '<tr>';
            		 if(item.stationToType == 1){
            			 html += '<td>'+'<input name="subcheck" type="checkbox" value="'+item.id+'" stationName="'+item.stationName+'"/></td>'; 
            		 }else{
            			 html += '<td>--</td>';
            		 }
            		/* html += '<td>'+item.id+'</td>';*/
            		 html += getTdHtml(item.stationName, 15);
            		 html += '<td>'+item.provinceDesc+(isEmpty(item.cityDesc)?"":'，'+item.cityDesc)+'</td>'; 
            		 html += getTdHtml(item.address, 15);
            		 html += '<td>'+getDefaultData(item.busMecName, "系统平台")+'</td>';
            		 html += '<td>'+getDefaultData(item.stationToTypeDesc, "--")+'</td>';
            		 /*html += '<td>'+item.busTypeDesc+'</td>';
            		 html += getTdHtml(item.busMecName, 15);*/
            		/* html += '<td>'+item.isShowDesc+'</td>';*/
            		 html += '<td class="text-center"><a href="javascript:;" onclick="showPileList(this)"'+
            		 ' stationid="'+item.id+
            		 '" stationname="'+item.stationName+
    				 '" busmecname="'+getDefaultData(item.busMecName, "系统平台")+
    				 '" address="'+item.address+
            		 '" piletype="'+CHARGEPOWER_TYPE_FAST+'">'+getNotNullData(item.fastNum)+'</a></td>';
            		 html += '<td class="text-center"><a href="javascript:;" onclick="showPileList(this)"'+
            		 ' stationid="'+item.id+
            		 '" piletype="'+CHARGEPOWER_TYPE_SLOW+'">'+getNotNullData(item.slowNum)+'</a></td>';
            		/* html += '<td class="text-center">'+getNotNullData(item.score)+'</td>';*/
            		 html += '<td>'+item.updateTimeStr+'</td>'
            		 html += '<td>'
            	     html += '<a class="a-blue" href="device/stationDetail.action?id='+item.id+'">详细</a>';
            		 if(userType == 1){
            			 html += ' | ';
            			 html += '<a class="a-blue" href="device/editStation.action?stationId='+item.id+'">修改</a> | ';
                		 html += '<a class="a-blue" href="device/addPile.action?stationId='+item.id+'">添加</a> | ';
                		 html += '<a class="a-blue" onclick="deleteStation(this);" style="cursor: pointer;" stationId="'+item.id+'" stationName="'+item.stationName+'">删除</a>';
            		 }
            		 /*else{
            			 html += '<span  style="color:gray">修改</span> | ';
                		 html += '<span  style="color:gray">添加</span>';	  
            		 }*/
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
	$("#busMecName").text(getDefaultData($(obj).attr("busmecname"),"北京"));
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
        beforeSend:function(){$("#loading").removeClass("hide");},
        complete:function(){
	    	  $("#loading").addClass("hide"); 
	      },
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
        			/* html += '<td>'+getNotNullData(item.isAppDesc)+'</td>';*/
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
			 if(chargePile.pileToType == 2){//自行车
				 $("#activeRule_chargeFee").text(activeRule.chargeFee+" 小时/元");
			 }else{
				 $("#activeRule_chargeFee").text(activeRule.chargeFee+" 元/度"); 
			 }
			
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
			 if(chargePile.pileToType == 2){//自行车
				 $("#unactiveRule_chargeFee").text(unactiveRule.chargeFee+" 小时/元");
			 }else{
				 $("#unactiveRule_chargeFee").text(unactiveRule.chargeFee+" 元/度"); 
			 }
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

function deleteStation(obj){
	param={};
	var tempStationId =$(obj).attr("stationId");
	if(isEmpty(tempStationId) || tempStationId <= 0){
		showInfo("参数非法");
		return false;
	}
	param.stationId=tempStationId;
	var option = {
			title: "系统提示",
			btn: window.wxc.xcConfirm.btnEnum.okcancel,
			onOk: function(){
				
				$.ajax({
					type:"POST",
					url:"device/station_deleteStation.action",
					data:param,
					dataType:'json',
			        cache: false,
			        beforeSend:showLoading,
			        complete:hideLoading,
			        success: function(data){
			        	if(data.success){
			        		var opt={
			        				onOk:reload,
			        				onClose:reload
			        		}
			        		showInfo("删除成功", opt)
						}else{
							showMsg(data.message, data.errormsg);
						}
			         }
			     });
			}
		}
		showConfirm("确定删除'"+$(obj).attr("stationName")+"'吗?",option);
}

function wrapCheckedData(){
	tempIssuedObj={};
	tempIssuedObj.ids=[];
	tempIssuedObj.stationNames=[];
	$("input[name='subcheck']:checked").each(function(index,data){
		tempIssuedObj.ids.push($(data).val())
		tempIssuedObj.stationNames.push($(data).attr("stationName"));
	})
}
