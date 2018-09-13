var param={};
var sysRole;
var msg;
$(document).ready(function(){
	loadPermission();
});

//载入功能权限
function loadPermission(){
	param={};
	param.roleid = roleid;
	param.tm = new Date().getTime();
	
	$.ajax({
		type:'POST',
		url:'sys/role_loadPermission.action',
		data:param,
		dataType:'json',
		cache:false,
		success:function(data,options){
			if(data.success){
				if(data.treeNodeList != null && data.treeNodeList.length > 0){
					$('#permissiontree').tree({  
						checkbox: true,  
						animate:true,  
						lines:true,  
						data:data.treeNodeList
					});  
				}
			}
		}
	});
}

function save(){
	var modules = getChecked();
	if(modules == null || modules.length == 0){
		showInfo("请选择功能模块.");
		return;
	}
	param = {};
	param.roleid = roleid;
	param.modules = modules;
	param.tm = new Date().getTime();
	$.ajax({
		type:'POST',
		url:'sys/role_savePermission.action',
		data:param,
		dataType:'json',
		cache:false,
		success:function(data,options){
			if(data.success){
				gobackAndReload();
			}else{
				showMsg(data.message, data.errormsg);
			}
		}
	});
}

function getChecked(){
    var arr = [];
    var checkeds = $('#permissiontree').tree('getChecked', 'checked');
    for (var i = 0; i < checkeds.length; i++) {
        arr.push(checkeds[i].id);
    }
    return arr.join(',');
}








