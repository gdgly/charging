<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
	<section class="content-header">
      	<h1>充值</h1> 
	    <ol class="breadcrumb">
		     <li>
	      	  	<a href="javascript:;" onclick="frameReload();">
	      	 		<i class="fa fa-home"></i> 首页
	      	 	</a>
	      	 </li>
	        <li><i class="fa fa-dollar"></i> 账户中心</li>
	        <li class="active">充值</li>
	    </ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
    	<div class="col-xs-12">
    	 <div class="box box-info">
            <div class="box-header with-border">
              <h3 class="box-title">在线充值</h3>
               <button style="margin-left: 10px;" type="button" onclick="href('userAccount/initUserAccount.action');" class="btn btn-info pull-right btn-sm">返回</button>
               <button id="doRechargeBtn" type="button" class="btn btn-info pull-right btn-sm">提交</button>
            </div>
            <!-- /.box-header -->
            <!-- form start -->
              <div class="box-body">
                <form class="form-horizontal" role="form" action="userAccount/aliPay.action" id="rechargeForm" method="post">
			<%-- 	<div class="form-group">
					<label for="totalMoney" class="col-sm-2 control-label">账户总额</label>
					<div class="col-sm-8">
						<p id="totalMoney"><span style="color:red;font-size:17px;">${busAccount.totalMoney}</span> 元</p>
					</div>
				</div> --%>
				<div class="form-group">
					<label for="usableMoney" class="col-sm-2 control-label">账户余额</label>
					<div class="col-sm-8">
					<p id="usableMoney" style="margin-top: 4px;"><span style="color:red;font-size:17px;">${usableMoney}</span> 元</p>
					</div>
				</div>
				<div class="form-group">
					<label for="rechargeWay" class="col-sm-2 control-label">充值方式</label>
					<div class="col-sm-2">
					<select id="rechargeWay" name="rechargeWay" class="form-control">
					<option value="2">支付宝</option>
					<option value="3">微信</option>
					</select>
					</div>
				</div>
				<div class="form-group">
					<label for="rechargeMoney" class="col-sm-2 control-label">充值金额</label>
					<div class="col-sm-5">
						<input placeholder="充值金额保留2位小数" type="text" class="form-control" id="rechargeMoney" name="rechargeMoney"/>
					</div>
				</div>
			</form>
              </div>
              <!-- /.box-body -->
          </div>
    	
          <div class="box box-info">
            <div class="box-header">
						<h3 class="box-title">充值记录</h3>
						<button style="margin-left: 10px;" id="searchBtn" type="button" class="btn btn-info pull-right">搜索</button>
	                    <div class='input-group date pull-right' id='datepicker' style="width: 20%;">
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
                  <th>充值金额（元）</th>
                  <th>充值方式</th>
                  <th>充值状态</th>
                  <th>交易号</th>
                  <th>充值时间</th>
                </tr>
                </thead>
                <tbody id="userRechargeBody">
                </tbody>
              </table>
              </div>
               <%@include file="../../common/global/pagingtoolbar.jsp"%>
              </div>
           </div>
      </div>
	</section>
 <script src="res/js/user/account/userRecharge.js"></script>

