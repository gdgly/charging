<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>资金日志</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
    <!-- 资金日志列表 -->
  	<div id="accountLogResult">
	    <form id="conditionFrom" class="form-inline" role="form" action="deal/accountLog_queryList.action">
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
			     <select id="logType" name="logtype" class="form-control">
			     	<option value="0">操作类型</option>
			     	<s:iterator value="#request.logTypeList" var="item" status="st">
			     		<s:if test="#request.logType == #item.value">
			     			<option value="${item.value}" selected="selected">${item.text}</option>
			     		</s:if>
			     		<s:else>
				     		<option value="${item.value}">${item.text}</option>
			     		</s:else>
			     	</s:iterator>
			     </select>
			     <select id="direction" name="direction" class="form-control">
			     	<option value="0">资金流向</option>
			     	<s:iterator value="#request.directionList" var="item" status="st">
			     		<s:if test="#request.direction == #item.value">
			     			<option value="${item.value}" selected="selected">${item.text}</option>
			     		</s:if>
			     		<s:else>
				     		<option value="${item.value}">${item.text}</option>
			     		</s:else>
			     	</s:iterator>
			     </select>
		     <input id="isExport" name="isExport" value="true" type="hidden">
			 <input id="fileName" name="fileName" value="资金日志" type="hidden">
			</div>
			 <div class="form-group pull-right">
				<button type="button" id="queryBtn" class="btn btn-primary">查询</button>
		    	<button type="button" id="exportBtn" class="btn btn-primary">导出</button>
			 </div>
	    </form>
	    <!-- 表格 -->
	    <div id="accountLogTableDiv">
		    <table class="table table-condensed table-hover" id="accountLogTable">
		   		<thead>
		   			<tr>
		   				<th>用户编码</th>
		   				<th>用户昵称</th>
		   				<th>手机号码</th>
		   				<th>用户类型</th>
		   				<th>记录Id</th>
		   				<th>操作类型</th>
		   				<th>资金流向</th>
		   				<th>操作金额</th>
		   				<th>账户总额</th>
		   				<th>可用金额</th>
		   				<th>冻结金额</th>
		   				<th>更新时间</th>
		   			</tr>
		   		</thead>
			   	<tbody></tbody>
			</table>
	    </div>
	     <!-- 分页条 -->
	    <%@include file="../common/global/pagingtoolbar.jsp" %>
  	  </div>
  	  <!-- 结算详细 -->
  	  <div id="billsResult" class="hide">
  	  	<span class="glyphicon glyphicon-menu-left" data-toggle="tooltip" title="返回" aria-hidden="true" 
	    style="cursor:pointer;font-weight: bold;font-size: 16px;" onclick="showResult('showaccountlog')"></span>
  	  	<form class="form-horizontal" role="form">
  	  		<div class="form-group">
		        <label class="col-sm-2 control-label">用户id</label>
		        <div class="col-sm-4">
		      	  <p id="bills_userid" class="form-control-static"></p>
		        </div>
		        <label class="col-sm-2 control-label">用户类型</label>
		        <div class="col-sm-4">
		      	  <p id="bills_usertype" class="form-control-static"></p>
		        </div>
    	    </div>
    	    <div class="form-group">
		        <label class="col-sm-2 control-label">用户昵称</label>
		        <div class="col-sm-4">
		      	  <p id="bills_username" class="form-control-static"></p>
		        </div>
		        <label class="col-sm-2 control-label">手机号码</label>
		        <div class="col-sm-4">
		      	  <p id="bills_phone" class="form-control-static"></p>
		        </div>
    	    </div>
    	    <div class="form-group">
		        <label class="col-sm-2 control-label">结算时间</label>
		        <div class="col-sm-4">
		      	  <p id="bills_checkcycle" class="form-control-static"></p>
		        </div>
		        <label class="col-sm-2 control-label">收入总额</label>
		        <div class="col-sm-4">
		      	  <p id="bills_totalfeein" class="form-control-static"></p>
		        </div>
    	    </div>
    	    <div class="form-group">
		        <label class="col-sm-2 control-label">预约费</label>
		        <div class="col-sm-4">
		      	  <p id="bills_appfeein" class="form-control-static"></p>
		        </div>
		        <label class="col-sm-2 control-label">充电费</label>
		        <div class="col-sm-4">
		      	  <p id="bills_chafeein" class="form-control-static"></p>
		        </div>
    	    </div>
    	    <div class="form-group">
		        <label class="col-sm-2 control-label">服务费</label>
		        <div class="col-sm-4">
		      	  <p id="bills_servicefeein" class="form-control-static"></p>
		        </div>
		        <label class="col-sm-2 control-label">停车费</label>
		        <div class="col-sm-4">
		      	  <p id="bills_parkfeein" class="form-control-static"></p>
		        </div>
    	    </div>
  	  	</form>
  	  </div>
    </div>
  <script src="res/js/deal/accountLogList.js" type="text/javascript"></script>
</body>
</html>

