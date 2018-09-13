<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN" style="height: 100%;">
<head>
  <title>充电记录</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
<script type="text/javascript">
var isSelects=true;
</script>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
    <form id="conditionFrom" class="form-inline" role="form" action="deal/charge_queryList.action" style="width: 110%;">
	    <div class="form-group">
	    	<select id="isUser" name="isUser" class="form-control">
	    		<option value="1" selected="selected">系统用户</option>
	    	<s:if test="#request.webUser.usertype.value != 9">
	    		<option value="2">非系统用户</option>
	    	</s:if>
		     </select>
		    <select id="stationToType" name="stationToType" class="form-control">
		    	    <s:iterator value="#request.stationToTypeList" var="item" status="st">
				     	<option value="${item.value}">${item.text}</option>
			     	</s:iterator>
		    </select>
			<div class="input-group date" id="startDateDiv" style="width: 210px;">
                <input id="startDate" name="startdate" type="text" class="form-control" placeholder="开始时间"/>
                <span class="input-group-addon">
                	<span class="glyphicon glyphicon-calendar"></span>
                </span>
		     </div>
		     <span>—</span>
 		 	 <div class='input-group date' id="endDateDiv" style="width: 210px;">
                <input id="endDate" name="enddate" type="text" class="form-control" placeholder="结束时间"/>
                <span class="input-group-addon">
                	<span class="glyphicon glyphicon-calendar"></span>
                </span>
		     </div>
		     
		     <div class="input-group date" id="startUpdateDateDiv" style="width: 210px;">
                <input id="startUpdateDate" name="startupdatedate" type="text" class="form-control" placeholder="更新开始时间"/>
                <span class="input-group-addon">
                	<span class="glyphicon glyphicon-calendar"></span>
                </span>
		     </div>
		     <span>—</span>
 		 	 <div class='input-group date' id="endUpdateDateDiv" style="width: 210px;">
                <input id="endUpdateDate" name="endupdatedate" type="text" class="form-control" placeholder="更新结束时间"/>
                <span class="input-group-addon">
                	<span class="glyphicon glyphicon-calendar"></span>
                </span>
		     </div>
		     
		 	<select id="dealStatus" name="dealstatus" class="form-control hide">
		     	<option value="0">交易状态</option>
		     	<s:iterator value="#request.dealStatusList" var="item" status="st">
		     		<s:if test="#request.dealStatus == #item.value">
		     			<option value="${item.value}" selected="selected">${item.text}</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.value}">${item.text}</option>
		     		</s:else>
		     	</s:iterator>
		     </select>
		     
		     <select id="payStatus" name="paystatus" class="form-control hide">
		     	<option value="0">支付状态</option>
		     	<s:iterator value="#request.payStatusList" var="item" status="st">
		     		<s:if test="#request.payStatus == #item.value">
		     			<option value="${item.value}" selected="selected">${item.text}</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.value}">${item.text}</option>
		     		</s:else>
		     	</s:iterator>
		     </select>
		     
		     <select id="billStatus" name="billstatus" class="form-control hide">
		     	<option value="0">是否结算</option>
		     	<s:iterator value="#request.billStatusList" var="item" status="st">
		     		<s:if test="#request.billStatus == #item.value">
		     			<option value="${item.value}" selected="selected">${item.text}</option>
		     		</s:if>
		     		<s:else>
			     		<option value="${item.value}">${item.text}</option>
		     		</s:else>
		     	</s:iterator>
		     </select>
		</div>
		 <div class="form-group" style="margin-top: 10">
		     <input id="searchTradeno" name="searchtradeno" type="text" class="form-control" placeholder="请输入充电卡号/车牌号"/>
		     <input id="searchStation" name="searchstation" type="text" class="form-control" placeholder="请输入充电站名称/地址"/>
		     <input id="searchUser" name="searchuser" type="text" class="form-control" placeholder="请输入用户姓名/手机"/>
		     <input id="isExport" name="isExport" value="true" type="hidden">
			 <input id="fileName" name="fileName" value="充电记录" type="hidden">
			 <input id="stationIdForSelectModal" name="stationIdForSelectModal" value="" type="hidden">
		</div>
		<div class="form-group" style="margin-top: 10;margin-left: 142;" >
		  	<button type="button" id="selectStationModalBtn" class="btn btn-primary">选择充电站</button>
		    <button type="button" id="queryBtn" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
		    <button type="button" id="chargeResetBtn" class="btn btn-primary"><i class="fa fa-repeat"></i> 重置</button>
		    <button type="button" id="exportBtn" class="btn btn-primary"><i class="fa fa-print"></i> 导出</button>
		</div>
    </form>
    <!-- 表格 -->
    <div id="chargetableDiv" style="width: 110%">
    	<form class="form-inline" role="form">
	     <div class="form-group">
	     	<label class="control-label">总计：<span style="font-weight: 500" id="allCountData">0</span> 条</label>
	     	<label class="control-label" style="margin-left: 30">充电电量总计：<span style="font-weight: 500" id="allChaPowerData">0</span> 度</label>
	       	<label class="control-label" style="margin-left: 10">充电电费统计：<span style="font-weight: 500" id="allChaFeeData">0</span> 元</label>
	       	<label class="control-label" style="margin-left: 10">充电服务费统计：<span style="font-weight: 500" id="allServiceFeeData">0</span> 元</label>
	       	<label class="control-label" style="margin-left: 10">充电总费统计：<span style="font-weight: 500" id="allShouldFeeData">0 </span> 元</label>
	       	<label class="control-label" style="margin-left: 10">充电时长统计：<span style="font-weight: 500" id="allChaLenData">0</span> 分钟</label>
	     </div>
    </form>
    </div>
    <div style="overflow: auto;" id="chargeTableDiv">
	    <table class="table table-condensed table-hover" id="chargeTable" style="margin-bottom: 0px;">
	   		<thead>
	   			<tr>
	   				<th class="text-center" style="min-width: 250">电桩信息</th>
	   				<th class="text-center" style="min-width: 200">订单编号</th>
	   				<th class="text-center" style="min-width: 100">充电卡号</th>
	   				<th class="text-center" style="min-width: 100">车牌号</th>
	   				<th class="text-center" style="min-width: 100">交易状态</th>
	   				<th class="text-center" style="min-width: 100">支付状态</th>
	   				<th class="text-center" id="realNameTh" style="min-width: 100">姓名</th>
	   				<th class="text-center" id="phoneTh" style="min-width: 100">手机号码</th>
	   				<th class="text-center" style="min-width: 150">开始时间</th>
	   				<th class="text-center" style="min-width: 150">结束时间</th>
	   				
	   				<th class="text-center" style="min-width: 100">充电电量(度)</th>
	   				<th class="text-center" style="min-width: 100">充电电费(元)</th>
	   				<th class="text-center" style="min-width: 150">充电服务费(元)</th>
	   				<!-- <th class="text-center">充电停车费(元)</th> -->
	   				<th class="text-center" style="min-width: 150">充电总费用(元)</th>
	   				<th class="text-center" style="min-width: 100">充电时长(分)</th>
	   				<th class="text-center" style="min-width: 150">更新时间</th>
	   				<!-- <th>结算状态</th> -->
	   				<!-- <th>更新时间</th> -->
	   				<th class="text-center" style="min-width: 100">操作</th>
	   			</tr>
	   		</thead>
		   	<tbody></tbody>
		</table>
		</div>
		 <%@include file="../common/global/pagingtoolbar.jsp" %>
    </div>
   
  <script src="res/js/deal/chargeList.js" type="text/javascript"></script>
</body>
</html>

