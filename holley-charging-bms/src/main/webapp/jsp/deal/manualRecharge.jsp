<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>人工充值</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
  	<form id="manualRechargeForm" class="form-horizontal" role="form">
  		<div class="form-group">
	      <label class="col-sm-2 control-label">用户编码</label>
	      <div class="col-sm-4">
	      	<p id="userId" class="form-control-static"><s:property value="#request.userInfo.userid"/></p>
	      </div>
	      <label class="col-sm-2 control-label">用户昵称</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.userInfo.username"/>
	      		<s:if test="#request.userInfo.realname != null">
	      		【<s:property value="#request.userInfo.realname"/>】
	      		</s:if>
	      	</p>
	      </div>
    	</div>
		<div class="form-group">
	      <label class="col-sm-2 control-label">手机号码</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.userInfo.phone"/></p>
	      	<!-- <input type="text" class="form-control" id="mobile" name="mobile" maxlength="20" placeholder="请输入手机号码" disabled="disabled"/> -->
	      </div>
	      <label class="col-sm-2 control-label">用户类型</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.userInfo.usertypeDesc"/></p>
	      </div>
    	</div>
    	<div class="form-group">
			<label class="col-sm-2 control-label"><span style="color: red;">*</span>充值金额(元)</label>
	        <div class="col-sm-4">
	      		<input type="text" class="form-control" id="money" name="money" placeholder="最少金额0.01元"/>
	        </div>
	        <label class="col-sm-2 control-label"><span style="color: red;">*</span>支付方式</label>
	        <div class="col-sm-4">
		      	<select id="payWay" name="payWay" class="form-control" disabled="disabled">
			     	<s:iterator value="#request.payWayList" var="item" status="st">
			     		<s:if test="#request.payWay == #item.value">
			     			<option value="${item.value}" selected="selected">${item.text}</option>
			     		</s:if>
			     		<s:else>
				     		<option value="${item.value}">${item.text}</option>
			     		</s:else>
			     	</s:iterator>
			     </select>
	         </div>
    	</div>
    	<div class="form-group">
	      <div class="col-sm-offset-2 col-sm-10">
	         <button id="saveBtn" type="button" class="btn btn-primary">保存</button>
	         <button id="cancelBtn" type="button" class="btn btn-primary" onclick="gobackAndReload()">取消</button>
	      </div>
   		</div>
    </form>
  </div>
  <script src="res/js/deal/manualRecharge.js" type="text/javascript"></script>
</body>
</html>

