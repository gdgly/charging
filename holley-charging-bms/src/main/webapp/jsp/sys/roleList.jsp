<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>角色管理</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
    <form id="conditionFrom" class="form-inline" role="form" action="sys/role_queryList.action">
	    <div class="form-group">
			<input id="roleName" name="rolename" type="text" class="form-control" placeholder="请输入角色名称"/>
			<select id="roleType" name="roletype" class="form-control">
		     	<option value="0">角色类型</option>
		     	<s:iterator value="#request.roleTypeList" var="item" status="st">
		     		<s:if test="#request.roleType == #item.value">
		     			<option value="${item.value}" selected="selected">${item.text}</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.value}">${item.text}</option>
		     		</s:else>
		     	</s:iterator>
		     </select>
		     <input id="isExport" name="isExport" value="true" type="hidden">
			 <input id="fileName" name="fileName" value="角色列表" type="hidden">
		</div>
		 <div class="form-group pull-right">
			<button type="button" id="queryBtn" class="btn btn-primary">查询</button>
		    <button type="button" id="addBtn" class="btn btn-primary">新增</button>
		    <button type="button" id="exportBtn" class="btn btn-primary">导出</button>
		 </div>
    </form>
    <!-- 表格 -->
    <div id="roleTableDiv">
	    <table class="table table-condensed table-hover" id="roleTable">
	   		<thead>
	   			<tr>
	   				<th>角色编码</th>
	   				<th>角色名称</th>
	   				<th>描述</th>
	   				<th>角色类型</th>
	   				<th>状态</th>
	   				<th>创建人</th>
	   				<th>创建时间</th>
	   				<th>操作</th>
	   			</tr>
	   		</thead>
		   	<tbody></tbody>
		</table>
    </div>
  </div>
  <script src="res/js/sys/roleList.js" type="text/javascript"></script>
</body>
</html>

