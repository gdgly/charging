/**
 * 个人账单
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
	var userConsumeUrl = "userjson/personal_userConsume.action";
	var userBillUrl = "userjson/account_userBill.action";
	var doReceiptUrl = "userjson/account_doReceipt.action";//去开票
	initExportBtn($("#exportBtn"),null,userBillUrl,null,"我的账单","searchDate","isBill");
	goUserMenu('userBill');
	//选择框util
    function subCheck(obj){
    	count = 0;
    	allcount = 0;
    	if(!$(obj).is(':checked')){
    		$("#allBillCheck").get(0).checked = false;
    	}
    	$("[name='billCheck']").each(function(index,value){
    			allcount = allcount + 1;
    	})
    		$("[name='billCheck']").each(function(index,value){
    			if($(value).is(':checked')){
    				count = count + 1;
    			}
    	})
    	if(((count+allcount) > 0) && (count == allcount)){
    		$("#allBillCheck").get(0).checked = true;
    	}
    }
	$(function(){
		for(x in userBillCharts){
			temp = parseInt(x)+1;
			option.title.text= userBillCharts[x].name;
			option.series[0].data[0].value = parseFloat(userBillCharts[x].appFee).toFixed(2);
			option.series[0].data[1].value = parseFloat(userBillCharts[x].chaFee).toFixed(2);
			if(userBillCharts[x].appFee == 0 && userBillCharts[x].chaFee == 0){
				$("#main"+temp).html("<p>"+userBillCharts[x].name+"</p>"+"<p class='text-center' style='margin-top:15%;'>暂无数据</p>");
			}else{
				echarts.init(document.getElementById('main'+temp),'macarons2').setOption(option);
			}
			
		}
		
		function initBody(){
			param.currentPage=$("#currentPage").val();
			param.searchDate = $("#searchDate").val();
			param.isBill = $("#isBill").val();
			var item = $("#userBillBody");
			$.ajax({
			      url:userBillUrl,// 跳转到 action
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
			    	  	$("#allBillCheck").get(0).checked = false;
			    		$("#allBillCheck").attr("disabled",false);
						item.empty();
						if(data.page.root.length > 0){
							$("#help-block").remove();
						$(data.page.root).each(function(index,value){
							totalMoney = value.appFeeOut+value.chaFeeOut+value.parkFeeOut+value.serviceFeeOut;
							str = "<tr>";
							if(value.receiptId && value.receiptId > 0){
								str = str + "<td>"+"<input  disabled='disabled' type='checkbox'/>"+"</td>";	
							}else if(value.totalFeeOutDesc <= 0){
								str = str + "<td>"+"<input  disabled='disabled' type='checkbox'/>"+"</td>";
							}
							else{
								str = str + "<td>"+"<input onclick='subCheck(this)' name='billCheck' type='checkbox' totalFeeOut='"+value.totalFeeOutDesc+"' billsId='"+value.id+"' time='"+value.checkCycle+"'>"+"</td>";	
							}
								
								str = str 
										+"<td class='outFeeColor'>-"+value.appFeeOut.toFixed(2)+"</td>"
										+"<td class='outFeeColor'>-"+value.chaFeeOut.toFixed(2)+"</td>"
										+"<td class='outFeeColor'>-"+value.parkFeeOut.toFixed(2)+"</td>"
										+"<td class='outFeeColor'>-"+value.serviceFeeOut.toFixed(2)+"</td>"
										+"<td class='outFeeColor' style='font-size:19px;'>-"+value.totalFeeOutDesc+"</td>"
										+"<td>"+value.checkCycle+"</td>";
								
								if(value.receiptId && value.receiptId > 0){
									str = str + "<td style='color:green;'>"+"已开票"+"</td>";
								}else if(value.totalFeeOutDesc <= 0){
									str = str + "<td>"+"</td>";
								}
								else{
									str = str + "<td style='color:#00acd6;'>"+"未开票"+"</td>";
								}
									str = str+"</tr>";
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
							 $("#allBillCheck").attr("disabled",true);
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
		//开票相关
		function checkReceiptInfo(obj){
			if(isEmpty(obj.time)){
				return  "开票周期不存在！！";
			}else if(!obj.money || isNaN(obj.money) || obj.money <= 0){
				return "开票金额必须大于0元！！";
			}else if(isEmpty(obj.billHead)){
				return "请填写发票抬头！！";
			}else if(isEmpty(obj.recipient) || !regBox.regCn.test(obj.recipient)){
				return "请正确填写收件人姓名！！";
			}else if(isEmpty(obj.phone) || !regBox.regMobile.test(obj.phone)){
				return "请正确填写11位手机号码！！";
			}else if(isEmpty(obj.address)){
				return "请填写收件地址！！";
			}
			return "success";
		}
		$("#saveBtn").on("click",function(){
			obj = getFormJson($("#userReiptForm"));
			obj.money = parseFloat($("#money").text());
			obj.time = $("#time").text();
			checkResult = checkReceiptInfo(obj);
			if("success" != checkResult){
				showWarning(checkResult);
				return false;
			}
			jsonObj = JSON.stringify(obj);
			param.jsonObj = jsonObj;
			param.billsId = $("#billsId").text();
			$.ajax({  
			      url:doReceiptUrl,// 跳转到 action  
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
			    	 if("success" == data.message){
			    		 	opt={};
							opt.onOk = function(){
								param.currentPage=$("#currentPage").val("1");
								param.searchDate = $("#searchDate").val("");
								param.isBill = $("#isBill").val("0");
								$("#userReiptModal").modal("hide");
								initBody();
								}
							opt.onClose = opt.onOk;
							showWarning("提交成功！！",opt);  
			    	 }else{
			    		 showWarning(data.message);
			    	 }
			      },
			      complete:function(){
			    	  $("#loading").addClass("hide"); 
			      },
			      error : function() {  
			           showWarning("异常！");
			      }  
			});
		});
		$("#searchBtn").on("click",function(){
			$("#currentPage").val(1);
			initBody();
		});
	//弹出开票modal
	function goReceipt(billId,billTime,billMoney){
		totalFeeOut = 0;
		time = "";
		for(x in billMoney){
			if(billMoney[x] <= 0){
				showWarning("不可选择月账单金额为0的记录！！");
				return;
			}
			totalFeeOut = totalFeeOut+parseFloat(billMoney[x]);
		}
		for(x in billTime){
			time = time + billTime[x]+",";
		}
		if(time.endWith(",")){
			time = time.substring(0,time.length-1);
		}
		if(totalFeeOut > 0){
			$("#money").text(totalFeeOut);
			$("#time").text(time);
			$("#billsId").text(billId);
			resetForm($("#userReiptForm"));
			$("#userReiptModal").modal();
		}else{
			showWarning("开票金额必须大于0元！！");
		}
	}
		$("#allBillCheck").on("click",function(){
			if($(this).is(':checked')){
				$("[name='billCheck']").each(function(index,value){
					$(value).get(0).checked = true;
				});
			}else{
				$("[name='billCheck']").each(function(index,value){
					$(value).get(0).checked = false;
				});
			}
		})
		var billId = [];
		var billTime = [];
		var billMoney = [];
		$("#goReceiptBtn").on("click",function(){
			 billId = [];
			 billTime = [];
			 billMoney = [];
			$("[name='billCheck']").each(function(index,value){
			if($(value).is(':checked')){
				billId.push($(value).attr("billsid"));
				billTime.push($(value).attr("time"));
				billMoney.push($(value).attr("totalfeeout"));
			}
			});
			if(billId.length > 0 && billTime.length > 0 && billMoney.length > 0){
				goReceipt(billId,billTime,billMoney);
			}else{
				showWarning("请选择开票记录！！");
			}
		});
		
		
		
		//消费明细相关
		function initBody2(){
			param.currentPage=$("#currentPage2").val();
			//param.searchKeyName = $("#searchKeyName").val().trim();
			var item = $("#userConsumeBody");
			$.ajax({  
			      url:userConsumeUrl,// 跳转到 action  
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
							temp = value.type;
							str = "<tr>"
								+"<td>"+value.name+"</td>";
							if(temp == 2){//充电
								str = str+"<td class='outFeeColor'>"+value.typedesc+"</td>";
							}else if(temp == 3){//预约
								str = str+"<td style='color:#3c8dbc;'>"+value.typedesc+"</td>";
							}else if(temp == 1){//充值
								str = str+"<td class='inFeeColor'>"+value.typedesc+"</td>";
							}
							else{
								str = str+"<td>"+value.typedesc+"</td>";
							}
								str = str
										+"<td class='outFeeColor'>-"+value.fee.toFixed(2)+"</td>"
										+"<td>"+value.datatimeDesc+"</td>"
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
		$("#goReceiptPageBtn").on("click",function(){
			$("#userBillChart").addClass("hide");
			$("#userConsumeTable").addClass("hide");
			$("#userBillTable").removeClass("hide");
			$("#currentPage").val("1");
			$("#searchDate").val("");
			$("#isBill").val("0");
			initBody();
		});
		$("#goMoreReceiptPageBtn").on("click",function(){
			$("#userBillChart").addClass("hide");
			$("#userConsumeTable").addClass("hide");
			$("#userBillTable").removeClass("hide");
			$("#currentPage").val("1");
			$("#searchDate").val("");
			$("#isBill").val("0");
			initBody();
		});
		$("#comeBackBtn").on("click",function(){
			$("#userBillChart").removeClass("hide");
			$("#userConsumeTable").removeClass("hide");
			$("#userBillTable").addClass("hide");
		});
	})
	
