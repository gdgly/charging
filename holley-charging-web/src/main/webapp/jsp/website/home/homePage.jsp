<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../../common/global/top.jsp" %>
<html lang="zh-CN" style="overflow: hidden; height: 100%;">
<head>
  <title>首页</title>
  <%@include file="../../common/global/meta.jsp"%>
  <link rel="stylesheet" href="<%=basePath%>res/css/website/homePage.css" >
<style type="text/css">
</style>
</head>
<body style="overflow: hidden; height: 100%;">
  <%@include file="../../common/global/header.jsp"%>
<!--main-->
	<div class="homePage" id="content" style="height: 100%;margin-top: 50px;">
	    <div class="section">
	        <div id="carousel" class="carousel slide">
			   <!-- 轮播（Carousel）项目 -->
			   <div class="carousel-inner">
			      <div class="item active" style="background-size:cover;">
			         <img src="${imgUrl}res/img/home/banner_01.jpg" alt="First slide">
			      </div>
			      <div class="item" style="background-size:cover;">
			         <img src="${imgUrl}res/img/home/banner_02.jpg" alt="Second slide">
			      </div>
			      <!-- 轮播（Carousel）指标 -->
				   <ol class="carousel-indicators">
				      <li data-target="#carousel" data-slide-to="0" class="active"></li>
				      <li data-target="#carousel" data-slide-to="1"></li>
				   </ol>  
			   </div>
		  		<div class="container" style="margin-top: 20px;">
		  			<div class="row">
			  			<div class="col-xs-6 col-sm-3">
				          <h4>互联互通</h4>
				          <p>为线上充电设施、线下车主</p>
				          <p>提供简单方便的充电服务。</p>
				        </div>
				        <div class="col-xs-6 col-sm-3">
				          <h4>海纳百川</h4>
				          <p>开放性平台，为各类运营商、</p>
				          <p>政府及个人提供服务。</p>
				        </div>
				        <div class="col-xs-6 col-sm-3">
				          <h4>智能改造</h4>
				          <p>为运营商提供智能计量、智</p>
				          <p>能通信解决方案。</p>
				        </div>
				        <div class="col-xs-6 col-sm-3">
				          <h4>智慧充电</h4>
				          <p>安全充电，智能提醒，提供</p>
				          <p>个性化充电服务</p>
				        </div>
		  			</div>
		  		</div>
		  	</div>
	    </div>
	    <div class="section" style="background-color: #ebf8f1;">
	        <div id="appContainer" class="container" style="padding-top: 5%;">
		 	  <div class="row">
		  		<div class="hidden-xs col-sm-6" style="text-align: right;vertical-align: middle;">
		  			<div class="thumbnail" style="float:right; ">
			  			<img alt="" src="${imgUrl}res/img/app/appbg.png" style="max-width: 285px;max-height: 585px;">
		  			</div>
		  		</div>
		  		<div class="col-xs-12 col-sm-6" >
		  			<h1>移动应用</h1>
		  			<hr class="title-hr" style="width: 145px;border-top-color: #0a94f2;"/>
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
		  				<%-- <div style="padding-top: 20px;" >
		  					<button type="button" class="btn btn-green btn-lg" onclick="loadAndroid();">
		  						<img alt="" src="${imgUrl}res/img/app/android.png">Android
		  					</button>
		  					<span style="margin-left: 20px;margin-right: 20px;"></span>
		  					<button type="button" class="btn btn-gray btn-lg" onclick="loadIOS();">
		  						&nbsp;&nbsp;&nbsp;&nbsp;
			  					<img alt="" src="${imgUrl}res/img/app/ios.png">IOS
			  					&nbsp;&nbsp;&nbsp;&nbsp;
		  					</button>
		  				</div> --%>
		  				<div class="appLoad">
		  					<div class="pull-left" style="padding-right: 10px;">
			  					<div class="thumbnail" style="width: 100px;height: 100px;">
						  			<img id="iosCodeImg" alt="" src="${imgUrl}res/img/home/ios_code.jpg" onmouseenter="openImg(this,'ios')" onmouseleave="closeImg(this)">
				         		</div>
				         		<div class="text-center">
					         		<p style="margin-bottom: 0;">IOS</p>
					         		<p>客户端下载</p>
				         		</div>
		  					</div>
			         		<div class="pull-left" style="padding-left: 10px;padding-right: 20px;">
			  					<div class="thumbnail" style="width: 100px;height: 100px;">
						  			<img id="andriodCodeImg" alt="" src="${imgUrl}res/img/home/andriod_code.jpg" onmouseenter="openImg(this,'andriod')" onmouseleave="closeImg(this)">
				         		</div>
				         		<div class="text-center">
					         		<p style="margin-bottom: 0;">Andriod</p>
					         		<p>客户端下载</p>
				         		</div>
		  					</div>
			         		<%-- <div class="pull-left" style="padding-left: 10px;">
			  					<div class="thumbnail" style="width: 100px;height: 100px;">
						  			<img id="wechatCodeImg" alt="" src="${imgUrl}res/img/home/wechat_code.jpg" onmouseenter="openImg(this,'wechat')" onmouseleave="closeImg(this)">
				         		</div>
				         		<div class="text-center">
					         		<p style="margin-bottom: 0;">扫描二维码</p>
					         		<p>关注51充电</p>
				         		</div>
		  					</div> --%>
		  				</div>
		  			</div>
		  			<div style="margin-top: 180px;">
			  			<h4 style="color:#6d6e72;padding-left: 10px;">立即开启你的充电之旅...</h4>
		  			</div>
		  			<%-- <div style="margin-top: 50px;">
		  				<div style="float:left;margin-right: 10px;">
			  				<img alt="" src="${imgUrl}res/img/home/barcode.jpg" style="width: 75px;height: 75px;">
		  				</div>
			  			<p>扫描二维码，关注充电桩<br>立即开启你的充电之旅</p>
		  			</div> --%>
		  		</div>
		  	  </div>
		  	</div>
	    </div>
	    <div class="section">
	    	<div style="padding-top:10% ">
	    		<div class="text-center">
	  				<h3 style="color: #fff">主要功能</h3>
	  				<img alt="" src="${imgUrl}res/img/home/head_decoration_02.png" style="position: relative;margin-top: -22px;width: 270px;">
	  			</div>
		  		<div class="container-fluid" style="text-align: center;background-color:rgba(255,255,255,0.3);margin-top: 20px;">
		  			<div class="container">
			         	<div class="pull-left" style="width: 20%">
			         		<div class="thumbnail">
					  			<img alt="" src="${imgUrl}res/img/home/function_search.png">
			         		</div>
				  			<div class="caption">
				  				<h5>设施查询</h5>
				  			</div>
		  				</div>
			         	<div class="pull-left" style="width: 20%">
			         		<div class="thumbnail">
					  			<img alt="" src="${imgUrl}res/img/home/function_address.png">
			         		</div>
				  			<div class="caption">
				  				<h5>精确导航</h5>
				  			</div>
	  					</div>
			         	<div class="pull-left" style="width: 20%">
			         		<div class="thumbnail">
					  			<img alt="" src="${imgUrl}res/img/home/function_appoint.png">
			         		</div>
				  			<div class="caption">
				  				<h5>预约充电</h5>
				  			</div>
		  				</div>
			         	<div class="pull-left" style="width: 20%">
			         		<div class="thumbnail">
					  			<img alt="" src="${imgUrl}res/img/home/function_pay.png">
			         		</div>
				  			<div class="caption">
				  				<h5>便捷支付</h5>
				  			</div>
		  				</div>
			         	<div class="pull-left" style="width: 20%">
			         		<div class="thumbnail">
					  			<img alt="" src="${imgUrl}res/img/home/function_community.png">
			         		</div>
				  			<div class="caption">
				  				<h5>互动社区</h5>
				  			</div>
		  				</div>
		  			</div>
		  		</div>
	    	</div>
	  		<div class="function-bg">
				<img alt="" src="${imgUrl}res/img/home/bg_02.png" style="width:100%;height: 100%">
			</div>
	    </div>
	</div>
	<%@include file="../../common/global/js.jsp" %>
  <%@include file="../../common/global/footer.jsp"%>
  <script src="<%=basePath%>res/js/website/home/homePage.js" type="text/javascript"></script>
 
</body>
</html>

