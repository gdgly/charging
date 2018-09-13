/**
 * 个人预约
 */
	var param={};
	var userAppointmentUrl = "userjson/account_userAppointment.action";
/*	$("#exportBtn").on("click",function(){
		href(userAppointmentUrl+"?isExport=1");
	});*/
	
	initExportBtn($("#exportBtn"),null,userAppointmentUrl,null,"我的预约","searchDate");
	$(function(){
		function initBody(){
			param.currentPage=$("#currentPage").val();
			param.searchDate = $("#searchDate").val();
			param.userName = $("#userName").val();
			var item = $("#userAppointmentBody");
			$.ajax({
			      url:userAppointmentUrl,// 跳转到 action  
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
								str = str +"<td style='color:red;'>"+value.appFeeDesc+"</td>"
								+"<td style='color:green;'>"+value.appLen+"</td>";
								temp = value.payStatus;
							if(1 == temp){//未支付
								str = str + "<td style='color:#FF8247;'>"+value.payStatusDesc+"</td>";
							}else if(2 == temp){//支付中
								str = str + "<td style='color:#FF8247;'>"+value.payStatusDesc+"</td>";
							}else if(3 == temp){//支付成功
								str = str + "<td style='color:#9ACD32;'>"+value.payStatusDesc+"</td>";
							}else{//支付失败
								str = str + "<td style='color:red;'>"+value.payStatusDesc+"</td>";
							}
							temp = value.appStatus;//预约状态
							if(1 == temp){//预约中
								str = str + "<td style='color:#00c0ef;'>"+value.appStatusDesc+"</td>";
							}else if(2 == temp){//履约
								str = str + "<td style='color:#FF8247;'>"+value.appStatusDesc+"</td>";
							}else if(3 == temp){//取消
								str = str + "<td style='color:#FF8247;'>"+value.appStatusDesc+"</td>";
							}else if(4 == temp){//过时
								str = str + "<td style='color:#FF8247;'>"+value.appStatusDesc+"</td>";
							}else if(5 == temp){//已删除
								str = str + "<td style='color:#FF8247;'>"+value.appStatusDesc+"</td>";
							}else if(6 == temp){//被续约
								str = str + "<td style='color:#3c8dbc;'>"+value.appStatusDesc+"</td>";
							}else{//未知
								str = str + "<td>"+value.appStatusDesc+"</td>";
							}
								str = str 
								+ "<td>"+value.appNo+"</td>"
								+ "<td>"+value.addTimeDesc+"</td>"
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
	
