$(document).ready(function(){
    changeCode();
    $("#persionTab").bind("click", selectPersionTab);
    $("#enterpriseTab").bind("click", selectEnterpriseTab);
    /*$("body").keydown(function() {
        if (event.keyCode == "13") {//keyCode=13是回车键
        	alert(13);
            $('#loginBtn').click();
        }
    });*/
    
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
		url:'user/userlogin_login.action',
		data:{usertype:usertype,loginuser:loginuser,password:password,verifycode:verifycode,tm:genTimestamp()},
		 dataType:'json',
         cache: false,
         success: function(data,options){
        	
             if(data.success){
            	 if(usertype == ENTERPRISE){
            		 href("monitor/monitorMap.action");
            	 }else if(usertype == PERSION){
            		 href("userAdmin/home.action");
            	 }
             }else{
            	 if("isLogin" == data.message){
            		 opt={};
 					 opt.onOk = function(){reload();}
 					 opt.onClose = opt.onOk;
            		 showWarning("有别的账号正在登录，请退出后重新登录！！", opt)
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
		 $("#message").text('请输入手机或邮箱帐号!');
	     $("#message").css('visibility','visible');
	     return false;
	 }
	/* else if(!((mobileReg.test(loginuser) && loginuser.length == 11) || mailReg.test(loginuser))){
		 $("#message").text('请输入正确的手机或邮箱帐号!');
	     $("#message").css('visibility','visible');
	     return false;
	 }*/
	 else{
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

var usertype= PERSION;
//var usertype= ENTERPRISE;
function selectPersionTab(){
	if(usertype != PERSION){
		usertype = PERSION;
		changeCode();
	}
}

function selectEnterpriseTab(){
	if(usertype != ENTERPRISE){
		usertype = ENTERPRISE;
		changeCode();
	}
}

function forgetPwd(){
	href("user/pwdreset_init.action?usertype="+usertype);
}
