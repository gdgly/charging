<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<%@include file="../../common/global/meta.jsp"%>
<%@include file="../../common/global/js.jsp"%>
<%@include file="../head.jsp"%>
<html lang="zh-CN">
<head>
</head>
<body>
	<div class="bg">
		<!--main-content-->
		<div class="container main-body">
			<div class="row">
				<div class="col-sm-2">
					<h3>基本资料</h3>
				</div>
				<div class="col-sm-offset-8 col-sm-2 text-right"
					style="margin-top: 21px;">
					<button disabled="disabled" id="saveInfoBtn" type="button"
						class="btn btn-warning btn-sm">
						<span class="glyphicon glyphicon-floppy-disk"></span> 保存
					</button>
				</div>
			</div>
			<hr />
			<form class="form-horizontal" role="form" id="form1">
				<h5>基本信息</h5>
				<div class="form-group">
					<label for="busType" class="col-sm-2 control-label">会员身份：</label>
					<div class="col-sm-4">
						<p id="busType" class="pTop8">
						企业
						</p>
					<!-- 	<input type="text" class="form-control" id="memberType"
							placeholder="企业" disabled="disabled" /> -->
					</div>
							<label for="busName" class="col-sm-2 control-label">公司名称：</label>
					<div class="col-sm-4">
					<p name="busName" id="busName" class="pTop8">
					<s:if test="#request.busBussinessInfo.busName != null">
					${busBussinessInfo.busName}
					</s:if>
					<s:else>
					暂无（<a href="bussiness/realName.action">去实名认证</a>）
					</s:else>
						
						</p>
						<%-- <input disabled="disabled" type="text" class="form-control"
							name="busName" id="busName"
							placeholder="${busBussinessInfo.busName}" /> --%>
					</div>
				</div>

				<hr class="dashed">
				<h5>业务信息</h5>
				<div class="form-group">
					<label for="busDomain" class="col-sm-2 control-label"><span
						style="color: red;">*</span>业务应用：</label>
					<div class="col-sm-4">
						<select id="busDomain" name="busDomain" class="form-control">
							<option value="0">请选择</option>
							<s:iterator value="#request.busDomainList" status="item">
								<s:if test="#request.busBussinessInfo.busDomain == value">
									<option selected="selected" value=<s:property value="value"/>><s:property
											value="name" /></option>
								</s:if>
								<s:else>
									<option value=<s:property value="value"/>><s:property
											value="name" /></option>
								</s:else>

							</s:iterator>
						</select>
					</div>
						<label for="domain" class="col-sm-2 control-label">主营业务：</label>
					<div class="col-sm-4">
					<textarea placeholder="最多填写100个字符..." class="form-control" rows="3" id="domain" name="domain" maxlength="100">${busBussinessInfo.domain}</textarea>
						<%-- <input type="text" class="form-control" id="domain" name="domain"
							value="${busBussinessInfo.domain}" /> --%>
					</div>
				</div>
				<hr class="dashed">
				<h5>地域信息</h5>
				<div class="form-group">
					<label for="province" class="col-sm-2 control-label"><span
						style="color: red;">*</span>所在地区：</label>
					<div class="col-sm-2">
						<select id="province" name="province" class="form-control">
							<option value="0">请选择省</option>
							<s:iterator value="#request.provinceList" status="statu"
								id="item">
								<s:if test="#request.busBussinessInfo.province !=null">
									<s:if test="id == #request.busBussinessInfo.province">
										<option value="<s:property value='id'/>" selected="selected"><s:property
												value="name" />
										</option>
									</s:if>
									<s:else>
										<option value="<s:property value='id'/>"><s:property
												value="name" />
										</option>
									</s:else>
								</s:if>
								<s:else>
									<option value="<s:property value='id'/>"><s:property
											value="name" />
									</option>
								</s:else>

							</s:iterator>
						</select>
					</div>

					<div class="col-sm-2">
						<select id="city" name="city" class="form-control">
							<s:if test="#request.cityList != null">
								<s:iterator value="#request.cityList" status="statu" id="item">
									<s:if test="id == #request.busBussinessInfo.city">
										<option value="<s:property value='id'/>" selected="selected"><s:property
												value="name" />
										</option>
									</s:if>
									<s:else>
										<option value="<s:property value='id'/>"><s:property
												value="name" />
										</option>
									</s:else>
								</s:iterator>
							</s:if>
							<s:else>
								<option value="0">请先选择省份</option>
							</s:else>
						</select>
					</div>
						<label for="tel" class="col-sm-2 control-label"><span
						style="color: red;">*</span>联系电话：</label>
					<div class="col-sm-4">
						<input placeholder="11位手机号码或座机号码" type="text" class="form-control" id="tel" name="tel"
							value="${busBussinessInfo.tel}" maxlength="20"/>
					</div>
				</div>
				<div class="form-group">
					<label for="address" class="col-sm-2 control-label">街道地址：</label>
					<div class="col-sm-4">
					<textarea placeholder="公司详细地址" class="form-control" rows="3" id="address" name="address" maxlength="150">${busBussinessInfo.address}</textarea>
				
					</div>
				</div>
			
				
			

			</form>

		</div>
	</div>
</body>
<script type="text/javascript">
	setBowHeight1();
	$(function() {
		var saveBaseInfoUrl = "bussiness_saveBaseInfo.action";
		initArea($("#province"), $("#city"));
		resetForm($("#form1"));
		$("#saveInfoBtn").attr("disabled", true);
		$("#address").on("keydown",function(){
			$("#saveInfoBtn").removeAttr("disabled");
		});
		$("#domain").on("keydown",function(){
			$("#saveInfoBtn").removeAttr("disabled");
		});
		$("#tel").on("keydown",function(){
			$("#saveInfoBtn").removeAttr("disabled");
		});
		$("#busDomain").on("change", function() {
			$("#saveInfoBtn").removeAttr("disabled");
		});
		$("#province").on("change", function() {
			$("#saveInfoBtn").removeAttr("disabled");
		});
		$("#city").on("change", function() {
			$("#saveInfoBtn").removeAttr("disabled");
		});
		
		$("#tel").on("change",function(){
			temp = $(this).val();
			if(isEmpty(temp)){
				opt={};
				opt.onOk = function(){$("#tel").val("").focus();}
				opt.onClose = opt.onOk;
				showWarning("请填写电话号码！！",opt)
			}else if(!regBox.regMobile.test(temp) && !regBox.regTel.test(temp)){
				opt={};
				opt.onOk = function(){$("#tel").val("").focus();}
				opt.onClose = opt.onOk;
				showWarning("请正确填写座机号码或11位手机号码！！",opt)
			}
		});
		$("#saveInfoBtn").on("click", function() {
			$.ajax({  
			      url:saveBaseInfoUrl,// 跳转到 action  
			      data:$('#form1').serialize(),  
			      type:'post',  
			      cache:false,  
			      dataType:'json',  
			      beforeSend:function(){$("#loading").removeClass("hide");},
			      success:function(data) {
						if ("success" == data.message) {
							$("#saveInfoBtn").attr("disabled", true);
							var opt={};
							opt.onOk = function(){reload()}
							opt.onClose = opt.onOk;
							showSuccess("修改成功！！",opt);
						} else {
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
			/* $.post(saveBaseInfoUrl, $('#form1').serialize(), ); */
		});

	})
</script>
</html>

