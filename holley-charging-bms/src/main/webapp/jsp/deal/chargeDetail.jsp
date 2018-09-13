<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>充电详细信息</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
  	<form id="userInfoForm" class="form-horizontal" role="form">
  		<h5 class="form-header">用户信息</h5>
  		<hr class="dashed">
  		<div class="form-group">
	      <label class="col-sm-2 control-label">用户编码</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.payment.userId"/></p>
	        </div>
    	</div>
		<div class="form-group">
	      <label class="col-sm-2 control-label">用户昵称</label>
	      <div class="col-sm-4">
	        <p class="form-control-static"><s:property value="#request.payment.username"/></p>
	      </div>
	      <label class="col-sm-2 control-label">手机号码</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.payment.phone"/></p>
	      </div>
    	</div>
  		<h5 class="form-header">充电信息</h5>
  		<hr class="dashed">
  		<div class="form-group">
  			<label class="col-sm-2 control-label">交易ID</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.payment.id"/></p>
	        </div>
		    <label class="col-sm-2 control-label">交易号</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.payment.tradeNo"/></p>
	        </div>
    	</div>
    	<div class="form-group">
			<label class="col-sm-2 control-label">充电开始时间</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.payment.startTimeDesc"/></p>
	        </div>
	        <label class="col-sm-2 control-label">充电结束时间</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.payment.endTimeDesc"/></p>
		    </div>
    	</div>
    	<div class="form-group">
		    <label class="col-sm-2 control-label">充电时长(分钟)</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.payment.chaLen"/></p>
	        </div>
	        <label class="col-sm-2 control-label">充电电量(度)</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.payment.chaPower"/></p>
		    </div>
    	</div>
    	<div class="form-group">
		    <label class="col-sm-2 control-label">充电费用(元)</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.payment.chaFee"/></p>
	        </div>
	        <label class="col-sm-2 control-label">停车费用(元)</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.payment.parkFee"/></p>
		    </div>
    	</div>
    	<div class="form-group">
		    <label class="col-sm-2 control-label">服务费用(元)</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.payment.serviceFee"/></p>
	        </div>
    	</div>
    	<div class="form-group">
		    <label class="col-sm-2 control-label">应缴费用(元)</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.payment.shouldMoney"/></p>
	        </div>
	        <label class="col-sm-2 control-label">实缴费用(元)</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.payment.actualMoney"/></p>
		    </div>
    	</div>
    	<div class="form-group">
		    <label class="col-sm-2 control-label">交易状态</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.payment.dealStatusDesc"/></p>
	        </div>
	        <label class="col-sm-2 control-label">支付状态</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.payment.payStatusDesc"/></p>
		    </div>
    	</div>
    	<div class="form-group">
		    <label class="col-sm-2 control-label">支付方式</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.payment.payWayDesc"/></p>
	        </div>
	        <label class="col-sm-2 control-label">支付信息</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.payment.accountInfo"/></p>
		    </div>
    	</div>
    	<div class="form-group">
		    <label class="col-sm-2 control-label">结算状态</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.payment.isBillDesc"/></p>
	        </div>
	        <label class="col-sm-2 control-label">更新时间</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.payment.updateTimeDesc"/></p>
		    </div>
    	</div>
    	<h5 class="form-header">电桩信息</h5>
  		<hr class="dashed">
  		<div class="form-group">
	      <label class="col-sm-2 control-label">充电点</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.payment.stationName"/></p>
	      </div>
	      <label class="col-sm-2 control-label">地址</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.payment.address"/></p>
	      </div>
    	</div>
		<div class="form-group">
	      <label class="col-sm-2 control-label">充电桩</label>
	      <div class="col-sm-4">
	        <p class="form-control-static"><s:property value="#request.payment.pileName"/></p>
	      </div>
	      <label class="col-sm-2 control-label">充电桩ID</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.payment.pileId"/></p>
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

