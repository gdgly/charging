/**
 * 读取充电卡信息
 */

var param={};
var temp;
var isInitCard = false;
var MAX_RECHARGE_MONEY = 100000;//充电卡最大充值金额
var cardRechargeUrl = "person/cardManager_cardRecharge.action";//充电卡充值接口
var changeCardPwdUrl = "person/cardManager_changeCardPwd.action";//修改充电卡密码接口
var showBadChargeListUrl = "person/cardManager_showBadChargeList.action";//查看灰色记录
var cleanBadRecordUrl = "person/cardManager_cleanBadRecord.action";//清灰色记录
var badChargeId = [];//灰色记录ID
var shouldMoney = [];//应缴金额
$(document).ready(function(){
	initParams();
});
//选择框util
function subCheck(obj){
	count = 0;
	allcount = 0;
	if(!$(obj).is(':checked')){
		$("#allBillCheck").get(0).checked = false;
	}
	$("[name='billCheck']").each(function(index,value){
			allcount = allcount + 1;
	})
		$("[name='billCheck']").each(function(index,value){
			if($(value).is(':checked')){
				count = count + 1;
			}
	})
	if(((count+allcount) > 0) && (count == allcount)){
		$("#allBillCheck").get(0).checked = true;
	}
}

//弹出付款modal
function goPayment(badChargeId,shouldMoney,chargeCardNo,usableMoney){
	totalShouldMoney = 0;
	time = "";
	for(x in shouldMoney){
		if(shouldMoney[x] <= 0){
			showInfo("不可选择月账单金额为0的记录！！");
			return;
		}
		totalShouldMoney += parseFloat(shouldMoney[x]);
	}
	if(totalShouldMoney > usableMoney){
		showInfo("充电卡余额不足！！");
		return;
	}
	if(totalShouldMoney > 0){
		resetForm($("#badPaymentForm"));
		$("#totalShouldMoneyModel").text(totalShouldMoney);//所选记录支付总金额
		$("#badChargeIdModel").val(badChargeId);//所选付款灰色记录ID
		$("#chargeCardNoModel").text(chargeCardNo);//充电卡号
		$("#usableMoneyModel").text(usableMoney);//充电卡可用余额
		$("#badPaymentModal").modal().css({
			'margin-top': function () {
				return "5%";
			}
		});
	}else{
		showInfo("付款金额必须大于0元！！");
	}
}
function initParams(){
	if(checkCardInit()){
		searchCardInfo($("#chargeCardNo").val(),READ_CARDINFO);
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
	$("#readCardUsableBtn").on("click",function(){
		resetForm($("#readCardUsableMoneyForm"));
		$("#cardNoForReadCardUsableMoney").text($("#chargeCardNo").val());
		$("#readCardUsableMoneyModal").modal().css({
			'margin-top': function () {
				return "5%";
			}
		});
	});
	$("#readCardUsableMoneyBtn").on("click",function(){
		pwd = $("#readCardUsableMoneyPwd").val();
		if(isEmpty(pwd) || pwd.length != 6 || isNaN(pwd)){
			showInfo("请输入正确的6位数字密码！！");
			return;
		}
		if(checkCardResult(getCardUsableMoney(pwd))){
			$("#usableMoney").val(getReslutByType(operaType.getUsableMoneyCard).cardUsableMoney);
			$("#readCardUsableMoneyModal").modal("hide");
		}
		
	});
	
	$("#allBillCheck").on("click",function(){
		if($(this).is(':checked')){
			$("[name='billCheck']").each(function(index,value){
				$(value).get(0).checked = true;
			});
		}else{
			$("[name='billCheck']").each(function(index,value){
				$(value).get(0).checked = false;
			});
		}
	})
		$("#paymentBtn").on("click",function(){
			 badChargeId = [];
			 shouldMoney = [];
			 totalShouldMoney=0;
			 usableMoney=$("#usableMoney").val();
			 chargeCardNo=$("#chargeCardNo").val();
			$("[name='billCheck']").each(function(index,value){
			if($(value).is(':checked')){
				badChargeId.push($(value).attr("badChargeId"));
				shouldMoney.push($(value).attr("shouldMoney"));
			}
			});
			if(badChargeId.length > 0 && shouldMoney.length > 0){
				if(isEmpty(chargeCardNo)){
					showInfo("无充电卡信息！！");
				}else if(isNaN(usableMoney)){
					showInfo("充电卡余额信息有误！！");
				}else{
					goPayment(badChargeId,shouldMoney,chargeCardNo,usableMoney);
				}
				
			}else{
				showInfo("请选择付款记录！！");
			}
		});
		
	$("#paymentModelBtn").on("click",function(){
		param={};
		param.chargeCardNo = $("#chargeCardNoModel").text();
		param.badChargeId = $("#badChargeIdModel").val();
		param.totalShouldMoney = $("#totalShouldMoneyModel").text();
		param.usableMoney = $("#usableMoneyModel").text();
		$.ajax({
			type:"POST",
			url:cleanBadRecordUrl,
			data:param,
			dataType:'json',
	        cache: false,
	        success: function(data){
	        	$("#doRechargeBtn").attr("disabled",false);
	        	if(data.message){
	        		if("success"==data.message){
						showInfo("扣款成功！！");
					}else{
						showInfo(data.message);
					}
	        	}else{
	        		showMsg(data.message, data.errormsg);
	        	}
	         },
	         error:function(){
	        	 showInfo("系统异常！！");
	         }
	     });
	});
	$("#checkBadRcordBtn").on("click",function(){
	/*	checkCardResult(getBadRecord("123456"));
		return;*/
		param.badChargeIds = $("#badChargeIds").val();
		param.chargeCardNo = $("#chargeCardNo").val();
		var tbody = $("#badChargeListBody");
		var html = '';
		$.ajax({
			type:"POST",
			url:showBadChargeListUrl,
			data:param,
			dataType:'json',
	        cache: false,
	        success: function(data){
	        	$("#doRechargeBtn").attr("disabled",false);
	        	if(data.map){
	        		if("success"==data.map.msg){
	        			 $(data.map.badChargeList).each(function(index,item){
	        				 html += '<tr>';
	        				 html += '<td>'+ '<input onclick="subCheck(this)" name="billCheck" type="checkbox" badChargeId="'+item.id+'" shouldMoney="'+item.shouldMoney+'"/>'+'</td>';
	        				 /*html += getTdHtml(item.tradeNo, 2);*/
	        				 html += '<td>'+item.tradeNo+'</td>';
	            			 html += getTdHtml(item.stationName, 15);
	            			 html += getTdHtml(item.address, 15);
	            			 html += '<td>'+item.pileName+'</td>';
	            			 html += '<td>'+item.phone+'</td>';
	            			 html += '<td>'+item.chaLen+'</td>';
	            			 html += '<td>'+item.chaFee+'</td>';
	            			 html += '<td>'+item.serviceFee+'</td>';
	            			 html += '<td>'+item.parkFee+'</td>';
	            			 html += '<td>'+item.updateTimeDesc+'</td>';
	            			 html += '<td>'+item.shouldMoney+'</td>';
	            			 html += '<td>'+item.payStatusDesc+'</td>';
	            			 html += '</tr>';
	        			 });
	        			 tbody.html(html);
	        			 $("#CardInfoDiv").addClass("hide");
	        			 $("#badChargeListDiv").removeClass("hide");
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
	});
	//灰色记录end
	
	//初始化刷新按钮
	$("#refreshBtn").on("click",function(){
		reload();
	});
	//充值start
	$("#rechargeBtn").on("click",function(){
		resetForm($("#rechargeForm"));
		 $("#chargeCardNoForRechargeForm").text($("#chargeCardNo").val());
			$("#CardInfoDiv").addClass("hide");
			$("#rechargeCardDiv").removeClass("hide");
	});
	///////////////////////////////////////////////////
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
	///////////////////////////////////////////////////
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
	//充值end
	
	//密码修改start
	$("#goChangePwdBtn").on("click",function(){
		$("#changePwdBtn").attr("disabled",true);
		$("#cardNoForChangePwd").text($("#chargeCardNo").val());
		resetForm($("#cardChangePwdForm"));
		$("#cardChangePwdModal").modal().css({
			'margin-top': function () {
				return "5%";
			}
		});
	});
	$("#password").on("keyup",function(){
		$("#changePwdBtn").attr("disabled",false);
	});
	
	$("#changePwdBtn").on("click",function(){
		newPassword = $("#password").val().trim();
		
		if(isEmpty(newPassword) || newPassword.length < 6 || isNaN(newPassword)){
			showInfo("密码必须为6位数字密码！！")
			return;
		}
		param={};
		param.chargeCardNo = $("#chargeCardNo").val();
		param.newPassword = $("#password").val();
		if(checkCardResult(resetCardPwd(newPassword))){
			$.ajax({
				type:"POST",
				url:changeCardPwdUrl,
				data:param,
				dataType:'json',
		        cache: false,
		        success: function(data){
		        	if(data.message){
		        		if("success"==data.message){
		        			opt={};
		        			opt.onOk = function(){
		        				$("#cardChangePwdModal").modal("hide");
		        			}
		        			opt.onClose = opt.onOk;
							showInfo("修改成功！！",opt);
						}else{
							showInfo(data.message);
						}
		        	}else{
		        		showMsg(data.message, data.errormsg);
		        	}
		         },
		         error:function(){
		        	 showInfo("系统异常！！");
		         }
		     });	
		}
	});
	//密码修改end
}
