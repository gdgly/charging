<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>用户管理</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container" id="userListContainer">
    <form id="conditionFrom" class="form-inline" role="form" action="sys/user_queryList.action">
	    <div class="form-group">
			<input id="keyword" name="keyword" type="text" class="form-control" placeholder="请输入用户昵称/手机号码" style="width: 190px;"/>
			
			<select id="userType" name="usertype" class="form-control">
				<s:if test="#request.webUser.usertype.value != 9">
					<s:iterator value="#request.userTypeList" var="item" status="st">
			     	<s:if test="#item.value == 1 || #item.value == 3 || #item.value == 9">
			     	<option value="${item.value}">${item.text}</option>
			     	</s:if>
			     	</s:iterator>
				</s:if>
		     	<s:else>
		     		<option value="3">个人（平台）</option>
		     	</s:else>
		     </select>
		     		<select id="groupId" name="groupId" class="form-control hide">
						<option value="0">系统平台</option>
						<s:iterator value="#request.companyList" status="statu" id="item">
							<option value=<s:property value="id"/> ><s:property value="username"/></option>
						</s:iterator>
					</select>
		     <select id="realStatus" name="realstatus" class="form-control hide">
		     	<option value="0">实名状态</option>
		     	<%-- <s:iterator value="#request.realStatusList" var="item" status="st">
		     		<s:if test="#request.realStatus == #item.value">
		     			<option value="${item.value}" selected="selected">${item.text}</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.value}">${item.text}</option>
		     		</s:else>
		     	</s:iterator> --%>
		     </select>
		     <select id="lockStatus" name="lockstatus" class="form-control hide">
		     	<option value="0">是否锁定</option>
		     	<%-- <s:iterator value="#request.lockStatusList" var="item" status="st">
		     		<s:if test="#request.lockStatus == #item.value">
		     			<option value="${item.value}" selected="selected">${item.text}</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.value}">${item.text}</option>
		     		</s:else>
		     	</s:iterator> --%>
		     </select>
		     <input id="isExport" name="isExport" value="true" type="hidden">
			 <input id="fileName" name="fileName" value="用户列表" type="hidden">
		</div>
		 <div class="form-group pull-right">
			<button type="button" id="queryBtn" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
		    <button type="button" id="addBtn" class="btn btn-primary"><i class="fa fa-plus"></i> 新增</button>
		    <button type="button" id="exportBtn" class="btn btn-primary"><i class="fa fa-print"></i> 导出</button>
		 </div>
    </form>
    <!-- 表格 -->
    <div id="userTableDiv">
	    <table class="table table-condensed table-hover" id="userTable">
	   		<thead>
	   			<tr>
	   				<!-- <th>用户编码</th> -->
	   				<th id="nameAndRealName">用户昵称</th>
	   				<th>手机号码</th>
	   				<!-- <th>实名状态</th> -->
	   				<th>用户类型</th>
	   				<th>用户角色</th>
	   				<!-- <th id="usableMoeby" class="hide">账户余额</th> -->
	   				<th>注册时间</th>
	   				<th>操作</th>
	   			</tr>
	   		</thead>
		   	<tbody></tbody>
		</table>
    </div>
     <!-- 分页条 -->
    <%@include file="../common/global/pagingtoolbar.jsp" %>
  </div>
  <div class="container hide" id="userRechargeContainer">
    <span class="glyphicon glyphicon-menu-left" onclick="returnUserList();" data-toggle="tooltip" title="返回" aria-hidden="true"  style="cursor:pointer;font-weight: bold;font-size: 16px;"></span>
     
     		<form class="form-horizontal" role="form" id="userRechargeForm">
     			<div class="form-group">
					<label for="username" class="col-sm-2 control-label">用户</label>
					<div class="col-sm-8">
					 <input id="userId" value="0" type="hidden"/>
					<p id="username" style="margin-top: 4px;"></p>
					</div>
				</div>
     			<div class="form-group">
					<label for="usableMoney" class="col-sm-2 control-label">账户余额</label>
					<div class="col-sm-8">
					<p id="usableMoney" style="margin-top: 4px;"><span style="color:red;font-size:17px;">0</span> 元</p>
					</div>
				</div>
		
					<div class="form-group">
						<label for="rechargeMoney" class="col-sm-2 control-label"><span
							style="color: red;">*</span>充值金额：</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="rechargeMoney"
								name="rechargeMoney" maxlength="30" placeholder="请输入充值金额"/>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
						<button id="doRechargeBtn" type="button" class="btn btn-primary btn-sm">提交</button>
						</div>
					</div>
			</form> 
  </div>
<!-- 模态框（Modal） -->
<div class="modal fade" id="modifyPwdModal2" tabindex="-1" role="dialog" aria-labelledby="modifyPwdModal2Label" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h4 class="modal-title" id="modifyPwdModal2Label">修改密码</h4>
         </div>
         <div class="modal-body">
		 	<form class="form-horizontal" role="form" id="pwdform"  enctype="multipart/form-data">
			<!-- 	<div class="form-group">
			      <label class="col-sm-3 control-label">旧密码</label>
			      <div class="col-sm-9">
			      	<input type="password" class="form-control" id="oldpwd" onblur="ispassword(this);" placeholder="请输入旧密码" maxlength="16">
			      </div>
		    	</div> -->
		    	<div class="form-group">
			      <label class="col-sm-3 control-label">新密码</label>
			      <div class="col-sm-9">
			      <input type="hidden" id="userIdForModifyPwd" value=""/>
			      	<input type="password" class="form-control" id="newpwd2" onblur="ispassword(this);" placeholder="请输入新密码" maxlength="16">
			      </div>
		    	</div>
		    	<div class="form-group">
			      <label class="col-sm-3 control-label">重新输入新密码</label>
			      <div class="col-sm-9">
			      	<input type="password" class="form-control" id="repeatpwd2" onblur="ispassword(this);" placeholder="请再一次输入新密码" maxlength="16">
			      </div>
		    	</div>
			</form>
         </div>
         <div class="modal-footer">
            <button type="button" id="modifyPwdModal2Btn" class="btn btn-primary">确定</button>
            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
         </div>
      </div><!-- /.modal-content -->
</div><!-- /.modal -->
</div>
  <script src="res/js/sys/userList.js" type="text/javascript"></script>
  <script type="text/javascript">
  var webuserid = ${webUser.userId};
  </script>
</body>
</html>

