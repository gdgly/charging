var param={};
$(document).ready(function(){
	initParams();
	queryList();
});

function initParams(){
	initStartEndDate($('#startDateDiv'),$('#endDateDiv'));
	initPagingToolbar(queryList);
	$("#queryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryList();
	});
	$("#dealBtn").on("click",function(){
		dealSuggestion();
	});
}

function queryList(){
	param={};
	param.startdate =  $.trim($("#startDate").val());
	param.enddate =  $.trim($("#endDate").val());
	param.keyword = $.trim($("#keyword").val());
	param.status = $('#dealStatus option:selected').val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	param.tm = new Date().getTime();
	
	var tbody = $("#suggestionTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'sys/suggestion_queryList.action',
		data:param,
		dataType:'json',
        cache: false,
        success: function(data,options){
             if(data.success){
            	 if(data.page){
            		 tbody.empty();
            		 var dataList = data.page.root;
            		 $(dataList).each(function(index,item){
            			 html += '<tr style="cursor:pointer;" onclick="showDetail(this);">';
            			 html += '<td id="addtime">'+item.addTimeDesc+'</td>';
            			 html += '<td id="userid" class="text-right">'+item.userId+'</td>';
            			 html += '<td id="usertype">'+item.usertypeDesc+'</td>';
            			 html += getTdHtml2("username",item.username, 10);
            			 html += '<td id="phone">'+getNotNullData(item.phone)+'</td>';
            			 html += '<td id="status" dealstatus="'+item.status+'">'+item.statusDesc+'</td>';
            			 html += getTdHtml2("content",item.content, 15);
            			 html += getTdHtml2("pic",item.pic, 10);
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

function showDetail(obj){
	var tempObj;
	$('#detail_userid').text($(obj).find('[id=userid]').text());
	$('#detail_usertype').text($(obj).find('[id=usertype]').text());
	tempObj = $(obj).find('[id=username]');
	if(tempObj.attr('title')){
		$('#detail_username').text(tempObj.attr('title'));
	}else{
		$('#detail_username').text(tempObj.text());
	}
	$('#detail_phone').text($(obj).find('[id=phone]').text());
	$('#detail_status').text($(obj).find('[id=status]').text());
	if($(obj).find('[id=status]').attr('dealstatus') == SUGGESTION_STATUS_UNPROCESSED){
		$('#dealBtn').removeClass('hide');
	}else{
		$('#dealBtn').addClass('hide');
	}
	$('#detail_addtime').text($(obj).find('[id=addtime]').text());
	tempObj = $(obj).find('[id=content]');
	if(tempObj.attr('title')){
		$('#detail_content').text(tempObj.attr('title'));
	}else{
		$('#detail_content').text(tempObj.text());
	}
	var pic = $(obj).find('[id=pic]').attr('title');
	$('#picDiv').html('');
	if(!isEmpty(pic)){
		var picArray = pic.split(",");
		var html = '';
		for(var i=0;i<picArray.length;i++){
			if(i == 0){
				html += '<div class="col-sm-offset-2 col-sm-2">';
			}else{
				html += '<div class="col-sm-2">';
			}
			html += '<img id="detail_pic1" class="img-thumbnail img-thumbnail-size" src="'+IMG_SRC+picArray[i]+'" onmouseenter="openImg(this,\'意见图片\')" onmouseleave="closeImg(this)">'
		    html += '</div>';	  
		}
		$('#picDiv').html(html);
	}
	showSuggestionResult("suggestionDetail");
}

function showSuggestionResult(flag){
	if(flag == 'suggestionDetail'){
		$('#suggestionList').addClass('hide');
		$('#suggestionDetail').removeClass('hide');
	}else{
		$('#suggestionList').removeClass('hide');
		$('#suggestionDetail').addClass('hide');
		queryList();
	}
}

function dealSuggestion(){
	param = {};
	param.userid = $('#detail_userid').text();
	param.addtime = $('#detail_addtime').text();
	$.ajax({
		type:'POST',
		url:'sys/suggestion_dealSuggestion.action',
		data:param,
		dataType:'json',
		cache:false,
	    success:function(data){
	    	if(data.success){
	    		showSuggestionResult("suggestionList");
	    		showSuccess("处理成功");
	    	}else{
	    		showMsg(data.message, data.errormsg);
	    	}
	    }
	});
}




