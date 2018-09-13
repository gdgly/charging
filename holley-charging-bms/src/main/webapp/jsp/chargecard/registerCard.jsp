<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>开户开卡/开卡</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
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
	         <button id="doRechargeBtn" type="button" class="btn btn-primary">确定</button>
	         <button type="button" class="btn btn-primary" onclick="href('person/cardInfo.action')">返回查看卡信息</button>
	      </div>
   		</div>
   		</form>
  </div>
  <div id="registerCardDiv">
  	<form id="userForm" class="form-horizontal" role="form">
  	<h5>用户基本信息</h5>
		<div class="form-group">
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>姓名</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="realName" name="realName" maxlength="10" placeholder="用户名真实姓名" disabled="disabled" value="${userInfo.realName}"/>
	      </div>
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>性别</label>
	      <div class="col-sm-4">
	      <s:if test="#request.userInfo.sex == 1">
		      <label class="checkbox-inline">
		      	<input type="radio" name="sex"  value='1' checked>  男
			  </label>
			    <label class="checkbox-inline"> 
		      	<input type="radio" name="sex"  value='2'>  女
			  </label>
	      </s:if>
	      <s:elseif test="#request.userInfo.sex == 2">
	      <label class="checkbox-inline">
		      	<input type="radio" name="sex"  value='1'>  男
			  </label>
			    <label class="checkbox-inline"> 
		      	<input type="radio" name="sex"  value='2' checked>  女
			  </label>
	      </s:elseif>
	      </div>
    	</div>
    	<div class="form-group">
			<label class="col-sm-2 control-label"><span style="color: red;">*</span>手机号码</label>
		        <div class="col-sm-4">
		      		<input type="text" class="form-control" id="phone" name="phone" maxlength="20" placeholder="11位手机号码" value="${user.phone}" disabled="disabled"/>
		        </div>
		    <label class="col-sm-2 control-label"><span style="color: red;">*</span>邮箱地址</label>
		        <div class="col-sm-4">
		        	<input type="text" class="form-control" id="email" name="email" maxlength="20" placeholder="邮箱地址" value="${user.email}"/>
		        </div>
    	</div>
    	<div class="form-group">
	       <label class="col-sm-2 control-label"><span style="color: red;">*</span>证件类型</label>
	     <div class="col-sm-4">
	       <select id="cardType" name="cardType" class="form-control">
	       <option value="1">身份证</option>
	       </select>
	        </div>
	      	<label class="col-sm-2 control-label"><span style="color: red;">*</span>证件号码</label>
	        <div class="col-sm-4">
	      		<input type="text" class="form-control" id="userCardNo" name="userCardNo" maxlength="20" placeholder="证件号码" value="${userInfo.cardNo}"/>
	        </div>
    	</div>
    	</form>
    	<hr/>
    	<h5>充电卡基本信息</h5>
    	<form id="chargeCardForm" class="form-horizontal" role="form">
    	<div class="form-group">
    	       <label class="col-sm-2 control-label"><span style="color: red;">*</span>发卡运营商编号</label>
	        <div class="col-sm-4">
	          <input type="hidden" value="${user.id}" id="userId" name="userId"/>
	      		<input type="text" class="form-control" id="busNo" name="busNo" maxlength="20" value="" disabled="disabled"/>
	        </div>
	        	<label class="col-sm-2 control-label"><span style="color: red;">*</span>充电卡卡号</label>
	        <div class="col-sm-4">
	      		<input type="text" class="form-control" id="chargeCardNo" name="chargeCardNo" maxlength="20"  disabled="disabled"/>
	        </div>
    	</div>
    	<!-- <div class="form-group">
		
	       <label class="col-sm-2 control-label"><span style="color: red;">*</span>应用类型标识</label>
	        <div class="col-sm-4">
	        	<select id="applicationType" name="applicationType" class="form-control">
	        	<option value="1">应用类型1</option>
	        	</select>
	        </div>
	        <label class="col-sm-2 control-label"><span style="color: red;">*</span>卡类型标识</label>
	        <div class="col-sm-4">
	        	<select id="cardType" name="cardType" class="form-control">
	        	<option value="1">用户卡</option>
	        	</select>
	        </div>	 
    	</div> -->

    	<div class="form-group">
	         <label class="col-sm-2 control-label"><span style="color: red;">*</span>充电卡密码</label>
	        <div class="col-sm-4">
	        <input type="text" class="form-control" id="password" name="password" maxlength="20" placeholder="充电卡密码" value="${password}"/>
	        </div>
	          <label class="col-sm-2 control-label"><span style="color: red;">*</span>启用日期</label>
	        <div class="col-sm-4">
	        	<div class='input-group date' id='startTimeDate'>
							<input id="startTime" name="startTime" type='text' class="form-control" placeholder="充电卡启用日期"/> 
							<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span> </span>
				</div>
	        </div>
    	</div>
    	<div class="form-group">
    	    <label class="col-sm-2 control-label"><span style="color: red;">*</span>职工标识</label>
	        <div class="col-sm-4">
	        	<input type="text" class="form-control" maxlength="20" value="${worker.userName}" disabled="disabled"/>
	        	<input type="hidden" value="${worker.userId}" id="worker" name="worker"/>
	        </div>
	          <label class="col-sm-2 control-label"><span style="color: red;">*</span>有效日期</label>
	        <div class="col-sm-4">
	        	<div class='input-group date' id='endTimeDate'>
							<input id="endTime" name="endTime" type='text' class="form-control" placeholder="充电卡有效日期"/> 
							<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span> </span>
				</div>
	        </div>
	          
    	</div>
    	<div class="form-group">
	      <div class="col-sm-offset-2 col-sm-10">
	         <button id="saveBtn" type="button" class="btn btn-primary" disabled="disabled">保存</button>
	         <button id="rechargeBtn" type="button" class="btn btn-primary" disabled="disabled">充值</button>
	         <button onclick="reload();" type="button" class="btn btn-primary">刷新</button>
	         <button id="cancelBtn" type="button" class="btn btn-primary" onclick="href('person/cardManager.action')">返回</button>
	      </div>
   		</div>
    </form>
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
 <script src="res/js/chargecard/registerCard.js" type="text/javascript"></script> 
</body>
</html>

