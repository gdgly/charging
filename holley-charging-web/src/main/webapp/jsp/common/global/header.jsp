<%--
  头部、全文检索、导航栏
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
  <header>
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				 <button class="navbar-toggle collapsed" type="button" data-toggle="collapse" data-target=".navbar-collapse">
		            <span class="sr-only">Toggle navigation</span>
		            <span class="icon-bar"></span>
		            <span class="icon-bar"></span>
		            <span class="icon-bar"></span>
		          </button>
		        <%--  <div class="hidden-xs" style="position: relative;float: left;top: 18px;">
					<img alt="" src="${imgUrl}res/img/home/logo.png" style="width: 50px;height: 50px;">
		         </div>
				 <a class="navbar-brand" style="color: #fff;text-decoration: none;font-size:18px;letter-spacing: 3px;" href="#">&nbsp;&nbsp;爱车易充</a>
				  --%>
				 <a class="navbar-brand" style="color: #fff;text-decoration: none;padding-top: 9px;" href="home/homepage.action">
				 	<img alt="" src="${imgUrl}res/img/home/logo.png">
				 </a>
			</div>
			<div class="navbar-collapse collapse">
				<ul class="nav navbar-nav">
					<li class="<s:if test="#request.currentModule=='homepage'">active nav-current</s:if>">
						<a href="home/homepage.action">首页</a>
					</li>
					<li class="<s:if test="#request.currentModule=='chargenet'">active nav-current</s:if>">
						<a href="chargenet/chargenet.action">充电网络</a>
					</li>
					<li class="<s:if test="#request.currentModule=='news'">active nav-current</s:if>">
						<a href="news/news.action">动态</a>
					</li>
				</ul>
				<form class="navbar-form navbar-right" style="padding-top: 15px;padding-bottom: 15px;">
				<s:if test="#request.webUser != null">
				<s:if test="#request.webUser.roleType == 2 || #request.webUser.roleType == 4">
				 <a href="bussiness/safeInfo.action"><img class="img-circle" src="${imgUrl}${webUser.headImg}" style="height: 30px;width: 30px;margin-right: 5px;margin-top: -4px;">${webUser.userName}</a>
					&nbsp;
					<span style="color:#ffffff;">|</span>
					&nbsp;
				<a href="user/userlogin_logout.action">退出</a>
				</s:if>
				<s:elseif test="#request.webUser.roleType == 3 || #request.webUser.roleType==5">
					<a href="userAdmin/home.action"><img class="img-circle" src="${imgUrl}${webUser.headImg}" style="height: 30px;width: 30px;margin-right: 5px;margin-top: -4px;">${webUser.userName}</a>
					&nbsp;
					<span style="color:#ffffff;">|</span>
					&nbsp;
					<a href="user/userlogin_logout.action">退出</a>
				</s:elseif>
				</s:if>
				<s:else>
				<div style="float: left;background-image:url(${imgUrl}res/img/btn/login_button_bg.png);width:50px;height:21px;padding-left:18px;padding-top:2px;">
						<a href="<%=request.getContextPath()%>/user/userlogin_init.action">
							<span style="color:#f49447;">登录</span>
						</a>
					</div>
					<div style="float: left;margin-left: 20px;margin-right: 20px;padding-top:2px;">
						<span style="color:#ffffff;">|</span>
					</div>
					<div style="float: left;background-image:url(${imgUrl}res/img/btn/register_button_bg.png);width:50px;height:21px;padding-left:8px;padding-top:2px;">
						<a href="<%=request.getContextPath()%>/user/userregister_init.action">
							<span style="color:#f49447;">注册</span>
						</a>
					</div>
				</s:else>
				</form>
				<!-- 
				<form class="navbar-form navbar-right" role="search" style="padding-top: 10px;padding-bottom: 10px;">
					<div class="form-group">
						<input type="text" class="form-control input-sm" placeholder="Search" style="width: 120px;border-radius:12px;">
                        <span class="glyphicon glyphicon-search" style="margin-left: -25px"></span>
					</div>
				</form>
				 -->
			</div>
		</div>
	</div>
</header>

