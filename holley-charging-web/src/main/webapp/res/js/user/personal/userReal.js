/**
 * 个人资料
 */
function cheackInfo(info){
	if(isEmpty(info.realName)){
		return "请填写姓名！！";
	}else if(isEmpty(info.cardNum)){
		return "请填写身份证号！！";
	}else if(isEmpty($("#addCarImg").val())){
		return "请上传身份证照片！！";
	}else if(!regBox.regCn.test(info.realName)){
		return "请输入中文名！！";
	}else if(!(regBox.regCard15.test(info.cardNum) || regBox.regCard18.test(info.cardNum))){
		return "请输入15位或18位身份证号！！";
	}else{
		return "success";
	}
}
$(function(){
	commomChangeImg("addCarImg","showCarImg");
	var param={};
	var doUserRealUrl = "userjson/personal_doUserReal.action";
	$("#saveUserInfoBtn").on("click",function(){
		userRealInfo = getFormJson($("#userRealForm"));
		msg = cheackInfo(userRealInfo);
		if("success" == msg){
			param.userRealInfo = JSON.stringify(userRealInfo);
			$("#userRealForm").ajaxSubmit(
					{
						url : doUserRealUrl,
						type : 'post',
						dataType : 'json',
						data : param,
						beforeSubmit:function(){$("#loading").removeClass("hide")},
						success : function(data) {
													$("#loading").addClass("hide");
											    	if(data.userLoginStatus){
											    		  checkLoginStatus(data,false);
											    		  return;
											    	  }
													if("success" == data.message){
														var opt={};
														opt.onOk = function(){href("userAdmin/initUserInfo.action");}
														opt.onClose = opt.onOk;
														showWarning("提交成功！！",opt)
													}else{
														showWarning(data.message)
													}
													}
					})
		}else{
			showWarning(msg);
		}

	});	
	$("#goBackBtn").on("click",function(){
		href("userAdmin/initUserInfo.action");
	});
})

