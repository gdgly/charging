var param={};
$(document).ready(function(){
	
	$("#saveBtn").on("click",function(){
		save();
	});
	$("#cancelBtn").on("click",function(){
		gobackAndReload();
	});
});

function save(){
	param={};
	param.id = id;
	param.validstatus = $('#validStatus option:selected').val();
	param.validremark = $.trim($("#validRemark").val());
	param.noticetypes = getNoticeTypes();
	param.tm = new Date().getTime();
	var msg = validParam(param);
	if ("success" != msg) {
		showInfo(msg);
		return;
	}
	$.ajax({
		type:"POST",
		url:'deal/cashVerify_cashVerify.action',
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

//审核结果切换事件
function onStatusChange(){
	var validStatus = $('#validStatus option:selected').val();
	if(validStatus == CASH_VERIFY_STATUS_FAILED){
		$('#validRemarkDiv').removeClass('hide');
		$('#noticeTypeDiv').removeClass('hide');
	}else{
		$('#validRemarkDiv').addClass('hide');
		$('#noticeTypeDiv').addClass('hide');
		$('#validRemark').val('');
	}
}

function validParam(obj){
	if(isEmpty(param.id)){
		return "提现记录id不能为空";
	}else if(isEmpty(obj.validstatus)){
		return "审核结果不能为空";
	}else if(obj.validstatus == CASH_VERIFY_STATUS_FAILED && isEmpty(param.validremark)){
		return "审核失败原因不能为空";
	}
	return "success";
}



