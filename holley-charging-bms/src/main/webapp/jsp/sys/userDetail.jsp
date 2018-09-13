<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>用户详细信息</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
  	<form id="userForm" class="form-horizontal" role="form">
  		<h5 class="form-header">基本信息</h5>
  		<hr class="dashed">
		<div class="form-group">
	      <label class="col-sm-2 control-label">用户昵称</label>
	      <div class="col-sm-4">
	        <p class="form-control-static"><s:property value="#request.busUser.username"/></p>
	      </div>
	      <label class="col-sm-2 control-label">用户角色</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.busUser.rolename"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">用户类型</label>
	      <div class="col-sm-4">
	        <p class="form-control-static"><s:property value="#request.busUser.userTypeDesc"/></p>
	      </div>
	      <label class="col-sm-2 control-label">注册时间</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.busUser.registTimeStr"/></p>
	        </div>
    	</div>
    	<div class="form-group">
			<label class="col-sm-2 control-label">手机号码</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.busUser.phone"/></p>
	        </div>
	        <label class="col-sm-2 control-label">电子邮箱</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.busUser.email"/></p>
		    </div>
    	</div>
    	<div class="form-group">
			<label class="col-sm-2 control-label">手机认证</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.busUser.phoneStatusDesc"/></p>
	        </div>
	        <label class="col-sm-2 control-label">邮箱认证</label>
		    <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.busUser.emailStatusDesc"/></p>
		    </div>
    	</div>
    	<div class="form-group">
	        <label class="col-sm-2 control-label">用户头像</label>
		    <div class="col-sm-4">
		    	<s:if test="#request.busUser.headImg != null && #request.busUser.headImg != ''">
			      	<img class="img-thumbnail img-thumbnail-size" alt="" src="${imgUrl}<s:property value="#request.busUser.headImg"/>" 
		      		 	 onmouseenter="openImg(this,'用户头像')" onmouseleave="closeImg(this)">
		    	</s:if>
		    </div>
		    <label class="col-sm-2 control-label">实名认证</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.busUser.realStatusDesc"/></p>
	        </div>
    	</div>
    </form>
    <s:if test="#request.busUser.userType == 2">
    	<form id="busInfoForm" class="form-horizontal" role="form">
	  		<h5 class="form-header">运营商信息</h5>
	  		<hr class="dashed">
			<div class="form-group">
		      <label class="col-sm-2 control-label">运营商名称</label>
		      <div class="col-sm-4">
		        <p class="form-control-static"><s:property value="#request.busInfo.busName"/></p>
		      </div>
		      <label class="col-sm-2 control-label">联系电话</label>
		      <div class="col-sm-4">
		        <p class="form-control-static"><s:property value="#request.busInfo.tel"/></p>
		      </div>
	    	</div>
	    	<div class="form-group">
		      <label class="col-sm-2 control-label">银行开户名</label>
		      <div class="col-sm-4">
		        <p class="form-control-static"><s:property value="#request.busInfo.accRealName"/></p>
		      </div>
		      <label class="col-sm-2 control-label">开户行名称</label>
		        <div class="col-sm-4">
		      		<p class="form-control-static"><s:property value="#request.busInfo.bankName"/></p>
		        </div>
	    	</div>
	    	<div class="form-group">
				<label class="col-sm-2 control-label">银行账户</label>
		        <div class="col-sm-4">
		      		<p class="form-control-static"><s:property value="#request.busInfo.bankAccount"/></p>
		        </div>
		        
	    	</div>
	    	<div class="form-group">
				<label class="col-sm-2 control-label">营业年限</label>
		        <div class="col-sm-4">
		      		<p class="form-control-static"><s:property value="#request.busInfo.limitYear"/></p>
		        </div>
		        <label class="col-sm-2 control-label">企业规模</label>
			    <div class="col-sm-4">
			      	<p class="form-control-static"><s:property value="#request.busInfo.scale"/></p>
			    </div>
	    	</div>
	    	<div class="form-group">
				<label class="col-sm-2 control-label">主营业务</label>
		        <div class="col-sm-4">
		      		<p class="form-control-static"><s:property value="#request.busInfo.domain"/></p>
		        </div>
		        <label class="col-sm-2 control-label">主要业务应用</label>
			    <div class="col-sm-4">
			      	<p class="form-control-static"><s:property value="#request.busInfo.busDomain"/></p>
			    </div>
	    	</div>
	    	<div class="form-group">
				<label class="col-sm-2 control-label">地区</label>
		        <div class="col-sm-4">
		      		<p class="form-control-static">
			      		<s:property value="#request.busInfo.country"/>
			      		<s:if test="#request.busInfo.country != '' && #request.busInfo.provinceDesc != ''">,</s:if>
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
		        <label class="col-sm-2 control-label">营业执照</label>
			    <div class="col-sm-4">
			    	<s:if test="#request.busInfo.licenceImg != null && #request.busInfo.licenceImg != ''">
				      	<img class="img-thumbnail img-thumbnail-size" alt="" src="${imgUrl}<s:property value="#request.busInfo.licenceImg"/>" 
		      		 	 onmouseenter="openImg(this,'营业执照')" onmouseleave="closeImg(this)">
			    	</s:if>
			    </div>
			    <label class="col-sm-2 control-label">添加时间</label>
			    <div class="col-sm-4">
			      	<p class="form-control-static"><s:property value="#request.busInfo.addTimeStr"/></p>
			    </div>
	    	</div>
	    	<div class="form-group">
				<label class="col-sm-2 control-label">法人代表身份证件</label>
		        <div class="col-sm-4">
		        	<s:if test="#request.busInfo.corporateImg != null && #request.busInfo.corporateImg != ''">
			      		<img class="img-thumbnail img-thumbnail-size" alt="" src="${imgUrl}<s:property value="#request.busInfo.corporateImg"/>" 
		      		 	 onmouseenter="openImg(this,'法人代表')" onmouseleave="closeImg(this)">
		        	</s:if>
		        </div>
		        <label class="col-sm-2 control-label">办理人身份证件</label>
			    <div class="col-sm-4">
			    	<s:if test="#request.busInfo.transatorImg != null && #request.busInfo.transatorImg != ''">
				      	<img class="img-thumbnail img-thumbnail-size" alt="" src="${imgUrl}<s:property value="#request.busInfo.transatorImg"/>" 
		      		 	 onmouseenter="openImg(this,'办理人')" onmouseleave="closeImg(this)">
			    	</s:if>
			    </div>
	    	</div>
	    </form>
    </s:if>
    <s:elseif test="#request.busUser.userType == 3">
    	<form id="userInfoForm" class="form-horizontal" role="form">
	  		<h5 class="form-header">用户信息</h5>
	  		<hr class="dashed">
			<div class="form-group">
		      <label class="col-sm-2 control-label">真实姓名</label>
		      <div class="col-sm-4">
		        <p class="form-control-static"><s:property value="#request.userInfo.realName"/></p>
		      </div>
		      <label class="col-sm-2 control-label">性别</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static">
		      		<s:if test="#request.userInfo.sex == 1">男</s:if>
		      		<s:elseif test="#request.userInfo.sex == 2">女</s:elseif>
		      		<s:else>未知</s:else>
		      	</p>
		      </div>
	    	</div>
	    	<div class="form-group">
		      <label class="col-sm-2 control-label">身份证号</label>
		      <div class="col-sm-4">
		        <p class="form-control-static"><s:property value="#request.userInfo.cardNo"/></p>
		      </div>
		      <label class="col-sm-2 control-label">出生日期</label>
		        <div class="col-sm-4">
		      		<p class="form-control-static"><s:property value="#request.userInfo.birthdayDesc"/></p>
		        </div>
	    	</div>
	    	<div class="form-group">
				<label class="col-sm-2 control-label">身份证件</label>
		        <div class="col-sm-4">
		        	<s:if test="#request.userInfo.front != null && #request.userInfo.front != ''">
			      		<img class="img-thumbnail img-thumbnail-size" alt="" src="${imgUrl}<s:property value="#request.userInfo.front"/>" 
			      		 	 onmouseenter="openImg(this,'身份证件')" onmouseleave="closeImg(this)">
		        	</s:if>
		        </div>
		        <label class="col-sm-2 control-label">行驶证</label>
		        <div class="col-sm-4">
		        	<s:if test="#request.userInfo.pic != null && #request.userInfo.pic != ''">
			      		<img class="img-thumbnail img-thumbnail-size" alt="" src="${imgUrl}<s:property value="#request.userInfo.pic"/>" 
			      		 	 onmouseenter="openImg(this,'行驶证')" onmouseleave="closeImg(this)">
		        	</s:if>
		        </div>
	    	</div>
	    	<div class="form-group">
				<label class="col-sm-2 control-label">地区</label>
		        <div class="col-sm-4">
		      		<p class="form-control-static"><s:property value="#request.userInfo.provinceDesc"/>
		      		<s:if test="#request.userInfo.provinceDesc != '' && #request.userInfo.cityDesc != ''">,</s:if>
		      		<s:property value="#request.userInfo.cityDesc"/></p>
		        </div>
		        <label class="col-sm-2 control-label">车品牌</label>
			    <div class="col-sm-4">
			      	<p class="form-control-static"><s:property value="#request.userInfo.brandDesc"/></p>
			    </div>
	    	</div>
	    	<div class="form-group">
				<label class="col-sm-2 control-label">车架号</label>
		        <div class="col-sm-4">
		      		<p class="form-control-static"><s:property value="#request.userInfo.vin"/></p>
		        </div>
		        <label class="col-sm-2 control-label">车型号</label>
			    <div class="col-sm-4">
			      	<p class="form-control-static"><s:property value="#request.userInfo.modelDesc"/></p>
			    </div>
	    	</div>
	    	<div class="form-group">
		        <label class="col-sm-2 control-label">车牌号</label>
			    <div class="col-sm-4">
			      	<p class="form-control-static"><s:property value="#request.userInfo.plateNo"/></p>
			    </div>
			    <label class="col-sm-2 control-label">签名</label>
			    <div class="col-sm-4">
			      	<p class="form-control-static"><s:property value="#request.userInfo.sign"/></p>
			    </div>
	    	</div>
	    </form>
    </s:elseif>
    
    <div class="form-group">
      <div class="col-sm-offset-2 col-sm-10" >
         <button type="button" class="btn btn-primary" onclick="gobackAndReload()">返回</button>
      </div>
  	</div>
  </div>
</body>
</html>

