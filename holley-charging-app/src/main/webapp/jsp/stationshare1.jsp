<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<base href="<%=basePath%>">
<html lang="zh-CN" style="height: 100%">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="content-language" content="zh-CN" />
    <meta name="viewport" content="width=device-width,initial-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="format-detection" content="telephone=no" />
    <title>51充电</title>
    <link rel="stylesheet" href="css/reset.css">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/pullToRefresh.css" />
</head>
<body style="height: 100%;">

<header class="header f-cb">
    <h1>51充电</h1>
</header>

<div class="banner">
    <img src="img/icon-img1.jpg" alt="">
    <div class="dowload f-cb">
        <div class="img fl">
            <img src="img/home_44.png" alt="">
        </div>
        <div class="text fl">
            <h1>51充电</h1>
            <p>最懂你的找桩软件</p>
        </div>
        <a href="javascript:;" class="butt fr" onclick="download()">立即下载</a>
    </div>
</div>
<div class="info">
    <ul class="info-box">
        <li class="cap">
            <h1><s:property value="#request.station.stationname"/></h1>
        </li>
        <li class="info-class f-cb">
        	<s:if test="#request.station.fastnum > 0">
	            <i class="fast"></i>
	            <span>快充<s:property value="#request.station.fastnum"/>个</span>
        	</s:if>
        	<s:if test="#request.station.slownum > 0">
	            <i class="packed"></i>
	            <span>慢充<s:property value="#request.station.slownum"/>个</span>
        	</s:if>
        </li>
        <li>
            <p class="info-addr">
            	<s:property value="#request.station.address"/>
            </p>
            <p class="info-tel">
            	<s:property value="#request.station.linkphone"/>
            </p>
            <p id="stationid" hidden>
            	<s:property value="#request.station.stationid"/>
            </p>
        </li>
    </ul>
</div>
<dl id="comment" class="list">
    <dt id="commentdt" class="list-cap xingx f-cb">
        <span id="commenttotal"></span>
        <s:if test="#request.station.score < 1">
        	<i class="n2"></i>
        	<i class="n2"></i>
        	<i class="n2"></i>
        	<i class="n2"></i>
        	<i class="n2"></i>
        </s:if>
        <s:elseif test="#request.station.score < 2">
        	<i class="n1"></i>
        	<i class="n2"></i>
        	<i class="n2"></i>
        	<i class="n2"></i>
        	<i class="n2"></i>
        </s:elseif>
        <s:elseif test="#request.station.score < 3">
        	<i class="n1"></i>
        	<i class="n1"></i>
        	<i class="n2"></i>
        	<i class="n2"></i>
        	<i class="n2"></i>
        </s:elseif>
        <s:elseif test="#request.station.score < 4">
        	<i class="n1"></i>
        	<i class="n1"></i>
        	<i class="n1"></i>
        	<i class="n2"></i>
        	<i class="n2"></i>
        </s:elseif>
        <s:elseif test="#request.station.score < 5">
        	<i class="n1"></i>
        	<i class="n1"></i>
        	<i class="n1"></i>
        	<i class="n1"></i>
        	<i class="n2"></i>
        </s:elseif>
        <s:else>
        	<i class="n1"></i>
        	<i class="n1"></i>
        	<i class="n1"></i>
        	<i class="n1"></i>
        	<i class="n1"></i>
        </s:else>
        <b id="totalscore"><s:property value="#request.station.score"/>.0</b>
    </dt>
    <div id="wrapper">
	    <ul id="commentul">
	    </ul>
	</div>
</dl>

 <script src="js/jQuery.js"></script>
 <script src="js/main.js"></script>
 <script src="js/iscroll.js"></script>
 <script src="js/pullToRefresh.js"></script>
 <script src="js/stationshare.js"></script>
 <script type="text/javascript">
 	var imgurl = '<s:property value="#request.imgurl"/>';
 	var iosurl = '<s:property value="#request.iosurl"/>';
 	var androidurl = '<s:property value="#request.androidurl"/>';
 </script>
</body>
</html>