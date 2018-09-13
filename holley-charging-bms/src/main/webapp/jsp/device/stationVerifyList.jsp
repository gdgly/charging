<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>设备审核记录</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
    <form class="form-inline" role="form">
	    <div class="form-group">
		 	<div class="input-group date" id="stationStartDateDiv" style="width: 150px;">
                <input id="stationStartDate" type="text" class="form-control" placeholder="开始时间"/>
                <span class="input-group-addon">
                	<span class="glyphicon glyphicon-calendar"></span>
                </span>
		     </div>
		     <span>—</span>
 		 	 <div class='input-group date' id="stationEndDateDiv" style="width: 150px;">
                <input id="stationEndDate" type="text" class="form-control" placeholder="结束时间"/>
                <span class="input-group-addon">
                	<span class="glyphicon glyphicon-calendar"></span>
                </span>
		     </div>
		     <input id="stationName" type="text" class="form-control" placeholder="请输入充电点名称"/>
		     <select id="stationVerifyStatus" name="stationVerifyStatus" class="form-control">
		     	<option value="0">审核状态</option>
		     	<s:iterator value="#request.verifyStatusList" var="item" status="st">
		     		<s:if test="#request.stationVerifyStatus == #item.value">
		     			<option value="${item.value}" selected="selected">${item.text}</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.value}">${item.text}</option>
		     		</s:else>
		     	</s:iterator>
		     </select>
		</div>
		<div class="form-group pull-right">
			<button type="button" id="stationQueryBtn" class="btn btn-primary">查询</button>
		</div>
		 
    </form>
    <!-- 表格 -->
    <div id="stationTableDiv">
	    <table class="table table-condensed table-hover" id="stationTable">
	   		<thead>
	   			<tr>
	   				<th>ID</th>
	   				<th>点名称</th>
	   				<th>充电点ID</th>
	   				<th>更新时间</th>
	   				<th>运营类型</th>
	   				<th>用户操作</th>
	   				<th>审核状态</th>
	   				<th>审核时间</th>
	   				<th>审核备注</th>
	   				<th>操作</th>
	   			</tr>
	   		</thead>
		   	<tbody></tbody>
		</table>
    </div>
    <!-- 分页条 -->
    <%@include file="../common/global/pagingtoolbar.jsp" %>
</div>
  
  <script src="res/js/device/stationVerifyList.js" type="text/javascript"></script>
</body>
</html>

