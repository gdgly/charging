<%@ page contentType="text/html;charset=UTF-8"  language="java" pageEncoding="UTF-8" %>
<%--获取请求路径、引入全局css样式--%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
  <base href="<%=basePath%>">
  <link rel="stylesheet" type="text/css" href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css">
  <!-- Font Awesome -->
  <!-- <link rel="stylesheet" type="text/css" href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css"> -->
  <!-- Theme style -->
  <link rel="stylesheet" type="text/css" href="//cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css">
  <link rel="stylesheet" type="text/css" href="res/userplugins/css/AdminLTE.min.css">
  <link rel="stylesheet" type="text/css" href="res/userplugins/css/_all-skins.min.css">
  <link rel="stylesheet" type="text/css" href="res/css/xcConfirm2.css">
  <link rel="stylesheet" href="res/css/user/common.css">
  <style>
  	.box{
  		border-top: 2px solid rgb(210, 214, 222);
  		}
	.height400{
		height: 450px;
		}
	.height200{
		height: 280px;
		}
	body{
		background-color: #ecf0f5;
		height: 935px;
		}
	html, body,h1,h3,h4,h5{font-family: "Microsoft YaHei" ! important; }
	h5{
		color:#C1CDCD;
	}
  </style>
  <!-- Ionicons -->
  <!-- <link rel="stylesheet" type="text/css" href="//cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css"> -->
  <!-- iCheck -->
  <!-- <link rel="stylesheet" type="text/css" href="res/userplugins/plugins/iCheck/flat/blue.css"> -->
  <!-- Morris chart -->
  <!--  <link rel="stylesheet" type="text/css" href="res/userplugins/plugins/morris/morris.css"> -->
  <!-- jvectormap -->
  <!--  <link rel="stylesheet" type="text/css" href="res/userplugins/plugins/jvectormap/jquery-jvectormap-1.2.2.css"> -->
  <!-- Date Picker -->
  <!--  <link rel="stylesheet" type="text/css" href="res/userplugins/plugins/datepicker/datepicker3.css"> -->
  <!-- Daterange picker -->
  <!-- <link rel="stylesheet" type="text/css" href="res/userplugins/plugins/daterangepicker/daterangepicker-bs3.css"> -->
  <!-- bootstrap wysihtml5 - text editor -->
  <!-- <link rel="stylesheet" type="text/css" href="res/userplugins/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css"> -->
