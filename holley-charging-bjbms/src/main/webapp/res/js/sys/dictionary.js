var param={};
$(document).ready(function(){
	initParams();
	queryLinkList();
});
function initParams(){
	initPagingToolbar(queryLinkList);
	//初始化查询按钮
	$("#queryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryLinkList();
	});
	//初始化导出按钮
	initExportBtn($("#exportBtn"),$("#conditionFrom"),$("#fileName").val());
}


function queryLinkList(){
	param = {};
	param.linktype = $('#linkType option:selected').val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	var tbody = $("#linkTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'sys/dic_queryLinkList.action',
		data:param,
		dataType:'json',
        cache: false,
        success: function(data,options){
             if(data.success && data.page){
            	 tbody.empty();
            	 var dataList = data.page.root;
            	 $(dataList).each(function(index,item){
            		 html += '<tr>';
            		 html += '<td>'+item.id+'</td>';
            		 html += '<td>'+item.name+'</td>';
            		 html += '<td>'+item.value+'</td>';
            		 html += '<td>'+item.typeDesc+'</td>';
//            		 html += '<td><a href="sys/editLinkInit.action?id='+item.id+'">修改</a></td>';
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
	addLink();
});

function addLink(){
	href("sys/addLinkInit.action");
}






