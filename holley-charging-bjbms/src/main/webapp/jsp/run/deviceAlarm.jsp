<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>设备告警记录</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
    <form id="conditionFrom" class="form-inline" role="form">
	    <div class="form-group">
			<input id="keyword" name="keyword" maxlength="50" type="text" style="width: 200px;" class="form-control" placeholder="请输入充电站名称/地址"/>
			<div class='input-group date' id='startTimeDate'>
							<input id="startTime" name="startTime" type='text' class="form-control" placeholder="开始时间" style="width: 150px;"/> 
							<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span> </span>
			</div>
			一
			<div class='input-group date' id='endTimeDate'>
							<input id="endTime" name="endTime" type='text' class="form-control" placeholder="结束时间" style="width: 150px;"/> 
							<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span> </span>
			</div>
		</div>
		 <div class="form-group pull-right">
			<button type="button" id="queryBtn" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
		 </div>
    </form>
    <!-- 表格 -->
    <div id="deviceAlarmTableDiv">
	    <table class="table table-condensed table-hover" id="deviceAlarmTable">
	   		<thead>
	   			<tr>
	   				<th>充电站名称</th>
	   				<th>桩名称</th>
	   				<th>地址</th>
	   				<th>事件描述</th>
	   				<th>告警时间</th>
	   			</tr>
	   		</thead>
		   	<tbody id="deviceAlarmTbody"></tbody>
		</table>
		<p id="noListMsg" class="text-center" style="margin-top: 50px;">暂无告警信息</p>
    </div>
     <%@include file="../common/global/pagingtoolbar.jsp" %>
  </div>
<script src="res/js/run/deviceAlarm.js" type="text/javascript"></script>
</body>
</html>

