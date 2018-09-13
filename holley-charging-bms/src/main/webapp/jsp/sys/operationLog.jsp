<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>操作日志</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
  	<div id="logList">
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
				<input id="keyword" type="text" class="form-control" placeholder="用户昵称/手机号码/操作内容" style="width: 240px;"/>
				<select id="userType" name="userType" class="form-control">
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
			     <select id="logType" name="logType" class="form-control">
			     	<option value="0">日志类型</option>
			     	<s:iterator value="#request.logTypeList" var="item" status="st">
			     		<s:if test="#request.logType == #item.value">
			     			<option value="${item.value}" selected="selected">${item.name}</option>
			     		</s:if>
			     		<s:else>
				     		<option value="${item.value}">${item.name}</option>
			     		</s:else>
			     	</s:iterator>
			     </select>
			     <select id="operatorType" name="operatorType" class="form-control">
			     	<option value="0">操作类型</option>
			     	<s:iterator value="#request.operatorTypeList" var="item" status="st">
			     		<s:if test="#request.operatorType == #item.value">
			     			<option value="${item.value}" selected="selected">${item.title}</option>
			     		</s:if>
			     		<s:else>
				     		<option value="${item.value}">${item.title}</option>
			     		</s:else>
			     	</s:iterator>
			     </select>
			</div>
			 <div class="form-group pull-right">
				<button type="button" id="queryBtn" class="btn btn-primary">查询</button>
			 </div>
	    </form>
	    <!-- 表格 -->
	    <div id="logTableDiv">
		    <table class="table table-condensed table-hover" id="logTable">
		   		<thead>
		   			<tr>
		   				<th>日志ID</th>
		   				<th>用户编码</th>
		   				<th>用户类型</th>
		   				<th>用户昵称</th>
		   				<th>手机号码</th>
		   				<th>操作类型</th>
		   				<th>操作内容</th>
		   				<th class="hide">操作IP</th>
		   				<th>添加时间</th>
		   			</tr>
		   		</thead>
			   	<tbody></tbody>
			</table>
	    </div>
	     <!-- 分页条 -->
	    <%@include file="../common/global/pagingtoolbar.jsp" %>
  	</div>
  	<div id="logDetail" class="hide">
  		<span class="glyphicon glyphicon-menu-left" data-toggle="tooltip" title="返回" aria-hidden="true" 
	    style="cursor:pointer;font-weight: bold;font-size: 16px;" onclick="showLogResult('logList')"></span>
    	<form class="form-horizontal" role="form">
	    	<div class="form-group">
		        <label class="col-sm-2 control-label">用户编码</label>
		        <div class="col-sm-4">
		      	  <p id="detail_userid" class="form-control-static"></p>
		        </div>
		        <label class="col-sm-2 control-label">用户类型</label>
		        <div class="col-sm-4">
		      	  <p id="detail_usertype" class="form-control-static"></p>
		        </div>
    	    </div>
    	    <div class="form-group">
		        <label class="col-sm-2 control-label">用户昵称</label>
		        <div class="col-sm-4">
		      	  <p id="detail_username" class="form-control-static"></p>
		        </div>
		        <label class="col-sm-2 control-label">手机号码</label>
		        <div class="col-sm-4">
		      	  <p id="detail_phone" class="form-control-static"></p>
		        </div>
    	    </div>
    	    <div class="form-group">
		        <label class="col-sm-2 control-label">操作类型</label>
		        <div class="col-sm-4">
		      	  <p id="detail_type" class="form-control-static"></p>
		        </div>
		        <label class="col-sm-2 control-label">操作内容</label>
		        <div class="col-sm-4">
		      	  <p id="detail_described" class="form-control-static"></p>
		        </div>
    	    </div>
    	    <div class="form-group">
		        <label class="col-sm-2 control-label">操作IP</label>
		        <div class="col-sm-4">
		      	  <p id="detail_ip" class="form-control-static"></p>
		        </div>
		        <label class="col-sm-2 control-label">添加时间</label>
		        <div class="col-sm-4">
		      	  <p id="detail_addtime" class="form-control-static"></p>
		        </div>
    	    </div>
  			
   	    </form>
  	</div>
  </div>
  <script src="res/js/sys/operationLog.js" type="text/javascript"></script>
  <script type="text/javascript">
  var webuserid = ${webUser.userId};
  </script>
</body>
</html>

