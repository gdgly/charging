<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<% String basepath=request.getContextPath();
%>
<html lang="zh-CN">
<head>
  <title>注册</title>
  <%@include file="../common/global/meta.jsp"%>
    <link rel="stylesheet" href="res/css/frame/register.css" >
<style type="text/css">
</style>
</head>
<body style="background-color: #ecf4f7">
<!--main-->
	<div class="container">
		<div class="row">
			<div class="col-md-3"></div>
			<div class="col-md-6">
				<div style="margin-top:80px;margin-bottom: 60px;text-align: center;">
					<h1>找回密码</h1>
				</div>
				<div class="register-page box-shadow">
					  <div style="padding-top: 50px;">
						  <div class="regist-content">
						  	<form class="form-horizontal" id="registerForm" action="">
								<div class="form-group">
									<label for="mobile" class="col-sm-3 control-label">手机号码</label>
									<div class="col-sm-9">
										<input type="number" class="form-control" id="mobile" name="mobile" onblur="ismobile();" placeholder="请输入手机号码" onkeyup="changeBtnStatus()">
										<span class="hidden">请正确填写11位手机号</span>
									</div>
								</div>
						        <div class="form-group">
						            <label for="phonecode" class="col-sm-3 control-label">验证码</label>
						            <div class="col-sm-9">
						                <input class="form-control" style="width:130px;float:left;" id ="phonecode" name="phonecode" type="text" onblur="isPhonecode()" placeholder="请输入验证码" onkeyup="changeBtnStatus()"/>
						                <input type="button" class="btn btn-phonecode" onclick="sendPhoneCode(this);" value="获取验证码" id="phonecodeBtn"/>
						                <span id="phonecodeTip" class="hidden">手机验证码不正确,请重发</span>
						            </div>
						        </div>
						        <div class="form-group">
						            <label for="password" class="col-sm-3 control-label">新密码</label>
						            <div class="col-sm-9">
						                <input type="password" class="form-control" name="password" id="password" onblur="ispassword();" placeholder="请输入密码" onkeyup="changeBtnStatus()">
						                <span class="hidden">密码为6-16位数字字母组合</span>
						            </div>
						        </div>
						        <div class="form-group">
						            <label for="checkpassword" class="col-sm-3 control-label">再次确认</label>
						            <div class="col-sm-9">
						                <input type="password" class="form-control" name="checkpassword" id="checkpassword" onblur="ischeckpassword();" placeholder="请输入密码" onkeyup="changeBtnStatus()">
						                <span class="hidden">两次输入的密码不一致</span>
						            </div>
						        </div>
						        <div class="form-group">
						        </div>
								<div class="form-group">
						            <div class="col-sm-offset-3 col-sm-9">
						             	<button type="button" class="btn btn-blue btn-lg" id="cancelBtn" onclick="cancel()">取消</button>
						             	<span style="margin-left: 20px;margin-right: 20px;"></span>
						                <button type="button" class="btn btn-gray btn-lg" id="registerBtn" onclick="resetPwd();">提交</button>
						                <!-- 
							            <a href="<%=basepath%>/user/userlogin_init.action">已有账号，去登录</a>
							             -->
						            </div>
						        </div>
						        <div class="form-group">
						        </div>
							</form>
						  </div>
					  </div>
				</div>
			</div>
			<div class="col-md-3"></div>
		</div>
	</div>
  <%@include file="../common/global/js.jsp" %>
  <script src="res/js/frame/pwdReset.js" type="text/javascript"></script>
</body>
</html>

