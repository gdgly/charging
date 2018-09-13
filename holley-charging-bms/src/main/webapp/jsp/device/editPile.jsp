<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<%@ include file="../common/global/top.jsp" %>
<%@include file="../common/global/meta.jsp"%>
<%@include file="../common/global/js.jsp" %>
<html lang="zh-CN">
<head>
<style type="text/css">
</style>
</head>
<body>
 <%@include file="../common/global/header.jsp"%>
	<!--main-->
		<!--main-content-->
		<div class="container" id="editPileDiv">
			<form class="form-horizontal" role="form" id="pileForm">
			<h5>基本信息</h5>
				<div class="form-group">
					<label for="pileName" class="col-sm-2 control-label"><span
						style="color: red;">*</span>电桩名称：</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="pileName"
							name="pileName" value="${pobChargingPile.pileName}" maxlength="30"/>
					</div>
					<label for="pileType" class="col-sm-2 control-label"><span
						style="color: red;">*</span>充电类型：</label>
					<div class="col-sm-4">
						<select id="pileType" name="pileType" class="form-control">
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
					<label for="pileCode" class="col-sm-2 control-label"><span
						style="color: red;">*</span>电桩编号：</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="pileCode"
							name="pileCode" value="${pobChargingPile.pileCode}" maxlength="30"/>
					</div>
					<label for="buildTime" class="col-sm-2 control-label"><span
						style="color: red;">*</span>安装时间：</label>
					<div class="col-sm-4">
						<div class='input-group date' id='oneTimeDate'>

							<input id="buildTime" name="buildTime" type='text'
								class="form-control"
								value='<s:date name="#request.pobChargingPile.buildTime" format="yyyy/MM/dd" />' />
							<span class="input-group-addon"><span
								class="glyphicon glyphicon-calendar"></span> </span>
						</div>
					</div>
				</div>

				<div class="form-group">
					<label for="chaWay" class="col-sm-2 control-label"><span
						style="color: red;">*</span>充电方式：</label>
					<div class="col-sm-4">
						<s:iterator value="#request.chaWayList" status="statu" id="item">
							<s:if test="value == #request.pobChargingPile.chaWay">
								<label class="checkbox-inline"> <input type="radio"
									name="chaWay" id="chaWay" value='<s:property value="value"/>'
									checked> <s:property value="name" />
								</label>
							</s:if>
							<s:else>
								<label class="checkbox-inline"> <input type="radio"
									name="chaWay" id="chaWay" value='<s:property value="value"/>'>
									<s:property value="name" />
								</label>
							</s:else>
						</s:iterator>
					</div>
					<label for="pileModel" class="col-sm-2 control-label"><span
						style="color: red;">*</span>电桩型号：</label>
					<div class="col-sm-4">
					<div class="form-group">
					<div class="col-sm-10">
						<select id="pileModel" name="pileModel" class="form-control">
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
						<div class="col-sm-2">
							<lable id="detailPileModelBtn" class="glyphicon glyphicon-zoom-in" title="查看详情"
							style="top:7px;cursor:pointer;font-size: 20px;" />
						</div>
					</div>
				</div>
			</div>
				<div class="form-group">
					<label for="address" class="col-sm-2 control-label"><span
						style="color: red;">*</span>详细地址：</label>
					<div class="col-sm-4">
						<textarea type="text" class="form-control" id="address" rows="3"
							name="address" placeholder="最多填写150个字符..." maxlength="150"><s:property value="#request.pobChargingPile.address"/></textarea>
					</div>
						<!-- <label for="doc" class="col-sm-2 control-label">上传文件：</label>
					<div class="col-sm-4">
						<input type="file" id="editPileDoc" name="doc" />
					</div> -->
				</div>
		<hr class="dashed">
		<h5>通讯信息</h5>
		<div class="form-group">
					<label for="comType" class="col-sm-2 control-label"><span
						style="color: red;">*</span>通讯协议：</label>
					<div class="col-sm-4">
						<select id="comType" name="comType" class="form-control">
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
						<label for="comAddr" class="col-sm-2 control-label"><span
						style="color: red;">*</span>通讯地址：</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="comAddr" name="comAddr" maxlength="30" value='<s:property value="#request.pobChargingPile.comAddr"/>'  />
					</div>
					</div>
				<div class="form-group">
				<label for="comType" class="col-sm-2 control-label"><span
						style="color: red;">*</span>通讯子地址：</label>
					<div class="col-sm-4">
						<select id="comSubAddr" name="comSubAddr" class="form-control">
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
				<label for="payWay" class="col-sm-2 control-label"><span
						style="color: red;">*</span>支付方式：</label>
					<div class="col-sm-4" id="payWay">
						<s:iterator value="#request.payWayList" status="statu" id="item">
							<label class="checkbox-inline"> <input type="checkbox"
								value="<s:property value='value'/>" name="payWay"> <s:property
									value='name' />
							</label>
						</s:iterator>
					</div>
						<label for="chaType" class="col-sm-2 control-label">充电模式：</label>
					<div class="col-sm-4" id="chaType">
							<label class="checkbox-inline"> 
							<s:if test="#request.pobChargingPile.isTimeCha == 1">
							<input type="checkbox" checked="checked" value="1" name="isTimeCha"> 定时
							</s:if>
							<s:else>
							<input type="checkbox" value="1" name="isTimeCha"> 定时
							</s:else>
							</label>
							<label class="checkbox-inline"> 
							<s:if test="#request.pobChargingPile.isRationCha == 1">
								<input checked="checked" type="checkbox" value="1" name="isRationCha"> 定量
							</s:if>
							<s:else>
								<input type="checkbox" value="1" name="isRationCha"> 定量
							</s:else>
							</label>
							<label class="checkbox-inline"> 
							<s:if test="#request.pobChargingPile.isMoneyCha == 1">
								<input checked="checked" type="checkbox" value="1" name="isMoneyCha"> 定金额
							</s:if>
							<s:else>
								 <input type="checkbox" value="1" name="isMoneyCha"> 定金额
							</s:else>
							</label>
					</div>
				</div>
						<div class="form-group">
				<label for="isApp" class="col-sm-2 control-label"><span
						style="color: red;">*</span>支持预约：</label>
					<div class="col-sm-4">
						<s:iterator value="#request.isOrNoList" id="item" status="statu">
							<s:if test="#request.pobChargingPile.isApp == value">
								<label class="checkbox-inline"> <input type="radio"
									name="isApp" id="isApp" value='<s:property value="value"/>'
									checked> <s:property value="name" />
								</label>
							</s:if>
							<s:else>
								<label class="checkbox-inline"> <input type="radio"
									name="isApp" id="isApp" value='<s:property value="value"/>'>
									<s:property value="name" />
								</label>
							</s:else>
						</s:iterator>
					</div>
				</div>
		<hr class="dashed">
		<s:if test="#request.activeRule != null">
		<div class="form-group">
			<div class="col-sm-12 text-center">
				<a href="javascript:;" id="showActiveChargeRuleBtn">显示在使用电费规则</a>
			</div>
		</div>
		<div id="activeChargeRule" style="display: none;" >
		<h5>正在使用电费规则</h5>
			<div class="form-group">
				<label for="activeFeeRule" class="col-sm-2 control-label"><span
						style="color: red;">*</span>电费规则：</label>
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
					<label for="activeChargeFee" class="col-sm-2 control-label"><span
						style="color: red;">*</span>单一电费(元)：</label>
					<div class="col-sm-4">
						<input disabled="disabled" maxlength="10" value='<s:property value="#request.activeRule.chargeFee"/>' type="text" class="form-control" id="activeChargeFee"/>
					</div>
					</s:if>
					<s:else>
					<label for="activeFeeRuleMsg" class="col-sm-2 control-label">电费规则说明：</label>
					<div id="activeFeeRuleMsg" class="col-sm-4">
					<input type="text" class="form-control" disabled="disabled" value="尖：${activeRule.jianFee}元; 峰：${activeRule.fengFee}元; 平：${activeRule.pingFee}元; 谷：${activeRule.guFee}元;"/>
					<%-- 尖：<s:property value="#request.activeRule.jianFee"/>
					峰：<s:property value="#request.activeRule.fengFee"/>
					平：<s:property value="#request.activeRule.pingFee"/>
					谷：<s:property value="#request.activeRule.guFee"/> --%>
					</div>
					</s:else>
			</div>
			<div class="form-group">
					<label for="activeParkFee" class="col-sm-2 control-label"><span
						style="color: red;">*</span>停车费(元)：</label>
					<div class="col-sm-4">
						<input maxlength="10" disabled="disabled" value='<s:property value="#request.activeRule.parkFee"/>' type="text" class="form-control" id="activeParkFee"/>
					</div>
					<label for="activeServiceFee" class="col-sm-2 control-label"><span
						style="color: red;">*</span>服务费(元)：</label>
					<div class="col-sm-4">
						<input maxlength="10" disabled="disabled" value='<s:property value="#request.activeRule.serviceFee"/>' type="text" class="form-control" id="activeServiceFee"/>
					</div>
			</div>
			<div class="form-group">
					<label for="activeTimeOk" class="col-sm-2 control-label"><span
						style="color: red;">*</span>启用时间：</label>
					<div class="col-sm-4">
						<input disabled="disabled" value='<s:property value="#request.activeRule.activeTimeDesc"/>' type="text" class="form-control" id="activeTimeOk"/>	
					</div>
				</div>
		<hr class="dashed">
	</div>
	</s:if>
	<h5>电费规则</h5>
					<div class="form-group">
					<label for="feeRule" class="col-sm-2 control-label"><span
						style="color: red;">*</span>电费规则：</label>
					<div class="col-sm-4">
						<select id="feeRule" name="feeRule" class="form-control">
						<option value="0">请选择</option>
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
						<label for="chargeFee" class="col-sm-2 control-label"><span
							style="color: red;">*</span>单一电费(元)：</label>
						<div class="col-sm-4">
							<input type="text" maxlength="10" class="form-control" id="chargeFee" name="chargeFee"
							value='<s:property value="#request.unActiveRule.chargeFee"/>' placeholder="保留小数点后2位"/>
						</div>
					</div>
					
					 <div id="feeRuleMsgDiv" class="hide">
						<label for="feeRuleMsg" class="col-sm-2 control-label">电费规则说明：</label>
					<div id="feeRuleMsg" class="col-sm-4">
					</div>
					</div>
				</div>
				<div class="form-group">
					<label for="parkFee" class="col-sm-2 control-label"><span
						style="color: red;">*</span>停车费(元)：</label>
					<div class="col-sm-4">
						<input maxlength="10" type="text" class="form-control" id="parkFee"
							name="parkFee"
							value='<s:property value="#request.unActiveRule.parkFee"/>' placeholder="保留小数点后2位"/>
					</div>
					<label for="serviceFee" class="col-sm-2 control-label"><span
						style="color: red;">*</span>服务费(元)：</label>
					<div class="col-sm-4">
						<input maxlength="10" type="text" class="form-control" id="serviceFee"
							name="serviceFee"
							value='<s:property value="#request.unActiveRule.serviceFee"/>' placeholder="保留小数点后2位"/>
					</div>
				</div>
				<div class="form-group">
					<label for="activeTime" class="col-sm-2 control-label"><span
						style="color: red;">*</span>启用时间：</label>
					<div class="col-sm-4">
							<div class='input-group date' id='oneTimeDate2'>
						 	<input id="activeTime" name="activeTime" type='text' value='<s:property value="#request.unActiveRule.activeTimeDesc"/>' class="form-control" /> 
						 	<span class="input-group-addon">
							<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</div>
				</div>
	<div class="form-group">
	<div class="col-sm-offset-2 col-sm-4">
	<button id="saveBtn" type="button" disabled="disabled" class="btn btn-warning btn-sm">保存</button>
	<button id="backBtn" type="button" class="btn btn-warning btn-sm">返回</button>
	</div>
	</div>
 </form>
	<input type="hidden" id="pileId" value="${pobChargingPile.id}" />
