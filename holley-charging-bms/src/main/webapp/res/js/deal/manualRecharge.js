var param={};
var sysRole;
var msg;
$(document).ready(function(){
});

$("#saveBtn").on("click",function(){
	queryRechargeUser();
});

function queryRechargeUser(){
	param={};
	var recharge = getFormJson($("#manualRechargeForm"));
	recharge.payWay = $('#payWay option:selected').val();
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
	showCustom(content+"</br>确定给该用户的钱包充值吗?",option);
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
	$('#manualRechargeForm').ajaxSubmit({
		url:'deal/account_manualRecharge.action',
		type:'post',
		dataType:'json',
		data:param,
		success:function(data){
			if(data.success){
				if(data.message){
					var opt={};
					opt.onOk = function(){
						href("deal/accountList.action");
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
	}else if(obj.payWay == null || obj.payWay <= 0){
		return "请选择支付方式.";
	}
	return "success";
}








