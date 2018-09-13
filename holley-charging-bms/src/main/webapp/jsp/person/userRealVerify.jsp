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
    		<label class="col-sm-2 control-label"><span style="color: red;">*</span>审核结果</label>
		    <div class="col-sm-4">
				<select id="validStatus" name="validStatus" class="form-control" onchange="onStatusChange()">
			     	<s:iterator value="#request.statusList" var="item" status="st">
			     		<s:if test="#request.status == #item.value">
			     			<option value="${item.value}" selected="selected">${item.text}</option>
			     		</s:if>
			     		<s:else>
				     		<option value="${item.value}">${item.text}</option>
			     		</s:else>
			     	</s:iterator>
		     	</select>
		    </div>
		    <label class="col-sm-2 control-label">通知方式</label>
		    <div class="col-sm-4" id="noticeTypeDiv">
		      	<s:iterator value="#request.noticeTypeList" status="statu" var="item">
					<label class="checkbox-inline"> 
						<input type="checkbox" value="<s:property value='value'/>" name="noticeType" checked="checked"><s:property value='text' />
					</label>
				</s:iterator>
		     </div>
		</div>
		<div id="validRemarkDiv" class="form-group hide">
			<label class="col-sm-2 control-label"><span style="color: red;">*</span>审核失败原因</label>
		    <div class="col-sm-4">
			    <textarea id="validRemark" class="form-control" rows="3"></textarea>
		    </div>
		</div>
    	<div class="form-group">
	      <div class="col-sm-offset-2 col-sm-10">
	         <button id="saveBtn" type="button" class="btn btn-primary">保存</button>
	         <button id="cancelBtn" type="button" class="btn btn-primary" onclick="gobackAndReload()">取消</button>
	      </div>
   		</div>
    </form>
</div>
  <script type="text/javascript">
  	var userid = '<s:property value="#request.userReal.userId"/>';
  </script>
  
  <script src="res/js/person/userRealVerify.js" type="text/javascript"></script>
</body>
</html>

