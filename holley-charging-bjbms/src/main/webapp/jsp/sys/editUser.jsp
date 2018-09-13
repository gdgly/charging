<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>用户新增/修改</title>
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
  <s:if test="#request.busUser == null || #request.busUser.userType != 3">
  		<div class="form-group">
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>用户昵称</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="username" placeholder="用户昵称" name="username" maxlength="20" value="<s:property value='#request.busUser.username'/>"/>
	      </div>
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>角色</label>
	      <div class="col-sm-4">
	      	<select id="roleid" name="roleid" class="form-control">
		     	<s:iterator value="#request.roleList" var="item" status="st">
		     		<s:if test="#request.busUser.roleid == #item.id">
		     			<option value="${item.id}" selected="selected">${item.rolename}</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.id}">${item.rolename}</option>
		     		</s:else>
		     	</s:iterator>
		     </select>
	      </div>
    	</div>
    	<div class="form-group">
			<label class="col-sm-2 control-label">手机号码</label>
	        <div class="col-sm-4">
	      		<input type="text" class="form-control" placeholder="手机号码" id="phone" name="phone" maxlength="20" value="<s:property value='#request.busUser.phone'/>"/>
	        </div>
	        <s:if test="#request.requestType == 1">
	         <label class="col-sm-2 control-label"><span style="color: red;">*</span>密码</label>
	        <div class="col-sm-4">
	      		<input type="text" class="form-control" id="password" placeholder="用户密码" name="password" maxlength="20" />
	        </div>
	        </s:if>
    	</div>
  
  	</s:if> 
  	<s:else>
  		<div class="form-group">
		    <label class="col-sm-2 control-label"><span style="color: red;">*</span>用户姓名</label>
		    <div class="col-sm-4">
		    <input type="hidden" id="username" name="username" value="${busUser.username}"/>
		    <input type="hidden" id="roleid" name="roleid" value="${busUser.roleid}"/>
		      	<input disabled="disabled" type="text" class="form-control" id="username" placeholder="用户姓名" name="username" maxlength="20" value="<s:property value='#request.userInfo.realName'/>"/>
		    </div>
		    <label class="col-sm-2 control-label">手机号码</label>
	        <div class="col-sm-4">
	      		<input type="text" class="form-control" placeholder="手机号码" id="phone" name="phone" maxlength="20" value="<s:property value='#request.busUser.phone'/>"/>
	        </div> 
	     </div>
	     <div class="form-group">
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>所属子公司</label>
	      	<div class="col-sm-4">
					<select id="groupId" name="groupId" class="form-control">
						<option value="0">系统平台</option>
						<s:iterator value="#request.companyList" status="statu" id="item">
						<s:if test="#request.busUser.groupId == #item.id">
							<option selected="selected" value=<s:property value="id"/> ><s:property value="username"/></option>
						</s:if>
						<s:else>
							<option value=<s:property value="id"/> ><s:property value="username"/></option>
						</s:else>
						</s:iterator>
					</select>
			</div>
			<s:if test="#request.busUser.userType == 3">
			  <s:if test="#request.busUser.groupId == null || #request.busUser.groupId != -1">
			   <label for="groupId" class="col-sm-2 control-label">优惠方案：</label>
				<div class="col-sm-4">
						<select id="rebateId" name="rebateId" class="form-control">
							<option value="0">优惠方案</option>
							<s:iterator value="#request.rebateList" status="statu" id="item">
								<s:if test="#request.rebateBean != null && #request.rebateBean.rebateId == #item.id">
									<option selected="selected" value=<s:property value="id"/> ><s:property value="rebateDesc"/> 【折扣:<s:property value="rebate"/>】</option>
								</s:if>
								<s:else>
									<option value=<s:property value="id"/> ><s:property value="rebateDesc"/> 【折扣:<s:property value="rebate"/>】</option>
								</s:else>
								
							</s:iterator>
						</select>
				</div>
			  </s:if>
			 
			</s:if>
			
	     </div>
  	</s:else>
    	
   <%--  	<div id="groupUserInfoDiv" class="hide">
    	<hr/>
    	<h5>集团基本信息</h5>
    	<div class="form-group">
    	       <label class="col-sm-2 control-label"><span style="color: red;">*</span>集团名称</label>
	        <div class="col-sm-4">
	      		<input type="text" class="form-control" id="groupName" name="groupName" maxlength="20" value=""/>
	        </div>
	        	<label class="col-sm-2 control-label"><span style="color: red;">*</span>集团规模</label>
	        <div class="col-sm-4">
	      		<select id="scale" name="scale" class="form-control">
	      		<option value="0">请选择</option>
	      		<s:iterator id="item" value="#request.scaleTypeList">
	      		<option value="<s:property value="value"/>"><s:property value="name"/></option>
	      		</s:iterator>
	      		</select>
	        </div>
    	</div>
    	<div class="form-group">
		
	       <label class="col-sm-2 control-label"><span style="color: red;">*</span>所在地区</label>
	        <div class="col-sm-2">
	      		<select id="province" name="province" class="form-control">
	      		<option value="0">请选择省份</option>
	      		<s:iterator id="item" value="#request.provinceList">
	      		<option value="<s:property value="id"/>"><s:property value="name"/></option>
	      		</s:iterator>
	      		</select>
	        </div>
	       	<div class="col-sm-2">
	      		<select id="city" name="city" class="form-control">
	      		<option value="0">请选择市区</option>
	      		</select>
	        </div>
	        <label class="col-sm-2 control-label"><span style="color: red;">*</span>联系电话</label>
	        <div class="col-sm-4">
	      		<input type="text" class="form-control" id="tel" name="tel" maxlength="20" value="<s:property value='#request.busUser.phone'/>"/>
	        </div>	 
    	</div>

    	<div class="form-group">
	         <label class="col-sm-2 control-label"><span style="color: red;">*</span>主营业务</label>
	        <div class="col-sm-4">
	        <textarea rows="3" id="domain" name="domain" class="form-control" name="domain"></textarea>
	        </div>
	          <label class="col-sm-2 control-label"><span style="color: red;">*</span>详细地址</label>
	        <div class="col-sm-4">
	        <textarea rows="3" id="address" name="address" class="form-control" name="domain"></textarea>
	        </div>
    	</div>
    	</div> --%>
    	<div class="form-group">
	      <div class="col-sm-offset-2 col-sm-10">
	         <button id="saveBtn" type="button" class="btn btn-primary">保存</button>
	         <button id="cancelBtn" type="button" class="btn btn-primary" onclick="gobackAndReload()">取消</button>
	      </div>
   		</div>
   		
    </form>
  </div>
  
  <script type="text/javascript">
  	var requestType = <s:property value='#request.requestType'/>
  	var userid = "<s:property value='#request.busUser.id'/>";
  	var editUserType = "${busUser.userType}";
  </script>
  <script src="res/js/sys/editUser.js" type="text/javascript"></script>
</body>
</html>

