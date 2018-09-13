<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>预约详细信息</title>
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
	      		<p class="form-control-static"><s:property value="#request.appointment.userId"/></p>
	        </div>
    	</div>
		<div class="form-group">
	      <label class="col-sm-2 control-label">用户昵称</label>
	      <div class="col-sm-4">
	        <p class="form-control-static"><s:property value="#request.appointment.username"/></p>
	      </div>
	      <label class="col-sm-2 control-label">手机号码</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.appointment.phone"/></p>
	      </div>
    	</div>
  		<h5 class="form-header">预约信息</h5>
  		<hr class="dashed">
  		<div class="form-group">
  			<label class="col-sm-2 control-label">预约ID</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.appointment.id"/></p>
	        </div>
		    <label class="col-sm-2 control-label">交易号</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.appointment.appNo"/></p>
	        </div>
    	</div>
    	<div class="form-group">
			<label class="col-sm-2 control-label">预约开始时间</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.appointment.startTimeDesc"/></p>
	        </div>
	        <label class="col-sm-2 control-label">预约结束时间</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.appointment.endTimeDesc"/></p>
		    </div>
    	</div>
    	<div class="form-group">
		    <label class="col-sm-2 control-label">预约时长(分钟)</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.appointment.appLen"/></p>
	        </div>
	        <label class="col-sm-2 control-label">预约费用(元)</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.appointment.appFeeDesc"/></p>
		    </div>
    	</div>
    	<div class="form-group">
		    <label class="col-sm-2 control-label">预约状态</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.appointment.appStatusDesc"/></p>
	        </div>
	        <label class="col-sm-2 control-label">支付状态</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.appointment.payStatusDesc"/></p>
		    </div>
    	</div>
    	<div class="form-group">
		    <label class="col-sm-2 control-label">支付方式</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.appointment.payWayDesc"/></p>
	        </div>
	        <label class="col-sm-2 control-label">支付信息</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.appointment.accountInfo"/></p>
		    </div>
    	</div>
    	<div class="form-group">
		    <label class="col-sm-2 control-label">结算状态</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.appointment.isBillDesc"/></p>
	        </div>
	        <label class="col-sm-2 control-label">更新时间</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.appointment.addTimeDesc"/></p>
		    </div>
    	</div>
    	<h5 class="form-header">电桩信息</h5>
  		<hr class="dashed">
  		<div class="form-group">
	      <label class="col-sm-2 control-label">充电点</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.appointment.stationName"/></p>
	      </div>
	      <label class="col-sm-2 control-label">地址</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.appointment.address"/></p>
	      </div>
    	</div>
		<div class="form-group">
	      <label class="col-sm-2 control-label">充电桩</label>
	      <div class="col-sm-4">
	        <p class="form-control-static"><s:property value="#request.appointment.pileName"/></p>
	      </div>
	      <label class="col-sm-2 control-label">充电桩ID</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.appointment.pileId"/></p>
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

