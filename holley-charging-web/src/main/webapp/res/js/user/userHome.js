//首页
/**
 * 消费明细
 */

$(function(){
	var userConsumeUrl = "userjson/personal_userConsume.action";
	var param={};
	function initBody(){
		param.currentPage=$("#currentPage").val();
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
							str = str
									+"<td class='outFeeColor'>-"+value.fee.toFixed(2)+"</td>"
									+"<td>"+value.datatimeDesc+"</td>"
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
	initPagingToolbar(initBody);
	initBody();
})