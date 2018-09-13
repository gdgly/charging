/**
 * 个人资料
 */

$(function(){

	goUserMenu("userInfo");
	initOneBootStrapDate($('#datepicker'));
	initArea($("#province"),$("#city"));//初始化省市
	commomChangeImg("addHeadImg","showHeadImg");
	var param={};
	var editUserInfoUrl = "userjson/personal_editUserInfo.action";
	var editUserHeadImgUrl = "userjson/personal_editUserHeadImg.action";
	var editUsernameUrl = "userjson/personal_editUsername.action";
	//安全设置
	realSafeLevel = $("#realSafeLevel").val();
	if(realSafeLevel <= 2){
		$("#safeLevel").css("width","30%").addClass("progress-bar-danger");
		$("#warn").text("低").css("color","red");;
	}
	else if(realSafeLevel == 3){
		$("#safeLevel").css("width","60%").addClass("progress-bar-warning");
		$("#warn").text("中").css("color","orange");;
	}
	else if(realSafeLevel >= 4){
		$("#safeLevel").css("width","100%").addClass("progress-bar-success");
		$("#warn").text("高").css("color","green");;
	}
	$("#goRealName").on("click",function(){
		href("userAdmin/initUserReal.action");
	});
	
	//基本资料
	$("#saveUserInfoBtn").on("click",function(){
		userInfo = formDataToJsonString($("#userInfoForm"));
		param.userInfo = userInfo;
		$.ajax({  
		      url:editUserInfoUrl,
		      data:param, 
		      type:'post',
		      cache:false,  
		      dataType:'json', 
		      beforeSend:function(){$("#loading").removeClass("hide");},
		      success:function(data){
		    	  if(data.userLoginStatus){
		    		  checkLoginStatus(data,false);
		    		  return;
		    	  }
		    	  if("success" == data.message){
		      	  	opt={};
					opt.onOk = function(){reload();}
					opt.onClose = opt.onOk;
					showWarning("修改成功！！",opt);
		    	  }else{
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
	});
$("#showHeadImg").on("click",function(){
	$("#addHeadImg").click();
});
//修改昵称
$("#username").on('keyup',function(){
	$("#editUsernameBtn").attr("disabled",false);
});
$("#editUsernameBtn").on("click",function(){
	param.username = $("#username").val();
	if(!param.username || param.username.trim() == ""){
		showWarning("请输入昵称！！");
		return;
	}
	$.ajax({  
	      url:editUsernameUrl,
	      data:param, 
	      type:'post',
	      cache:false,  
	      dataType:'json', 
	      beforeSend:function(){$("#loading").removeClass("hide");},
	      success:function(data){
	    	  if("success" == data.message){
	    		  $("#editUsernameBtn").attr("disabled",true);
	    		  getParentElementById("username").text(data.username);
	    		  getParentElementById("username2").text(data.username);
	    	  }else{
	    		  	opt={};
					opt.onOk = function(){reload();}
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
	})
});
//修改昵称
//按钮
$("#sign").on("keyup",function(){
	$("#saveUserInfoBtn").attr("disabled",false);
});
$("#qq").on("keyup",function(){
	$("#saveUserInfoBtn").attr("disabled",false);
});
$("#province").on("change",function(){
	$("#saveUserInfoBtn").attr("disabled",false);
});
$("#city").on("change",function(){
	$("#saveUserInfoBtn").attr("disabled",false);
});
$("input[name=sex]").on("change",function(){
	$("#saveUserInfoBtn").attr("disabled",false);
});
//按钮
$("#addHeadImg").on("change",function(){
	if($(this).val()){
		$('#userInfoForm').ajaxSubmit({
			url:editUserHeadImgUrl,
			type:'post',
			dataType:'json',
			data:param,
			beforeSubmit:function(){$("#loading").removeClass("hide")},
			success:function(data){
				$("#loading").addClass("hide");
				 if(data.userLoginStatus){
		    		  checkLoginStatus(data,true);
		    		  return;
		    	  }
				 if("success" == data.message){
					 getParentElementById("userHeadImg2").attr("src",IMG_SRC+data.headImgUrl);
					 getParentElementById("userHeadImg1").attr("src",IMG_SRC+data.headImgUrl);
					 getParentElementById("userHeadImg3").attr("src",IMG_SRC+data.headImgUrl);;
				 }else{
					 showWarning(data.message)
				 }
			}
		});  
	}
	
});

})

