<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
    <!-- Content Header (Page header) -->
    <section class="content-header">
       <h1>我的设备</h1> 
      <ol class="breadcrumb">
       	<li>
      	  	<a href="javascript:;" onclick="frameReload();">
      	 		<i class="fa fa-home"></i> 首页
      	 	</a>
      	 </li>
        <li><i class="fa fa-laptop"></i> 设备管理</li>
        <li class="active">我的设备</li>
      </ol>
    </section>
    <!-- Main content -->
<section class="content">
  <div class="row">
   <div class="col-xs-12">
        <div class="box box-info">
           <div class="box-header">
	            	<span id="goBackBtn" title="返回" class="glyphicon glyphicon-menu-left" style="color: 337ab7;font-size: 24px;cursor: pointer;"></span><h3 class="box-title">待审充电点</h3>
	            	<!-- <button id="goBackBtn" type="button" style="margin-left: 10px;" class="btn btn-info pull-right">返回</button> -->
	            	<button id="searchBtn" type="button" style="margin-left: 10px;" class="btn btn-info pull-right">搜索</button>
					<input placeholder="充电点名称" style="width: 20%;" id="searchName" name="searchName" type='text' class="form-control pull-right" /> 
            </div>
            	<div class="box-body height200">
             	 <table class="table table-bordered table-hover">
             	 <thead>
	                <tr>
	                  <th>充电点名称</th>
	                  <th>地址</th>
	                  <th>开放日</th>
	                  <th>开放时间</th>
	                  <th>操作类型</th>
	                  <th>审核状态</th>
	                  <th>操作</th>
	                </tr>
                </thead>
                <tbody id="userUnValidStationBody">
                </tbody>
              </table>
              </div>
               <%@include file="../../common/global/pagingtoolbar.jsp"%>
              </div>
            <div class="box box-info">
         	<div class="box-header">
	            	<h3 class="box-title">待审充电桩</h3>
	            	<button id="searchBtn2" type="button" style="margin-left: 10px;" class="btn btn-info pull-right">搜索</button>
					<input placeholder="充电桩名称" style="width: 20%;" id="searchName2" name="searchName2" type='text' class="form-control pull-right" /> 
            </div>
            	<div class="box-body height200">
             	 <table class="table table-bordered table-hover">
             	 <thead>
	                <tr>
	                  <th>充电桩名称</th>
	                  <th>桩编号</th>
	                  <th>充电类型</th>
	                  <th>充电方式</th>
	                  <th>通讯协议</th>
	                  <th>通讯地址</th>
	                  <th>支持预约</th>
	                  <th>支付方式</th>
	                  <th>操作类型</th>
	                  <th>审核状态</th>
	                  <th>操作</th>
	                </tr>
                </thead>
                <tbody id="userUnValidPileBody">
                </tbody>
              </table>
              </div>
               <%@include file="../../common/global/pagingtoolbar2.jsp"%>
              </div>
              
          </div>
          </div>
   
</section>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=A4749739227af1618f7b0d1b588c0e85"></script>
<script type="text/javascript">
$("#goBackBtn").on("click",function(){
	href("userDevice/initUserDevice.action");
});
</script>
<script src="res/js/user/device/userUnDevice.js"></script>
<script type="text/javascript">

</script>
