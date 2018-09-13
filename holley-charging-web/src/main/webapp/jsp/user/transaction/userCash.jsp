<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
	<section class="content-header">
      	<h1>提现</h1> 
	    <ol class="breadcrumb">
		     <li>
	      	  	<a href="javascript:;" onclick="frameReload();">
	      	 		<i class="fa fa-home"></i> 首页
	      	 	</a>
	      	 </li>
	        <li><i class="fa fa-pie-chart"></i> 交易中心</li>
	        <li class="active">提现</li>
	    </ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
    	<div class="col-xs-12">
    	<!-- 提现申请start -->
    	   <div class="box box-info">
            <div class="box-header with-border">
              <h3 class="box-title">提现申请</h3>
               <button type="button" style="margin-left: 10px;" onclick="href('userAccount/initUserAccount.action');" class="btn btn-info pull-right btn-sm">返回</button>
               <button id="doAccountCashBtn" type="button" class="btn btn-info pull-right btn-sm">提交</button>
            </div>
            <!-- /.box-header -->
            <!-- form start -->
              <div class="box-body">
                <form class="form-horizontal" role="form" id="cashForm">
			<%-- 	<div class="form-group">
					<label for="totalMoney" class="col-sm-2 control-label">账户总额</label>
					<div class="col-sm-8">
						<p id="totalMoney"><span style="color:red;font-size:17px;">${busAccount.totalMoney}</span> 元</p>
					</div>
				</div> --%>
				<div class="form-group">
					<label for="usableMoney" class="col-sm-2 control-label">账户余额</label>
					<div class="col-sm-8">
					<p id="totalMoney" style="margin-top: 4px;"><span style="color:red;font-size:17px;">${busAccount.usableMoney}</span> 元</p>
					</div>
				</div>
<%-- 				<div class="form-group">
					<label for="freezeMoney" class="col-sm-2 control-label">冻结金额</label>
					<div class="col-sm-8">
						<input disabled="disabled" class="form-control"
							placeholder="${busAccount.freezeMoney}元" id="freezeMoney" />
					</div>
				</div> --%>
				<div class="form-group">
					<label for="cashWay" class="col-sm-2 control-label">提现方式</label>
					<div class="col-sm-2">
					<select id="cashWay" class="form-control">
					<option value="1">支付宝</option>
					<option value="3">微信</option>
					</select>
					</div>
				</div>
				<div class="form-group">
					<label for="cashMoney" class="col-sm-2 control-label">提现金额</label>
					<div class="col-sm-5">
						<input placeholder="提现金额保留2位小数" type="text" class="form-control" id="cashMoney" value="" />

					</div>
				</div>
				<div class="form-group">
					<label for="accountInfo" class="col-sm-2 control-label">提现账号</label>
					<div class="col-sm-5">
					<input placeholder="请输入提现账号" type="text" class="form-control" id="accountInfo" maxlength="30"/>
					</div>
				</div>
			</form>
              </div>
              <!-- /.box-body -->
            <!--   <div class="box-footer">
                <button id="saveUserInfoBtn" type="button" class="btn btn-info pull-right">保存</button>
              </div> -->
              <!-- /.box-footer -->
          </div>
    	<!-- 提现申请end -->
    	<!-- 提现记录start -->
          <div class="box box-info">
            <div class="box-header">
             <h3 class="box-title">提现记录</h3>
              <button id="searchBtn" type="button" style="margin-left: 10px;" class="btn btn-info pull-right">搜索</button>
                    <div class='input-group date pull-right' id='datepicker' style="width: 20%;">
							<input placeholder="请选择月份" id="searchDate" name="searchDate" type='text' class="form-control" /> 
							<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span> </span>
					</div>
              <span id="exportBtn" title="导出" class="fa fa-file-excel-o inFeeColor" style="cursor: pointer;margin-left: 10px;"></span>
            </div>
            <!-- /.box-header -->
            <div class="box-body" style="height: 380px;">
              <table class="table table-bordered table-hover">
                <thead>
                <tr>
                  <th>提现金额(元)</th>
                  <th>账户信息</th>
                  <th>提现方式</th>
                  <th>审核状态</th>
                  <th>审核备注</th>
                  <th>提现时间</th>
                </tr>
                </thead>
                <tbody id="userCashBody">
                </tbody>
              </table>
              </div>
               <%@include file="../../common/global/pagingtoolbar.jsp"%>
              </div>
           </div>
      </div>
	</section>
 <script src="res/js/user/transaction/userCash.js"></script>

