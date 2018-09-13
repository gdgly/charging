<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>个人审核信息</title>
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
	      <label class="col-sm-2 control-label">用户昵称</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.userReal.username"/></p>
	      </div>
	      <label class="col-sm-2 control-label">手机号码</label>
	      <div class="col-sm-4">
	      	<p id="accRealName" class="form-control-static"><s:property value="#request.userReal.phone"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">真实姓名</label>
	      <div class="col-sm-4">
	      	<p id="bankName" class="form-control-static"><s:property value="#request.userReal.realName"/></p>
	      </div>
	      <label class="col-sm-2 control-label">身份证号</label>
	      <div class="col-sm-4">
	      	<p id="bankAccount" class="form-control-static"><s:property value="#request.userReal.cardNum"/></p>
	      </div>
    	</div>
		<div class="form-group">
    	 	<label for="name" class="col-sm-2 control-label">身份证照片</label>
		    <div class="col-sm-4">
		    	<s:if test="#request.userReal.front != null && #request.userReal.front != ''">
		      		<img class="img-thumbnail img-thumbnail-size" alt="" src="${imgUrl}<s:property value="#request.userReal.front"/>" 
		      		 	 onmouseenter="openImg(this,'身份证照片')" onmouseleave="closeImg(this)">
		    	</s:if>
	      	</div>
		</div>
		<div class="form-group">
	      <label class="col-sm-2 control-label">提交时间</label>
	      <div class="col-sm-4">
	      	<p id="addTimeStr" class="form-control-static"><s:property value="#request.userReal.addTimeStr"/></p>
	      </div>
	      <label class="col-sm-2 control-label">状态</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.userReal.statusDesc"/></p>
	      </div>
    	</div>
    	<div class="form-group">
		    <label class="col-sm-2 control-label">审核备注</label>
		    <div class="col-sm-10">
			    <p id="remark" class="form-control-static" ><s:property value="#request.userReal.remark"/></p>
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

