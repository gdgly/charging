<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
    <section class="content-header">
   		<h1>实名认证</h1> 
      <ol class="breadcrumb">
         <li>
      	  	<a href="javascript:;" onclick="frameReload();">
      	 		<i class="fa fa-home"></i> 首页
      	 	</a>
      	 </li>
        <li><i class="fa fa-user"></i> 个人中心</li>
        <li class="active">实名认证</li>
      </ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
      <s:if test="#request.webUser.realStatus == 2">
                	已经通过实名认证</br>
                	真实姓名：<s:property value="#request.busUserInfo.realNameDesc"/></br>
       				身份证号：<s:property value="#request.busUserInfo.cardNoDesc"/>
      </s:if>
      <s:elseif test="#request.userRealInfo != null">
     正在审核中。。。
      </s:elseif>
      <s:else>
          <div class="col-xs-12">
      <div class="box box-info">
            <div class="box-header with-border">
              <h3 class="box-title">认证资料</h3>
               	<button id="goBackBtn" type="button" class="btn btn-info btn-sm pull-right">返回</button>
           		<button id="saveUserInfoBtn" type="button" class="btn btn-info btn-sm pull-right" style="margin-right: 10px;">保存</button>
            </div>
            <!-- /.box-header -->
            <!-- form start -->
            <form class="form-horizontal" id="userRealForm">
              <div class="box-body">
              <s:if test="#request.webUser.realStatus != 2">
                <div class="form-group">
                  <label for="realName" class="col-sm-offset-2 col-sm-2 control-label">真实姓名</label>
                  <div class="col-sm-4">
                    <input type="text" class="form-control" id="realName" name="realName" placeholder="请输入真实姓名" value="" maxlength="10">
                  </div> 
                </div>
                 <div class="form-group">
                 <label for="cardNum" class="col-sm-offset-2 col-sm-2 control-label">身份证号</label>
                  <div class="col-sm-4">
         			 <input type="text" class="form-control" id="cardNum" name="cardNum" placeholder="请输入身份证号" value="" maxlength="30">
                  </div> 
                 </div>
                <div class="form-group">
 				<label for="showCarImg" class="col-sm-offset-2 col-sm-2 control-label">身份证照片</label>
                  <div class="col-sm-4">
          			<img id="showCarImg" alt="Image preview" class="img-thumbnail" style="height: 15%; width: 46%;" src="${imgUrl}data/stationImg/stationDefault.jpg" /> 
					   <input type="file" name="pic" id="addCarImg" />            
                  </div> 
					  
                </div>
               </s:if>
              </div>
            </form>
          </div>
          </div>
      </s:else>
      </div>
    </section>
<script src="res/js/user/personal/userReal.js"></script>