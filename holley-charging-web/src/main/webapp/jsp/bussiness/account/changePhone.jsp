<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<%@include file="../../common/global/meta.jsp"%>
<%@include file="../../common/global/js.jsp"%>
<%@include file="../head.jsp"%>
<html lang="zh-CN">
<head>
<style type="text/css">
</style>
</head>
<body>
	<div class="bg">
		<!--main-content-->
		<div class="container main-body" style="height: 45%;">
			<div class="row">
				<div class="col-sm-2">
					<h3>手机修改</h3>
				</div>
				<div class="col-sm-offset-8 col-sm-2 text-right"
					style="margin-top: 21px;">
					<button id="changePhoneBtn" type="button" class="btn btn-warning btn-sm">确定</button>
				</div>
			</div>
			<hr>
			<form class="form-horizontal" role="form">

				<div class="form-group">
					<label for="phone" class="col-sm-2 control-label">新手机号码</label>
					<div class="col-sm-5">
						<input type="hidden" id="actionType" name="actionType"
							value="${actionType}" /> <input type="text" class="form-control"
							id="phone" placeholder="请输入新手机号码" maxlength="20"/>
					</div>
					
				</div>
				<div class="form-group">
					<label for="phoneCode" class="col-sm-2 control-label">验证码</label>
					<div class="col-sm-5">
						<input type="text" placeholder="请输入验证码" class="form-control" id="phoneCode" maxlength="4"/>
					</div>
					<div class="col-sm-1">
						<input type="button" class="btn btn-warning btn-sm" id="phoneCodeBtn"
							value="免费获取验证码" />
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
<script type="text/javascript">
setBowHeight1();
$(function(){
	var param={};
	var countdown=60; 
	var doChangePhoneUrl = "bussiness_doChangePhone.action";//修改手机接口
	var getPhoneCodeUrl = "bussiness_phoneCodeForChangePhone.action";//获取短信验证码接口
	var hrefUrl="bussiness/safeInfo.action";
	$("#phone").removeAttr("disabled").val("");//初始化参数
	$("#phoneCodeBtn").removeAttr("disabled");//初始化发送邮箱验证码
	$("#phoneCode").val("");
	//计时方法
	function settime(obj) { 
	    if (countdown == 0) { 
	        obj.removeAttr("disabled");    
	        obj.val("免费获取验证码"); 
	        countdown = 60; 
	        return;
	    } else { 
	        obj.attr("disabled", true); 
	        ss = "重新发送(" + countdown + ")";
	        obj.val(ss); 
	        countdown--; 
	    } 
	setTimeout(function() { 
	    settime(obj) }
	    ,1000) 
	}
	$("#changePhoneBtn").on("click",function(){
		param.phone = $("#phone").val();
		param.phoneCode = $("#phoneCode").val();
		param.actionType = $("#actionType").val();
		$.ajax({  
		      url:doChangePhoneUrl,// 跳转到 action  
		      data:param,  
		      type:'post',  
		      cache:false,  
		      dataType:'json',  
		      beforeSend:function(){$("#loading").removeClass("hide");},
		      success:function(data){
					if(data.message == "success"){
						var opt={};
						opt.onOk = function(){
							href(hrefUrl);	
						}
						opt.onClose = opt.onOk;
						showSuccess("修改手机成功！！", opt);
					}
					else if(data.message == "error"){
						var opt={};
						opt.onOk = function(){
							href(hrefUrl);	
						}
						opt.onClose = opt.onOk;
						showError("修改手机失败，请重新修改！！", opt);
					}
					else{
						var opt={};
						opt.onOk = function(){
							$("#phoneCode").val("").focus();
						}
						opt.onClose = opt.onOk;
						showWarning(data.message, opt);
					}
				},  
		      complete:function(){
		    	  $("#loading").addClass("hide"); 
		      },
		      error : function() {  
		           showWarning("异常！");
		      }  
		 });
		
	});
	//获取手机动态验证码
	$("#phoneCodeBtn").on("click",function(){
	 	param.phone = $("#phone").val();
		if(!param.phone && param.phone.length < 1){
			showWarning("请填写手机号码！！");
		}
		else if(!regBox.regMobile.test(param.phone)){
			showWarning("请正确填写手机号码！！");
		}
		else{
	 	$.post(getPhoneCodeUrl,param,function(data){
	 		if("success" != data.message){
	 			showWarning(data.message);
	 		}
	 		else{
	 			$("#phone").attr("disabled",true);
				 settime($("#phoneCodeBtn"));
	 		}
	 		
		}); 
	}
	});
	


})
</script>
</html>

