/**
 * 开户
 */
var param={};
var newInfoId=0;
$(document).ready(function(){
	initParams();
	//queryUserInfoList();
});
function initParams(){
	//初始化省市
	initArea($("#province"), $("#city"));
	initCarBrand($("#brand"),$("#model"));
	//初始化查询按钮
	$("#saveBtn").on("click",function(){
		addUser();
	});
	$("#registerCardBtn").on("click",function(){
		if(newInfoId > 0){
			href("person/registerCard.action?infoId="+newInfoId);
		}else{
			showInfo("非法操作！！");
		}
		
	});
}
function checkFormData(userObj){
	msg = "success";
	if(!REGBOX.regCn.test(userObj.realName)){
		msg = "请输入正确的姓名！！";
	}else if(!REGBOX.regMobile.test(userObj.phone)){
		msg = "请输入正确的11位手机号码！！";
	}else if(userObj.province <= 0){
		msg = "请选择省份！！";
	}else if(userObj.city <= 0){
		msg = "请选择市区！！";
	}else if(!REGBOX.regEmail.test(userObj.email)){
		msg = "请输入正确的邮箱！！";
	}else if((!REGBOX.regCard15.test(userObj.cardNo) && !REGBOX.regCard18.test(userObj.cardNo))){
		msg = "请输入正确的身份证号码！！";
	}else if(isEmpty(userObj.plateNo)){
		msg = "请输入车牌号！！";
	}else if(userObj.brand <= 0){
		msg = "请选择车品牌！！";
	}else if(userObj.model <= 0){
		msg = "请选择车型号！！";
	}else if(isEmpty(userObj.vin)){
		msg = "请输入车架号！！";
	}
	return msg;
}
function addUser(){
	userObj = getFormJson($("#userForm"));
	msg = checkFormData(userObj);
	if(msg == "success"){
		param.userObjStr = objToJsonString(userObj);
		$.ajax({
			type:"POST",
			url:'person/cardManager_doRegisterUser.action',
			data:param,
			dataType:'json',
	        cache: false,
	        success: function(data){
	        	if(data.map){
	        	   	if("success" == data.map.msg){
		        		newInfoId = data.map.newInfoId;
		        		opt={};
						opt.onOk = function(){
						$("#saveBtn").attr("disabled",true);
						$("#registerCardBtn").attr("disabled",false);
						}
						opt.onClose = opt.onOk;
		        		showInfo("开户成功！！",opt);
		        	}else{
		        		showInfo(data.map.msg);
		        	}
	        	}else{
	        		showMsg(data.message, data.errormsg);
	        	}
	         }
	     });
	}else{
		showInfo(msg);
	}
}
















