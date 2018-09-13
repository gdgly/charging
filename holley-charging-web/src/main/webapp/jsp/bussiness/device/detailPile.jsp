<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<%@include file="../../common/global/meta.jsp"%>
<%@include file="../../common/global/js.jsp"%>
<%@include file="../head.jsp"%>
<html lang="zh-CN">
<head>
<style type="text/css">
</style>
</head>
<body>
	<!--main-->
	<div class="bg" style="height: 950px;">
		<!--main-content-->
		<div class="container main-body" style="height: 92%;">
			<div class="row">
				<div class="col-sm-3">
					<h3>桩设备信息</h3>
				</div>
				<div class="col-sm-offset-7 col-sm-2 text-right"
					style="margin-top: 21px;">
					<s:if test="#request.validOrUnValid == 'valid'">
						<button onclick="href('deviceManager/searchValidDevice.action?stationId=${backStationId}');"
							type="button" class="btn btn-warning btn-sm">返回</button>
					</s:if>
					<s:elseif test="#request.validOrUnValid == 'unValid'">
						<button onclick="href('deviceManager/searchUnValidDevice.action');"
							type="button" class="btn btn-warning btn-sm">返回</button>
					</s:elseif>
				</div>
			</div>
			<hr />
			<form class="form-horizontal" role="form" id="pileForm">
			<h5>基本信息</h5>
				<div class="form-group">
					<label for="pileName" class="col-sm-2 control-label">电桩名称：</label>
					<div class="col-sm-4">
						<input disabled="disabled" type="text" class="form-control" id="pileName"
							name="pileName" value="${pobChargingPile.pileName}" />
					</div>
					<label for="pileType" class="col-sm-2 control-label">充电类型：</label>
					<div class="col-sm-4">
						<select disabled="disabled" id="pileType" name="pileType" class="form-control">
							<s:iterator value="#request.pileTypeList" status="statu"
								id="item">
								<s:if test="value==#request.pobChargingPile.pileType">
									<option value='<s:property value="value"/>' selected="selected"><s:property
											value="name" /></option>
								</s:if>
								<s:else>
									<option value='<s:property value="value"/>'><s:property
											value="name" /></option>
								</s:else>
							</s:iterator>
						</select>
					</div>
				</div>

				<div class="form-group">
					<label for="pileCode" class="col-sm-2 control-label">电桩编号：</label>
					<div class="col-sm-4">
						<input disabled="disabled" type="text" class="form-control" id="pileCode"
							name="pileCode" value="${pobChargingPile.pileCode}" />
					</div>
					<label for="buildTime" class="col-sm-2 control-label">安装时间：</label>
					<div class="col-sm-4">
					<div class='input-group date' id='oneTimeDate'>
							<input disabled="disabled" id="buildTime" name="buildTime" type='text' value='<s:date name="#request.pobChargingPile.buildTime" format="yyyy/MM/dd" />'
								class="form-control" /> <span class="input-group-addon"><span
								class="glyphicon glyphicon-calendar"></span> </span>
						</div>
					</div>
				</div>

				<div class="form-group">
					<label for="chaWay" class="col-sm-2 control-label">充电方式：</label>
					<div class="col-sm-4">
						<s:iterator value="#request.chaWayList" status="statu" id="item">
							<s:if test="value == #request.pobChargingPile.chaWay">
								<label class="checkbox-inline"> <input type="radio" disabled="disabled"
									name="chaWay" id="chaWay" value='<s:property value="value"/>'
									checked > <s:property value="name" />
								</label>
							</s:if>
							<s:else>
								<label class="checkbox-inline"> <input type="radio" disabled="disabled"
									name="chaWay" id="chaWay" value='<s:property value="value"/>'>
									<s:property value="name" />
								</label>
							</s:else>
						</s:iterator>

					</div>
					<label for="pileModel" class="col-sm-2 control-label">电桩型号：</label>
					<div class="col-sm-4">
						<select disabled="disabled" id="pileModel" name="pileModel" class="form-control">
							<s:iterator value="#request.busPileModelList" status="statu" id="item">
								<s:if test="id == #request.pobChargingPile.pileModel">
									<option selected="selected" value='<s:property value="id"/>'><s:property
											value="brand" /></option>
								</s:if>
								<s:else>
									<option value='<s:property value="id"/>'><s:property
											value="brand" /></option>
								</s:else>
							</s:iterator>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label for="address" class="col-sm-2 control-label">详细地址：</label>
					<div class="col-sm-4">
						<textarea disabled="disabled" type="text" class="form-control" id="address" rows="3"
							name="address"><s:property value="#request.pobChargingPile.address"/></textarea>
					</div>
					<label for="validRemark" class="col-sm-2 control-label">审核备注：</label>
					<div class="col-sm-4">
						<textarea disabled="disabled" type="text" class="form-control" id="validRemark" rows="3"
							name="validRemark"><s:property value="#request.pobChargingPile.validRemark"/></textarea>
					</div>
			<!-- 			<label for="doc" class="col-sm-2 control-label">上传文件：</label>
					<div class="col-sm-4">
						<input type="file" id="editPileDoc" name="doc" />
					</div> -->
				</div>
		<hr class="dashed">
		<h5>通讯信息</h5>
		<div class="form-group">
					<label for="comType" class="col-sm-2 control-label">通讯协议：</label>
					<div class="col-sm-4">
						<select disabled="disabled" id="comType" name="comType" class="form-control">
							<s:iterator value="#request.comTypeList" status="statu" id="item">
								<s:if test="value == #request.pobChargingPile.comType">
									<option selected="selected" value='<s:property value="value"/>'><s:property
											value="name" /></option>
								</s:if>
								<s:else>
									<option value='<s:property value="value"/>'><s:property
											value="name" /></option>
								</s:else>
							</s:iterator>
						</select>
					</div>
						<label for="comAddr" class="col-sm-2 control-label">通讯地址：</label>
					<div class="col-sm-4">
						<input disabled="disabled" type="text" class="form-control" value='<s:property value="#request.pobChargingPile.comAddr"/>' />
					</div>
					</div>
					<div class="form-group">
					<label for="comType" class="col-sm-2 control-label">
					<span style="color: red;">*</span>通讯子地址：</label>
					<div class="col-sm-4">
						<select id="comSubAddr" name="comSubAddr" class="form-control" disabled="disabled">
							<s:iterator value="#request.comSubAddrList" id="item" status="statu">
							<s:if test="#request.pobChargingPile.comSubAddr == value">
							<option value="<s:property value='value'/>" selected="selected"><s:property
										value='name' /></option>
							</s:if>
							<s:else>
							<option value="<s:property value='value'/>"><s:property
										value='name' /></option>
							</s:else>
								
							</s:iterator>
						</select>
					</div>
		</div>
		<hr class="dashed">
		<h5>支持类型</h5>
				<div class="form-group">

				<label for="payWay" class="col-sm-2 control-label">支付方式：</label>
					<div class="col-sm-4" id="payWay">
						<s:iterator value="#request.payWayList" status="statu" id="item">
							<label class="checkbox-inline"> <input type="checkbox" disabled="disabled"
								value="<s:property value='value'/>" name="payWay"> <s:property
									value='name' />
							</label>
						</s:iterator>
					</div>
						<label for="chaType" class="col-sm-2 control-label">充电模式：</label>
					<div class="col-sm-4" id="chaType">
							<label class="checkbox-inline"> 
							<s:if test="#request.pobChargingPile.isTimeCha == 1">
							<input type="checkbox" disabled="disabled" checked="checked" value="1" name="isTimeCha"> 定时
							</s:if>
							<s:else>
							<input type="checkbox" disabled="disabled" value="1" name="isTimeCha"> 定时
							</s:else>
							</label>
							<label class="checkbox-inline"> 
							<s:if test="#request.pobChargingPile.isRationCha == 1">
							<input type="checkbox" disabled="disabled" checked="checked"  value="1" name="isRationCha"> 定量
							</s:if>
							<s:else>
								<input type="checkbox" disabled="disabled" value="1" name="isRationCha"> 定量
							</s:else>
							</label>
							<label class="checkbox-inline"> 
							<s:if test="#request.pobChargingPile.isMoneyCha == 1">
								<input checked="checked" disabled="disabled" type="checkbox" value="1" name="isMoneyCha"> 定金额
							</s:if>
							<s:else>
								 <input type="checkbox" disabled="disabled" value="1" name="isMoneyCha"> 定金额
							</s:else>
							</label>
					</div>
				</div>
						<div class="form-group">
				<label for="isApp" class="col-sm-2 control-label">支持预约：</label>
					<div class="col-sm-4">
						<s:iterator value="#request.isOrNoList" id="item" status="statu">
							<s:if test="#request.pobChargingPile.isApp == value">
								<label class="checkbox-inline"> <input type="radio" disabled="disabled"
									name="isApp" id="isApp" value='<s:property value="value"/>'
									checked> <s:property value="name" />
								</label>
							</s:if>
							<s:else>
								<label class="checkbox-inline"> <input type="radio" disabled="disabled"
									name="isApp" id="isApp" value='<s:property value="value"/>'>
									<s:property value="name" />
								</label>
							</s:else>
						</s:iterator>
					</div>
				</div>
		<hr class="dashed">
		
		<s:if test="#request.showPileType == 'validPile' && #request.activeRule != null">
		<div class="form-group">
			<div class="col-sm-12 text-center">
				<a href="javascript:;" id="showActiveChargeRuleBtn">显示在使用电费规则</a>
			</div>
		</div>
		<div id="activeChargeRule" style="display: none;" >
		<h5>正在使用电费规则</h5>
			<div class="form-group">
				<label for="activeFeeRule" class="col-sm-2 control-label">电费规则：</label>
					<div class="col-sm-4">
						<select id="activeFeeRule" disabled="disabled" class="form-control">
							<s:iterator value="#request.chargeRuleList" id="item"
								status="statu">
								<s:if test="#request.activeRule.id == id">
								<option selected="selected" value="<s:property value='id'/>"><s:property
										value='name' /></option>
								</s:if>
							</s:iterator>
						</select>
					</div>
					<s:if test="#request.activeRule.id == 1">
					<label for="activeChargeFee" class="col-sm-2 control-label">单一电费(元)：</label>
					<div class="col-sm-4">
						<input disabled="disabled" value='<s:property value="#request.activeRule.chargeFee"/>' type="text" class="form-control" id="activeChargeFee"/>
					</div>
					</s:if>
					<s:else>
					<label for="activeFeeRuleMsg" class="col-sm-2 control-label">电费规则说明：</label>
					<div id="activeFeeRuleMsg" class="col-sm-4">
					<input type="text" class="form-control" disabled="disabled" value="尖：${activeRule.jianFee}元; 峰：${activeRule.fengFee}元; 平：${activeRule.pingFee}元; 谷：${activeRule.guFee}元;"/>
				<%-- 	尖费用：<s:property value="#request.activeRule.jianFee"/>
					峰费用：<s:property value="#request.activeRule.fengFee"/>
					平费用：<s:property value="#request.activeRule.pingFee"/>
					谷费用：<s:property value="#request.activeRule.guFee"/> --%>
					</div>
					</s:else>
			</div>
			<div class="form-group">
					<label for="activeParkFee" class="col-sm-2 control-label">停车费(元)：</label>
					<div class="col-sm-4">
						<input disabled="disabled" value='<s:property value="#request.activeRule.parkFee"/>' type="text" class="form-control" id="activeParkFee"/>
					</div>
					<label for="activeServiceFee" class="col-sm-2 control-label">服务费(元)：</label>
					<div class="col-sm-4">
						<input disabled="disabled" value='<s:property value="#request.activeRule.serviceFee"/>' type="text" class="form-control" id="activeServiceFee"/>
					</div>
			</div>
			<div class="form-group">
					<label for="activeTimeOk" class="col-sm-2 control-label">启用时间：</label>
					<div class="col-sm-4">
						<input disabled="disabled" value='<s:date name="#request.activeRule.activeTime" format="yyyy/MM/dd" />' type="text" class="form-control" id="activeTimeOk"/>	
					</div>
				</div>
		<hr class="dashed">
	</div>
	</s:if>
	<h5>电费规则</h5>
	<s:if test="#request.showPileType == 'validPile'">
	              <s:if test="#request.unActiveRule != null">
					<div class="form-group">
					<label for="feeRule" class="col-sm-2 control-label">电费规则：</label>
					<div class="col-sm-4">
						<select disabled="disabled" id="feeRule" name="feeRule" class="form-control">
							<s:iterator value="#request.chargeRuleList" id="item"
								status="statu">
								<s:if test="#request.unActiveRule.id == id">
									<option selected="selected" value="<s:property value='id'/>"><s:property
											value='name' /></option>
								</s:if>
								<s:else>
									<option value="<s:property value='id'/>"><s:property
											value='name' /></option>
								</s:else>
							</s:iterator>
						</select>
					</div>
					<div id="chargeFeeDiv">
						<label for="chargeFee" class="col-sm-2 control-label">单一电费(元)：</label>
						<div class="col-sm-4">
							<input disabled="disabled" type="text" class="form-control" id="chargeFee" name="chargeFee"
							value='<s:property value="#request.unActiveRule.chargeFee"/>' />
						</div>
					</div>
					
					 <div id="feeRuleMsgDiv" class="hide">
						<label for="feeRuleMsg" class="col-sm-2 control-label">电费规则说明：</label>
					<div id="feeRuleMsg" class="col-sm-4">
					</div>
					</div>
				</div>
				<div class="form-group">
					<label for="parkFee" class="col-sm-2 control-label">停车费(元)：</label>
					<div class="col-sm-4">
						<input disabled="disabled" type="text" class="form-control" id="parkFee"
							name="parkFee"
							value='<s:property value="#request.unActiveRule.parkFee"/>' />
					</div>
					<label for="serviceFee" class="col-sm-2 control-label">服务费(元)：</label>
					<div class="col-sm-4">
						<input disabled="disabled" type="text" class="form-control" id="serviceFee"
							name="serviceFee"
							value='<s:property value="#request.unActiveRule.serviceFee"/>' />
					</div>
				</div>
				<div class="form-group">
					<label for="activeTime" class="col-sm-2 control-label">启用时间：</label>
					<div class="col-sm-4">
							<div class='input-group date' id='oneTimeDate2'>
							<input disabled="disabled" id="activeTime" name="activeTime" type='text' value='<s:date name="#request.unActiveRule.activeTime" format="yyyy/MM/dd" />'
								class="form-control" /> <span class="input-group-addon"><span
								class="glyphicon glyphicon-calendar"></span> </span>
						</div>
					</div>
				</div>
				</s:if>
				<s:else>
				<p class="text-center help-block" style="margin-top: 70px;">暂无待激活电费规则</p>
				</s:else>
	</s:if>
	<s:if test="#request.showPileType == 'unValidPile'">
	 <s:if test="#request.pobChargingPile.feeRule > 0">
				<div class="form-group">
					<label for="feeRule" class="col-sm-2 control-label">电费规则：</label>
					<div class="col-sm-4">
						<select disabled="disabled" id="feeRule" name="feeRule" class="form-control">
							<s:iterator value="#request.chargeRuleList" id="item"
								status="statu">
								<s:if test="#request.pobChargingPile.feeRule == id">
									<option selected="selected" value="<s:property value='id'/>"><s:property
											value='name' /></option>
								</s:if>
								<s:else>
									<option value="<s:property value='id'/>"><s:property
											value='name' /></option>
								</s:else>
							</s:iterator>
						</select>
					</div>
						<div id="chargeFeeDiv">
					<label for="chargeFee" class="col-sm-2 control-label">单一电费(元)：</label>
					<div class="col-sm-4">
						<input type="text" disabled="disabled" class="form-control" id="chargeFee" name="chargeFee"
							value='<s:property value="#request.pobChargingPile.chargeFee"/>' />
					</div>
					</div>
					
					 <div id="feeRuleMsgDiv" class="hide">
						<label for="feeRuleMsg" class="col-sm-2 control-label">电费规则说明：</label>
					<div id="feeRuleMsg" class="col-sm-4">
					</div>
					</div>
				</div>
				<div class="form-group">
					<label for="parkFee" class="col-sm-2 control-label">停车费(元)：</label>
					<div class="col-sm-4">
						<input disabled="disabled" type="text" class="form-control" id="parkFee"
							name="parkFee"
							value='<s:property value="#request.pobChargingPile.parkFee"/>' />
					</div>
					<label for="serviceFee" class="col-sm-2 control-label">服务费(元)：</label>
					<div class="col-sm-4">
						<input disabled="disabled" type="text" class="form-control" id="serviceFee"
							name="serviceFee"
							value='<s:property value="#request.pobChargingPile.serviceFee"/>' />
					</div>
				</div>
				<div class="form-group">
					<label for="activeTime" class="col-sm-2 control-label">启用时间：</label>
					<div class="col-sm-4">
							<div class='input-group date' id='oneTimeDate2'>
							<input disabled="disabled" id="activeTime" name="activeTime" type='text' value='<s:date name="#request.pobChargingPile.activeTime" format="yyyy/MM/dd" />'
								class="form-control" /> <span class="input-group-addon"><span
								class="glyphicon glyphicon-calendar"></span> </span>
						</div>
					</div>
				</div>
				</s:if>
				<s:else>
             		<p class="text-center help-block" style="margin-top: 70px;">无电费修改信息</p>
             	</s:else>
			</s:if>
             
			</form>
		</div>
	</div>
	<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
</body>
<script type="text/javascript">
	$(function() {
		var chargeRuleListJson = ${chargeRuleListJson};
		var myPayWay = '${pobChargingPile.payWay}';
		var showPileType = '${showPileType}';
		var backUrl = "deviceManager/searchUnValidPiles.action";
		var feeRule = '${pobChargingPile.feeRule}';
		 if("validPile" == showPileType){
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
				$("#chargeFeeDiv").addClass("hide");
				$("#feeRuleMsgDiv").removeClass("hide");
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
		temp1 = $(".bg").css("height");
 		$("#showActiveChargeRuleBtn").on("click",function(){
			$("#activeChargeRule").slideToggle(0,function(){
				temp2 = $(".bg").css("height");
				if(temp1 == temp2){
					$(".bg").css("height","1200px");
				}else{
					$(".bg").css("height",temp1);
				}
				var tempStr = $("#showActiveChargeRuleBtn").text();
				if( tempStr == "显示在使用电费规则"){
					$("#showActiveChargeRuleBtn").text("收起");
				}
				else if(tempStr == "收起"){
					$("#showActiveChargeRuleBtn").text("显示在使用电费规则");
				}
			});
		}); 
	})
</script>
</html>

