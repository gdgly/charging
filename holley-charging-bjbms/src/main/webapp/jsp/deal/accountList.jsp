<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>账户查询</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
    <div id="accountResult">
	    <form id="accountFrom" class="form-inline" role="form" action="deal/account_queryList.action">
		    <div class="form-group">
				<input id="keyword" name="keyword" type="text" class="form-control" placeholder="请输入用户姓名/手机号码" style="width: 190px;"/>
				<s:if test="#request.webUser.usertype.value != 9">
				<select id="groupId" name="groupId" class="form-control">
						<option value="-1">所属子公司</option>
						<s:iterator value="#request.companyList" status="statu" id="item">
						<option value=<s:property value="id"/> ><s:property value="username"/></option>
						</s:iterator>
				</select>
				<select id="personUserType" name="personUserType" class="form-control">
						<s:iterator value="#request.personUserTypeList" status="statu" id="item">
						<option value=<s:property value="value"/> ><s:property value="text"/></option>
						</s:iterator>
				</select>
				</s:if>
				
				<select id="userType" name="usertype" class="form-control hide">
			     	<option value="0">用户类型</option>
			     	<s:iterator value="#request.userTypeList" var="item" status="st">
			     		<s:if test="#request.userType == #item.value">
			     			<option value="${item.value}" selected="selected">${item.text}</option>
			     		</s:if>
			     		<s:else>
				     		<option value="${item.value}">${item.text}</option>
			     		</s:else>
			     	</s:iterator>
			     </select>
			     <select id="accountStatus" name="accountstatus" class="form-control hide">
			     	<option value="0">账户状态</option>
			     	<s:iterator value="#request.statusList" var="item" status="st">
			     		<s:if test="#request.accountStatus == #item.value">
			     			<option value="${item.value}" selected="selected">${item.text}</option>
			     		</s:if>
			     		<s:else>
				     		<option value="${item.value}">${item.text}</option>
			     		</s:else>
			     	</s:iterator>
			     </select>
			     <input id="isExport" name="isExport" value="true" type="hidden">
			 	 <input id="fileName" name="fileName" value="账户列表" type="hidden">
			</div>
			 <div class="form-group pull-right">
				<button type="button" id="queryBtn" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
				<button type="button" id="exportBtn" class="btn btn-primary"><i class="fa fa-print"></i> 导出</button>
			 </div>
	    </form>
	    <!-- 表格 -->
	    <div id="accountTableDiv">
		    <table class="table table-condensed table-hover" id="accountTable">
		   		<thead>
		   			<tr>
		   				<th>用户编码</th>
		   				<th>用户姓名</th>
		   				<th>所属子公司</th>
		   				<th>手机号码</th>
		   				<th>用户类型</th>
		   				<!-- <th>总金额</th> -->
		   				<th>账户金额</th>
		   				<!-- <th>冻结金额</th> -->
		   				<th>状态</th>
		   				<th>更新时间</th>
			   			<s:if test="#request.webUser.usertype.value != 9">
			   			<th>操作</th>
			   			</s:if>
		   			</tr>
		   		</thead>
			   	<tbody></tbody>
			</table>
	    </div>
	     <!-- 分页条 -->
	    <%@include file="../common/global/pagingtoolbar.jsp" %>
    </div>
    
    <div id="cashResult" style="display: none;">
	    <span class="glyphicon glyphicon-menu-left" data-toggle="tooltip" title="返回" aria-hidden="true" 
	    style="cursor:pointer;font-weight: bold;font-size: 16px;" onclick="hideFreezeData()"></span>
		<label id="freezetotal" class="pull-right" style="color: red;"></label>
       	<table class="table table-condensed table-hover" id="freezeTable">
	   		<thead>
	   			<tr>
	   				<th>申请时间</th>
	   				<th>提现方式</th>
	   				<th>账户信息</th>
	   				<th>提现状态</th>
	   				<th>提现金额</th>
	   			</tr>
	   		</thead>
		   	<tbody></tbody>
		</table>
		<!-- 分页条 -->
        <%@include file="../common/global/pagingtoolbar2.jsp" %>
    </div>
  </div>
  <script type="text/javascript">
  var adminUserType = '${webUser.usertype.value}';
  </script>
  <script src="res/js/deal/accountList.js" type="text/javascript"></script>
</body>
</html>

