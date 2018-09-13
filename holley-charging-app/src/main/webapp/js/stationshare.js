var pageindex = 1;//当前页
var pagecount = 0;//总页数
var pagelimit = 5;
$(document).ready(function(){
	queryList();
});

function queryList(){
	var stationid = trim($("#stationid").text());
	 $.ajax({
	        type: "POST",
	        url: 'pob/sharepilecomment.htm',
	        data: {stationid:stationid,pageindex:pageindex,pagelimit:pagelimit,tm:new Date().getTime()},
	        dataType:'json',
	        cache: false,
	        success: function(data,options){
	            if(data.resultBean.success){
	            	var page = data.page;
	            	var list = page.root;
	            	pagecount = Math.floor(page.totalProperty/pagelimit) + (page.totalProperty%pagelimit>0?1:0);
	            	if(page.totalProperty == 0){
	            		$("#commentul").html('');
	            	}
	            	if(list == null || list.length == 0){
	            		hideLoadMore();
	            	}else{
	            		createList(list);  
	            		if(pageindex == pagecount && list.length <= pagelimit){
	            			hideLoadMore();
	            		}
	            	}
	            }
	        }
	    });
}


//创建评论
function createList(list){
	var html = '';
	$.each(list, function(i, item){
		  html += '<li>';
		  html += '<div class="list-top f-cb">';
		  html += '<div class="img fl">';
		  html += '<img src="'+imgurl+item.headImg+'" alt="">';
		  html += '</div>';
		  html += '<div class="text fl">';
		  html += '<span>'+item.username+'</span>';
		  html += '<div class="xingx">';
		  var num1 = (item.score > 50 ? 50 : item.score) / 10;
		  for(var k=0;k<num1;k++){
			  html += '<i class="n1"></i>';
		  }
		  var num2 = 5 - num1;
		  for(var j=0;j<num2;j++){
			  html += '<i class="n2"></i>';
		  }
		  html += '<b>'+num1+'.0</b>';
		  html += '</div>';
		  html += '</div>';
		  html += '<div class="time fr">'+item.addTime.split("T")[0]+'</div>';
		  html += '</div>';
		  html += '<div class="list-text f-cb">';
		  html += '<p style="word-wrap: break-word;">'+getNotNullData(item.content)+'</p>';
		  if(item.path != null && item.path.length > 0){
			  var imgArray = item.path.split(",");
			  for(var t=0;t<imgArray.length;t++){
				  html += '<div class="img">';
				  html += '<img src="'+imgurl+imgArray[t]+'" alt="">';
				  html += '</div>';
			  }
		  }
		  html += '</div>';
		  html += '</li>';
	});
	if(pageindex == 1){
		$("#commentul").html('');
		$("#commentul").append(html); 
	}else{
		$("#commentul").append(html); 
	}
}

//点击加载更多
function loadMore(){
	pageindex ++;
	if(pageindex < 1) pageindex = 1;
	if(pageindex > pagecount) {
		pageindex = pagecount;
		hideLoadMore();
		return;
	}
	queryList();
}

//显示“加载更多"
function showLoadMore(){
	$("#loadMoreBtn").css("display","block");
	$("#noMoreText").css("visibility","hidden");
}
//隐藏"加载更多"
function hideLoadMore(){
	$("#loadMoreBtn").css("display","none");
	$("#noMoreText").css("visibility","visible");
}

function trim(str){ //删除左右两端的空格
	return str.replace(/(^\s*)|(\s*$)/g, "");
}

function getNotNullData(data){
	if(!data){
		return "";
	}else{
		return data;
	}	
}





