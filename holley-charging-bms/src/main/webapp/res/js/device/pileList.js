var param={};
$(document).ready(function(){
	initParams();
	queryPileList();
});

function initParams(){
	initPagingToolbar(queryPileList);
	//初始化充电桩列表查询按钮
	$("#pileQueryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryPileList();
	});
	//初始化充电桩列表导出按钮
	initExportBtn($("#pileExportBtn"),$("#conditionFrom"),$("#pileFileName").val());
}

function queryPileList(){
	param={};
	param.keyword = $.trim($("#keyword").val());
	param.piletype = $("#pileType option:selected").val();
	param.chaway = $("#chaWay option:selected").val();
	param.intftype = $("#intfType option:selected").val();
	param.payway = $("#payWay option:selected").val();
	param.comtype = $("#comType option:selected").val();
	param.pilestatus = $("#pileStatus option:selected").val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	param.tm = new Date().getTime();
	
	var tbody = $("#pileTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'device/pile_queryPileList.action',
		data:param,
		dataType:'json',
        cache: false,
        success: function(data,options){
             if(data.page){
            	 tbody.empty();
            	 var dataList = data.page.root; 
            	 $(dataList).each(function(index,item){
            		 temp = item.busType;
            		 html += '<tr>';
            		 html += '<td>'+item.id+'</td>';
            		 html += '<td>'+item.pileCode+'</td>';
            		 html += getTdHtml(item.pileName, 15);
            		 html += getTdHtml(item.stationName, 15)
            		 html += '<td>'+item.pileTypeDesc+'</td>';
            		 html += '<td>'+item.chaWayDesc+'</td>';
            		 html += '<td>'+item.intfTypeDesc+'</td>';
            		 html += '<td>'+item.payWayDesc+'</td>';
            		 html += '<td>'+item.comTypeDesc+'</td>';
            		 html += '<td>'+getNotNullData(item.comAddr)+'</td>';
            		 html += '<td>'+item.isAppDesc+'</td>';
            		 html += '<td>'+item.statusDesc+'</td>';
            		 html += '<td>'+getNotNullData(item.updateTimeStr)+'</td>';
            		 html += '<td>';
            		 html += '<a class="a-blue" href="device/pileDetail.action?id='+item.id+'">详细</a> | ';
            		 if(temp==1){
            			 html +='<a class="a-blue" href="device/editPile.action?pileId='+item.id+'">修改</a>'; 
            		 }else{
            			 html +='<span style="color:gray;">修改</span>'; 
            		 }
            		 html +='</td>';
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
