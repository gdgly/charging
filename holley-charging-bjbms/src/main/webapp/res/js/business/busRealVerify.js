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
		url:'business/busRealVerify_realVerify.action',
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
		$('#noticeTypeDiv').removeClass('hide');
	}else{
		$('#validRemarkDiv').addClass('hide');
		$('#noticeTypeDiv').addClass('hide');
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

//线上打款审核
//function verifyByOnlineBankTransfer(){
//	param={};
//	param.businfoid = businfoid;
//	param.addtimestr = $("#addTimeStr").text();
//	param.accrealname = $("#accRealName").text();
//	param.bankname = $("#bankName").text();
//	param.bankaccount = $("#bankAccount").text();
//	param.remark =  $.trim($("#remark").val());
//	param.tm = new Date().getTime();
//	$.ajax({
//		type:"POST",
//		url:'business/busRealVerify_verifyByBankTransfer.action',
//		data:param,
//		dataType:'json',
//        cache: false,
//        success:function(data,options){
//        	if(data.success){
//        		gobackAndReload();
//        	}else{
//        		showMsg(data.message, data.errormsg);
//        	}
//        }
//	});
//}

//验证转账金额(小于1元)
//function verifyMoney(){
//	var moneyReg = /^0\.[1-9]{0,2}$/;
//	 if(validmoney=="" || !moneyReg.test(validmoney)){
//		 $("#message").text('请输入0.00~0.99元的转账金额.');
//		 $("#message").css('color','red');
//	     return false;
//	 }else{
//		 $("#message").text('打款审核通过，请输入转账金额(0.00~0.99元).');
//		 $("#message").css('color','black');
//		 $("#validMoney").val($.trim($("#validMoney").val()));
//	 }
//    return true
//}


