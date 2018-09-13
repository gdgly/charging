//var newsType = TRENDS;
var pageIndex = 1;//当前页
var pageCount = 0;//总页数
$(document).ready(function(){
	queryList();
});

//切换到充电充电
function selectTrendsTab(item){
	if(newsType == TRENDS)return;
	newsType = TRENDS;
	setParam(item);
}

//切换到新闻资讯
function selectNewsTab(item){
	if(newsType == NEWS)return;
	newsType = NEWS;
	setParam(item);
	
}

function setParam(item){
	$('div.list-group > a').removeClass('active');
	$(item).addClass('active');
	$('#title').text(item.innerText);
	pageIndex = 1;
	showLoadMore();
	queryList();
}

function queryList(){
	 $.ajax({
	        type: "POST",
	        url: 'news/news_queryList.action',
	        data: {newsType:newsType,pageIndex:pageIndex,pageLimit:PAGE_LIMIT,tm:new Date().getTime()},
	        dataType:'json',
	        cache: false,
	        success: function(data,options){
	        	$("#newsContainer").css("height","auto");
	            if(data.success){
	            	var page = data.page;
	            	var list = page.root;
	            	pageCount = Math.floor(page.totalProperty/PAGE_LIMIT) + (page.totalProperty%PAGE_LIMIT>0?1:0);
	            	if(page.totalProperty == 0){
	            		$("#listGroup").html('');
	            	}
	            	if(list == null || list.length == 0){
	            		hideLoadMore();
	            	}else{
	            		createLi(list);  
	            		if(pageIndex == pageCount && list.length <= PAGE_LIMIT){
	            			hideLoadMore();
	            		}
	            	}
	            	if($("#listGroup").height() < getContentHeight()-176){
            			$("#newsContainer").css("height",getContentHeight());
            		}
	            }else{
	            	
	            }
	        }
	    });
}

function createLi(list){
	var html = '';
	$.each(list, function(i, item){
		  html += '<li class="list-group-item" style="border-left-style: none;border-right-style: none;">';
		  html += '<div class="row">';
		  html += '<div class="col-sm-3" style="min-height: 155px;">';
		  html += '<a class="thumbnail" href="news/newsdetail.action?id='+item.id+'" style="margin-bottom:0;">';
		  html += '<img src="'+item.picture+'" alt="消息图片" style="width:100%;height:145px;">';
		  html += '</a></div>';
		  html += '<div class="col-sm-9">';
		  html += '<h4 style="margin-top: 10px;">';
		  html += '<a href="news/newsdetail.action?id='+item.id+'"><span style="color:#000;">'+item.title+'</span></a>';
		  html += '</h4>';
		  html += '<p style="color:#9d9d9d;margin-top:10px;">'+item.summary+'</p>';
		  html += '<div class="pull-right" style="color:#666666">';
		  html += '<span class="glyphicon glyphicon-time"></span>&nbsp;<time>'+item.publishdatestr+'</time>';
		  html += '</div></div></div></li>';
//		  html += '<hr style="height:1px;border:none;border-top:1px dashed #ccc" />';
	});
	if(pageIndex == 1){
		$("#listGroup").html('');
		$("#listGroup").html(html); 
	}else{
		$("#listGroup").append(html); 
	}
}

//点击加载更多
function loadMore(){
	pageIndex ++;
	if(pageIndex < 1) pageIndex = 1;
	if(pageIndex > pageCount) {
		pageIndex = pageCount;
		hideLoadMore();
		return;
	}
	queryList();
}

//显示“加载更多"
function showLoadMore(){
	$("#loadMoreBtn").removeClass("hidden");
	$("#noMoreText").addClass("hidden");
}
//隐藏"加载更多"
function hideLoadMore(){
	$("#loadMoreBtn").addClass("hidden");
	$("#noMoreText").removeClass("hidden");
}

