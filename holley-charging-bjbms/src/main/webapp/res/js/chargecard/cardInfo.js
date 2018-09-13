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
var badArray = [];//异常灰
var okArray=[];//正常灰
var tempPwd;
$(function(){
	initParams();
})



//选择框util
function subCheck(obj){
	//
	
	$("[name='billCheck']").not($(obj)).each(function(index,value){
		$(value).get(0).checked = false;
	})
	
	//
	
	
	
	
	/*count = 0;
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
	}*/
}

//弹出付款modal
function goPayment(payNo,shouldMoney,chargeCardNo,usableMoney,badrecordId,freeze){
	//totalShouldMoney = 0;
	/*for(x in shouldMoney){
		if(shouldMoney[x] <= 0){
			showInfo("不可选择月账单金额为0的记录！！");
			return;
		}
		totalShouldMoney += parseFloat(shouldMoney[x]);
	}
	if(totalShouldMoney > usableMoney){
		showInfo("充电卡余额不足！！");
		return;
	}*/
	if(shouldMoney > 0){
		resetForm($("#badPaymentForm"));
		$("#totalShouldMoneyModel").text(shouldMoney);//所选记录支付总金额
		$("#badChargeIdModel").val(payNo);//所选付款灰色记录ID
		$("#chargeCardNoModel").text(chargeCardNo);//充电卡号
		$("#usableMoneyModel").text(usableMoney);//充电卡可用余额
		$("#freezeModel").text(freeze)
		$("#badrecordIdModel").text(badrecordId)
		$("#badPaymentPwd").val(tempPwd);
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
	$("#readCardUsableBtn").on("click",function(){
		resetForm($("#readCardUsableMoneyForm"));
		$("#cardNoForReadCardUsableMoney").text($("#chargeCardNo").val());
		$("#readCardUsableMoneyModal").modal().css({
			'margin-top': function () {
				return "5%";
			}
		});
	});
	
	$("#checkBadRcordBtn").on("click",function(){
		resetForm($("#readBadRecordForm"));
		$("#cardNoForReadBadRecord").text($("#chargeCardNo").val());
		$("#readBadRecordPwd").val(defaultPwd)
		$("#readBadRecordModal").modal().css({
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
	
	/*$("#allBillCheck").on("click",function(){
		if($(this).is(':checked')){
			$("[name='billCheck']").each(function(index,value){
				$(value).get(0).checked = true;
			});
		}else{
			$("[name='billCheck']").each(function(index,value){
				$(value).get(0).checked = false;
			});
		}
	})*/
		$("#paymentBtn").on("click",function(){
			payNo = $("[name='billCheck']:checked").attr("payNo");
			shouldMoney = $("[name='billCheck']:checked").attr("shouldMoney");
			badrecordId = $("[name='billCheck']:checked").attr("badrecordId");
			freeze = $("[name='billCheck']:checked").attr("freeze");
			chargeCardNo=$("#chargeCardNo").val();
			usableMoney=$("#usableMoney").val();
			if(!isEmpty(payNo)){
				if(isEmpty(chargeCardNo)){
					showInfo("无充电卡信息！！");
				}else{
					goPayment(payNo,shouldMoney,chargeCardNo,usableMoney,badrecordId,freeze);
				}
			}else{
				showInfo("请选择付款记录！！");
			}
			/*
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
			}*/
		});
		
	$("#paymentModelBtn").on("click",function(){
		param={};
		param.chargeCardNo = $("#chargeCardNoModel").text();
		param.payNo = $("#badChargeIdModel").val();
		param.totalShouldMoney = $("#totalShouldMoneyModel").text();
		param.usableMoney = $("#usableMoneyModel").text();
		param.freeze = $("#freezeModel").text();//冻结金额
		param.badrecordId = $("#badrecordIdModel").text();
		password = $("#badPaymentPwd").val();
		
		if(parseFloat(param.freeze) < parseFloat(param.totalShouldMoney)){
			showInfo("灰记录["+param.payNo+"]冻结金额小于应付金额！！");
			return false;
		}
		//////////////实卡解灰/////////////////
		if(checkCardResult(unLockBadRecord(password,param.badrecordId,currentTime,param.totalShouldMoney))){
			$.ajax({
				type:"POST",
				url:cleanBadRecordUrl,
				data:param,
				dataType:'json',
		        cache: false,
		        success: function(data){
		        	$("#doRechargeBtn").attr("disabled",false);
		        	$("#badPaymentModal").modal("hide");
		        	if(data.message){
		        		if("success"==data.message){
		        			var opt={};
							opt.onOk = function(){
								chargeCardNo = $("#chargeCardNo").val();
								$("#badPaymentModal").modal("hide");
								if(isEmpty(tempPwd)){
									readRecord(tempPwd);
								}else{
									readRecord(defaultPwd);	
								}
								
								$("#readBadRecordBtn").click();//读灰记录
							}
							showInfo("扣款成功！！",opt);
						}else{
							var opt={};
							opt.onOk = function(){
								//badChargeIds="8888888888888888000100000000111100000000";//读取卡灰记录
								//freeze = tempObj.freeze;
								chargeCardNo = $("#chargeCardNo").val();
								$("#badPaymentModal").modal("hide");
								if(isEmpty(tempPwd)){
									readRecord(tempPwd);
								}else{
									readRecord(defaultPwd);	
								}
								
								/*readRecord();
								searchBadRecord(chargeCardNo);*/
							}
							showInfo(data.message,opt);
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
		
		//////////////////////////////

	});
	function readRecord(password){
		pwd = defaultPwd;
		if(!isEmpty(password)){
			pwd = password;
		}
		badArray = [];//异常灰
		okArray = [];//正常灰
		$("#readBadRecordModal").modal("hide");
		if(checkCardResult(getBadRecord(pwd))){//读取卡内灰色记录号
			tempPwd = pwd;
			tempObj = getReslutByType(operaType.getBadRecordCard);
			okStr = tempObj.okChargeStr;
			badStr = tempObj.badChargeStr;
			if(isEmpty(okStr) && isEmpty(badStr)){
				showInfo("无灰色记录！！")
				return false;
			}
			okStrs = okStr.split(";");
			badStrs = badStr.split(";");
			
			for(x = 0;x < okStrs.length;x++){
				tempObj={};
				tempStr = okStrs[x].split(",");
				tempObj.recordId = tempStr[0];//灰序号
				tempObj.freeze = tempStr[1];//冻结金额
				tempObj.payNo = tempStr[2];//灰记录号
				if(!isEmpty(tempObj.recordId)){
					okArray[x] = tempObj;
				}
			}
			for(y = 0;y < badStrs.length;y++){
				tempObj={};
				tempStr = badStrs[y].split(",");
				tempObj.recordId = tempStr[0];//灰序号
				tempObj.freeze = tempStr[1];//冻结金额
				tempObj.payNo = tempStr[2];//灰记录号
				if(!isEmpty(tempObj.recordId)){
					badArray[y] = tempObj;
				}
				
			}
			chargeCardNo = $("#chargeCardNo").val();
			if(isEmpty(chargeCardNo)){
				showInfo("卡号有误！！");
			}else if(okArray.length <= 0 && badArray.length <= 0){
				showInfo("无灰色记录！！");
			}else{
				searchBadRecord(chargeCardNo);
			}
		}
	}

	$("#readBadRecordBtn").on("click",function(){
		pwd = $("#readBadRecordPwd").val();
		readRecord(pwd);//读取灰记录
	});
	
	
	function searchBadRecord(chargeCardNo){
		if(okArray && okArray.length > 0){
			param.okArray = JSON.stringify(okArray);//正常灰
		}
		if(badArray && badArray.length > 0){
			param.badArray = JSON.stringify(badArray);//异常灰
		}
		//alert(param.badArray)
		//param.badChargeIds = badChargeIds;
		param.chargeCardNo = chargeCardNo;
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
	        				 html += '<td>'+ '<input onclick="subCheck(this)" name="billCheck" type="checkbox" payNo="'+item.payNo+'" shouldMoney="'+item.shouldMoney+'" badRecordId="'+item.badRecordId+'" freeze="'+item.freeze+'"/>'+'</td>';
	        				 html += '<td>'+item.badRecordId+'</td>';
	        				 html += '<td>'+item.payNo+'</td>';
	        				 html += '<td>'+item.freeze+'</td>';
	        				 /*html += getTdHtml(item.tradeNo, 2);*/
	        				 html += '<td>'+item.tradeNo+'</td>';
	            			 html += getTdHtml(item.stationName, 15);
	            			 html += getTdHtml(item.address, 15);
	            			 html += '<td>'+getNotNullData(item.pileName)+'</td>';
	            			 html += '<td>'+getNotNullData(item.phone)+'</td>';
	            			 html += '<td>'+getNotNullData(item.chaLen)+'</td>';
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
						showInfo(data.map.msg);
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
	$("#goBackBadListPageBtn").on("click",function(){
		 $("#CardInfoDiv").removeClass("hide");
		 $("#badChargeListDiv").addClass("hide");
	})
	//灰色记录end
	
	//初始化刷新按钮
	$("#refreshBtn").on("click",function(){
		result = checkCardInit();
		//cardOcx = document.getElementById("chargeCardOcx");//初始化OCX控件
		//alert(cardOcx.getUserInfo())
		//alert(cardOcx.cardNo)
		//alert(cardOcx.cardNo)
		if(result == SUCCESS){
			searchCardInfo($("#chargeCardNo").val(),READ_CARDINFO);
		}else if(result == ERROR){
			opt={};
			opt.onOk = function(){
				reload();
			}
			showConfirm("读卡信息失败请确定已安装插件!!", opt)
		}
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
		if(temp){
			$(this).val(temp.toFixed(2));	
		}
		
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
	$("#cancelRechargeBtn").on("click",function(){
		$("#CardInfoDiv").removeClass("hide");
		$("#rechargeCardDiv").addClass("hide");
		if(checkCardResult(getCardUsableMoney(defaultPwd),true)){
			$("#usableMoney").val(getReslutByType(operaType.getUsableMoneyCard).cardUsableMoney);
		}else{
			$("#usableMoney").val("未知");
		}
	});
}
