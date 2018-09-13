/**
 * 个人收藏
 */
	var param={};
	var userFavoriteUrl = "userjson/personal_userFavorite.action";
	var userFavoriteDelUrl = "userjson/personal_favoritedel.action";
	function favoritedel(obj){
		stationName = $(obj).attr("stationName");
		favoriteId = $(obj).attr("id");
		if(favoriteId > 0){
			param.favoriteId = favoriteId;
			opt={};
			opt.onOk = function(){
				doFavoritedel(param);
			}
			showConfirm("确定删除收藏点：'"+stationName+"'？",opt);	
			
		}else{
			showWarning("请选择要取消的充电点！！");
		}
	}
	function doFavoritedel(param){
		$.ajax({  
		      url:userFavoriteDelUrl,// 跳转到 action  
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
		    	  if("success" == data.message){
		    		  reload();
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
$(function(){
	
	function initBody(){
		param.currentPage=$("#currentPage").val();
		//param.searchKeyName = $("#searchKeyName").val().trim();
		var item = $("#userFavoriteBody");
		$.ajax({  
		      url:userFavoriteUrl,// 跳转到 action  
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
							+"<td>"+value.stationname+"</td>"
							+"<td title='"+value.address+"'>"
							+mySubString(value.address,13)
							+"</td>"
							+"<td>"
							+"<img class='startImg' style='background-position:-"+(50-parseInt(value.score))+"px;background-image:url("+IMG_SRC+"res/img/bussiness/starBackImg.png);'"+" src="+IMG_SRC+"res/img/bussiness/starImg.gif"+">"
							+"</td>"
							+"<td>"+value.fastnum+"</td>"
							+"<td>"+value.slownum+"</td>"
							+"<td><a id='"
							+value.favoriteid
							+"' stationName='"
							+value.stationname
							+"' "
							+"onclick='favoritedel(this);' href='javascript:;'>取消</a></td>"
							+"</tr>";
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
	initPagingToolbar(initBody);
	initBody();
})

