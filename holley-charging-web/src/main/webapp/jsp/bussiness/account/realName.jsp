<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<%@include file="../../common/global/meta.jsp"%>
<%@include file="../../common/global/js.jsp"%>
<%@include file="../head.jsp"%>
<html lang="zh-CN">
<head>
<style type="text/css">
</style>
</head>
<body>
	<div class="bg">
		<!--main-content-->
		<div class="container main-body" style="height: 60%;">
			<h3>实名认证</h3>
			<hr />
			<div class="col-sm-1">
				<img src="${imgUrl}res/img/bussiness/unionpay.png" />
			</div>
			<div class="col-sm-5">
				<h4>企业银行卡信息认证(2-3个工作日)</h4>
				<p class="help-block">1.点击“立即认证”</p>
				<p class="help-block">2.输入“企业对公账号”</p>
				<p class="help-block">3.平台向对公账号打款（1-2个工作日）</p>
				<p class="help-block">4.登录实名认证中心，输入打款金额</p>
				<p class="help-block">5.金额准确，认证完成</p>
			</div>
			<div class="col-sm-2">
				<button id="realNameBtn" class="btn btn-warning btn-sm">立即认证</button>
				<!-- <a href="bussiness/realNameInfo.action">立即认证</a> -->
			</div>
			<div class="col-sm-4">
				<div class="col-sm-12">
					<img
						src="${imgUrl}res/img/bussiness/information.png" />
					<p class="help-block">第一步 填写基本资料</p>
				</div>
				<div class="col-sm-offset-5">
					<img
						src="${imgUrl}res/img/bussiness/safe.png" />
					<p class="help-block">第二步 填写基本资料</p>
				</div>
				<div class="col-sm-12">
					<img
						src="${imgUrl}res/img/bussiness/identification.png" />
					<p class="help-block">第三步 填写基本资料</p>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		var url = "bussiness/realNameInfo.action";
		$("#realNameBtn").on("click", function() {
			href(url);
		});
	})
</script>
</html>

