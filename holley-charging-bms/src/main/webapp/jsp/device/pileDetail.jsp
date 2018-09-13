<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>充电桩详细信息</title>
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
	      	<p class="form-control-static"><s:property value="#request.chargePile.pileName"/></p>
	      </div>
	      <label class="col-sm-2 control-label">充电桩类型</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargePile.pileTypeDesc"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">充电桩编号</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargePile.pileCode"/></p>
	      </div>
	      <label class="col-sm-2 control-label">电桩型号</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargePile.pileModelDesc"/></p>
	      </div>
	        
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">充电方式</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargePile.chaWayDesc"/></p>
	      </div>
	      <label class="col-sm-2 control-label">充电模式</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargePile.chaModelDesc"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">支付方式</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargePile.payWayDesc"/></p>
	      </div>
	      <label class="col-sm-2 control-label">所属充电点</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargePile.stationName"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">是否支持远程控制</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargePile.isControlDesc"/></p>
	      </div>
	      <label class="col-sm-2 control-label">是否支持负荷调度</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargePile.isChaLoadDesc"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">是否支持预约</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargePile.isAppDesc"/></p>
	      </div>
	      <label class="col-sm-2 control-label">状态</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargePile.statusDesc"/></p>
	      </div>
    	</div>
    	<%-- <div class="form-group">
	     <label class="col-sm-2 control-label">软件版本</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargePile.softVersion"/></p>
	      </div>
	      <label class="col-sm-2 control-label">硬件版本</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargePile.hardVersion"/></p>
	      </div>
		</div> --%>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">更新时间</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargePile.updateTimeStr"/></p>
	      </div>
	      <label class="col-sm-2 control-label">安装时间</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargePile.buildTimeStr"/></p>
	      </div>
    	</div>
    	<div class="form-group">
    	  <label class="col-sm-2 control-label">详细地址</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargePile.address"/></p>
	      </div>
    	</div>
    	<!-- ------------------------------通讯信息------------------------------------------------>
    	<h5 class="form-header">通讯信息</h5>
  		<hr class="dashed">
    	<div class="form-group">
	      <label class="col-sm-2 control-label">通讯协议</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargePile.comTypeDesc"/></p>
	      </div>
	     <label class="col-sm-2 control-label">通讯地址</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargePile.comAddr"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">通讯子地址</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargePile.comSubAddr"/></p>
	      </div>
    	</div>
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
    	<div class="form-group">
	      <div class="col-sm-offset-2 col-sm-10">
	         <button id="backBtn" type="button" class="btn btn-primary" onclick="javascript:history.back(-1);">返回</button>
	      </div>
   		</div>
    </form>
</div>
  
</body>
</html>

