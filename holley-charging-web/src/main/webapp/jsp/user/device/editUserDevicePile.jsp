<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
<style>
p{
margin-top: 7px;
}
</style>
    <!-- Content Header (Page header) -->
    <section class="content-header">
       <h1>我的设备</h1> 
      <ol class="breadcrumb">
       	<li>
      	  	<a href="javascript:;" onclick="frameReload();">
      	 		<i class="fa fa-home"></i> 首页
      	 	</a>
      	 </li>
        <li><i class="fa fa-laptop"></i> 设备管理</li>
        <li class="active">我的设备</li>
      </ol>
    </section>
    <!-- Main content -->
<section class="content">
   <div class="row">
       <div class="col-xs-12">
        <div class="box box-info" id="editPileDiv">
            <div class="box-header with-border">
              <h3 class="box-title">修改设备</h3>
					<s:if test="#request.actionType == 'editValidStationPile'">
						<button style="margin-left: 10px;" onclick="href('userDevice/initUserDevice.action?gobackStationId=${backStationId}');" type="button" class="btn btn-info btn-sm pull-right">
							返回
						</button>
					</s:if>
					<s:else>
						<button style="margin-left: 10px;" onclick="href('userDevice/initUserUnDevice.action');" type="button" class="btn btn-info btn-sm pull-right">
							返回
						</button>
					</s:else>
					<button id="saveBtn" type="button" class="btn btn-info btn-sm pull-right">保存</button>
            </div>
            <!-- /.box-header -->
            <!-- form start -->
            <div class="box-body">
            <form class="form-horizontal" role="form" id="pileForm">
			<h5>基本信息</h5>
				<div class="form-group">
					<label for="pileName" class="col-sm-2 control-label"><span
						style="color: red;">*</span>电桩名称：</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="pileName" name="pileName" value="${pobChargingPile.pileName}" maxlength="30"/>
					</div>
					<label for="pileType" class="col-sm-2 control-label">
					<span style="color: red;">*</span>充电类型：
					</label>
					<div class="col-sm-4">
						<select id="pileType" name="pileType" class="form-control">
							<s:iterator value="#request.pileTypeList" status="statu" id="item">
								<s:if test="value==#request.pobChargingPile.pileType">
									<option value='<s:property value="value"/>' selected="selected">
										<s:property value="name" />
									</option>
								</s:if>
								<s:else>
									<option value='<s:property value="value"/>'>
										<s:property value="name" />
									</option>
								</s:else>
							</s:iterator>
						</select>
					</div>
				</div>

				<div class="form-group">
					<label for="pileCode" class="col-sm-2 control-label">
						<span style="color: red;">*</span>电桩编号：
					</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="pileCode" name="pileCode" value="${pobChargingPile.pileCode}" maxlength="30"/>
					</div>
					<label for="buildTime" class="col-sm-2 control-label">
					<span style="color: red;">*</span>安装时间：
					</label>
					<div class="col-sm-4">
						<div class='input-group date' id='oneTimeDate'>
							<input id="buildTime" name="buildTime" type='text' class="form-control" value='<s:date name="#request.pobChargingPile.buildTime" format="yyyy/MM/dd" />' />
							<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span> </span>
						</div>
					</div>
				</div>

				<div class="form-group">
					<label for="chaWay" class="col-sm-2 control-label">
						<span style="color: red;">*</span>充电方式：
					</label>
					<div class="col-sm-4">
						<s:iterator value="#request.chaWayList" status="statu" id="item">
							<s:if test="value == #request.pobChargingPile.chaWay">
								<label class="checkbox-inline"> <input type="radio" name="chaWay" id="chaWay" value='<s:property value="value"/>' checked> 
									<s:property value="name" />
								</label>
							</s:if>
							<s:else>
								<label class="checkbox-inline"> <input type="radio" name="chaWay" id="chaWay" value='<s:property value="value"/>'>
									<s:property value="name" />
								</label>
							</s:else>
						</s:iterator>
					</div>
					<label for="pileModel" class="col-sm-2 control-label">
						<span style="color: red;">*</span>电桩型号：
					</label>
					<div class="col-sm-4">
					<div class="form-group">
					<div class="col-sm-10">
						<select id="pileModel" name="pileModel" class="form-control">
							<s:iterator value="#request.busPileModelList" status="statu" id="item">
								<s:if test="id == #request.pobChargingPile.pileModel">
									<option selected="selected" value='<s:property value="id"/>'>
										<s:property value="brand" />
									</option>
								</s:if>
								<s:else>
									<option value='<s:property value="id"/>'>
										<s:property value="brand" />
									</option>
								</s:else>
							</s:iterator>
						</select>
						</div>
						<div class="col-sm-2">
							<lable id="detailPileModelBtn" class="glyphicon glyphicon-zoom-in" title="查看详情" style="top:7px;cursor:pointer;font-size: 20px;" />
						</div>
					</div>
				</div>
			</div>
				<div class="form-group">
					<label for="address" class="col-sm-2 control-label">
						<span style="color: red;">*</span>详细地址：
					</label>
					<div class="col-sm-4">
						<textarea type="text" maxlength="150" class="form-control" id="address" rows="3" name="address"><s:property value="#request.pobChargingPile.address"/></textarea>
					</div>
						<!-- <label for="doc" class="col-sm-2 control-label">上传文件：</label>
					<div class="col-sm-4">
						<input type="file" id="editPileDoc" name="doc" />
					</div> -->
				</div>
		<hr class="dashed">
		<h5>通讯信息</h5>
		<div class="form-group">
					<label for="comType" class="col-sm-2 control-label">
						<span style="color: red;">*</span>通讯协议：
					</label>
					<div class="col-sm-4">
						<select id="comType" name="comType" class="form-control">
							<s:iterator value="#request.comTypeList" status="statu" id="item">
								<s:if test="value == #request.pobChargingPile.comType">
									<option selected="selected" value='<s:property value="value"/>'>
										<s:property value="name" />
									</option>
								</s:if>
								<s:else>
									<option value='<s:property value="value"/>'>
										<s:property value="name" />
									</option>
								</s:else>
							</s:iterator>
						</select>
					</div>
						<label for="comAddr" class="col-sm-2 control-label">
							<span style="color: red;">*</span>通讯地址：
						</label>
					<div class="col-sm-4">
						<input type="text" maxlength="30" class="form-control" id="comAddr" name="comAddr" value='<s:property value="#request.pobChargingPile.comAddr"/>'  />
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
				<label for="payWay" class="col-sm-2 control-label">
					<span style="color: red;">*</span>支付方式：
				</label>
					<div class="col-sm-4" id="payWay">
						<s:iterator value="#request.payWayList" status="statu" id="item">
							<label class="checkbox-inline"> <input type="checkbox" value="<s:property value='value'/>" name="payWay"> <s:property value='name' />
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
		<s:if test="#request.actionType == 'editValidStationPile' && #request.activeRule != null">
		<div class="form-group">
			<div class="col-sm-12 text-center">
				<a href="javascript:;" id="showActiveChargeRuleBtn">显示在使用电费规则</a>
			</div>
		</div>
		<div id="activeChargeRule" style="display: none;" >
		<h5>正在使用电费规则</h5>
			<div class="form-group">
				<label for="activeFeeRule" class="col-sm-2 control-label">
					<span style="color: red;">*</span>电费规则：
				</label>
					<div class="col-sm-4">
						<select id="activeFeeRule" disabled="disabled" class="form-control">
							<s:iterator value="#request.chargeRuleList" id="item" status="statu">
								<s:if test="#request.activeRule.id == id">
								<option selected="selected" value="<s:property value='id'/>">
									<s:property value='name' />
								</option>
								</s:if>
							</s:iterator>
						</select>
					</div>
					<s:if test="#request.activeRule.id == 1">
					<label for="activeChargeFee" class="col-sm-2 control-label">
						<span style="color: red;">*</span>单一电费(元)：
					</label>
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
					<label for="activeParkFee" class="col-sm-2 control-label">
						<span style="color: red;">*</span>停车费(元)：
					</label>
					<div class="col-sm-4">
						<input disabled="disabled" maxlength="10" value='<s:property value="#request.activeRule.parkFee"/>' type="text" class="form-control" id="activeParkFee"/>
					</div>
					<label for="activeServiceFee" class="col-sm-2 control-label">
						<span style="color: red;">*</span>服务费(元)：
					</label>
					<div class="col-sm-4">
						<input disabled="disabled" maxlength="10" value='<s:property value="#request.activeRule.serviceFee"/>' type="text" class="form-control" id="activeServiceFee"/>
					</div>
			</div>
			<div class="form-group">
					<label for="activeTimeOk" class="col-sm-2 control-label">
						<span style="color: red;">*</span>启用时间：
					</label>
					<div class="col-sm-4">
						<input disabled="disabled" value='<s:property value="#request.activeRule.activeTimeDesc"/>' type="text" class="form-control" id="activeTimeOk"/>	
					</div>
				</div>
		<hr class="dashed">
	</div>
	</s:if>
	<h5>电费规则</h5>
	<s:if test="#request.actionType == 'editValidStationPile'">
					<div class="form-group">
					<label for="feeRule" class="col-sm-2 control-label">
						<span style="color: red;">*</span>电费规则：
					</label>
					<div class="col-sm-4">
						<select id="feeRule" name="feeRule" class="form-control">
						<option value="0">请选择</option>
							<s:iterator value="#request.chargeRuleList" id="item" status="statu">
								<s:if test="#request.unActiveRule.id == id">
									<option selected="selected" value="<s:property value='id'/>">
										<s:property value='name' />
									</option>
								</s:if>
								<s:else>
									<option value="<s:property value='id'/>">
										<s:property value='name' />
									</option>
								</s:else>
							</s:iterator>
						</select>
					</div>
					<div id="chargeFeeDiv">
						<label for="chargeFee" class="col-sm-2 control-label"><span
							style="color: red;">*</span>单一电费(元)：</label>
						<div class="col-sm-4">
							<input type="text" maxlength="10" class="form-control" id="chargeFee" name="chargeFee" value='<s:property value="#request.unActiveRule.chargeFee"/>' placeholder="保留小数点后2位"/>
						</div>
					</div>
					
					 <div id="feeRuleMsgDiv" class="hide">
						<label for="feeRuleMsg" class="col-sm-2 control-label">电费规则说明：</label>
						<div id="feeRuleMsg" class="col-sm-4">
						</div>
					</div>
				</div>
				<div class="form-group">
					<label for="parkFee" class="col-sm-2 control-label">
						<span style="color: red;">*</span>停车费(元)：
					</label>
					<div class="col-sm-4">
						<input type="text" maxlength="10" class="form-control" id="parkFee" name="parkFee" value='<s:property value="#request.unActiveRule.parkFee"/>' placeholder="保留小数点后2位"/>
					</div>
					<label for="serviceFee" class="col-sm-2 control-label">
						<span style="color: red;">*</span>服务费(元)：
					</label>
					<div class="col-sm-4">
						<input type="text" maxlength="10" class="form-control" id="serviceFee" name="serviceFee" value='<s:property value="#request.unActiveRule.serviceFee"/>' placeholder="保留小数点后2位"/>
					</div>
				</div>
				<div class="form-group">
					<label for="activeTime" class="col-sm-2 control-label">
						<span style="color: red;">*</span>启用时间：
					</label>
					<div class="col-sm-4">
							<div class='input-group date' id='oneTimeDate2'>
						 	<input id="activeTime" name="activeTime" type='text' value='<s:property value="#request.unActiveRule.activeTimeDesc"/>' class="form-control" /> 
						 	<span class="input-group-addon">
							<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</div>
				</div>
	</s:if>
	<s:if test="#request.actionType == 'editNewStationPile'">
				<div class="form-group">
					<label for="feeRule" class="col-sm-2 control-label">
						<span style="color: red;">*</span>电费规则：
					</label>
					<div class="col-sm-4">
						<select id="feeRule" name="feeRule" class="form-control">
						<option value="0">请选择</option>
							<s:iterator value="#request.chargeRuleList" id="item" status="statu">
								<s:if test="#request.pobChargingPile.feeRule == id">
									<option selected="selected" value="<s:property value='id'/>">
										<s:property value='name' />
									</option>
								</s:if>
								<s:else>
									<option value="<s:property value='id'/>">
										<s:property value='name' />
									</option>
								</s:else>
							</s:iterator>
						</select>
					</div>
						<div id="chargeFeeDiv">
					<label for="chargeFee" class="col-sm-2 control-label">
						<span style="color: red;">*</span>单一电费(元)：
					</label>
					<div class="col-sm-4">
						<input type="text" maxlength="10" class="form-control" id="chargeFee" name="chargeFee" value='<s:property value="#request.pobChargingPile.chargeFee"/>' />
					</div>
					</div>
					
					 <div id="feeRuleMsgDiv" class="hide">
						<label for="feeRuleMsg" class="col-sm-2 control-label">电费规则说明：</label>
						<div id="feeRuleMsg" class="col-sm-4">
						</div>
					</div>
				</div>
				<div class="form-group">
					<label for="parkFee" class="col-sm-2 control-label">
						<span style="color: red;">*</span>停车费(元)：
					</label>
					<div class="col-sm-4">
						<input type="text" maxlength="10" class="form-control" id="parkFee" name="parkFee" value='<s:property value="#request.pobChargingPile.parkFee"/>' />
					</div>
					<label for="serviceFee" class="col-sm-2 control-label">
						<span style="color: red;">*</span>服务费(元)：
					</label>
					<div class="col-sm-4">
						<input type="text" maxlength="10" class="form-control" id="serviceFee" name="serviceFee" value='<s:property value="#request.pobChargingPile.serviceFee"/>' />
					</div>
				</div>
				<div class="form-group">
					<label for="activeTime" class="col-sm-2 control-label">
						<span style="color: red;">*</span>启用时间：
					</label>
					<div class="col-sm-4">
							<div class='input-group date' id='oneTimeDate2'>
							<input id="activeTime" name="activeTime" type='text' value='<s:date name="#request.pobChargingPile.activeTime" format="yyyy/MM/dd" />' class="form-control" /> 
							<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span> </span>
						</div>
					</div>
				</div>
			</s:if>
			</form>
			<input type="hidden" id="pileId" value="${pileId}" /> 
			<input type="hidden" id="actionType" value="${actionType}" />
            </div>
          </div>
          <!-- detailPileModel -->
          <div class="box box-info hide" id="showPileModelDiv">
            	<div class="box-header with-border">
              	<h3 class="box-title">设备型号详情</h3>
              			<button id="returnEditPileBtn" type="button" style="margin-left: 10px;" class="btn btn-info pull-right btn-sm">
							返回
						</button>
              </div>
               <div class="box-body">
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
            </div>
          </div>
      </div>
</section>
<script type="text/javascript">
var chargeRuleListJson = ${chargeRuleListJson};
var myPayWay = '${pobChargingPile.payWay}';
var feeRule = '${pobChargingPile.feeRule}';
var currentTime = "${currentTime}";
</script>
<script src="res/js/user/device/editUserPile.js"></script>
