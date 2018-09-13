var param={};
$(document).ready(function(){
	initParams();
	queryList();
});

function initParams(){
	initPagingToolbar(queryList);
	//初始化查询按钮
	$("#queryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryList();
	});
	//初始化导出按钮
	initExportBtn($("#exportBtn"),$("#conditionFrom"),$("#fileName").val());
}


function queryList(){
	param = {};
	param.keyword = $.trim($("#keyword").val());
	param.status = $("#ruleStatus option:selected").val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	var tbody = $("#ruleTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'sys/rule_queryList.action',
		data:param,
		dataType:'json',
        cache: false,
        success: function(data,options){
             if(data.success && data.page){
            	 tbody.empty();
            	 var dataList = data.page.root;
            	 var jsonstr;
            	 $(dataList).each(function(index,item){
            		 jsonstr = objToJsonString(item);
            		 jsonstr = jsonstr.replace(/"/g,"'");
            		 html += '<tr>';
            		 html += '<td>'+item.id+'</td>';
            		 html += getTdHtml(item.name, 20);
            		 html += getTdHtml(item.ruleCheck, 20);
            		 html += '<td>'+item.statusDesc+'</td>';
            		 html += '<td>'+item.addTimeStr+'</td>';
            		 html += '<td>';
            		 html += '<a href="sys/editRuleInit.action?id='+item.id+'" data-toggle="tooltip" title="修改"><span class="glyphicon glyphicon-edit"></span></a>';
            		 html += ' | ';
            		 html += '<a href="javascript:;" onclick="showRuleDetail('+jsonstr+')" data-toggle="tooltip" title="详细"><span class="glyphicon glyphicon-list-alt"></span></a>';
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
	addLink();
});

function addLink(){
	href("sys/addRuleInit.action");
}

//规则详细
function showRuleDetail(obj){
	$("#detail_id").text(obj.id);
	$("#detail_name").text(obj.name);
	$("#detail_status").text(obj.statusDesc);
	$("#detail_rulecheck").text(obj.ruleCheck);
	$("#detail_remark").text(getNotNullData(obj.remark));
	$("#detail_addtime").text(getNotNullData(obj.addTimeStr));
	showDetailResult();
}

//隐藏规则详细
function hideDetailResult(){
	$("#ruleListResult").css("display","inline");
	$("#ruleDetailResult").css("display","none");
	queryList();//刷新列表
}
//显示规则详细
function showDetailResult(){
	$("#ruleListResult").css("display","none");
	$("#ruleDetailResult").css("display","inline");
}







