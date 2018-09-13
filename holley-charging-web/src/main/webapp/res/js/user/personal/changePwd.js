/**
 * 信息验证
 * 
 */
$(function(){
	var param={};
	var doChangePwdUrl = "userjson/personal_doChangePwd.action";//修改密码接口
	var url="user/userlogin_init.action";
	$("#changePwdBtn").on("click",function(){
		param.oldPwd = $("#oldPwd").val();
		param.newPwd = $("#newPwd").val();
		param.checkNewPwd = $("#checkNewPwd").val();
		param.actionType = $("#actionType").val();
		if(param.newPwd != param.checkNewPwd){
			showWarning("密码重复输入不一致！！");
		}
		else{
			$.ajax({  
			      url:doChangePwdUrl,// 跳转到 action  
			      data:param, 
			      type:'post',  
			      cache:false,  
			      dataType:'json',  
			      beforeSend:function(){$("#loading").removeClass("hide");},
			      success:function(data){
						if("success"==data.message){
							//if(param.actionType == "changePwd"){
								var opt={};
								opt.onOk = function(){
									frameHref(url);
								}
								opt.onClose = opt.onOk;
								showSuccess("修改密码成功,请重新登录！！", opt);
							//}
						}
						else if("fail"==data.message || "timeOut"==data.message){
							var opt={};
							opt.onOk = function(){
								frameHref(url);
							}
							opt.onClose = opt.onOk;
							showWarning("修改密码超时，请重新修改！！", opt);
						}
						
						else{
							showWarning(data.message);
						}
						
					},  
			      complete:function(){
			    	  $("#loading").addClass("hide"); 
			      },
			      error : function() {  
			           showWarning("异常！");
			      }  
			 });
	}
	});
})

