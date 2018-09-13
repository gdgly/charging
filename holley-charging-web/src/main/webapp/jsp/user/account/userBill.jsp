<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
<script src="res/js/bussiness/echart/echarts-all.js" type="text/javascript"></script>
	<section class="content-header">
      	<h1><s:if test="#request.webUser.usertype.value == 3">我的账单</s:if><s:else>账单管理</s:else></h1> 
	    <ol class="breadcrumb">
		     <li>
	      	  	<a href="javascript:;" onclick="frameReload();">
	      	 		<i class="fa fa-home"></i> 首页
	      	 	</a>
	      	 </li>
	        <li><i class="fa fa-dollar"></i> 账户中心</li>
	        <li class="active"><s:if test="#request.webUser.usertype.value == 3">我的账单</s:if><s:else>账单管理</s:else></li>
	    </ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
    	<div class="col-xs-12">
    	<!-- 圆饼图区域start -->
    	       <div class="box box-info" id="userBillChart">
            <div class="box-header with-border">
              	<h3 class="box-title">月账单</h3>
              <!-- 	<button id="goMoreReceiptPageBtn" type="button" style="margin-left: 10px;" class="btn btn-info pull-right btn-sm">更多</button> -->
               	<button id="goReceiptPageBtn" type="button" class="btn btn-info pull-right btn-sm">申请开票</button>
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
    	   <div class="box box-info" id="userConsumeTable">
            <div class="box-header">
              <h3 class="box-title">消费明细</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body height400">
              <table class="table table-bordered table-hover">
	                <tbody id="userConsumeBody">
	                </tbody>
              	</table>
              </div>
                 <%@include file="../../common/global/pagingtoolbar2.jsp"%>
              </div>
    	
          <div class="box box-info hide" id="userBillTable">
            <div class="box-header">
            	<button id="comeBackBtn" type="button" style="margin-left: 10px;" class="btn btn-info pull-right">返回</button>
               	<button id="goReceiptBtn" type="button" style="margin-left: 10px;" class="btn btn-info pull-right">开票</button>
               	<button id="searchBtn" type="button" style="margin-left: 10px;" class="btn btn-info pull-right">搜索</button>
               	<div class='input-group date pull-right' style="width: 20%;margin-left: 10px;" id='datepicker'>
						<input placeholder="请选择月份" id="searchDate" name="searchDate" type='text' class="form-control" /> 
						<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span> </span>
				</div>
             	<select id="isBill" name="isBill" style="width: 10%;" class="form-control pull-right">
             	 	 <option value="0">全部</option>
             	 	 <option value="1">未开票</option>
             	 	 <option value="2">已开票</option>
             	</select>
 			<span id="exportBtn" title="导出" class="fa fa-file-excel-o inFeeColor" style="cursor: pointer;"></span>
            </div>
            <!-- /.box-header -->
            <div class="box-body height400">
              <table class="table table-bordered table-hover">
                <thead>
                <tr>
                  <th><input id="allBillCheck" type='checkbox'/> 全选</th>
                  <th>预约金额（元）</th>
                  <th>充电金额（元）</th>
                  <th>停车金额（元）</th>
                  <th>服务金额（元）</th>
                   <th>消费总金额（元）</th>
                  <th>结账周期</th>
                  <th>状态</th>
                </tr>
                </thead>
                <tbody id="userBillBody">
                </tbody>
              </table>
              </div>
              <%@include file="../../common/global/pagingtoolbar.jsp"%>
              </div>
           </div>
      </div>
	</section>
	
			<!-- 模态框（Modal） -->
	<div class="modal fade" id="userReiptModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">开票</h4>
				</div>
				<div class="modal-body">
			<form role="form" id="userReiptForm">
              <div class="box-body">
                <div class="form-group">
                 <label for="money">开票月份</label>
	                <p class="" style="color: #00c0ef;font-size: 20px;" id="time" name="time"></p>
	                <p style="display: none;" id="billsId"></p>
                </div>
               	<div class="form-group">
                  <label for="money">开票金额</label>
	                <p style="color: #dd4b39;" class="form-control" id="money" name="money">
                	</p>
                </div>
                <div class="form-group">
                  <label for="billHead">开票抬头</label>
                  <input type="text" class="form-control" id="billHead" name="billHead" placeholder="请填写发票抬头（公司/个人）">
                </div>
                <div class="form-group">
                  <label for="recipient">收件人</label>
                  <input type="text" class="form-control" id="recipient" name="recipient" placeholder="请填写真实姓名">
                </div>
                <div class="form-group">
                  <label for="phone">联系电话</label>
                  <input type="text" class="form-control" id="phone" name="phone" placeholder="请填写11位手机号码">
                </div>
                <div class="form-group">
                  <label for="address">收件地址</label>
                  <input type="text" class="form-control" id="address" name="address" placeholder="请填写地址">
                </div>
              </div>
              <!-- /.box-body -->
            </form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="saveBtn" type="button" class="btn btn-primary">提交</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal -->
	</div>
	<script type="text/javascript">
	var userBillCharts = ${userBillCharts}
</script>
<script src="res/js/user/account/userBill.js"></script>

