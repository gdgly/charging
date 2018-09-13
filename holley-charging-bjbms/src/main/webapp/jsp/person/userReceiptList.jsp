<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>个人开票记录</title>
  <%@include file="../common/global/meta.jsp"%>
   <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
    <form id="conditionFrom" class="form-inline" role="form" action="person/userReceipt_queryList.action">
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
		     <input id="keyword" name="keyword" type="text" class="form-control" placeholder="请输入申请人昵称/申请人号码/收件人/收件人号码/快递单号" style="width: 400px;"/>
		     <select id="status" name="status" class="form-control">
		     	<option value="0">状态</option>
		     	<s:iterator value="#request.statusList" var="item" status="st">
		     		<s:if test="#request.status == #item.value">
		     			<option value="${item.value}" selected="selected">${item.text}</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.value}">${item.text}</option>
		     		</s:else>
		     	</s:iterator>
		     </select>
		     <input id="isExport" name="isExport" value="true" type="hidden">
			 <input id="fileName" name="fileName" value="个人开票记录" type="hidden">
		</div>
		<div class="form-group pull-right">
		    <button type="button" id="queryBtn" class="btn btn-primary">查询</button>
		    <button type="button" id="exportBtn" class="btn btn-primary">导出</button>
		</div>
    </form>
    <!-- 表格 -->
    <table class="table table-condensed table-hover" id="userReceiptTable">
   		<thead>
   			<tr>
   				<th>开票ID</th>
   				<th>发票类型</th>
   				<th>发票抬头</th>
   				<th>开票月份</th>
   				<th>开票金额</th>
   				<th>开票状态</th>
   				<th>收件人</th>
   				<th>收件人号码</th>
   				<th>收件地址</th>
   				<th>申请人</th>
   				<th>申请时间</th>
   				<th>操作</th>
   			</tr>
   		</thead>
	   	<tbody></tbody>
	</table>
    <!-- 分页条 -->
    <%@include file="../common/global/pagingtoolbar.jsp" %>
</div>
  <script src="res/js/person/userReceiptList.js" type="text/javascript"></script>
</body>
</html>

