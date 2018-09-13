<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>开户开卡/开户</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
  	<form id="userForm" class="form-horizontal" role="form">
  	 <h5>用户基本信息</h5>
		<div class="form-group">
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>姓名</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="realName" name="realName" maxlength="10" placeholder="用户名真实姓名"/>
	      </div>
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>性别</label>
	      <div class="col-sm-4">
	      <label class="checkbox-inline"> 
	      	<input type="radio" name="sex"  value='1' checked>  男
		  </label>
		  <label class="checkbox-inline"> 
	      	<input type="radio" name="sex"  value='2'>  女
		  </label>
	      </div>
    	</div>
    	<div class="form-group">
			<label class="col-sm-2 control-label"><span style="color: red;">*</span>手机号码</label>
	        <div class="col-sm-4">
	      		<input type="text" class="form-control" id="phone" name="phone" maxlength="20" placeholder="11位手机号码"/>
	        </div>
	        	   <label class="col-sm-2 control-label"><span style="color: red;">*</span>省市</label>
	       <div class="col-sm-2">
	      	<select id="province" name="province" class="form-control">
	      		<option value="0">请选择省份</option>
	      		<s:iterator id="item" value="#request.provinceList">
	      		<option value="<s:property value="id"/>"><s:property value="name"/></option>
	      		</s:iterator>
	      	</select>
	        </div>
	       	<div class="col-sm-2">
	      		<select id="city" name="city" class="form-control">
	      		<option value="0">请选择市区</option>
	      		</select>
	        </div>
    	</div>
    	<div class="form-group">
	       <label class="col-sm-2 control-label">证件类型</label>
	     <div class="col-sm-4">
	       <select id="cardType" name="cardType" class="form-control">
	       <option value="1">身份证</option>
	       </select>
	        </div>
	                <label class="col-sm-2 control-label">邮箱地址</label>
	        <div class="col-sm-4">
	        <input type="text" class="form-control" id="email" name="email" maxlength="20" placeholder="邮箱地址"/>
	        </div>
	        
    	</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">证件号码</label>
	        <div class="col-sm-4">
	      		<input type="text" class="form-control" id="cardNo" name="cardNo" maxlength="20" placeholder="证件号码"/>
	        </div>	 
	        <label for="groupId" class="col-sm-2 control-label">优惠方案：</label>
			<div class="col-sm-4">
					<select id="rebateId" name="rebateId" class="form-control">
						<option value="0">优惠方案</option>
						<s:iterator value="#request.rebateList" status="statu" id="item">
						<option value=<s:property value="id"/> ><s:property value="rebateDesc"/> 【折扣:<s:property value="rebate"/>】</option>
						</s:iterator>
					</select>
			</div>
    	</div>
    	
    	<h5>车辆基本信息</h5>
    	<div class="form-group">
			<label class="col-sm-2 control-label"><span style="color: red;">*</span>车牌号</label>
	        <div class="col-sm-4">
	         <input type="text" class="form-control" id="plateNo" name="plateNo" placeholder="车牌号" value="${busUserInfo.plateNo}" maxlength="20">
	        </div>
	       <label for="brand" class="col-sm-2 control-label"><span style="color: red;">*</span>车品牌</label>
					<div class="col-sm-2">
						<select id="brand" name="brand" class="form-control">
							<option value="0">请选择车品牌</option>
							<s:iterator value="#request.carBrandList" status="statu" id="item">
								<option value="<s:property value='id'/>">
									<s:property value="name" />
								</option>
							</s:iterator>
						</select>
					</div>
					<div class="col-sm-2">
						<select id="model" name="model" class="form-control">
								<option value="0">请先选择车型号</option>
						</select>
					</div>
    	</div>
    	<div class="form-group">
    	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>车架号</label>
		      <div class="col-sm-4">
		        <input type="text" class="form-control" id="vin" name="vin" placeholder="车架号" value="${busUserInfo.vin}" maxlength="30">
		      </div>
		      
		      	<label for="groupId" class="col-sm-2 control-label"><span
						style="color: red;">*</span>所属子公司：</label>
					<div class="col-sm-4">
					<select id="groupId" name="groupId" class="form-control">
						<option value="0">系统平台</option>
						<s:iterator value="#request.companyList" status="statu" id="item">
						<option value=<s:property value="id"/> ><s:property value="username"/></option>
						</s:iterator>
						</select>
					</div>
        </div>
    	<div class="form-group">
	      <div class="col-sm-offset-2 col-sm-10">
	         <button id="saveBtn" type="button" class="btn btn-primary">保存</button>
	         <button id="registerCardBtn" type="button" disabled="disabled" class="btn btn-primary">去开卡</button>
	         <button id="cancelBtn" type="button" class="btn btn-primary" onclick="gobackAndReload()">取消</button>
	      </div>
   		</div>
    </form>
  </div>
<script src="res/js/chargecard/registerUser.js" type="text/javascript"></script>
</body>
</html>

