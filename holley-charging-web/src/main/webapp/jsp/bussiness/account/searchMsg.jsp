<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<%@include file="../../common/global/meta.jsp"%>
<%@include file="../../common/global/js.jsp"%>
<%@include file="../head.jsp"%>
<html lang="zh-CN">
<head>
<style type="text/css">
</style>
</head>
<body>
	<div class="bg">
		<!--main-content-->
		<div class="container main-body tablePageSize" id="msgListDiv">
			<div class="col-sm-3">
			<h3>系统消息</h3>
			</div>
			<div class="col-sm-offset-6 col-sm-3" style="padding-top: 8px;">
				<ul class="nav nav-pills" style="float: right;" id="searchMsgBtn">
					<li class="active" id="allMsg"><a href="javascript:;">全部</a></li>
					<li id="unMsg"><a href="javascript:;">未读</a></li>
					<li id="yesMsg"><a href="javascript:;">已读</a></li>
				</ul>
				<input type="hidden" id="isRead" value="0" />
			</div>

			<!--     <div class="form-group">
				<div class='input-group date' id='datetime'>
                	<input type='text' class="form-control" />
                	<span class="input-group-addon"><span class="glyphicon glyphicon-calendar">
                      </span>
                </span>
            </div>
     </div> -->


			<!-- 消息列表 -->
			<div id="msgDiv">
				<div class="tableDivSize">
					<table class="table table-hover" id="msgTable">
						<tbody>
						</tbody>
					</table>
				</div>
				<%@include file="../../common/global/pagingtoolbar.jsp"%>
			</div>
		</div>
		
		<div class="container main-body tablePageSize hide" id="msgContentDiv">
		<div class="row">
			<div class="col-sm-10">
			<h3 id="msgTitle">系统信息：</h3>
			</div>
			<div class="col-sm-2 text-right" style="margin-top: 13px;">
								<button id="comeBackMsgListBtn" onclick="href('bussiness/searchMsg.action');" type="button" class="btn btn-warning btn-sm">
						返回
					</button>
		<!-- <ul class="pager text-right" style="text-align:right;">
    			<li><a id="comeBackMsgListBtn" href="bussiness/searchMsg.action" ><span class="glyphicon glyphicon-menu-left"></span> 返回</a></li>
 			 </ul> -->
			</div>
		</div>
			<hr style="margin-top: 0px;"/>
		<div class="form-group">
		<div id="msgContent" style="font-size: 16px;line-height: 45px;">
		</div>
		</div>
		<div class="form-group">
		<div id="msgAddTime" class="text-right">
		</div>
		</div>
		</div>
	</div>
	<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
	<script src="res/js/bussiness/searchMsg.js" type="text/javascript"></script>
</body>
</html>

