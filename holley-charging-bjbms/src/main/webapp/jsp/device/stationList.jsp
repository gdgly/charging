<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>充电站信息</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
  <!--------------------------------------充电站列表 ------------------------------------------->
  	<div id="stationListResult">
	    <form id="stationForm" class="form-inline" role="form" action="device/station_queryStationList.action">
		    <div class="form-group">
				<input id="keyword" name="keyword" type="text" class="form-control" placeholder="请输入充电站名称/地址" style="width: 240px;"/>
			     <select id="province" name="province" class="form-control">
					<option value="0">请选择省份</option>
					<s:iterator value="#request.provinceList" status="status" id="item">
						<option value="<s:property value='id'/>"><s:property value="name" /></option>
					</s:iterator>
				</select>
				<select id="city" name="city" class="form-control">
					<option value="0">请选择市区</option>
				</select>
				<select id="busType" name="bustype" class="form-control hide">
			     	<option value="0">运营类型</option>
			     	<s:iterator value="#request.busTypeList" var="item" status="st">
				     	<option value="${item.value}">${item.text}</option>
			     	</s:iterator>
			     </select>
			     <select id="stationToType" name="stationToType" class="form-control">
					<option value="0">站类型</option>
					<s:iterator value="#request.stationToTypeList" var="item" status="st">
				     	<option value="${item.value}">${item.text}</option>
			     	</s:iterator>
				</select>
				<select id="isShow" name="isshow" class="form-control hide">
			     	<option value="0">状态</option>
			     	<s:iterator value="#request.showStatusList" var="item" status="st">
			     		<s:if test="#request.isShow == #item.value">
			     			<option value="${item.value}" selected="selected">${item.text}</option>
			     		</s:if>
			     		<s:else>
				     		<option value="${item.value}">${item.text}</option>
			     		</s:else>
			     	</s:iterator>
			     </select>
			     <input	id="stationIsExport" name="isExport" value="true" type="hidden">
			     <input	id="stationFileName" name="fileName" value="充电站列表" type="hidden">
			     <input id="stationIdForSelectModal" name="stationIdForSelectModal" value="0" type="hidden">
			     
			</div>
			 <div class="form-group pull-right">
			<button class="btn btn-primary" type="button"  id="selectStationModalBtn">选择充电站</button>
			<s:if test="#request.webUser.usertype.value == 1">
				<button type="button" id="addStationBtn" class="btn btn-primary"><i class="fa fa-plus"></i> 添加设备</button>
			</s:if>
			 	
			 	<button type="button" id="stationQueryBtn" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
			 	<button type="button" id="stationExportBtn" class="btn btn-primary"><i class="fa fa-print"></i> 导出</button>
			 	<button type="button" id="issuedChargeRuleBtn" class="btn btn-primary"><i class="fa fa-print"></i> 下发</button>
			 </div>
	    </form>
	    <!-- 表格 -->
	    <div id="pileTableDiv">
		    <table class="table table-condensed table-hover" id="stationTable">
		   		<thead>
		   			<tr>
		   			 	<th><input type="checkbox" id="allcheck"/>下发选择</th>
		   				<!-- <th>ID</th> -->
		   				<th>站名称</th>
		   				<th>地区</th>
		   				<th>详细地址</th>
		   				<th>运营机构</th>
		   				<th>站类型</th>
		   				<!--<th>运营机构</th> -->
		   				<!-- <th>状态</th> -->
		   				<th>快桩数</th>
		   				<th>慢桩数</th>
		   				<!-- <th>评分</th> -->
		   				<th>更新时间</th>
		   				<th>操作</th>
		   			</tr>
		   		</thead>
			   	<tbody></tbody>
			</table>
	    </div>
	    <!-- 分页条 -->
	    <%@include file="../common/global/pagingtoolbar.jsp" %>
  	</div>
  	<!--------------------------------------充电桩列表 ------------------------------------------->
  	<div id="pileListResult" class="hide">
  		<span class="glyphicon glyphicon-menu-left" data-toggle="tooltip" title="返回" aria-hidden="true" onclick="showResult('showstationlist')" 
	    style="cursor:pointer;font-weight: bold;font-size: 16px;"></span>
	    <label class="pull-right">
		    <span id="busMecName" class="color-orange"></span> | <span id="stationName" class="color-orange"></span> | <span id="address" class="color-orange"></span>
	    </label>
	    <form id="pileForm" class="form-inline" role="form" style="padding-left: 20px;padding-top: 10px;" action="device/station_queryPileList.action">
		    <div class="form-group">
				<select id="pileType" name="piletype" class="form-control">
			     	<option value="0">电桩类型</option>
			     	<s:iterator value="#request.pileTypeList" var="item" status="st">
			     		<option value="${item.value}">${item.name}</option>
			     	</s:iterator>
			   </select>
			   <input id="pileIsExport" name="isExport" value="true" type="hidden">
			   <input id="pileFileName" name="fileName" value="充电桩列表" type="hidden">
			   <input id="stationId" name="stationid" type="hidden">
			   
			</div>
			 <div class="form-group pull-right">
				<button type="button" id="pileQueryBtn" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
				<button type="button" id="pileExportBtn" class="btn btn-primary"><i class="fa fa-print"></i> 导出</button>
			 </div>
	    </form>
       	<table class="table table-condensed table-hover" id="pileTable">
	   		<thead>
	   			<tr>
	   				<th>充电桩Id</th>
	   				<th>桩名称</th>
	   				<th>桩编号</th>
	   				<th>通信协议</th>
	   				<th>通信地址</th>
	   				<th>电桩类型</th>
	   				<th>充电方式</th>
	   				<!-- <th>是否支持预约</th> -->
	   				<th>支付方式</th>
	   				<th>状态</th>
	   			</tr>
	   		</thead>
		   	<tbody></tbody>
		</table>
  	</div>
  	<!--------------------------------------充电桩详细------------------------------------------->
  	<div id="pileDetailResult" class="hide">
  		<span class="glyphicon glyphicon-menu-left" data-toggle="tooltip" title="返回" aria-hidden="true" onclick="showResult('showpilelist')" 
	    style="cursor:pointer;font-weight: bold;font-size: 16px;"></span>
  		<form class="form-horizontal" role="form">
    <!-- ------------------------------基本信息------------------------------------------------>
  		<h5 class="form-header">基本信息</h5>
  		<hr class="dashed">
    	<div class="form-group">
	      <label class="col-sm-2 control-label">充电桩名称</label>
	      <div class="col-sm-4">
	      	<p id="pileDetail_pileName" class="form-control-static"></p>
	      </div>
	      <label class="col-sm-2 control-label">充电桩类型</label>
	      <div class="col-sm-4">
	      	<p id="pileDetail_pileType" class="form-control-static"></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">充电桩编号</label>
	      <div class="col-sm-4">
	      	<p id="pileDetail_pileCode" class="form-control-static"></p>
	      </div>
	      <label class="col-sm-2 control-label">电桩型号</label>
	      <div class="col-sm-4">
	      	<p id="pileDetail_pileModel" class="form-control-static"></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">充电方式</label>
	      <div class="col-sm-4">
	      	<p id="pileDetail_chaWay" class="form-control-static"></p>
	      </div>
	      <label class="col-sm-2 control-label">充电模式</label>
	      <div class="col-sm-4">
	      	<p id="pileDetail_chaMode" class="form-control-static"></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">支付方式</label>
	      <div class="col-sm-4">
	      	<p id="pileDetail_payWay" class="form-control-static"></p>
	      </div>
	      <label class="col-sm-2 control-label">所属充电站</label>
	      <div class="col-sm-4">
	      	<p id="pileDetail_stationName" class="form-control-static"></p>
	      </div>
    	</div>
    	<!-- <div class="form-group">
	      <label class="col-sm-2 control-label">是否支持远程控制</label>
	      <div class="col-sm-4">
	      	<p id="pileDetail_isControl" class="form-control-static"></p>
	      </div>
	      <label class="col-sm-2 control-label">是否支持负荷调度</label>
	      <div class="col-sm-4">
	      	<p id="pileDetail_isChaLoad" class="form-control-static"></p>
	      </div>
    	</div> -->
    	<div class="form-group">
	      <!-- <label class="col-sm-2 control-label">是否支持预约</label>
	      <div class="col-sm-4">
	      	<p id="pileDetail_isApp" class="form-control-static"></p>
	      </div> -->
	      <label class="col-sm-2 control-label">状态</label>
	      <div class="col-sm-4">
	      	<p id="pileDetail_status" class="form-control-static"></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">更新时间</label>
	      <div class="col-sm-4">
	      	<p id="pileDetail_updateTime" class="form-control-static"></p>
	      </div>
	      <label class="col-sm-2 control-label">安装时间</label>
	      <div class="col-sm-4">
	      	<p id="pileDetail_buildTime" class="form-control-static"></p>
	      </div>
    	</div>
    	<div class="from-group">
    	  <label class="col-sm-2 control-label">详细地址</label>
	      <div class="col-sm-10">
	      	<p id="pileDetail_address" class="form-control-static"></p>
	      </div>
    	</div>
    	<!-- ------------------------------通讯信息------------------------------------------------>
    	<h5 class="form-header">通讯信息</h5>
  		<hr class="dashed">
    	<div class="form-group">
	      <label class="col-sm-2 control-label">通讯协议</label>
	      <div class="col-sm-4">
	      	<p id="pileDetail_comType" class="form-control-static"><s:property value="#request.chargePile.comTypeDesc"/></p>
	      </div>
	     <label class="col-sm-2 control-label">通讯地址</label>
	      <div class="col-sm-4">
	      	<p id="pileDetail_comAddr" class="form-control-static"><s:property value="#request.chargePile.comAddr"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">通讯子地址</label>
	      <div class="col-sm-4">
	      	<p id="pileDetail_comSubAddr" class="form-control-static"><s:property value="#request.chargePile.comSubAddr"/></p>
	      </div>
    	</div>
    	<!-- ------------------------------正在使用的费用信息------------------------------------------------>
    	<div id="activeRuleDiv" class="hide">
	    	<h5 class="form-header">正在使用的费用信息</h5>
	  		<hr class="dashed">
	    	<div class="form-group">
		      <label class="col-sm-2 control-label">收费规则</label>
		      <div class="col-sm-4">
		      	<p id="activeRule_feeRule" class="form-control-static"></p>
		      </div>
		      <div id="achargeFeeDiv" class="hide">
		      	 <label class="col-sm-2 control-label">单一电费金额</label>
			      <div class="col-sm-4">
			      	<p id="activeRule_chargeFee" class="form-control-static"></p>
			      </div>
		      </div>
		      <div id="afeeRuleDetailDiv" class="hide">
			      <label class="col-sm-2 control-label">收费规则详细</label>
			      <div class="col-sm-4">
			      	<p id="activeRule_feeRuleDetail" class="form-control-static"></p>
			      </div>
		      </div>
	    	</div>
	    	<div class="form-group">
		      <label class="col-sm-2 control-label">停车费用</label>
		      <div class="col-sm-4">
		      	<p id="activeRule_parkFee" class="form-control-static"><s:property value="#request.activeRule.parkFee"/>&nbsp;元/时</p>
		      </div>
		      <label class="col-sm-2 control-label">服务费用</label>
		      <div class="col-sm-4">
		      	<p id="activeRule_serviceFee" class="form-control-static"><s:property value="#request.activeRule.serviceFee"/>&nbsp;元/度</p>
		      </div>
	    	</div>
	    	<div class="form-group">
		      <label class="col-sm-2 control-label">费用有效时间</label>
		      <div class="col-sm-4">
		      	<p id="activeRule_activeTime" class="form-control-static"><s:property value="#request.activeRule.activeTimeStr"/></p>
		      </div>
	    	</div>
    	</div>
    	<!-- ------------------------------待激活的电费信息------------------------------------------------>
    	<div id="unactiveRuleDiv" class="hide">
	    	<h5 class="form-header">待激活的电费信息</h5>
	  		<hr class="dashed">
	    	<div class="form-group">
		      <label class="col-sm-2 control-label">收费规则</label>
		      <div class="col-sm-4">
		      	<p id="unactiveRule_feeRule" class="form-control-static"></p>
		      </div>
		      <div id="uchargeFeeDiv" class="hide"> 
		      	 <label class="col-sm-2 control-label">单一电费金额</label>
			      <div class="col-sm-4">
			      	<p id="unactiveRule_chargeFee" class="form-control-static"></p>
			      </div>
		      </div>
		      <div id="ufeeRuleDetailDiv" class="hide">
			      <label class="col-sm-2 control-label">收费规则详细</label>
			      <div class="col-sm-4">
			      	<p id="unactiveRule_feeRuleDetail" class="form-control-static"></p>
			      </div>
		      </div>
	    	</div>
	    	<div class="form-group">
		      <label class="col-sm-2 control-label">停车费用</label>
		      <div class="col-sm-4">
		      	<p id="unactiveRule_parkFee" class="form-control-static"></p>
		      </div>
		      <label class="col-sm-2 control-label">服务费用</label>
		      <div class="col-sm-4">
		      	<p id="unactiveRule_serviceFee" class="form-control-static"></p>
		      </div>
	    	</div>
	    	<div class="form-group">
		      <label class="col-sm-2 control-label">费用有效时间</label>
		      <div class="col-sm-4">
		      	<p id="unactiveRule_activeTime" class="form-control-static"></p>
		      </div>
	    	</div>
    	</div>
    </form>
  	</div>
</div>
 <%@include file="../common/global/chargeFeeRuleCheck.jsp" %>
 <script type="text/javascript">
 var chargeRuleJsonObj = '${chargeRuleJsonObj}';
 var currentTime = "${currentTime}";	
 </script>
  <script src="res/js/device/stationList.js" type="text/javascript"></script>
</body>
</html>

