<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>充电卡充值记录</title>
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
			<input id="keyword" name="keyword" maxlength="50" type="text" style="width: 350px;" class="form-control" placeholder="请输入用户名姓名/身份证号/手机号/车牌号/卡号"/>
		</div>
		 <div class="form-group pull-right">
			<button type="button" id="queryBtn" class="btn btn-primary">查询</button>
		 </div>
    </form>
    <!-- 表格 -->
    <div id="cardRechargeTableDiv">
	    <table class="table table-condensed table-hover" id="cardRechargeTable">
	   		<thead>
	   			<tr>
	   				<th>用户</th>
	   				<th>手机号码</th>
	   				<th>身份证号</th>
	   				<th>卡号</th>
	   				<th>车牌号</th>
	   				<th>订单号</th>
	   				<th>充值金额(元)</th>
	   				<th>充值时间</th>
	   			</tr>
	   		</thead>
		   	<tbody id="cardRechargeTbody"></tbody>
		</table>
		<p id="noListMsg" class="text-center" style="margin-top: 50px;">暂无充值信息</p>
    </div>
     <%@include file="../common/global/pagingtoolbar.jsp" %>
  </div>
<script src="res/js/chargecard/cardRechargeList.js" type="text/javascript"></script>
</body>
</html>

