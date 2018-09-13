<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@include file="css.jsp"%>
<%@include file="js.jsp"%>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>51充电</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <link rel="shortcut icon" href="${imgUrl}res/img/mark/logo.png"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
<%@include file="header.jsp"%>
<%@include file="left.jsp"%>
  <!-- Content Wrapper. Contains page content -->
    <!-- Content Header (Page header) -->
 	 <div class="content-wrapper"> 
    <iframe id="mainWindow" src="userAdmin/initUserHome.action" allowtransparency="true" style="background-color=transparent" title="test" frameborder="0" width="100%" height="100%" scrolling="no">

	</iframe>
	</div> 
<%@include file="footer.jsp"%>
</div>
</body>
<script type="text/javascript">
var tradeStatus = "${tradeStatus}";
if(tradeStatus){
$("#mainWindow").attr("src","userAccount/initUserAccount.action");
}
</script>
</html>