</div>
		<div class="container hide" id="showPileModelDiv">
			<div class="row">
			<div class="col-sm-8">
			<h3>设备型号详情</h3>
			</div>
			<div class="col-sm-4">
			 <ul class="pager text-right" style="text-align:right;">
    			<li><a id="returnEditPileBtn" href="javascript:;" ><span class="glyphicon glyphicon-menu-left"></span> 返回</a></li>
 			 </ul>
			</div>
			</div>
			<form class="form-horizontal" role="form">
			<div class="form-group">
					<label for="modelBrand" class="col-sm-2 control-label">品牌：</label>
					<div class="col-sm-4">
						<p  id="modelBrand"></p>
					</div>
					<label for="modelChaWay" class="col-sm-2 control-label">充电方式：</label>
					<div class="col-sm-4">
						<p  id="modelChaWay"></p>
					</div>
				</div>
				<div class="form-group">
					<label for="modelChaType" class="col-sm-2 control-label">电桩类型：</label>
					<div class="col-sm-4">
						<p  id="modelChaType"></p>
					</div>
					<label for="modelIsIntelligent" class="col-sm-2 control-label">是否智能：</label>
					<div class="col-sm-4">
						<p  id="modelIsIntelligent"></p>
					</div>
				</div>
				<div class="form-group">
					<label for="modelInV" class="col-sm-2 control-label">输入电压：</label>
					<div class="col-sm-4">
						<p  id="modelInV"></p>
					</div>
					<label for="modelOutV" class="col-sm-2 control-label">输出电压：</label>
					<div class="col-sm-4">
						<p  id="modelOutV"></p>
					</div>
				</div>
				<div class="form-group">
					<label for="modelMaxP" class="col-sm-2 control-label">最大功率：</label>
					<div class="col-sm-4">
						<p  id="modelMaxP"></p>
					</div>
					<label for="modelRatP" class="col-sm-2 control-label">额定功率：</label>
					<div class="col-sm-4">
						<p  id="modelRatP"></p>
					</div>
				</div>
				<div class="form-group">
					<label for="modelHull" class="col-sm-2 control-label">外壳：</label>
					<div class="col-sm-4">
						<p  id="modelHull"></p>
					</div>
					<label for="modelSize" class="col-sm-2 control-label">尺寸：</label>
					<div class="col-sm-4">
						<p  id="modelSize"></p>
					</div>
				</div>
				<div class="form-group">
					<label for="modelProLevel" class="col-sm-2 control-label">防护等级：</label>
					<div class="col-sm-4">
						<p  id="modelProLevel"></p>
					</div>
					<label for="modelLineLen" class="col-sm-2 control-label">缆线长度：</label>
					<div class="col-sm-4">
						<p  id="modelLineLen"></p>
					</div>
				</div>
				<div class="form-group">
					<label for="modelRate" class="col-sm-2 control-label">频率：</label>
					<div class="col-sm-4">
						<p  id="modelRate"></p>
					</div>
					<label for="modelMeaAcc" class="col-sm-2 control-label">计量精度：</label>
					<div class="col-sm-4">
						<p  id="modelMeaAcc"></p>
					</div>
				</div>
				<div class="form-group">
					<label for="modelWeight" class="col-sm-2 control-label">重量：</label>
					<div class="col-sm-4">
						<p  id="modelWeight"></p>
					</div>
					<label for="modelWindow" class="col-sm-2 control-label">用户界面：</label>
					<div class="col-sm-4">
						<p  id="modelWindow"></p>
					</div>
				</div>
				<div class="form-group">
					<label for="modelWorkTem" class="col-sm-2 control-label">工作温度：</label>
					<div class="col-sm-4">
						<p  id="modelWorkTem"></p>
					</div>
					<label for="modelRelaHum" class="col-sm-2 control-label">相对湿度：</label>
					<div class="col-sm-4">
						<p  id="modelRelaHum"></p>
					</div>
				</div>
				<div class="form-group">
					<label for="modelAltitude" class="col-sm-2 control-label">海拔：</label>
					<div class="col-sm-4">
						<p  id="modelAltitude"></p>
					</div>
					<label for="modelInsMethod" class="col-sm-2 control-label">安装方式：</label>
					<div class="col-sm-4">
						<p  id="modelInsMethod"></p>
					</div>
				</div>
				<div class="form-group">
					<label for="modelWorkSta" class="col-sm-2 control-label">工作标准：</label>
					<div class="col-sm-4">
						<p  id="modelWorkSta"></p>
					</div>
					<label for="modelIdentify" class="col-sm-2 control-label">认证：</label>
					<div class="col-sm-4">
						<p  id="modelIdentify"></p>
					</div>
				</div>
				</form>
			</div>

