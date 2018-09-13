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
		var actionType = $("#actionType").val();
		var param = {};
		var msg;
		var maxDocSize = 5;//5M
		var backUrl = "deviceManager/searchUnValidPiles.action";
		var showDetailPileModelUrl = "device_showDetailPileModel.action";
	
		initParam();
		$("#chargeFee").on("change",function(){
			tempFee = $(this).val().trim();
			 if(!tempFee || isNaN(tempFee)){
				var opt={};
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
		
		if("editValidStationPile" == actionType){
			feeRule = "${unActiveRule.id}"
		}
		function initFeeRuleMsg(bean) {
			for ( var i in bean) {
				var obj = chargeRuleListJson[i];
				if (obj.id == feeRule) {
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
		function initFeeRuleMsg2(bean, selectFeeRule) {
			for ( var i in bean) {
				var obj = bean[i];
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
		//feeRuleMsg
		function inintMyPayWay() {
			if (feeRule && parseInt(feeRule) > 1) {
				initFeeRuleMsg(chargeRuleListJson);
				$("#chargeFee").attr("disabled", true);
				$("#chargeFeeDiv").addClass("hide");
				$("#feeRuleMsgDiv").removeClass("hide");
			} else {
				$("#chargeFee").attr("disabled", false);
			}
			if (myPayWay) {
				var myPayWayList = myPayWay.split(",");
				$(myPayWayList).each(function(index, data) {
					$("#payWay").find("input").each(function(index2, data2) {
						var obj = $(data2);
						if (obj.val() == data) {
							obj.attr("checked", true);
						}
					});
				});
			}

		}
		inintMyPayWay();
		resetForm($("#pileForm"));
		initOneBootStrapDate($("#oneTimeDate"));//初始化时间插件
		initMinBootStrapDate($("#oneTimeDate2"),new Date(currentTime));//初始化时间插件
		commomChangeDoc("editPileDoc");
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
			} 
			
			if(pile.feeRule > 0 || actionType == "editNewStationPile"){
			if(pile.feeRule <= 0){
				return "请选择收费规则！！";
			}
			else if (pile.feeRule == 1) {
				if (!pile.chargeFee) {
					return "请输入单一充电费金额！！";
				} else if (isNaN(pile.chargeFee)) {
					return "输入的充电费金额必须为数字且保留2位有效数字！！";
				}
			}
			if (!pile.parkFee || isNaN(pile.parkFee)) {
				return "请输入停车费用金额！！";
			} else if (!pile.serviceFee || isNaN(pile.serviceFee)) {
				return "请输入服务费用金额！！";
			} else if (isEmpty(pile.activeTime)) {
				return "请选择费用规则启用时间！！";
			} else{
				temp = new Date(pile.activeTime);
				
				    dd = new Date(currentTime);
				    if(temp < dd){
				    	return "启用时间不能小于当前日！！";
				    }
			}
			}
			return "success";
		}

		

		$("#feeRule").on("change", function() {
			var feeRule2 = $("#feeRule").val();
			if(feeRule2 == 0){return;}
			
			if (feeRule2 == 1) {
				$("#chargeFee").attr("disabled",false).val("");
				$("#chargeFeeDiv").removeClass("hide");
				$("#feeRuleMsgDiv").addClass("hide");
			} else {
				initFeeRuleMsg2(chargeRuleListJson, feeRule2);
				$("#chargeFee").attr("disabled",true).val("");
				$("#chargeFeeDiv").addClass("hide");
				$("#feeRuleMsgDiv").removeClass("hide");
			}

		});
		//window.location.href = finishUrl;
		$("#saveBtn").on(
				"click",
				function() {
					//actionType = $("#actionType").val();
					var editPileByAjaxUrl = "device_editPileByAjax.action";
					if (actionType) {
						param.pileId = $("#pileId").val();
						param.actionType = actionType;
						editPileByAjaxUrl = editPileByAjaxUrl + "?stationId="
								+ param.stationId;
						var pileObj = getFormJson($("#pileForm"))
						if (pileObj.payWay && pileObj.payWay instanceof Array) {
							pileObj.payWay = pileObj.payWay.join();
						}
						if($("#comType").val() == CR_MODEL){
							pileObj.pileCode = $("#pileCode").val();
						}
						msg = cheack(pileObj);
						if ("success" != msg) {
							showWarning(msg);
						}
						else{
						param.pobChargingTempPile = JSON.stringify(pileObj);
						$("#pileForm").ajaxSubmit({
							url : editPileByAjaxUrl,
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
									/* var opt={};
									opt.onOk = function(){reload();}
									opt.onClose = opt.onOk; */
									showSuccess("修改成功！！");
								} else {
									showWarning(data.map.msg);
								}
							}
						})
						}
					} else {
						showWarning("非法提交！！");
					}

				});
		$("#backBtn").on("click", function() {
			href(backUrl);
		});
		temp1 = getParentElementById("mainWindow").css("height");
		$("#showActiveChargeRuleBtn").on("click",function(){
			$("#activeChargeRule").slideToggle(0,function(){
				temp2 = getParentElementById("mainWindow").css("height");
				if(temp1 == temp2){
					getParentElementById("mainWindow").css("height","1100px");
				}else{
					getParentElementById("mainWindow").css("height",temp1);
				}
				
				//alert($("#showActiveChargeRuleBtn").text());
				var tempStr = $("#showActiveChargeRuleBtn").text();
				if( tempStr == "显示在使用电费规则"){
				
					$("#showActiveChargeRuleBtn").text("收起");
				}
				else if(tempStr == "收起"){
					$("#showActiveChargeRuleBtn").text("显示在使用电费规则");
				}
			});
		});
		//显示具体桩型号
		$("#returnEditPileBtn").on("click",function(){
			$("#showPileModelDiv").addClass("hide");
			$("#editPileDiv").removeClass("hide");
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
						$("#editPileDiv").addClass("hide");
						$("#showPileModelDiv").removeClass("hide");
					}
					else{
						showWarning(data.map.msg);
					}
				});
			} 
		});
		
	})