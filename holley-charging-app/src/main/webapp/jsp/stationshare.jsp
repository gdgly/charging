<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<base href="<%=basePath%>">
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="content-language" content="zh-CN" />
    <meta name="viewport" content="width=device-width,initial-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="format-detection" content="telephone=no" />
    <title>51充电</title>
    <link rel="stylesheet" href="css/reset.css">
    <link rel="stylesheet" href="css/style.css">
    <link rel="shortcut icon" href="${imgurl}res/img/mark/logo.png"/>
    <script src="js/jQuery.js" type="text/javascript"></script>
    <script src="js/main.js" type="text/javascript"></script>
    <script src="js/stationshare.js" type="text/javascript"></script>
    
    <style>
		*{margin:0; padding:0;}
		img{max-width: 100%; height: auto;}
	</style>
</head>
<body>

<header class="header f-cb">
    <h1>51充电</h1>
</header>

<div class="banner">
	<s:if test="#request.station.img == ''">
		<img src="img/station.jpg" alt="">
	</s:if>
	<s:else>
	    <img src="<s:property value="#request.imgurl"/><s:property value="#request.station.img"/>" alt="">
	</s:else>
    <div class="dowload f-cb">
        <div class="img fl">
            <img src="img/home_44.png" alt="">
        </div>
        <div class="text fl">
            <h1>51充电</h1>
            <p>最懂你的找桩软件</p>
        </div>
        <a href="javascript:;" class="butt fr" onclick="ondownload()">立即下载</a>
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
            <p id="stationid" style="visibility: hidden;">
            	<s:property value="#request.station.stationid"/>
            </p>
        </li>
    </ul>
</div>
<dl id="comment" class="list">
    <dt id="commentdt" class="list-cap xingx f-cb">
        <span id="star"></span>
        <b id="totalscore"></b>
    </dt>
    <ul id="commentul">
    </ul>
    <div style="text-align: center;">
		<button id="loadMoreBtn" onclick="loadMore()" style="width: 100%;height: 40px;border-style: none;background-color: #f0f0f0">加载更多</button>
		<p id="noMoreText" style="visibility:hidden;height: 40px;line-height: 40px;background-color: #f0f0f0;">没有更多内容</p>
	</div>
</dl>
<script type="text/javascript">
 var imgurl = '<s:property value="#request.imgurl"/>';
 var iosurl = '<s:property value="#request.iosurl"/>';
 var androidurl = '<s:property value="#request.androidurl"/>';
 var totalscore = '<s:property value="#request.station.score"/>';
 
 $(document).ready(function(){
	 if(totalscore == null){
		 totalscore = 0.0;
	 }else if(totalscore > 50){
		 totalscore = 5.0;
	 }else{
		 totalscore = totalscore/10;
	 }
	 
	 var star = parseInt(totalscore, 0);
	 var star_half = (totalscore-star) > 0 ? 1 : 0;
	 var star_empty = 5 - star - star_half;
	 
	 var html = '';
	 for(var i=0;i<star;i++){
		 html += '<i class="n1"></i>';
	 }
	 for(var i=0;i<star_half;i++){
		 html += '<i class="n3"></i>';
	 }
	 for(var i=0;i<star_empty;i++){
		 html += '<i class="n2"></i>';
	 }
	 $('#star').html(html);
	 if(String(totalscore).length == 1){
	 	$('#totalscore').text(String(totalscore)+".0");
	 }else{
		 $('#totalscore').text(totalscore);
	 }
});
 
 function ondownload(){
	    var sUserAgent = navigator.userAgent.toLowerCase();
	    var bIsWeixin = sUserAgent.match(/MicroMessenger/i) == "micromessenger";
	    //智能机浏览器版本信息
		var browser = {
		 	versions: function() {
				 var u = navigator.userAgent, app = navigator.appVersion;
				 return {//移动终端浏览器版本信息 
					 mobile: !!u.match(/AppleWebKit.*Mobile.*/) || !!u.match(/AppleWebKit/), //是否为移动终端
					 ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
					 android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
					 iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //是否为iPhone或者QQHD浏览器
					 iPad: u.indexOf('iPad') > -1, //是否iPad
					 webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
		 		};
		 	}(),
		 	language: (navigator.browserLanguage || navigator.language).toLowerCase()
		 }
		 if(bIsWeixin){
			var winHeight = typeof window.innerHeight != 'undefined' ? window.innerHeight : document.documentElement.clientHeight;
			var weixinTip;
			if (browser.versions.ios || browser.versions.iPhone || browser.versions.iPad) {
				weixinTip = $('<div id="weixinTip"><p><img src="img/live_weixin_ios.png" alt="微信打开"/></p></div>');
			 }else{
				weixinTip = $('<div id="weixinTip"><p><img src="img/live_weixin_android.png" alt="微信打开"/></p></div>');
			 }
			
			$("body").append(weixinTip);
			$("#weixinTip").css({
				"position":"fixed",
				"left":"0",
				"top":"0",
				"height":winHeight,
				"width":"100%",
				"z-index":"1000",
				"background-color":"rgba(0,0,0,0.8)",
				"filter":"alpha(opacity=80)",
			});
			$("#weixinTip p").css({
				"text-align":"center",
				"margin-top":"5%",
				"padding-left":"5%",
				"padding-right":"5%"
			});
		} else { 
			/* window.location = "http://a.app.qq.com/o/simple.jsp?pkgname=com.bocai.huali"; */
			  if (browser.versions.ios || browser.versions.iPhone || browser.versions.iPad) {
				 window.location = iosurl;
			 }else if (browser.versions.android){
				 window.location = androidurl;
			 } 
		 } 
	}
 
</script>
</body>
</html>