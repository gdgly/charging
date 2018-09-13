var param={};
var msg;
$(document).ready(function(){
});

//查询按钮点击事件
$("#saveBtn").on("click",function(){
	if(requestType == REQUEST_TYPE_ADD){
		addPileModel();
	}else{
		editPileModel();
	}
});

function addPileModel(){
	param={};
	var pileModel = getFormJson($("#pileModelForm"));
	msg = validForm(pileModel);
	if ("success" != msg) {
		showInfo(msg);
		return false;
	}
	param.pileModel=formDataToJsonString($("#pileModelForm"));
	$('#pileModelForm').ajaxSubmit({
		url:'device/pileModel_addPileModel.action',
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

function editPileModel(){
	param={};
	var pileModel = getFormJson($("#pileModelForm"));
	msg = validForm(pileModel);
	if ("success" != msg) {
		showInfo(msg)
		return false;
	}
	param.pileModel=formDataToJsonString($("#pileModelForm"));
	param.modelid = modelid;
	$('#pileModelForm').ajaxSubmit({
		url:'device/pileModel_editPileModel.action',
		type:'post',
		dataType:'json',
		data:param,
		success:function(data){
			if(data.success){
				gobackAndReload();
			}else{
				showInfo(data.message);
			}
		}
	});
}

//校验表单信息
function validForm(obj) {
	if (isEmpty(obj.brand)) {
		return "请输入品牌名称.";
	} else if (isEmpty(obj.chaWay)) {
		return "请选择充电方式.";
	}else if (isEmpty(obj.chaType)) {
		return "请选择充电类型.";
	}else if(isEmpty(obj.standard)){
		return "请选择标准.";
	}else if(isEmpty(obj.outV)){
		return "请输入输出电压.";
	}else if(isEmpty(obj.ratP)){
		return "请输入额定功率.";
	}
	return "success";
}






