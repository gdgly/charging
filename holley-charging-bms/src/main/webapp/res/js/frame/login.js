$(document).ready(function(){
    changeCode();
    
    document.onkeydown = function(e){
        var ev = document.all ? window.event : e;
        if(ev.keyCode==13) {
        	 $('#loginBtn').click();
         }
    }
});

function genTimestamp() {
    var time = new Date();
    return time.getTime();
}

function changeCode() {
    $("#code").attr("src", "user/changecode.action?t=" + genTimestamp());
}

function login(){
	if(!verifyLoginForm())return;
	var loginuser = $.trim($("#loginuser").val());
	var password = $.trim($("#password").val());
	var verifycode = $.trim($("#verifycode").val());
	
	$.ajax({
		type:"POST",
		url:'user/login_login.action',
		data:{loginuser:loginuser,password:password,verifycode:verifycode,tm:genTimestamp()},
		 dataType:'json',
         cache: false,
         success: function(data,options){
             if(data.success){
            	 if(data.homepage){
            		 href(data.homepage);
            	 }
             }else{
            	 if("isLogin" == data.message){
            		 opt={};
 					 opt.onOk = function(){reload();}
 					 opt.onClose = opt.onOk;
            		 showInfo("有别的账号正在登录，请退出后重新登录！！", opt) 
            	 }else{
            		 $("#message").text(data.message);
                     $("#message").css("visibility",'visible');
                     changeCode(); 
            	 }
//                 $("#password").focus();
             }
         }
     });
}

//验证信息
function verifyLoginForm(){
	//验证手机或邮箱帐号
	var mobileReg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
	 var mailReg = /^(?:[a-zA-Z0-9]+[_\-\+\.]?)*[a-zA-Z0-9]+@(?:([a-zA-Z0-9]+[_\-]?)*[a-zA-Z0-9]+\.)+([a-zA-Z]{2,})+$/;
	 var loginuser = $("#loginuser").val();
	 if(loginuser==""){
		 $("#message").text('请输入昵称或手机号码!');
	     $("#message").css('visibility','visible');
	     return false;
	 }else if(!((mobileReg.test(loginuser) && loginuser.length == 11) ||  loginuser.length <= 50)){//mailReg.test(loginuser) ||
		 $("#message").text('请输入正确的昵称或手机号码!');
	     $("#message").css('visibility','visible');
	     return false;
	 }else{
		 $("#message").css('visibility','hidden');
		 $("#user").val($.trim($("#loginuser").val()));
	 }
    
    if($("#password").val()==""){
   	 	 $("#message").text('请输入登录密码!');
	     $("#message").css('visibility','visible');
	     $("#password").focus();
       return false;
   }else{
       $("#password").val($.trim($("#password").val()));
   }
    
    var verifycode = $("#verifycode").val();
    if(verifycode=="" || verifycode.length != 4){
  	 	 $("#message").text('请输入正确的验证码!');
	     $("#message").css('visibility','visible');
	     $("#verifycode").focus();
      return false;
  }else{
      $("#verifycode").val($.trim($("#verifycode").val()));
  }
    return true
}

