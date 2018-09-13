var param={};
$(document).ready(function(){
	initUserParams();
	queryUserList();
});

function initUserParams(){
	initPagingToolbar(queryUserList);
	//初始化查询按钮
	$("#queryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryUserList();
	});
	//初始化导出按钮
	initExportBtn($("#exportBtn"),$("#conditionFrom"),$("#fileName").val());
}

function queryUserList(){
	param={};
	param.keyword = $.trim($("#keyword").val());
	param.usertype = $('#userType option:selected').val();
	param.realstatus = $('#realStatus option:selected').val();
	param.lockstatus = $('#lockStatus option:selected').val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	param.tm = new Date().getTime();
	
	var tbody = $("#userTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'sys/user_queryList.action',
		data:param,
		dataType:'json',
        cache: false,
        success: function(data,options){
             if(data.success){
            	 if(data.page){
            		 tbody.empty();
            		 var dataList = data.page.root;
            		 $(dataList).each(function(index,item){
            			 html += '<tr>';
            			 html += '<td>'+item.id+'</td>';
            			 html += getTdHtml(item.username, 15);
            			 html += '<td>'+getNotNullData(item.phone)+'</td>';
            			 html += '<td>'+getNotNullData(item.realStatusDesc)+'</td>';
            			 html += '<td>'+getNotNullData(item.userTypeDesc)+'</td>';
            			 html += '<td>'+item.rolename+'</td>';
            			 html += '<td>'+getNotNullData(item.registTimeStr)+'</td>';
            			 html += '<td>';
            			 if(item.userType == PLATFORM && item.id != ADMIN_USER_ID && item.isLock == 2){
            				 var params = item.id+",'"+item.username+"'";
        					 html += '<a href="sys/editUserInit.action?userid='+item.id+'" data-toggle="tooltip" title="修改"><span class="glyphicon glyphicon-edit"></span></a>';
        					 html += ' | ';
        					 html += '<a data-toggle="tooltip" title="删除" onclick="showDelUserBox('+params+')"><span class="glyphicon glyphicon-trash"></span></a>';
        					 html += ' | ';
        					 html += '<a data-toggle="tooltip" title="密码初始化" onclick="showInitPwdBox('+params+')"><span class="glyphicon glyphicon-lock"></span></a>';
            			 }else {
            				 html += '<a data-toggle="tooltip" title="修改" class="a-disable"><span class="glyphicon glyphicon-edit"></span></a>';
        					 html += ' | ';
        					 html += '<a data-toggle="tooltip" title="删除" class="a-disable"><span class="glyphicon glyphicon-trash"></span></a>';
        					 html += ' | ';
        					 html += '<a data-toggle="tooltip" title="密码初始化" class="a-disable"><span class="glyphicon glyphicon-lock"></span></a>';
            			 }
            			 html += ' | ';
            			 html += '<a href="sys/userDetailInit.action?userid='+item.id+'&usertype='+item.userType+'" data-toggle="tooltip" title="详细"><span class="glyphicon glyphicon-list-alt"></span></a>';
            			 html += '</td>';
            			 html += '</tr>';
            		 });
            		 tbody.html(html);
            		 
            		 var currentPage = data.page.startRow/(data.page.endRow-data.page.startRow)+1;
            		 var totalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
            		 setPagingToolbarParams(data.page.totalProperty, totalPage,currentPage);
            	 }
             }else{
            	 showMsg(data.message, data.errormsg);
             }
         }
     });
}

//新增按钮点击事件
$("#addBtn").on("click",function(){
	addUser();
});

function addUser(){
	href("sys/addUserInit.action");
}

function showInitPwdBox(id,username){
	var option = {
			title: "系统提示",
			btn: window.wxc.xcConfirm.btnEnum.okcancel,
			onOk: function(){
				initPwd(id,username);
			}
		}
	showCustom("确定初始化用户'"+username+"'的密码吗?",option);
}

function initPwd(id,username){
	if (id == null) {
		showInfo("参数非法.");
		return false;
	}
	param={};
	param.userid = id;
	$.ajax({
		type:"POST",
		url:'sys/user_initPassword.action',
		data:param,
		dataType:'json',
        cache: false,
        success: function(data,options){
             if(data.success){
            	 showSuccess("用户'"+username+"'密码初始化成功.");
             }else{
            	 showMsg(data.message, data.errormsg);
             }
         }
     });
}

function showDelUserBox(id,username){
	var option = {
			title: "系统提示",
			btn: window.wxc.xcConfirm.btnEnum.okcancel,
			onOk: function(){
				delUser(id,username);
			}
		}
	showCustom("用户删除后不能正常登录系统,确定删除用户'"+username+"'吗?",option);
}

function delUser(id,username){
	if (id == null) {
		showInfo("参数非法.");
		return false;
	}
	param.userid = id;
	$.ajax({
		type:"POST",
		url:'sys/user_delUser.action',
		data:param,
		dataType:'json',
        cache: false,
        success: function(data,options){
             if(data.success){
            	 queryUserList();
             }else{
            	 showMsg(data.message, data.errormsg);
             }
         }
     });
}




