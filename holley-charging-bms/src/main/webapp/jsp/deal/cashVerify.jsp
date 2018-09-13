<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>运营商审核信息</title>
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
	      <label class="col-sm-2 control-label">真实姓名</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.busCash.realName"/></p>
	      </div>
	      <label class="col-sm-2 control-label">手机号码</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.busCash.phone"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">提现方式</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.busCash.cashWayDesc"/></p>
	      </div>
	      <label class="col-sm-2 control-label">提现账户</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.busCash.account"/></p>
	      </div>
    	</div>
    	<s:if test="#request.busCash.cashWay==2">
	    	<div class="form-group">
		      <label class="col-sm-2 control-label">开户姓名</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.busCash.accRealName"/></p>
		      </div>
		      <label class="col-sm-2 control-label">开户银行</label>
		      <div class="col-sm-4">
		      	<p id="addTimeStr" class="form-control-static"><s:property value="#request.busCash.bankName"/></p>
		      </div>
	    	</div>
    	</s:if>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">提现金额</label>
	      <div class="col-sm-4">
	      	<p id="addTimeStr" class="form-control-static"><s:property value="#request.busCash.money"/></p>
	      </div>
		    <label class="col-sm-2 control-label">提现状态</label>
		    <div class="col-sm-4">
		      <p id="addTimeStr" class="form-control-static"><s:property value="#request.busCash.cashStatusDesc"/></p>
		    </div>
		</div>
		<div class="form-group">
	      <label class="col-sm-2 control-label">备注</label>
	      <div class="col-sm-4">
	      	<p id="addTimeStr" class="form-control-static"><s:property value="#request.busCash.remark"/></p>
	      </div>
		    <label class="col-sm-2 control-label">申请时间</label>
		    <div class="col-sm-4">
		      <p id="addTimeStr" class="form-control-static"><s:property value="#request.busCash.addTimeDesc"/></p>
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
		    <div id="noticeTypeDiv" class="hide">
			  <label class="col-sm-2 control-label">通知方式</label>
			  <div class="col-sm-4">
			  	<s:iterator value="#request.noticeTypeList" status="statu" var="item">
					<label class="checkbox-inline"> 
						<input type="checkbox" value="<s:property value='value'/>" name="noticeType" checked="checked"><s:property value='text' />
					</label>
				</s:iterator>
			  </div>
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
	         <button id="cancelBtn" type="button" class="btn btn-primary">取消</button>
	      </div>
   </div>
    </form>
</div>
  <script src="res/js/deal/cashVerify.js" type="text/javascript"></script>
  <script type="text/javascript">
  	var id = <s:property value='#request.busCash.id'/>;
  </script>
</body>
</html>

