/**
 * 个人充电
 */
	var param={};
	var userChargeUrl = "userjson/account_userCharge.action";
	initExportBtn($("#exportBtn"),null,userChargeUrl,null,"我的充电","searchDate");
	$(function(){
		function initBody(){
			param.currentPage=$("#currentPage").val();
			param.searchDate = $("#searchDate").val();
			param.userName = $("#userName").val();
			var item = $("#userChargeBody");
			$.ajax({
			      url:userChargeUrl,// 跳转到 action
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
							str = "<tr>";
							if(userType == 5){
								str = str +"<td>"+value.userName+"</td>";
							}
							str = str
								+"<td style='color:red;'>"+value.chaFee.toFixed(2)+"</td>"
								+"<td style='color:red;'>"+value.parkFee.toFixed(2)+"</td>"
								+"<td style='color:red;'>"+value.serviceFee.toFixed(2)+"</td>"
								+"<td>"+value.chaLen+"</td>"
								+"<td>"+value.payStatusDesc+"</td>"
								+"<td>"+value.tradeNo+"</td>"
								+"<td>"+value.updateTimeDesc+"</td>"
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
		$("#searchBtn").on("click",function(){
			$("#currentPage").val(1);
			initBody();
		});
		initOneBootStrapDate2($('#datepicker'));
		initPagingToolbar(initBody);
		initBody();
	})
	
