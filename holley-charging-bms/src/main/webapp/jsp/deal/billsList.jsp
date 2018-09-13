<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>结算账单查询</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
  <!-- ------------------------------结算账单列表 --------------------------------------------->
  	<div id="billsListResult">
	    <form id="conditionFrom" class="form-inline" role="form" action="deal/bills_queryBillsList.action">
		    <div class="form-group" style="margin-top: 5px;">
		    	<div class="input-group date" id="startDateDiv" style="width: 120px;">
	                <input id="startDate" name="startmonth" type="text" class="form-control" placeholder="开始月份"/>
	                <span class="input-group-addon">
	                	<span class="glyphicon glyphicon-calendar"></span>
	                </span>
			     </div>
			     <span>—</span>
	 		 	 <div class='input-group date' id="endDateDiv" style="width: 120px;">
	                <input id="endDate" name="endmonth" type="text" class="form-control" placeholder="结束月份"/>
	                <span class="input-group-addon">
	                	<span class="glyphicon glyphicon-calendar"></span>
	                </span>
			     </div>
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
			     <select id="isReceipt" name="isreceipt" class="form-control">
			     	<option value="0">是否开票</option>
			     	<s:iterator value="#request.whetherList" var="item" status="st">
			     		<s:if test="#request.isReceipt == #item.value">
			     			<option value="${item.value}" selected="selected">
			     				<s:if test="#item.value == 1">已开票</s:if>
			     				<s:else>未开票</s:else>
		     				</option>
			     		</s:if>
			     		<s:else>
				     		<option value="${item.value}">
				     			<s:if test="#item.value == 1">已开票</s:if>
			     				<s:else>未开票</s:else>
				     		</option>
			     		</s:else>
			     	</s:iterator>
			     </select>
			     <input id="isExport" name="isExport" value="true" type="hidden">
			 <input id="fileName" name="fileName" value="结算账单" type="hidden">
			</div>
			<div class="form-group pull-right">
				<button type="button" id="queryBtn" class="btn btn-primary">查询</button>
		    	<button type="button" id="exportBtn" class="btn btn-primary">导出</button>
			 </div>
	    </form>
	    <!-- 表格 -->
	    <table class="table table-condensed table-hover" id="billsTable">
	   		<thead>
	   			<tr>
	   				<th>ID</th>
	   				<th>用户昵称</th>
	   				<th>手机号码</th>
	   				<th>月份</th>
	   				<th>预约+</th>
	   				<th>充电+</th>
	   				<th>服务+</th>
	   				<th>停车+</th>
	   				<th>总收入</th>
	   				<th>充值</th>
	   				<th>提现</th>
	   				<th>预约-</th>
	   				<th>充电-</th>
	   				<th>服务-</th>
	   				<th>停车-</th>
	   				<th>总支出</th>
	   				<th>开票id</th>
	   			</tr>
	   		</thead>
		   	<tbody></tbody>
		</table>
	    <!-- 分页条 -->
	    <%@include file="../common/global/pagingtoolbar.jsp" %>
  	</div>
</div>
  <script src="res/js/deal/billsList.js" type="text/javascript"></script>
</body>
</html>

