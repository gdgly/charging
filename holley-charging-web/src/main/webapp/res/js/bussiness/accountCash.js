/**
 * 提现列表
 */
$(function(){
	var searchAccountCashsUrl="account_searchAccountCashsByAjax.action";
	var param={};
	var str;
	var item;
	function init(){
		$("#currentPage").val(1);
		$("#searchDate").val("");
	}
	function initStationBody(){
		param={};
		param.currentPage=$("#currentPage").val();
		param.searchDate = $("#searchDate").val();
		item =$("#accountCashTable").find("tbody");
		$.ajax({  
		      url:searchAccountCashsUrl,// 跳转到 action  
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
		    	  if(data.page.root.length > 0){
					$(data.page.root).each(function(index,value){
						str = "<tr>"
							+"<td>"+value.addTimeDesc+"</td>"
							+"<td>账户信息：<span>"+value.accountInfoDesc+"</span></td>"
							+"<td>审核信息：<span title='"+value.validRemarkDesc+"'>"+mySubString(value.validRemarkDesc,10)+"</span></td>"
							+"<td ><span style='color:coral;font-size:17px;'>"+value.moneyDesc+"</span></td>"     		         
							team = parseInt(value.validStatus);
						if(team == 1){
							str=str+"<td class='text-right'><span style='color:red;'>"+value.validStatusDesc2+"</span></td>";
						}
						else if(team == 2){
							str=str+"<td class='text-right'><span style='color:green;'>"+value.validStatusDesc2+"</span></td>";
						}
						else if(team == 3){
							str=str+"<td class='text-right'><span style='color:#aaa;'>"+value.validStatusDesc2+"</span></td>";
						}
						else{
							str=str+"<td class='text-right'>"+value.validStatusDesc2+"</td>";
						}
						str=str+"</tr>"; 
						item.append(str);

					});
					 $("#pagingToolbar").show();
		    	  }else{
		    		  $("#pagingToolbar").hide();
		    /*		  $("#searchKeyNameBtn").attr("disabled",true);
		    		  $("#exportBtn").attr("disabled",true);*/
		    		  item.append("<p class='help-block text-center' style='margin-top:10%;'>暂无记录</p>");  
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
		})
	};
	init();
	initPagingToolbar(initStationBody);
	initStationBody();
	initOneBootStrapDate2($("#datetime"));//初始化日常控件
	initExportBtn($("#exportBtn"),null,searchAccountCashsUrl,null,"提现记录","searchDate");
	$("#searchKeyNameBtn").on("click",function(){
		$("#currentPage").val(1)
		initStationBody();
	});
})

