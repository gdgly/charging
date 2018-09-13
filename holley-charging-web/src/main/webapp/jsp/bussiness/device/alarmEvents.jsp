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
			<!-- <h3>告警事件信息</h3> -->
		<div class="row">
				<div class="col-sm-3">
					<h3>告警事件信息</h3>
				</div>
				<div class="col-sm-offset-7 col-sm-2 text-right">
				<button style="margin-top: 15px;" id="exportBtn" class="btn btn-warning btn-sm"
									type="button"> 
									导出
								 </button> 
				</div>
			</div>


			<!-- 告警列表 -->
			<div id="alarmDiv">
				<div class="tableDivSize">
					<table class="table table-condensed" id="alarmTable">
						<tbody>
						</tbody>
					</table>
				</div>
				<%@include file="../../common/global/pagingtoolbar.jsp"%>
			</div>
		</div>
		</s:else>
	</div>
	<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
	<script src="res/js/bussiness/alarmEvent.js" type="text/javascript"></script>
</body>
</html>

