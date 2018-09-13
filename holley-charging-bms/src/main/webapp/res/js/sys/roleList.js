var param={};
$(document).ready(function(){
	queryRoleList();
	//初始化查询按钮
	$("#queryBtn").on("click",function(){
		queryRoleList();
	});
	//初始化导出按钮
	initExportBtn($("#exportBtn"),$("#conditionFrom"),$("#fileName").val());
});

function queryRoleList(){
	param={};
	param.rolename = $.trim($("#roleName").val());
	param.roletype = $('#roleType option:selected').val();
	param.tm = new Date().getTime();
	
	var tbody = $("#roleTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'sys/role_queryList.action',
		data:param,
		dataType:'json',
        cache: false,
        success: function(data,options){
             if(data.success){
            	 tbody.empty();
            	 $(data.roleList).each(function(index,item){
            		 html += '<tr>';
            		 html += '<td>'+item.id+'</td>';
            		 html += '<td>'+item.rolename+'</td>';
            		 html += '<td>'+getNotNullData(item.remark)+'</td>';
            		 html += '<td>'+getNotNullData(item.typedesc)+'</td>';
            		 html += '<td>'+getNotNullData(item.statusdesc)+'</td>';
            		 html += '<td>'+getNotNullData(item.creatorname)+'</td>';
            		 html += '<td>'+getNotNullData(item.addTimeStr)+'</td>';
            		 html += '<td>';
            		 if(item.id == ADMIN_ROLE_ID){
            			 html += '<a data-toggle="tooltip" title="修改" class="a-disable"><span class="glyphicon glyphicon-edit"></span></a>';
            			 html += ' | ';
            			 html += '<a data-toggle="tooltip" title="删除" class="a-disable"><span class="glyphicon glyphicon-trash"></span></a>';
            			 html += ' | ';
            			 html += '<a data-toggle="tooltip" title="权限分配" class="a-disable"><span class="glyphicon glyphicon-lock"></span></a>';
            		 }else{
            			 html += '<a href="sys/editRoleInit.action?roleid='+item.id+'" data-toggle="tooltip" title="修改"><span class="glyphicon glyphicon-edit"></span></a>';
            			 html += ' | ';
            			 html += '<a data-toggle="tooltip" title="删除" onclick="showDelRole('+item.id+')"><span class="glyphicon glyphicon-trash"></span></a>';
            			 html += ' | ';
            			 html += '<a href="sys/permissionInit.action?roleid='+item.id+'" data-toggle="tooltip" title="权限分配"><span class="glyphicon glyphicon-lock"></span></a>';
            		 }
            		 html += '</td>';
            	 });
            	 tbody.html(html);
             }else{
            	 showMsg(data.message, data.errormsg);
             }
         }
     });
}

//新增按钮点击事件
$("#addBtn").on("click",function(){
	addRole();
});
function addRole(){
	href("sys/addRoleInit.action");
}
function showDelRole(id){
	roleid = id;
	var option = {
			title: "系统提示",
			btn: window.wxc.xcConfirm.btnEnum.okcancel,
			onOk: function(){
				delRole(id);
			}
		}
	showCustom("确定删除角色吗?",option);
}

function delRole(id){
	if (id == null) {
		alert("角色编码不能为空.");
		return false;
	}
	param.roleid = id;
	$.ajax({
		type:"POST",
		url:'sys/role_delRole.action',
		data:param,
		dataType:'json',
        cache: false,
        success: function(data,options){
             if(data.success){
//            	 showSuccess("角色已成功删除.");
            	 queryRoleList();
             }else{
            	 showMsg(data.message, data.errormsg);
             }
         }
     });
}




