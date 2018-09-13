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
    <form class="form-inline" role="form">
	    <div class="form-group">
		 	<div class="input-group date" id="startDateDiv" style="width: 150px;">
                <input id="startDate" type="text" class="form-control" placeholder="开始时间"/>
                <span class="input-group-addon">
                	<span class="glyphicon glyphicon-calendar"></span>
                </span>
		     </div>
		     <span>—</span>
 		 	 <div class='input-group date' id="endDateDiv" style="width: 150px;">
                <input id="endDate" type="text" class="form-control" placeholder="结束时间"/>
                <span class="input-group-addon">
                	<span class="glyphicon glyphicon-calendar"></span>
                </span>
		     </div>
		     <input id="busName" type="text" class="form-control" placeholder="请输入运营商名称"/>
		     <select id="verifyStatus" name="verifyStatus" class="form-control">
		     	<option value="0">审核状态</option>
		     	<s:iterator value="#request.statusList" var="item" status="st">
		     		<s:if test="#request.verifyStatus == #item.value">
		     			<option value="${item.value}" selected="selected">${item.text}</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.value}">${item.text}</option>
		     		</s:else>
		     	</s:iterator>
		     </select>
		</div>
		<div class="form-group pull-right">
		    <button type="button" id="queryBtn" class="btn btn-primary">查询</button>
		</div>
    </form>
    <!-- 表格 -->
    <div id="tableDiv">
	    <table class="table table-condensed table-hover" id="busRealTable">
	   		<thead>
	   			<tr>
	   				<th>企业信息ID</th>
	   				<th>企业名称</th>
	   				<th>提交时间</th>
	   				<th>开户姓名</th>
	   				<th>开户银行</th>
	   				<th>开户帐号</th>
	   				<th>申请人</th>
	   				<th>审核状态</th>
	   				<th>转账金额(元)</th>
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
  <script src="res/js/business/busRealTransferList.js" type="text/javascript"></script>
</body>
</html>

