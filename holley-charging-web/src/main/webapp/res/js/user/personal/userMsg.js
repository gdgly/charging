	/**
	 * 个人信息
	 */
	var param={};
	var userMsgUrl = "userjson/personal_userMsg.action";
	var detailMsgUrl = "userjson/personal_userDetailMsg.action";
	var allMsg=0;
	var readMsg=1;
	var unReadMsg=2;
	function showMsgContent(obj){
		id = $(obj).attr("id");
		if(id){
			param.noticeId = id;
			$.ajax({
			      url:detailMsgUrl,// 跳转到 action  
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
						if("success" == data.result.msg){
							$("#msgDiv").addClass("hide");
							$("#msgTitle").html(data.result.noticeMessage.title);
							$("#msgContent").html(data.result.noticeMessage.content);
							$("#msgAddTime").html(data.result.noticeMessage.addTimeDesc);
							$("#msgDetailDiv").removeClass("hide")
							
							//noticeMessage
		/*					$("#msgTitle").html(
									data.result.noticeMessage.title
							);
							$("#msgContent").html(
									data.result.noticeMessage.content
							);
							$("#msgAddTime").html(
									"<span style='top:2px;' class='glyphicon glyphicon-time'></span> "+"<time>"+getFormatDate(data.result.noticeMessage.addTime)+"</time>"
							);
							
							$("#msgListDiv").addClass("hide");
							$("#msgContentDiv").removeClass("hide");*/
						}
						else{
							showWarning(data.result.msg)
						}
			      },
			      complete:function(){
			    	  $("#loading").addClass("hide"); 
			      },
			      error : function() {  
			           showWarning("异常！");
			      }  
			})
		}
		else{
			showWarning("请选择阅读的信息！！");
		}
	}
	$(function(){
		function initBody(){
			param={};
			param.currentPage=$("#currentPage").val();
			param.isRead=$("#isRead").val();
			var item =$("#userMsgBody");
			$.ajax({  
			      url:userMsgUrl,// 跳转到 action  
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
							str = "<tr onclick='showMsgContent(this);'  style='cursor:pointer' id='"
								+value.id
								+"'>"
								+"<td>"+value.title+"</td>"
								+"<td>"+mySubString(value.content,30)+"</td>" 
								+"<td><time>"+value.addTimeDesc+"</time></td>" 
								+"</tr>"; 
						
							item.append(str);//封装tableBody
						});
						  $("#pagingToolbar").show();
			    	  }else{
			    		  $("#pagingToolbar").hide();
			    		  if(!$("#help-block").attr("id")){
								 item.parent().parent().append("<p id='help-block' class='help-block text-center' style='margin-top:10%;'>暂无记录</p>");
							 }
			    		 // item.append("<p class='help-block text-center' style='margin-top:10%;'>暂无信息</p>"); 
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
			})
		};
		initPagingToolbar(initBody);
		initBody();
		$("#allMsg").on("click",function(){
			$("#isRead").val(allMsg);
			$("#currentPage").val("1");
			initBody();
		})
		$("#unMsg").on("click",function(){
			$("#isRead").val(unReadMsg);
			$("#currentPage").val("1");
			initBody();
		})
		$("#yesMsg").on("click",function(){
			$("#isRead").val(readMsg);
			$("#currentPage").val("1");
			initBody();
		})
$("#comebackMsgBtn").on("click",function(){
	$("#msgDiv").removeClass("hide");
	$("#msgDetailDiv").addClass("hide")	
});
/*		$("#allMsg").on("click",function(){
			$("#searchMsgBtn").find("li").each(function(index,data){
				$(data).removeClass("active");
			});
			$(this).addClass("active");
			$("#isRead").val(allMsg);
			initMsgBody();
		});
		$("#unMsg").on("click",function(){
			$("#searchMsgBtn").find("li").each(function(index,data){
				$(data).removeClass("active");
			});
			$(this).addClass("active");
			$("#isRead").val(unReadMsg);
			initMsgBody();
		});
		$("#yesMsg").on("click",function(){
			$("#searchMsgBtn").find("li").each(function(index,data){
				$(data).removeClass("active");
			});
			$(this).addClass("active");
			$("#isRead").val(readMsg);
			initMsgBody();
		});*/
	})