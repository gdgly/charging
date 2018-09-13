/**
 * 收益分析
 */
$(function(){
	//点击保存图片居中
	$(".bg").on("click",function(){
		$("[title='点击保存']").css("margin-top","15%");
	});
	var data=[];
	var param={};
	var seriesParam={};
	var pofitAnalysisByAjaxUrl = "profit_profitAnalysisByAjax.action";
	var BY_YEAR          = 1;
	var BY_MONTH         = 2;
	var BY_QUARTER       = 3;
	function initMain2(){
		option2.title.text = currentMonth+"月次数统计（次）";
		option2.series[0].data[0].value = countApp;
		option2.series[0].data[1].value = countCha;
		echarts.init(document.getElementById('main2'),'macarons').setOption(option2);
	}
	//初始化main4图表
	function initMain4(data){
		option4.title.text = data.map.main4.titleText;
		option4.title.subtext = data.map.main4.titleSubtext;
		option4.legend.data = data.map.main4.legendData;
		option4.xAxis[0].data = data.map.main4.xAxisData;
		for(var i=0;i< option4.legend.data.length;i++ ){
			seriesParam={};
			seriesParam.name = option4.legend.data[i];
			seriesParam.type="bar";
			seriesParam.stack = "总量";
			//seriesParam.itemStyle = { normal: {label : {show: true, position: 'insideRight'}}};
			seriesParam.data = data.map.main4.chargingMoney[i];
			option4.series[i] = seriesParam;
		}
		echarts.init(document.getElementById('main4'),'macarons').setOption(option4);
	}
//	初始化main1图表
	function initMain1(data){
		option1.title.text = data.map.main1.titleText;
		option1.title.subtext = data.map.main1.titleSubtext;
		option1.legend.data = data.map.main1.legendData;
		option1.xAxis[0].data = data.map.main1.xAxisData;

		//for(var i=0;i< option1.legend.data.length;i++ ){
		seriesParam={};
		//seriesParam.itemStyle = { normal: {label : {show: true, position: 'insideRight'}}};
		seriesParam.data = data.map.main1.totalMoney;
		seriesParam.name = option1.legend.data[0];
		seriesParam.type="line";
		seriesParam.smooth=true;
		seriesParam.itemStyle={normal: {areaStyle: {type: 'default',color:'#C1FFC1'}}};
		option1.series[0] = seriesParam;
		//}
		echarts.init(document.getElementById('main1'),'macarons').setOption(option1);
	}

	function initProfit(index,profitType){
		if(index == 0){//初始化所有图标
			param.index = index;
			param.profitType=profitType;
			param.year = $("#datetimeInput").val();
			if(param.profitType == BY_MONTH || param.profitType == BY_QUARTER){
				if(isEmpty(param.year)){
					showWarning("请选择年份！！");
					return;
				}
			}
			
			$.ajax({  
				      url:pofitAnalysisByAjaxUrl,// 跳转到 action  
				      data:param,  
				      type:'post',  
				      cache:false,  
				      dataType:'json',  
				      beforeSend:function(){$("#loading").removeClass("hide");},
				      success:function(data) {
				    	  if(data.userLoginStatus){
				    		  checkLoginStatus(data,true);
				    		  return;
				    	  }
				    	 if(data.map.main4){
								initMain4(data);
							}
							if(data.map.main1){
								initMain1(data);
							}
				      },  
				      complete:function(){
				    	  $("#loading").addClass("hide"); 
				      },
				      error : function() {  
				           showWarning("异常！");
				      }  
				 });
		}
	}
/*	$("#datetimeInput").on("keydown",function(){
		$(this).val(new Date().getFullYear());
	});*/
	$("#datetimeInput").on("change",function(){
		$(this).val(new Date().getFullYear());
	});
	initOneBootStrapDate3($("#datetime"));//初始化日常控件
	initMain2();
	//1：年2：月3：季
	initProfit(0,BY_MONTH);
	
	//echarts.init(document.getElementById('main3'),'macarons').setOption(option3);
	//echarts.init(document.getElementById('main1'),'macarons').setOption(option1);

	$("#quarterBtn").on("click",function(){
		$("#MQYBtnGroup").find("button").each(function(index,data){
			$(data).removeClass("btn-success").addClass("btn-default");
		});
		$(this).addClass("btn-success");
		param.profitType = BY_QUARTER;
		initProfit(0,param.profitType);
	});
	$("#monthBtn").on("click",function(){
		$("#MQYBtnGroup").find("button").each(function(index,data){
			$(data).removeClass("btn-success").addClass("btn-default");
		});
		$(this).addClass("btn-success");
		param.profitType = BY_MONTH;
		initProfit(0,param.profitType);
	});
	$("#yearBtn").on("click",function(){
		$("#MQYBtnGroup").find("button").each(function(index,data){
			$(data).removeClass("btn-success").addClass("btn-default");
		});
		$(this).addClass("btn-success");
		param.profitType = BY_YEAR;
		initProfit(0,param.profitType);
	});

}); 


