<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>规则配置</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
  	<div id = "ruleListResult">
	    <form id="conditionFrom" class="form-inline" role="form" action="sys/rule_queryList.action">
		    <div class="form-group">
				<input id="keyword" name="keyword" type="text" class="form-control" placeholder="请输入规则名称 " style="width: 190px;"/>
			    <select id="ruleStatus" name="status" class="form-control">
			     	<option value="0">规则状态</option>
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
				<input id="fileName" name="fileName" value="系统规则" type="hidden">
			</div>
			 <div class="form-group pull-right">
				<button type="button" id="queryBtn" class="btn btn-primary">查询</button>
			    <button type="button" id="addBtn" class="btn btn-primary">新增</button>
		    	<button type="button" id="exportBtn" class="btn btn-primary">导出</button>
			 </div>
	    </form>
	    <!-- 数据表格 -->
	    <div id="ruleTableDiv">
		    <table class="table table-condensed table-hover" id="ruleTable">
		   		<thead>
		   			<tr>
		   				<th>规则ID</th>
		   				<th>规则名称</th>
		   				<th>规则值</th>
		   				<th>状态</th>
		   				<th>添加时间</th>
		   				<th>操作</th>
		   			</tr>
		   		</thead>
			   	<tbody></tbody>
			</table>
	    </div>
	     <!-- 分页条 -->
	    <%@include file="../common/global/pagingtoolbar.jsp" %>
  	</div>
  	<div id="ruleDetailResult" style="display: none;">
  		<span id="backPileListBtn" class="glyphicon glyphicon-menu-left" data-toggle="tooltip" title="返回" aria-hidden="true" 
	    style="cursor:pointer;font-weight: bold;font-size: 16px;" onclick="hideDetailResult()"></span>
  		<form class="form-horizontal" role="form">
  			<div class="form-group">
		        <label class="col-sm-2 control-label">规则id</label>
		        <div class="col-sm-4">
		      	  <p id="detail_id" class="form-control-static"></p>
		        </div>
		        <label class="col-sm-2 control-label">规则名称</label>
		        <div class="col-sm-4">
		      	  <p id="detail_name" class="form-control-static"></p>
		        </div>
    	    </div>
    	    <div class="form-group">
		        <label class="col-sm-2 control-label">规则状态</label>
		        <div class="col-sm-4">
		      	  <p id="detail_status" class="form-control-static"></p>
		        </div>
		        <label class="col-sm-2 control-label">规则值</label>
		        <div class="col-sm-4">
		      	  <p id="detail_rulecheck" class="form-control-static"></p>
		        </div>
    	    </div>
    	    <div class="form-group">
		        <label class="col-sm-2 control-label">备注</label>
		        <div class="col-sm-4">
		      	  <p id="detail_remark" class="form-control-static"></p>
		        </div>
		        <label class="col-sm-2 control-label">添加时间</label>
		        <div class="col-sm-4">
		      	  <p id="detail_addtime" class="form-control-static"></p>
		        </div>
    	    </div>
  		</form>
  	</div>
  </div>
  <script src="res/js/sys/ruleList.js" type="text/javascript"></script>
</body>
</html>

