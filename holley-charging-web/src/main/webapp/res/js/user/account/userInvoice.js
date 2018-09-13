/**
 * 个人开票
 */
	var param={};
	var userInvoiceUrl = "userjson/account_userInvoice.action";
	initExportBtn($("#exportBtn"),null,userInvoiceUrl,null,"我的发票","searchDate");
	$(function(){
		function initBody(){
			param.currentPage=$("#currentPage").val();
			param.searchDate = $("#searchDate").val();
			var item = $("#userInvoiceBody");
			$.ajax({
			      url:userInvoiceUrl,// 跳转到 action
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
							str = "<tr>"
								+"<td title='"+value.time+"'>"+mySubString(value.time, 15)+"</td>"
								+"<td style='color:red;'>"+value.money.toFixed(2)+"</td>"
								+"<td>"+value.billHead+"</td>"
								+"<td>"+value.recipient+"</td>"
								+"<td>"+value.phone+"</td>"
								+"<td title='"+value.address+"'>"+mySubString(value.address,10)+"</td>"
								+"<td>"+getDefaultData(value.expressNum,"暂无")+"</td>";
							
								temp = value.status;
							if(1 == temp){//审核中
								str = str + "<td style='color:#FF8247;'>"+value.statusDesc+"</td>";
							}else if(2 == temp){//开票成功
								str = str + "<td style='color:#9ACD32;'>"+value.statusDesc+"</td>";
							}else{//开票失败
								str = str + "<td style='color:red;'>"+value.statusDesc+"</td>";
							}
								str = str 
									+"<td title='"+getDefaultData(value.validRemark,"暂无")+"'>"+mySubString(getDefaultData(value.validRemark,"暂无"),10)+"</td>"
									+"<td>"+value.addTimeDesc+"</td>"
									+"</tr>";
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
	
