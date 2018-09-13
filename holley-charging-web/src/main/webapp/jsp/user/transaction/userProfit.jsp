<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
	<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
	<script src="res/js/bussiness/echart/echarts-all.js" type="text/javascript"></script>
	<section class="content-header">
       <h1 id="">收益分析</h1> 
      <ol class="breadcrumb">
       	 <li>
      	  	<a href="javascript:;" onclick="frameReload();">
      	 		<i class="fa fa-home"></i> 首页
      	 	</a>
      	 </li>
        <li><i class="fa fa-user"></i> 交易中心</li>
         <li class="active">收益分析</li>
      </ol>
    </section>
    <!-- Main content -->
    <section class="content">
    
      <div class="row">
            <div class="col-md-2">
        <div class="box-body">
      			<div class="btn-group" id="MQYBtnGroup">
					<button type="button" class="btn btn-info" id="monthBtn">月</button>
					<button type="button" class="btn btn-default" id="quarterBtn">季</button>
					<button type="button" class="btn btn-default" id="yearBtn">年</button>
				</div>
				</div>
      </div>
          <div class="col-md-10">
            <div class="box-body">
            	<div class="input-group date" id="datetime" style="width: 145px;">
							<input  type='text' class="form-control" placeholder="请选择月份" id="datetimeInput"/> 
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
				</div>
            </div>
      					
      	</div>
          <div class="col-md-7">
	          <div class="box box-info">
	            <div class="box-body">
	            	<div class="chart">
	                	<div id="main1" style="height: 300px;"></div>
	                </div>
	            </div>
	            <!-- /.box-body -->
	          </div>
	          <div class="box box-info">
	            <div class="box-body">
	            	<div class="chart">
	                	<div id="main4" style="height: 300px;"></div>
	                </div>
	            </div>
	            <!-- /.box-body -->
	          </div>
          </div>
          
         <div class="col-md-4">
          <div class="box box-info">
            <div class="box-body">
            	<div class="chart">
                	<div id="main2" style="height: 300px;"></div>
                </div>
            </div>
            <!-- /.box-body -->
          </div>
          <div class="box box-info">
            <div class="box-body">
            	<div class="chart" style="height: 300px;">
                				<h4 class="text-center">${currentMonth}月收益排行(元)</h4>
					<hr />
					<s:if test="#request.countProfits != null && #request.countProfits.size > 0">
					<s:iterator value="#request.countProfits" status="status" id="item">
						<div style="height: 40px">
							<div class="col-sm-3">
								<p class="text-center">
									第
									<s:property value="#status.index+1" />
									名
								</p>
							</div>
							<div class="col-sm-4">
								<p class="text-center">
									<s:property value="pileName" />
								</p>
							</div>
							<div class="col-sm-5">
								<p class="text-center">
									<s:property value="totalMoney" />
								</p>
							</div>
						</div>
					</s:iterator>
					</s:if>
					<s:else>
					<p class="text-center help-block" style="margin-top: 20%;">暂无排名信息</p>
					</s:else>
                </div>
            </div>
            <!-- /.box-body -->
          </div>
          </div>
      </div>
    </section>
	<script type="text/javascript">
			var countApp = "${countApp}";
			var countCha = "${countCha}";
			var currentMonth = "${currentMonth}";
	</script>
    
    <script src="res/js/user/transaction/userProfit.js" type="text/javascript"></script>
