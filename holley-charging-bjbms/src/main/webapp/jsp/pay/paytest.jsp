<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <%@include file="../common/global/meta.jsp"%>
    <link rel="stylesheet" href="res/css/frame/login.css" >
<style type="text/css">
</style>
</head>
<body style="background-color: #ecf4f7">
  <!--main-content-->
  <div class="container">
  	<button id="gopay" type="button" class="btn btn-warning btn-sm">去支付</button>
  </div>
  <%@include file="../common/global/js.jsp" %>
  <script type="text/javascript">
  $("#gopay").on("click",function(){
	  $.ajax({
			type:"POST",
			url:'user/pay_wechatPay.action',
			//data:param,
			dataType:'json',
	        cache: false,
	        success: function(data){
	        	var url = data.wechatPayUrl;
	        	if(!isEmpty(url)){
	        		window.location.href=url;
	        	}else{
	        		alert("支付账单生成失败");
	        	}
	        }
	  });
		  
  });
  </script>
</body>
</html>

