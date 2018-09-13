<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <h1><s:if test="#request.webUser.roleType == 3">我的钱包</s:if><s:else>账户信息</s:else></h1> 
	    <ol class="breadcrumb">
		     <li>
	      	  	<a href="javascript:;" onclick="frameReload();">
	      	 		<i class="fa fa-home"></i> 首页
	      	 	</a>
	      	 </li>
	        <li><i class="fa fa-dollar"></i> 账户中心</li>
	        <li class="active">  <s:if test="#request.webUser.roleType == 3">我的钱包</s:if><s:else>账户信息</s:else></li>
	    </ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
       <div class="col-xs-12">
     		<div class="box box-info">
	        <div class="box-header with-border">
	          <h3 class="box-title"><s:if test="#request.webUser.roleType == 3">我的钱包</s:if><s:else>账户信息</s:else></h3>
	          <!-- <button id="cashBtn" type="button" style="margin-left: 10px;" onclick="href('userTransaction/initUserCash.action');" class="btn btn-info pull-right btn-sm">提现</button> -->
	          <button id="rechargeBtn" type="button" onclick="href('userAccount/initUserRecharge.action');" class="btn btn-info pull-right btn-sm">充值</button>
	        </div>
	        <div class="box-body">
	         	<p>余额（元）：<span class="feeColor">${usableMoney}</span></p>
	         	<s:if test="#request.webUser.roleType == 3">
	         	<p><span>支出（元）：<span class="outFeeColor">-${totalOut}</span></span><span style="padding-left:20%;">收入（元）：<span style="color:#A2CD5A;">+${totalIn}</span></span></p>
	         	</s:if>
	        </div>
      	</div>
      	<!-- 收支明细 -->
      	   <div class="box box-info">
            <div class="box-header">
               <h3 class="box-title">交易明细</h3>
               <button id="searchBtn" type="button" style="margin-left: 10px;" class="btn btn-info pull-right">搜索</button>
              <div class='input-group date pull-right' style="width: 20%;margin-left: 10px;" id='datepicker'>
								<input placeholder="请选择月份" id="searchDate" name="searchDate" type='text' class="form-control" /> 
								<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span> </span>
						</div>
             	<select id="dealType" name="dealType" style="width: 10%;" class="form-control pull-right">
             	 	 <option value="0">全部</option>
             	 	 <option value="1">充值</option>
             	 	 <option value="2">充电</option>
             	 	 <option value="3">预约</option>
             	 	 <!-- <option value="4">提现</option> -->
             	 	</select>
            </div>
            <!-- /.box-header -->
            <div class="box-body height400">
              <table class="table table-bordered table-hover">
                <thead>
                <tr>
                 <th>名称</th>
                 <th>交易类型</th>
                 <th>交易金额（元）</th>
                 <th>交易时间</th>
                </tr>
                </thead>
                <tbody id="userDealBody">
                </tbody>
              </table>
              </div>
              <%@include file="../../common/global/pagingtoolbar.jsp"%>
             </div>
          </div>
      </div>
    </section>
 	<script src="res/js/user/account/userAccount.js"></script>
