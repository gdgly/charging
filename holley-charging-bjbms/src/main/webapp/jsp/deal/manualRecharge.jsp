<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>人工充值 | 提现</title>
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
			<label class="col-sm-2 control-label">账户余额（元）</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.account.usableMoney"/></p>
		      </div>
		    <s:if test="#request.rechargeType == 1">
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
	         </s:if>
	          <s:if test="#request.rechargeType == 2">
	           <label class="col-sm-2 control-label"><span style="color: red;">*</span>提现方式</label>
	           <div class="col-sm-4">
	          	<select id="cashWay" name="cashWay" class="form-control">
			     	<option value="0">提现方式</option>
			     	<s:iterator value="#request.cashWayList" var="item" status="st">
				     		<option value="${item.value}">${item.text}</option>
			     	</s:iterator>
			     </select>
			     </div>
	          </s:if>
    	</div>
    	<div class="form-group">
    	<label class="col-sm-2 control-label"><span style="color: red;">*</span>
    		<s:if test="#request.rechargeType == 1">
    			充值金额(元)
    		</s:if>
    		<s:elseif test="#request.rechargeType == 2">
    			提现金额(元)
    		</s:elseif>
    	</label>
	        <div class="col-sm-4">
	      		<input type="text" class="form-control" id="money" name="money" placeholder="最少金额0.01元"/>
	        </div>
	        <s:if test="#request.rechargeType == 2">
	        	<label class="col-sm-2 control-label"><span style="color: red;">*</span>
	    		提现账户信息
	    		</label>
	    		<div class="col-sm-4">
	    			<input type="text" class="form-control" id="accountInfo" maxlength="30" name="accountInfo" placeholder="账户信息"/>
	    		</div>
	    	</s:if>
    	</div>
    	    	<s:if test="#request.rechargeType == 2">
	    	<div class="form-group">
	    		<label class="col-sm-2 control-label">
	    		备注信息
	    		</label>
	    		<div class="col-sm-4">
	    			<textarea type="text" rows="3" class="form-control" id="validRemark" maxlength="150" name="validRemark" placeholder="备注信息"></textarea>
	    		</div>
	    	</div>
    	</s:if>
    	<div class="form-group">
	      <div class="col-sm-offset-2 col-sm-10">
	         <button id="saveBtn" type="button" class="btn btn-primary">保存</button>
	         <button id="cancelBtn" type="button" class="btn btn-primary" onclick="href('deal/accountList.action')">取消</button>
	      </div>
   		</div>
    </form>
  </div>
  <script type="text/javascript">
  var rechargeType = ${rechargeType};
  </script>
  <script src="res/js/deal/manualRecharge.js" type="text/javascript"></script>
</body>
</html>

