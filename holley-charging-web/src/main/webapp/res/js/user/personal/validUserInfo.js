/**
 * 信息验证
 * 
 */
goUserMenu("userInfo");
$(function(){
	var param = {};
	var countdown = 60;
	var getPhoneCodeUrl = "userjson/personal_phoneCode.action";//获取验证码接口
	var doValidChangePwdUrl = "userjson/personal_doValidUserInfo.action";//修改密码验证用户接口
	var changePwdHref = "userAdmin/changePwd.action";//修改账户密码
	var changeEmailHref = "userAdmin/changeEmail.action";
	var changePhoneHref = "userAdmin/changePhone.action";
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
							opt.onOk = function(){href("userAdmin/initUserInfo.action");}
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

