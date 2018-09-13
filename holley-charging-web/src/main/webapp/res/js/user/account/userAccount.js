//个人收支明细
$(function(){
	goUserMenu("userAccount");
	var userDealUrl = "userjson/account_userDeal.action";
	var param={};
	function initBody(){
		param.currentPage=$("#currentPage").val();
		param.searchDate = $("#searchDate").val().trim();
		param.dealType=$("#dealType").val();
		var item = $("#userDealBody");
		$.ajax({  
		      url:userDealUrl,// 跳转到 action  
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
						temp = value.direction;
						if(temp == 1){//收入
							str = str
							+"<td style='color:#CD2626;'>+"+value.fee.toFixed(2)+"</td>"
						}else if(temp == 2){//支出
							str = str
							+"<td style='color:#CD2626;'>-"+value.fee.toFixed(2)+"</td>"
						}else{//不变
							str = str
							+"<td style='color:#CD2626;'>+"+value.fee.toFixed(2)+"</td>"	
						}
						str = str+"<td>"+value.datatimeDesc+"</td>"
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
	initOneBootStrapDate2($('#datepicker'));
	initPagingToolbar(initBody);
	initBody();
	$("#searchBtn").on("click",function(){
		initBody();
	});
})