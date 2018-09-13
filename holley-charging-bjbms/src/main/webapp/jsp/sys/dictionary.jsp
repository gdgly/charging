<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>数据字典</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
    <form id="conditionFrom" class="form-inline" role="form" action="sys/dic_queryLinkList.action">
	    <div class="form-group">
			<select id="linkType" name="linktype" class="form-control">
		     	<option value="0">数据类型</option>
		     	<s:iterator value="#request.linkTypeList" var="item" status="st">
		     		<s:if test="#request.linkType == #item.id">
		     			<option value="${item.id}" selected="selected">${item.name}</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.id}">${item.name}</option>
		     		</s:else>
		     	</s:iterator>
		     </select>
		     <input id="isExport" name="isExport" value="true" type="hidden">
			 <input id="fileName" name="fileName" value="数据字典" type="hidden">
		</div>
		 <div class="form-group pull-right">
			<button type="button" id="queryBtn" class="btn btn-primary">查询</button>
		    <button type="button" id="addBtn" class="btn btn-primary">新增</button>
		    <button type="button" id="exportBtn" class="btn btn-primary">导出</button>
		 </div>
    </form>
    <!-- 数据表格 -->
    <div id="linkTableDiv">
	    <table class="table table-condensed table-hover" id="linkTable">
	   		<thead>
	   			<tr>
	   				<th>ID</th>
	   				<th>名称</th>
	   				<th>值</th>
	   				<th>类型</th>
	   				<!-- <th>操作</th> -->
	   			</tr>
	   		</thead>
		   	<tbody></tbody>
		</table>
    </div>
     <!-- 分页条 -->
    <%@include file="../common/global/pagingtoolbar.jsp" %>
  </div>
  <script src="res/js/sys/dictionary.js" type="text/javascript"></script>
</body>
</html>

