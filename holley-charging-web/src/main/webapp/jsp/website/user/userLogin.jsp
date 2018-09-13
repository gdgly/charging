<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../../common/global/top.jsp" %>

<html lang="zh-CN">
<head>
  <title>登录</title>
  <%@include file="../../common/global/meta.jsp"%>
  <link rel="stylesheet" href="res/css/website/userLogin.css" >
<style type="text/css">
</style>
</head>
<body>
	<div class="container-fluid logincenter">
		<div class="navbar navbar-inverse logincenter-head">
			<div class="container">
				<div class="pull-right">
					<a href="home/homepage.action"><span class="glyphicon glyphicon-home"></span> 返回首页</a>
				</div>
			</div>
		</div>
		<div class="container">
			<div class="row"> 
				<div class="col-md-4 col-md-offset-8">
					<div class="login-page box-shadow">
						<!--nav-tabs-->
						 <ul class="nav nav-tabs" role="tablist">
					  		<li role="presentation" class="active">
						     	<a href="#" role="tab" data-toggle="tab" id="persionTab">个人登录</a>
					     	</li>
						    <li role="presentation">
						     	<a href="#" role="tab" data-toggle="tab" id="enterpriseTab">运营商登录</a>
					     	</li>
				        </ul>
				        <!--main-content-->
				        <div role="tabpanel">
					        <div style="padding-top: 15px;">
					        	<p class="login-tip" id="message"></p>
					        	<form class="form-horizontal" role="form" action="">
									<div class="form-group has-feedback">
										<label for="loginuser" class="sr-only">帐号</label>
										<div class="col-sm-12">
											<span class="glyphicon glyphicon-user form-control-feedback"></span>
											<input type="text" class="form-control input-lg" name="loginuser" id="loginuser"
											maxlength="20"	placeholder="手机号码 / 账户名">
										</div>
									</div>
									<div class="form-group has-feedback">
										<label for="password" class="sr-only">密码</label>
										<div class="col-sm-12">
											<span class="glyphicon glyphicon-lock form-control-feedback"></span>
											<input type="password" class="form-control input-lg" name="password"
												id="password" placeholder="密码" maxlength="30">
										</div>
									</div>
									<div class="form-group has-feedback">
										<label for="verifycode" class="sr-only">验证码</label>
										<div class="col-sm-12">
											<span class="glyphicon glyphicon-phone form-control-feedback"></span>
											<input class="form-control input-lg" style="width: 165px; float: left;"
												id="verifycode" name="verifycode" type="text" placeholder="验证码" maxlength="4"/>
											<img style="width: 90px; float: left; height: 46px; margin-left: 10px;"
												id="code" onclick="javascript:changeCode();" src="">
										</div>
									</div>
									<div class="form-group" style="margin-bottom: 0">
										<p class="col-sm-6 text-left">
											<a class="forgetpwd" href="javascript:;" onclick="forgetPwd()">忘记密码?</a>
										</p>
										<p class="col-sm-6 text-right">
											<a class="register" href="user/userregister_init.action">立即注册</a>
										</p>
									</div>
									<div class="form-group">
										<div class="col-sm-12">
											<button type="button" id="loginBtn" onclick="login();" class="btn login-btn btn-lg" style="width: 100%">登录</button>
										</div>
									</div>
								</form>
					        </div>
				        </div>
					</div>
				</div>
			</div>
		</div>
		<div class="authcenter-background">
			<img alt="" src="${imgUrl}res/img/home/login_bg.jpg" style="width:100%;height: 100%">
		</div>
	</div>
	<%@include file="../../common/global/js.jsp" %>
  <script src="res/js/website/user/userLogin.js" type="text/javascript"></script>
</body>
</html>

