/**
 * 信息验证
 * 
 */
$(function(){
	var param = {};
	var countdown = 60;
	var doChangeEamilUrl = "userjson/personal_doChangeEamil.action";//修改邮箱接口
	var getEmailCodeUrl = "userjson/personal_emailCode.action";//获取邮箱验证码接口
	var hrefUrl = "userAdmin/initUserInfo.action";
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

