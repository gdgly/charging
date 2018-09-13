<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>单站实时监测</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
  <link rel="stylesheet" type="text/css" href="res/css/common/singleStaMonitor.css">
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
<div class="container">
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom:5px;">
		<div style="float:left;">
	        <span class="navbar-toggleText" style="margin-top:8px;margin-left:0px;font-size:14px;"><span id="staName">--</span></span>
	        <div style="float:left;margin-right:15px;">
	           		<button type="button" id="selectStationModalBtn" class="btn btn-primary">选择充电站</button>
	        </div>
	        
	    </div>
		<div style="float:left;">
		  	<select id="pileSta" class="form-control">
					     	<option value="-1">终端状态</option>
					     	<s:iterator value="#request.list" var="item" status="st">
						     	<option value="${item.value}">${item.text}</option>
					     	</s:iterator>
			</select>
		</div>
		<div style="float:left;margin-left: 15">
			 <input	id="pileCode" class="form-control" placeholder="请输入终端编号" >
			 <input type="hidden" id="selectStationId" value="0"/>
		</div>
	   	<div style="float:left;margin-left: 15">
	     	<button type="button" id="singleStaSearchBtn" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
	   	</div>
	   	<div style="float:left;margin-left: 15">
	     	<button type="button" id="singleStaResetBtn" class="btn btn-primary"><i class="fa fa-repeat"></i> 重置</button>
	   	</div>
	   	<div style="float:left;margin-left: 15">
	     	<button type="button" id="singleStaFreshBtn" class="btn btn-primary"><i class="fa fa-refresh"></i> 刷新</button>
	   	</div>
	</div>
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	     <hr class="wide " style="margin-top:2px;margin-bottom:2px;" />
	</div>
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                            <div style="overflow: hidden;display:inline-block;">
                                <div style="float:left;">
                                    <span class="navbar-toggleText" style="margin-left:0;">终端总数</span>
                                    <div id="pileSum" class="navbar-toggleColor2 numcolor">0</div>
                                </div>
                                <div style="float:left;">
                                    <span class="navbar-toggleText">快充终端数</span>
                                    <div id="fastPileCount" class="navbar-toggleColor2 numcolor">0</div>
                                </div>
                                <div style="float:left;">
                                    <span class="navbar-toggleText">慢充终端数</span>
                                    <div id="slowPileCount" class="navbar-toggleColor2 numcolor">0</div>
                                </div>
                                <!-- <div style="float:left;">
                                    <span class="navbar-toggleText">实际总功率(kW)</span>
                                    <div id="GrossPower" class="navbar-toggleColor2 numcolor">0</div>
                                </div>
                                <div style="float:left;">
                                    <span class="navbar-toggleText">额定总功率(kW)</span>
                                    <div id="ChargingPower" class="navbar-toggleColor2 numcolor">0</div>
                                </div> -->
                            </div>
	</div>
	
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	     <hr class="wide " style="margin-top:2px;margin-bottom:2px;" />
	</div>

	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                            <div style="overflow: hidden;display:inline-block;">
                                <div style="float:left;">
                                    <div class="navbar-toggleColor statecolorH"></div>
                                    <span class="navbar-toggleText">空闲</span>
                                    <div class="navbar-toggleColor2 numcolor"><span id="idleCount">0</span></div>
                                </div>
                                
                                <div style="float:left;">
                                    <div class="navbar-toggleColor statecolorM"></div>
                                    <span class="navbar-toggleText">充电中</span>
                                    <div class="navbar-toggleColor2 numcolor"><span id="chargingCount">0</span></div>
                                </div>
                                
                                <div style="float:left;">
                                    <div class="navbar-toggleColor statecolorZ"></div>
                                    <span class="navbar-toggleText">离线</span>
                                    <div class="navbar-toggleColor2 numcolor"><span id="unLineCount">0</span></div>
                                </div>
                                
                                <div style="float:left;">
                                    <div class="navbar-toggleColor statecolorL"></div>
                                    <span class="navbar-toggleText">故障</span>
                                    <div class="navbar-toggleColor2 numcolor"><span id="faultCount">0</span></div>
                                </div>
                                
                                <div style="float:left;">
                                    <div class="navbar-toggleColor statecolorY"></div>
                                    <span class="navbar-toggleText">忙碌中</span>
                                    <div class="navbar-toggleColor2 numcolor"><span id="busyCount">0</span></div>
                                </div>
                            </div>
	</div>

	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
		     <hr class="wide " style="margin-top:2px;margin-bottom:2px;" />
	</div>

	<div id="pillBox">
	
	</div>
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
		     <hr class="wide " style="margin-top:2px;margin-bottom:2px;" />
	</div>
</div>
</body>
<script src="res/js/run/singleStaMonitor.js" type="text/javascript"></script>
</html>

