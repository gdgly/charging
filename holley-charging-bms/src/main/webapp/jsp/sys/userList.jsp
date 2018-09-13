<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>用户管理</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
    <form id="conditionFrom" class="form-inline" role="form" action="sys/user_queryList.action">
	    <div class="form-group">
			<input id="keyword" name="keyword" type="text" class="form-control" placeholder="请输入用户昵称/手机号码" style="width: 190px;"/>
			<select id="userType" name="usertype" class="form-control">
		     	<option value="0">用户类型</option>
		     	<s:iterator value="#request.userTypeList" var="item" status="st">
		     		<s:if test="#request.userType == #item.value">
		     			<option value="${item.value}" selected="selected">${item.text}</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.value}">${item.text}</option>
		     		</s:else>
		     	</s:iterator>
		     </select>
		     <select id="realStatus" name="realstatus" class="form-control">
		     	<option value="0">实名状态</option>
		     	<s:iterator value="#request.realStatusList" var="item" status="st">
		     		<s:if test="#request.realStatus == #item.value">
		     			<option value="${item.value}" selected="selected">${item.text}</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.value}">${item.text}</option>
		     		</s:else>
		     	</s:iterator>
		     </select>
		     <select id="lockStatus" name="lockstatus" class="form-control">
		     	<option value="0">是否锁定</option>
		     	<s:iterator value="#request.lockStatusList" var="item" status="st">
		     		<s:if test="#request.lockStatus == #item.value">
		     			<option value="${item.value}" selected="selected">${item.text}</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.value}">${item.text}</option>
		     		</s:else>
		     	</s:iterator>
		     </select>
		     <input id="isExport" name="isExport" value="true" type="hidden">
			 <input id="fileName" name="fileName" value="用户列表" type="hidden">
		</div>
		 <div class="form-group pull-right">
			<button type="button" id="queryBtn" class="btn btn-primary">查询</button>
		    <button type="button" id="addBtn" class="btn btn-primary">新增</button>
		    <button type="button" id="exportBtn" class="btn btn-primary">导出</button>
		 </div>
    </form>
    <!-- 表格 -->
    <div id="userTableDiv">
	    <table class="table table-condensed table-hover" id="userTable">
	   		<thead>
	   			<tr>
	   				<th>用户编码</th>
	   				<th>用户昵称</th>
	   				<th>手机号码</th>
	   				<th>实名状态</th>
	   				<th>用户类型</th>
	   				<th>用户角色</th>
	   				<th>注册时间</th>
	   				<th>操作</th>
	   			</tr>
	   		</thead>
		   	<tbody></tbody>
		</table>
    </div>
     <!-- 分页条 -->
    <%@include file="../common/global/pagingtoolbar.jsp" %>
  </div>
  <script src="res/js/sys/userList.js" type="text/javascript"></script>
  <script type="text/javascript">
  var webuserid = ${webUser.userId};
  </script>
</body>
</html>

