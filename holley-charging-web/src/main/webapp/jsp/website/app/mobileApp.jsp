<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../../common/global/top.jsp" %>
<html lang="zh-CN" style="height: 100%;">
<head>
  <title>移动网络</title>
  <%@include file="../../common/global/meta.jsp"%>
  <link rel="stylesheet" href="res/css/website/mobileApp.css" >
<style type="text/css">
</style>
</head>
<body style="height: 100%;">
  <%@include file="../../common/global/header.jsp"%>
<!--main-->
<div id="content" class="mobileApp container-fluid" style="background-color: #ebf8f1;height: 100%;">
  <!--main-content-->
  <div id="main">
  	<div class="row">
  		<div class="col-sm-5" style="text-align: right;vertical-align: middle;">
  			<img alt="" src="${imgUrl}res/img/app/appbg.png" style="width: 285px;height: 585px;">
  		</div>
  		<div class="col-sm-1" >
  		</div>
  		<div class="col-sm-6">
  			<h1>移动应用</h1>
  			<hr style="border-top-color: #0a94f2"/>
  			<div style="margin-top: 50px;">
	  			<h2>产品介绍</h2>
	  			<div style="padding-top: 15px;">
		  			<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;为电动汽车用户提供移动应用服务，可通过APP<br>
		  			<span style="color: #f49447">查找充电点</span>，实现户外、室内<span style="color: #f49447">精确导航</span>，<span style="color: #f49447">预约充电</span>，<br>
		  			<span style="color: #f49447">便捷支付</span>等功能，并可通过APP的社交功能，与附近<br>
		  			车友进行互动与分项。</p>
	  			</div>
  			</div>
  			<div style="margin-top: 30px;">
  				<h2>APP下载</h2>
  				<div style="padding-top: 20px;" >
  					<button type="button" class="btn btn-green btn-lg" onclick="loadAndroid();">
  						<img alt="" src="${imgUrl}res/img/app/android.png">Android
  					</button>
  					<span style="margin-left: 20px;margin-right: 20px;"></span>
  					<button type="button" class="btn btn-gray btn-lg" onclick="loadIOS();">
  						&nbsp;&nbsp;&nbsp;&nbsp;
	  					<img alt="" src="${imgUrl}res/img/app/ios.png">IOS
	  					&nbsp;&nbsp;&nbsp;&nbsp;
  					</button>
  				</div>
  			</div>
  			<div style="margin-top: 50px;">
  				<div style="float:left;margin-right: 10px;">
	  				<img alt="" src="" style="width: 75px;height: 75px;">
  				</div>
	  				<p>扫描二维码，关注充电桩<br>立即开启你的充电之旅</p>
  			</div>
  		</div>
  	</div>
  </div>
</div>

  <%@include file="../../common/global/js.jsp" %>
  <%@include file="../../common/global/footer.jsp"%>
  <script src="res/js/website/app/mobileApp.js" type="text/javascript"></script>
</body>
</html>

