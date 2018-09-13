<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<%@include file="../../common/global/meta.jsp"%>
<%@include file="../../common/global/js.jsp"%>
<%@include file="../head.jsp"%>
<html lang="zh-CN">
<head>
<script src="res/js/bussiness/echart/echarts-all.js"></script>
<style type="text/css">
#showBillsDiv span{
color:green;
font-size:17px;
}
</style>
</head>
<body>
	<div class="bg">
		<!--main-content-->
		<div class="container main-body tablePageSize" id="billsListDiv">
		<div class="form-group">
		<div class="col-sm-3">
		<h3>历史月账单</h3>
		</div>
			<div class="col-sm-9" style="padding-top: 8px;">
				<ul class="nav nav-tabs" style="float: right;margin-top: 12px;" id="showBillsMenu">
				<s:iterator value="#request.billsList" id="item" status="statu">
				<s:if test="#statu.first">
					<li class="active" checkCycle=<s:property value="checkCycle"/> userId=<s:property value="userId"/> id=<s:property value="id"/> ><a href="javascript:;" onclick="showOneBills(this);"><s:property value="checkCycle"/></a></li>
				</s:if>
				<s:else>
				<li checkCycle=<s:property value="checkCycle"/> userId=<s:property value="userId"/> id=<s:property value="id"/> ><a href="javascript:;" onclick="showOneBills(this);"><s:property value="checkCycle"/></a></li>
				</s:else>
				</s:iterator>
				</ul>
			</div>
			</div>
		<div class="form-group">
			<div class="col-sm-5" style="margin-top: 50px;" id="showBillsDiv">
			<h5>收入：</h5>
				<label for="totalFee" class="col-sm-4 control-label">总费用收入：</label>
				<div class="col-sm-8">
						<p>
						<span id="totalFee"></span> 元
						</p>
				</div>
				<label for="appFee" class="col-sm-4 control-label">预约费收入：</label>
				<div class="col-sm-8">
						<p>
						<span id="appFee"></span> 元
						</p>
				</div>
				<label for="chaFee" class="col-sm-4 control-label">充电费收入：</label>
				<div class="col-sm-8">
						<p>
						<span id="chaFee"></span> 元
						</p>
				</div>
				<label for="serviceFee" class="col-sm-4 control-label">服务费收入：</label>
				<div class="col-sm-8">
						<p>
						<span id="serviceFee"></span> 元
						</p>
				</div>
				<label for="parkFee" class="col-sm-4 control-label">停车费收入：</label>
				<div class="col-sm-8">
						<p>
						<span id="parkFee"></span> 元
						</p>
				</div>
				<h5>提现：</h5>
				<label for="cashFee" class="col-sm-4 control-label">提现总额：</label>
				<div class="col-sm-8">
						<p>
						<span style="color: coral;" id="cashFee"></span> 元
						</p>
				</div>
				<div class="col-sm-12">
				<a id="showBillsDetalBtn" billsDetailTitle="" billsId="0" href="javascript:;">查看账单详情>></a>
				</div>
			</div>
			<div class="col-sm-5 hide" style="margin-top: 15%;" id="noBillsDiv">
			<p class="text-center help-block">暂无结账信息</p>
			</div>
				<div class="col-sm-7" style="margin-top: 20px;">
				<div style="width: 634px; height: 400px;" id="main"></div>
				</div>
			</div>	
		</div>
		
			<div class="container main-body tablePageSize hide" id="billsDetailDiv">
		<div class="row">
			<div class="col-sm-offset-10 col-sm-1 text-right" style="margin-top: 13px;">
					<button style="margin-right: -18px;" id="comeBackBillsListBtn" type="button" class="btn btn-warning btn-sm">
						返回
					</button>
			</div>
			<div class="col-sm-1" style="margin-top: 13px;">
					<button id="exportBtn" type="button" class="btn btn-warning btn-sm">
						导出
					</button>
			</div>
		</div>
			<div class="from-group">
		<label for="tel" class="col-sm-2 control-label">客户名：</label>
					<div class="col-sm-2 text-left">
						<p>${webUser.userName}</p>
					</div>
				<%-- 		<label for="tel" class="col-sm-2 control-label"></span>联系电话：</label>
					<div class="col-sm-10">
						<p>${webUser.phone}</p>
					</div> --%>
					<label for="tel" class="col-sm-2 control-label"></span>结算周期：</label>
					<div class="col-sm-2">
						<p id="billsDetailTitle"></p>
					</div>
		</div>
			<div id="billsDetailDiv">
				<div class="tableDivSize">
					<table class="table table-condensed" id="billsDetailTable">
						<tbody>
						</tbody>
					</table>
				</div>
				<%@include file="../../common/global/pagingtoolbar.jsp"%>
			</div>
		</div>
	</div>
	
	
	
