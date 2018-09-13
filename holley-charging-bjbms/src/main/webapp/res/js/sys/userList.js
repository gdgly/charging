var param={};
var maxRechargeMoney = 100000;
$(document).ready(function(){
	initUserParams();
	queryUserList();
	$("#rechargeMoney").on("keyup",function(){
		temp = $(this).val();
		if(isNaN(temp) || isEmpty(temp)){
			$(this).val("").focus();
			return;
		}else if(temp > maxRechargeMoney){
			$(this).val("").focus();
			return;
		}else{
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
		userId = $("#userId").val();
		msg = cheackRechargeMoney(rechargeMoney,userId);
		if("success" == msg){
			opt={};
			opt.onOk = function(){
				doRecharge(rechargeMoney,userId);
			}
			showConfirm("确定充值金额"+ rechargeMoney + "元？",opt);
		}else{
			showInfo(msg);
		}
	})
	/**
	 * 修改用户密码
	 */
	$("#modifyPwdModal2Btn").on("click",function(){
		userId = $("#userIdForModifyPwd").val();
		pwd = $("#newpwd2").val().trim();
		repeatpwd = $("#repeatpwd2").val().trim();
		if(isEmpty(pwd) || pwd.length < 6){
			showInfo("输入的密码不能小于6个字符。");
			return false;
		}else if(pwd != repeatpwd){
			showInfo("2次输入的密码不一致。");
			return false;
		}
		$("#modifyPwdModal2").modal('hide');
		if(userId > 0){
			param={};
			param.userId = userId;
			param.password = pwd;
			param.repeatpwd = repeatpwd;
			$.ajax({
				type:"POST",
				url:'sys/user_modifyPassword.action',
				data:param,
				dataType:'json',
		        cache: false,
		        beforeSend:function(){$("#loading").removeClass("hide");},
		        complete:function(){
			    	  $("#loading").addClass("hide"); 
			      },
		        success: function(data){
		           if(data.message = "success"){
		        	   showInfo("修改成功！！");
		           }else{
		        	   showMsg(data.message, data.errormsg);
		           }
		         }
		     });
		}
	});
});

function showmodifyPwd(obj){
	uid = $(obj).attr("uid");
	uname = $(obj).attr("uname");
	if(uid > 0){
		$("#userIdForModifyPwd").val(uid);
		tem = "用户'"+uname+"'密码修改"
		$("#modifyPwdModal2Label").text(tem)
		$("#modifyPwdModal2").modal();
	}else{
		showInfo("请选择用户！");
	}
}
function doRecharge(rechargeMoney,userId){
	param={};
	param.userId = userId;
	param.money = rechargeMoney;
	$.ajax({
		type:"POST",
		url:'deal/account_doRechargeBj.action',
		data:param,
		dataType:'json',
        cache: false,
        beforeSend:function(){$("#loading").removeClass("hide");},
        complete:function(){
	    	  $("#loading").addClass("hide"); 
	      },
        success: function(data){
           if(data.message = "success"){
        	   showInfo("账户充值成功！！");
        	   searchAccountById(userId);
           }else{
        	   showMsg(data.message, data.errormsg);
           }
         }
     });
}
/////////////////////////////充值//////////////////////////////////
function cheackRechargeMoney(rechargeMoney,userId) {
	if(!userId || userId <= 0){
		return "请选择用户！！";
	}else if (!rechargeMoney) {
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
/**
 * 用户账户充值跳转
 * @param obj
 */
function recharge(obj){
	userId = $(obj).attr("userId");
	if(userId > 0){
		$("#userId").val(userId);
		$("#username").text( $(obj).attr("username"));
		searchAccountById(userId);
	}else{
		showInfo("请选择用户！！");
	}
	
}

function searchAccountById(userId){
	param={};
	param.userId = userId;
	$.ajax({
		type:"POST",
		url:'deal/account_queryUserAccount.action',
		data:param,
		dataType:'json',
        cache: false,
        beforeSend:function(){$("#loading").removeClass("hide");},
        complete:function(){
	    	  $("#loading").addClass("hide"); 
	      },
        success: function(data){
           if(data.message = "success"){
        	   $("#usableMoney").text(data.account.usableMoney);
        	   $("#rechargeMoney").val("0.00");
        	   if($("#userRechargeContainer").hasClass("hide")){
       			$("#userRechargeContainer").removeClass("hide");
       		}
       		if(!$("#userListContainer").hasClass("hide")){
       			$("#userListContainer").addClass("hide");
       		}
           }else{
        	   showMsg(data.message, data.errormsg);
           }
         }
     });
}

function returnUserList(){
	if(!$("#userRechargeContainer").hasClass("hide")){
		$("#userRechargeContainer").addClass("hide");
	}
	if($("#userListContainer").hasClass("hide")){
		$("#userListContainer").removeClass("hide");
	}
	$("#rechargeUserId").val(0);
	$("#queryBtn").click()
}

function initUserParams(){
	initPagingToolbar(queryUserList);
	//初始化查询按钮
	$("#queryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryUserList();
	});
	$("#userType").on("change",function(){
		if($("#userType").val() == PERSION){//个人
			$("#nameAndRealName").text("用户姓名");
			$("#keyword").attr("placeholder","请输入用户姓名/手机号码");
			$("#groupId").val(0)
			$("#groupId").removeClass("hide");
		}else{
			$("#nameAndRealName").text("用户昵称");
			$("#keyword").attr("placeholder","请输入用户昵称/手机号码");
			$("#groupId").val(0)
			$("#groupId").addClass("hide");
		}
		
		$("#queryBtn").click();
	});
	$("#groupId").on("change",function(){
		$("#queryBtn").click();
	});
	//初始化导出按钮
	initExportBtn($("#exportBtn"),$("#conditionFrom"),$("#fileName").val(),$("#userTable"));
	bindKey13([$("#keyword")],$("#queryBtn"));//回车自动搜索
}

function queryUserList(){
	param={};
	param.keyword = $.trim($("#keyword").val());
	param.usertype = $('#userType option:selected').val();
	param.realstatus = $('#realStatus option:selected').val();
	param.lockstatus = $('#lockStatus option:selected').val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	param.groupId = $("#groupId").val();
	param.tm = new Date().getTime();
	
	var tbody = $("#userTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'sys/user_queryList.action',
		data:param,
		dataType:'json',
        cache: false,
        beforeSend:function(){$("#loading").removeClass("hide");},
        complete:function(){
	    	  $("#loading").addClass("hide"); 
	      },
        success: function(data,options){
             if(data.success){
            	 if(data.page){
            		 tbody.empty();
            		 var dataList = data.page.root;
            		 $(dataList).each(function(index,item){
            			 html += '<tr>';
            			/* html += '<td>'+item.id+'</td>';*/
            			 if(item.userType == 3){//个人
            				 html += getTdHtml(item.realName, 15); 
            			 }else{
            				 html += getTdHtml(item.username, 15);
            			 }
            			 html += '<td>'+getNotNullData(item.phone)+'</td>';
            	/*		 html += '<td>'+getNotNullData(item.realStatusDesc)+'</td>';*/
            			 html += '<td>'+getNotNullData(item.userTypeDesc)+'</td>';
            			 html += '<td>'+item.rolename+'</td>';
            			 /*if(item.userType == PERSION){
            				 html += '<td>'+item.usableMoney+'</td>'; 
            			 }*/
            			 
            			 html += '<td>'+getNotNullData(item.registTimeStr)+'</td>';
            			 html += '<td>';
            			 var params = item.id+",'"+item.username+"','"+item.isLock+"'";
            			 
            		//	 if((item.userType == PLATFORM || item.userType == COMPANY)){
            				 if(item.id != ADMIN_USER_ID){
            				 if(item.isLock == 2 && item.groupId != -1){
            					 html += '<a href="sys/editUserInit.action?userid='+item.id+'" data-toggle="tooltip" title="修改"><span class="glyphicon glyphicon-edit"></span></a>'; 
            					 html += ' | ';
            					 if(item.userType == PERSION){
            						 html += '<a data-toggle="tooltip" title="禁用" class="a-disable"><span class="glyphicon glyphicon-remove"></span></a>';
                					 html += ' | ';
                					 html += '<a data-toggle="tooltip" title="密码初始化" class="a-disable"><span class="glyphicon glyphicon-lock"></span></a>';
            					 }
            					 else{
            					 html += '<a data-toggle="tooltip" title="禁用" onclick="showDelUserBox('+params+')"><span class="glyphicon glyphicon-remove" style="color:red;cursor: pointer;"></span></a>';
            					 html += ' | ';
            					 if(item.userType == COMPANY){
            						 html += '<a data-toggle="tooltip" title="密码修改" uname="'+item.username+'" uid="'+item.id+'" onclick="showmodifyPwd(this);"><span class="glyphicon glyphicon-lock"></span></a>';
            					 }else{
            						 html += '<a data-toggle="tooltip" title="密码初始化" onclick="showInitPwdBox('+params+')"><span class="glyphicon glyphicon-lock"></span></a>';	 
            					 }
            					 }
            				 }else if(item.isLock == 1 && item.groupId != -1){
            					 html += '<a data-toggle="tooltip" title="修改" class="a-disable"><span class="glyphicon glyphicon-edit"></span></a>';
            					 html += ' | ';
            					 html += '<a data-toggle="tooltip" title="启用" onclick="showDelUserBox('+params+')"><span class="glyphicon glyphicon-ok" style="color:green;cursor: pointer;"></span></a>';
            					 html += ' | ';
            					 html += '<a data-toggle="tooltip" title="密码初始化" class="a-disable"><span class="glyphicon glyphicon-lock"></span></a>'; 
            				 }else if(item.groupId == -1){
            					 html += '<a data-toggle="tooltip" title="修改" class="a-disable"><span class="glyphicon glyphicon-edit"></span></a>';
            					 html += ' | ';
            					 html += '<a data-toggle="tooltip" title="启用" class="a-disable"><span class="glyphicon glyphicon-ok"></span></a>';
            					 html += ' | ';
            					 html += '<a data-toggle="tooltip" title="密码初始化" class="a-disable"><span class="glyphicon glyphicon-lock"></span></a>'; 
            				 }
        					
            			 }
            			 else {
            				 html += '<a data-toggle="tooltip" title="修改" class="a-disable"><span class="glyphicon glyphicon-edit"></span></a>';
        					 html += ' | ';
        					 html += '<a data-toggle="tooltip" title="禁用" class="a-disable"><span class="glyphicon glyphicon-ok"></span></a>';
        					 html += ' | ';
        					 html += '<a data-toggle="tooltip" title="密码初始化" class="a-disable"><span class="glyphicon glyphicon-lock"></span></a>';
            			 }
            			 html += ' | ';
            			 html += '<a href="sys/userDetailInit.action?userid='+item.id+'&usertype='+item.userType+'" data-toggle="tooltip" title="详细"><span class="glyphicon glyphicon-list-alt"></span></a>';
            			/*if(item.userType == PERSION){
            				html += ' | ';
            				html += '<a onclick="recharge(this);" href="javascript:;" userId="'+item.id+'" username="'+item.username+'" data-toggle="tooltip" title="账户充值"><span class="glyphicon glyphicon-usd"></span></a>'; 
            			}*/
            			 html += '</td>';
            			 html += '</tr>';
            		 });
            		 tbody.html(html);
            		 
            		 var currentPage = data.page.startRow/(data.page.endRow-data.page.startRow)+1;
            		 var totalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
            		 setPagingToolbarParams(data.page.totalProperty, totalPage,currentPage);
            	 }
             }else{
            	 showMsg(data.message, data.errormsg);
             }
         }
     });
}

//新增按钮点击事件
$("#addBtn").on("click",function(){
	addUser();
});

function addUser(){
	href("sys/addUserInit.action");
}

function showInitPwdBox(id,username){
	var option = {
			title: "系统提示",
			btn: window.wxc.xcConfirm.btnEnum.okcancel,
			onOk: function(){
				initPwd(id,username);
			}
		}
	showCustom("确定初始化用户'"+username+"'的密码吗?",option);
}

function initPwd(id,username){
	if (id == null) {
		showInfo("参数非法.");
		return false;
	}
	param={};
	param.userid = id;
	$.ajax({
		type:"POST",
		url:'sys/user_initPassword.action',
		data:param,
		dataType:'json',
        cache: false,
        beforeSend:function(){$("#loading").removeClass("hide");},
        complete:function(){
	    	  $("#loading").addClass("hide"); 
	      },
        success: function(data,options){
             if(data.success){
            	 showSuccess("用户'"+username+"'密码初始化成功.");
             }else{
            	 showMsg(data.message, data.errormsg);
             }
         }
     });
}

function showDelUserBox(id,username,isLock){
	var option = {
			title: "系统提示",
			btn: window.wxc.xcConfirm.btnEnum.okcancel,
			onOk: function(){
				delUser(id,username,isLock);
			}
		}
	if(isLock == 2){//去禁用
		showCustom("用户禁用后不能正常登录系统,确定禁用用户'"+username+"'吗?",option);
	}else if(isLock == 1){//去激活
		showCustom("用户激活能正常登录系统,确定激活用户'"+username+"'吗?",option);
	}
	
}

function delUser(id,username,isLock){
	if (id == null) {
		showInfo("参数非法.");
		return false;
	}
	param.userid = id;
	param.isLock = isLock;
	$.ajax({
		type:"POST",
		url:'sys/user_delUser.action',
		data:param,
		dataType:'json',
        cache: false,
        beforeSend:function(){$("#loading").removeClass("hide");},
        complete:function(){
	    	  $("#loading").addClass("hide"); 
	      },
        success: function(data,options){
             if(data.success){
            	 queryUserList();
             }else{
            	 showMsg(data.message, data.errormsg);
             }
         }
     });
}
