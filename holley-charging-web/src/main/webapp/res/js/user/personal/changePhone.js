/**
 * 信息验证
 * 
 */
$(function(){
	var param={};
	var countdown=60; 
	var doChangePhoneUrl = "userjson/personal_doChangePhone.action";//修改手机接口
	var getPhoneCodeUrl = "userjson/personal_phoneCodeForChangePhone.action";//获取短信验证码接口
	var hrefUrl="userAdmin/initUserInfo.action";
	$("#phone").removeAttr("disabled").val("");//初始化参数
	$("#phoneCodeBtn").removeAttr("disabled");//初始化发送邮箱验证码
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
	    settime(obj) }
	    ,1000) 
	}
	$("#changePhoneBtn").on("click",function(){
		param.phone = $("#phone").val();
		param.phoneCode = $("#phoneCode").val();
		param.actionType = $("#actionType").val();
		$.ajax({  
		      url:doChangePhoneUrl,// 跳转到 action  
		      data:param,  
		      type:'post',  
		      cache:false,  
		      dataType:'json',  
		      beforeSend:function(){$("#loading").removeClass("hide");},
		      success:function(data){
					if(data.message == "success"){
						var opt={};
						opt.onOk = function(){
							href(hrefUrl);	
						}
						opt.onClose = opt.onOk;
						showSuccess("修改手机成功！！", opt);
					}
					else if(data.message == "error"){
						var opt={};
						opt.onOk = function(){
							href(hrefUrl);	
						}
						opt.onClose = opt.onOk;
						showError("修改手机失败，请重新修改！！", opt);
					}
					else{
						var opt={};
						opt.onOk = function(){
							$("#phoneCode").val("").focus();
						}
						opt.onClose = opt.onOk;
						showWarning(data.message, opt);
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
	//获取手机动态验证码
	$("#phoneCodeBtn").on("click",function(){
	 	param.phone = $("#phone").val();
		if(!param.phone && param.phone.length < 1){
			showWarning("请填写手机号码！！");
		}
		else if(!regBox.regMobile.test(param.phone)){
			showWarning("请正确填写手机号码！！");
		}
		else{
	 	$.post(getPhoneCodeUrl,param,function(data){
	 		if("success" != data.message){
	 			showWarning(data.message);
	 		}
	 		else{
	 			$("#phone").attr("disabled",true);
				 settime($("#phoneCodeBtn"));
	 		}
	 		
		}); 
	}
	});
})

