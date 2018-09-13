/**
 * 告警事件列表
 */
setBowHeight1();
var searchMsgsUrl="bussiness_searchMsgByAjax.action";
var detailMsgUrl="bussiness_detailMsgByAjax.action";
var param={};
var str;
var item;
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
		    		  checkLoginStatus(data,true);
		    		  return;
		    	  }
					if("success" == data.result.msg){
						$("#msgTitle").html(
								data.result.noticeMessage.title
						);
						$("#msgContent").html(
								data.result.noticeMessage.content
						);
						$("#msgAddTime").html(
								"<span style='top:2px;' class='glyphicon glyphicon-time'></span> "+"<time>"+getFormatDate(data.result.noticeMessage.addTime)+"</time>"
						);
						
						$("#msgListDiv").addClass("hide");
						$("#msgContentDiv").removeClass("hide");
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
/*	$("#comeBackMsgListBtn").on("click",function(){
		$("#msgListDiv").removeClass("hide");
		$("#msgContentDiv").addClass("hide");
	});*/
	function initMsgBody(){
		param={};
		param.currentPage=$("#currentPage").val();
		param.isRead=$("#isRead").val();
		item =$("#msgTable").find("tbody");
		$.ajax({  
		      url:searchMsgsUrl,// 跳转到 action  
		      data:param,  
		      type:'post',  
		      cache:false,  
		      dataType:'json',  
		      beforeSend:function(){$("#loading").removeClass("hide");},
		      success:function(data){
		    	  if(data.userLoginStatus){
		    		  checkLoginStatus(data,true);
		    		  return;
		    	  }
		    	  item.empty();
		    	  if(data.page.root.length > 0){
					$(data.page.root).each(function(index,value){
						str = "<tr onclick='showMsgContent(this);'  style='cursor:pointer' id='"
							+value.id
							+"'>"
							+"<td>"+value.title+"</td>"
							+"<td>"+mySubString(value.content,30)+"</td>" 
							+"<td><time>"+getFormatDate(value.addTime)+"</time></td>" 
							+"</tr>"; 
					
						item.append(str);//封装tableBody
					});
		
		    	  }else{
		    		  $("#pagingToolbar").hide();
		    		  item.append("<p class='help-block text-center' style='margin-top:10%;'>暂无记录</p>"); 
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
	initPagingToolbar(initMsgBody);
	initMsgBody();
	$("#allMsg").on("click",function(){
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
	});
})

