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
			<div class="row tableSearchSize" style="margin-bottom: 10px;">
				<div class="col-sm-3">
					<ul class="nav nav-pills">
						<li class="active" id="searchStationBtn"><a href="javascript::">充电点</a></li>
						<li id="searchPileBtn"><a href="javascript::">充电桩</a></li>
					</ul>
				</div>
				<div class="col-sm-offset-3 col-sm-6">
					<div class="form-group">
			
					<div class="col-sm-4">
						<select id="validStatus" class="form-control">
							<option value="0">全部</option>
							<option value="1">待审核</option>
							<option value="2">审核中</option>
							<option value="4">审核失败</option>
						</select>
					</div>
					<div class="col-sm-6">
						<div class="input-group">
							<input placeholder="充电点/桩名称" type="text" class="form-control" id="searchKeyName">
							<span class="input-group-btn">
								<button id="searchKeyNameBtn" class="btn btn-default" type="button"
									style="height: 34px;">
									<img src="${imgUrl}res/img/bussiness/search_02.png" />
								</button>
							</span>
						</div>
					</div>
						<div class="col-sm-2 text-right">
								<button style="margin-top: 2px;" id="exportBtn" class="btn btn-warning btn-sm"
									type="button"> 
									导出
								 </button> 
						</div>
					</div>
				</div>
				<input id="searchType" value='station' type="hidden" />
			</div>
			<!-- 待审核充电点 -->
			<div id="stationDiv">
				<div class="tableDivSize">
					<!-- 充电点列表 -->
					<table class="table table-condensed" id="unValidStationTable">
						<tbody>
						</tbody>
					</table>
				</div>
				<%@include file="../../common/global/pagingtoolbar.jsp"%>
			</div>
			<!--待审核充电桩  -->
			<div id="pileDiv" class="hide">
				<div class="tableDivSize">
					<table class="table table-condensed" id="unValidPileTable">
						<tbody>
						</tbody>
					</table>
				</div>
				<%@include file="../../common/global/pagingtoolbar2.jsp"%>
			</div>
		</div>
		</s:else>
	</div>
	<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
	<script src="res/js/bussiness/unValidDevice.js" type="text/javascript"></script>
</body>
</html>

