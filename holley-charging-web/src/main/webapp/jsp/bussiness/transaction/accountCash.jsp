<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<%@include file="../../common/global/meta.jsp"%>
<%@include file="../../common/global/js.jsp"%>
<%@include file="../head.jsp"%>
<html lang="zh-CN">
<head>
<style type="text/css">
p{
margin-top: 4px;
}

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
		<div class="container main-body" style="height: 76%;">
			<form class="form-horizontal" role="form" id="cashForm">
				<div class="row">
					<div class="col-sm-2">
						<h3>提现</h3>
					</div>
					<div class="col-sm-offset-8 col-sm-2 text-right"
						style="margin-top: 21px;">
						<button id="doAccountCashBtn" type="button"
							class="btn btn-warning btn-sm">提交</button>
						<button onclick="href('account/accountInfo.action');"
							type="button" class="btn btn-warning btn-sm">返回</button>
					</div>
				</div>
				<hr />
				<div class="form-group">
					<label for="totalMoney" class="col-sm-2 control-label">账户总额：</label>
					<div class="col-sm-8">
						<p id="totalMoney"><span style="color:red;font-size:17px;">${busAccount.totalMoney}</span> 元</p>
					</div>
				</div>
				<div class="form-group">
					<label for="usableMoney" class="col-sm-2 control-label">可用金额：</label>
					<div class="col-sm-8">
					<p id="totalMoney"><span style="color:red;font-size:17px;">${busAccount.usableMoney}</span> 元</p>
					</div>
				</div>
<%-- 				<div class="form-group">
					<label for="freezeMoney" class="col-sm-2 control-label">冻结金额</label>
					<div class="col-sm-8">
						<input disabled="disabled" class="form-control"
							placeholder="${busAccount.freezeMoney}元" id="freezeMoney" />
					</div>
				</div> --%>
				<div class="form-group">
					<label for="cashMoney" class="col-sm-2 control-label">提现金额：</label>
					<div class="col-sm-8">
						<input placeholder="提现金额保留2位小数" type="text" class="form-control" id="cashMoney" value="" maxlength="15"/>

					</div>
				</div>
				<div class="form-group">
					<label for="remark" class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-8">
						<textarea id="remark" class="form-control" rows="3" maxlength="200" placeholder="最多填写200个字符..."></textarea>
					</div>
				</div>
			</form>
			<div class="row">
				<div class="col-sm-offset-1">
					<p>转账须知：</p>
				</div>
				<div class="col-sm-offset-2">
					<p class="help-block warn">1.单笔转账金额必须大于1元。</p>
				</div>
				<div class="col-sm-offset-2">
					<p class="help-block warn">2.一天最多转账10笔，一天最多转账10万元。</p>
				</div>
				<div class="col-sm-offset-2">
					<p class="help-block warn">3.转账时间为每天10:00-17:00（除法定节假日）。</p>
				</div>
			</div>
		</div>
		</s:else>
	</div>
</body>
<script type="text/javascript">
setBowHeight1();
	$(function() {
		var param = {};
		var doAccountCashUrl = "account_doAccountCash.action";
		var cashMoney;
		var remark;
		var msg;
		resetForm($("#cashForm"));
		function cheackCashMoney(cashMoney) {
			if (!cashMoney) {
				return "请输入金额！！";
			} else if (isNaN(cashMoney)) {
				return "请输入正确的金额！！";
			} else if (parseFloat(cashMoney) <= 0) {
				return "输入的金额必须大于0元！！";
			} else {
				return "success";
			/* 	return confirm("确定提现金额为"
						+ Math.round(parseFloat(cashMoney * 100)) / 100 + "元？"); */
			}
		}
		function doCash(cashMoney, remark) {
			param.cashMoney = cashMoney;
			param.remark = remark;
			$.ajax({  
			      url:doAccountCashUrl,// 跳转到 action  
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
				if(count.length > 0){
					$("#cashMoney").val(Math.round(parseFloat(temp * 100)) / 100);
				}
			}
			//$("#cashMoney").val(Math.round(parseFloat(temp * 100)) / 100);
		}
	}); 
		$("#doAccountCashBtn").on("click", function() {
			cashMoney = $("#cashMoney").val();
			remark = $("#remark").val();
			msg = cheackCashMoney(cashMoney);
			if("success" == msg){
				cashMoney = Math.round(parseFloat(cashMoney * 100)) / 100;
				var opt={};
				opt.onOk = function(){
					doCash(cashMoney, remark);
				}
				showConfirm("确定提现金额为"+ cashMoney + "元？",opt);
			}
			else{
				var opt={};
				opt.onOk = function(){
					$("#cashMoney").val("").focus();
				}
				opt.onClose = opt.onOk;
				showWarning(msg, opt);
			}
			
		});
	})
</script>
</html>

