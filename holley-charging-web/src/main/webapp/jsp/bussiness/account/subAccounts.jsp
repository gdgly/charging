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
	  <!--msg-content-->
	  <s:if test="#request.msg != null">
		<div class="container main-body"  style="height: 50%;">
			<p class="text-center" style="padding-top: 14%;">
			<strong>提示信息：</strong>${msg}
			<s:if test="#request.backUrl != null">
			<a href="<s:property value="#request.backUrl"/>">
			<s:if test="#request.retrunDec != null">
			<s:property value="#request.retrunDec"/>
			</s:if>
			<s:else>
			返回
			</s:else>
			</a>
			</s:if>
			<s:else>
			<a onclick="history.go(-1)" href="javascript::">返回</a>
			</s:else>
			</p>
		</div>
	</s:if>
	<s:else>
		<!--main-content-->
		<div class="container main-body" style="height: 55%;">
			<div class="row">
				<div class="col-sm-3">
					<h3>子账户信息</h3>
				</div>
				<div class="col-sm-offset-7 col-sm-2 text-right"
					style="margin-top: 13px;">
					<button id="addSubAccountBtn" type="button" class="btn btn-warning btn-sm">
						<span class="glyphicon glyphicon-plus"></span> 添加子账户
					</button>
				</div>
			</div>

			<!-- 子账户列表 -->
			<div id="subAccountDiv" style="height: 68%;">
				<div class="tableDivSize">
					<table class="table table-condensed" id="subAccountTable">
						<tbody>
							<s:iterator value="#request.subAccountList" status="statu"
								id="item">
								<tr>
									<td><s:if test="roleType == 2">
											<span class="glyphicon glyphicon-user" style="color: green;"></span> 用户名：<span><s:property
													value="userName" /></span>
										</s:if> <s:else>
   &nbsp;&nbsp;&nbsp;&nbsp;用户名：<span><s:property value="userName" /></span>
										</s:else></td>
									<td>手机号：<span><s:property value="phone" /></span>
									</td>
									<td><s:if test="isLock == 2">
											<span class="glyphicon glyphicon-ok" style="color: green;"></span> 启用
   </s:if> <s:else>
											<span class="glyphicon glyphicon-remove" style="color: red;"></span> 禁用
  </s:else></td>
									<s:if test="roleType != 2">
										<td class="text-right"><s:if test="isLock==2">
												<a title='禁用' id="disableSubAccount"
													onclick="editSubAccount(this);"
													userName='<s:property value="userName"/>'
													userId="<s:property value='userId'/>" href="javascript:"><span
													class="label label-danger">禁用</span></a>
											</s:if> <s:else>
												<a title='启用' id="enableSubAccount"
													onclick="editSubAccount(this);"
													userName='<s:property value="userName"/>'
													userId="<s:property value='userId'/>" href="javascript:"><span
													class="label label-success">启用</span></a>
											</s:else> &nbsp;&nbsp;&nbsp;<a title='修改' id="editSubAccount"
											onclick="editSubAccount(this);"
											userName='<s:property value="userName"/>'
											phone='<s:property value="phone"/>'
											userId="<s:property value='userId'/>" href="javascript:"><span
												class="label label-primary">修改</span></a></td>
									</s:if>
									<s:else>
										<td></td>
									</s:else>
								</tr>
							</s:iterator>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		</s:else>
	</div>
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="addSubAccountModal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">添加子账户</h4>
				</div>
				<div class="modal-body">

					<form class="form-horizontal" role="form" id="subAccountForm">
						<div class="form-group">
							<label for="userName" class="col-sm-2 control-label"><span
								style="color: red;">*</span>用户名：</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="userName" maxlength="20"/> <input
									type="hidden" class="form-control" id="requestType" /> <input
									type="hidden" class="form-control" id="userId" />
							</div>
						</div>
						<div class="form-group">
							<label for="phone" class="col-sm-2 control-label"><span
								style="color: red;">*</span>手机号：</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="phone" maxlength="20"/>
							</div>
						</div>

					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭
					</button>
					<button disabled="disabled" id="saveSubAccountBtn" type="button"
						class="btn btn-primary">提交</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal -->
	</div>
	<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
	<script type="text/javascript">
	setBowHeight1();
		var msg;
		var param = {};
		var editSubAccountUrl = "bussiness_editSubAccount.action";
		var addSubAccountUrl = "bussiness_addSubAccount.action";
		function editSubAccount(obj) {
			param = {};
			param.requestType = $(obj).attr("id");
			param.userId = $(obj).attr("userId");
			param.userName = $(obj).attr("userName");
			param.phone = $(obj).attr("phone");
			if ("disableSubAccount" == param.requestType
					|| "enableSubAccount" == param.requestType) {
				if ("disableSubAccount" == param.requestType) {
					msg = "是否要禁止用户'" + param.userName + "'";
				} else if ("enableSubAccount" == param.requestType) {
					msg = "是否要激活用户'" + param.userName + "'";
				}
				
				var opt={};
				opt.onOk = function(){
					$.post(editSubAccountUrl, param, function(data) {
						if ("success" == data.message) {
							href("bussiness/searchSubAccount.action");
						} else {
							showWarning(data.message);
						}
					});
				}
				showConfirm(msg,opt);
			} else if ("editSubAccount" == param.requestType) {
				$("#userName").val(param.userName);
				$("#phone").val(param.phone);
				$("#requestType").val(param.requestType);
				$("#userId").val(param.userId);
				$("#saveSubAccountBtn").attr("disabled", true);
				$("#addSubAccountModal").modal().css({
					'margin-top' : function() {
						return "15%";
					}
				});
			} else {
				showWarning("error");
			}
		}
		$(function() {
			function checkAddSubAccountInfo() {
				var userName = $("#userName").val();
				var phone = $("#phone").val();
				if (isEmpty(userName)) {
					msg = "请输入用户名！！"
				} else if (isEmpty(phone)) {
					msg = "请输入手机号码！！"
				} else if (!regBox.regMobile.test(phone)) {
					msg = "请正确填写11位手机号！！"
				} else {
					msg = "success";
				}
				return msg;

			}
			resetForm($("#subAccountForm"));
			$("#saveSubAccountBtn").attr("disabled", true);
			$("#addSubAccountBtn").on("click", function() {
				resetForm($("#subAccountForm"));
				$("#saveSubAccountBtn").attr("disabled", true);
				$("#requestType").val("");
				$("#addSubAccountModal").modal().css({
					'margin-top' : function() {
						return "15%";
					}
				});
			});

			$("#userName").on("keyup", function() {
				$("#saveSubAccountBtn").attr("disabled", false);
			});

			$("#phone").on("keyup", function() {
				$("#saveSubAccountBtn").attr("disabled", false);
			});
			$("#saveSubAccountBtn").on("click", function() {
				msg = checkAddSubAccountInfo();
				param = {};
				if ("success" == msg) {
					if ("editSubAccount" == $("#requestType").val()) {
						param.userName = $("#userName").val();
						param.phone = $("#phone").val();
						param.requestType = $("#requestType").val();
						param.userId = $("#userId").val();
						//提交修改
						$.post(editSubAccountUrl, param, function(data) {
							if ("success" == data.message) {
								href("bussiness/searchSubAccount.action");
							} else {
								showWarning(data.message);
							}
						});
					} else {
						param.userName = $("#userName").val();
						param.phone = $("#phone").val();
						$.post(addSubAccountUrl, param, function(data) {
							if ("success" == data.message) {
								var opt={};
								opt.onOk = function(){href("bussiness/searchSubAccount.action");}
								opt.onClose = opt.onOk;
								showSuccess("添加成功！！",opt);
								
							} else {
								showWarning(data.message);
							}
						});
					}
				} else {
					showWarning(msg);
				}
			});
		});
	</script>
</body>
</html>

