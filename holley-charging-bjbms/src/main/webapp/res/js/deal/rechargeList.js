var param={};
$(document).ready(function(){
	initParams();
	queryList();
});

function initParams(){
	initStartEndDate($('#startDateDiv'),$('#endDateDiv'));
	initPagingToolbar(queryList);
	//初始化充值记录查询按钮
	$("#queryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryList();
	});
	$("#rechargeStatus").on("change",function(){
		$("#queryBtn").click();
	});
	$("#payWay").on("change",function(){
		$("#queryBtn").click();
	});
	$("#resetBtn").on("click",function(){
		resetForm($("#conditionFrom"));
		$("#queryBtn").click();
	});
	//初始化充值记录导出按钮
	initExportBtn($("#exportBtn"),$("#conditionFrom"),$("#fileName").val(),$("#rechargeTable"));
}

function queryList(){
	param={};
	param.startdate =  $.trim($("#startDate").val());
	param.enddate =  $.trim($("#endDate").val());
	param.searchtradeno = $.trim($("#searchTradeno").val());
	param.searchuser = $.trim($("#searchUser").val());
	param.status = $('#rechargeStatus option:selected').val();
	param.payway = $('#payWay option:selected').val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	param.tm = new Date().getTime();
	
	var tbody = $("#rechargeTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'deal/recharge_queryList.action',
		data:param,
		dataType:'json',
        cache: false,
        beforeSend:function(){$("#loading").removeClass("hide");},
        complete:function(){
	    	  $("#loading").addClass("hide"); 
	      },
        success: function(data,options){
             if(data.page){
            	 tbody.empty();
            	 var dataList = data.page.root;
            	 $(dataList).each(function(index,item){
            		 html += '<tr>';
            		/* html += '<td>'+item.id+'</td>';*/
            		 html += '<td>'+item.tradeNo+'</td>';
            		 html += '<td>'+item.phone+'</td>';
            		 html += '<td>'+item.statusDesc+'</td>';
            		 html += '<td>'+item.moneyDesc+'</td>';
            		 html += '<td>'+item.payWayDesc+'</td>';
            		 html += getTdHtml(item.accountInfo,20);
            		 html += '<td>'+item.addTimeDesc+'</td>';
            		 html += '<td>'
            		 html += '<a href="deal/rechargeDetailInit.action?rechargeid='+item.id+'">详细</a>';
            		 html += '</td>'
            	 });
            	 tbody.html(html);
            	 var currentPage = data.page.startRow/(data.page.endRow-data.page.startRow)+1;
            	 var totalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
            	 setPagingToolbarParams(data.page.totalProperty, totalPage, currentPage);
             }else{
            	 showMsg(data.message, data.errormsg);
             }
         }
     });
}




