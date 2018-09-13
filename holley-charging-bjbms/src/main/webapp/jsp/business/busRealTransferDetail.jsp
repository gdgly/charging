<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
 <title>运营商审核详细信息</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
    <form class="form-horizontal" role="form">
		<div class="form-group">
	      <label class="col-sm-2 control-label">企业名称</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.businessReal.busName"/></p>
	      </div>
	      <label class="col-sm-2 control-label">开户姓名</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.businessReal.accRealName"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">开户银行</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.businessReal.bankName"/></p>
	      </div>
	      <label class="col-sm-2 control-label">银行账号</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.businessReal.bankAccount"/></p>
	      </div>
    	</div>
    	<div class="form-group">
    	 	<label for="name" class="col-sm-2 control-label">营业执照照片</label>
		    <div class="col-sm-4">
		    	<s:if test="#request.businessReal.licenceImg != null && #request.businessReal.licenceImg != ''">
		      		<img class="img-thumbnail  img-thumbnail-size" alt="" src="${imgUrl}<s:property value="#request.businessReal.licenceImg"/>"
		      		     onmouseenter="openImg(this,'营业执照照片')" onmouseleave="closeImg(this)">
		    	</s:if>
	      	</div>
		    <label for="name" class="col-sm-2 control-label">法人代表身份证</label>
		    <div class="col-sm-4">
		    	<s:if test="#request.businessReal.corporateImg != null && #request.businessReal.corporateImg != ''">
		      		<img class="img-thumbnail img-thumbnail-size" alt="" src="${imgUrl}<s:property value="#request.businessReal.corporateImg"/>"
		      		     onmouseenter="openImg(this,'法人代表身份证')" onmouseleave="closeImg(this)">
		    	</s:if>
	      	</div>
		</div>
		<div class="form-group">
	      	<label for="name" class="col-sm-2 control-label">办理人身份证</label>
		    <div class="col-sm-4">
		    	<s:if test="#request.businessReal.transatorImg != null && #request.businessReal.transatorImg != ''">
		      		<img class="img-thumbnail img-thumbnail-size" alt="" src="${imgUrl}<s:property value="#request.businessReal.transatorImg"/>"
		      		     onmouseenter="openImg(this,'办理人身份证')" onmouseleave="closeImg(this)">
		    	</s:if>
	      	</div>
		</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">申请人</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.businessReal.userId"/></p>
	      </div>
	      <label class="col-sm-2 control-label">申请时间</label>
	      <div class="col-sm-4">
	      	<p id="addTimeStr" class="form-control-static"><s:property value="#request.businessReal.addTimeStr"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">审核状态</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.businessReal.validStatusDesc"/></p>
	      </div>
		    <label for="name" class="col-sm-2 control-label">转账金额</label>
		    <div class="col-sm-4">
		      <p class="form-control-static"><s:property value="#request.businessReal.validMoney"/> 元</p>
		    </div>
		</div>
		<div class="form-group">
		    <label for="name" class="col-sm-2 control-label">审核备注</label>
		    <div class="col-sm-4">
		      <p class="form-control-static"><s:property value="#request.businessReal.remark"/></p>
		    </div>
		    <label for="name" class="col-sm-2 control-label">转账时填写的备注内容：</label>
		    <div class="col-sm-4">
			    <p id="validMoney" class="form-control-static">您的校验码为<span style="color:red"> <s:property value="#request.businessReal.validCode"/></span></p>
		    </div>
		</div>
    	<div class="form-group">
	      <div class="col-sm-offset-2 col-sm-10">
	         <button id="backBtn" type="button" class="btn btn-primary" onclick="javascript:history.back(-1);">返回</button>
	      </div>
   </div>
    </form>
</div>
</body>
</html>

