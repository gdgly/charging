<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
<script src="res/js/bussiness/echart/echarts-all.js" type="text/javascript"></script>
	<section class="content-header">
      	<h1>收益账单</h1> 
	    <ol class="breadcrumb">
		     <li>
	      	  	<a href="javascript:;" onclick="frameReload();">
	      	 		<i class="fa fa-home"></i> 首页
	      	 	</a>
	      	 </li>
	        <li><i class="fa fa-dollar"></i> 账户中心</li>
	        <li class="active">收益账单</li>
	    </ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
    	<div class="col-xs-12">
    	<!-- 圆饼图区域start -->
    	       <div class="box box-info" id="userDeviceBillChart">
            <div class="box-header with-border">
              <h3 class="box-title">月账单</h3>
              <button id="goMoreDeviceBillPageBtn" type="button" style="margin-left: 10px;" class="btn btn-info pull-right btn-sm">更多</button>
            </div>
              <div class="box-body">
              
            	<div class="col-xs-4">
                	<div id="main1" style="height: 200px;"></div>
                </div>
                <div class="col-xs-4">
                	<div id="main2" style="height: 200px;"></div>
                </div>
                <div class="col-xs-4">
                	<div id="main3" style="height: 200px;"></div>
                </div>
              </div>
          </div>
    	<!-- 圆饼图区域end -->
    	<!-- 消费明细 -->
    	   <div class="box box-info" id="userIncomeTable">
            <div class="box-header">
              <h3 class="box-title">收入明细</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body height400">
              <table class="table table-bordered table-hover">
         <!--        <thead>
                <tr>
                  <th>充电点名称</th>
                  <th>地址</th>
                  <th>评分</th>
                  <th>快充桩（个）</th>
                  <th>慢充桩（个）</th>
                  <th>操作</th>
                </tr>
                </thead> -->
                <tbody id="userIncomeBody">
                <!-- <tr>
                <td>华立科技</td>
                  <td>留下五常大道222号</td>
                    <td>5</td>
                      <td>3</td>
                        <td>3</td>
                          <td>详细</td>
                </tr> -->
                </tbody>
              </table>
              </div>
                 <%@include file="../../common/global/pagingtoolbar2.jsp"%>
              </div>
    	
    	
          <div class="box box-info hide" id="userDeviceBillTable">
            <div class="box-header">
             <h3 class="box-title">月账单</h3>
              <button id="comeBackBtn" type="button" style="margin-left: 10px;" class="pull-right btn btn-info">返回</button>
              <button id="searchBtn" type="button" style="margin-left: 10px;" class="btn btn-info pull-right ">搜索</button>
	                    <div class='input-group date pull-right' style="width: 20%" id='datepicker'>
								<input placeholder="请选择月份" id="searchDate" name="searchDate" type='text' class="form-control" /> 
								<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span> </span>
						</div>
              
             <span id="exportBtn" title="导出" class="fa fa-file-excel-o inFeeColor" style="cursor: pointer;margin-left: 10px;"></span>
            </div>
            <!-- /.box-header -->
            <div class="box-body height400">
              <table class="table table-bordered table-hover">
                <thead>
                <tr>
                  <th>预约金额（元）</th>
                  <th>充电金额（元）</th>
                  <th>停车金额（元）</th>
                  <th>服务金额（元）</th>
                  <th>收入总金额（元）</th>
                  <th>结账周期</th>
                </tr>
                </thead>
                <tbody id="userDeviceBillBody">
                </tbody>
              </table>
              </div>
              <%@include file="../../common/global/pagingtoolbar.jsp"%>
              </div>
           </div>
      </div>
	</section>
	
	
	<script type="text/javascript">
	var userDeviceBillCharts = ${userDeviceBillCharts}
</script>
<script src="res/js/user/transaction/userDeviceBill.js"></script>

