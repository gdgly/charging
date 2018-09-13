<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<%@include file="../../common/global/meta.jsp"%>
<%@include file="../../common/global/js.jsp"%>
<%@include file="../head.jsp"%>
<html lang="zh-CN">
<head>
</head>
<body>
	<script src="res/js/bussiness/echart/echarts-all.js"></script>
	<div class="bg">
		  <!--msg-content-->
	  <s:if test="#request.msg != null">
		<div class="container main-body"  style="height: 50%;">
			<p class="text-center" style="padding-top: 14%;">
			<strong>提示信息：</strong>${msg}
			<s:if test="#request.backUrl != null">
			<a href="<s:property value="#request.backUrl"/>">
			<s:if test="#request.retrunDec != null">
			<s:property value="#request.retrunDec"/>
			</s:if>
			<s:else>
			返回
			</s:else>
			</a>
			</s:if>
			<s:else>
			<a onclick="history.go(-1)" href="javascript::">返回</a>
			</s:else>
			</p>
		</div>
	</s:if>
	<s:else>
		<!--main-content-->
		<div class="container row" style="width: 100%;">
			<div class="container  col-sm-offset-1 col-sm-11"
				style="height: 6%; background-color: transparent; margin-top: 20px;">
				<div class="btn-group btn-group-sm " style="padding-top: 30px;float: left;"
					id="MQYBtnGroup">
					<button type="button" class="btn btn-success" id="monthBtn">月</button>
					<button type="button" class="btn btn-default" id="quarterBtn">季</button>
					<button type="button" class="btn btn-default" id="yearBtn">年</button>
				</div>
						<div class="input-group date" id="datetime" style="padding-top: 30px;padding-left: 15px;width: 145px;">
						<input style="height: 31px;" type='text' class="form-control" placeholder="请选择月份" id="datetimeInput"/> 
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
			</div>
			<div class="container main-body col-sm-offset-1 col-sm-4"
				style="width: 55%; height: 300px;">
				<div style="width: 100%; height: 100%;" id="main1"></div>
			</div>
			<div class="container main-body col-sm-offset-1 col-sm-3"
				style="width: 22%; height: 300px;">
				<div style="width: 100%; height: 100%;" id="main2"></div>
			</div>

			<div class="container main-body col-sm-offset-1 col-sm-4"
				style="width: 55%; height: 300px;">
				<div style="width: 100%; height: 100%;" id="main4"></div>
			</div>
			<div class="container main-body col-sm-offset-1 col-sm-3"
				style="width: 22%; height: 300px;">
				<div class="row" id="main3" style="width: 100%; height: 100%;">
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
					<p class="text-center help-block" style="margin-top: 30%;">暂无排名信息</p>
					</s:else>
				</div>
			</div>
		</div>
		</s:else>
		<!-- <div class="col-sm-6">
	<div style="width:150px;">
      <select id="echart2" class="form-control">
         <option selected="selected" value="3">按季度统计</option>
         <option value="2">按月统计</option>
         <option value="1">按年统计</option>
      </select>
     </div>
</div> -->
		<!-- <div class="col-sm-6"  id="main1" style="width:550px;height: 400px;"></div>
<div class="col-sm-6" id="main2" style="width:550px;height: 400px;"></div>
<div class="col-sm-6">
	<div style="width:150px;">
      <select id="echart3" class="form-control">
         <option selected="selected" value="3">按季度统计</option>
         <option value="2">按月统计</option>
         <option value="1">按年统计</option>
      </select>
     </div>
</div>
<div class="col-sm-6">
	<div style="width:150px;">
      <select id="echart4" class="form-control">
         <option selected="selected" value="3">按季度统计</option>
         <option value="2">按月统计</option>
         <option value="1">按年统计</option>
      </select>
     </div>
</div> -->
	</div>
</body>
<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
<script type="text/javascript">
var countApp = "${countApp}";
var countCha = "${countCha}";
var currentMonth = "${currentMonth}";
</script>
<script src="res/js/bussiness/profit.js"></script>
</html>

