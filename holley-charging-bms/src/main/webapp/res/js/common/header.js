var currentTopMenu;
var currentSubMenu;
var currentBtn;
$(function(){
	createMenu();
	setTopMenuActive();
	
	$("#modifyPwdBtn").on("click",function(){
		$("#modifyPwdModal").modal().css({
			'margin-top': function () {
				return '10%';
			}
		});
		resetForm($("#pwdform"));
	});
	
	$("#savePwdBtn").on("click",function(){
		savePwd();
	});
	
});
/**
 * 创建菜单
 */
function createMenu(){
	var html = '';
	var topMenus = getMainMenu(modules);
	 $.each(topMenus, function(i, item){
		 // 构建一级菜单
		 html += '<li id="topMenu_'+item.moduleid+'" onclick="test()">';
		 html += '<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">'+item.modulename+'<span class="caret"></span></a>';
		 //构建二级菜单
		 var subMenus = getSubMenu(item.moduleid, modules);
		 if(subMenus != null && subMenus.length > 0){
			 html += '<ul class="dropdown-menu" style="padding-top: 0;padding-bottom: 0;">';
			 $.each(subMenus, function(j, subitem){
				 html += ' <li id="subMenu_'+subitem.moduleid+'"><a href="'+subitem.url+'">'+subitem.modulename+'</a></li>';
			 });
			 html += '</ul>';
		 }
		 html += '</li>';
	 });
	 $("#menu").html(html);
}

/**
 * 选中当前一级菜单
 */
function setTopMenuActive(){
	var thisURL = document.URL;
	var basepath = $("#basePath").text();
	var url = thisURL.replace(basepath, "");
	var index = url.indexOf("?");
	if(index > -1){
		url = url.split("?")[0]; 
	}
	$("#menu").find("li").removeClass("active nav-current");
	var subModule = getModuleByUrl(url, modules);
	if(subModule == null){
		currentBtn = getButtonByUrl(url, buttons);
		if(currentBtn != null){
			currentSubMenu = getModuledefByPrimaryKey(currentBtn.moduleid, modules);
		}
	}else{
		currentSubMenu = subModule;
	}
	if(currentSubMenu != null){
		$("#subMenu_"+currentSubMenu.moduleid).addClass("active");
		currentTopMenu = getParentModuleById(currentSubMenu.parentmoduleid, modules);
	}
	if(currentTopMenu != null){
		$("#topMenu_"+currentTopMenu.moduleid).addClass("active nav-current");
	}
	createBreadcrumb();
}

function createBreadcrumb(){
	if("02020000" == currentSubMenu.moduleid){
		$("#header_breadcrumb").hide();
		return;
	}
	var html = '';
	if(currentTopMenu != null){
		html += '<li class="active">'+currentTopMenu.modulename+'</li>';
	}
	if(currentSubMenu != null){
		html += '<li><a href="'+currentSubMenu.url+'">'+currentSubMenu.modulename+'</a></li>';
	}
	if(currentBtn != null){
		html += '<li class="active">'+currentBtn.buttonname+'</li>';
	}
	$("#header_breadcrumb").html(html);
}

/**
 * 查找一级菜单
 * @param list
 * @returns {Array}
 */
function getMainMenu(list){
	 var menuList = new Array();
	 $.each(list, function(i, item){
		 if (item.parentmoduleid == item.moduleid) {
             menuList.push(item);
         }
	    });
     return menuList;
}
/**
 * 查找二级菜单
 * @param list
 * @returns {Array}
 */
function getSubMenu(moduleid,list){
	var menuList = new Array();
	 $.each(list, function(i, item){
		 if (item.parentmoduleid != item.moduleid && item.parentmoduleid == moduleid) {
             menuList.push(item);
         }
	    });
     return menuList;
}
/**
 * 根据url查找菜单
 * @param url
 * @param list
 * @returns
 */
function getModuleByUrl(url,list){
	for(var i=0;i<list.length;i++){
		if(list[i].url != null && list[i].url == url){
			 return list[i];
		 }
	}
	 return null;
}
/**
 * 根据url查找按钮
 * @param url
 * @param list
 * @returns
 */
function getButtonByUrl(url,list){
	for(var i=0;i<list.length;i++){
		if(list[i].url != null && list[i].url == url){
			 return list[i];
		 }
	}
	 return null;
}
/**
 * 递归查找最顶层菜单
 * @param moduleid
 * @param list
 * @returns
 */
function getParentModuleById(moduleid,list){
	var module = getModuledefByPrimaryKey(moduleid, list);
	if(module == null)return null;
	if(module.moduleid != module.parentmoduleid){
		return getParentModuleById(module.parentmoduleid, list);
	}
	return module;
}

/**
 * 取当前id对应的模块
 * 
 * @param moduleid
 * @return
 */
function getModuledefByPrimaryKey(moduleid,list) {
    for (var i=0;i<list.length;i++) {
        if (moduleid == list[i].moduleid) {
            return list[i];
        }
    }
    return null;
}


function savePwd(){
	if(!verifyChangePwdForm())return;
	var param = {};
	param.oldpwd =  $.trim($("#oldpwd").val());
	param.newpwd = $.trim($("#newpwd").val());
	param.repeatpwd = $.trim($("#repeatpwd").val());
	$.post("user/login_changePwd.action",param,function(data){
		if(data.success){
			$("#modifyPwdModal").modal("hide");
			alert("密码修改成功.");
		}else{
			alert(data.message);
		}
	});
}

//验证密码
function ispassword(item){
    var passreg = /^[a-zA-Z0-9]{6,16}$/;
    if($(item).val()==""){
    	$(item).next().removeClass("hidden");
    }else if(!passreg.test($(item).val())){
    	$(item).next().removeClass("hidden");
    }else{
    	$(item).next().addClass("hidden");
    	$(item).val($.trim($(item).val()));
    }
}

//验证表单信息
function verifyChangePwdForm(){
    var passreg = /^[a-zA-Z0-9]{6,16}$/;
    if($("#oldpwd").val()=="" || !passreg.test($("#oldpwd").val())){
        $("#oldpwd").next().removeClass("hidden");
        return false;
    }else{
    	$("#oldpwd").next().addClass("hidden");
        $("#oldpwd").val($.trim($("#oldpwd").val()));
    }
    
    if($("#newpwd").val()=="" || !passreg.test($("#newpwd").val())){
        $("#newpwd").next().removeClass("hidden");
        return false;
    }else{
    	$("#newpwd").next().addClass("hidden");
        $("#newpwd").val($.trim($("#newpwd").val()));
    }
    
    if($("#repeatpwd").val()==""){
        $("#repeatpwd").next().removeClass("hidden");
        return false;
    }else if($("#newpwd").val()!=$("#repeatpwd").val()){
        $("#repeatpwd").next().removeClass("hidden");
        alert("确认密码输入不一致.");
        return false;
    }else{
    	 $("#repeatpwd").next().addClass("hidden");
         $("#repeatpwd").val($.trim($("#repeatpwd").val()));
    }
    return true
}

