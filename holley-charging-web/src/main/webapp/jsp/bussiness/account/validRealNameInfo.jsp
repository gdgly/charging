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
		<div class="container main-body realNameInfoSize">
			<form class="form-horizontal" role="form" id="validRealNameInfoForm">
				<div class="row">
					<div class="col-sm-3">
						<h3>企业信息验证</h3>
					</div>
					<div class="col-sm-offset-7 col-sm-2 text-right"
						style="margin-top: 21px;">
						<button id="validInfoBtn" type="button" class="btn btn-warning btn-sm">提交</button>
					</div>
				</div>
				<hr />
				<!-- <p class="help-block"> 基本信息：</p> -->
				<%-- <div class="form-group">
      <label for="province" class="col-sm-3 control-label"><span style="color: red;">*</span>所在地区：</label>
      <div class="col-sm-4">
       <select id="province" class="form-control">
       <option value="0">请选择省</option>
    <s:iterator value="#request.provinceList" status="statu" id="item">
       <option value="<s:property value='id'/>"><s:property value="name"/> </option>
	</s:iterator> 
       </select>
      </div>
      
        <div class="col-sm-4">
       <select id="city" class="form-control">
			<option value="0">请先选择市区</option>
       </select>
      </div>
   </div> --%>


				<!-- <div class="form-group">
      <label for="tel" class="col-sm-3 control-label"><span style="color: red;">*</span>营业执照注册号：</label>
      <div class="col-sm-8">
           	<input type="text" class="form-control" id="tel" value=""/>
      </div>
   </div> -->
				<p class="help-block">对公账号信息：</p>

				<div class="form-group">

					<label for="busName" class="col-sm-3 control-label"><span
						style="color: red;">*</span>公司名称：</label>
					<div class="col-sm-8">
						<input disabled="disabled" type="text" class="form-control"
							id="busName" name="busName" value="${busBussinessReal.busName}" />
					</div>
				</div>
				<div class="form-group">
					<label for="accRealName" class="col-sm-3 control-label"><span
						style="color: red;">*</span>企业银行开户真实姓名：</label>
					<div class="col-sm-8">
						<input disabled="disabled" type="text" class="form-control"
							id="accRealName" name="accRealName"
							value="${busBussinessReal.accRealName}"/>
					</div>
				</div>
				<div class="form-group">
					<label for="bankName" class="col-sm-3 control-label"><span
						style="color: red;">*</span>对公账号开户行：</label>
					<div class="col-sm-8">
						<select disabled="disabled" class="form-control" id="bankName"
							name="bankName">
							<s:iterator value="#request.bankNameList" id="item"
								status="status">
								<s:if test="#request.busBussinessReal.bankName == value">
									<option value='<s:property value="value"/>'><s:property
											value="name" /></option>
								</s:if>
							</s:iterator>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label for="bankAccount" class="col-sm-3 control-label"><span
						style="color: red;">*</span>公司对公账号：</label>
					<div class="col-sm-8">
						<input disabled="disabled" type="text" class="form-control"
							id="bankAccount" name="bankAccount"
							value="${busBussinessReal.bankAccount}" />
					</div>
				</div>
				<div class="form-group">
					<label for="validCode" class="col-sm-3 control-label"><span
						style="color: red;">*</span>验证码：</label>
					<div class="col-sm-8">
						<input type="text" placeholder="请输入验证信息" class="form-control"
							id="validCode" name="validCode" maxlength="5"/>
					</div>


				</div>

			</form>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		var msg;
		var param = {};
		var validRealNameInfoByAjaxUrl = "bussiness_validRealNameInfoByAjax.action";
		var hrefUrl = "bussiness/safeInfo.action";
		resetForm($("#validRealNameInfoForm"));
		$("#validInfoBtn").on("click", function() {
			param.validCode = $("#validCode").val();
			if (!param.validCode) {
				var opt={};
				opt.onOk = function(){$("#validCode").val("").focus();}
				opt.onClose = opt.onOk;
				showWarning("请输入验证码！！",opt);
			} else {
				$.ajax({  
				      url:validRealNameInfoByAjaxUrl,// 跳转到 action  
				      data:param,  
				      type:'post',  
				      cache:false,  
				      dataType:'json',  
				      beforeSend:function(){$("#loading").removeClass("hide");},
				      success:function(data) {
							if ("success" == data.message) {
								var opt={};
								opt.onOk = function(){href(hrefUrl);}
								opt.onClose = opt.onOk;
								showSuccess("验证成功！！",opt);
								
							} else if ("outMaxCount" == data.message) {
								var opt={};
								opt.onOk = function(){href(hrefUrl);}
								opt.onClose = opt.onOk;
								showWarning("验证失败超过5次，请重新提交认证！！",opt);
							} else if ("error" == data.message) {
								var opt={};
								opt.onOk = function(){href(hrefUrl);}
								opt.onClose = opt.onOk;
								showError("系统异常请稍后再试！！",opt);
							} else {
								var opt={};
								opt.onOk = function(){$("#validCode").val("").focus();}
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

		});

	})
</script>
</html>

