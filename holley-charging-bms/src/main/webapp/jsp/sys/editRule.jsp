<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>规则新增/修改</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
  	<form id="ruleForm" class="form-horizontal" role="form">
  		<div class="form-group">
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>规则id</label>
	      <div class="col-sm-4">
	      	<s:if test="#request.requestType == 2">
		      	<input type="text" class="form-control" id="id" name="id" maxlength="30" value="<s:property value='#request.sysRule.id'/>" disabled="disabled"/>
	      	</s:if>
	      	<s:else>
	      		<input type="text" class="form-control" id="id" name="id" maxlength="30" value="<s:property value='#request.sysRule.id'/>" placeholder="请输入规则id"/>
	      	</s:else>
	      </div>
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>规则名称</label>
	      <div class="col-sm-4">
	        	<input type="text" class="form-control" id="name" name="name" maxlength="50" value="<s:property value='#request.sysRule.name'/>" placeholder="请输入规则名称"/>
	      </div>
    	</div>
		<div class="form-group">
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>规则值</label>
	      <div class="col-sm-4">
	        <textarea class="form-control" id="ruleCheck" name="ruleCheck" rows="3" maxlength="512"><s:property value='#request.sysRule.ruleCheck'/></textarea>
	        <%-- <input type="text" class="form-control" id="ruleCheck" name="ruleCheck" maxlength="512" value="<s:property value='#request.sysRule.ruleCheck'/>" placeholder="请输入规则值"/> --%>
	      </div>
	      <label class="col-sm-2 control-label">备注</label>
	      <div class="col-sm-4">
	        <textarea class="form-control" id="remark" name="remark" rows="3"><s:property value='#request.sysRule.remark' /></textarea>
	      </div>
    	</div>
    	<div class="form-group">
    	  <label class="col-sm-2 control-label"><span style="color: red;">*</span>规则状态</label>
	      <div class="col-sm-4">
	        	<select id="status" name="status" class="form-control">
		     	<s:iterator value="#request.statusList" var="item" status="st">
		     		<s:if test="#request.sysRule.status == #item.value">
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
  
  <script type="text/javascript">
  	var requestType = <s:property value='#request.requestType'/>
  </script>
  <script src="res/js/sys/editRule.js" type="text/javascript"></script>
</body>
</html>

