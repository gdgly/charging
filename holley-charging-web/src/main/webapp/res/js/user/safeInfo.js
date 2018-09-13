var realName = "realName";
var realNameUrl = "userjson/personal_userRealName.action";
var changePwd = "changePwd";
var changePwdUrl = "userjson/personal_userChangePwd.action";
var changePhone = "changePhone";
var changePhoneUrl = "userjson/personal_userChangePhone.action";
var changeEmail = "changeEmail";
var changeEmailUrl = "userjson/personal_userChangeEmail.action";
var param={};
//提交表单
function submit(actionUrl){
	//param.jsonBean = formDataToJsonString($("#userHomeForm"));
	$("#userHomeForm").ajaxSubmit(
			{
				url : actionUrl,
				type : 'post',
				dataType : 'json',
				beforeSubmit:function(){$("#loading").removeClass("hide")},
				success : function(data) {
					$("#loading").addClass("hide");
			    	  if(data.userLoginStatus){
			    		  checkLoginStatus(data,false);
			    		  return;
			    	  }
					if("success" == data.message){
						opt={};
						opt.onOk = function(){$('#userHomeModal').modal('hide');}
						opt.onClose = opt.onOk;
						showWarning("修改成功！！",opt);
					}else{
						showWarning(data.message)
					}
				}
			});
}

function showModal(){
	$("#userHomeModal").modal().css({
		'margin-top': function () {
			return "5%";
		}
	});	
}

function addHtml(obj,type,title,name,placeholder,btnName){
	str="";
	if(btnName){
		str = "<div class='form-group'>"
	    	+"<label for='money' class='control-label col-sm-3'>"
	    	+title
	    	+"</label><div class='col-sm-6'>"
	    	+"<input class='form-control' id='"+name+"'"+"type='"+type+"' name='"+name+"' placeholder='"+placeholder+"'>"
			+"</input></div>"
			+"<div class='col-sm-3'>"
			+"<button type='button' class='btn btn-primary'>"+btnName+"</button>"
			+"</div>"
			+"</div>";
	}else{
		str = "<div class='form-group'>"
	    	+"<label for='money' class='control-label col-sm-3'>"
	    	+title
	    	+"</label><div class='col-sm-9'>"
	    	+"<input class='form-control' id='"+name+"'"+"type='"+type+"' name='"+name+"' placeholder='"+placeholder+"'>"
			+"</input></div>"
			+"</div>";
	}
	
	obj.append(str);
}

function checkForm(formType){
	return "success";
}
$("#realNameBtn").on("click",function(){
	href("userAdmin/initUserReal.action");
/*	$("#formType").val(realName);
	$("#userHomeModelTitle").text("实名认证");
	$("#userHomeBody").empty();
	addHtml($("#userHomeBody"),"真实姓名","realName","请填写真实姓名");
	showModal();*/
});
$("#changePwdBtn").on("click",function(){
	$("#formType").val(changePwd);
	$("#userHomeModelTitle").text("修改密码");
	$("#userHomeBody").empty();
	addHtml($("#userHomeBody"),"password","原密码","oldPwd","请填写原密码");
	addHtml($("#userHomeBody"),"password","新密码","newPwd","请填写新密码");
	addHtml($("#userHomeBody"),"password","新密码确认","checkNewPwd","请再次填写新密码");
	showModal();
});
$("#changePhoneBtn").on("click",function(){
	$("#formType").val(changePhone);
	$("#userHomeModelTitle").text("修改手机");
	$("#userHomeBody").empty();
	addHtml($("#userHomeBody"),"text","真实姓名","realName","请填写真实姓名");
	showModal();
});
$("#changeEmailBtn").on("click",function(){
	$("#formType").val(changeEmail);
	$("#userHomeModelTitle").text("修改邮箱");
	$("#userHomeBody").empty();
	addHtml($("#userHomeBody"),"text","邮箱","email","请填写邮箱地址");
	addHtml($("#userHomeBody"),"text","验证码","emailCode","请填写4位邮箱验证码","获取验证码");
	showModal();
});

$("#saveBtn").on("click",function(){
	formType = $("#formType").val();
	if(realName == formType){
		submit(realNameUrl);
	}else if(changePwd == formType){
		submit(changePwdUrl);
	}else if(changePhone == formType){
		submit(changePhoneUrl);
	}else if(changeEmail == formType){
		submit(changeEmailUrl);
	}else{
		showWarning("error！！");
	}
});