/**
 * 提现列表
 */
$(function(){
	var searchAccountCashsUrl="${webroot}/monitor_searchAllOnlinePileListByAjax.action";
	var param={};
	var str;
	var item;
	function initStationBody(){
		param={};
		param.currentPage=$("#currentPage").val();
		item =$("#accountCashTable").find("tbody");
		$.post(searchAccountCashsUrl,param,function(data){
			  if(data.userLoginStatus){
	    		  checkLoginStatus(data,true);
	    		  return;
	    	  }
			//$("#accountCashCurrentPage").val(data.page.startRow/(data.page.endRow-data.page.startRow)+1);
			item.empty();
			$(data.page.root).each(function(index,value){
				str = "<tr>"
					+"<td>"+getFormatDate(value.addTime)+"</td>"
					+"<td>-"+parseFloat(value.money).toFixed(2)+"</td>"     		         
					team = parseInt(value.validStatus);
				if(team == 1){
					str=str+"<td>"+'审核中'+"</td>";
				}
				else if(team == 2){
					str=str+"<td>"+'审核通过'+"</td>";
				}
				else if(team == 3){
					str=str+"<td>"+'审核未通过'+"</td>";
				}
				else{
					str=str+"<td>"+'未知'+"</td>";
				}
				str=str+"</tr>"; 
				item.append(str);//封装tableBody

			});
			var stationTotalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
			setPagingToolbarParams(data.page.totalProperty,stationTotalPage,$("#currentPage").val());
		});
	};
	/*initPagingToolbar(initStationBody);
	initStationBody();*/
})

