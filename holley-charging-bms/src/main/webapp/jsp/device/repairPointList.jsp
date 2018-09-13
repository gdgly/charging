<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>服务点</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
    <form id="conditionFrom" class="form-inline" role="form" action="device/repairPoint_queryList.action">
	    <div class="form-group">
			<input id="keyword" name="keyword" type="text" class="form-control" placeholder="请输入服务点名称/地址" style="width: 190px;"/>
			<select id="isShow" name="isshow" class="form-control">
		     	<option value="0">状态</option>
		     	<s:iterator value="#request.showStatusList" var="item" status="st">
		     		<s:if test="#request.isShow == #item.value">
		     			<option value="${item.value}" selected="selected">${item.text}</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.value}">${item.text}</option>
		     		</s:else>
		     	</s:iterator>
		     </select>
		     <input id="isExport" name="isExport" value="true" type="hidden">
			 <input id="fileName" name="fileName" value="服务点列表" type="hidden">
		</div>
		 <div class="form-group pull-right">
			<button type="button" id="queryBtn" class="btn btn-primary">查询</button>
		    <button type="button" id="addBtn" class="btn btn-primary">新增</button>
		    <button type="button" id="exportBtn" class="btn btn-primary">导出</button>
		 </div>
    </form>
    <!-- 表格 -->
    <div>
	    <table class="table table-condensed table-hover" id="repairPointTable">
	   		<thead>
	   			<tr>
	   				<th>服务点ID</th>
	   				<th>名称</th>
	   				<th>地址</th>
	   				<th>状态</th>
	   				<th>经度</th>
	   				<th>纬度</th>
	   				<th>座机号码</th>
	   				<th>手机号码</th>
	   				<th>营业时间</th>
	   				<th>更新时间</th>
	   				<th>操作</th>
	   			</tr>
	   		</thead>
		   	<tbody></tbody>
		</table>
    </div>
     <!-- 分页条 -->
    <%@include file="../common/global/pagingtoolbar.jsp" %>
  </div>
  <script src="res/js/device/repairPointList.js" type="text/javascript"></script>
</body>
</html>

