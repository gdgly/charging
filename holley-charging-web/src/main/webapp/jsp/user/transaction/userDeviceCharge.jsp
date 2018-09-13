<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
	<section class="content-header">
      	<h1>充电统计</h1> 
	    <ol class="breadcrumb">
		     <li>
	      	  	<a href="javascript:;" onclick="frameReload();">
	      	 		<i class="fa fa-home"></i> 首页
	      	 	</a>
	      	 </li>
	        <li><i class="fa fa-pie-chart"></i> 交易中心</li>
	        <li class="active">充电统计</li>
	    </ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
    	<div class="col-xs-12">
          <div class="box box-info">
            <div class="box-header">
               	<div class="col-xs-offset-8 col-xs-3">
					<input placeholder="充电点名称" id="searchName" name="searchName" type='text' class="form-control" /> 
            	</div>
             <div class="col-xs-1">
              		<button id="searchBtn" type="button" class="btn btn-info pull-left">搜索</button>
             </div>
            </div>
            <!-- /.box-header -->
            <div class="box-body height400">
              <table class="table table-bordered table-hover">
                <thead>
                <tr>
                  <th>充电点名称</th>
                  <th>地址</th>
                  <th>桩数量(个)</th>
                  <th>充电次数(次)</th>
                  <th>充电总费用(元)</th>
                  <th>评分</th>
                </tr>
                </thead>
                <tbody id="userDeviceChargeBody">
                </tbody>
              </table>
              </div>
               <%@include file="../../common/global/pagingtoolbar.jsp"%>
              </div>
           </div>
      </div>
	</section>
 <script src="res/js/user/transaction/userDeviceCharge.js"></script>

