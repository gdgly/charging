<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>意见反馈</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
  <!-- ----------------------------------个人桩代管申请列表 ------------------------------>
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
			<input id="keyword" type="text" class="form-control" placeholder="真实姓名/联系方式/申请人昵称/申请人手机" style="width: 300px;"/>
			<select id="validStatus" name="validStatus" class="form-control">
		     	<option value="0">审核状态</option>
		     	<s:iterator value="#request.statusList" var="item" status="st">
		     		<s:if test="#request.dealStatus == #item.value">
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
    <table class="table table-condensed table-hover" id="applyTable">
   		<thead>
   			<tr>
   				<th>ID</th>
   				<th>真实姓名</th>
   				<th>联系方式</th>
   				<th>电桩地址</th>
   				<th>备注</th>
   				<th>申请人昵称</th>
   				<th>申请人手机</th>
   				<th>申请时间</th>
   				<th>审核状态</th>
   				<th>操作</th>
   			</tr>
   		</thead>
	   	<tbody></tbody>
	</table>
     <!-- 分页条 -->
    <%@include file="../common/global/pagingtoolbar.jsp" %>
  </div>
  <script src="res/js/person/userPileApplyList.js" type="text/javascript"></script>
</body>
</html>

