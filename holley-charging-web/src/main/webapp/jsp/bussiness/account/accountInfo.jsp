<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<html lang="zh-CN">
<head>
<%@include file="../../common/global/meta.jsp"%>
<%@include file="../../common/global/js.jsp"%>
<%@include file="../head.jsp"%>
<style type="text/css">
#accountInfoDiv span{
color:red;
font-size:17px;
}
</style>
</head>
<body>
	<div class="bg">
		<div class="container main-body" style="height: 230px;" id="accountInfoDiv">
			<div class="row">
				<div class="col-sm-3">
					<h3>账户总览</h3>
				</div>
				<s:if test="#request.webUser.roleType == 2">
				<div class="col-sm-offset-7 col-sm-2 text-right"  style="margin-top: 17px;">
					 <!-- <a href="account/accountCash.action">提现申请</a> -->
					 <button onclick="href('account/accountCash.action');"
						type="button" class="btn btn-warning btn-sm">提现</button>
				</div>
				</s:if>
			</div>
			<hr style="margin-top:10px;"/>
			<div class="row" style="height: 50px;" >
				<div class="col-sm-4">
					账户总额：<span>${busAccount.totalMoney}</span>元
				</div>
				<div class="col-sm-4">
					账户余额：<span>${busAccount.usableMoney}</span>元
				</div>
				<div class="col-sm-4">
					冻结金额：<span>${busAccount.freezeMoney}</span>元
				</div>
			</div>
			<div class="row" style="height: 50px;">
				<div class="col-sm-4">
					平台桩数量：<span>${countPileModel.totalFast+countPileModel.totalSlow}</span>个
				</div>
				<div class="col-sm-4">
					快充桩数量：<span>${countPileModel.totalFast+0}</span>个
				</div>
				<div class="col-sm-4">
					慢充桩数量：<span>${countPileModel.totalSlow+0}</span>个
				</div>
			</div>
			<!-- <div style="width: 40%;">
 					1.单笔转账金额必须大于1元
					2.一天最多转账10笔，一天最多转账10万元
 					3.转账时间为每天10:00-17:00（除法定节假日）
				</div> -->
		</div>
		<s:if test="#request.webUser.roleType == 2">
		<div class="container main-body"
			style="height: 62%; margin-top: 20px;overflow: visible;">
			<div class="row tableSearchSize">
				<div class="col-sm-3">
					<p style="font-size: 24px;">提现记录</p>
				</div>
				<div class="col-sm-offset-3 col-sm-6">
					<div class="form-group">
						<div class="col-sm-offset-4 col-sm-5">
						<div class="input-group date" id="datetime" style="float: left;">
						<input type='text' class="form-control" placeholder="请选择月份" id="searchDate"/> 
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
						</div>
						<div class="col-sm-1 text-right">
							<button style="margin-top: 2px;" id="searchKeyNameBtn" class="btn btn-warning btn-sm"
									type="button"> 
									查询
								 </button> 
						</div>
						<div class="col-sm-2 text-right">
								<button style="margin-top: 2px;" id="exportBtn" class="btn btn-warning btn-sm"
									type="button"> 
									导出
								 </button> 
						</div>
					
 				</div>
			</div>

			</div>
			<div id="accountCashDiv">
				<div class="tableDivSize">
					<table class="table table-condensed" id="accountCashTable">
						<tbody>
						</tbody>
					</table>
				</div>
				<%@include file="../../common/global/pagingtoolbar.jsp"%>
			</div>
		</div>
		</s:if>
	</div>
	<script type="text/javascript">
	var roleType = '${webUser.roleType}'
	if(roleType && roleType == 4){
		setBowHeight1();
	}
	</script>
	<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
	<script src="res/js/bussiness/accountCash.js" type="text/javascript"></script>
</body>
</html>

