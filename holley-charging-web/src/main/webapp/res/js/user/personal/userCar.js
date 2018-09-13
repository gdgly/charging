/**
 * 个人资料
 */
$(function(){
	initCarBrand($("#brand"),$("#model"));
	commomChangeImg("addCarImg","showCarImg");
	var param={};
	var editUserInfoUrl = "userjson/personal_editUserCar.action";
	$("#saveUserInfoBtn").on("click",function(){
		userInfo = formDataToJsonString($("#userCarForm"));
		param.userInfo = userInfo;
		$("#userCarForm").ajaxSubmit(
				{
					url : editUserInfoUrl,
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
					      	  	opt={};
								opt.onOk = function(){reload();}
								opt.onClose = opt.onOk;
								showWarning("修改成功！！",opt);  
					    	  }else{
					    		  showWarning(data.message);  
					    	  }	
					}
												
				})
	});
	$("#plateNo").on("keyup",function(){
		$("#saveUserInfoBtn").attr("disabled",false);
	});
	$("#vin").on("keyup",function(){
		$("#saveUserInfoBtn").attr("disabled",false);
	});
	$("#addCarImg").on("change",function(){
		$("#saveUserInfoBtn").attr("disabled",false);
	});
	$("#brand").on("change",function(){
		$("#saveUserInfoBtn").attr("disabled",false);
	});
	$("#model").on("change",function(){
		$("#saveUserInfoBtn").attr("disabled",false);
	});
})

