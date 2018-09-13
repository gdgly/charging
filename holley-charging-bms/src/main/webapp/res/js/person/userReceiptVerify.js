var param={};
$(document).ready(function(){
	$("#saveBtn").on("click",function(){
		verify();
	});
	
});


//保存审核
function verify(){
	param={};
	param.id = $.trim($('#receiptid').text());
	param.validstatus = $('#validStatus option:selected').val();
	param.expresscompany = $('#expressCompany').val();
	param.expressnum = $('#expressNum').val();
	param.validremark =  $.trim($("#validRemark").val());
	param.noticetypes = getNoticeTypes();
	param.tm = new Date().getTime();
	var msg = validParam(param);
	if ("success" != msg) {
		showInfo(msg);
		return;
	}
	$.ajax({
		type:"POST",
		url:'person/userReceipt_receiptVerify.action',
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
	if(validStatus == BILL_STATUS_FAILURE){
		$('#expressDiv').addClass('hide');
		$('#validRemarkDiv').removeClass('hide');
		
		$('#expressCompany').val('');
		$('#expressNum').val('');
	}else{
		$('#expressDiv').removeClass('hide');
		$('#validRemarkDiv').addClass('hide');
		
		$('#validRemark').val('');
	}
}

function getNoticeTypes(){
	var noticetypeArray = new Array();
	$("input[name='noticeType']:checked").each(function(){ 
		noticetypeArray.push($(this).val());
	}); 
	return noticetypeArray.join(",");
}

function validParam(obj){
	if(isEmpty(obj.id)){
		return "开票id不能为空";
	}else if(isEmpty(obj.validstatus)){
		return "审核结果不能为空";
	}else if(obj.validstatus == BILL_STATUS_SUCCESS && isEmpty(param.expresscompany)){
		return "快递公司不能为空";
	}else if(obj.validstatus == BILL_STATUS_SUCCESS && isEmpty(param.expressnum)){
		return "快递单号不能为空";
	}else if(obj.validstatus == BILL_STATUS_FAILURE && isEmpty(param.validremark)){
		return "审核失败原因不能为空";
	}
	return "success";
}





