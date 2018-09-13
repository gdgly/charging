<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>意见反馈</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
  <!-- ----------------------------------意见反馈列表 ------------------------------>
  	<div id="suggestionList">
	    <form class="form-inline" role="form">
		    <div class="form-group">
		    	<div class="input-group date" id="startDateDiv" style="width: 150px;">
                <input id="startDate" type="text" class="form-control" placeholder="开始时间"/>
                <span class="input-group-addon">
                	<span class="glyphicon glyphicon-calendar"></span>
                </span>
		     </div>
		     <span>—</span>
 		 	 <div class='input-group date' id="endDateDiv" style="width: 150px;">
                <input id="endDate" type="text" class="form-control" placeholder="结束时间"/>
                <span class="input-group-addon">
                	<span class="glyphicon glyphicon-calendar"></span>
                </span>
		      </div>
				<input id="keyword" type="text" class="form-control" placeholder="用户昵称/手机号码/操作内容" style="width: 240px;"/>
				<select id="dealStatus" name="dealStatus" class="form-control">
			     	<option value="0">处理状态</option>
			     	<s:iterator value="#request.statusList" var="item" status="st">
			     		<s:if test="#request.dealStatus == #item.value">
			     			<option value="${item.value}" selected="selected">${item.text}</option>
			     		</s:if>
			     		<s:else>
				     		<option value="${item.value}">${item.text}</option>
			     		</s:else>
			     	</s:iterator>
			     </select>
			</div>
			 <div class="form-group pull-right">
				<button type="button" id="queryBtn" class="btn btn-primary">查询</button>
			 </div>
	    </form>
	    <!-- 表格 -->
	    <table class="table table-condensed table-hover" id="suggestionTable">
	   		<thead>
	   			<tr>
	   				<th>反馈时间</th>
	   				<th>用户编码</th>
	   				<th>用户类型</th>
	   				<th>用户昵称</th>
	   				<th>手机号码</th>
	   				<th>状态</th>
	   				<th>内容</th>
	   				<th>图片</th>
	   			</tr>
	   		</thead>
		   	<tbody></tbody>
		</table>
	     <!-- 分页条 -->
	    <%@include file="../common/global/pagingtoolbar.jsp" %>
  	</div>
  	<!-- ----------------------------------意见反馈详细 ------------------------------>
  	<div id="suggestionDetail" class="hide">
  		<span class="glyphicon glyphicon-menu-left" data-toggle="tooltip" title="返回" aria-hidden="true" 
	    style="cursor:pointer;font-weight: bold;font-size: 16px;" onclick="showSuggestionResult('suggestionList');"></span>
	     <button id="dealBtn" type="button" class="btn btn-primary pull-right hide">已处理</button>
    	<form class="form-horizontal" role="form" style="margin-top: 20px;">
	    	<div class="form-group">
		        <label class="col-sm-2 control-label">用户编码</label>
		        <div class="col-sm-4">
		      	  <p id="detail_userid" class="form-control-static"></p>
		        </div>
		        <label class="col-sm-2 control-label">用户类型</label>
		        <div class="col-sm-4">
		      	  <p id="detail_usertype" class="form-control-static"></p>
		        </div>
    	    </div>
    	    <div class="form-group">
		        <label class="col-sm-2 control-label">用户昵称</label>
		        <div class="col-sm-4">
		      	  <p id="detail_username" class="form-control-static"></p>
		        </div>
		        <label class="col-sm-2 control-label">手机号码</label>
		        <div class="col-sm-4">
		      	  <p id="detail_phone" class="form-control-static"></p>
		        </div>
    	    </div>
    	    <div class="form-group">
		        <label class="col-sm-2 control-label">状态</label>
		        <div class="col-sm-4">
		      	  <p id="detail_status" class="form-control-static"></p>
		        </div>
		        <label class="col-sm-2 control-label">反馈时间</label>
		        <div class="col-sm-4">
		          <p id="detail_addtime" class="form-control-static"></p>
		        </div>
    	    </div>
    	    <div class="form-group">
		        <label class="col-sm-2 control-label">内容</label>
		        <div class="col-sm-10">
		      	  <p id="detail_content" class="form-control-static"></p>
		        </div>
    	    </div>
    	    <div id="picDiv" class="form-group">
    	    </div>
   	    </form>
  	</div>
  </div>
  <script src="res/js/sys/suggestion.js" type="text/javascript"></script>
</body>
</html>

