<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>用户管理</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
    <form id="conditionFrom" class="form-inline" role="form" action="device/pileModel_queryList.action">
	    <div class="form-group">
			<input id="keyword" name="keyword" type="text" class="form-control" placeholder="请输入品牌名称" style="width: 190px;"/>
			<select id="chaWay" name="chaway" class="form-control">
		     	<option value="0">充电方式</option>
		     	<s:iterator value="#request.currentTypeList" var="item" status="st">
		     		<s:if test="#request.chaWay == #item.value">
		     			<option value="${item.value}" selected="selected">${item.text}</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.value}">${item.text}</option>
		     		</s:else>
		     	</s:iterator>
		     </select>
		     <select id="chaType" name="chatype" class="form-control">
		     	<option value="0">充电类型</option>
		     	<s:iterator value="#request.powerTypeList" var="item" status="st">
		     		<s:if test="#request.chaType == #item.value">
		     			<option value="${item.value}" selected="selected">${item.name}</option>
		     		</s:if>
		     		<s:else>
		     			<option value="${item.value}">${item.name}</option>
		     		</s:else>
		     	</s:iterator>
		     </select>
		     <select id="intfType" name="intftype" class="form-control">
		     	<option value="0">接口标准</option>
		     	<s:iterator value="#request.intfTypeList" var="item" status="st">
		     		<s:if test="#request.intfType == #item.value">
		     			<option value="${item.value}" selected="selected">${item.text}</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.value}">${item.text}</option>
		     		</s:else>
		     	</s:iterator>
		     </select>
		     <select id="modelStatus" name="modelstatus" class="form-control">
		     	<option value="0">状态</option>
		     	<s:iterator value="#request.statusList" var="item" status="st">
		     		<s:if test="#request.pileStatus == #item.value">
		     			<option value="${item.value}" selected="selected">${item.text}</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.value}">${item.text}</option>
		     		</s:else>
		     	</s:iterator>
		     </select>
		     <input id="isExport" name="isExport" value="true" type="hidden">
			 <input id="fileName" name="fileName" value="电桩型号列表" type="hidden">
		</div>
		 <div class="form-group pull-right">
			<button type="button" id="queryBtn" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
		    <button type="button" id="addBtn" class="btn btn-primary"><i class="fa fa-plus"></i> 新增</button>
		    <button type="button" id="resetBtn" class="btn btn-primary"><i class="fa fa-repeat"></i> 重置</button>
		    <button type="button" id="exportBtn" class="btn btn-primary"><i class="fa fa-print"></i> 导出</button>
		 </div>
    </form>
    <!-- 表格 -->
    <div id="modelTableDiv">
	    <table class="table table-condensed table-hover" id="modelTable">
	   		<thead>
	   			<tr>
	   				<th>电桩型号ID</th>
	   				<th>品牌名称</th>
	   				<th>充电方式</th>
	   				<th>充电类型</th>
	   				<th>接口标准</th>
	   				<th>输出电压</th>
	   				<th>额定功率</th>
	   				<th>状态</th>
	   				<th>操作</th>
	   			</tr>
	   		</thead>
		   	<tbody></tbody>
		</table>
    </div>
     <!-- 分页条 -->
    <%@include file="../common/global/pagingtoolbar.jsp" %>
  </div>
  <script src="res/js/device/pileModelList.js" type="text/javascript"></script>
</body>
</html>