</body>
<script type="text/javascript">
function initParam(){
	//如果默认创锐的桩桩编码自动生成start
	pileModel = $("#pileModel").val();
	if(pileModel == CR_MODEL){//创锐
		$("#pileCode").attr("disabled",true);
	}
	//end
	$("#pileModel").on("change",function(){
		if($(this).val() == CR_MODEL){
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
		if($("#pileModel").val() == CR_MODEL){
			if(!isEmpty($("#comAddr").val())){
				temp = $("#comAddr").val()+"0"+$("#comSubAddr").val();
				$("#pileCode").val(temp);	
			}else{
				$("#pileCode").val("");	
			}
		}
	});
	$("#comSubAddr").on("change",function(){
		if($("#pileModel").val() == CR_MODEL && !isEmpty($("#comAddr").val())){
			temp = $("#comAddr").val()+"0"+$("#comSubAddr").val();
			$("#pileCode").val(temp);
		}
	});
}
	$(function() {
		var chargeRuleListJson = ${chargeRuleListJson};
		var myPayWay = '${pobChargingPile.payWay}';
		var actionType = $("#actionType").val();
		var param = {};
		var msg;
		var maxDocSize = 5;//5M
		var backUrl = "device/pileList.action";
		var feeRule = '${pobChargingPile.feeRule}';
		var showDetailPileModelUrl = "device/pile_showDetailPileModel.action";
		var currentTime = "${currentTime}";
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
		//if("editValidStationPile" == actionType){
			feeRule = "${unActiveRule.id}"
		//}
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
					var editPileByAjaxUrl = "device/pile_editPileByAjax.action";
						param.pileId = $("#pileId").val();
					/* 	editPileByAjaxUrl = editPileByAjaxUrl + "?stationId="
								+ param.stationId; */
						var pileObj = getFormJson($("#pileForm"))
						if (pileObj.payWay && pileObj.payWay instanceof Array) {
							pileObj.payWay = pileObj.payWay.join();
						}
						if($("#pileModel").val() == CR_MODEL){
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
									opt={};
									opt.onOk = function(){reload();}
									opt.onClose = opt.onOk; 
									showSuccess("修改成功！！",opt);
								} else {
									showWarning(data.map.msg);
								}
							}
						})
						}
					

				});
		$("#backBtn").on("click", function() {
			href(backUrl);
		});
		//temp1 = $(".bg").css("height");
		$("#showActiveChargeRuleBtn").on("click",function(){
			$("#activeChargeRule").slideToggle(0,function(){
				/* temp2 = $(".bg").css("height");
				if(temp1 == temp2){
					$(".bg").css("height","1200px");
				}else{
					$(".bg").css("height",temp1);
				} */
				
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
/////////////////////////////////
		function removeDisabled(){
			$("#saveBtn").attr("disabled",false); 
		}
		
		function actionFun(obj,action){
			obj.on(action,removeDisabled);
		}
		function initChangeSaveBtn(){
			actionFun($("input[name='pileName']"),"keydown");
			actionFun($("select[name='pileType']"),"change");
			actionFun($("input[name='pileCode']"),"keydown");
			actionFun($("input[name='buildTime']"),"change");
			actionFun($("textarea[name='address']"),"keydown");
			actionFun($("select[name='comSubAddr']"),"change");
			actionFun($("input[name='comAddr']"),"keydown");
			actionFun($("select[name='feeRule']"),"change");
			actionFun($("input[name='chargeFee']"),"keydown");
			actionFun($("input[name='parkFee']"),"keydown");
			actionFun($("input[name='serviceFee']"),"keydown");
			actionFun($("select[name='pileModel']"),"change");
			actionFun($("select[name='comType']"),"change");
			actionFun($(".input-group-addon"),"click");
			actionFun($("input[name='payWay"),"click");
			actionFun($("input[type='checkbox"),"click");
			actionFun($("input[type='radio"),"click");
			/* 
			actionFun($("textarea[name='address']"),"keydown");
			actionFun($("textarea[name='remark']"),"keydown");
			actionFun($("input[name='img']"),"change");
			actionFun($("input[name='parkType']"),"change");
			actionFun($("select[name='province']"),"change");
			actionFun($("select[name='city']"),"change");
			actionFun($("select[name='openDay']"),"change");
			actionFun($("select[name='openTime']"),"change"); */
		}
		initChangeSaveBtn();
	///////////////////////////////////
	})
</script>
</html>

