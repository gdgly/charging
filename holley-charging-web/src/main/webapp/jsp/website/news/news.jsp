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
<div id="newsContainer" class="news container" style="padding-top: 20px;margin-top: 50px;">
  <div class="row">
  	<div class="col-sm-3 newstype">
  		<div class="list-group" style="margin-top: 55px;">
		  <a href="javascript:;" class="list-group-item <s:if test="#request.newstype== 1">active</s:if>" id="trendsTab" onclick="selectTrendsTab(this)">充电动态</a>
		  <a href="javascript:;" class="list-group-item <s:if test="#request.newstype== 2">active</s:if>" id="newsTab" onclick="selectNewsTab(this)">新闻资讯</a>
		</div>
  	</div>
  	<div class="col-sm-9 newslist">
  		<h4 id="title"><s:if test="#request.newstype== 1">充电动态</s:if><s:else>新闻资讯</s:else></h4>
  		<hr style="height:1px;border:none;border-top:1px solid #ccc;margin-top: 0;margin-bottom: 5px;">
  		<ul id="listGroup" class="list-group">
  		</ul>
  		<div style="text-align: center; margin-bottom: 50px;">
  			<button id="loadMoreBtn" class="loadMoreBtn" onclick="loadMore()">加载更多</button>
  			<p id="noMoreText" class="noMoreText hidden">没有更多内容</p>
  		</div>
  	</div>
  </div>
</div>

  <%@include file="../../common/global/js.jsp" %>
  <%@include file="../../common/global/footer.jsp"%>
  <script type="text/javascript">
     var newsType = <s:property value="#request.newstype" escape="false"/>;
     if(newsType == null){
    	 newsType = TRENDS
     }
   </script>
  <script src="res/js/website/news/news.js" type="text/javascript"></script>
</body>
</html>

