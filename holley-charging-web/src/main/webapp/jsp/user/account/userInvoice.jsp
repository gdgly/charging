<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
	<section class="content-header">
      	<h1>我的发票</h1> 
	    <ol class="breadcrumb">
		     <li>
	      	  	<a href="javascript:;" onclick="frameReload();">
	      	 		<i class="fa fa-home"></i> 首页
	      	 	</a>
	      	 </li>
	        <li><i class="fa fa-dollar"></i> 账户中心</li>
	        <li class="active">我的发票</li>
	    </ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
    	<div class="col-xs-12">
          <div class="box box-info">
            <div class="box-header">
	            	<button id="searchBtn" type="button" style="margin-left: 10px;" class="btn btn-info pull-right">搜索</button>
	                <div class='input-group date pull-right' id='datepicker' style="width: 20%;">
								<input placeholder="请选择开票月份" id="searchDate" name="searchDate" type='text' class="form-control" /> 
								<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span> </span>
					</div>
              		<span id="exportBtn" title="导出" class="fa fa-file-excel-o inFeeColor" style="cursor: pointer;"></span>
            </div>
            <!-- /.box-header -->
            <div class="box-body height400">
              <table class="table table-bordered table-hover">
                <thead>
                <tr>
                  <th>开票月份</th>
                  <th>开票金额（元）</th>
                  <th>开票抬头</th>
                  <th>收件人</th>
                  <th>联系电话</th>
                  <th>收件地址</th>
                  <th>快递单号</th>
                  <th>开票状态</th>
                  <th>审核备注</th>
                  <th>开票时间</th>
                </tr>
                </thead>
                <tbody id="userInvoiceBody">
                </tbody>
              </table>
              </div>
               <%@include file="../../common/global/pagingtoolbar.jsp"%>
              </div>
           </div>
      </div>
	</section>
 	<script src="res/js/user/account/userInvoice.js"></script>

