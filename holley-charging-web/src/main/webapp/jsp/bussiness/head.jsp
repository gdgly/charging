<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<%@ include file="../common/global/top.jsp"%>
<link rel="stylesheet" type="text/css" href="res/css/bussiness/common.css">
<style type="text/css">
.nav .open > a, .nav .open > a:focus, .nav .open > a:hover {
    background-color: #1d2f3b;
    border-color: #1d2f3b;
   color:white;
}
#headDiv >a,#headDiv >a:hover,#headDiv >a:focus{
text-decoration: none;
  
}
#headDiv >a:hover{
 color:white;
}
  
</style>
<div id="loading" class="modal-backdrop fade in hide" style="background-color:#ddd;">
<div class="loading">
loading...
</div>
</div>

	<!-- 导航栏 -->
<!-- 	<div class="welcome">
		<div class="container">
			<div class="col-sm-2">欢迎访问爱车易充</div>
			<div class="col-sm-offset-8 col-sm-2 text-right">
				<a href="monitor/monitorMap.action" style="color: #fff;">运行监视 </a> |
				<a href="deviceManager/addStation.action" style="color: #fff;">新增设备</a>
				<a href="user/userlogin_logout.action" style="color: #fff;">退出</a>
			</div>
		</div>
	</div> -->
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="container" id="topMenu">
				<div class="navbar-header" style="padding-top: 10px;">
					<img class="logo"
						src="${imgUrl}res/img/bussiness/logo.png" style="cursor: pointer;" onclick="href('home/homepage.action')"/>
					<button type="button" class="navbar-toggle" data-toggle="collapse"
						data-target="#example-navbar-collapse">
						<span class="sr-only">切换导航</span> 
						<span class="icon-bar"></span>
						<span class="icon-bar"></span> 
						<span class="icon-bar"></span>
					</button>
				</div>
<div class="collapse navbar-collapse" id="example-navbar-collapse">
	<ul class="nav navbar-nav navbar-right" id="mainMenu" style="background-color:#1d2f3b;">
						<s:iterator value="#request.topMenu" id="item" status="statu">
							<li id=<s:property value="note"/>><a
								href=<s:property value="url"/>><s:property
										value="modulename" /></a></li>
						</s:iterator>
		
	<li class="dropdown" style="width: 180px;height: 50px;">
		<div id="headDiv" style="padding-top: 10px;padding-left: 25px;">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown" style="height: 100%;">
            	<img  class="img-circle" src="${imgUrl}${webUser.headImg}" style="height: 30px;width: 30px;" />
            			${webUser.userName}<b class="caret"></b>
           	 </a>
						<ul class="dropdown-menu" style="width: 180px;margin-top: 10px;">
							<li class="text-center" id="changeHeadImgBtn">
							<a href="javascript:;">
							修改头像
							</a>
							</li>
							<li class="divider"></li>
							<li class="text-center"><a href="user/userlogin_logout.action">注销</a></li>
						</ul>
		</div>
	</li>
	</ul>
</div>
</div>
</div>
	<div class="box-shadow2" id="subMenuBox" style="position: fixed; top: 70px;z-index:1001;">
		<div class="container">
			<div id="subMenu" style="padding-bottom: 5px; padding-top: 5px;">
				<s:iterator value="#request.topMenu" id="item1" status="statu1">
					<ul class="hide nav nav-pills"
						id=<s:property value="'sub'+#item1.moduleid"/>>
						<s:iterator value="#request.subMenu" id="item2" status="statu2">
							<s:if test="#item1.moduleid == #item2.parentmoduleid">
								<li id=<s:property value="note"/> >
									<a href=<s:property value="url"/> ><s:property value="modulename"/> </a>
								</li>
							</s:if>
						</s:iterator>
					</ul>
				</s:iterator>
			</div>
		</div>
	</div>
		<!-- 模态框（Modal） -->
	<div class="modal fade" id="changeHeadImgModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">修改头像</h4>
				</div>
				<div class="modal-body">

					<form id="imform" enctype="multipart/form-data">
						<div class="row">
							<div class="col-sm-4">
								<div>
									<img id="showHeadImg" class="img-circle" style="width: 100px;height: 100px;"
										src="${imgUrl}data/stationImg/stationDefault.jpg"
										alt="Image preview" />
								</div>
								<div style="padding-left:10px;padding-top:5px;">
									<a href="javascript:;" class="file">选择图片 <input type="file" name="headImg" id="changeHeadImg">
									</a>
								</div>
							</div>
							<div class="col-sm-6">
							<div class="form-group">
								<label style="padding-top:6px;" for="username" class="col-sm-4 control-label">昵称：</label>
								<div class="col-sm-8">
									<input type="text" id="username" class="form-control"
										name="username"
										value="<s:property value='#request.webUser.userName'/>" maxlength="20"/>
								</div>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭
					</button>
					<button id="editImgBtn" type="button" class="btn btn-primary">
						提交更改</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal -->
	</div>
