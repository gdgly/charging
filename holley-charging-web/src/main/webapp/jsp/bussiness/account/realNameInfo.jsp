<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<%@include file="../../common/global/meta.jsp"%>
<%@include file="../../common/global/js.jsp"%>
<%@include file="../head.jsp"%>
<html lang="zh-CN">
<head>
<style type="text/css">
p{
font-size: 12px;
line-height: 20px;
}
strong{
font-size: 15px;
}

</style>
</head>
<body>
	<div class="bg">
		<!--main-content-->
		<div class="container main-body realNameInfoSize">
			<form class="form-horizontal" role="form" id="realNameInfoForm">
				<div class="row">
					<div class="col-sm-2">
						<h3>企业信息</h3>
					</div>
					<div class="col-sm-offset-8 col-sm-2 text-right"
						style="margin-top: 21px;">
						<button id="saveInfoBtn" type="button" class="btn btn-warning btn-sm">提交</button>
					</div>
				</div>
				<hr />
				 <p class="help-block">基本信息：</p>
				<div class="form-group">
					<label for="busName" class="col-sm-2 control-label"><span
						style="color: red;">*</span>公司名称：</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="busName"
							name="busName" placeholder="公司名称" maxlength="20"/>
					</div>
					<label for="accRealName" class="col-sm-2 control-label"><span
						style="color: red;">*</span>企业银行开户名：</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="accRealName"
							name="accRealName" placeholder="企业银行开户名" maxlength="20"/>
					</div>
				</div>
				<div class="form-group">
					<label for="bankName" class="col-sm-2 control-label"><span
						style="color: red;">*</span>开户银行：</label>
					<div class="col-sm-4">
						<select class="form-control" id="bankName" name="bankName">
							<option value="0">请选择</option>
							<s:iterator value="#request.bankNameList" status="status"
								id="item">
								<option value=<s:property value='value'/>><s:property
										value='name' /></option>
							</s:iterator>
						</select>
					</div>
					<label for="bankAccount" class="col-sm-2 control-label"><span
						style="color: red;">*</span>公司对公账号：</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="bankAccount"
							name="bankAccount" placeholder="公司对公账号" maxlength="30"/>
					</div>

				</div>
				<hr class="dashed">
 			<p class="help-block">图片信息：</p>
 				<div class="form-group">
					<label for="corporateImg" class="col-sm-2 control-label"><span
						style="color: red;">*</span>法人身份证件照：</label>
					<div class="col-sm-4">
						<img id="showCorporateImg" alt="Image preview" class="img-thumbnail" style="height: 20%; width: 50%;" src="${imgUrl}data/stationImg/stationDefault.jpg" /> 
							<input type="file" name="corporateImg" id="corporateImg" />
					</div>
					<label for="transatorImg" class="col-sm-2 control-label"><span
						style="color: red;">*</span>操作人身份证件照：</label>
					<div class="col-sm-4">
						<img id="showTransatorImg" alt="Image preview" class="img-thumbnail" style="height: 20%; width: 50%;" src="${imgUrl}data/stationImg/stationDefault.jpg" /> 
							<input type="file" name="transatorImg" id="transatorImg" />
					</div>
					
				</div>
					<div class="form-group">
					<label for="licenceImg" class="col-sm-2 control-label"><span
						style="color: red;">*</span>运营证件照：</label>
					<div class="col-sm-4">
						<img id="showLicenceImg" alt="Image preview" class="img-thumbnail" style="height: 20%; width: 50%;" src="${imgUrl}data/stationImg/stationDefault.jpg" /> 
							<input type="file" name="licenceImg" id="licenceImg" />
					</div>
					</div>
				<div class="checkbox col-sm-offset-3 col-sm-9">
					<label> <input checked="checked" type="checkbox"
						id="agreementCheckbox"> 已认证同意认证服务协议<a href="javascript::" id="agreement">点击查看认证服务协议</a>
					</label>
				</div>

			</form>
		</div>
	</div>
	
		<!-- 模态框（Modal） -->
	<div class="modal fade" id="agreementMsgModel" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h5 class="modal-title" id="myModalLabel">隐私条款</h5>
				</div>
				<div class="modal-body">
                <h3 class="text-center">51充电网站隐私条款 </h3>
                <h6 class="text-center">最后更新时间：2016年07月01日</h6>
					<blockquote>
					<p>
					以下是本网站（www.ycharge.cn）及其运营公司杭州市51充电网络有限公司（以下合称“本网站”）的隐私规则
					</p>
					<strong>1. 用户身份限制</strong>
					<p>
					未成年人（年龄18周岁以下人士）、限制行为能力人、中国大陆以外（不包括香港特区、澳门特区及台湾地区）地区人士及自然人以外的组织无资格注册成为本网站用户并使用本网站的服务，本网站要求未成年人、中国大陆以外人士及各类组织不要向本网站提交任何个人资料。
					</p>
					<strong>2. 密码的安全性</strong>
					<p>
					您须对使用您的用户名和密码所采取的一切行为负责。因此，本网站建议您不要向任何第三方披露您在本网站的用户名和密码。
					</p>
					<strong>3. 规则修改</strong>
					<p>
					本网站可能不时按照您的意见和本网站的需要修改本隐私规则，以准确地反映本网站的资料收集及披露惯例。本规则的所有修改，在本网站于拟定生效日期前至少三十(30)日在网站公布有关修改通知。
					</p>
					</blockquote>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭
					</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal -->
	</div>	
	
	
