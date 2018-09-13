<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>充值详细信息</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
  	<form id="rechargeDetailForm" class="form-horizontal" role="form">
  		<h5 class="form-header">用户信息</h5>
  		<hr class="dashed">
  		<div class="form-group">
	      <label class="col-sm-2 control-label">用户编码</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.recharge.userId"/></p>
	        </div>
    	</div>
		<div class="form-group">
	      <label class="col-sm-2 control-label">用户昵称</label>
	      <div class="col-sm-4">
	        <p class="form-control-static"><s:property value="#request.recharge.username"/></p>
	      </div>
	      <label class="col-sm-2 control-label">手机号码</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.recharge.phone"/></p>
	      </div>
    	</div>
  		<h5 class="form-header">充值信息</h5>
  		<hr class="dashed">
  		<div class="form-group">
  			<label class="col-sm-2 control-label">充值ID</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.recharge.id"/></p>
	        </div>
		    <label class="col-sm-2 control-label">交易号</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.recharge.tradeNo"/></p>
	        </div>
    	</div>
    	<div class="form-group">
			<label class="col-sm-2 control-label">订单状态</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.recharge.statusDesc"/></p>
	        </div>
	        <label class="col-sm-2 control-label">充值金额</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.recharge.moneyDesc"/></p>
		    </div>
    	</div>
    	<div class="form-group">
		    <label class="col-sm-2 control-label">支付方式</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.recharge.payWayDesc"/></p>
	        </div>
	        <label class="col-sm-2 control-label">支付信息</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.recharge.accountInfo"/></p>
		    </div>
    	</div>
    	<div class="form-group">
		    <label class="col-sm-2 control-label">手续费</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.recharge.fee"/></p>
	        </div>
	        <label class="col-sm-2 control-label">更新时间</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.recharge.addTimeDesc"/></p>
		    </div>
    	</div>
    	<div class="form-group">
	      <div class="col-sm-offset-2 col-sm-10" >
	         <button type="button" class="btn btn-primary" onclick="gobackAndReload()">返回</button>
	      </div>
	  	</div>
    </form>
  </div>
</body>
</html>

