<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
    <!-- Content Header (Page header) -->
    <section class="content-header">
       <h1><s:if test="#request.webUser.usertype.value == 3">我的消息</s:if><s:else>消息中心</s:else></h1> 
      <ol class="breadcrumb">
         <li>
      	  	<a href="javascript:;" onclick="frameReload();">
      	 		<i class="fa fa-home"></i> 首页
      	 	</a>
      	 </li>
        <li><i class="fa fa-user"></i> <s:if test="#request.webUser.usertype.value == 3">个人中心</s:if><s:else>集团中心</s:else></li>
        <li class="active"><s:if test="#request.webUser.usertype.value == 3">我的消息</s:if><s:else>消息中心</s:else></li>
      </ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
      <div class="col-xs-12">
 <div class="nav-tabs-custom" id="msgDiv">
            <ul class="nav nav-tabs" id="searchMsgBtn">
              <li class="active" id="allMsg"><a href="#" data-toggle="tab">全部</a></li>
              <li id="unMsg"><a href="#" data-toggle="tab">未读</a></li>
              <li id="yesMsg"><a href="#" data-toggle="tab">已读</a></li>
              <input type="hidden" id="isRead" value="0"/>
            </ul>
            <div class="tab-content">
              <div class="active tab-pane">
            	<div class="box-body height400">
             	 <table class="table table-bordered table-hover">
                <tbody id="userMsgBody">
                </tbody>
              </table>
              </div>
               <%@include file="../../common/global/pagingtoolbar.jsp"%>
              </div>
              <!-- /.tab-pane -->
            </div>
            <!-- /.tab-content -->
          </div>
          
          <!-- ====================DETAIL_MSG============================ -->
          
	          <div class="box box-info hide" id="msgDetailDiv">
		            <div class="box-header with-border">
		              <h3 class="box-title" id="msgTitle">51充电</h3>
		            </div>
	        <form class="form-horizontal">
              <div class="box-body">
                <div class="form-group">
                  <div class="col-sm-12" id="msgContent" style="line-height: 30px;">
                  </div> 
                </div>
                 <div class="form-group">
                  	<div class="col-sm-12 text-right" id="msgAddTime">
					</div>
                 </div>
              </div>
              <!-- /.box-body -->
              <div class="box-footer">
                <button id="comebackMsgBtn" type="button" class="btn btn-info pull-right">返回</button>
              </div>
              <!-- /.box-footer -->
            </form>
	          </div>
	      <!-- ====================DETAIL_MSG============================ -->
         </div>
      </div>
    </section>
<script src="res/js/user/personal/userMsg.js"></script>
