<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>运营商审核信息</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
    <form class="form-horizontal" role="form">
    <!-- ------------------------------基本信息------------------------------------------------>
    	<h5 class="form-header">基本信息</h5>
  		<hr class="dashed">
    	<div class="form-group">
	      <label class="col-sm-2 control-label">充电桩名称</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempPile.pileName"/></p>
	      </div>
	      <label class="col-sm-2 control-label">充电桩类型</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempPile.pileTypeDesc"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">充电桩编号</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempPile.pileCode"/></p>
	      </div>
	      <label class="col-sm-2 control-label">电桩型号</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempPile.pileModelDesc"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">充电方式</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempPile.chaWayDesc"/></p>
	      </div>
	      <label class="col-sm-2 control-label">充电模式</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempPile.chaModelDesc"/></p>
	      </div>
    	</div>
    	<div class="form-group">
    		<label class="col-sm-2 control-label">支付方式</label>
		    <div class="col-sm-4">
		    	<p class="form-control-static"><s:property value="#request.tempPile.payWayDesc"/></p>
		    </div>
		    <label class="col-sm-2 control-label">所属充电站</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.tempPile.realStationName"/></p>
	      	</div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">是否支持远程控制</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempPile.isControlDesc"/></p>
	      </div>
	      <label class="col-sm-2 control-label">是否支持负荷调度</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempPile.isChaLoadDesc"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">是否支持预约</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempPile.isAppDesc"/></p>
	      </div>
	      <label class="col-sm-2 control-label">状态</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempPile.statusDesc"/></p>
	      </div>
    	</div>
    	<div class="form-group">
		  <label class="col-sm-2 control-label">更新时间</label>
		   <div class="col-sm-4">
		      <p class="form-control-static"><s:property value="#request.tempPile.updateTimeStr"/></p>
		   </div>
		   <label class="col-sm-2 control-label">安装时间</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempPile.buildTimeStr"/></p>
	      </div>
    	</div>
    	<div class="form-group">
    		<label class="col-sm-2 control-label">详细地址</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.tempPile.address"/></p>
	      	</div>
    	</div>
    	<!-- ------------------------------通讯信息------------------------------------------------>
    	<h5 class="form-header">通讯信息</h5>
  		<hr class="dashed">
  		<div class="form-group">
  			<label class="col-sm-2 control-label">通讯协议</label>
	      	<div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.tempPile.comTypeDesc"/></p>
	      	</div>
	        <label class="col-sm-2 control-label">通讯地址</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.tempPile.comAddr"/></p>
	        </div>
  		</div>
  		<div class="form-group">
	      <label class="col-sm-2 control-label">通讯子地址</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempPile.comSubAddr"/></p>
	      </div>
    	</div>
    	<!-- ------------------------------待审核的费用信息------------------------------------------------>
    	<h5 class="form-header">待审核的费用信息</h5>
  		<hr class="dashed">
  		<s:if test="#request.tempPile.feeRule != null && #request.tempPile.feeRule > 0">
	    	<div class="form-group">
		      <label class="col-sm-2 control-label">收费规则</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.tempPile.feeRuleDesc"/></p>
		      </div>
		      <s:if test="#request.tempPile.feeRule == 1">
		      	 <label class="col-sm-2 control-label">单一电费金额</label>
			      <div class="col-sm-4">
			      	<p class="form-control-static"><s:property value="#request.tempPile.chargeFee"/>&nbsp;元/度</p>
			      </div>
		      </s:if>
		      <s:else>
			      <label class="col-sm-2 control-label">收费规则详细</label>
			      <div class="col-sm-4">
			      	<p class="form-control-static"><s:property value="#request.tempPile.feeRuleDetail"/></p>
			      </div>
		      </s:else>
	    	</div>
	    	<div class="form-group">
		      <label class="col-sm-2 control-label">停车费用</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.tempPile.parkFee"/>&nbsp;元/时</p>
		      </div>
		      <label class="col-sm-2 control-label">服务费用</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.tempPile.serviceFee"/>&nbsp;元/度</p>
		      </div>
	    	</div>
	    	<div class="form-group">
		      <label class="col-sm-2 control-label">费用有效时间</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.tempPile.activeTimeStr"/></p>
		      </div>
	    	</div>
  		</s:if>
  		<s:else>
  			<div class="col-sm-12 text-center">
		      	<p class="form-control-static">暂无信息</p>
		      </div>
  		</s:else>
    	<!-- ------------------------------正在使用的费用信息------------------------------------------------>
  		<s:if test="#request.activeRule != null">
	    	<h5 class="form-header">正在使用的费用信息</h5>
	  		<hr class="dashed">
	    	<div class="form-group">
		      <label class="col-sm-2 control-label">收费规则</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.activeRule.feeRuleDesc"/></p>
		      </div>
		      <s:if test="#request.activeRule.chargeruleId == 1">
		      	 <label class="col-sm-2 control-label">单一电费金额</label>
			      <div class="col-sm-4">
			      	<p class="form-control-static"><s:property value="#request.activeRule.chargeFee"/>&nbsp;元/度</p>
			      </div>
		      </s:if>
		      <s:else>
			      <label class="col-sm-2 control-label">收费规则详细</label>
			      <div class="col-sm-4">
			      	<p class="form-control-static"><s:property value="#request.activeRule.feeRuleDetail"/></p>
			      </div>
		      </s:else>
	    	</div>
	    	<div class="form-group">
		      <label class="col-sm-2 control-label">停车费用</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.activeRule.parkFee"/>&nbsp;元/时</p>
		      </div>
		      <label class="col-sm-2 control-label">服务费用</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.activeRule.serviceFee"/>&nbsp;元/度</p>
		      </div>
	    	</div>
	    	<div class="form-group">
		      <label class="col-sm-2 control-label">费用有效时间</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.activeRule.activeTimeStr"/></p>
		      </div>
	    	</div>
  		</s:if>
    	<!-- ------------------------------待激活的电费信息------------------------------------------------>
    	<s:if test="#request.unactiveRule != null">
	    	<h5 class="form-header">待激活的电费信息</h5>
	  		<hr class="dashed">
	    	<div class="form-group">
		      <label class="col-sm-2 control-label">收费规则</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.unactiveRule.feeRuleDesc"/></p>
		      </div>
		      <s:if test="#request.unactiveRule.chargeruleId == 1">
		      	 <label class="col-sm-2 control-label">单一电费金额</label>
			      <div class="col-sm-4">
			      	<p class="form-control-static"><s:property value="#request.unactiveRule.chargeFee"/>&nbsp;元/度</p>
			      </div>
		      </s:if>
		      <s:else>
			      <label class="col-sm-2 control-label">收费规则详细</label>
			      <div class="col-sm-4">
			      	<p class="form-control-static"><s:property value="#request.unactiveRule.feeRuleDetail"/></p>
			      </div>
		      </s:else>
	    	</div>
	    	<div class="form-group">
		      <label class="col-sm-2 control-label">停车费用</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.unactiveRule.parkFee"/>&nbsp;元/时</p>
		      </div>
		      <label class="col-sm-2 control-label">服务费用</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.unactiveRule.serviceFee"/>&nbsp;元/度</p>
		      </div>
	    	</div>
	    	<div class="form-group">
		      <label class="col-sm-2 control-label">费用有效时间</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.unactiveRule.activeTimeStr"/></p>
		      </div>
	    	</div>
    	</s:if>
    	<h5 class="form-header">审核结果</h5>
  		<hr class="dashed">
  		<s:if test="#request.tempPile.validStatus == 1 || #request.tempPile.validStatus == 2">
  			<div class="form-group">
	    		<label class="col-sm-2 control-label"><span style="color: red;">*</span>审核结果</label>
			    <div class="col-sm-4">
					<select id="validStatus" name="validStatus" class="form-control" onchange="onStatusChange()">
				     	<s:iterator value="#request.verifyStatusList" var="item" status="st">
				     		<s:if test="#request.status == #item.value">
				     			<option value="${item.value}" selected="selected">${item.text}</option>
				     		</s:if>
				     		<s:else>
					     		<option value="${item.value}">${item.text}</option>
				     		</s:else>
				     	</s:iterator>
			     	</select>
			    </div>
			    <label class="col-sm-2 control-label">通知方式</label>
			    <div class="col-sm-4" id="noticeTypeDiv">
			      	<s:iterator value="#request.noticeTypeList" status="statu" var="item">
						<label class="checkbox-inline"> 
							<input type="checkbox" value="<s:property value='value'/>" name="noticeType" checked="checked"><s:property value='text' />
						</label>
					</s:iterator>
			     </div>
			</div>
			<div id="validRemarkDiv" class="form-group hide">
				<label class="col-sm-2 control-label"><span style="color: red;">*</span>审核失败原因</label>
			    <div class="col-sm-4">
				    <textarea id="validRemark" class="form-control" rows="3"></textarea>
			    </div>
			</div>
  		</s:if>
  		<s:else>
  			<div class="form-group">
			    <label class="col-sm-2 control-label">审核状态</label>
		      	<div class="col-sm-4">
		      		<p class="form-control-static"><s:property value="#request.tempPile.validStatusDesc"/></p>
		      	</div>
		      	<label class="col-sm-2 control-label">审核时间</label>
		      	<div class="col-sm-4">
		      		<p class="form-control-static"><s:property value="#request.tempPile.validTimeStr"/></p>
		      	</div>
			</div>
			<div class="form-group">
		      <label class="col-sm-2 control-label">审核备注</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.tempPile.validRemark"/></p>
		      </div>
	    	</div>
  		</s:else>
    	
    	<div class="form-group">
	      <div class="col-sm-offset-2 col-sm-10">
	      	<s:if test="#request.tempPile.validStatus == 1 || #request.tempPile.validStatus == 2">
	         <button id="saveBtn" type="button" class="btn btn-primary">保存</button>
	      	</s:if>
	         <button id="cancelBtn" type="button" class="btn btn-primary" onclick="gobackAndReload()">取消</button>
	      </div>
   		</div>
    </form>
</div>
  <script type="text/javascript">
  	var id = <s:property value="#request.tempPile.id"/>;
  </script>
  <script src="res/js/device/pileVerify.js" type="text/javascript"></script>
</body>
</html>

