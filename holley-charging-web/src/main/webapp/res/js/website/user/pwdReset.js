//发送短信验证
function sendPhoneCode(obj){
    if(ismobile()){
    	 $("#phonecodeTip").addClass("hidden");
        var mobile = $.trim($("#mobile").val());
        $.ajax({
            type: "post",
            url: 'user/pwdreset_sendPhoneCode.action',
            data: {mobile:mobile,usertype:usertype,tm:new Date().getTime()},
            dataType:'json',
            cache: false,
            success: function(data,options){
                if(data.success){
//                    alert(data.message);
                    settime(obj);
                }else{
                	 $("#phonecodeTip").html(data.message);
            		 $("#phonecodeTip").removeClass("hidden");
                }
            }
        });
    }
}

//todo 重新刷新页面让倒计时不重新计算
var countdown=60;
function settime(obj) {
    if (countdown == 0) {
    	 $("#phonecodeTip").addClass("hidden");
        obj.removeAttribute("disabled");
        obj.value = "免费获取验证码";
        countdown = 60;
        return;
    } else {
    	obj.setAttribute("disabled", true);
	    obj.value = "重新发送(" + countdown + "秒后)";
        countdown--;
    }
    setTimeout(function () {
            settime(obj);
        }
        , 1000)
}

//验证手机号码
function ismobile(){
    var myreg = /^(((13[0-9]{1})|159)+\d{8})$/;
    if($("#mobile").val()==""){
        $("#mobile").next().removeClass("hidden");
        return false;
    }else if($("#mobile").val().length != 11 && !myreg.test($("#mobile").val())){
        $("#mobile").next().removeClass("hidden");
        return false;
    }else{
        $("#mobile").next().addClass("hidden");
        $("#mobile").val($.trim($("#mobile").val()));
    }
    return true;
} 

function isPhonecode(){
	if($("#phonecode").val()=="" || $("#phonecode").val().length != 4){
   	 	$("#phonecodeBtn").next().removeClass("hidden");
	  }else{
		  $("#phonecodeBtn").next().addClass("hidden");
	      $("#phonecode").val($.trim($("#phonecode").val()));
	  }
}

//验证密码
function ispassword(){
    var passreg = /^[a-zA-Z0-9]{6,16}$/;
    if($("#password").val()==""){
        $("#password").next().removeClass("hidden");
    }else if(!passreg.test($("#password").val())){
        $("#password").next().removeClass("hidden");
    }else{
        $("#password").next().addClass("hidden");
        $("#password").val($.trim($("#password").val()));
    }
}

//验证确认密码
function ischeckpassword(){
    $("#password").val($.trim($("#password").val()));
    if($("#checkpassword").val()==""){
        $("#checkpassword").next().removeClass("hidden");
    }else if($("#password").val()!=$("#checkpassword").val()){
        $("#checkpassword").next().removeClass("hidden");
    }else{
        $("#checkpassword").next().addClass("hidden");
        $("#checkpassword").val($.trim($("#checkpassword").val()));
    }
}

//验证注册表单信息
function verifyForm(){
    var myreg = /^(((13[0-9]{1})|159)+\d{8})$/;
    if($("#mobile").val()==""){
        $("#mobile").next().removeClass("hidden");
        return false;
    }else if($("#mobile").val().length != 11 && !myreg.test($("#mobile").val())){
        $("#mobile").next().removeClass("hidden");
        return false;
    }else{
    	$("#mobile").next().addClass("hidden");
    	$("#mobile").val($.trim($("#mobile").val()));
    }
    
    if($("#phonecode").val()=="" || $("#phonecode").val().length != 4){
    	 $("#phonecodeBtn").next().removeClass("hidden");
         return false;
	  }else{
		  $("#phonecodeBtn").next().addClass("hidden");
	      $("#phonecode").val($.trim($("#phonecode").val()));
	  }
    
    if($("#password").val()==""){
        $("#password").next().removeClass("hidden");
        return false;
    }else{
    	$("#password").next().addClass("hidden");
        $("#password").val($.trim($("#password").val()));
    }
    
    if($("#checkpassword").val()==""){
        $("#checkpassword").next().removeClass("hidden");
        return false;
    }else if($("#password").val()!=$("#checkpassword").val()){
        $("#checkpassword").next().removeClass("hidden");
        return false;
    }else{
    	 $("#checkpassword").next().addClass("hidden");
         $("#checkpassword").val($.trim($("#checkpassword").val()));
    }
    return true
}

var usertype= PERSION;
function userTypeChange(type){
	if(type == 'persion'){
		usertype = PERSION;
	}else if(type == 'enterprise'){
		usertype = ENTERPRISE;
	}
}

//松开键盘校验信息并更改提交按钮状态
function changeBtnStatus(){
	var myreg = /^(((13[0-9]{1})|159)+\d{8})$/;
	if($("#mobile").val()=="" || $("#mobile").val().length != 11 && !myreg.test($("#mobile").val()) ||
			$("#phonecode").val()=="" || $("#phonecode").val().length != 4 ||
			$("#password").val()=="" || 
			$("#checkpassword").val()=="" || $("#password").val()!=$("#checkpassword").val()){
		$("#cancelBtn").css('background-color','#0a95f2');
		$("#registerBtn").css('background-color','#cccccc');
	}else{
		$("#cancelBtn").css('background-color','#cccccc');
		$("#registerBtn").css('background-color','#0a95f2');
	}
}

//用户注册
function resetPwd(){
	if(!verifyForm()){
		return;
	}
    var mobile = $.trim($("#mobile").val());
    var password = $.trim($("#password").val());
    var phonecode = $.trim($("#phonecode").val());
    $.ajax({
        type: "POST",
        url: 'user/pwdreset_resetPwd.action',
        data: {usertype:usertype,mobile:mobile,password:password,phonecode:phonecode,tm:new Date().getTime()},
        dataType:'json',
        cache: false,
        success: function(data,options){
            if(data.success){
            	var option = {
            			title: "系统提示",
            			btn: window.wxc.xcConfirm.btnEnum.ok,
            			onOk: function(){
            				href("user/userlogin_init.action");
            			}
            		}
            	showSuccess("密码修改成功!",option);
            }else{
            	showInfo(data.message);
            }
        }
    });
}


//取消跳转到主页
function cancel(){
	href("home/homepage.action");
}


