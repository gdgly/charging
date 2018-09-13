<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>个人详细信息</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
  	<form id="userInfoForm" class="form-horizontal" role="form">
  		<h5 class="form-header">基本信息</h5>
  		<hr class="dashed">
		<div class="form-group">
	      <label class="col-sm-2 control-label">用户昵称</label>
	      <div class="col-sm-4">
	        <p class="form-control-static"><s:property value="#request.busUserInfo.username"/></p>
	      </div>
	      <label class="col-sm-2 control-label">手机号码</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.busUserInfo.phone"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">身份证号</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.busUserInfo.cardNo"/></p>
	        </div>
	        <label class="col-sm-2 control-label">真实姓名</label>
	        <div class="col-sm-4">
	        	<p class="form-control-static"><s:property value="#request.busUserInfo.realName"/></p>
	        </div>
    	</div>
    	<div class="form-group">
			<label class="col-sm-2 control-label">身份证件</label>
		        <div class="col-sm-4">
		        	<s:if test="#request.busUserInfo.front != null && #request.busUserInfo.front != ''">
			      		<img class="img-thumbnail img-thumbnail-size" alt="" src="${imgUrl}<s:property value="#request.busUserInfo.front"/>" 
			      		 	 onmouseenter="openImg(this,'身份证件')" onmouseleave="closeImg(this)">
		        	</s:if>
		        </div>
	        <label class="col-sm-2 control-label">个人签名</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.busUserInfo.sign"/></p>
		    </div>
    	</div>
    	<div class="form-group">
			<label class="col-sm-2 control-label">性别</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.busUserInfo.sexDesc"/></p>
	        </div>
	        <label class="col-sm-2 control-label">出生日期</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.busUserInfo.birthdayDesc"/></p>
		    </div>
    	</div>
  		<h5 class="form-header">车辆信息</h5>
  		<hr class="dashed">
    	<div class="form-group">
			<label class="col-sm-2 control-label">地区</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static">
	      			<s:property value="#request.busUserInfo.provinceDesc"/>
	      			<s:if test="#request.busUserInfo.provinceDesc != null && #request.busUserInfo.cityDesc != null">,</s:if>
	      			<s:property value="#request.busUserInfo.cityDesc"/></p>
	        </div>
	        <label class="col-sm-2 control-label">车牌号</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.busUserInfo.plateNo"/></p>
		    </div>
    	</div>
    	<div class="form-group">
		    <label class="col-sm-2 control-label">车架号</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.busUserInfo.vin"/></p>
	        </div>
	        <label class="col-sm-2 control-label">车品牌</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.busUserInfo.brandDesc"/></p>
		    </div>
    	</div>
    	<div class="form-group">
		    <label class="col-sm-2 control-label">行驶证</label>
	        <div class="col-sm-4">
	        	<s:if test="#request.busUserInfo.pic != null && #request.busUserInfo.pic != ''">
		      		<img class="img-thumbnail img-thumbnail-size" alt="" src="${imgUrl}<s:property value="#request.busUserInfo.pic"/>" 
		      		 	 onmouseenter="openImg(this,'行驶证')" onmouseleave="closeImg(this)">
	        	</s:if>
	        </div>
	        <label class="col-sm-2 control-label">车型号</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.busUserInfo.modelDesc"/></p>
		    </div>
    	</div>
    	<div class="form-group">
	      <div class="col-sm-offset-2 col-sm-10" >
	         <button type="button" class="btn btn-primary" onclick="gobackAndReload()">返回</button>
	      </div>
	  	</div>
    </form>
  </div>
</body>
</html>

