var param={};
var sysRole;
var msg;
$(document).ready(function(){
	
});

//查询按钮点击事件
$("#saveBtn").on("click",function(){
	if(requestType == REQUEST_TYPE_ADD){
		addRole();
	}else{
		editRole();
	}
});

function addRole(){
	param={};
	var role = getFormJson($("#roleForm"));
	msg = validForm(role);
	if ("success" != msg) {
		alert(msg);
		return false;
	}
	param.sysRole=formDataToJsonString($("#roleForm"));
	$('#roleForm').ajaxSubmit({
		url:'sys/role_addRole.action',
		type:'post',
		dataType:'json',
		data:param,
		success:function(data){
			if(data.success){
				href("sys/roleList.action");
			}else{
				showMsg(data.message, data.errormsg);
			}
		}
	});
}

function editRole(){
	param={};
	var role = getFormJson($("#roleForm"));
	msg = validForm(role);
	if ("success" != msg) {
		alert(msg);
		return false;
	}
	param.sysRole=formDataToJsonString($("#roleForm"));
	param.roleid = roleid;
	$('#roleForm').ajaxSubmit({
		url:'sys/role_editRole.action',
		type:'post',
		dataType:'json',
		data:param,
		success:function(data){
			if(data.success){
				href("sys/roleList.action");
			}else{
				showMsg(data.message, data.errormsg);
			}
		}
	});
}

//校验表单信息
function validForm(obj) {
	if (isEmpty(obj.rolename)) {
		return "请输入角色名称.";
	} else if (isEmpty(obj.type)) {
		return "请选择角色类型.";
	} 
	/*else if (isEmpty(obj.status)) {
		return "请选择角色状态.";
	}*/
	return "success";
}






