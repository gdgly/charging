<%@ page contentType="text/html;charset=UTF-8"  language="java" pageEncoding="UTF-8" %>
<%--获取请求路径、引入全局css样式--%>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<base href="<%=basePath%>">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css"  >
<link rel="stylesheet" type="text/css" href="//cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="//cdn.bootcss.com/fullPage.js/2.7.8/jquery.fullPage.min.css">
<link rel="stylesheet/less" type="text/css" href="res/css/common/myLess.less">
<link rel="stylesheet/less" type="text/css" href="res/css/common/bootstrap-datetimepicker.css">
<link rel="stylesheet/less" type="text/css" href="res/css/common/xcConfirm.css">
<link rel="stylesheet" href="res/css/common/common.css">
<link rel="shortcut icon" href="${imgUrl}res/img/mark/logo.png"/>

<div id="basePath" class="hide"><%=basePath%></div>
<title>51充电后台管理系统</title>
<script type="text/javascript">
var IMG_SRC = "${imgUrl}";
var WEB_ROOT = "${webroot}";
</script>

