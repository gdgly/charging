<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
	<section class="content-header">
      	<h1>用户管理</h1> 
	    <ol class="breadcrumb">
		     <li>
	      	  	<a href="javascript:;" onclick="frameReload();">
	      	 		<i class="fa fa-home"></i> 首页
	      	 	</a>
	      	 </li>
	        <li><i class="fa fa-dollar"></i> 用户中心</li>
	        <li class="active"> 用户管理</li>
	    </ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
    	<div class="col-xs-12">
          <div class="box box-info" id="groupUserPage">
            <div class="box-header">
              	<button id="addGroupUserBtn" type="button" style="margin-left: 10px;" class="btn btn-info pull-right">添加用户</button>
              	<button id="searchBtn" type="button" style="margin-left: 10px;" class="btn btn-info pull-right">搜索</button>
			   	<input type="text" class="form-control pull-right" style="width: 15%;" id="userName" placeholder="用户名">
               	<span id="exportBtn" title="导出" class="fa fa-file-excel-o inFeeColor" style="cursor: pointer;"></span>
            </div>
            <!-- /.box-header -->
            <div class="box-body height400">
              <table class="table table-bordered table-hover">
                <thead>
                <tr>
                  <th>用户名</th>
                  <th>手机号码</th>
                  <th>用户状态</th>
                  <th>添加时间</th>
                  <th>操作</th>
                </tr>
                </thead>
                <tbody id="groupUserBody">
                </tbody>
              </table>
              </div>
                <%@include file="../../common/global/pagingtoolbar.jsp"%>
              </div>
              
              <!-- 添加/修改用户 start-->
               <div class="box box-info hide" id="addOrEditGroupUserPage">
               		<div class="box-header">
               		<h3 class="box-title" id="addOrEditGroupUserTitle">添加用户</h3>
		              	<button id="backBtn" type="button" style="margin-left: 10px;" class="btn btn-info pull-right">返回</button>
		              	<button disabled="disabled" id="saveGroupUserBtn" type="button" style="margin-left: 10px;" class="btn btn-info pull-right">保存</button>
            		</div>
            		<div class="box-body">
            		<form class="form-horizontal" id="addOrEditGroupUserForm">
            		  <div class="form-group">
                  <label for="groupUserName" class="col-sm-2 control-label">用户名：</label>
                  <div class="col-sm-3">
                    <input type="text" class="form-control" id="groupUserName"  placeholder="用户名" value="" maxlength="20">
                    <input type="hidden" class="form-control" id="groupUserId"  value="">
                  </div> 
                   <label for="phone" class="col-sm-2 control-label">手机：</label>
                    <div class="col-sm-3">
                    <input type="text" class="form-control" id="groupUserPhone" placeholder="手机号码" value="" maxlength="20">
                  </div>
                </div>
                <div class="form-group">
                   <div class="col-sm-8">
                   <p><b>说明：</b></p>
                   <p style="color:#CD8500;margin-left: 35px;">1.添加的子账户（用户名，手机号码）必须唯一</p>
                   <p style="color:#CD8500;margin-left: 35px;">2.添加的子账户密码默认（${defaultPwd}）</p>
                   <p style="color:#CD8500;margin-left: 35px;">3.最多添加${maxGroupUser}个子账户</p>
                   </div>
                </div>
            		</form>
            		</div>
               </div>
              <!-- 添加/修改用户end -->
           </div>
      </div>
	</section>
<!-- <script type="text/javascript">
var userType = "${webUser.usertype.value}";
</script> -->
<script src="res/js/user/groupuser/userManagement.js"></script>