<script type="text/javascript">
/* setBowHeight1(); */
option = {
	    title : {
	        text: '6个月收益走势',
	        subtext: ''
	    },
	    tooltip : {
	        trigger: 'axis'
	    },
	    legend: {
	        data:['月收入(元)']
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            mark : {show: false},
	            dataView : {show: false, readOnly: false},
	            magicType : {show: true, type: ['line', 'bar']},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    calculable : true,
	    xAxis : [
	        {
	            type : 'category',
	            boundaryGap : false,
	            data : []
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value',
	            axisLabel : {
	                formatter: '{value} 元'
	            }
	        }
	    ],
	    series : [
	        {
	            name:'月收入(元)',
	            type:'line',
	            data:[],
	            markPoint : {
	                data : [
	                    {type : 'max', name: '最大值'},
	                    {type : 'min', name: '最小值'}
	                ]
	            }
	        }
	    ]
	}
//显示详细信息
function showDetail(obj){
	checkMark = $(obj).attr("checkMark");
	html = "";
	if(checkMark == 1){
	var html1 = "<div class='form-group' style='width:160px;'>"
		+"<label for='chaFee' class='col-sm-8 control-label'>充电费用：</label>"
		+"<div class='col-sm-4'>"
		+"<p id='chaFee' class='text-center'>"
		+parseFloat($(obj).attr("chaFee")).toFixed(2)
		+"</p>"
		+"</div>";
	var html2 = "<div class='form-group' style='width:160px;'>"
		+"<label for='serviceFee' class='col-sm-8 control-label'>服务费用：</label>"
		+"<div class='col-sm-4'>"
		+"<p id='serviceFee' class='text-center'>"
		+parseFloat($(obj).attr("serviceFee")).toFixed(2)
		+"</p>"
		+"</div>";
	var html3 = "<div class='form-group' style='width:160px;'>"
		+"<label for='parkFee' class='col-sm-8 control-label'>停车费用：</label>"
		+"<div class='col-sm-4'>"
		+"<p id='parkFee' class='text-center'>"
		+parseFloat($(obj).attr("parkFee")).toFixed(2)
		+"</p>"
		+"</div>";
	html = html1+html2+html3;
	}else if(checkMark == 2){
		html = "<div class='form-group' style='width:160px;'>"
			+"<label for='chaFee' class='col-sm-8 control-label'>预约费用：</label>"
			+"<div class='col-sm-4'>"
			+"<p id='chaFee' class='text-center'>"
			+parseFloat($(obj).attr("appFee")).toFixed(2)
			+"</p>"
			+"</div>";
	}
	$(obj).popover({ 
		html : true,
		title: function() {
			return "<p class='text-center' style='margin-bottom:1px;'>费用明细</p>";
		},
		content: function() {
			return html;
		}
	});
	$(obj).popover("show");
}
//关闭详细信息
function closeDetail(obj){
	$(obj).popover("hide");
}


	                    
var billsId;
var showBillsByAjaxURL = "account_showBillsByAjax.action";
var param={};
var busBills;
var josnBillsList = ${josnBillsList};
var showBillsDetailByAjaxUrl = "account_showBillsDetailByAjax.action";
var item;
var defaultData = 0;
 	option.xAxis[0].data=[];
	option.series[0].data=[];
for(var i in josnBillsList){
	billsBean = josnBillsList[i];
	option.xAxis[0].data.push(billsBean.checkCycle);
	totalCheckFee = parseFloat(billsBean.appFeeIn)+parseFloat(billsBean.chaFeeIn)+parseFloat(billsBean.serviceFeeIn)+parseFloat(billsBean.parkFeeIn);
	if(totalCheckFee){
		totalCheckFee = totalCheckFee.toFixed(2);
	}else{
		totalCheckFee = 0; 
	}
	option.series[0].data.push(totalCheckFee);
} 
echarts.init(document.getElementById('main'),'macarons').setOption(option);
function getBillsByAjax(){
	$.ajax({  
	      url:showBillsByAjaxURL,// 跳转到 action  
	      data:param,  
	      type:'post',  
	      cache:false,  
	      dataType:'json',  
	      beforeSend:function(){$("#loading").removeClass("hide");},
	      success:function(data){
	    	  if(data.userLoginStatus){
	    		  checkLoginStatus(data,true);
	    		  return;
	    	  }
	    	  busBills = data.map.busBills;
	    	  if(busBills){
	    		  appFeeIn = busBills.appFeeIn;
	    		  chaFeeIn= busBills.chaFeeIn;
	    		  serviceFeeIn = busBills.serviceFeeIn;
	    		  parkFeeIn = busBills.parkFeeIn;
	    		  cashFee= busBills.cashFee;
	    		  totalFeeIn = appFeeIn+chaFeeIn+serviceFeeIn+parkFeeIn;
	    		  $("#totalFee").text("+"+totalFeeIn.toFixed(2));
	    		  $("#appFee").text("+"+appFeeIn);
	    		  $("#chaFee").text("+"+chaFeeIn);
	    		  $("#serviceFee").text("+"+serviceFeeIn);
	    		  $("#parkFee").text("+"+parkFeeIn);
	    		  $("#cashFee").text("-"+cashFee.toFixed(2));
	    		  $("#showBillsDetalBtn").attr("billsId",busBills.id);
	    		  $("#showBillsDetalBtn").attr("billsDetailTitle",busBills.checkCycle);
	    		  
	    		  $("#showBillsDiv").removeClass("hide");
		  		  $("#noBillsDiv").addClass("hide");
		  		  
	    	  }else{
	    		  $("#showBillsDiv").addClass("hide");
		  			$("#noBillsDiv").removeClass("hide");
	    	  }
	      },
	      complete:function(){
	    	  $("#loading").addClass("hide"); 
	      },
	      error : function() {
	           showWarning("异常！");
	      }
	})
}
function showOneBills(obj){
	$("#showBillsMenu").find("li").each(function(index,data){
		$(data).removeClass("active");
	});
	$(obj).parent().addClass("active");
	billsId = $(obj).parent().attr("id");
	userId = $(obj).parent().attr("userId");
	checkCycle = $(obj).parent().attr("checkCycle");
	if(billsId && billsId > 0){
		param.billsId = billsId;
		param.userId = userId;
		param.checkCycle = checkCycle;
		getBillsByAjax();
	}else{
		$("#showBillsDiv").addClass("hide");
		$("#noBillsDiv").removeClass("hide");
	}
	
}

$(function(){
	$(".bg").on("click",function(){
		$("[title='点击保存']").css("margin-top","15%");
	});
	$("#showBillsMenu").find("li").each(function(index,data){
		if($(data).hasClass("active")){
			billsId = $(data).attr("id");
			param.userId = $(data).attr("userId");
			param.checkCycle = $(data).attr("checkCycle");
		}
	});
	if(billsId && billsId > 0){
		param.billsId = billsId;
		getBillsByAjax();
	}else{
		$("#showBillsDiv").addClass("hide");
		$("#noBillsDiv").removeClass("hide");
	}
	$("#showBillsDetalBtn").on("click",function(){
		billsId = param.billsId;
		if(billsId && billsId > 0){
			$("#currentPage").val(1)
			initBillDetailBody();
			$("#billsDetailTitle").text(param.checkCycle);
			$("#billsListDiv").addClass("hide");
			$("#billsDetailDiv").removeClass("hide");
		}else{
			showWarning("请选择要查看的账单详情月份");
		}
	});
	$("#comeBackBillsListBtn").on("click",function(){
		$("#billsListDiv").removeClass("hide");
		$("#billsDetailDiv").addClass("hide");
	});
	//分页查询详细账单
	function initBillDetailBody(){
	
		param.currentPage=$("#currentPage").val();
		item = $("#billsDetailTable").find("tbody");
		$.ajax({  
		      url:showBillsDetailByAjaxUrl,// 跳转到 action  
		      data:param,  
		      type:'post',  
		      cache:false,  
		      dataType:'json',  
		      beforeSend:function(){$("#loading").removeClass("hide");},
		      success:function(data){
		    	  if(data.userLoginStatus){
		    		  checkLoginStatus(data,true);
		    		  return;
		    	  }
		    	  item.empty();
		    	  $(data.page.root).each(function(index,value){
		 				str = "<tr>"
		 					+"<td>"+value.stationName+"</td>"
		 					+"<td>"+value.pileName+"</td>"
		 					+"<td><div style='width:180px;'>收入金额：<span style='color:green;'>+"+value.totalFeeDesc+"</span>"
		 					+"&nbsp;&nbsp;<img style='float:right;padding-top:5px;' src='"
		 					+IMG_SRC
		 					+"res/img/bussiness/detail.png'"
		 					+" chaFee="+getDefaultData(value.chaFee,0)
							+" serviceFee="+getDefaultData(value.serviceFee,0)
							+" parkFee="+getDefaultData(value.parkFee,0)
							+" appFee="+getDefaultData(value.appFee,0)
							+" checkMark="+value.checkMark
							+" onmouseleave='closeDetail(this);' onmouseenter='showDetail(this);' name='detail'/>"
							+"</div>"
							+"</td>"
						 	+"<td>入账方式：<span>"+value.checkMarkDesc+"</span></td>"
						 	+"<td class='text-right'>"+value.createTimeDesc+"</td>"
							+"</tr>";
							/* +"<td>充电费：<span style='color:green;'>+"+getDefaultData(value.chaFee, defaultData.toFixed(2))+"</span></td>"
							+"<td>服务费：<span style='color:green;'>+"+getDefaultData(value.serviceFee, defaultData.toFixed(2))+"</span></td>"
							+"<td>停车费：<span style='color:green;'>+"+getDefaultData(value.parkFee, defaultData.toFixed(2))+"</span></td>"; */
							
						item.append(str);//封装table 
					});
		    	    stationTotalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
					setPagingToolbarParams(data.page.totalProperty,stationTotalPage,$("#currentPage").val());
		    	  
		      },
		      complete:function(){
		    	  $("#loading").addClass("hide"); 
		      },
		      error : function() {  
		           showWarning("异常！");
		      }  
	})
	}
	initPagingToolbar(initBillDetailBody);
	$("#exportBtn").on("click",function(){
		href(showBillsDetailByAjaxUrl+"?isExport=1"+"&billsId="+param.billsId+"&userId="+param.userId+"&checkCycle="+param.checkCycle);
	})
})
</script>
</body>
</html>

