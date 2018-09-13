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
	<!--main-content-->
	<div class="bg">
		<div class="container main-body" style="height: 45%;">
			<div class="row">
				<div class="col-sm-2">
					<h3>密码修改</h3>
				</div>
				<div class="col-sm-offset-8 col-sm-2 text-right"
					style="margin-top: 21px;">
					<button id="changePwdBtn" type="button" class="btn btn-warning btn-sm">确定</button>
				</div>
			</div>

			<hr />
			<form class="form-horizontal" role="form">
				<div class="form-group">
					<label for="oldPwd" class="col-sm-2 control-label">旧密码</label>
					<div class="col-sm-10">
						<input type="password" class="form-control" id="oldPwd"
							placeholder="请输入旧密码" maxlength="30"/> <input type="hidden" id="actionType"
							name="actionType" value="${actionType}" />
					</div>
				</div>
				<div class="form-group">
					<label for="newPwd" class="col-sm-2 control-label">新密码</label>
					<div class="col-sm-10">
						<input type="password" class="form-control" id="newPwd"
							placeholder="请输入新密码" maxlength="30"/>
					</div>
				</div>
				<div class="form-group">
					<label for="checkNewPwd" class="col-sm-2 control-label">重新输入新密码</label>
					<div class="col-sm-10">
						<input type="password" class="form-control" id="checkNewPwd"
							placeholder="请重新输入新密码" maxlength="30"/>
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
	var doChangePwdUrl = "bussiness_doChangePwd.action";//修改密码接口
	var url="user/userlogin_init.action";
	$("#changePwdBtn").on("click",function(){
		param.oldPwd = $("#oldPwd").val();
		param.newPwd = $("#newPwd").val();
		param.checkNewPwd = $("#checkNewPwd").val();
		param.actionType = $("#actionType").val();
		if(param.newPwd != param.checkNewPwd){
			showWarning("密码重复输入不一致！！");
		}
		else{
			$.ajax({  
			      url:doChangePwdUrl,// 跳转到 action  
			      data:param,  
			      type:'post',  
			      cache:false,  
			      dataType:'json',  
			      beforeSend:function(){$("#loading").removeClass("hide");},
			      success:function(data){
						if("success"==data.message){
							//if(param.actionType == "changePwd"){
								var opt={};
								opt.onOk = function(){
									href(url);
								}
								opt.onClose = opt.onOk;
								showSuccess("修改密码成功,请重新登录！！", opt);
							//}
						}
						else if("fail"==data.message || "timeOut"==data.message){
							var opt={};
							opt.onOk = function(){
								href(url);
							}
							opt.onClose = opt.onOk;
							showWarning("修改密码超时，请重新修改！！", opt);
						}
						
						else{
							showWarning(data.message);
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
	});
})
</script>
</html>

