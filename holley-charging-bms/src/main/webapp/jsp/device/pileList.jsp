<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>充电桩信息</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
 	<div>
	    <form id="conditionFrom" class="form-inline" role="form" action="device/pile_queryPileList.action">
		    <div class="form-group">
				<input id="keyword" name="keyword" type="text" class="form-control" placeholder="请输入点名称/桩名称/桩编号/通讯地址" style="width: 270px;"/>
				<select id="pileType" name="piletype" class="form-control">
			     	<option value="0">电桩类型</option>
			     	<s:iterator value="#request.pileTypeList" var="item" status="st">
				     	<option value="${item.value}">${item.text}</option>
			     	</s:iterator>
			     </select>
			     <select id="chaWay" name="chaway" class="form-control">
			     	<option value="0">充电方式</option>
			     	<s:iterator value="#request.chaWayList" var="item" status="st">
				     	<option value="${item.value}">${item.text}</option>
			     	</s:iterator>
			     </select>
			     <select id="intfType" name="intftype" class="form-control">
			     	<option value="0">接口类型</option>
			     	<s:iterator value="#request.intfTypeList" var="item" status="st">
				     	<option value="${item.value}">${item.text}</option>
			     	</s:iterator>
			     </select>
			     <select id="payWay" name="payway" class="form-control">
			     	<option value="-1">支付方式</option>
			     	<s:iterator value="#request.payWayList" var="item" status="st">
				     	<option value="${item.value}">${item.text}</option>
			     	</s:iterator>
			     </select>
			     <select id="comType" name="comtype" class="form-control">
			     	<option value="0">通讯协议</option>
			     	<s:iterator value="#request.comTypeList" var="item" status="st">
				     	<option value="${item.value}">${item.name}</option>
			     	</s:iterator>
			     </select>
			     <select id="pileStatus" name="pilestatus" class="form-control">
			     	<option value="0">状态</option>
			     	<s:iterator value="#request.statusList" var="item" status="st">
				     	<option value="${item.value}">${item.text}</option>
			     	</s:iterator>
			     </select>
			     <input id="pileIsExport" name="isExport" value="true" type="hidden">
			     <input id="pileFileName" name="fileName" value="充电桩列表" type="hidden">
			</div>
			<div class="form-group pull-right">
				<button type="button" id="pileQueryBtn" class="btn btn-primary">查询</button>
				<button type="button" id="pileExportBtn" class="btn btn-primary">导出</button>
			</div>
	    </form>
	    <!-- 表格 -->
	    <div id="pileTableDiv">
		    <table class="table table-condensed table-hover" id="pileTable">
		   		<thead>
		   			<tr>
		   				<th>ID</th>
		   				<th>桩编号</th>
		   				<th>桩名称</th>
		   				<th>所属充电点</th>
		   				<th>电桩类型</th>
		   				<th>充电方式</th>
		   				<th>接口类型</th>
		   				<th>支付方式</th>
		   				<th>通讯协议</th>
		   				<th>通讯地址</th>
		   				<th>是否可约</th>
		   				<th>状态</th>
		   				<th>更新时间</th>
		   				<th>操作</th>
		   			</tr>
		   		</thead>
			   	<tbody></tbody>
			</table>
	    </div>
	    <!-- 分页条 -->
	    <%@include file="../common/global/pagingtoolbar.jsp" %>
 	</div>
</div>
  <script src="res/js/device/pileList.js" type="text/javascript"></script>
</body>
</html>