</body>
<script type="text/javascript">
	$(function() {
		var msg;
		var param = {};
		var saveRealNameInfoByAjaxUrl = "bussiness_saveRealNameInfoByAjax.action";
		var hrefUrl = "bussiness/safeInfo.action";
		resetForm($("#realNameInfoForm"));
		function checkRealNameInfo(realNameInfo) {
			msg = "success";
			if (isEmpty(realNameInfo.busName)) {
				msg = "请填写公司名称！！";
			} else if (isEmpty(realNameInfo.accRealName)) {
				msg = "请填写企业银行开户名！！";
			} else if (!(parseInt(realNameInfo.bankName) > 0)) {
				msg = "请选择对公账号开户行！！";
			} else if (isEmpty(realNameInfo.bankAccount)) {
				msg = "请填写公司对公账号！！";
			} else if(isEmpty($("#corporateImg").val())){
				msg = "请上传法人身份证证件照！！";
			}else if(isEmpty($("#transatorImg").val())){
				msg = "请上传操作人身份证证件照！！";
			}else if(isEmpty($("#licenceImg").val())){
				msg = "请上传企业运营证件照！！";
			}else if (!$("#agreementCheckbox").is(':checked')) {
				msg = "请阅读认证服务协议！！";
			}
			return msg;
		}

		$("#saveInfoBtn").on("click",
						function() {
							var $realNameInfoForm = $("#realNameInfoForm");
							var realNameInfobean = getFormJson($realNameInfoForm);
							var resultMsg = checkRealNameInfo(realNameInfobean);
							if (resultMsg == "success") {
								param.realNameInfoJson = formDataToJsonString($realNameInfoForm);
								$('#realNameInfoForm').ajaxSubmit({
									url:saveRealNameInfoByAjaxUrl,
									type:'post',
									dataType:'json',
									beforeSubmit:function(){$("#loading").removeClass("hide")},
									data:param,
									success:function(data){
										$("#loading").addClass("hide");
										if ("success" == data.message) {
											//alert("申请成功，等待审核！！");
											var opt={};
											opt.onOk = function(){href(hrefUrl);}
											opt.onClose = opt.onOk;
											showSuccess("申请成功，等待审核！！",opt);
										} else if ("error" == data.message) {
											href(hrefUrl);
										} else {
											showWarning(data.message);
										}	
									}
									
								});
							} else {
								showWarning(resultMsg);
							}

						});
		commomChangeImg("corporateImg","showCorporateImg");
		commomChangeImg("transatorImg","showTransatorImg");
		commomChangeImg("licenceImg","showLicenceImg");
$("#agreement").on("click",function(){
	$("#agreementMsgModel").modal();
});
$("#bankAccount").on("change",function(){
	temp = $(this).val();
	if(!regBox.regNum.test(temp)){
		opt={};
		opt.onOk = function(){$("#bankAccount").val("").focus();}
		opt.onClose = opt.onOk;
		showWarning("请输入正确的账户！！",opt);
		
	}
});
	})
</script>
</html>

