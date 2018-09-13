<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>实时监测</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
    <!-- 充电站列表 -->
    <div id="stationResult">
	    <form id="sconditionFrom" class="form-inline" role="form" action="run/monitor_queryStationList.action">
		    <div class="form-group">
				<input id="keyword" name="keyword" type="text" class="form-control" placeholder="请输入充电站名称/地址/运营商名称" style="width: 280px;"/>
				<select id="province" name="province" class="form-control">
					<option value="0">请选择省份</option>
					<s:iterator value="#request.provinceList" status="status" id="item">
						<option value="<s:property value='id'/>"><s:property value="name" /></option>
					</s:iterator>
				</select>
				<select id="city" name="city" class="form-control">
					<option value="0">请选择市区</option>
				</select>
				<input id="sisExport" name="isExport" value="true" type="hidden">
			 	<input id="sfileName" name="fileName" value="实时监控列表" type="hidden">
			</div>
			 <div class="form-group pull-right">
				<button type="button" id="queryStationBtn" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
				<button type="button" id="exportStationBtn" class="btn btn-primary"><i class="fa fa-print"></i> 导出</button>
			 </div>
	    </form>
	    <!-- 表格 -->
	    <div id="stationTableDiv">
		    <table class="table table-condensed table-hover" id="stationTable">
		   		<thead>
		   			<tr>
		   				<th>充电站Id</th>
		   				<th>充电站名称</th>
		   				<th>地区</th>
		   				<th>运营机构</th>
		   				<th>运营类型</th>
		   				<!-- <th>地址</th> -->
		   				<th>电桩个数</th>
		   				<th>空闲数</th>
		   				<th>充电数</th>
		   				<!-- <th>预约数</th> -->
		   				<th>离线数</th>
		   				<th>故障数</th>
		   				<th>忙碌数</th>
		   			</tr>
		   		</thead>
			   	<tbody></tbody>
			</table>
	    </div>
	     <!-- 分页条 -->
	    <%@include file="../common/global/pagingtoolbar.jsp" %>
    </div>
    <!-- 充电桩列表 -->
    <div id="pileResult" class="hide">
	    <span id="backStationListBtn" class="glyphicon glyphicon-menu-left" data-toggle="tooltip" title="返回" aria-hidden="true" 
	    style="cursor:pointer;font-weight: bold;font-size: 16px;"></span>
	    <label class="pull-right">
		    <span id="busMecName" class="color-orange"></span> | <span id="stationName" class="color-orange"></span> | <span id="address" class="color-orange"></span>
	    </label>
	    <form id="pconditionFrom" class="form-inline" role="form" style="padding-left: 20px;padding-top: 10px;" action="run/monitor_queryPileList.action">
		    <div class="form-group">
				<select id="pileStatus" name="pilestatus" class="form-control">
			     	<option value="-1">电桩状态</option>
			     	<s:iterator value="#request.pileStatusList" var="item" status="st">
			     		<s:if test="#request.pileStatus == #item.value">
			     			<option value="${item.value}" selected="selected">${item.text}</option>
			     		</s:if>
			     		<s:else>
			     		<s:if test="#item.value == 3">
			     		
			     		</s:if>
			     		<s:else>
			     			<option value="${item.value}">${item.text}</option>
			     		</s:else>
			     		</s:else>
			     	</s:iterator>
			     </select>
			     <input id="pisExport" name="isExport" value="true" type="hidden">
			 	 <input id="pfileName" name="fileName" value="电桩状态列表" type="hidden">
			 	 <input id="stationId" name="stationid" value="true" type="hidden">
			</div>
			 <div class="form-group pull-right">
				<button type="button" id="queryPileBtn" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
				<button type="button" id="exportPileBtn" class="btn btn-primary"><i class="fa fa-print"></i> 导出</button>
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
	   				<th>支付方式</th>
	   				<th>电压</th>
	   				<th>电流</th>
	   				<th>状态</th>
	   				<th>图表</th>
	   			</tr>
	   		</thead>
		   	<tbody></tbody>
		</table>
    </div>
    <!-- 充电桩状态详情 -->
    <div id="pileDetailResult" class="hide">
    	<span id="backPileListBtn" class="glyphicon glyphicon-menu-left" data-toggle="tooltip" title="返回" aria-hidden="true" 
	    style="cursor:pointer;font-weight: bold;font-size: 16px;"></span>
	    <div class="form-group pull-right">
			<button type="button" id="refreshPileStatusBtn" class="btn btn-primary"><i class="fa fa-refresh"></i> 刷新</button>
	    </div>
    	<form class="form-horizontal" role="form">
    		<h5 class="form-header">电桩信息</h5>
  			<hr class="dashed">
  			<div class="form-group">
		        <label class="col-sm-2 control-label">充电桩名称</label>
		        <div class="col-sm-4">
		      	  <p id="detail_pilename" class="form-control-static"></p>
		        </div>
		        <label class="col-sm-2 control-label">充电桩编号</label>
		        <div class="col-sm-4">
		      	  <p id="detail_pilecode" class="form-control-static"></p>
		        </div>
    	    </div>
    	    <div class="form-group">
		        <label class="col-sm-2 control-label">所属运营商</label>
		        <div class="col-sm-4">
		      	  <p id="detail_busmecname" class="form-control-static"></p>
		        </div>
		        <label class="col-sm-2 control-label">所属充电站</label>
		        <div class="col-sm-4">
		      	  <p id="detail_stationname" class="form-control-static"></p>
		        </div>
    	    </div>
    	    <div class="form-group">
		        <label class="col-sm-2 control-label">充电类型</label>
		        <div class="col-sm-4">
		      	  <p id="detail_powertype" class="form-control-static"></p>
		        </div>
		        <label class="col-sm-2 control-label">充电方式</label>
		        <div class="col-sm-4">
		      	  <p id="detail_currenttype" class="form-control-static"></p>
		        </div>
    	    </div>
    	    <div class="form-group">
		        <label class="col-sm-2 control-label">通讯协议</label>
		        <div class="col-sm-4">
		      	  <p id="detail_comtype" class="form-control-static"></p>
		        </div>
		        <label class="col-sm-2 control-label">通讯地址</label>
		        <div class="col-sm-4">
		      	  <p id="detail_comaddr" class="form-control-static"></p>
		        </div>
    	    </div>
    	    <div class="form-group">
		        <label class="col-sm-2 control-label">电压</label>
		        <div class="col-sm-4">
		      	  <p id="detail_outv" class="form-control-static"></p>
		        </div>
		        <label class="col-sm-2 control-label">电流</label>
		        <div class="col-sm-4">
		      	  <p id="detail_outi" class="form-control-static"></p>
		        </div>
    	    </div>
    	    <div class="form-group">
		        <label class="col-sm-2 control-label">状态</label>
		        <div class="col-sm-4">
		      	  <p id="detail_status" class="form-control-static"></p>
		        </div>
		        <label class="col-sm-2 control-label">支付方式</label>
		        <div class="col-sm-4">
		      	  <p id="detail_payway" class="form-control-static"></p>
		        </div>
    	    </div>
    	    <div id="userInfo">
	    	    <h5 class="form-header">用户信息</h5>
	  			<hr class="dashed">
	    	    <div class="form-group">
			        <label class="col-sm-2 control-label">用户编码</label>
			        <div class="col-sm-4">
			      	  <p id="detail_userid" class="form-control-static"></p>
			        </div>
			        <label class="col-sm-2 control-label">用户昵称</label>
			        <div class="col-sm-4">
			      	  <p id="detail_username" class="form-control-static"></p>
			        </div>
	    	    </div>
	    	    <div class="form-group">
			        <label class="col-sm-2 control-label">手机号码</label>
			        <div class="col-sm-4">
			      	  <p id="detail_phone" class="form-control-static"></p>
			        </div>
	    	    </div>
    	    </div>
    	    <div id="appInfo">
    	    	<h5 class="form-header">预约信息</h5>
	  			<hr class="dashed">
	  			<div class="form-group">
			        <label class="col-sm-2 control-label">预约记录id</label>
			        <div class="col-sm-4">
			      	  <p id="detail_apprecordid" class="form-control-static"></p>
			        </div>
			        <label class="col-sm-2 control-label">预约截止时间</label>
			        <div class="col-sm-4">
			      	  <p id="detail_appendtime" class="form-control-static"></p>
			        </div>
	    	    </div>
    	    </div>
    	    <div id="chargeInfo">
	  			<h5 class="form-header">充电信息</h5>
	  			<hr class="dashed">
	    	    <div class="form-group">
			        <label class="col-sm-2 control-label">当前充电时长</label>
			        <div class="col-sm-4">
			      	  <p id="detail_chalen" class="form-control-static"></p>
			        </div>
			        <label class="col-sm-2 control-label">当前充电电量</label>
			        <div class="col-sm-4">
			      	  <p id="detail_chapower" class="form-control-static"></p>
			        </div>
	    	    </div>
	    	    <div class="form-group">
			        <label class="col-sm-2 control-label">当前充电金额</label>
			        <div class="col-sm-4">
			      	  <p id="detail_money" class="form-control-static"></p>
			        </div>
			        <label class="col-sm-2 control-label">交易号</label>
			        <div class="col-sm-4">
			      	  <p id="detail_tradeno" class="form-control-static"></p>
			        </div>
	    	    </div>
    	    </div>
    	  </form>
    </div>
    
    
        <!-- 充电桩图表详情 -->
    <div id="pileChartResult" class="hide">
    	<span id="backPileListForChartBtn" class="glyphicon glyphicon-menu-left" data-toggle="tooltip" title="返回" aria-hidden="true" 
	    style="cursor:pointer;font-weight: bold;font-size: 16px;"></span>
	   
	    <div class="form-group pull-right" style="margin-left: 10px">
			<button type="button" id="refreshPileChartBtn" class="btn btn-primary"><i class="fa fa-refresh"></i> 刷新</button>
			
	    </div>
	     <div class="form-group pull-right">
	    <select id="addDay" name="addDay" class="form-control">
			<option value="0">当天</option>
			<option value="-1">历史2天</option>
			<option value="-2">历史3天</option>
			</select>
	    </div>
	    <input type="hidden" id="pileIDForChart" value=""/>
	    <input type="hidden" id="pileNameForChart" value=""/>
	    <input type="hidden" id="pileTypeForChart" value=""/>
   <div id="main1" style="width: 100%;height: 30%;margin-top: 22px;">
   </div>
     <div id="main2" style="width: 100%;height: 30%;margin-top: 5%">
   </div>
    </div>
  </div>
  <script src="res/js/common/echarts.common.min.js" type="text/javascript"></script>
  <script src="res/js/run/monitorList.js" type="text/javascript"></script>
</body>
</html>

