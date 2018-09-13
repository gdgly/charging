<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=Gf39Vxv6N9I9pfdb8tyg6GIA"></script>
<style>
h5{
font-family: Microsoft YaHei ! important;
color:black;
}
.list-group-item.active, .list-group-item.active:focus, .list-group-item.active:hover{
background-color: #eeeeee;
border-color:#eeeeee;
}
h6{
font-family: Microsoft YaHei ! important;
color:#666666;
}
h5>span{
color:#0a94f2;
}
ul, li {
	margin: 0;
	padding: 0
}
.float {
	z-index: 999px;
	width: 350px;
	height: 90px;
	position: absolute;
	top: 90px;
	right: 80px;
	background-color: #F5F5F5;
	border-radius: 5px;
	overflow: hidden;
	opacity:0.9;
}

.floatSearch {
	z-index: 999px;
	border-radius: 10px;
	position: absolute;
	top: 35px;
	right: 38px;
}
</style>
    
<div id="allmap" style="width: 100%; height: 100%;"></div>
<div class="btn-group btn-group-sm floatSearch box-shadow">
		<button title="刷新" id="refreshMapBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-refresh"></span></button>
		<button title="设备监控" id="shoWMonitorListBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-eye-open"></span></button>
		<button title="设备告警" onclick="href('userDevice/initUserDeviceAlarm.action');" type="button" class="btn btn-default"><span class="glyphicon glyphicon-warning-sign"></span></button>
</div>
	<div class="float box-shadow" id="detailPanel">
		<div style="height: 90px;padding-top: 26px;border-bottom: 1px solid #ccc;">
			<div class="col-sm-offset-1 col-sm-5">
				空闲：<span style="color: #1ED538;">${freePile}</span>个
			</div>
			<div class="col-sm-6">
				充电中：<span style="color: #3EA2CC;">${chaPile}</span>个
			</div>
			<div class="col-sm-offset-1  col-sm-5">
				离线：<span style="color: #6E6E6E;">${offPile}</span>个
			</div>
			<div class="col-sm-6">
				预约中：<span style="color: #FF8D18;">${appPile}</span>个
			</div>
		</div>
		
		<div id="info" style="height: 81%;">
	 	<div id="stationResult" style="overflow-y: auto;height: 100%;">
		<s:iterator id="item" status="statu" value="#request.stationList">
			<a href="javascript:;" onclick="clickListStation(this);" class="list-group-item" stationId=<s:property value='id'/> lng=<s:property value='lng'/> lat=<s:property value='lat'/> >
			<h5><s:property value="#statu.index+1"/>.
			<span><s:property value="stationName"/> </span>
			</h5>
			<h6><s:property value="address"/> </h6>
			</a>
		</s:iterator>
		</div> 
		<!-- 详情 start -->
		<form class="form-horizontal hide" role="form" id="pileResult">
		</form>
		<!-- 详情 end -->
		</div>
	</div>
<script type="text/javascript">
	var jsonList = ${jsonList};
 	var jsonPobChargingStation = ${jsonPobChargingStation};
	var jsonPileList = ${jsonPileList}; 
</script>
<script src="res/js/user/device/userDeviceMap.js"></script>

