<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<%@include file="../../common/global/meta.jsp"%>
<%@include file="../../common/global/js.jsp"%>
<%@include file="../head.jsp"%>
<html lang="zh-CN">
<head>
</head>
<body>
	<div class="bg">
		  <!--msg-content-->
	  <s:if test="#request.msg != null">
		<div class="container main-body"  style="height: 50%;">
			<p class="text-center" style="padding-top: 14%;">
			<strong>提示信息：</strong>${msg}
			<s:if test="#request.backUrl != null">
			<a href="<s:property value="#request.backUrl"/>">
			<s:if test="#request.retrunDec != null">
			<s:property value="#request.retrunDec"/>
			</s:if>
			<s:else>
			返回
			</s:else>
			</a>
			</s:if>
			<s:else>
			<a onclick="history.go(-1)" href="javascript::">返回</a>
			</s:else>
			</p>
		</div>
	</s:if>
	<s:else>
		<!--main-content-->
		<div class="container main-body tablePageSize">
			<div class="row tableSearchSize">
				<div class="col-sm-3">
								
				<p style="font-size: 24px;">
					<a class="hide" id="comeBack" title="返回" href="javascript:;">
						<span class="glyphicon glyphicon-menu-left"></span>
						<input id="stationId" type="hidden" />
					</a>
					<span>已有设备</span>
				</p>
				</div>
				<div class="col-sm-offset-3 col-sm-6">
					<div class="form-group col-sm-offset-3 col-sm-6">
						<div class="input-group">
							<input placeholder="充电点/桩名称" type="text" class="form-control" id="searchKeyName">
							<span class="input-group-btn" style="height: 100%;">
								<button id="searchKeyNameBtn" class="btn btn-default"
									type="button" style="height: 34px;">
									<img
										src="${imgUrl}res/img/bussiness/search_02.png" />
								</button>
								
							</span>
						</div>
					</div>
					<div class="col-sm-1">
								<button id="addStationBtn" stationId="0" style="margin-top: 2px;" class="btn btn-warning btn-sm" type="button"> 
									新增
								 </button> 
						</div>
					<div class="col-sm-2 text-right">
								<button style="margin-top: 2px;" id="exportBtn" class="btn btn-warning btn-sm"
									type="button"> 
									导出
								 </button> 
						</div>
				</div>
			</div>
			<!-- 充电点列表 -->
			<div id="stationDiv">
				<div class="tableDivSize">
					<table class="table table-condensed" id="validStation">
						<tbody>
						</tbody>
					</table>
				</div>
				<%@include file="../../common/global/pagingtoolbar.jsp"%>
			</div>

			<!-- 充电桩列表 -->
			<div id="pileDiv" class="hide">
				<div class="tableDivSize">
					<table class="table table-condensed" id="validPile">
						<tbody>
						</tbody>
					</table>
				</div>
				<%@include file="../../common/global/pagingtoolbar2.jsp"%>
			</div>
		</div>
		</s:else>
	</div>
	<script type="text/javascript">
	var backStationId = '${stationId}';
	</script>
	<script src="res/js/bussiness/device.js" type="text/javascript"></script>
</body>
</html>

