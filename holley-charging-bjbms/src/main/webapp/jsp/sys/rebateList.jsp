<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>优惠方案</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container" id="rebateListDiv">
    <form id="conditionFrom" class="form-inline" role="form" action="sys/user_queryRebateList.action">
	    <div class="form-group">
			<input id="keyword" name="keyword" type="text" class="form-control" placeholder="请输入优惠方案名称" style="width: 190px;"/>
		     <input id="isExport" name="isExport" value="true" type="hidden">
			 <input id="fileName" name="fileName" value="优惠方案" type="hidden">
		</div>
		 <div class="form-group pull-right">
			<button type="button" id="queryBtn" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
		    <button type="button" id="addBtn" class="btn btn-primary"><i class="fa fa-plus"></i> 新增</button>
		    <button type="button" id="exportBtn" class="btn btn-primary"><i class="fa fa-print"></i> 导出</button>
		 </div>
    </form>
    <!-- 表格 -->
    <div id="rebateTableDiv">
	    <table class="table table-condensed table-hover" id="rebateTable">
	   		<thead>
	   			<tr>
	   				<th>名称</th>
	   				<th>折扣</th>
	   				<th>开始时间</th>
	   				<th>结束时间</th>
	   				<th>添加时间</th>
	   				<th>状态</th>
	   				<th>操作</th>
	   			</tr>
	   		</thead>
		   	<tbody id="rebateBody"></tbody>
		</table>
    </div>
     <!-- 分页条 -->
    <%@include file="../common/global/pagingtoolbar.jsp" %>
   
  </div>
   <div class="container hide" id="editRebateDiv">
    	<form role="form" class="form-horizontal" id="rebateForm">
    	<div class="form-group">
	    	<label class="col-sm-2 control-label"><span style="color: red;">*</span>优惠方案名称：</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="rebateDesc" name="rebateDesc" placeholder="优惠方案名称" maxlength="20"/>
							<input type="hidden" value="0" id="rebateId"/>
						</div>
			<label class="col-sm-2 control-label"><span style="color: red;">*</span>折扣：</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="rebate" name="rebate" placeholder="保留小数点后2位" maxlength="10"/>
					</div>			
		</div>
		<div class="form-group">
	    	<label class="col-sm-2 control-label"><span style="color: red;">*</span>优惠开始时间：</label>
						<div class="col-sm-4">
							<div class='input-group date' id='startTimeDate'>
								<input id="startTime" name="startTime" type='text' class="form-control" value='' />
								<span class="input-group-addon">
									<span class="glyphicon glyphicon-calendar"></span> 
								</span>
							</div>
						</div>
			<label class="col-sm-2 control-label"><span style="color: red;">*</span>优惠结束时间：</label>
						<div class="col-sm-4">
							<div class='input-group date' id='endTimeDate'>
								<input id="endTime" name="endTime" type='text' class="form-control" value='' />
								<span class="input-group-addon">
									<span class="glyphicon glyphicon-calendar"></span> 
								</span>
							</div>
						</div>			
		</div>
	<div class="form-group">
		<div class="col-sm-offset-2 col-sm-4">
			<button id="saveBtn" type="button" class="btn btn-warning btn-sm">保存</button>
			<button id="backBtn" type="button" class="btn btn-warning btn-sm">返回</button>
		</div>
	</div>
</form>
    </div>
  <script src="res/js/sys/rebateList.js" type="text/javascript"></script>
</body>
</html>

