<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>运营商详细信息</title>
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
	      <label class="col-sm-2 control-label">企业名称</label>
	      <div class="col-sm-4">
	        <p class="form-control-static"><s:property value="#request.busInfo.busName"/></p>
	      </div>
	      <label class="col-sm-2 control-label">联系电话</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.busInfo.tel"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">所属地区</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static">
	      			<s:property value="#request.busInfo.provinceDesc"/>
	      			<s:if test="#request.busInfo.provinceDesc != '' && #request.busInfo.cityDesc != ''">,</s:if>
	      			<s:property value="#request.busInfo.cityDesc"/>
	      		</p>
	        </div>
	        <label class="col-sm-2 control-label">详细地址</label>
	        <div class="col-sm-4">
	        	<p class="form-control-static"><s:property value="#request.busInfo.address"/></p>
	        </div>
    	</div>
    	<div class="form-group">
			<label class="col-sm-2 control-label">主要业务应用</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.busInfo.busDomain"/></p>
		    </div>
	        <label class="col-sm-2 control-label">主要业务</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.busInfo.domain"/></p>
		    </div>
    	</div>
    	<div class="form-group">
			<label class="col-sm-2 control-label">企业规模</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.busInfo.scale"/></p>
	        </div>
	        <label class="col-sm-2 control-label">营业年限</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.busInfo.limitYear"/></p>
		    </div>
    	</div>
  		<h5 class="form-header">认证信息</h5>
  		<hr class="dashed">
    	<div class="form-group">
			<label class="col-sm-2 control-label">企业银行开户名</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.busInfo.accRealName"/></p>
	        </div>
	        <label class="col-sm-2 control-label">企业对公账户</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.busInfo.bankAccount"/></p>
		    </div>
    	</div>
    	<div class="form-group">
		    <label class="col-sm-2 control-label">营业执照照片</label>
	        <div class="col-sm-4">
	        	<s:if test="#request.busInfo.licenceImg != null && #request.busInfo.licenceImg != ''">
		      		<img class="img-thumbnail img-thumbnail-size" alt="" src="${imgUrl}<s:property value="#request.busInfo.licenceImg"/>" 
		      		 	 onmouseenter="openImg(this,'营业执照')" onmouseleave="closeImg(this)">
	        	</s:if>
	        </div>
	        <label class="col-sm-2 control-label">开户行名称</label>
	        <div class="col-sm-4">
	        	<p class="form-control-static"><s:property value="#request.busInfo.bankName"/></p>
	        </div>
    	</div>
    	<div class="form-group">
    		<label class="col-sm-2 control-label">法人代表身份证</label>
		    <div class="col-sm-4">
		    	<s:if test="#request.busInfo.corporateImg != null && #request.busInfo.corporateImg != ''">
			      	<img class="img-thumbnail img-thumbnail-size" alt="" src="${imgUrl}<s:property value="#request.busInfo.corporateImg"/>" 
		      		 	 onmouseenter="openImg(this,'法人代表身份证')" onmouseleave="closeImg(this)">
		    	</s:if>
		    </div>
	        <label class="col-sm-2 control-label">办理人身份证</label>
		    <div class="col-sm-4">
		    	<s:if test="#request.busInfo.transatorImg != null && #request.busInfo.transatorImg != ''">
			      	<img class="img-thumbnail img-thumbnail-size" alt="" src="${imgUrl}<s:property value="#request.busInfo.transatorImg"/>" 
		      		 	 onmouseenter="openImg(this,'办理人身份证')" onmouseleave="closeImg(this)">
		    	</s:if>
		    </div>
    	</div>
    	<h5 class="form-header">企业帐号</h5>
  		<hr class="dashed">
  		<div id="userTableDiv">
		    <table class="table table-condensed table-hover" id="userTable">
		   		<thead>
		   			<tr>
		   				<th>用户编码</th>
		   				<th>用户昵称</th>
		   				<th>手机号码</th>
		   				<th>电子邮箱</th>
		   				<th>用户角色</th>
		   			</tr>
		   		</thead>
			   	<tbody>
			   		<s:iterator value="#request.userList" var="item" status="st">
			   			<tr>
				   			<td><s:property value="#item.id"/></td>
				   			<td><s:property value="#item.username"/></td>
				   			<td><s:property value="#item.phone"/></td>
				   			<td><s:property value="#item.email"/></td>
				   			<td><s:property value="#item.rolename"/></td>
			   			</tr>
			   		</s:iterator>
			   	</tbody>
			</table>
	    </div>
    	<div class="form-group">
	      <div class="col-sm-offset-2 col-sm-10">
	         <button type="button" class="btn btn-primary" onclick="gobackAndReload()">返回</button>
	      </div>
	  	</div>
    </form>
  </div>
</body>
</html>

