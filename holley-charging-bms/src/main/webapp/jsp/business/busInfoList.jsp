<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>运营商信息查询</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
    <form id="conditionFrom" class="form-inline" role="form" action="business/busInfo_queryList.action">
	    <div class="form-group">
			<input id="keyword" name="keyword" type="text" class="form-control" placeholder="请输入企业名称" style="width: 190px;"/>
			<select id="province" name="province" class="form-control">
				<option value="0">请选择省份</option>
				<s:iterator value="#request.provinceList" status="status" id="item">
					<option value="<s:property value='id'/>"><s:property value="name" /></option>
				</s:iterator>
			</select>
			<select id="city" name="city" class="form-control">
				<option value="0">请选择市区</option>
			</select>
		    <input id="isExport" name="isExport" value="true" type="hidden">
			<input id="fileName" name="fileName" value="运营商信息列表" type="hidden">
		</div>
		 <div class="form-group pull-right">
			<button type="button" id="queryBtn" class="btn btn-primary">查询</button>
		    <button type="button" id="exportBtn" class="btn btn-primary">导出</button>
		 </div>
    </form>
    <!-- 表格 -->
    <div id="busInfoTableDiv">
	    <table class="table table-condensed table-hover" id="busInfoTable">
	   		<thead>
	   			<tr>
	   				<th>企业ID</th>
	   				<th>企业名称</th>
	   				<th>所属地区</th>
	   				<th>详细地址</th>
	   				<th>主要业务应用</th>
	   				<th>联系电话</th>
	   				<th>添加时间</th>
	   				<th>操作</th>
	   			</tr>
	   		</thead>
		   	<tbody></tbody>
		</table>
    </div>
     <!-- 分页条 -->
    <%@include file="../common/global/pagingtoolbar.jsp" %>
  </div>
  <script src="res/js/business/busInfoList.js" type="text/javascript"></script>
</body>
</html>

