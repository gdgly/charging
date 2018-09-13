<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
    <!-- Content Header (Page header) -->
    <section class="content-header">
       <h1>修改邮箱</h1> 
      	<ol class="breadcrumb">
      	 <li>
      	  	<a href="javascript:;" onclick="frameReload();">
      	 		<i class="fa fa-home"></i> 首页
      	 	</a>
      	 </li>
	        <li>
	        <i class="fa fa-user"></i> 个人中心
	        </li>
	        <li class="active">修改邮箱</li>
      	</ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
       <div class="col-xs-12">
        <div class="box box-info">
            <div class="box-header with-border">
             <!--  <h3 class="box-title">您正在使用手机验证码验证身份，请完成以下操作</h3> -->
             <button type="button" onclick="href('userAdmin/initUserInfo.action');" style="margin-left: 10px;" class="btn btn-info pull-right btn-sm">返回</button>
               <button id="changeEamilBtn" type="button" class="btn btn-info pull-right btn-sm">确定</button>
            </div>
            <!-- /.box-header -->
            <!-- form start -->
              <div class="box-body">
           			<form class="form-horizontal" role="form" id="changeEmail">
				<div class="form-group">
					<label for="oldPwd" class="col-sm-2 control-label">登录名</label>
					<div class="col-sm-5">
						<input type="text" class="form-control" id="username" value="${currentUser.userName}" disabled="disabled" /> 
						<input type="hidden" id="actionType" name="actionType" value="${actionType}" />
					</div>
				</div>
				<div class="form-group">
					<label for="email" class="col-sm-2 control-label">邮箱</label>
					<div class="col-sm-5">
						<input type="text" class="form-control" id="email" placeholder="请输入邮箱" maxlength="30"/>
					</div>
				</div>
				<div class="form-group">
					<label for="emailCode" class="col-sm-2 control-label">验证码</label>
					<div class="col-sm-5">
						<input type="text" class="form-control" id="emailCode" maxlength="4"/>
					</div>
					<div class="col-sm-1">
						<input type="button" class="btn btn-info btn-sm" id="emailCodeBtn" value="免费获取验证码" />
					</div>
				</div>
			</form>
              </div>
          </div>
          </div>
      </div>
    </section>
<script src="res/js/user/personal/changeEmail.js"></script>
