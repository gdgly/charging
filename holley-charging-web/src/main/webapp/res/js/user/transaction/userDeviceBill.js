/**
 * 收益账单
 */
option = {
		title : {
			text: '7月总费用（元）',
			 textStyle : {
                 fontSize : '15'
             }
		},
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: {
        data:['预约','充电']
    },
    toolbox: {
        show : false,
        feature : {
            mark : {show: false},
            dataView : {show: false, readOnly: false},
            magicType : {
                show: false, 
                type: ['pie', 'funnel'],
                option: {
                    funnel: {
                        x: '25%',
                        width: '50%',
                        funnelAlign: 'center',
                        max: 1548
                    }
                }
            },
            restore : {show: false},
            saveAsImage : {show: true}
        }
    },
    calculable : false,
    series : [
        {
            name:'费用',
            type:'pie',
            radius : ['55%', '70%'],
            itemStyle : {
                normal : {
                    label : {
                        show : false
                    },
                    labelLine : {
                        show : false
                    }
                },
                emphasis : {
                    label : {
                        show : true,
                        position : 'center',
                        textStyle : {
                            fontSize : '15',
                            fontWeight : 'bold'
                        }
                    }
                }
            },
            data:[
                {value:'2001', name:'预约'},
                {value:310, name:'充电'}
            ]
        }
    ]
};
                    
	var param={};
	var userIncomeUrl = "userjson/transaction_userIncome.action";
	var userDeviceBillUrl = "userjson/transaction_userDeviceBill.action";
	goUserMenu('userDeviceBill');
	initExportBtn($("#exportBtn"),null,userDeviceBillUrl,null,"收益账单");
	$(function(){
		for(x in userDeviceBillCharts){
			temp = parseInt(x)+1;
			option.title.text= userDeviceBillCharts[x].name;
			option.series[0].data[0].value = parseFloat(userDeviceBillCharts[x].appFee).toFixed(2);
			option.series[0].data[1].value = parseFloat(userDeviceBillCharts[x].chaFee).toFixed(2);
			if(userDeviceBillCharts[x].appFee == 0 && userDeviceBillCharts[x].chaFee == 0){
				$("#main"+temp).html("<p>"+userDeviceBillCharts[x].name+"</p>"+"<p class='text-center' style='margin-top:15%;'>暂无数据</p>");
			}else{
				echarts.init(document.getElementById('main'+temp),'macarons2').setOption(option);
			}
			
		}
		//echarts.init(document.getElementById('main1'),'macarons2').setOption(option);
		//全部设备月账单
		function initBody(){
			param.currentPage=$("#currentPage").val();
			param.searchDate = $("#searchDate").val();
			var item = $("#userDeviceBillBody");
			$.ajax({
			      url:userDeviceBillUrl,// 跳转到 action
			      data:param,  
			      type:'post',
			      cache:false,  
			      dataType:'json', 
			      beforeSend:function(){$("#loading").removeClass("hide");},
			      success:function(data){
			    	  if(data.userLoginStatus){
			    		  checkLoginStatus(data,false);
			    		  return;
			    	  }
						item.empty();
						if(data.page.root.length > 0){
							$("#help-block").remove();
						$(data.page.root).each(function(index,value){
							//totalMoney = value.appFeeOut+value.chaFeeOut+value.parkFeeOut+value.serviceFeeOut;
								str = "<tr>" 
										+"<td class='inFeeColor'>+"+value.appFeeIn.toFixed(2)+"</td>"
										+"<td class='inFeeColor'>+"+value.chaFeeIn.toFixed(2)+"</td>"
										+"<td class='inFeeColor'>+"+value.parkFeeIn.toFixed(2)+"</td>"
										+"<td class='inFeeColor'>+"+value.serviceFeeIn.toFixed(2)+"</td>"
										+"<td class='inFeeColor' style='font-size:19px;'>+"+value.totalFeeInDesc+"</td>"
										+"<td>"+value.checkCycle+"</td>"
										+"</tr>";
								
								/*temp = value.status;
							if(1 == temp){//操作中
								str = str + "<td style='color:#FF8247;'>"+value.statusDesc+"</td>";
							}else if(2 == temp){//成功
								str = str + "<td style='color:#9ACD32;'>"+value.statusDesc+"</td>";
							}else{//失败
								str = str + "<td style='color:red;'>"+value.statusDesc+"</td>";
							}
								str = str +"<td>"+value.addTimeDesc+"</td>"
									+"</tr>";*/
							item.append(str);//封装table
						});
						$("#pagingToolbar").show();
						}else{
							 $("#pagingToolbar").hide();
							 if(!$("#help-block").attr("id")){
								 item.parent().parent().append("<p id='help-block' class='help-block text-center' style='margin-top:10%;'>暂无记录</p>");
							 }
						}
						var stationTotalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
						setPagingToolbarParams(data.page.totalProperty,stationTotalPage,$("#currentPage").val());
					},  
			      complete:function(){
			    	  $("#loading").addClass("hide"); 
			      },
			      error : function() {  
			           showWarning("异常！");
			      }  
			 });
		};
		initOneBootStrapDate2($('#datepicker'));
		initPagingToolbar(initBody);
		//initBody();
		//收入明细相关
		function initBody2(){
			param.currentPage=$("#currentPage2").val();
			//param.searchKeyName = $("#searchKeyName").val().trim();
			var item = $("#userIncomeBody");
			$.ajax({  
			      url:userIncomeUrl,// 跳转到 action  
			      data:param,  
			      type:'post',  
			      cache:false,  
			      dataType:'json', 
			      beforeSend:function(){$("#loading").removeClass("hide");},
			      success:function(data){
			    	  if(data.userLoginStatus){
			    		  checkLoginStatus(data,false);
			    		  return;
			    	  }
						item.empty();
						if(data.page.root.length > 0){
							$("#help-block2").remove();
						$(data.page.root).each(function(index,value){
							temp = value.checkMark;
							str = "<tr>"
								+"<td>"+value.stationName+" "+value.pileName+"</td>";
							if(temp == 1){//充电
								str = str+"<td class='outFeeColor'>"+value.checkMarkDesc+"</td>";
							}else if(temp == 2){//预约
								str = str+"<td style='color:#3c8dbc;'>"+value.checkMarkDesc+"</td>";
							}
							else{
								str = str+"<td>"+value.checkMarkDesc+"</td>";
							}
								str = str
										+"<td class='inFeeColor'>+"+value.totalFeeDesc.toFixed(2)+"</td>"
										+"<td>"+value.createTimeDesc+"</td>"
										+"</tr>";
							item.append(str);//封装table
						});
						 	 $("#pagingToolbar2").show();
						}else{
							 $("#pagingToolbar2").hide();
							 if(!$("#help-block2").attr("id")){
								 item.parent().parent().append("<p id='help-block2' class='help-block text-center' style='margin-top:10%;'>暂无记录</p>");
							 }
						}
						var stationTotalPage2 = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
						setPagingToolbar2Params(data.page.totalProperty,stationTotalPage2,$("#currentPage2").val());
					},  
			      complete:function(){
			    	  $("#loading").addClass("hide"); 
			      },
			      error : function() {  
			           showWarning("异常！");
			      }  
			 });
		};
		initPagingToolbar2(initBody2);
		initBody2();
		$("#goMoreDeviceBillPageBtn").on("click",function(){
			$("#userDeviceBillChart").addClass("hide");
			$("#userIncomeTable").addClass("hide");
			$("#userDeviceBillTable").removeClass("hide");
			$("#currentPage").val("1");
			$("#searchDate").val("");
			initBody();
		});
		$("#comeBackBtn").on("click",function(){
			$("#userDeviceBillChart").removeClass("hide");
			$("#userIncomeTable").removeClass("hide");
			$("#userDeviceBillTable").addClass("hide");
		});
		$("#searchBtn").on("click",function(){
			$("#currentPage").val("1");
			initBody();
		});
	})
	
