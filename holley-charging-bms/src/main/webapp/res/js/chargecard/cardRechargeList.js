/**
 * 读取充电卡信息
 */
var param={};
var temp;
var showCardRechargeListUrl = "person/cardManager_showCardRechargeList.action";

$(document).ready(function(){
	initParams();
	queryCardRechargeList();
});


function initParams(){
	$("#queryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryCardRechargeList();
	});
	initPagingToolbar(queryCardRechargeList);
}


function queryCardRechargeList(){
	param={};
	param.keyword = $.trim($("#keyword").val());
	param.pageindex = $.trim($("#currentPage").val());
	var tbody = $("#cardRechargeTbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:showCardRechargeListUrl,
		data:param,
		dataType:'json',
        cache: false,
        success: function(data){
        	$("#doRechargeBtn").attr("disabled",false);
        	if(data.page){
        		 	tbody.empty();
        		 	 if(data.page.root.length > 0){
        			 $(data.page.root).each(function(index,item){
        				 html += '<tr>';
        				 html += '<td>'+getNotNullData(item.realName)+'</td>';
        				 html += '<td>'+getNotNullData(item.phone)+'</td>';
        				 html += '<td>'+getNotNullData(item.userCardNo)+'</td>';
        				 /*html += getTdHtml(item.tradeNo, 2);*/
        				 html += '<td>'+getNotNullData(item.cardNo)+'</td>';
        				 html += '<td>'+getNotNullData(item.plateNo)+'</td>';
        				 html += '<td>'+getNotNullData(item.tradeNo)+'</td>';
        				 html += '<td>'+item.money+'</td>';
        				 html += '<td>'+getNotNullData(item.addTimeDesc)+'</td>';
        				 html += '</tr>';
        			 });
        		 	 $("#noListMsg").remove();
        			 tbody.html(html);
        			 var currentPage = data.page.startRow/(data.page.endRow-data.page.startRow)+1;
                	 var totalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
                	 	 setPagingToolbarParams(data.page.totalProperty, totalPage,currentPage);
        		 	 }else{
            			 if(!$("#noListMsg").attr("id")){
             				$("#userTableDiv").append('<p id="noListMsg" class="text-center" style="margin-top: 50px;">暂无充值信息</p>'); 
             			 }
             		 }
				
        	}else{
        		showMsg(data.message, data.errormsg);
        	}
         },
         error:function(){
        	 showInfo("系统异常！！");
         }
     });
}
