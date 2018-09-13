<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>充电卡管理</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
    <form id="conditionFrom" class="form-inline" role="form" action="sys/role_queryList.action">
	    <div class="form-group">
			<input id="keyword" name="keyword" maxlength="50" type="text" style="width: 350px;" class="form-control" placeholder="请输入用户名姓名/身份证号/手机号/车牌号/卡号"/>
		</div>
		 <div class="form-group pull-right">
			<button type="button" id="queryBtn" class="btn btn-primary">查询</button>
			<button type="button" id="registerUserBtn" onclick="href('person/registerUser.action');" class="btn btn-primary">开户</button>
			<button type="button" id="cardInfoBtn" onclick="href('person/cardInfo.action');" class="btn btn-primary">卡信息查询</button>
		 </div>
    </form>
   	<!--<form id="userAndCardInfoForm" class="form-horizontal" role="form">
    	<div class="form-group">
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>用户姓名</label>
	       <div class="col-sm-4">
	      	<input type="text" class="form-control" id="realName" name="realName" maxlength="20"/>
	      </div>
	      </div>
    	</form> 
    -->
    <!-- 表格 -->
    <div id="userTableDiv">
	    <table class="table table-condensed table-hover" id="userTable">
	   		<thead>
	   			<tr>
	   				<th>用户名</th>
	   				<th>手机号码</th>
	   				<th>真实姓名</th>
	   				<th>身份证号</th>
	   				<th>性别</th>
	   				<th>地区</th>
	   				<th>车牌号</th>
	   				<th>充电卡数(张)</th>
	   				<th>操作</th>
	   			</tr>
	   		</thead>
		   	<tbody id="userTbody"></tbody>
		</table>
		<p id="noListMsg" class="text-center" style="margin-top: 50px;">暂无用户信息</p>
    </div>
     <%@include file="../common/global/pagingtoolbar.jsp" %>
     <div id="cardTableDiv" class="hide">
         <table class="table table-condensed table-hover" id="cardTable">
	   		<thead>
	   			<tr>
	   				<th>发卡运营商编号</th>
	   				<th>充点卡卡号</th>
	   				<th>卡类型</th>
	   				<th>启用日期</th>
	   				<th>有效日期</th>
	   				<th>卡内余额(元)</th>
	   			</tr>
	   		</thead>
		   	<tbody id="cardTbody"></tbody>
		</table>
		</div>
  </div>
  <script type="text/javascript">
  var maxChargeCard = ${maxChargeCard};//添加查看数量上限
  </script>
  <script src="res/js/chargecard/cardManager.js" type="text/javascript"></script>
</body>
</html>

