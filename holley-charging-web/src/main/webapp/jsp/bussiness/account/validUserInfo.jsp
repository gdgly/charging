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
		<div class="container main-body" style="height: 45%;">
			<div class="row">
				<div class="col-sm-2">
					<h3>信息验证</h3>
				</div>
				<div class="col-sm-offset-8 col-sm-2 text-right"
					style="margin-top: 21px;">
					<button id="volidChangePwdBtn" type="button"
						class="btn btn-warning btn-sm">下一步</button>
				</div>
			</div>
			<hr />

			<p class="help-block">您正在使用手机验证码验证身份，请完成以下操作</p>
			<form class="form-horizontal" role="form">
				<div class="form-group">
					<label for="phone" class="col-sm-2 control-label">手机号码</label>
					<div class="col-sm-5">
					<p id="phone" class="pTop8">
						<s:if test="#request.currentUser.phone.length()==11">
						<s:property value="#request.currentUser.phone.substring(0,3)+'...'+#request.currentUser.phone.substring(#request.currentUser.phone.length()-3,#request.currentUser.phone.length())" />
						</s:if>
						<s:else>
						<s:property value="#request.currentUser.phone"/>
						</s:else>
					</p>
<%-- 						<input type="text" class="form-control" id="phone"
							disabled="disabled"
							placeholder="<s:if test="#request.currentUser.phone.length()==11">
<s:property value="#request.currentUser.phone.substring(0,3)+'...'+#request.currentUser.phone.substring(#request.currentUser.phone.length()-3,#request.currentUser.phone.length())" />
</s:if>
<s:else>
<s:property value="#request.currentUser.phone"/>
</s:else>" /> --%>
						<input type="hidden" id="actionType" name="actionType"
							value="${actionType}" />
					</div>
				</div>
				<div class="form-group">
					<label for="phoneCode" class="col-sm-2 control-label">验证码</label>
					<div class="col-sm-5">
						<input type="text" class="form-control" id="phoneCode"
							placeholder="请输入验证码" maxlength="4"/>
					</div>
					<div class="col-sm-2">
						<input type="button" class="btn btn-warning btn-sm" id="phoneCodeBtn"
							value="免费获取验证码" />
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
<script type="text/javascript">
setBowHeight1();
	$(function() {
		var param = {};
		var countdown = 60;
		var getPhoneCodeUrl = "bussiness_phoneCode.action";//获取验证码接口
		var doValidChangePwdUrl = "bussiness_doValidUserInfo.action";//修改密码验证用户接口
		var changePwdHref = "bussiness/changePwd.action";
		var changeEmailHref = "bussiness/changeEmail.action";
		var changePhoneHref = "bussiness/changePhone.action";
		var hrefUrl;
		$("#phoneCodeBtn").removeAttr("disabled");
		$("#phoneCode").val("");

		//计时方法
		function settime(obj) {
			if (countdown == 0) {
				obj.removeAttr("disabled");
				obj.val("免费获取验证码");
				countdown = 60;
				return;
			} else {
				obj.attr("disabled", true);
				ss = "重新发送(" + countdown + ")";
				obj.val(ss);
				countdown--;
			}
			setTimeout(function() {
				settime(obj)
			}, 1000)
		}
		//获取手机验证码	
		$("#phoneCodeBtn").on("click", function() {
			$.post(getPhoneCodeUrl, param, function(data) {
				if (data.message != "success") {
					showWarning(data.message);
				} else {
					settime($("#phoneCodeBtn"));
				}
			});
		});
		$("#volidChangePwdBtn").on(
				"click",
				function() {
					param.phoneCode = $("#phoneCode").val();
					param.actionType = $("#actionType").val();

					if (!param.phoneCode || param.phoneCode.length != 4) {
						showWarning("请输入4位验证码！！");
					}
					else{
					$.post(doValidChangePwdUrl, param, function(data) {
						if (data.message == "success") {
							if (param.actionType == "changePwd"
									|| param.actionType == "changePayPwd") {
								hrefUrl = changePwdHref + "?actionType="
										+ param.actionType;
								href(hrefUrl);
							} else if (param.actionType == "changeEmail") {
								hrefUrl = changeEmailHref + "?actionType="
										+ param.actionType;
								href(hrefUrl);
							} else if (param.actionType == "changePhone") {
								hrefUrl = changePhoneHref + "?actionType="
										+ param.actionType;
								href(hrefUrl);
							} else if ("error" == data.message) {
								var opt={};
								opt.onOk = function(){href("bussiness/safeInfo.action");}
								opt.onClose = opt.onOk;
								showWarning("验证失败请重新操作！！",opt);
							}
						} else {
						 	var opt={};
							opt.onOk = function(){$("#phoneCode").val("").focus()}
							opt.onClose = opt.onOk; 
							showWarning(data.message,opt);
						}
					});
					}
				});
	})
</script>
</html>

