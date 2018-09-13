<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="common.jsp"%>
<script type="text/javascript">
msg= "${msg}";
if(!msg){
	frameHref('user/userlogin_init.action');	
}
</script>
    <!-- Content Header (Page header) -->
    <section class="content-header">
       <h1>系统提示</h1> 
    <!--   <ol class="breadcrumb">
        <li><i class="fa fa-user"></i> 个人中心</li>
        <li class="active">我的消息</li>
      </ol> -->
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
      <div class="col-xs-12">
       <div class="box box-info">
		<p class="text-center" style="padding-top: 12%;padding-bottom: 12%">
		<strong>提示信息：</strong>${msg}
		<s:if test="#request.backUrl != null">
		<a href="javascript:;" onclick="frameHref('${backUrl}');">
		<s:if test="#request.retrunDec != null">
		<s:property value="#request.retrunDec"/>
		</s:if>
		<s:else>
		返回
		</s:else>
		</a>
		</s:if>
		<s:elseif test="#request.backUrl2 != null">
		<a href="${backUrl2}">
		<s:if test="#request.retrunDec != null">
		<s:property value="#request.retrunDec"/>
		</s:if>
		<s:else>
		返回
		</s:else>
		</a>
		</s:elseif>
		<s:else>
		<a onclick="frameReload();" href="javascript:;">返回</a>
		</s:else>
		</p>
		</div>
       </div>
      </div>
    </section>
