var param={};
$(document).ready(function(){
	initParams();
	queryList();
});

function initParams(){
	initPagingToolbar(queryList);
	//初始化服务点列表查询按钮
	$("#queryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryList();
	});
	//初始化服务点列表导出按钮
	initExportBtn($("#exportBtn"),$("#conditionFrom"),$("#fileName").val());
}

function queryList(){
	param={};
	param.keyword = $.trim($("#keyword").val());
	param.isshow = $('#isShow option:selected').val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	param.tm = new Date().getTime();
	
	var tbody = $("#repairPointTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'device/repairPoint_queryList.action',
		data:param,
		dataType:'json',
        cache: false,
        success: function(data,options){
        	 if(data.page){
        		 tbody.empty();
        		 var dataList = data.page.root;
        		 $(dataList).each(function(index,item){
        			 html += '<tr>';
        			 html += '<td>'+item.id+'</td>';
        			 html += getTdHtml2('name',item.name, 15);
        			 html += getTdHtml2('address',item.address, 15);
        			 html += '<td>'+item.isShowDesc+'</td>';
        			 html += '<td>'+item.lng+'</td>';
        			 html += '<td>'+item.lat+'</td>';
        			 html += '<td>'+getNotNullData(item.tel)+'</td>';
        			 html += '<td>'+getNotNullData(item.phone)+'</td>';
        			 html += getTdHtml(item.workTime, 15);
        			 html += '<td>'+getNotNullData(item.addTimeDesc)+'</td>';
        			 html += '<td><a data-toggle="tooltip" title="修改" href="device/editRepairPointInit.action?requesttype='+REQUEST_TYPE_EDIT+'&id='+item.id+'"><span class="glyphicon glyphicon-edit"></span></a></td>';
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

//新增按钮点击事件
$("#addBtn").on("click",function(){
	href("device/editRepairPointInit.action?requesttype="+REQUEST_TYPE_ADD);
});

