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
  	<s:if test="#request.detailtype == 'indetail'">
	  	<!-- ------------------------------收入明细 --------------------------------------------->
	  	<div id="inDetailResult">
	  		<form id="conditionFrom" class="form-inline" role="form" style="padding-left: 20px;padding-top: 10px;" action="deal/bills_queryInDetail.action">
			    <div class="form-group" style="margin-top: 5px;">
			    	<div class="input-group date" id="detailDateDiv" style="width: 120px;">
		                <input id="detailDate" name="checkcycle" type="text" class="form-control" placeholder="月份"/>
		                <span class="input-group-addon">
		                	<span class="glyphicon glyphicon-calendar"></span>
		                </span>
				     </div>
					<select id="billMarkin" name="checkmark" class="form-control">
				     	<option value="0">业务类型</option>
				     	<s:iterator value="#request.biiiMarkList" var="item" status="st">
					     	<option value="${item.value}">${item.text}</option>
				     	</s:iterator>
				     </select>
				     <input id="isExport" name="isExport" value="true" type="hidden">
			 		 <input id="fileName" name="fileName" value="收入明细" type="hidden">
			 		 <input id="userid" name="userid" value="<s:property value="#request.userid"/>" type="hidden">
				</div>
				<div class="form-group pull-right">
					<button type="button" class="btn btn-primary" onclick="goback()">返回</button>
					<button type="button" id="queryBtn" class="btn btn-primary">查询</button>
		    		<button type="button" id="exportBtn" class="btn btn-primary">导出</button>
				 </div>
		    </form>
		    <label class="pull-right">
			    <span id="appFeeIn" class="color-orange"></span> | 
			    <span id="chaFeeIn" class="color-orange"></span> | 
			    <span id="serviceFeeIn" class="color-orange"></span> | 
			    <span id="parkFeeIn" class="color-orange"></span> | 
			    <span id="totalFeeIn" class="color-orange"></span>
		    </label>
		    <!-- 表格 -->
		    <table class="table table-condensed table-hover" id="detailInTable">
		   		<thead>
		   			<tr>
		   				<!-- <th>ID</th> -->
		   				<th>数据时间</th>
		   				<th>电桩</th>
		   				<th>类型</th>
		   				<th>预约费</th>
		   				<th>充电费</th>
		   				<th>服务费</th>
		   				<th>停车费</th>
		   				<th>结算后合计</th>
		   				<th>结算前合计</th>
		   				<th>充电/预约ID</th>
		   			</tr>
		   		</thead>
			   	<tbody></tbody>
			</table>
		    <!-- 分页条 -->
		    <%@include file="../common/global/pagingtoolbar.jsp" %>
	  	</div>
  	</s:if>
  	<s:elseif test="#request.detailtype == 'outdetail'">
	  	<!-- ------------------------------支出明细 --------------------------------------------->
	  	<div id="outDetailResult">
	  		<form id="conditionFrom" class="form-inline" role="form" style="padding-left: 20px;padding-top: 10px;" action="deal/bills_queryOutDetail.action">
			    <div class="form-group" style="margin-top: 5px;">
			    	<div class="input-group date" id="detailDateDiv" style="width: 120px;">
		                <input id="detailDate" name="checkcycle" type="text" class="form-control" placeholder="月份"/>
		                <span class="input-group-addon">
		                	<span class="glyphicon glyphicon-calendar"></span>
		                </span>
				     </div>
					<select id="billMarkout" name="checkmark" class="form-control">
				     	<option value="0">业务类型</option>
				     	<s:iterator value="#request.biiiMarkList" var="item" status="st">
					     	<option value="${item.value}">${item.text}</option>
				     	</s:iterator>
				     </select>
				     <input id="isExport" name="isExport" value="true" type="hidden">
			 		 <input id="fileName" name="fileName" value="支出明细" type="hidden">
			 		 <input id="userid" name="userid" value="<s:property value="#request.userid"/>" type="hidden">
				</div>
				<div class="form-group pull-right">
				    <button type="button" class="btn btn-primary" onclick="goback()">返回</button>
					<button type="button" id="queryBtn" class="btn btn-primary">查询</button>
		    		<button type="button" id="exportBtn" class="btn btn-primary">导出</button>
				 </div>
		    </form>
		    <label class="pull-right">
			    <span id="appFeeOut" class="color-orange"></span> | 
			    <span id="chaFeeOut" class="color-orange"></span> | 
			    <span id="serviceFeeOut" class="color-orange"></span> | 
			    <span id="parkFeeOut" class="color-orange"></span> | 
			    <span id="totalFeeOut" class="color-orange"></span>
		    </label>
		    <!-- 表格 -->
		    <table class="table table-condensed table-hover" id="detailOutTable">
		   		<thead>
		   			<tr>
		   				<th>数据时间</th>
		   				<th>电桩</th>
		   				<th>类型</th>
		   				<th>预约费</th>
		   				<th>充电费</th>
		   				<th>服务费</th>
		   				<th>停车费</th>
		   				<th>合计</th>
		   				<th>充电/预约ID</th>
		   			</tr>
		   		</thead>
			   	<tbody></tbody>
			</table>
		    <!-- 分页条 -->
		    <%@include file="../common/global/pagingtoolbar.jsp" %>
	  	</div>
  	</s:elseif>
  	<s:elseif test="#request.detailtype == 'rechargedetail'">
  		<!-- ------------------------------充值明细 --------------------------------------------->
	  	<div id="rechargeDetailResult">
	  		<form id="conditionFrom" class="form-inline" role="form" style="padding-left: 20px;padding-top: 10px;" action="deal/bills_queryRechargeDetail.action">
			    <div class="form-group" style="margin-top: 5px;">
			    	<div class="input-group date" id="detailDateDiv" style="width: 120px;">
		                <input id="detailDate" name="checkcycle" type="text" class="form-control" placeholder="月份"/>
		                <span class="input-group-addon">
		                	<span class="glyphicon glyphicon-calendar"></span>
		                </span>
				     </div>
				     <input id="isExport" name="isExport" value="true" type="hidden">
			 		 <input id="fileName" name="fileName" value="充值明细" type="hidden">
			 		 <input id="userid" name="userid" value="<s:property value="#request.userid"/>" type="hidden">
				</div>
				<div class="form-group pull-right">
					<label style="padding-right: 10px;"><span id="totalMoney" class="color-orange"></span></label>
				    <button type="button" class="btn btn-primary" onclick="goback()">返回</button>
					<button type="button" id="queryBtn" class="btn btn-primary">查询</button>
		    		<button type="button" id="exportBtn" class="btn btn-primary">导出</button>
				 </div>
		    </form>
		    <!-- 表格 -->
		    <table class="table table-condensed table-hover" id="detailRechargeTable">
		   		<thead>
		   			<tr>
		   				<th>更新时间</th>
		   				<th>充值金额(元)</th>
		   				<th>支付方式</th>
		   				<th>支付信息</th>
		   				<th>交易号</th>
		   				<th>充值ID</th>
		   			</tr>
		   		</thead>
			   	<tbody></tbody>
			</table>
		    <!-- 分页条 -->
		    <%@include file="../common/global/pagingtoolbar.jsp" %>
	  	</div>
  	</s:elseif>
  	<s:elseif test="#request.detailtype == 'cashdetail'">
  		<!-- ------------------------------提现明细 --------------------------------------------->
	  	<div id="rechargeDetailResult">
	  		<form id="conditionFrom" class="form-inline" role="form" style="padding-left: 20px;padding-top: 10px;" action="deal/bills_queryCashDetail.action">
			    <div class="form-group" style="margin-top: 5px;">
			    	<div class="input-group date" id="detailDateDiv" style="width: 120px;">
		                <input id="detailDate" name="checkcycle" type="text" class="form-control" placeholder="月份"/>
		                <span class="input-group-addon">
		                	<span class="glyphicon glyphicon-calendar"></span>
		                </span>
				     </div>
				     <input id="isExport" name="isExport" value="true" type="hidden">
			 		 <input id="fileName" name="fileName" value="提现明细" type="hidden">
			 		 <input id="userid" name="userid" value="<s:property value="#request.userid"/>" type="hidden">
				</div>
				<div class="form-group pull-right">
					<label><span id="totalMoney" class="color-orange"></span></label>
				    <button type="button" class="btn btn-primary" onclick="goback()">返回</button>
					<button type="button" id="queryBtn" class="btn btn-primary">查询</button>
		    		<button type="button" id="exportBtn" class="btn btn-primary">导出</button>
				 </div>
		    </form>
		    <!-- 表格 -->
		    <table class="table table-condensed table-hover" id="detailCashTable">
		   		<thead>
		   			<tr>
		   				<th>申请时间</th>
		   				<th>提现金额(元)</th>
		   				<th>提现方式</th>
		   				<th>提现状态</th>
		   				<th>提现ID</th>
		   			</tr>
		   		</thead>
			   	<tbody></tbody>
			</table>
		    <!-- 分页条 -->
		    <%@include file="../common/global/pagingtoolbar.jsp" %>
	  	</div>
  	</s:elseif>
</div>
  <script src="res/js/deal/billsDetail.js" type="text/javascript"></script>
  <script type="text/javascript">
  	var userid = '<s:property value="#request.userid"/>';
  	var detailtype = '<s:property value="#request.detailtype"/>';
  	var datatime = '<s:property value="#request.datatime"/>';
  </script>
</body>
</html>

