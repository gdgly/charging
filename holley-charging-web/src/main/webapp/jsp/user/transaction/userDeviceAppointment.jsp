<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
	<section class="content-header">
      	<h1>收益统计</h1> 
	    <ol class="breadcrumb">
		     <li>
	      	  	<a href="javascript:;" onclick="frameReload();">
	      	 		<i class="fa fa-home"></i> 首页
	      	 	</a>
	      	 </li>
	        <li><i class="fa fa-pie-chart"></i> 交易中心</li>
	        <li class="active">收益统计</li>
	    </ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
    	<div class="col-xs-12">
          <div class="box box-info" id="userDeviceAppointmentTable">
            <div class="box-header">
            	<h3 class="box-title">预约收益</h3>
              	<button id="searchBtn" type="button" style="margin-left: 10px;" class="btn btn-info pull-right">搜索</button>
               <input style="width: 20%;" placeholder="充电点名称" id="searchName" name="searchName" type='text' class="form-control pull-right" maxlength="30"/>
            	<span id="exportBtn" title="导出" class="fa fa-file-excel-o inFeeColor" style="cursor: pointer;margin-left: 10px;"></span>
            </div>
            <!-- /.box-header -->
            <div class="box-body height200">
              <table class="table table-bordered table-hover">
                <thead>
                <tr>
                  <th>充电点名称</th>
                  <th>地址</th>
                  <th>桩数量(个)</th>
                  <th>预约次数(次)</th>
                  <th>预约总金额(元)</th>
                   <th>评分</th>
                </tr>
                </thead>
                <tbody id="userDeviceAppointmentBody">
                </tbody>
              </table>
              </div>
               <%@include file="../../common/global/pagingtoolbar.jsp"%>
              </div>
              <!-- 充电统计 -->
           <div class="box box-info" id="userDeviceChargeTable">
            <div class="box-header">
            	<h3 class="box-title">充电收益</h3>
            		<button id="searchBtn2" style="margin-left: 10px;" type="button" class="btn btn-info pull-right">搜索</button>
					<input placeholder="充电点名称" maxlength="30" style="width: 20%;" id="searchName2" name="searchName2" type='text' class="form-control pull-right" /> 
              		<span id="exportBtn2" title="导出" class="fa fa-file-excel-o inFeeColor" style="cursor: pointer;margin-left: 10px;"></span>
            </div>
            <!-- /.box-header -->
            <div class="box-body height200">
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
               <%@include file="../../common/global/pagingtoolbar2.jsp"%>
              </div>
              <!-- 预约/充电桩记录 -->
           <div class="box box-info hide" id="userDevicePileTable">
            <div class="box-header">
            	<h3 class="box-title" id="stationName"></h3>
            	<input type="hidden" id="isAppOrCha" value=""/>
            	<input type="hidden" id="stationId" value="0"/>
            	<button id="comebackBtn" style="margin-left: 10px;" type="button" class="btn btn-info pull-right">返回</button>
            	<button id="searchBtn3" style="margin-left: 10px;" type="button" class="btn btn-info pull-right">搜索</button>
				<input style="width: 20%;" maxlength="30" placeholder="充电桩名称" id="searchName3" name="searchName3" type='text' class="form-control pull-right" />
				 <span id="exportBtn3" title="导出" class="fa fa-file-excel-o inFeeColor" style="cursor: pointer;margin-left: 10px;"></span>
            </div>
            <!-- /.box-header -->
            <div class="box-body height400">
              <table class="table table-bordered table-hover">
                <thead>
                <tr>
                  <th>桩名称</th>
                  <th>用户名</th>
                  <th>手机号码</th>
                  <th>缴费状态</th>
                  <th>缴费金额</th>
                  <th>是否入账</th>
                  <th class="text-center">添加时间</th>
                </tr>
                </thead>
                <tbody id="userDevicePileBody">
                </tbody>
              </table>
              </div>
               <%@include file="../../common/global/pagingtoolbar3.jsp"%>
              </div>
           </div>
      </div>
	</section>
	<script src="res/js/user/transaction/userDeviceAppointment.js"></script>
	<script src="res/js/user/transaction/userDeviceCharge2.js"></script>
	<script type="text/javascript">
     $("#comebackBtn").on("click",function(){
    	 $("#userDevicePileTable").addClass("hide");
 		$("#userDeviceChargeTable").removeClass("hide");
 		$("#userDeviceAppointmentTable").removeClass("hide");
     });
     $("#searchBtn3").on("click",function(){
    	 temp = $("#isAppOrCha").val();
    	 if("app" == temp){
    		 $("#currentPage3").val("1")
    		 initPileBody();
    	 }else if("cha" == temp){
    		 $("#currentPage3").val("1")
    		 initPileBody2();
    	 }else{
    		 showWarning("error!!");
    	 }
     });
     initPagingToolbar3(initPileBody,initPileBody2);
	</script>
