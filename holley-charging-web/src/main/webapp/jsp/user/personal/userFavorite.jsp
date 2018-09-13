<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
	<section class="content-header">
      	<h1>我的收藏</h1> 
	    <ol class="breadcrumb">
		     <li>
	      	  	<a href="javascript:;" onclick="frameReload();">
	      	 		<i class="fa fa-home"></i> 首页
	      	 	</a>
	      	 </li>
	        <li><i class="fa fa-user"></i> 个人中心</li>
	        <li class="active">我的收藏</li>
	    </ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
    	<div class="col-xs-12">
          <div class="box box-info">
            <div class="box-header">
              <h3 class="box-title">收藏记录</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body height400">
              <table class="table table-bordered table-hover">
                <thead>
                <tr>
                  <th>充电点名称</th>
                  <th>地址</th>
                  <th>评分</th>
                  <th>快充桩（个）</th>
                  <th>慢充桩（个）</th>
                  <th>操作</th>
                </tr>
                </thead>
                <tbody id="userFavoriteBody">
                </tbody>
              </table>
              </div>
                 <%@include file="../../common/global/pagingtoolbar.jsp"%>
              </div>
           </div>
      </div>
	</section>
<script src="res/js/user/personal/userFavorite.js"></script>

