<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<%@include file="../../common/global/meta.jsp"%>
<%@include file="../../common/global/js.jsp"%>
<%@include file="../head.jsp"%>
<html lang="zh-CN">
<head>
</head>
<body id="appBody">
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
					<a class="hide" id="comeBack" title="返回" onclick="comeback();" href="javascript:;">
						<span class="glyphicon glyphicon-menu-left"></span>
						<input id="stationId" type="hidden" />
					</a>
					<span>预约记录</span>
				</p>
				</div>
				<div class="col-sm-offset-3 col-sm-6">
					<div class="form-group">
						<div class="input-group date col-sm-4 hide" id="datetime" style="float: left;">
						<input type='text' class="form-control" placeholder="请选择月份" id="datetimeInput"/> 
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
						<div class="col-sm-offset-4 col-sm-5" id="searchKeyNameDiv">
								<input placeholder="充电点/桩名称" type="text" class="form-control col-sm-offset-1" id="searchKeyName">
						</div>
						<div class="col-sm-1 text-right">
							<button style="margin-top: 2px;" id="searchKeyNameBtn" class="btn btn-warning btn-sm"
									type="button"> 
									查询
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
		</div>
			<!--充电点 -->
			<div id="stationDiv">
				<div class="tableDivSize">
					<table class="table table-condensed" id="stationAppTable">
						<tbody>
						</tbody>
					</table>
				</div>
				<%@include file="../../common/global/pagingtoolbar.jsp"%>
			</div>
			<!--充电桩  -->
			<div id="pileDiv" class="hide">
				<div class="tableDivSize">
					<table class="table table-condensed" id="pileAppTable">
						<tbody>
						</tbody>
					</table>
				</div>
				<%@include file="../../common/global/pagingtoolbar2.jsp"%>
			</div>

			<div id="noData" class="hide">暂无记录</div>
		</div>
		</s:else>
	</div>
	<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
	<script src="res/js/bussiness/appointment.js" type="text/javascript"></script>
</body>
</html>

