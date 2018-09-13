<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
 <title>运营商审核详细信息</title>
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
		<div class="form-group">
	      <label class="col-sm-2 control-label">真实姓名</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.busCash.realName"/></p>
	      </div>
	      <label class="col-sm-2 control-label">手机号码</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.busCash.phone"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">提现方式</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.busCash.cashWayDesc"/></p>
	      </div>
	      <label class="col-sm-2 control-label">提现账户</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.busCash.account"/></p>
	      </div>
    	</div>
    	<s:if test="#request.busCash.cashWay==2">
	    	<div class="form-group">
		      <label class="col-sm-2 control-label">开户姓名</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.busCash.accRealName"/></p>
		      </div>
		      <label class="col-sm-2 control-label">开户银行</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.busCash.bankName"/></p>
		      </div>
	    	</div>
    	</s:if>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">提现金额</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.busCash.money"/></p>
	      </div>
		    <label class="col-sm-2 control-label">提现状态</label>
		    <div class="col-sm-4">
		      <p class="form-control-static"><s:property value="#request.busCash.cashStatusDesc"/></p>
		    </div>
		</div>
		<div class="form-group">
	      <label class="col-sm-2 control-label">备注</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.busCash.remark"/></p>
	      </div>
		    <label class="col-sm-2 control-label">申请时间</label>
		    <div class="col-sm-4">
		      <p class="form-control-static"><s:property value="#request.busCash.addTimeDesc"/></p>
		    </div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">审核状态</label>
	      	<div class="col-sm-4">
			    <p class="form-control-static"><s:property value="#request.busCash.validStatusDesc"/></p>
		    </div>
	        <label class="col-sm-2 control-label">审核时间</label>
	      	<div class="col-sm-4">
			    <p class="form-control-static"><s:property value="#request.busCash.validTimeDesc"/></p>
		    </div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">审核备注</label>
	      	<div class="col-sm-4">
			    <p class="form-control-static"><s:property value="#request.busCash.validRemark"/></p>
		    </div>
		</div>
    	<div class="form-group">
	      <div class="col-sm-offset-2 col-sm-10">
	          <button id="backBtn" type="button" class="btn btn-primary" onclick="javascript:history.back(-1);">返回</button>
	      </div>
   		</div>
    </form>
</div>
</body>
</html>

