<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <title>充电卡管理/卡信息管理</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
   <div id="badChargeListDiv" class="hide">
    <form class="form-inline" role="form">
     <div class="form-group pull-right">
			<button type="button" id="paymentBtn" class="btn btn-primary">付款</button>
			<button type="button" class="btn btn-primary" onclick="href('person/cardInfo.action');">返回</button>
		 </div>
    </form>
   <table class="table table-condensed table-hover" id="badChargeListTable">
	   		<thead>
	   			<tr>
	   				<th><input id="allBillCheck" type='checkbox'/> 全选</th>
	   				<th>订单号</th>
	   				<th>充电点名称</th>
	   				<th>充电地址</th>
	   				<th>充电桩名称</th>
	   				<th>手机号码</th>
	   				<th>充电时长（分）</th>
	   				<th>充电费（元）</th>
	   				<th>服务费（元）</th>
	   				<th>停车费（元）</th>
	   				<th>充电时间</th>
	   				<th>应付总费用（元）</th>
	   				<th>支付状态</th>
	   			</tr>
	   		</thead>
		   	<tbody id="badChargeListBody"></tbody>
		</table>
   </div>
  <div id="rechargeCardDiv" class="hide">
  <form id="rechargeForm" class="form-horizontal" role="form">
  	<h5>充电卡充值</h5>
      	<div class="form-group">
			<label class="col-sm-2 control-label"><span style="color: red;">*</span>充点卡卡号</label>
		        <div class="col-sm-4">
		        <p style="color: #00c0ef;font-size: 20px;" id="chargeCardNoForRechargeForm"></p>
		        </div>
		    <label class="col-sm-2 control-label"><span style="color: red;">*</span>充值金额</label>
		        <div class="col-sm-4">
		        	<input type="text" class="form-control" id="rechargeMoney" name="rechargeMoney" maxlength="10" placeholder="请输入充值金额"/>
		        </div>
    	</div>
    	<div class="form-group">
    	 <label class="col-sm-2 control-label"><span style="color: red;">*</span>卡密码</label>
		        <div class="col-sm-4">
		        	<input type="password" class="form-control" id="rechargeCardPwd" name="rechargeCardPwd" maxlength="6" placeholder="请输入卡密码"/>
		        </div>
    	</div>
    	
    		<div class="form-group">
	      <div class="col-sm-offset-2 col-sm-10">
	         <button id="doRechargeBtn" type="button" class="btn btn-primary" disabled="disabled">确定</button>
	         <button id="cancelRechargeBtn" type="button" class="btn btn-primary" onclick="reload()">返回</button>
	      </div>
   		</div>
   		</form>
  </div>
  <div id="CardInfoDiv">
  	<form id="userForm" class="form-horizontal" role="form">
  	<h5>用户基本信息</h5>
		<div class="form-group">
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>姓名</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="realName" name="realName" maxlength="10" disabled="disabled"/>
	      </div>
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>性别</label>
	      <div class="col-sm-4">
	      
	      <label class="checkbox-inline">
		      	<input type="radio" name="sex"  id="sex1" value='1' disabled="disabled">  男
			  </label>
			    <label class="checkbox-inline"> 
		      	<input type="radio" name="sex"  id="sex2" value='2' disabled="disabled">  女
			  </label>
	     
	      </div>
    	</div>
    	<div class="form-group">
			<label class="col-sm-2 control-label"><span style="color: red;">*</span>手机号码</label>
		        <div class="col-sm-4">
		      		<input type="text" class="form-control" id="phone" name="phone" maxlength="20" disabled="disabled"/>
		        </div>
		    <label class="col-sm-2 control-label"><span style="color: red;">*</span>邮箱地址</label>
		        <div class="col-sm-4">
		        	<input type="text" class="form-control" id="email" name="email" maxlength="20" disabled="disabled"/>
		        </div>
    	</div>
    	<div class="form-group">
	       	<label class="col-sm-2 control-label"><span style="color: red;">*</span>证件类型</label>
	     	<div class="col-sm-4">
		       <select id="cardType" name="cardType" class="form-control" disabled="disabled">
		       <option value="1">身份证</option>
		       </select>
	       	</div>
	      	<label class="col-sm-2 control-label"><span style="color: red;">*</span>证件号码</label>
	        <div class="col-sm-4">
	      		<input type="text" class="form-control" id="userCardNo" name="userCardNo" maxlength="20" disabled="disabled"/>
	        </div>
    	</div>
    	</form>
    	<hr/>
    	<h5>充电卡基本信息</h5>
    	<form id="chargeCardForm" class="form-horizontal" role="form">
    	<div class="form-group">
    	       <label class="col-sm-2 control-label"><span style="color: red;">*</span>发卡运营商编号</label>
	        <div class="col-sm-4">
	      		<input type="text" class="form-control" id="busNo" name="busNo" maxlength="20" disabled="disabled"/>
	        </div>
	        	<label class="col-sm-2 control-label"><span style="color: red;">*</span>充电卡卡号</label>
	        <div class="col-sm-4">
	      		<input type="text" class="form-control" id="chargeCardNo" name="chargeCardNo" maxlength="20" disabled="disabled"/>
	        </div>
    	</div>
    	
    <!-- 	<div class="form-group">
	       <label class="col-sm-2 control-label"><span style="color: red;">*</span>应用类型标识</label>
	        <div class="col-sm-4">
	        	<select id="applicationType" name="applicationType" class="form-control" disabled="disabled">
	        	<option value="1">应用类型1</option>
	        	</select>
	        </div>
	        <label class="col-sm-2 control-label"><span style="color: red;">*</span>卡类型标识</label>
	        <div class="col-sm-4">
	        	<select id="cardType" name="cardType" class="form-control" disabled="disabled">
	        	<option value="1">用户卡</option>
	        	</select>
	        </div>	 
    	</div> -->

    	<div class="form-group">
	     <!--     <label class="col-sm-2 control-label"><span style="color: red;">*</span>充电卡密码</label>
	        <div class="col-sm-3">
	        <input type="text" class="form-control" id="password" name="password" maxlength="20" placeholder="充电卡密码"/>
	        </div>
	        <div class="col-sm-1">
	         <button id="changePwdBtn" type="button" class="btn btn-primary" disabled="disabled">修改</button>
	        </div> -->
	          <label class="col-sm-2 control-label"><span style="color: red;">*</span>启用日期</label>
	        <div class="col-sm-4">
	        	<div class='input-group date' id='startTimeDate'>
							<input id="startTime" name="startTime" type='text' class="form-control" disabled="disabled" /> 
							<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span> </span>
				</div>
	        </div>
	       	<label class="col-sm-2 control-label"><span style="color: red;">*</span>卡内余额(元)</label>
	        <div class="col-sm-2">
	        	<input type="text" class="form-control"  id="usableMoney" maxlength="20" disabled="disabled"/>
	        </div>
	        <div class="col-sm-2 text-right">
	         	<button id="rechargeBtn" type="button" class="btn btn-primary" disabled="disabled">充值</button>
	        	<button id="readCardUsableBtn" type="button" class="btn btn-primary" disabled="disabled">读余额</button>
	        </div>
    	</div>
    	
    	<div class="form-group">
    	  <label class="col-sm-2 control-label"><span style="color: red;">*</span>有效日期</label>
	        <div class="col-sm-4">
	        	<div class='input-group date' id='endTimeDate'>
						<input id="endTime" name="endTime" type='text' class="form-control" disabled="disabled" /> 
						<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span> </span>
				</div>
	        </div>
    	
	 		<label class="col-sm-2 control-label"><span style="color: red;">*</span>灰色记录</label>
	        <div class="col-sm-3">
	        	<input type="text" id="badChargeIds" class="form-control" maxlength="20" disabled="disabled" value="332,335"/>
	        </div>
	        <div class="col-sm-1">
	           <button id="checkBadRcordBtn" type="button" class="btn btn-primary" disabled="disabled">查看</button>
	        </div>
    	</div>
    	
    	
    	<div class="form-group">
	      <div class="col-sm-offset-2 col-sm-10">
	         <button id="refreshBtn" type="button" class="btn btn-primary">刷新</button>
	         <button id="goChangePwdBtn" type="button" class="btn btn-primary" disabled="disabled">卡密码修改</button>
	         <button id="cancelBtn" type="button" class="btn btn-primary" onclick="href('person/cardManager.action')">返回</button>
	      </div>
   		</div>
    </form>
    </div>
    
    <!-- 模态框（Modal）badPaymentModal -->
	<div class="modal fade" id="badPaymentModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">灰记录扣款</h4>
				</div>
				<div class="modal-body">
					<form role="form" id="badPaymentForm">
		              <div class="box-body">
		                <div class="form-group">
		                 	<label for="chargeCardNoModel">充电卡号</label>
			                <p style="color: #00c0ef;font-size: 20px;" id="chargeCardNoModel"></p>
			                <input type="hidden" id="badChargeIdModel" />
		                </div>
		               	<div class="form-group">
		                  	<label>卡内余额</label>
			                <p style="color: #dd4b39;" class="form-control" id="usableMoneyModel">
		                	</p>
		                </div>
		                <div class="form-group">
		                  <label for="totalShouldMoneyModel">付款金额</label>
		                  <p type="text" class="form-control" id="totalShouldMoneyModel" ></p>
		                </div>
		              </div>
		              <!-- /.box-body -->
		            </form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="paymentModelBtn" type="button" class="btn btn-primary">提交</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal -->
	</div>
	
	  <!-- 模态框（Modal）cardChangePwdModal -->
	<div class="modal fade" id="cardChangePwdModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">密码修改</h4>
				</div>
				<div class="modal-body">
					<form role="form" id="cardChangePwdForm">
		              <div class="box-body">
		                <div class="form-group">
		                 	<label>充电卡号</label>
			                <p style="color: #00c0ef;font-size: 20px;" id="cardNoForChangePwd"></p>
		                </div>
		               	<div class="form-group">
		                  	<label>新密码</label>
		                  	<input type="password" class="form-control"  id="password" maxlength="6"/>
		                	</p>
		                </div>
		              </div>
		              <!-- /.box-body -->
		            </form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="changePwdBtn" type="button" class="btn btn-primary">提交</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal -->
	</div>
	
	
	  <!-- 模态框（Modal）readCardUsableMoneyModal -->
	<div class="modal fade" id="readCardUsableMoneyModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">读卡余额</h4>
				</div>
				<div class="modal-body">
					<form role="form" id="readCardUsableMoneyForm">
		              <div class="box-body">
		                <div class="form-group">
		                 	<label>充电卡号</label>
			                <p style="color: #00c0ef;font-size: 20px;" id="cardNoForReadCardUsableMoney"></p>
		                </div>
		               	<div class="form-group">
		                  	<label>密码</label>
		                  	<input type="password" class="form-control"  id="readCardUsableMoneyPwd" maxlength="6"/>
		                	</p>
		                </div>
		              </div>
		              <!-- /.box-body -->
		            </form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="readCardUsableMoneyBtn" type="button" class="btn btn-primary">提交</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal -->
	</div>
  </div>
  
  	<object id="chargeCardOcx" TYPE="application/oleobject" 
  		classid="CLSID:E1422977-A13F-46D6-9138-AB67666EC80E" 
  		codebase="${imgUrl}res/ocx/HLChargeCard.cab"
  		width="0" height="0" align="center" hspace="0" vspace="0">
	</object>
<script type="text/javascript">
var currentTime = "${currentTime}";
</script>
<script src="res/js/chargecard/cardInfoInit.js" type="text/javascript"></script>
<script src="res/js/chargecard/cardInfo.js" type="text/javascript"></script>
</body>
</html>

