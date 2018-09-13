var param={};
var sysRole;
var msg;
$(document).ready(function(){
	
});

//查询按钮点击事件
$("#saveBtn").on("click",function(){
	if(requestType == REQUEST_TYPE_ADD){
		addRule();
	}else{
		editRule();
	}
});

function addRule(){
	param={};
	var record = getFormJson($("#ruleForm"));
	msg = validForm(record);
	if ("success" != msg) {
		showInfo(msg);
		return false;
	}
	param.sysRule=objToJsonString(record);
	$('#ruleForm').ajaxSubmit({
		url:'sys/rule_addRule.action',
		type:'post',
		dataType:'json',
		data:param,
		success:function(data){
			if(data.success){
				href("sys/ruleList.action");
			}else{
				showMsg(data.message, data.errormsg);
			}
		}
	});
}

function editRule(){
	param={};
	var record = getFormJson($("#ruleForm"));
	record.id = $.trim($("#id").val());
	msg = validForm(record);
	if ("success" != msg) {
		showInfo(msg);
		return false;
	}
	param.sysRule = objToJsonString(record);
	$('#ruleForm').ajaxSubmit({
		url:'sys/rule_editRule.action',
		type:'post',
		dataType:'json',
		data:param,
		success:function(data){
			if(data.success){
				href("sys/ruleList.action");
			}else{
				showMsg(data.message, data.errormsg);
			}
		}
	});
}

//校验表单信息
function validForm(obj) {
	if (isEmpty(obj.id)) {
		return "请输入id.";
	} else if (isEmpty(obj.name)) {
		return "请输入名称.";
	} else if (isEmpty(obj.status)) {
		return "请输选择规则状态.";
	} else if (isEmpty(obj.ruleCheck)) {
		return "请输入规则值.";
	}
	return "success";
}






