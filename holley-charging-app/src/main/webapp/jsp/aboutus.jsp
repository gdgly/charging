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
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="content-language" content="zh-CN" />
    <meta name="viewport" content="width=device-width,initial-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="format-detection" content="telephone=no" />
    <title>51充电</title>
    <link rel="stylesheet" href="css/reset.css">
    <link rel="stylesheet" href="css/style.css">
    <script src="js/jQuery.js"></script>
    <script src="js/main.js"></script>
</head>
<body>
<div class="info-text">
    <div class="cap">
        <span>关于51充电</span>
    </div>
    <div class="text">
    	<pre>      51充电App是由华立科技股份有限公司打造的一款能够精确导航与智能充电的找桩软件。</pre>
        <pre>      通过此款APP可以将充电设备与云服务平台紧密的连接起来，建设与分享卓越的充电服务技术平台，致力于打造一个涵盖范围广，充电效率高的充电服务联盟。努力收录全国范围内的充电桩，精确的导航让用户轻松的找到每一个充电桩。只要你有桩，你就是运营商，充分的利用了每一个充电桩，大大减轻了电动汽车充电难的烦恼。支持支付宝、微信与账号钱包支付，让充电与预约更加简便。</pre>
    </div>
</div>
<script>
</script>
</body>
</html>