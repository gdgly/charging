<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>角色新增/修改</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
  	<form id="roleForm" class="form-horizontal" role="form">
		<div class="form-group">
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>角色名称</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="rolename" name="rolename" maxlength="20" value="<s:property value='#request.sysRole.rolename'/>"/>
	      </div>
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>角色类型</label>
	      <div class="col-sm-4">
	      	<select id="type" name="type" class="form-control">
		     	<s:iterator value="#request.roleTypeList" var="item" status="st">
		     		<s:if test="#request.sysRole.type == #item.value">
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
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>状态</label>
	      <div class="col-sm-4">
	      	<select id="status" name="status" class="form-control">
		     	<s:iterator value="#request.roleStatusList" var="item" status="st">
		     		<s:if test="#request.sysRole.status == #item.value">
		     			<option value="${item.value}" selected="selected">${item.text}</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.value}">${item.text}</option>
		     		</s:else>
		     	</s:iterator>
		     </select>
	      </div>
	      <label class="col-sm-2 control-label">描述</label>
	      <div class="col-sm-4">
	      	<textarea class="form-control" rows="3" id="remark" name="remark" maxlength="128"><s:property value='#request.sysRole.remark'/></textarea>
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
  	var roleid = "<s:property value='#request.sysRole.id'/>";
  </script>
  <script src="res/js/sys/editRole.js" type="text/javascript"></script>
</body>
</html>

