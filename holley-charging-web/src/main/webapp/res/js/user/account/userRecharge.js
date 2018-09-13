/**
 * 个人收藏
 */
	var param={};
	var userRechargeUrl = "userjson/account_userRecharge.action";
	var maxRechargeMoney = 50000.0;
	initExportBtn($("#exportBtn"),null,userRechargeUrl,null,"我的充值","searchDate");
	goUserMenu('userAccount');

	$(function(){
		function initBody(){
			param.currentPage = $("#currentPage").val();
			param.searchDate = $("#searchDate").val();
			item = $("#userRechargeBody");
			$.ajax({  
			      url:userRechargeUrl,// 跳转到 action  
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
								+"<td style='color:red;'>"+value.money.toFixed(2)+"</td>"
								+"<td>"+value.payWayDesc+"</td>";
								temp = value.status;
							if(1 == temp){//操作中
								str = str + "<td style='color:#FF8247;'>"+value.statusDesc+"</td>";
							}else if(2 == temp){//成功
								str = str + "<td style='color:#9ACD32;'>"+value.statusDesc+"</td>";
							}else{//失败
								str = str + "<td style='color:red;'>"+value.statusDesc+"</td>";
							}
								str = str 
									+"<td>"+value.tradeNo+"</td>"
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
			$("#currentPage").val(1);
			initBody();
		});
		initOneBootStrapDate2($('#datepicker'));
		initPagingToolbar(initBody);
		initBody();
		
		/////////////////////////////充值//////////////////////////////////
		function cheackRechargeMoney(rechargeMoney) {
			if (!rechargeMoney) {
				return "请输入金额！！";
			} else if (isNaN(rechargeMoney)) {
				return "请输入正确的金额！！";
			} else if (parseFloat(rechargeMoney) <= 0) {
				return "输入的金额必须大于0元！！";
			}else if(parseFloat(rechargeMoney) > maxRechargeMoney){
				return "输入的金额不能大于"+maxRechargeMoney+"元！！";
			}else {
				return "success";
			/* 	return confirm("确定提现金额为"
						+ Math.round(parseFloat(cashMoney * 100)) / 100 + "元？"); */
			}
		}
		
		$("#rechargeMoney").on("keyup",function(){
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
						$("#rechargeMoney").val(Math.round(parseFloat(temp * 100)) / 100);
					}
				}
				//$("#cashMoney").val(Math.round(parseFloat(temp * 100)) / 100);
			}
		});
		$("#doRechargeBtn").on("click",function(){
			rechargeMoney = $("#rechargeMoney").val();
			rechargeWay = $("#rechargeWay").val();
			msg = cheackRechargeMoney(rechargeMoney);
			if("success" == msg){
				rechargeMoney = Math.round(parseFloat(rechargeMoney * 100)) / 100;
				opt={};
				opt.onOk = function(){
					if(2 == rechargeWay){
						frameHref("userAccount/aliPay.action"+"?rechargeWay="+rechargeWay+"&rechargeMoney="+rechargeMoney);	
					}else if(3 == rechargeWay){
						href("userAccount/wechatPay.action"+"?rechargeWay="+rechargeWay+"&rechargeMoney="+rechargeMoney);
					}else{
						showWarning("请选择支付方式！！");
					}
					
					//$("#rechargeForm").submit();
				}
				showConfirm("确定充值金额"+ rechargeMoney + "元？",opt);
				
			}else{
				opt={};
				opt.onOk = function(){$("#rechargeMoney").val("").focus();}
				opt.onClose = opt.onOk;
				showWarning(msg,opt);
			}
			
		});
		/////////////////////////////充值//////////////////////////////////
	})
	
