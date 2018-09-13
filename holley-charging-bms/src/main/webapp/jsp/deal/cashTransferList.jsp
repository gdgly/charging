<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>运营商实名审核记录</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
    <form id="conditionFrom" class="form-inline" role="form" action="deal/cashTransfer_queryCashList.action">
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
		     <input id="userInfo" name="userinfo" type="text" class="form-control" placeholder="请输入真实姓名/手机号"/>
		     <select id="cashStatus" name="cashstatus" class="form-control">
		     	<option value="0">提现状态</option>
		     	<s:iterator value="#request.cashStatusList" var="item" status="st">
		     		<s:if test="#request.verifyStatus == #item.value">
		     			<option value="${item.value}" selected="selected">${item.text}</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.value}">${item.text}</option>
		     		</s:else>
		     	</s:iterator>
		     </select>
		     <input id="isExport" name="isExport" value="true" type="hidden">
			 <input id="fileName" name="fileName" value="提现转账记录" type="hidden">
		</div>
		<div class="form-group pull-right">
		    <button type="button" id="queryBtn" class="btn btn-primary">查询</button>
		    <button type="button" id="exportBtn" class="btn btn-primary">导出</button>
		</div>
    </form>
    <!-- 表格 -->
    <div id="tableDiv">
	    <table class="table table-condensed table-hover" id="cashTable">
	   		<thead>
	   			<tr>
	   				<th>ID</th>
	   				<th>真实姓名</th>
	   				<th>用户手机</th>
	   				<th>用户类型</th>
	   				<th>提现金额(元)</th>
	   				<th>提现方式</th>
	   				<!-- <th>提现账户信息</th> -->
	   				<th>提现状态</th>
	   				<th>备注</th>
	   				<th>申请时间</th>
	   				<th>审核状态</th>
	   				<th>审核时间</th>
	   				<th>审核备注</th>
	   				<th style="width: 40px;">操作</th>
	   			</tr>
	   		</thead>
		   	<tbody></tbody>
		</table>
    </div>
    <!-- 分页条 -->
    <%@include file="../common/global/pagingtoolbar.jsp" %>
</div>
  <script src="res/js/deal/cashTransferList.js" type="text/javascript"></script>
</body>
</html>

