<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>计费模型</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container" id="chargeRuleListDiv">
    <form id="conditionFrom" class="form-inline" role="form" action="device/pileModel_queryChargeRule.action">
	    <div class="form-group">
			<input id="keyword" name="keyword" type="text" class="form-control" placeholder="请输入计费模型名称" style="width: 190px;"/>
		     <input id="isExport" name="isExport" value="true" type="hidden">
			 <input id="fileName" name="fileName" value="计费模型" type="hidden">
		</div>
		 <div class="form-group pull-right">
			<button type="button" id="queryBtn" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
		    <button type="button" id="addBtn" class="btn btn-primary"><i class="fa fa-plus"></i> 新增</button>
		    <button type="button" id="resetBtn" class="btn btn-primary"><i class="fa fa-repeat"></i> 重置</button>
		    <button type="button" id="exportBtn" class="btn btn-primary"><i class="fa fa-print"></i> 导出</button>
		 </div>
    </form>
    <!-- 表格 -->
    <div id="chargeRuleTableDiv">
	    <table class="table table-condensed table-hover" id="chargeRuleTable">
	   		<thead>
	   			<tr>
	   				<th>名称</th>
	   				<th>尖费用</th>
	   				<th>峰费用</th>
	   				<th>平费用</th>
	   				<th>谷费用</th>
	   				<th>备注</th>
	   				<th>添加时间</th>
	   				<th>操作</th>
	   			</tr>
	   		</thead>
		   	<tbody id="chargeRuleBody"></tbody>
		</table>
    </div>
     <!-- 分页条 -->
    <%@include file="../common/global/pagingtoolbar.jsp" %>
   
  </div>
   <div class="container hide" id="editChargeRuleDiv">
    	<form role="form" class="form-horizontal" id="chargeRuleForm">
    	<div class="form-group">
	    	<label class="col-sm-2 control-label"><span style="color: red;">*</span>计费模型名称：</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="name" name="name" placeholder="计费模型名称" maxlength="20"/>
							<input type="hidden" value="0" id="chargeRuleId"/>
						</div>
			<label class="col-sm-2 control-label"><span style="color: red;">*</span>尖费用：</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="jianFee" name="jianFee" placeholder="保留小数点后2位" maxlength="10"/>
					</div>			
		</div>
		<div class="form-group">
	    	<label class="col-sm-2 control-label"><span style="color: red;">*</span>峰费用：</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="fengFee" name="fengFee" placeholder="保留小数点后2位" maxlength="10"/>
						</div>
			<label class="col-sm-2 control-label"><span style="color: red;">*</span>平费用：</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="pingFee" name="pingFee" placeholder="保留小数点后2位" maxlength="10"/>
					</div>			
		</div>
		<div class="form-group">
	    	<label class="col-sm-2 control-label"><span style="color: red;">*</span>谷费用：</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="guFee" name="guFee" placeholder="保留小数点后2位" maxlength="10"/>
						</div>
			<label class="col-sm-2 control-label"><span style="color: red;">*</span>备注：</label>
					<div class="col-sm-4">
					<textarea rows="3"  class="form-control" id="remark" name="remark" placeholder="备注。。。" maxlength="20"/></textarea>
					</div>			
		</div>
		<div class="form-group">
	<div class="col-sm-offset-2 col-sm-4">
	<button id="saveBtn" type="button" disabled="disabled" class="btn btn-warning btn-sm">保存</button>
	<button id="backBtn" type="button" class="btn btn-warning btn-sm">返回</button>
	</div>
	</div>
</form>
    </div>
  <script src="res/js/device/chargeRuleList.js" type="text/javascript"></script>
</body>
</html>

