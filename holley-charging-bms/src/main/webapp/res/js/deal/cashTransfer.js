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
	param.cashstatus = $('#cashStatus option:selected').val();
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
		url:'deal/cashTransfer_cashTransfer.action',
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
	var cashStatus = $('#cashStatus option:selected').val();
	if(cashStatus == CASH_STATUS_FAILURE){
		$('#validRemarkDiv').removeClass('hide');
	}else{
		$('#validRemarkDiv').addClass('hide');
		$('#validRemark').val('');
	}
}

function validParam(obj){
	if(isEmpty(param.id)){
		return "提现记录id不能为空";
	}else if(isEmpty(obj.cashstatus)){
		return "提现结果不能为空";
	}else if(obj.cashstatus == CASH_STATUS_FAILURE && isEmpty(param.validremark)){
		return "提现失败原因不能为空";
	}
	return "success";
}