////////////1111111111111s////////
option4 = {
		calculable:false,
		title : {
			text: '各费用分析',
			subtext: '纯属虚构'
		},
		tooltip : {
			trigger: 'axis',
			axisPointer : {            // 坐标轴指示器，坐标轴触发有效
				type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
			}
		},
		legend: {
			data:["充电费", "停车费", "服务费", "预约费"]
		},
		toolbox: {
			show : true,
			feature : {
				mark : {show: false},
				dataView : {show: true, readOnly: false},
				magicType : {show: true, type: []},
				restore : {show: true},
				saveAsImage : {show: true}
			}
		},
		yAxis : [
		         {
		        	 type : 'value'
		         }
		         ],
		         xAxis : [
		                  {
		                	  type : 'category',
		                	  data : ['第一季度','第二季度','第三季度','第四季度']
		                  }
		                  ],
		                  series : [
		                            {
		                            	name:'预约费',
		                            	type:'bar',
		                            	stack: '总量',
		                            	//itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
		                            	data:[320, 302, 301, 334]
		                            },
		                            {
		                            	name:'服务费',
		                            	type:'bar',
		                            	stack: '总量',
		                            	// itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
		                            	data:[120, 132, 101, 134]
		                            },
		                            {
		                            	name:'停车费',
		                            	type:'bar',
		                            	stack: '总量',
		                            	//itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
		                            	data:[220, 182, 191, 234]
		                            },
		                            {
		                            	name:'充电费',
		                            	type:'bar',
		                            	stack: '总量',
		                            	//itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
		                            	data:[150, 212, 201, 154]
		                            }
		                            ]
};

// myChart1.setOption(option1);  

option2 = {
		title : {
			text: '6月统计（次）',
			subtext: '统计本月预约，充电次数'
		},
		grid:{
			height:"200px",
			width:"150px"
				//x2 :"200"
				//paddingLeft:"200px"
		},
		tooltip : {
			trigger: 'axis'
		},
		legend: {
			data:['']
		},
		toolbox: {
			show : true,
			feature : {
				mark : {show: false},
				dataView : {show: true, readOnly: false},
				magicType : {show: true, type: []},
				restore : {show: true},
				saveAsImage : {show: true}
			}
		},
		xAxis : [
		         {
		        	 type : 'category',
		        	 data : ['预约',"充电"]
		         }
		         ],
		         yAxis : [
		                  {
		                	  type : 'value'
		                  }
		                  ],
		                  series : [
		                            {
		                            	name:'次数',
		                            	type:'bar',
		                            	data:[
		                            	      {
		                            	    	  value:200,
		                            	    	  itemStyle:{
		                            	    		  normal:{color:'#9BCD9B'}
		                            	    	  }
		                            	      }, 
		                            	      {
		                            	    	  value:100,
		                            	    	  itemStyle:{
		                            	    		  normal:{color:'#436EEE'}
		                            	    	  }
		                            	      }
		                            	      ]

		                            }

		                            ]

};
///6666
option1 = {
		title : {
			text: '收益走势',
			subtext: '收益走势'
		},
		tooltip : {
			trigger: 'axis'
		},
		legend: {
			data:['收益']
		},

		toolbox: {
			show : true,
			feature : {
				mark : {show: false},
				dataView : {show: true, readOnly: false},
				magicType : {show: true, type: []},
				restore : {show: true},
				saveAsImage : {show: true}
			}
		},
		xAxis : [
		         {
		        	 type : 'category',
		        	 boundaryGap : false,
		        	 data : ['第一月','第二月','第三月','第四月','第五月','第六月','第七月','第八月','第九月','第十月','第十一月','第十二月']
		         }
		         ],
		         yAxis : [
		                  {
		                	  type : 'value'
		                  }
		                  ],
		                  series : [
		                            {
		                            	name:'收益',
		                            	type:'line',
		                            	smooth:true,
		                            	itemStyle: {normal: {areaStyle: {type: 'default',color:'#C1FFC1'}}},
		                            	data:[
		                            	      {
		                            	    	  value:40
		                            	      }, 
		                            	      {
		                            	    	  value:60,
		                            	    	  itemStyle:{normal: {color:'green'} }
		                            	      }, 
		                            	      71, 54, 50, 55, 71.55,70.0,72,71,65,66]
		                            }

		                            ]
};
