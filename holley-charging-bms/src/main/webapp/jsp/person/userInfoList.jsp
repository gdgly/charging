<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>个人信息查询</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
    <form id="conditionFrom" class="form-inline" role="form" action="person/userInfo_queryList.action">
	    <div class="form-group">
			<input id="keyword" name="keyword" type="text" class="form-control" placeholder="请输入用户昵称/手机号码" style="width: 190px;"/>
			<input id="realinfo" name="realinfo" type="text" class="form-control" placeholder="请输入真实姓名/身份证号" style="width: 190px;"/>
		    <select id="province" name="province" class="form-control">
				<option value="0">请选择省份</option>
				<s:iterator value="#request.provinceList" status="status" id="item">
					<option value="<s:property value='id'/>"><s:property value="name" /></option>
				</s:iterator>
			</select>
			<select id="city" name="city" class="form-control">
				<option value="0">请选择市区</option>
			</select>
		    <input id="isExport" name="isExport" value="true" type="hidden">
			<input id="fileName" name="fileName" value="个人信息列表" type="hidden">
		</div>
		 <div class="form-group pull-right">
			<button type="button" id="queryBtn" class="btn btn-primary">查询</button>
		    <button type="button" id="exportBtn" class="btn btn-primary">导出</button>
		 </div>
    </form>
    <!-- 表格 -->
    <div id="userInfoTableDiv">
	    <table class="table table-condensed table-hover" id="userInfoTable">
	   		<thead>
	   			<tr>
	   				<th>用户信息ID</th>
	   				<th>用户昵称</th>
	   				<th>手机号码</th>
	   				<th>真实姓名</th>
	   				<th>身份证号</th>
	   				<th>性别</th>
	   				<th>地区</th>
	   				<th>车牌号</th>
	   				<th>操作</th>
	   			</tr>
	   		</thead>
		   	<tbody></tbody>
		</table>
    </div>
     <!-- 分页条 -->
    <%@include file="../common/global/pagingtoolbar.jsp" %>
  </div>
  <script src="res/js/person/userInfoList.js" type="text/javascript"></script>
</body>
</html>

