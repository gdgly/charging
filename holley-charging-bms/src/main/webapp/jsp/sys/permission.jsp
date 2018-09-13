<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>权限分配</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
  <link rel="stylesheet" type="text/css" href="res/jquery-easyui/easyui.css">
  <link rel="stylesheet" type="text/css" href="res/jquery-easyui/icon.css">
  <script type="text/javascript" src="res/jquery-easyui/jquery.easyui.min.js"></script>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
  	<div id="test" class="easyui-panel" style="padding:5px">
		<ul id="permissiontree" class="easyui-tree" style="min-height: 100px;"></ul>
	</div>
	<div class="form-group" style="margin-top: 10px;">
      <div class="col-sm-12" style="text-align: right;">
         <button id="saveBtn" type="button" class="btn btn-primary" onclick="save()">保存</button>
         <button id="cancelBtn" type="button" class="btn btn-primary" onclick="gobackAndReload()">取消</button>
      </div>
  	</div>
  </div>
  
  <script type="text/javascript">
  	var roleid = "<s:property value='#request.roleid'/>";
  </script>
  <script src="res/js/sys/permission.js" type="text/javascript"></script>
</body>
</html>

