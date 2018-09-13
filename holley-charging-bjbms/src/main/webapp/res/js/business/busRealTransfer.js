var param={};
$(document).ready(function(){
	
	$("#cancelBtn").on("click",function(){
		gobackAndReload();
	});
	
	$("#saveBtn").on("click",function(){
		save();
	});
});


function save(){
	param={};
	param.businfoid = businfoid;
	param.addtimestr = $("#addTimeStr").text();
	param.validstatus = $('#validStatus option:selected').val();
	param.remark =  $.trim($("#remark").val());
	param.noticetypes = getNoticeTypes();
	param.tm = new Date().getTime();
	var msg = validParam(param);
	if ("success" != msg) {
		showInfo(msg);
		return;
	}
	$.ajax({
		type:"POST",
		url:'business/busRealTransfer_realVerify.action',
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
	if(validStatus == BUS_REAL_VERIFY_STATUS_VERIFYFAILED){
		$('#validRemarkDiv').removeClass('hide');
	}else{
		$('#validRemarkDiv').addClass('hide');
		$('#validRemark').val('');
	}
}

function validParam(obj){
	if(isEmpty(param.businfoid)){
		return "企业信息id不能为空";
	}else if(isEmpty(obj.addtimestr)){
		return "申请时间不能为空";
	}else if(isEmpty(obj.validstatus)){
		return "审核结果不能为空";
	}else if(obj.validstatus == BUS_REAL_VERIFY_STATUS_VERIFYFAILED && isEmpty(param.remark)){
		return "审核失败原因不能为空";
	}
	return "success";
}




