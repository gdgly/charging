<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <%@include file="../common/global/meta.jsp"%>
<style type="text/css">
</style>
</head>
<body class="bodyBg">
<%--   <%@include file="../common/global/header.jsp"%> --%>
<!--main-->
<div class="main">
  <!--main-content-->
  <div class="container">

  <form class="form-horizontal" role="form">
   <div class="form-group">
      <label for="phone" class="col-sm-2 control-label">用户名：</label>
      <div class="col-sm-10">
           	<input type="text" class="form-control" id="phone" 
            placeholder="请输入用户名"/>
      </div>
   </div>
   <div class="form-group">
      <label for="password" class="col-sm-2 control-label">密 码：</label>
      <div class="col-sm-10">
         <input type="password" class="form-control" id="password" 
            onkeydown="if(event.keyCode==13){document.getElementById('dologin').click();return false;}" placeholder="请输入密码"/>
      </div>
   </div>

   <div class="form-group">
      <div class="col-sm-offset-2 col-sm-10">
         <button id="dologin" type="button" class="btn btn-success">登录</button>
      </div>
   </div>
</form>

  </div>
</div>

  <%@include file="../common/global/js.jsp" %>
  <%@include file="../common/global/footer.jsp"%>
</body>
<script type="text/javascript">
var url="/home/doLogin.action";
var href="/bussiness/safeInfo.action";
var param={};
var okmsg = "success";
$(function(){
	$("#dologin").on("click",function(){
		param.phone = $("#phone").val();
		param.password = $("#password").val();
		$.post(url,param,function(data){
			if(data.msg == okmsg){
				window.document.location.href=href;
			}
			else{
				alert(data.msg);
			}
			
		});
	});
});
</script>
</html>

