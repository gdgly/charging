var param={};
$(document).ready(function(){
	$("#saveBtn").on("click",function(){
		verify();
	});
});


//保存审核
function verify(){
	param={};
	param.id = $.trim($('#applyid').text());
	param.validstatus = $('#validStatus option:selected').val();
	param.validremark =  $.trim($("#validRemark").val());
	param.tm = new Date().getTime();
	var msg = validParam(param);
	if ("success" != msg) {
		showInfo(msg);
		return;
	}
	$.ajax({
		type:"POST",
		url:'person/userPileApply_applyVerify.action',
		data:param,
		dataType:'json',
        cache: false,
        success:function(data,options){
        	if(data.success){
        		gobackAndReload();
        	}else{
        		showMsg(data.message, data.errormsg);
        	}
        }
	});
}

function validParam(obj){
	if(isEmpty(obj.id)){
		return "桩代管申请id不能为空";
	}else if(isEmpty(obj.validstatus)){
		return "审核结果不能为空";
	}
	return "success";
}





