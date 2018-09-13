var param={};
$(document).ready(function(){
	initUserParams();
	queryList();
});

function initUserParams(){
	initStartEndDate($('#startDateDiv'),$('#endDateDiv'));
	initPagingToolbar(queryList);
	$("#queryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryList();
	});
	$("#operatorType").on("change",function(){
		$("#queryBtn").click();
	});
	bindKey13([$("#keyword")],$("#queryBtn"));//回车自动搜索
}

function queryList(){
	param={};
	param.startdate =  $.trim($("#startDate").val());
	param.enddate =  $.trim($("#endDate").val());
	param.keyword = $.trim($("#keyword").val());
	param.usertype = $('#userType option:selected').val();
	param.logtype = $('#logType option:selected').val();
	param.operatortype = $('#operatorType option:selected').val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	param.tm = new Date().getTime();
	
	var tbody = $("#logTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'sys/operationLog_queryList.action',
		data:param,
		dataType:'json',
        cache: false,
        beforeSend:function(){$("#loading").removeClass("hide");},
        complete:function(){
	    	  $("#loading").addClass("hide"); 
	      },
        success: function(data,options){
             if(data.success){
            	 if(data.page){
            		 tbody.empty();
            		 var dataList = data.page.root;
            		 $(dataList).each(function(index,item){
            			 html += '<tr style="cursor:pointer;" onclick="showDetail(this);">';
            			 html += '<td id="logid">'+item.id+'</td>';
            			 html += '<td id="userid">'+item.userId+'</td>';
            			 html += '<td id="usertype">'+item.usertypeDesc+'</td>';
            			 html += getTdHtml2("username",item.username, 15);
            			 html += '<td id="phone">'+getNotNullData(item.phone)+'</td>';
            			 html += '<td id="type">'+item.typeDesc+'</td>';
            			 html += getTdHtml2("described",item.described, 20);
            			 html += '<td id="ip" class="hide">'+getNotNullData(item.ip)+'</td>';
            			 html += '<td id="addtime">'+item.addTimeDesc+'</td>';
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
	$('#detail_type').text($(obj).find('[id=logid]').text());
	tempObj = $(obj).find('[id=described]');
	if(tempObj.attr('title')){
		$('#detail_described').text(tempObj.attr('title'));
	}else{
		$('#detail_described').text(tempObj.text());
	}
	$('#detail_ip').text($(obj).find('[id=ip]').text());
	$('#detail_addtime').text($(obj).find('[id=addtime]').text());
	showLogResult("logDetail");
}

function showLogResult(flag){
	if(flag == 'logDetail'){
		$('#logList').addClass('hide');
		$('#logDetail').removeClass('hide');
	}else{
		$('#logList').removeClass('hide');
		$('#logDetail').addClass('hide');
	}
}




