<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
    <!-- Content Header (Page header) -->
    <section class="content-header">
       <h1>信息验证</h1> 
      	<ol class="breadcrumb">
      	 <li>
      	  	<a href="javascript:;" onclick="frameReload();">
      	 		<i class="fa fa-home"></i> 首页
      	 	</a>
      	 </li>
	        <li>
	        <i class="fa fa-user"></i> 个人中心
	        </li>
	        <li class="active">信息验证</li>
      	</ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
       <div class="col-xs-12">
        <div class="box box-info">
            <div class="box-header with-border">
              <h3 class="box-title">您正在使用手机验证码验证身份，请完成以下操作</h3>
             <button type="button" onclick="href('userAdmin/initUserInfo.action');" class="btn btn-info pull-right btn-sm">返回</button>
               <button id="volidChangePwdBtn" type="button" class="btn btn-info pull-right btn-sm" style="margin-right: 10px;">下一步</button>
            </div>
            <!-- /.box-header -->
            <!-- form start -->
              <div class="box-body">
             			<form class="form-horizontal" role="form">
				<div class="form-group">
					<label for="phone" class="col-sm-2 control-label">手机号码</label>
					<div class="col-sm-5">
					<p id="phone" class="pTop8">
						<s:if test="#request.currentUser.phone.length()==11">
						<s:property value="#request.currentUser.phone.substring(0,3)+'...'+#request.currentUser.phone.substring(#request.currentUser.phone.length()-3,#request.currentUser.phone.length())" />
						</s:if>
						<s:else>
						<s:property value="#request.currentUser.phone"/>
						</s:else>
					</p>
<%-- 						<input type="text" class="form-control" id="phone"
							disabled="disabled"
							placeholder="<s:if test="#request.currentUser.phone.length()==11">
<s:property value="#request.currentUser.phone.substring(0,3)+'...'+#request.currentUser.phone.substring(#request.currentUser.phone.length()-3,#request.currentUser.phone.length())" />
</s:if>
<s:else>
<s:property value="#request.currentUser.phone"/>
</s:else>" /> --%>
						<input type="hidden" id="actionType" name="actionType"
							value="${actionType}" />
					</div>
				</div>
				<div class="form-group">
					<label for="phoneCode" class="col-sm-2 control-label">验证码</label>
					<div class="col-sm-5">
						<input type="text" class="form-control" id="phoneCode" maxlength="4"
							placeholder="请输入验证码" />
					</div>
					<div class="col-sm-2">
						<input type="button" class="btn btn-info btn-sm" id="phoneCodeBtn"
							value="免费获取验证码" />
					</div>
				</div>
			</form>
              </div>
          </div>
          </div>
      </div>
    </section>
<script src="res/js/user/personal/validUserInfo.js"></script>
