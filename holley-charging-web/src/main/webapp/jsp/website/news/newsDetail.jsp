<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../../common/global/top.jsp" %>
<html lang="zh-CN" style="height: 100%">
<head>
  <title>动态</title>
  <%@include file="../../common/global/meta.jsp"%>
  <link rel="stylesheet" href="res/css/website/news.css" >
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../../common/global/header.jsp"%>
<!--main-->
<div id="content" class="news container" style="margin-top: 50px;">
  <div class="row">
  	<div class="col-lg-12">
  		<div class="text-center">
  			<h3><s:property value="#request.news.title" escape="false"/></h3>
  		</div>
  		<div class="text-center">
			<a style="text-align: left;" href="news/news.action?type=<s:property value="#request.news.type" escape="false"/>">
				<span style="color: #0a94f2"><s:property value="#request.news.typedesc" escape="false"/></span>
			</a>
			<span style="margin-left: 30px;"></span>
			<span class="glyphicon glyphicon-eye-open"></span>&nbsp;<s:property value="#request.news.visitcount" />
			<span style="margin-left: 30px;"></span>
			<span class="glyphicon glyphicon-time"></span>&nbsp;<s:property value="#request.news.publishdatestr" />
  		</div>
		<hr style="height:1px;border:none;border-top:1px solid #ccc;">
  		<div><s:property value="#request.news.content" escape="false"/></div>
  	</div>
  </div>
</div>

  <%@include file="../../common/global/js.jsp" %>
  <%@include file="../../common/global/footer.jsp"%>
  <script type="text/javascript">
  	$(function(){
  		if($("#content").height() < getContentHeight()){
  			$("#content").css("height",getContentHeight());
  		}
  	});
  	
  	function getContentHeight(){
  		return $(window).height()-HEADER_HEIGHT-FOOTER_HEIGHT;
  	}
  </script>
  
</body>
</html>

