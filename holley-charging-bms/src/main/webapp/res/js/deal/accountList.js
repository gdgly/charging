var param={};
$(document).ready(function(){
	initParams();
	queryAccountList();
});

function initParams(){
	initPagingToolbar(queryAccountList);
	initPagingToolbar2(queryFreezeList);
	//初始化账户列表查询按钮
	$("#queryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryAccountList();
	});
	//初始化账户列表导出按钮
	initExportBtn($("#exportBtn"),$("#accountFrom"),$("#fileName").val());
}

function queryAccountList(){
	param={};
	param.keyword = $.trim($("#keyword").val());
	param.usertype = $('#userType option:selected').val();
	param.accountstatus = $('#accountStatus option:selected').val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	param.tm = new Date().getTime();
	
	var tbody = $("#accountTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'deal/account_queryList.action',
		data:param,
		dataType:'json',
        cache: false,
        success: function(data,options){
        	 if(data.page){
        		 tbody.empty();
        		 var dataList = data.page.root;
        		 $(dataList).each(function(index,item){
        			 html += '<tr>';
        			 html += '<td>'+item.userId+'</td>';
        			 html += getTdHtml(item.username, 15);
        			 html += '<td>'+getNotNullData(item.phone)+'</td>';
        			 html += '<td>'+getNotNullData(item.userTypeDesc)+'</td>';
        			 html += '<td class="text-right">'+item.totalMoneyDesc+'</td>';
        			 html += '<td class="text-right">'+item.usableMoneyDesc+'</td>';
        			 html += '<td class="text-right">';
        			 html += '<a href="javascript:;" onclick="queryFreezeDetail('+item.userId+')">'+item.freezeMoneyDesc+'</a>';
        			 html += '</td>';
        			 html += '<td>'+getNotNullData(item.statusDesc)+'</td>';
        			 html += '<td>'+getNotNullData(item.updateTimeStr)+'</td>';
        			 if(item.userType == PERSION){
        				 html += '<td><a href="deal/manualRechargeInit.action?userid='+item.userId+'">充值</a></td>';
        			 }else{
        				 html += '<td><a class="a-disable">充值</a></td>';
        			 }
        			 html += '</tr>';
        		 });
        		 tbody.html(html);
        		 
        		 var currentPage = data.page.startRow/(data.page.endRow-data.page.startRow)+1;
        		 var totalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
        		 setPagingToolbarParams(data.page.totalProperty, totalPage,currentPage);
        	 }else{
            	 showMsg(data.message, data.errormsg);
             }
         }
     });
}

var userid;
function queryFreezeDetail(id){
	userid = id;
	showFreezeData();
	setPagingToolbar2Params(0, 0, 1);
	queryFreezeList();
}

function queryFreezeList(){
	param = {};
	param.userid = userid;
	param.pageindex = $.trim($("#currentPage2").val());
	param.pagelimit = PAGE_LIMIT;
	param.tm = new Date().getTime();
	var tbody = $("#freezeTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'deal/account_queryFreezeList.action',
		data:param,
		dataType:'json',
        cache: false,
        success: function(data,options){
        	 if(data.page){
        		 tbody.empty();
        		 var dataList = data.page.root;
        		 $(dataList).each(function(index,item){
        			 html += '<tr>';
        			 html += '<td>'+item.addTimeDesc+'</td>';
        			 html += '<td>'+getNotNullData(item.cashWayDesc)+'</td>';
        			 html += '<td>'+getNotNullData(item.accountInfo)+'</td>';
        			 html += '<td>'+getNotNullData(item.validStatusDesc)+'</td>';
        			 html += '<td class="text-right">'+item.money.toFixed(2)+'</td>';
        			 html += '</tr>';
        		 });
        		 tbody.html(html);
        		 
        		 var currentPage = data.page.startRow/(data.page.endRow-data.page.startRow)+1;
        		 var totalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
        		 setPagingToolbar2Params(data.page.totalProperty, totalPage,currentPage);
        		 
        		 if(data.freezetotal){
        			 $("#freezetotal").text('冻结金额合计： '+data.freezetotal+' 元');
        		 }else{
        			 $("#freezetotal").text('冻结金额合计： 0.00  元');
        		 }
        		 
        		
        	 }else{
            	 showMsg(data.message, data.errormsg);
             }
         }
     });
}

function hideFreezeData(){
	$("#accountResult").css("display","inline");
	$("#cashResult").css("display","none");
}

function showFreezeData(){
	$("#accountResult").css("display","none");
	$("#cashResult").css("display","inline");
}






