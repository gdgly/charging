<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
	<section class="content-header">
      	<h1>设备告警</h1> 
	    <ol class="breadcrumb">
		     <li>
	      	  	<a href="javascript:;" onclick="frameReload();">
	      	 		<i class="fa fa-home"></i> 首页
	      	 	</a>
	      	 </li>
	        <li><i class="fa fa-laptop"></i> 我的设备</li>
	        <li class="active">设备告警</li>
	    </ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
    	<div class="col-xs-12">
          <div class="box box-info">
            <div class="box-header">
             <button id="searchBtn" style="margin-left: 10px;" type="button" class="btn btn-info pull-right">搜索</button>
                    <div class='input-group date pull-right' id='datepicker' style="width: 20%;">
							<input placeholder="请选择月份" id="searchDate" name="searchDate" type='text' class="form-control" /> 
							<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span> </span>
					</div>
						<span id="exportBtn" title="导出" class="fa fa-file-excel-o inFeeColor" style="cursor: pointer;"></span>
            </div>
            <!-- /.box-header -->
            <div class="box-body height400">
              <table class="table table-bordered table-hover">
                <thead>
                <tr>
                  <th>充电点名称</th>
                  <th>桩名称</th>
                  <th>地址</th>
                  <th>事件描述</th>
                  <th>告警时间</th>
                </tr>
                </thead>
                <tbody id="userDeviceAlarmBody">
                </tbody>
              </table>
              </div>
                <%@include file="../../common/global/pagingtoolbar.jsp"%>
              </div>
           </div>
      </div>
	</section>
<script src="res/js/user/device/userDeviceAlarm.js"></script>

