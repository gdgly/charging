var param={};
$(document).ready(function(){
	initParams();
	queryModelList();
});

function initParams(){
	initPagingToolbar(queryModelList);
	//初始化电桩型号列表查询按钮
	$("#queryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryModelList();
	});
	//初始化电桩型号列表导出按钮
	initExportBtn($("#exportBtn"),$("#conditionFrom"),$("#fileName").val());
}

function queryModelList(){
	param={};
	param.keyword = $.trim($("#keyword").val());
	param.chaway = $('#chaWay option:selected').val();
	param.chatype = $('#chaType option:selected').val();
	param.intftype = $('#intfType option:selected').val();
	param.modelstatus = $('#modelStatus option:selected').val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	param.tm = new Date().getTime();
	
	var tbody = $("#modelTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'device/pileModel_queryList.action',
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
        			 html += getTdHtml(item.brand, 25);
        			 html += '<td>'+getNotNullData(item.chaWayDesc)+'</td>';
        			 html += '<td>'+getNotNullData(item.chaTypeDesc)+'</td>';
        			 html += '<td>'+getNotNullData(item.standardDesc)+'</td>';
        			 html += '<td>'+getNotNullData(item.outV)+'</td>';
        			 html += '<td>'+getNotNullData(item.ratP)+'</td>';
        			 html += '<td>'+getNotNullData(item.statusDesc)+'</td>';
        			 html += '<td>';
    			     html += '<a href="device/editPileModelInit.action?modelid='+item.id+'" data-toggle="tooltip" title="修改"><span class="glyphicon glyphicon-edit"></span></a>';
        			 html += ' | ';
        			 html += '<a href="device/pileModelDetailInit.action?modelid='+item.id+'" data-toggle="tooltip" title="详细"><span class="glyphicon glyphicon-list-alt"></span></a>';
        			 html += '</td>';
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
	addModel();
});

function addModel(){
	href("device/addPileModelInit.action");
}

