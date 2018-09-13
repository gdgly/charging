<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>开票信息审核</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
	<div id="billsTable"><!-- 开票依据列表 -->
	  	<h5 class="form-header">开票依据</h5>
	  	<hr class="dashed">
	  	<table class="table table-condensed table-hover">
	   		<thead>
	   			<tr>
	   				<th>资金统计id</th>
	   				<th>结算周期</th>
	   				<th>预约费</th>
	   				<th>充电费</th>
	   				<th>服务费</th>
	   				<th>停车费</th>
	   				<th>小计</th>
	   			</tr>
	   		</thead>
		   	<tbody>
			   	<s:iterator value="#request.billsList" var="item" status="st">
			   		<tr>
			   			<td><s:property value="id" /></td>
			   			<td><s:property value="checkCycle" /></td>
			   			<td><s:property value="appFeeOut" /></td>
			   			<td><s:property value="chaFeeOut" /></td>
			   			<td><s:property value="serviceFeeOut" /></td>
			   			<td><s:property value="parkFeeOut" /></td>
			   			<td><s:property value="totalFeeOutDesc" /></td>
			   		</tr>
			   	</s:iterator>
			   	<s:if test="#request.billsList.size > 1">
			   		<tr>
			   			<td></td>
			   			<td></td>
			   			<td></td>
			   			<td></td>
			   			<td></td>
			   			<td>费用合计：</td>
			   			<td><span style="color: red;"><s:property value="#request.totalFee"/></span></td>
			   		</tr>
			   	</s:if>
		   	</tbody>
		</table>
	</div>
  	<div id="receiptForm"><!-- 开票信息表单 -->
	  	<h5 class="form-header">开票信息</h5>
	  	<hr class="dashed">
	    <form class="form-horizontal" role="form">
			<div class="form-group">
		      <label class="col-sm-2 control-label">开票id</label>
		      <div class="col-sm-4">
		      	<p id="receiptid" class="form-control-static"><s:property value="#request.userReceipt.id"/></p>
		      </div>
		      <label class="col-sm-2 control-label">开票状态</label>
		      <div class="col-sm-4">
		      	<p id="accRealName" class="form-control-static"><s:property value="#request.userReceipt.statusDesc"/></p>
		      </div>
	    	</div>
	    	<div class="form-group">
		      <label class="col-sm-2 control-label">开票月份</label>
		      <div class="col-sm-4">
		      	<p id="bankName" class="form-control-static"><s:property value="#request.userReceipt.time"/></p>
		      </div>
		      <label class="col-sm-2 control-label">开票金额</label>
		      <div class="col-sm-4">
		      	<p id="bankAccount" class="form-control-static" style="color: red;"><s:property value="#request.userReceipt.moneyDesc"/></p>
		      </div>
	    	</div>
			<div class="form-group">
		      <label class="col-sm-2 control-label">发票类型</label>
		      <div class="col-sm-4">
		      	<p id="addTimeStr" class="form-control-static"><s:property value="#request.userReceipt.billTypeDesc"/></p>
		      </div>
		      <label class="col-sm-2 control-label">发票抬头</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.userReceipt.billHead"/></p>
		      </div>
	    	</div>
			<div class="form-group">
		      <label class="col-sm-2 control-label">收件人</label>
		      <div class="col-sm-4">
		      	<p id="addTimeStr" class="form-control-static"><s:property value="#request.userReceipt.recipient"/></p>
		      </div>
		      <label class="col-sm-2 control-label">收件人联系方式</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.userReceipt.phone"/></p>
		      </div>
	    	</div>
			<div class="form-group">
		      <label class="col-sm-2 control-label">收件地址</label>
		      <div class="col-sm-4">
		      	<p id="addTimeStr" class="form-control-static"><s:property value="#request.userReceipt.address"/></p>
		      </div>
		      <label class="col-sm-2 control-label">申请人编码</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.userReceipt.userId"/></p>
		      </div>
	    	</div>
			<div class="form-group">
		      <label class="col-sm-2 control-label">申请人昵称</label>
		      <div class="col-sm-4">
		      	<p id="addTimeStr" class="form-control-static"><s:property value="#request.userReceipt.username"/></p>
		      </div>
		      <label class="col-sm-2 control-label">申请人手机号码</label>
		      <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.userReceipt.userphone"/></p>
		      </div>
	    	</div>
	    	<s:if test="#request.requestType == 'isverify'"><!-- 开票审核 -->
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
								<s:if test="value == 1">
									<input type="checkbox" value="<s:property value='value'/>" name="noticeType" checked="checked"><s:property value='text' />
								</s:if>
								<s:else>
									<input type="checkbox" value="<s:property value='value'/>" name="noticeType"><s:property value='text' />
								</s:else>
							</label>
						</s:iterator>
				     </div>
				</div>
				<div id="expressDiv" class="form-group">
			      <label class="col-sm-2 control-label"><span style="color: red;">*</span>快递公司</label>
			      <div class="col-sm-4">
			      	<input type="text" class="form-control" id="expressCompany" name="expressCompany" maxlength="25" 
			      	value="<s:property value='#request.userReceipt.expressCompany'/>" placeholder="请输入如：顺丰快递"/>
			      </div>
			      <label class="col-sm-2 control-label"><span style="color: red;">*</span>快递单号</label>
			      <div class="col-sm-4">
			      	<input type="text" class="form-control" id="expressNum" name="expressNum" maxlength="25" 
			      	value="<s:property value='#request.userReceipt.expressNum'/>" placeholder="请输入如：203449111293"/>
			      </div>
		    	</div>
				<div id="validRemarkDiv" class="form-group hide">
					<label class="col-sm-2 control-label"><span style="color: red;">*</span>审核失败原因</label>
				    <div class="col-sm-4">
					    <textarea id="validRemark" class="form-control" rows="3"></textarea>
				    </div>
		    	</div>
	    	</s:if>
	    	<s:else><!-- 开票详细 -->
	    		<div class="form-group">
			      <label class="col-sm-2 control-label">快递公司</label>
			      <div class="col-sm-4">
			      	<p class="form-control-static"><s:property value="#request.userReceipt.expressCompany"/></p>
			      </div>
			      <label class="col-sm-2 control-label">快递单号</label>
			      <div class="col-sm-4">
			      	<p class="form-control-static"><s:property value="#request.userReceipt.expressNum"/></p>
			      </div>
		    	</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">审核失败原因</label>
				    <div class="col-sm-4">
				    	<p class="form-control-static"><s:property value="#request.userReceipt.validRemark"/></p>
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
  <script src="res/js/person/userReceiptVerify.js" type="text/javascript"></script>
</body>
</html>

