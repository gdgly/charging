<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>运营商实名转账审核</title>
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
	      	<p id="accRealName" class="form-control-static"><s:property value="#request.businessReal.accRealName"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">开户银行</label>
	      <div class="col-sm-4">
	      	<p id="bankName" class="form-control-static"><s:property value="#request.businessReal.bankName"/></p>
	      </div>
	      <label class="col-sm-2 control-label">银行账号</label>
	      <div class="col-sm-4">
	      	<p id="bankAccount" class="form-control-static"><s:property value="#request.businessReal.bankAccount"/></p>
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
    	 	<label for="name" class="col-sm-2 control-label">转账金额</label>
		    <div class="col-sm-4">
		    	<p id="validMoney" class="form-control-static"><s:property value="#request.businessReal.validMoney"/> 元</p>
		    </div>
		    <label for="name" class="col-sm-2 control-label">转账时填写的备注内容：</label>
		    <div class="col-sm-4">
			    <p id="validMoney" class="form-control-static">您的校验码为<span style="color:red"> <s:property value="#request.businessReal.validCode"/></span></p>
		    </div>
		</div>
		<div class="form-group">
	     <label class="col-sm-2 control-label"><span style="color: red;">*</span>审核结果</label>
		    <div class="col-sm-4">
				<select id="validStatus" name="validStatus" class="form-control" onchange="onStatusChange()">
			     	<s:iterator value="#request.verifyStatusList" var="item" status="st">
			     		<s:if test="#request.validStatus == #item.value">
			     			<option value="${item.value}" selected="selected">${item.text}</option>
			     		</s:if>
			     		<s:else>
				     		<option value="${item.value}">${item.text}</option>
			     		</s:else>
			     	</s:iterator>
		     	</select>
		  	</div>
		    <label class="col-sm-2 control-label">通知方式</label>
		    <div class="col-sm-4">
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
			    <textarea id="remark" class="form-control" rows="3"></textarea>
		    </div>
		</div>
    	<div class="form-group">
	      <div class="col-sm-offset-2 col-sm-10">
	         <button id="saveBtn" type="button" class="btn btn-primary">保存</button>
	         <button id="cancelBtn" type="button" class="btn btn-primary">取消</button>
	      </div>
   </div>
    </form>
</div>
  <script type="text/javascript">
  	var businfoid = <s:property value="#request.businessReal.busInfoId"/>;
  </script>
  <script src="res/js/business/busRealTransfer.js" type="text/javascript"></script>
</body>
</html>