<script type="text/javascript">
	
/**
 * 创建菜单
 */
$(function(){
	//修改头像 start
	commomChangeImg("changeHeadImg","showHeadImg");
	var oldHeadImgSrc = $("#showHeadImg").attr("src");
	$("#changeHeadImgBtn").on("click",function(){
		resetForm($("#imform"));
		$("#changeHeadImgModal").modal().css({
			'margin-top': function () {
				return "15%";
			}
		});
		
	});
	$('#changeHeadImgModal').on('hide.bs.modal', function () {
		$("#showHeadImg").attr("src",oldHeadImgSrc);
		})
		
		
	$('#editImgBtn').on('click',function(){
		$("#changeHeadImgModal").modal("hide");
		$('#imform').ajaxSubmit({
			url:"bussiness_editHeadImgByAjax.action",
			type:'post',
			dataType:'json',
			beforeSubmit:function(){$("#loading").removeClass("hide")},
			success:function(data){
				$("#loading").addClass("hide");
				if("success" == data.message){
					reload();
				}
				else{
					showWarning(data.message);
				}

			}
		}); 
	})
	//修改头像 end
	function initMainMenu(){
		$("#mainMenu").find("li").each(function(index,data){
			$(data).removeClass("active");
		});
	}

	function initSubMenu(){
		$("#subMenu").find("ul").each(function(index,data){
			$(data).addClass("hide");
			$(data).find("li").each(function(index,data){
				$(data).removeClass("active");
			});
		});
	}
	initMainMenu();
	initSubMenu();
	var actionName = "${actionName}";
	var topMenu = ${jsonTopMenu};
	var subMenu = ${jsonSubMenu};
	var moduleButton = ${jsonUserModuleButton};
	var menu=[];
	var parentModulId = 0;
	var subModulId = 0;
	var tempId;
	for(var i in subMenu){
		if(subMenu[i].url == actionName){
			parentModulId = subMenu[i].parentmoduleid;
			subModulId = subMenu[i].moduleid;
			break;
		}
	}
	if(parentModulId == 0){
		for(var i in moduleButton){
		if(moduleButton[i].url == actionName){
			subModulId = moduleButton[i].moduleid;
			break;
		}
		}
		if(subModulId > 0){
			for(var x in subMenu){
				if(subMenu[x].moduleid == subModulId){
					parentModulId = subMenu[x].parentmoduleid;
					break;
				}
			}
		}
	
	}
	for(var i in subMenu){
		if(subMenu[i].parentmoduleid == parentModulId){
			menu.push(subMenu[i]);	
		}
	}
	for(var i in topMenu){
		if(topMenu[i].moduleid == parentModulId){
			$("#"+topMenu[i].note).addClass("active");
			break;
		}
	}
	
			if(subModulId > 0){
				for(var i in menu){
					if(menu[i].moduleid == subModulId){
						if(menu[i].parentmoduleid == '23000000'){
							$("#subMenuBox").addClass("hide");
						}
						else{
						tempId = "#"+menu[i].note;
					 	$(tempId).addClass("active");
					/*  	if(menu[i].parentmoduleid == '21000000'){
					 		$("#blockImg").removeClass("hide");
					 	} */
					 	
					 	$("#sub"+parentModulId).removeClass("hide");
						}
					 	break;
					}
				}
			}
			

 	/**
 	 * 设备添加
 	 */
 	$("#addDeviceBtn").on("click",function(){
 		window.document.location.href = "deviceManager/addStation.action";
 	});
 	
});
 
</script>
