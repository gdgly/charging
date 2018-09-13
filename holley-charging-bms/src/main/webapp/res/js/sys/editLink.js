var param={};
var msg;
$(document).ready(function(){
	
});


//查询按钮点击事件
$("#saveBtn").on("click",function(){
	if(requestType == REQUEST_TYPE_ADD){
		addLink();
	}else{
		editLink();
	}
});

function addLink(){
	param={};
	var sysLink = getFormJson($("#linkForm"));
	msg = validForm(sysLink);
	if ("success" != msg) {
		showInfo(msg);
		return false;
	}
	param.sysLink=objToJsonString(sysLink);
	$('#linkForm').ajaxSubmit({
		url:'sys/dic_addLink.action',
		type:'post',
		dataType:'json',
		data:param,
		success:function(data){
			if(data.success){
				gobackAndReload();
			}else{
				showMsg(data.message, data.errormsg);
			}
		}
	});
}

function editLink(){
	param={};
	var sysLink = getFormJson($("#linkForm"));
	sysLink.typeId = $('#typeId option:selected').val();
	sysLink.value = $.trim($("#value").val());
	sysLink.id = $.trim($("#id").val());
	msg = validForm(sysLink);
	if ("success" != msg) {
		showInfo(msg);
		return false;
	}
	param.sysLink = objToJsonString(sysLink);
	param.id = id;
	$('#linkForm').ajaxSubmit({
		url:'sys/dic_editLink.action',
		type:'post',
		dataType:'json',
		data:param,
		success:function(data){
			if(data.success){
				gobackAndReload();
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
	} else if (isEmpty(obj.typeId)) {
		return "请选择数据类型.";
	} else if (isEmpty(obj.name)) {
		return "请输入名称.";
	} else if (isEmpty(obj.value)) {
		return "请输入值.";
	}
	return "success";
}






