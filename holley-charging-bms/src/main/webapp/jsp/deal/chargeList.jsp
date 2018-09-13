<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>充电记录</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
    <form id="conditionFrom" class="form-inline" role="form" action="deal/charge_queryList.action">
	    <div class="form-group">
			<div class="input-group date" id="startDateDiv" style="width: 150px;">
                <input id="startDate" name="startdate" type="text" class="form-control" placeholder="开始时间"/>
                <span class="input-group-addon">
                	<span class="glyphicon glyphicon-calendar"></span>
                </span>
		     </div>
		     <span>—</span>
 		 	 <div class='input-group date' id="endDateDiv" style="width: 150px;">
                <input id="endDate" name="enddate" type="text" class="form-control" placeholder="结束时间"/>
                <span class="input-group-addon">
                	<span class="glyphicon glyphicon-calendar"></span>
                </span>
		     </div>
		 	<select id="dealStatus" name="dealstatus" class="form-control">
		     	<option value="0">交易状态</option>
		     	<s:iterator value="#request.dealStatusList" var="item" status="st">
		     		<s:if test="#request.dealStatus == #item.value">
		     			<option value="${item.value}" selected="selected">${item.text}</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.value}">${item.text}</option>
		     		</s:else>
		     	</s:iterator>
		     </select>
		     <select id="payStatus" name="paystatus" class="form-control">
		     	<option value="0">支付状态</option>
		     	<s:iterator value="#request.payStatusList" var="item" status="st">
		     		<s:if test="#request.payStatus == #item.value">
		     			<option value="${item.value}" selected="selected">${item.text}</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.value}">${item.text}</option>
		     		</s:else>
		     	</s:iterator>
		     </select>
		     <select id="billStatus" name="billstatus" class="form-control">
		     	<option value="0">是否结算</option>
		     	<s:iterator value="#request.billStatusList" var="item" status="st">
		     		<s:if test="#request.billStatus == #item.value">
		     			<option value="${item.value}" selected="selected">${item.text}</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.value}">${item.text}</option>
		     		</s:else>
		     	</s:iterator>
		     </select>
		</div>
		 <div class="form-group" style="margin-top: 10px;">
		     <input id="searchTradeno" name="searchtradeno" type="text" class="form-control" placeholder="请输入交易号"/>
		     <input id="searchUser" name="searchuser" type="text" class="form-control" placeholder="请输入用户昵称/手机"/>
		     <input id="searchStation" name="searchstation" type="text" class="form-control" placeholder="请输入充电点名称/地址"/>
		     
		     <input id="isExport" name="isExport" value="true" type="hidden">
			 <input id="fileName" name="fileName" value="充电记录" type="hidden">
		</div>
		<div class="form-group pull-right" style="margin-top: 10px;">
		    <button type="button" id="queryBtn" class="btn btn-primary">查询</button>
		    <button type="button" id="exportBtn" class="btn btn-primary">导出</button>
		</div>
    </form>
    <!-- 表格 -->
    <div id="chargetableDiv">
	    <table class="table table-condensed table-hover" id="chargeTable">
	   		<thead>
	   			<tr>
	   				<th>交易ID</th>
	   				<th>交易号</th>
	   				<th>手机号码</th>
	   				<th>电桩信息</th>
	   				<th>开始时间</th>
	   				<th>充电时长(分)</th>
	   				<th>充电电量(度)</th>
	   				<th>应缴金额(元)</th>
	   				<th>交易状态</th>
	   				<th>支付状态</th>
	   				<th>结算状态</th>
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
  <script src="res/js/deal/chargeList.js" type="text/javascript"></script>
</body>
</html>

