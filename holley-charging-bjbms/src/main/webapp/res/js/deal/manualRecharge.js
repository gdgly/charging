var param={};
var sysRole;
var msg;
var maxRechargeMoney = 100000;
$(document).ready(function(){
	$("#money").on("keyup",function(){
		temp = $(this).val();
		if(isNaN(temp) || isEmpty(temp)){
			$(this).val("").focus();
			return;
		}else if(temp > maxRechargeMoney){
			$(this).val("").focus();
			return;
		}
		else{
			
			if(temp.indexOf(".") != -1){
				count = temp.substring(temp.indexOf(".")+1,temp.length);
				if(count.length > 1){
					$("#money").val(Math.round(parseFloat(temp * 100)) / 100);
				}
			}
			//$("#cashMoney").val(Math.round(parseFloat(temp * 100)) / 100);
		}
	});
	
	
});

$("#saveBtn").on("click",function(){
	queryRechargeUser();
});

function queryRechargeUser(){
	param={};
	var recharge = getFormJson($("#manualRechargeForm"));
	recharge.payWay = $('#payWay option:selected').val();
	recharge.cashWay = $('#cashWay option:selected').val();
	recharge.userId = $("#userId").text();
	msg = validForm(recharge);
	if ("success" != msg) {
		showInfo(msg);
		return false;
	}
	param.userid = recharge.userId;
	$.ajax({
		type:"POST",
		url:"deal/account_queryRechargeUser.action",
		data:param,
		dataType:"json",
		cache:false,
		beforeSend:function(){$("#loading").removeClass("hide");},
        complete:function(){
	    	  $("#loading").addClass("hide"); 
	      },
		success:function(data){
			if(data.userReal){
				showUser(data.userReal);
			}else{
				showMsg(data.message, data.errormsg);
			}
		}
		
	});
}

function showUser(obj){
	var option = {
			title: "系统提示",
			btn: window.wxc.xcConfirm.btnEnum.okcancel,
			onOk: function(){
				saveRecharge(obj.userid);
			}
		}
	var content = "用户信息："+obj.username;
	if(!isEmpty(obj.realname)){
		content += "【"+obj.realname+"】";
	}
	content += "</br>手机号码："+obj.phone;
	if(rechargeType == 1){//充值
		content +="</br>确定给该用户的钱包充值吗?";
	}else if(rechargeType == 2){//提现
		content +="</br>确定给该用户提现吗?";
	}else{
		showMsg("非法操作");
		return;
	}
	
	showCustom(content,option);
}


function saveRecharge(userid){
	param={};
	var recharge = getFormJson($("#manualRechargeForm"));
	recharge.payWay = $('#payWay option:selected').val();
	recharge.userId = userid;
	msg = validForm(recharge);
	if ("success" != msg) {
		showInfo(msg);
		return false;
	}
	param.recharge = objToJsonString(recharge);
	param.rechargeType=rechargeType;
	$('#manualRechargeForm').ajaxSubmit({
		url:'deal/account_manualRecharge.action',
		type:'post',
		dataType:'json',
		data:param,
		beforeSend:function(){$("#loading").removeClass("hide");},
        complete:function(){
	    	  $("#loading").addClass("hide"); 
	      },
		success:function(data){
			if(data.success){
				if(data.message){
					var opt={};
					opt.onOk = function(){
						reload();
					}
					showInfo(data.message,opt);
				}else{
					href("deal/accountList.action");
				}
			}else{
				showMsg(data.message, data.errormsg);
			}
		}
	});
}

//校验表单信息
function validForm(obj) {
	if(isEmpty(obj.userId)){
		return "用户编码不能为空.";
	}else if(!REGBOX.regMoney.test(obj.money)){
		return "请正确输入金额.";
	}
	if(rechargeType == 1){
		if(obj.payWay == null || obj.payWay <= 0){
			return "请选择支付方式.";
		}
	}else if(rechargeType == 2){
		if(obj.cashWay == null || obj.cashWay <= 0){
			return "请选择提现方式.";
		}else if(isEmpty(obj.accountInfo)){
			return "账户信息不能为空.";
		}
	}
	return "success";
}








