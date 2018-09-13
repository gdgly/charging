<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>数据新增/修改</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
  	<form id="linkForm" class="form-horizontal" role="form">
		<div class="form-group">
		  <label class="col-sm-2 control-label"><span style="color: red;">*</span>数据类型</label>
	      <div class="col-sm-4">
	        <s:if test="#request.requestType == 2">
	        	<select id="typeId" name="typeId" class="form-control" disabled="disabled">
	        </s:if>
	        <s:else>
	        	<select id="typeId" name="typeId" class="form-control">
	        </s:else>
		     	<s:iterator value="#request.linkTypeList" var="item" status="st">
		     		<s:if test="#request.sysLink.typeId == #item.value">
		     			<option value="${item.id}" selected="selected">${item.name}（${item.id}）</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.id}">${item.name}（${item.id}）</option>
		     		</s:else>
		     	</s:iterator>
		     </select>
	      </div>
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>ID</label>
	      <div class="col-sm-4">
	        <s:if test="#request.requestType == 2">
		      	<input type="text" class="form-control" id="id" name="id" maxlength="32" value="<s:property value='#request.sysLink.id'/>" placeholder="参考格式：数据类型ID_值" disabled="disabled"/>
	        </s:if>
	        <s:else>
	        	<input type="text" class="form-control" id="id" name="id" maxlength="32" value="<s:property value='#request.sysLink.id'/>" placeholder="参考格式：数据类型ID_值"/>
	        </s:else>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>名称</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="name" name="name" maxlength="50" value="<s:property value='#request.sysLink.name'/>"/>
	      </div>
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>值</label>
	      <div class="col-sm-4">
	        <s:if test="#request.requestType == 2">
	        	<input type="text" class="form-control" id="value" name="value" maxlength="50" value="<s:property value='#request.sysLink.value'/>" disabled="disabled"/>
	        </s:if>
	        <s:else>
		      	<input type="text" class="form-control" id="value" name="value" maxlength="50" value="<s:property value='#request.sysLink.value'/>"/>
	        </s:else>
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
  	var requestType = <s:property value='#request.requestType'/>
  </script>
  <script src="res/js/sys/editLink.js" type="text/javascript"></script>
</body>
</html>

