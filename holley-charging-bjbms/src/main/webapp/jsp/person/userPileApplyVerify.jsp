<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>个人桩代管审核</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
  	<div id="pileApplyForm"><!-- 开票信息表单 -->
	    <form class="form-horizontal" role="form">
			<div class="form-group">
		      <label class="col-sm-2 control-label">申请编码</label>
		      <div class="col-sm-4">
		      	<p id="applyid" class="form-control-static"><s:property value="#request.pileApply.id"/></p>
		      </div>
		      <label class="col-sm-2 control-label">真实姓名</label>
		      <div class="col-sm-4">
		      	<p id="accRealName" class="form-control-static"><s:property value="#request.pileApply.realName"/></p>
		      </div>
	    	</div>
	    	<div class="form-group">
		      <label class="col-sm-2 control-label">联系方式</label>
		      <div class="col-sm-4">
		      	<p id="bankName" class="form-control-static"><s:property value="#request.pileApply.phone"/></p>
		      </div>
		      <label class="col-sm-2 control-label">电桩地址</label>
		      <div class="col-sm-4">
		      	<p id="bankAccount" class="form-control-static"><s:property value="#request.pileApply.address"/></p>
		      </div>
	    	</div>
			<div class="form-group">
		      <label class="col-sm-2 control-label">备注</label>
		      <div class="col-sm-4">
		      	<p id="addTimeStr" class="form-control-static"><s:property value="#request.pileApply.remark"/></p>
		      </div>
		      <label class="col-sm-2 control-label">申请时间</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.pileApply.addTimeDesc"/></p>
		      </div>
	    	</div>
			<div class="form-group">
		      <label class="col-sm-2 control-label">申请人昵称</label>
		      <div class="col-sm-4">
		      	<p id="addTimeStr" class="form-control-static"><s:property value="#request.pileApply.username"/></p>
		      </div>
		      <label class="col-sm-2 control-label">申请人手机号码</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.pileApply.userphone"/></p>
		      </div>
	    	</div>
	    	<s:if test="#request.pileApply.validStatus > 1">
				<div class="form-group">
			      <label class="col-sm-2 control-label">审核状态</label>
			      <div class="col-sm-4">
			      	<p id="addTimeStr" class="form-control-static"><s:property value="#request.pileApply.validStatusDesc"/></p>
			      </div>
			      <label class="col-sm-2 control-label">审核时间</label>
			      <div class="col-sm-4">
			      	<p class="form-control-static"><s:property value="#request.pileApply.validTimeDesc"/></p>
			      </div>
		    	</div>
				<div class="form-group">
			      <label class="col-sm-2 control-label">审核备注</label>
			      <div class="col-sm-4">
			      	<p id="addTimeStr" class="form-control-static"><s:property value="#request.pileApply.validRemark"/></p>
			      </div>
		    	</div>
	    	</s:if>
	    	<s:else>
	    		<div class="form-group">
			      <label class="col-sm-2 control-label"><span style="color: red;">*</span>审核结果</label>
			      <div class="col-sm-4">
			      	<select id="validStatus" name="validStatus" class="form-control">
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
			      <label class="col-sm-2 control-label">审核备注</label>
			      <div class="col-sm-4">
				  	<textarea id="validRemark" class="form-control" rows="3"></textarea>
			      </div>
		    	</div>
	    	</s:else>
	    	<div class="form-group">
		      <div class="col-sm-offset-2 col-sm-10">
		      	<s:if test="#request.requestType == 'isverify'"><!-- 开票审核 -->
			         <button id="saveBtn" type="button" class="btn btn-primary">保存</button>
			         <button type="button" class="btn btn-primary" onclick="gobackAndReload()">取消</button>
		      	</s:if>
		      	<s:else>
		      		 <button type="button" class="btn btn-primary" onclick="gobackAndReload()">返回</button>
		      	</s:else>
		      </div>
	   		</div>
	    </form>
  	</div>
</div>
  <script src="res/js/person/userPileApplyVerify.js" type="text/javascript"></script>
</body>
</html>

