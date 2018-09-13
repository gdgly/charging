<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<html lang="zh-CN">
<head>
<%@include file="global/meta.jsp"%>
</head>
<body>
	<%@include file="global/header.jsp"%>
	<div class="bg" style="height: 75%;">
		<!--main-content-->
		<div class="container main-body" style="height: 50%;">
			<p class="text-center" style="padding-top: 14%;">
				<strong>提示信息：</strong>${msg}
					<a onclick="history.go(-1)" href="javascript::">返回</a>
			</p>
		</div>
	</div>
	<%@include file="global/js.jsp"%>
<%-- 	<%@include file="global/footer.jsp"%> --%>
</body>
<script type="text/javascript">
</script>
</html>