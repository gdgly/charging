/**
 * 个人提现
 */
	var param={};
	var userCashUrl = "userjson/transaction_userCash.action";
	var doUserCashUrl = "userjson/transaction_doUserCash.action";
	var cashMoney;
	var cashWay;
	var accountInfo;
	var msg;
	goUserMenu('userAccount');
	initExportBtn($("#exportBtn"),null,userCashUrl,null,"我的提现","searchDate");
$(function(){
	//提现start
	//resetForm($("#cashForm"));
	function cheackCashMoney(cashMoney,cashWay,accountInfo) {
		if (!cashMoney) {
			return "请输入金额！！";
		} else if (isNaN(cashMoney)) {
			return "请输入正确的金额！！";
		} else if (parseFloat(cashMoney) <= 0) {
			return "输入的金额必须大于0元！！";
		}else if(cashWay <= 0){
			return "请选择提现方式！！";
		}else if(isEmpty(accountInfo)){
			return "请输入提现账号！！";	
		}
		else {
			return "success";
		/* 	return confirm("确定提现金额为"
					+ Math.round(parseFloat(cashMoney * 100)) / 100 + "元？"); */
		}
	}
	function doCash(cashMoney, cashWay,accountInfo) {
		param.cashMoney = cashMoney;
		param.cashWay = cashWay;
		param.accountInfo = accountInfo;
		$.ajax({  
		      url:doUserCashUrl,// 跳转到 action  
		      data:param,
		      type:'post',  
		      cache:false,  
		      dataType:'json',  
		      beforeSend:function(){$("#loading").removeClass("hide");},
		      success:function(data) {
		    	  if(data.userLoginStatus){
		    		  checkLoginStatus(data,true);
		    		  return;
		    	  }
					if ("success" == data.message) {
						var opt={};
						opt.onOk = function(){
							location.reload();
						}
						opt.onClose = opt.onOk;
	                    showSuccess("提现成功！！", opt);
					} else {
						var opt={};
						opt.onOk = function(){
							$("#cashMoney").val("").focus();
						}
						opt.onClose = opt.onOk;
						showWarning(data.message,opt);
						
					}
				},  
		      complete:function(){
		    	  $("#loading").addClass("hide"); 
		      },
		      error : function() {  
		           showWarning("异常！");
		      }  
		 });
		
	}
	$("#cashMoney").on("keyup",function(){
	temp = $("#cashMoney").val();
	if(isNaN(temp) || isEmpty(temp)){
		return;
	}
	else{
		
		if(temp.indexOf(".") != -1){
			count = temp.substring(temp.indexOf(".")+1,temp.length);
			if(count.length > 1){
				$("#cashMoney").val(Math.round(parseFloat(temp * 100)) / 100);
			}
		}
		//$("#cashMoney").val(Math.round(parseFloat(temp * 100)) / 100);
	}
});
	$("#doAccountCashBtn").on("click", function() {
		cashMoney = $("#cashMoney").val();
		cashWay = $("#cashWay").val();
		accountInfo = $("#accountInfo").val();
		msg = cheackCashMoney(cashMoney,cashWay,accountInfo);
		if("success" == msg){
			cashMoney = Math.round(parseFloat(cashMoney * 100)) / 100;
			opt={};
			opt.onOk = function(){
				doCash(cashMoney, cashWay,accountInfo);
			}
			showConfirm("确定提现金额为"+ cashMoney + "元？",opt);
		}
		else{
			opt={};
			opt.onOk = function(){
				//$("#cashMoney").val("").focus();
			}
			opt.onClose = opt.onOk;
			showWarning(msg, opt);
		}
		
	});
	
	//提现end
	
	
	function initBody(){
		param.currentPage=$("#currentPage").val();
		param.searchDate = $("#searchDate").val().trim();
		var item = $("#userCashBody");
		$.ajax({  
		      url:userCashUrl,// 跳转到 action  
		      data:param,  
		      type:'post',  
		      cache:false,  
		      dataType:'json', 
		      beforeSend:function(){$("#loading").removeClass("hide");},
		      success:function(data){
		    	  if(data.userLoginStatus){
		    		  checkLoginStatus(data,false);
		    		  return;
		    	  }
					item.empty();
					if(data.page.root.length > 0){
						$("#help-block").remove();
					$(data.page.root).each(function(index,value){
						str = "<tr>"
							+"<td ><span style='color:coral;font-size:17px;'>"+value.moneyDesc+"</span></td>"
							+"<td><span>"+value.accountInfoDesc+"</span></td>"
							+"<td><span>"+value.cashWayDesc2+"</span></td>";
						team = parseInt(value.validStatus);
						if(team == 1){
							str=str+"<td><span style='color:red;'>"+value.validStatusDesc2+"</span></td>";
						}
						else if(team == 2){
							str=str+"<td><span style='color:green;'>"+value.validStatusDesc2+"</span></td>";
						}
						else if(team == 3){
							str=str+"<td><span style='color:#aaa;'>"+value.validStatusDesc2+"</span></td>";
						}
						else{
							str=str+"<td>"+value.validStatusDesc2+"</td>";
						}
						str = str
								+"<td><span title='"+value.validRemarkDesc+"'>"+mySubString(value.validRemarkDesc,10)+"</span></td>"
								+"<td>"+value.addTimeDesc+"</td>"
								+"</tr>"; 
						item.append(str);//封装table
					});
					     $("#pagingToolbar").show();
					}else{
						 $("#pagingToolbar").hide();
						 if(!$("#help-block").attr("id")){
							 item.parent().parent().append("<p id='help-block' class='help-block text-center' style='margin-top:10%;'>暂无记录</p>");
						 }
					}
					var stationTotalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
					setPagingToolbarParams(data.page.totalProperty,stationTotalPage,$("#currentPage").val());
				},  
		      complete:function(){
		    	  $("#loading").addClass("hide"); 
		      },
		      error : function() {  
		           showWarning("异常！");
		      }  
		 });
	};
	$("#searchBtn").on("click",function(){
		initBody();
	});
	initOneBootStrapDate2($('#datepicker'));
	initPagingToolbar(initBody);
	initBody();
})
