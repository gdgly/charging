<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
	<section class="content-header">
      	<h1><s:if test="#request.webUser.usertype.value == 3">我的充电</s:if><s:else>充电记录</s:else></h1> 
	    <ol class="breadcrumb">
		     <li>
	      	  	<a href="javascript:;" onclick="frameReload();">
	      	 		<i class="fa fa-home"></i> 首页
	      	 	</a>
	      	 </li>
	        <li><i class="fa fa-dollar"></i> 账户中心</li>
	        <li class="active"><s:if test="#request.webUser.usertype.value == 3">我的充电</s:if><s:else>充电记录</s:else></li>
	    </ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
    	<div class="col-xs-12">
          <div class="box box-info">
            <div class="box-header">
            <button id="searchBtn" type="button" style="margin-left: 10px;" class="btn btn-info pull-right">搜索</button>
                    <div class='input-group date pull-right' id='datepicker' style="width: 20%;margin-left: 10px;">
							<input placeholder="请选择月份" id="searchDate" name="searchDate" type='text' class="form-control" /> 
							<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span> </span>
					</div>
				<s:if test="#request.webUser.usertype.value == 5">
					 <input type="text" class="form-control pull-right" style="width: 10%;" id="userName" placeholder="用户名" maxlength="20"/>
				</s:if>
              <span id="exportBtn" title="导出" class="fa fa-file-excel-o inFeeColor" style="cursor: pointer;"></span>
            </div>
            <!-- /.box-header -->
            <div class="box-body height400">
              <table class="table table-bordered table-hover">
                <thead>
                <tr>
                <s:if test="#request.webUser.usertype.value == 5">
                   <th>用户名</th>
                </s:if>
                  <th>充电金额（元）</th>
                  <th>停车金额（元）</th>
                  <th>服务金额（元）</th>
                  <th>充电时长（分）</th>
                  <th>支付状态</th>
                  <th>交易号</th>
                  <th>充电时间</th>
                </tr>
                </thead>
                <tbody id="userChargeBody">
                </tbody>
              </table>
              </div>
              <%@include file="../../common/global/pagingtoolbar.jsp"%>
              </div>
           </div>
      </div>
	</section>
<script type="text/javascript">
var userType = "${webUser.usertype.value}";
</script>
 <script src="res/js/user/account/userCharge.js"></script>

