/**
 * 集团子账户列表
 */
	var param={};
	var groupUserUrl = "userjson/personal_groupUser.action";//查询集团子账户
	var editGroupUserUrl = "userjson/personal_editGroupUser.action";//修改集团子账户
	var addGroupUserUrl = "userjson/personal_addGroupUser.action";//添加集团子账户
	var tempGroupUserPhone;
	initExportBtn($("#exportBtn"),null,groupUserUrl,null,"集团子账户",null);
	function editGroupUser(obj){
		$("#saveGroupUserBtn").attr("disabled",true);
		groupUserId = $(obj).attr("groupUserId");
		groupUserPhone = $(obj).attr("groupUserPhone");
		tempGroupUserPhone = groupUserPhone;
		groupUserName = $(obj).attr("groupUserName");
		if(isNaN(groupUserId) || groupUserId <= 0){
			showWarning("操作有误！！");
			return false;
		}
		$("#groupUserName").val(groupUserName).attr("disabled",true);
		$("#groupUserId").val(groupUserId);
		$("#groupUserPhone").val(groupUserPhone);
		$("#addOrEditGroupUserTitle").text("修改用户");
		$("#groupUserPage").addClass("hide");
		$("#addOrEditGroupUserPage").removeClass("hide");
	}
	$(function(){
		function initBody(){
			param.currentPage=$("#currentPage").val();
			param.userName = $("#userName").val();
			var item = $("#groupUserBody");
			$.ajax({
			      url:groupUserUrl,// 跳转到 action  
			      data:param,  
			      type:'post',  
			      cache:false,  
			      dataType:'json', 
			      beforeSend:function(){$("#loading").removeClass("hide");},
			      success:function(data){
			    	  if(data.userLoginStatus){
			    		  checkLoginStatus(data,false);
			    		  return;
			    	  }
						item.empty();
						if(data.page.root.length > 0){
							$("#help-block").remove();
						$(data.page.root).each(function(index,value){
								str = "<tr>"
									 	+"<td>"+value.username+"</td>"
										+"<td>"+value.phone+"</td>";
										temp = value.isLock;
										if(temp == 1){
											str = str +"<td style='color:red;'>"+value.isLockDesc+"</td>";
										}else{
											str = str +"<td style='color:green;'>"+value.isLockDesc+"</td>";
										}
										str = str+"<td>"+value.registTimeStr+"</td>"
												 +"<td>"
												 +"<span title='修改'"+" groupUserId='"+value.id+"' groupUserPhone='"+value.phone+"' groupUserName='"+value.username+"'class='fa fa-edit' style='color:#337ab7;cursor:pointer;' onclick='editGroupUser(this);'></span>"
												 +"</td>"
												 +"</tr>";
								/*if(value.isLock == 1){
									str = str
									+"<span title='锁定' class='fa fa-unlock' style='color:#337ab7;cursor:pointer'></span>";
								}else{
									str = str
									+"<span title='解锁' class='fa fa-lock' style='color:#337ab7;cursor:pointer'></span>";
								}*/
										
							item.append(str);//封装table
						});
						$("#pagingToolbar").show();
						}else{
							 $("#pagingToolbar").hide();
							 if(!$("#help-block").attr("id")){
								 item.parent().parent().append("<p id='help-block' class='help-block text-center' style='margin-top:10%;'>暂无记录</p>");
							 }
				    		
						}
						var stationTotalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
						setPagingToolbarParams(data.page.totalProperty,stationTotalPage,$("#currentPage").val());
					},  
			      complete:function(){
			    	  $("#loading").addClass("hide"); 
			      },
			      error : function() {  
			           showWarning("异常！");
			      }  
			 });
		};
		$("#searchBtn").on("click",function(){
			$("#currentPage").val(1);
			initBody();
		});
		initPagingToolbar(initBody);
		initBody();
		
		//////添加/修改集团子账户
		$("#addGroupUserBtn").on("click",function(){
			$("#groupUserName").val("");
			$("#groupUserPhone").val("");
			$("#groupUserId").val("");
			$("#saveGroupUserBtn").attr("disabled",true);
			$("#groupUserName").attr("disabled",false);
			$("#addOrEditGroupUserTitle").text("添加用户");
			$("#groupUserPage").addClass("hide");
			$("#addOrEditGroupUserPage").removeClass("hide");
		});
		$("#backBtn").on("click",function(){
			$("#addOrEditGroupUserPage").addClass("hide");
			$("#groupUserPage").removeClass("hide");
		});
		$("#groupUserName").on("keyup",function(){
			$("#saveGroupUserBtn").attr("disabled",false);
		});
		$("#groupUserPhone").on("keyup",function(){
			$("#saveGroupUserBtn").attr("disabled",false);
		});
		
		function addGroupUser(groupUserName,groupUserPhone){
			if(isEmpty(groupUserName)){
				showWarning("请填写用户名！！");
				return;
			}else if(isEmpty(groupUserPhone)){
				showWarning("请填写手机号码！！");
				return;
			}else if(!regBox.regMobile.test(groupUserPhone)){
				showWarning("请正确填写11位手机号！！");
				return;
			}
			param={};
			param.groupUserName = groupUserName;
			param.groupUserPhone = groupUserPhone;
			$.ajax({
			      url:addGroupUserUrl,// 跳转到 action  
			      data:param,  
			      type:'post',  
			      cache:false,  
			      dataType:'json', 
			      beforeSend:function(){$("#loading").removeClass("hide");},
			      success:function(data){
			    	  if("success" == data.message){
			    		  	opt={};
							opt.onOk = function(){reload();}
							opt.onClose = opt.onOk;
							showWarning("添加成功！！",opt);
			    	  }else{
			    		  	showWarning(data.message);
			    	  }
			      },
			      complete:function(){
			    	  $("#loading").addClass("hide"); 
			      },
			      error : function() {
			           showWarning("异常！");
			      }  
			});
		}
		function editGroupUser(groupUserId,groupUserPhone){
			if(isEmpty(groupUserPhone)){
				showWarning("请填写手机号码！！");
				return;
			}else if(!regBox.regMobile.test(groupUserPhone)){
				showWarning("请填写正确的手机号码！！");
				return;
			}
			param={};
			param.groupUserId = groupUserId;
			param.groupUserPhone = groupUserPhone;
			$.ajax({
			      url:editGroupUserUrl,// 跳转到 action  
			      data:param,  
			      type:'post',  
			      cache:false,  
			      dataType:'json', 
			      beforeSend:function(){$("#loading").removeClass("hide");},
			      success:function(data){
			    	  if("success" == data.message){
			    		  	opt={};
							opt.onOk = function(){reload();}
							opt.onClose = opt.onOk;
							showWarning("修改成功！！",opt);
			    	  }else{
			    			opt={};
							opt.onOk = function(){$("#groupUserPhone").val(tempGroupUserPhone).focus()};
							opt.onClose = opt.onOk;
			    		  	showWarning(data.message,opt);
			    	  }
			      },
			      complete:function(){
			    	  $("#loading").addClass("hide"); 
			      },
			      error : function() {  
			           showWarning("异常！");
			      }  
			});
		}
		$("#saveGroupUserBtn").on("click",function(){
			groupUserId = $("#groupUserId").val();
			groupUserName = $("#groupUserName").val();
			groupUserPhone = $("#groupUserPhone").val();
			if(groupUserId > 0){
				editGroupUser(groupUserId,groupUserPhone);
			}else{
				addGroupUser(groupUserName,groupUserPhone);
			}
		});
	})
	
