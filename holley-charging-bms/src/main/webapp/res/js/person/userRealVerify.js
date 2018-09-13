var param={};
$(document).ready(function(){
	$("#saveBtn").on("click",function(){
		save();
	});
});


//保存审核结果
function save(){
	param={};
	param.userid = userid;
	param.addtimestr = $("#addTimeStr").text();
	param.validstatus = $('#validStatus option:selected').val();
	param.remark =  $.trim($("#validRemark").val());
	param.noticetypes = getNoticeTypes();
	param.tm = new Date().getTime();
	var msg = validParam(param);
	if ("success" != msg) {
		showInfo(msg);
		return;
	}
	$.ajax({
		type:"POST",
		url:'person/userRealVerify_realVerify.action',
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
	if(validStatus == USER_REAL_VERIFY_STATUS_FAILED){
		$('#validRemarkDiv').removeClass('hide');
	}else{
		$('#validRemarkDiv').addClass('hide');
		$('#validRemark').val('');
	}
}

function validParam(obj){
	if(isEmpty(param.userid)){
		return "用户id不能为空";
	}else if(isEmpty(obj.addtimestr)){
		return "提交时间不能为空";
	}else if(isEmpty(obj.validstatus)){
		return "审核结果不能为空";
	}else if(obj.validstatus == USER_REAL_VERIFY_STATUS_FAILED && isEmpty(param.remark)){
		return "审核失败原因不能为空";
	}
	return "success";
}






