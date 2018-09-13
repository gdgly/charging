<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@include file="../common/global/meta.jsp"%>
<%@include file="../common/global/js.jsp"%>
<%@include file="head.jsp"%>
<html lang="zh-CN">
<head>
</head>
<body>
<div class="bg"  style="margin-top: 60px;">
  <!--main-content-->
<div class="container main-body"  style="height: 50%;">
<p class="text-center" style="padding-top: 14%;">
<strong>提示信息：</strong>${msg}
<s:if test="#request.backUrl != null">
<a href="<s:property value="#request.backUrl"/>">
<s:if test="#request.retrunDec != null">
<s:property value="#request.retrunDec"/>
</s:if>
<s:else>
返回
</s:else>
</a>
</s:if>
<s:else>
<a onclick="history.go(-1)" href="javascript::">返回</a>
</s:else>
</p>
</div>
</div>
<!-- <footer id="footer" class="main-footer">
    <div class="container" >
       <div class="row">
       		<div class="col-md-12" style="text-align: center;">
       			<p style="margin-top: 5px">版权所有华立科技 2015-2016 </p>
       		</div>
       </div>
    </div>
</footer> -->
</body>
<script type="text/javascript">
setBowHeight2();
$("#subMenu").hide();
$("#mainMenu").hide();
</script>
</html>