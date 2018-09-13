function initParam(){
	//如果默认创锐的桩桩编码自动生成start
	//pileModel = $("#pileModel").val();
	if($("#comType").val() == CR_MODEL){//创锐
		$("#pileCode").attr("disabled",true);
	}
	//end
	$("#pileModel").on("change",function(){
		if($("#comType").val() == CR_MODEL){
			if(!isEmpty($("#comAddr").val())){
				temp = $("#comAddr").val()+"0"+$("#comSubAddr").val();
				$("#pileCode").val(temp).attr("disabled",true);	
			}else{
				$("#pileCode").val("").attr("disabled",true);
			}
			
		}else{
			$("#pileCode").val("").attr("disabled",false);
		}
	});
	$("#comAddr").on("change",function(){
		if($("#comType").val() == CR_MODEL){
			if(!isEmpty($("#comAddr").val())){
				temp = $("#comAddr").val()+"0"+$("#comSubAddr").val();
				$("#pileCode").val(temp);	
			}else{
				$("#pileCode").val("");	
			}
		}
	});
	$("#comSubAddr").on("change",function(){
		if($("#comType").val() == CR_MODEL && !isEmpty($("#comAddr").val())){
			temp = $("#comAddr").val()+"0"+$("#comSubAddr").val();
			$("#pileCode").val(temp);
		}
	});
}
$(function() {
		initParam();
		$("#chargeFee").on("change",function(){
			tempFee = $(this).val().trim();
			 if(!tempFee || isNaN(tempFee)){
				opt={};
				opt.onOk = function(){$("#chargeFee").val("").focus();}
				opt.onClose = opt.onOk;
				showWarning("请输入金额数字！！",opt);
			}else{
				tempFee = parseFloat(tempFee).toFixed(2);
				$("#chargeFee").val(tempFee);
			}
		});
		$("#parkFee").on("change",function(){
			tempFee = $(this).val().trim();
			 if(!tempFee || isNaN(tempFee)){
				var opt={};
				opt.onOk = function(){$("#parkFee").val("").focus();}
				opt.onClose = opt.onOk;
				showWarning("请输入金额数字！！",opt);
			}else{
				tempFee = parseFloat(tempFee).toFixed(2);
				$("#parkFee").val(tempFee);
			}
		});
		$("#serviceFee").on("change",function(){
			tempFee = $(this).val().trim();
			 if(!tempFee || isNaN(tempFee)){
				var opt={};
				opt.onOk = function(){$("#serviceFee").val("").focus();}
				opt.onClose = opt.onOk;
				showWarning("请输入金额数字！！",opt);
			}else{
				tempFee = parseFloat(tempFee).toFixed(2);
				$("#serviceFee").val(tempFee);
			}
		});
		function cheack(pile) {
			if (isEmpty(pile.pileName)) {
				return "请输入充电桩名称！！";
			} else if (isEmpty(pile.pileCode)) {
				return "请输入充电桩编号！！";
			} else if (isEmpty(pile.buildTime)) {
				return "请输入充电桩安装时间！！";
			}else if (isEmpty(pile.address)) {
				return "请输入详细地址！！";
			} else if(isEmpty(pile.comAddr)){
				return "请输入通讯地址！！";
			}else if (isEmpty(pile.payWay)) {
				return "请选择支付方式！！";
			} else if (pile.feeRule == 1) {
				if (!pile.chargeFee) {
					return "请输入单一充电费金额！！";
				} else if (isNaN(pile.chargeFee)) {
					return "输入的充电费金额必须为数字且保留2位有效数字！！";
				}
			}
			if (isEmpty(pile.parkFee)) {
				return "请输入停车费用金额！！";
			} else if (isEmpty(pile.serviceFee)) {
				return "请输入服务费用金额！！";
			} else if (isEmpty(pile.activeTime)) {
				return "请选择费用规则启用时间！！";
			}else{
				temp = new Date(pile.activeTime);
			    dd = new Date(currentTime);
			    if(temp < dd){
			    	return "启用时间不能小于当前日！！";
			    }
		}

			return "success";
		}
		function initFeeRuleMsg2(bean, selectFeeRule) {
			for ( var i in bean) {
				var obj = chargeRuleListJson[i];
				if (obj.id == selectFeeRule) {
					jian = parseFloat(obj.jianFee);
					feng = parseFloat(obj.fengFee);
					ping = parseFloat(obj.pingFee);
					gu = parseFloat(obj.guFee);
					$("#feeRuleMsg").html(
							"<input class='form-control' disabled='disabled' value='"+"尖：" + jian.toFixed(2) + "元; 峰：" + feng.toFixed(2)
									+ "元; 平：" + ping.toFixed(2) + "元; 谷："
									+ gu.toFixed(2) + "元'/>");
					break;
				}
			}
		}
		var param = {};
		var msg;
		var actionType;
		var addNewStationPileByAjaxUrl = "device_addStationPileByAjax.action";
		var pileObj;
		var isNext = true;
		var finishUrl = "deviceManager/searchStations.action";
		var showDetailPileModelUrl = "device_showDetailPileModel.action";
		
		resetForm($("#pileForm"));
		$("#chargeFee").attr("disabled",false);
		initOneBootStrapDate($("#oneTimeDate"));//初始化时间插件
		initMinBootStrapDate($("#oneTimeDate2"),new Date(currentTime));//初始化时间插件
		commomChangeDoc("addPiledoc");
		$("#feeRule").on("change", function() {
			var feeRule = $("#feeRule").val();
			if (feeRule == 1) {
				$("#chargeFee").attr("disabled",false);
				$("#chargeFeeDiv").removeClass("hide");
				$("#feeRuleMsgDiv").addClass("hide");
			} else {
				initFeeRuleMsg2(chargeRuleListJson, feeRule);
				$("#chargeFee").attr("disabled",true).val("");
				$("#chargeFeeDiv").addClass("hide");
				$("#feeRuleMsgDiv").removeClass("hide");
			}
		});
		$("#nextPileBtn").on("click", function() {
			$(this).attr("disabled",true);
			$("#finishBtn").attr("disabled",false);
			$("#pileName").val("").focus();
			$("#pileCode").val("");
			$("#comAddr").val("");
		/* 	pileObj = getFormJson($("#pileForm"))
			if (pileObj.payWay && pileObj.payWay instanceof Array) {
				pileObj.payWay = pileObj.payWay.join();
			}
			msg = cheack(pileObj);
			if ("success" != msg) {
				showWarning(msg)
			}
			else{
				isNext = false;
				$("#nextPileBtn").click();
			} */
		});
		$("#finishBtn").on("click",
						function() {
							actionType = $("#actionType").val();
							if (actionType) {
						 		if (actionType == "addNewStationPile") {
									//var addNextStationPileUrl = "deviceManager/addNewStationPile.action";
									param.newStationId = $("#stationId").val();
									/* addNextStationPileUrl = addNextStationPileUrl
											+ "?newStationId="
											+ param.newStationId; */
								} else if (actionType == "addValidStationPile") {
									//var addNextValidStationPileUrl = "deviceManager/addValidStationPile.action";
									param.validStationId = $("#stationId").val();
									/* addNextValidStationPileUrl = addNextValidStationPileUrl
											+ "?validStationId="
											+ param.validStationId; */
								} 
								param.actionType = actionType;
								pileObj = getFormJson($("#pileForm"))
								if (pileObj.payWay && pileObj.payWay instanceof Array) {
									pileObj.payWay = pileObj.payWay.join();
								}
								if($("#comType").val() == CR_MODEL){
									pileObj.pileCode = $("#pileCode").val();
								}
								msg = cheack(pileObj);
								if ("success" != msg) {
									showWarning(msg)
								}
								else{
								param.docType = ".doc";
								param.pobChargingTempPile = JSON.stringify(pileObj);
								//param.payWay = payWay.toString();
								$("#pileForm").ajaxSubmit(
												{
													url : addNewStationPileByAjaxUrl,
													type : 'post',
													dataType : 'json',
													data : param,
													beforeSubmit:function(){$("#loading").removeClass("hide")},
													success : function(data) {
														$("#loading").addClass("hide");
														 if(data.userLoginStatus){
												    		  checkLoginStatus(data,true);
												    		  return;
												    	  }
														if (data.map.msg == "success") {
															showWarning("添加成功！！");
															$("#finishBtn").attr("disabled",true);
															$("#nextPileBtn").attr("disabled",false);
								/* 							if (actionType == "addNewStationPile") {
																if (isNext) {
																	href(addNextStationPileUrl);
																} else {
																	href("deviceManager/searchUnValidDevice.action");
																}

															} else if (actionType == "addValidStationPile") {
																if (isNext) {
																	href(addNextValidStationPileUrl);
																} else {
																	href("deviceManager/searchValidDevice.action");
																}
															} */

														} else {
															showWarning(data.map.msg);
														}
													}

												});
							}
							} else {
								showError("非法提交！！");
							}
						});
		$("#returnAddPileBtn").on("click",function(){
			$("#showPileModelDiv").addClass("hide");
			$("#addPileDiv").removeClass("hide");
		});
		$("#detailPileModelBtn").on("click",function(){
			pileModelId = $("#pileModel").val();
	 		if(pileModelId > 0){
				param.pileModelId=pileModelId;
				$.post(showDetailPileModelUrl,param,function(data){
					 if(data.userLoginStatus){
			    		  checkLoginStatus(data,true);
			    		  return;
			    	  }
					if("success" == data.map.msg){
						busPileModel = data.map.busPileModel;
						temp = parseInt(busPileModel.chaWay);
						str="";
						if(temp == 1){
							str = "交流";
						}
						else if(temp == 2){
							str = "直流";
						}
						else{
							str="";
						}
						
						$("#modelChaWay").text(str);
						temp = parseInt(busPileModel.chaType);
						if(temp == 1){
							str = "快充";
						}
						else if(temp == 2){
							str = "慢充";
						}
						else if(temp == 3){
							str = "超充";
						}
						else{
							str="";
						}
						$("#modelChaType").text(str);
						temp = parseInt(busPileModel.isIntelligent);
						if(temp == 1){
							str = "是";
						}
						else{
							str = "否";
						}
						$("#modelIsIntelligent").text(str);
						$("#modelBrand").text(getNotNullData(busPileModel.brand));
						$("#modelStandard").text(getNotNullData(busPileModel.standard));
						$("#modelInV").text(getNotNullData(busPileModel.inV));
						$("#modelOutV").text(getNotNullData(busPileModel.outV));
						$("#modelMaxP").text(getNotNullData(busPileModel.maxP));
						$("#modelRatP").text(getNotNullData(busPileModel.ratP));
						$("#modelHull").text(getNotNullData(busPileModel.hull));
						$("#modelSize").text(getNotNullData(busPileModel.size));
						$("#modelProLevel").text(getNotNullData(busPileModel.proLevel));
						$("#modelLineLen").text(getNotNullData(busPileModel.lineLen));
						$("#modelRate").text(getNotNullData(busPileModel.rate));
						$("#modelMeaAcc").text(getNotNullData(busPileModel.meaAcc));
						$("#modelWeight").text(getNotNullData(busPileModel.weight));
						$("#modelWindow").text(getNotNullData(busPileModel.window));
						$("#modelWorkTem").text(getNotNullData(busPileModel.workTem));
						$("#modelRelaHum").text(getNotNullData(busPileModel.relaHum));
						$("#modelAltitude").text(getNotNullData(busPileModel.altitude));
						$("#modelInsMethod").text(getNotNullData(busPileModel.insMethod));
						$("#modelWorkSta").text(getNotNullData(busPileModel.workSta));
						$("#modelIdentify").text(getNotNullData(busPileModel.identify));
						$("#addPileDiv").addClass("hide");
						$("#showPileModelDiv").removeClass("hide");
					}
					else{
						showWarning(data.map.msg);
					}
				});
			} 
		});
	})