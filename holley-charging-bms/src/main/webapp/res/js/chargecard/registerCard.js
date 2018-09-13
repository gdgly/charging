/**
 * 开卡
 */
var param={};
var cardRechargeUrl = "person/cardManager_cardRecharge.action";//充电卡充值接口
var newCardId=0;
$(document).ready(function(){
	initParams();
	//queryUserInfoList();
});
function initParams(){
	//initOneBootStrapDate($("#startTimeDate"));
	//initOneBootStrapDate($("#endTimeDate"));
	initStartEndDate($("#startTimeDate"),$("#endTimeDate"));
	if(registerCardInit()){
		searchCardInfo($("#chargeCardNo").val(),REGISTER_CARD);
	}else{
		opt={};
		opt.onOk = function(){
			reload();
		}
		opt.onClose = opt.onOk;
		opt.onCancel=function(){
			href("person/cardManager.action");
		}
		opt.onClose = opt.onCancel;
		showConfirm("请放入充电卡!!", opt)
	}
	
	//初始化查询按钮
	$("#saveBtn").on("click",function(){
		userObj = getFormJson($("#userForm"));
		userObj.phone = $("#phone").val();
		userObj.realName = $("#realName").val();
		cardObj = getFormJson($("#chargeCardForm"));
		cardObj.cardNo = $("#chargeCardNo").val();
		cardObj.busNo = $("#busNo").val();
		cardObj.applicationType = 1;//应用类型标识默认1
		cardObj.cardType = 1;//卡类型标识默认1:用户卡
		msg = checkFormData(userObj,cardObj);
		if(msg == "success"){
			if(checkCardResult(openCard(cardObj.password,replaceAll(cardObj.startTime,"-",""),replaceAll(cardObj.endTime,"-",""),userObj.realName,userObj.userCardNo,userObj.phone,userObj.email,userObj.sex == 1?"男":"女"))){
				addCard(cardObj);
			}
		}else{
			showInfo(msg);	
		}
	});
	//去充值
	$("#rechargeBtn").on("click",function(){
		if(newCardId > 0){
			resetForm($("#rechargeForm"));
			 $("#chargeCardNoForRechargeForm").text($("#chargeCardNo").val());
			$("#registerCardDiv").addClass("hide");
			$("#rechargeCardDiv").removeClass("hide");
		}else{
			showInfo("无充电卡信息！！");
			}
		
	});
	
	$("#rechargeMoney").on("keyup",function(){
		t = $(this).val();
		if(isNaN(t)){
			$(this).val("").focus();
		}else{
			temp = parseFloat(t);
			if(temp > MAX_RECHARGE_MONEY){
				$(this).val("").focus();
			}else{
				$("#doRechargeBtn").attr("disabled",false)
			}
		}
	});
	
	$("#rechargeMoney").on("change",function(){
		$(this).val(temp.toFixed(2));
	});
	
	$("#doRechargeBtn").on("click",function(){
		tempRechargeMoney = $("#rechargeMoney").val();
		tempChargeCardNo = $("#chargeCardNo").val();
		tempPassword = $("#rechargeCardPwd").val();
		if(tempRechargeMoney <= 0){
			showInfo("充值金额有误！！");
			return;
		}else if(isEmpty(tempChargeCardNo)){
			showInfo("充电卡卡号信息有误！！");
			return;	
		}else if(isEmpty(tempPassword) || tempPassword.length != 6 || isNaN(tempPassword)){
			showInfo("充电卡密码必须为6位数字！！");
			return;
		}
		opt={};
		opt.onOk = function(){
			param.rechargeMoney = tempRechargeMoney;
			param.chargeCardNo=tempChargeCardNo;
		//物理卡充值
		if(checkCardResult(rechargeCard(tempPassword,currentTime,tempRechargeMoney*100))){
			$("#doRechargeBtn").attr("disabled",true);
			$.ajax({
				type:"POST",
				url:cardRechargeUrl,
				data:param,
				dataType:'json',
		        cache: false,
		        success: function(data){
		        	if(data.message){
		        		if("success"==data.message){
		        			opt={};
		        			opt.onOk = function(){
		        				$("#rechargeMoney").val("").focus();
		        			}
		        			opt.onClose = opt.onOk;
							showInfo("充值成功！！",opt);
						}else{
							showInfo(data.message);
						}
		        	}else{
		        		showMsg(data.message, data.errormsg);
		        	}
		         },
		         error:function(){
		        	 showInfo("充值异常！！");
		         }
		     });	
			}
		}
		showConfirm("确定为充电卡["+tempChargeCardNo+"]充值金额"+tempRechargeMoney+"?", opt)
	});
	$("#registerCardBtn").on("click",function(){
		if(newInfoId > 0){
			href("person/registerCard.action?infoId="+newInfoId);
		}else{
			showInfo("非法操作！！");
		}
		
	});
}
function checkFormData(userObj,cardObj){
	msg = "success";
	if(!REGBOX.regCn.test(userObj.realName)){
		msg = "请输入正确的姓名！！";
	}else if(userObj.sex <= 0){
		msg = "请选择性别！！";
	}else if(!REGBOX.regMobile.test(userObj.phone)){
		msg = "请输入正确的11位手机号码！！";
	}else if(userObj.province <= 0){
		msg = "请选择省份！！";
	}else if(userObj.city <= 0){
		msg = "请选择市区！！";
	}else if(!REGBOX.regEmail.test(userObj.email)){
		msg = "请输入正确的邮箱！！";
	}else if((!REGBOX.regCard15.test(userObj.userCardNo) && !REGBOX.regCard18.test(userObj.userCardNo))){
		msg = "请输入正确的身份证号码！！";
	}else if(isEmpty(cardObj.busNo)){
		msg = "发卡运营商编号不正确！！";
	}else if(isEmpty(cardObj.cardNo)){
		msg = "充电卡卡号不正确！！";
	}else if(cardObj.applicationType <= 0){
		msg = "请选择应用类型！！";
	}else if(cardObj.cardType <= 0){
		msg = "请选择卡类型！！";
	}else if(isEmpty(cardObj.password) || cardObj.password.length < 6 || isNaN(cardObj.password)){
		msg = "请输入6位数字密码！！";
	}else if(isEmpty(cardObj.startTime)){
		msg = "请输入启用日期！！";
	}else if(isEmpty(cardObj.endTime)){
		msg = "请输入有效日期！！";
	}
	return msg;
}
function addCard(cardObj){
		param.cardObjStr = objToJsonString(cardObj);
		$.ajax({
			type:"POST",
			url:'person/cardManager_doRegisterCard.action',
			data:param,
			dataType:'json',
	        cache: false,
	        success: function(data){
	        	if(data.map){
	        		if("success" == data.map.msg){
		        		newCardId = data.map.newCardId;
		        		opt={};
						opt.onOk = function(){
						$("#saveBtn").attr("disabled",true);
						$("#rechargeBtn").attr("disabled",false);
						}
						opt.onClose = opt.onOk;
		        		showInfo("开卡成功！！",opt);
		        	}else{
		        		showInfo(data.map.msg);
		        	}
	        	}else{
	        		showMsg(data.message, data.errormsg);
	        	}
	         }
	     });
}
















