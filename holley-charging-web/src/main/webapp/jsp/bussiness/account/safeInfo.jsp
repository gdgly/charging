<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<%@include file="../../common/global/meta.jsp"%>
<%@include file="../../common/global/js.jsp"%>
<%@include file="../head.jsp"%>
<html lang="zh-CN">
<head>
</head>
<body>
	<div class="bg">
		<!--main-content-->
		<div class="container main-body safeInfoSize">
			<h3>安全设置</h3>
			<hr />
			<form class="form-horizontal" role="form">
				<input type="hidden" id="realSafeLevel" value="${safeLevel}" />
				<div class="form-group">
					<label for="safeLevel" class="col-sm-3 control-label">账户安全：</label>
					<div class="col-sm-6" style="padding-top: 9px;">
						<div class="progress" style="width: 97%;">
							<div id="safeLevel" class="progress-bar" role="progressbar"
								aria-valuenow="60" aria-valuemin="0" aria-valuemax="100">
							</div>
						</div>
					</div>
					<div class="col-sm-3 text-center" style="padding-top: 9px;">
						安全级别：<span id="warn">低</span>
					</div>

				</div>
				<%-- <hr class="dashed"/>
   <div class="form-group">
      <label for="account" class="col-sm-2 control-label">登录账号：</label>
      <div class="col-sm-8" >
     	<p id="account" class="text-center">
     	  ${currentUser.username}（
      		<s:if test="#request.currentUser.realStatus == 1">
  			未认证，<a href="${webroot}/bussiness/realName.action">点击实名认证</a>）
			</s:if>
			<s:else>
			已认证）
			</s:else>
      	</p>
      </div>
   </div> --%>

				<hr class="dashed">
				<div class="form-group">
					<label for="password" class="col-sm-3 control-label">登录密码：</label>
					<div class="col-sm-6">
						<p class="help-block fontSize15">
							安全性能高的密码可以使账户更安全，建议您定期更换密码，设置一个包含字母、符号或数字长度超过6位的密码</p>
					</div>
					<div class="col-sm-3">
						<p id="password" class="text-center">
							<s:if test="#request.currentUser.password != null">
								<img
									src="${imgUrl}res/img/bussiness/steup.png"
									class="marginBottom3" />
								<span class="success">已设置</span> | <a
									href="bussiness/validChangePwd.action">修改</a>
							</s:if>
						</p>
					</div>
				</div>
				<%--     <hr class="dashed">
    <div class="form-group">
         <label for="payPassword" class="col-sm-3 control-label">交易密码：</label>
            <div class="col-sm-6">
       <p class="help-block fontSize15">
       	安全性能高的密码可以使账户更安全，建议您定期更换密码，设置一个包含字母、符号或数字中至少两项目长度超过6 位的密码。
       </p>
      </div>
      <div class="col-sm-3">
     	<p id="payPassword" class="text-center">
			<s:if test="#request.currentUser.payPassword != null">
				<img src="res/img/bussiness/steup.png" class="marginBottom3"/> <span class="success">已设置</span> | <a href="${webroot}/bussiness/validChangePayPwd.action">修改</a>
			</s:if>
      	</p>
      </div>
   </div> --%>
				<hr class="dashed">
				<div class="form-group">
					<label for="phone" class="col-sm-3 control-label">手机修改：</label>
					<div class="col-sm-6">
						<p class="help-block fontSize15">
							您已绑定了手机<span style="color: orange;">"${currentUser.phone}"</span>。[您的手机为安全手机，可以找回密码；为保障您的帐户安全，如果换绑手机，五天之内只能操作一次]
						</p>
					</div>
					<div class="col-sm-3">
						<p id="phone" class="text-center">
							<s:if test="#request.currentUser.phoneStatus == 2">
								<img
									src="${imgUrl}res/img/bussiness/steup.png"
									class="marginBottom3" />
								<span class="success">已设置</span> | <a
									href="bussiness/validChangePhone.action">修改</a>
							</s:if>
						</p>
					</div>
				</div>
				<hr class="dashed">
				<div class="form-group">
					<label for="email" class="col-sm-3 control-label">邮箱修改：</label>
					<div class="col-sm-6">
						<p class="help-block fontSize15">
							<s:if test="#request.currentUser.emailStatus == 2">
         						您已绑定了邮箱<span style="color: orange;">"${currentUser.email}"</span>[您的邮箱为安全邮箱，可以接收信息]
       						</s:if>
							<s:else>
             					 邮箱绑定后可用来接收各类系统、营销、服务通知。
       						</s:else>
						</p>
					</div>
					<div class="col-sm-3">
						<p id="email" class="text-center">
							<s:if test="#request.currentUser.emailStatus == 2">
								<img
									src="${imgUrl}res/img/bussiness/steup.png"
									class="marginBottom3">
								<span class="success">已设置</span> | <a
									href="bussiness/validChangeEmail.action">修改</a>
							</s:if>
							<s:else>
								<img
									src="${imgUrl}res/img/bussiness/no_setup.png"
									class="marginBottom3" />
								<span class="warn">未设置</span>  | <a
									href="bussiness/validChangeEmail.action">设置</a>
							</s:else>
						</p>
					</div>
				</div>
			</form>

		</div>
		<s:if test="#request.webUser.roleType == 2">
		<div class="container main-body"
			style="height: 210px; margin-top: 20px;">
			<h3>实名认证</h3>
			<hr />
			<div class="col-sm-3">
				<img src="${imgUrl}res/img/bussiness/unionpay.png" />
			</div>
			<div class="col-sm-5">
				银行卡认证 <img src="${imgUrl}res/img/bussiness/identification_sign.png" />
				<s:if test="#request.currentUser.realStatus == 2">
					<p class="help-block fontSize15">${busBussinessInfo.bankAccountDesc}</p>
				</s:if>
				<s:elseif test="#request.busBussinessReal !=null">
					<p class="help-block fontSize15">
						<s:if test="#request.busBussinessReal.validStatus == 1">
 							管理员审核中，请注意短信查收。。。
 						</s:if>
						<s:if test="#request.busBussinessReal.validStatus == 2">
							转账中，请注意短信查收。。。
 						</s:if>
						<s:if test="#request.busBussinessReal.validStatus == 3">
							请校验打款金额。。。
 						</s:if>
					</p>
				</s:elseif>
				<s:else>
					<p class="help-block fontSize15">未认证</p>
				</s:else>
			</div>
			<div class="col-sm-offset-1 col-sm-3 text-center">
				<s:if test="#request.busBussinessReal !=null">
					<s:if test="#request.busBussinessReal.validStatus == 3">
						<button class="btn btn-warning btn-sm" id="goValid">去验证</button>
					</s:if>
				</s:if>
				<s:elseif test="#request.currentUser.realStatus == 1">
					<button class="btn btn-warning btn-sm" id="goRealName">去认证</button>
				</s:elseif>
			</div>
		</div>
</s:if>

	</div>
	<script type="text/javascript">
	var roleType = '${webUser.roleType}'
		if(roleType && roleType == 4){
			setBowHeight1();
		}
	</script>
	<script src="res/js/bussiness/safeInfo.js" type="text/javascript"></script>
</body>
</html>

