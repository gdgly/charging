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
					<h3>邮箱修改</h3>
				</div>
				<div class="col-sm-offset-8 col-sm-2 text-right"
					style="margin-top: 21px;">
					<button id="changeEamilBtn" type="button" class="btn btn-warning btn-sm">确定</button>
				</div>
			</div>
			<hr>
			<form class="form-horizontal" role="form" id="changeEmail">
				<div class="form-group">
					<label for="oldPwd" class="col-sm-2 control-label">登录名</label>
					<div class="col-sm-5">
						<input type="text" class="form-control" id="username"
							value="${currentUser.userName}" disabled="disabled" /> <input
							type="hidden" id="actionType" name="actionType"
							value="${actionType}" />
					</div>
				</div>
				<div class="form-group">
					<label for="email" class="col-sm-2 control-label">邮箱</label>
					<div class="col-sm-5">
						<input type="text" class="form-control" id="email"
							placeholder="请输入邮箱" maxlength="30"/>
					</div>
					
				</div>
				<div class="form-group">
					<label for="emailCode" class="col-sm-2 control-label">验证码</label>
					<div class="col-sm-5">
						<input type="text" class="form-control" id="emailCode" maxlength="4"/>
					</div>
					<div class="col-sm-1">
						<input type="button" class="btn btn-warning btn-sm" id="emailCodeBtn"
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
		var doChangeEamilUrl = "bussiness_doChangeEamil.action";//修改邮箱接口
		var getEmailCodeUrl = "bussiness_emailCode.action";//获取邮箱验证码接口
		var hrefUrl = "bussiness/safeInfo.action";
		resetForm($("#changeEmail"));
		$("#email").removeAttr("disabled").val("");//初始化参数
		$("#emailCodeBtn").removeAttr("disabled");//初始化发送邮箱验证码
		$("#emailCode").val("");
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
		$("#changeEamilBtn").on("click", function() {
			param.email = $("#email").val();
			param.emailCode = $("#emailCode").val();
			param.actionType = $("#actionType").val();
			$.ajax({  
			      url:doChangeEamilUrl,// 跳转到 action  
			      data:param,  
			      type:'post',  
			      cache:false,  
			      dataType:'json',  
			      beforeSend:function(){$("#loading").removeClass("hide");},
			      success:function(data) {
						if (data.message == "success") {
							var opt={};
							opt.onOk = function(){href(hrefUrl);}
							opt.onClose = opt.onOk;
							showSuccess("修改邮箱成功！！",opt);
							
						} else if (data.message == "error") {
							var opt={};
							opt.onOk = function(){href(hrefUrl);}
							opt.onClose = opt.onOk;
							showWarning("修改邮箱失败，请重新修改！！",opt);
						} else {
							var opt={};
							opt.onOk = function(){$("#emailCode").val("").focus()}
							opt.onClose = opt.onOk;
							showWarning(data.message,opt);
						}

					},  
			      complete:function(){
			    	  $("#loading").addClass("hide"); 
			      },
			      error : function() {  
			           showWarning("异常！");
			      }  
			 });
		});
		//获取邮箱动态验证码
		$("#emailCodeBtn").on("click", function() {
			param.email = $("#email").val();
			if (!param.email) {
				showWarning("邮箱地址不能为空！！");
			}
			else if(!regBox.regEmail.test(param.email)){
				showWarning("请填写正确的邮箱地址！！");
			}
			else{
				$.post(getEmailCodeUrl, param, function(data) {
					if ("success" != data.message) {
						showWarning(data.message);
					}
					else{
						$("#email").attr("disabled", true);
						settime($("#emailCodeBtn"));
					}
				});
			}
			
		});

		//计时方法

	})
</script>
</html>

