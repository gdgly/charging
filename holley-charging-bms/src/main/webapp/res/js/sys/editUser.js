var param={};
var sysRole;
var msg;
$(document).ready(function(){
});

//查询按钮点击事件
$("#saveBtn").on("click",function(){
	if(requestType == REQUEST_TYPE_ADD){
		addUser();
	}else{
		editUser();
	}
});

function addUser(){
	param={};
	var user = getFormJson($("#userForm"));
	msg = validForm(user);
	if ("success" != msg) {
		showInfo(msg);
		return false;
	}
	param.busUser=formDataToJsonString($("#userForm"));
	$('#userForm').ajaxSubmit({
		url:'sys/user_addUser.action',
		type:'post',
		dataType:'json',
		data:param,
		success:function(data){
			if(data.success){
				href("sys/userList.action");
			}else{
				showMsg(data.message, data.errormsg);
			}
		}
	});
}

function editUser(){
	param={};
	var user = getFormJson($("#userForm"));
	msg = validForm(user);
	if ("success" != msg) {
		showInfo(msg)
		return false;
	}
	param.busUser=formDataToJsonString($("#userForm"));
	param.userid = userid;
	$('#userForm').ajaxSubmit({
		url:'sys/user_editUser.action',
		type:'post',
		dataType:'json',
		data:param,
		success:function(data){
			if(data.success){
				href("sys/userList.action");
			}else{
				showMsg(data.message, data.errormsg);
			}
		}
	});
}

//校验表单信息
function validForm(obj) {
	if (isEmpty(obj.username)) {
		return "请输入用户昵称.";
	} else if (obj.username.length < 3) {
		return "昵称长度不小于3个字符.";
	}else if (isEmpty(obj.roleid)) {
		return "请选择角色.";
	}else if(!isEmpty(obj.phone) && !REGBOX.regMobile.test(obj.phone)){
		return "请正确输入手机号码.";
	}
	if(obj.roleid == 5){
		if (isEmpty(obj.groupName)) {
			return "请输入集团名称.";
		}else if(obj.scale <= 0){
			return "请选择集团规模.";
		}else if(obj.province <= 0){
			return "请选择省份.";
		}else if(obj.city <= 0){
			return "请选择市区.";
		}else if(isEmpty(obj.tel)){
			return "请输入联系电话.";
		}else if(isEmpty(obj.domain)){
			return "请输入主营业务.";
		}else if(isEmpty(obj.address)){
			return "请输入详细地址.";
		}
	}
	return "success";
}

//集团管理员相关
initArea($("#province"), $("#city"));
$("#roleid").on("change",function(){
	roleid = $(this).val();
	if(5 == roleid){
		if($("#groupUserInfoDiv").hasClass("hide")){
			$("#groupName").val("");
			$("#scale").val("0");
			$("#province").val("0");
			$("#city").val("0");
			$("#tel").val("");
			$("#domain").val("");
			$("#address").val("");
			$("#groupUserInfoDiv").removeClass("hide");
		}
	}else{
		if(!$("#groupUserInfoDiv").hasClass("hide")){
			$("#groupUserInfoDiv").addClass("hide");
		}
	}
});



