/**
 * 充电卡管理
 */
var param={};
$(document).ready(function(){
	initParams();
	//queryUserInfoList();
});
function cardNumClick(obj){
	queryUserCardList($(obj).attr("infoId"));
	$("#cardTableDiv").removeClass("hide");
}
function initParams(){
	//初始化省市
	initArea($("#province"), $("#city"));
	//初始化分页条
	initPagingToolbar(queryUserInfoList);
	//初始化查询按钮
	$("#queryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		$("#cardTableDiv").addClass("hide");
		queryUserInfoList();
	});
	//初始化导出按钮
	//initExportBtn($("#exportBtn"),$("#conditionFrom"),$("#fileName").val());
}
function queryUserCardList(infoId){
	param={};
	param.infoId = infoId;
	if(param.infoId > 0){
		var tbody = $("#cardTbody");
		var html = '';
		$.ajax({
			type:"POST",
			url:'person/cardManager_showCardList.action',
			data:param,
			dataType:'json',
	        cache: false,
	        success: function(data){
	        		 tbody.empty();
	        		 var dataList = data.map.cardList;
	        		 if(data.map){
	        			 if(dataList.length > 0){
		        			 $(dataList).each(function(index,item){
		            			 html += '<tr>';
		            			 html += '<td>'+getNotNullData(item.busNo)+'</td>';
		            			 html += '<td>'+getNotNullData(item.cardNo)+'</td>';
		            			 html += '<td>'+getNotNullData(item.cardTypeDesc)+'</td>';
		            			 html += '<td>'+getNotNullData(item.startTimeDesc)+'</td>';
		            			 html += '<td>'+getNotNullData(item.endTimeDesc)+'</td>';
		            			 html += '<td>'+item.usableMoney+'</td>';
		            			 html += '</tr>';
		            		 });
		            		 tbody.html(html);
		        		 }else{
		        			 showInfo("无充电卡数据！！")
		        		 } 
	        		 }else{
	        			 showMsg(data.message, data.errormsg);
	        		 }
	         }
	     });
	}else{
		showInfo("请选择用户！！")
		return;
	}
}
function queryUserInfoList(){
	param={};
	param.keyword = $.trim($("#keyword").val());
	param.pageindex = $.trim($("#currentPage").val());
	if(isEmpty(param.keyword)){
		showInfo("请输入搜索信息！！")
		return;
	}
	var tbody = $("#userTbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'person/cardManager_userInfoList.action',
		data:param,
		dataType:'json',
        cache: false,
        success: function(data){
        	 if(data.page){
        		 tbody.empty();
        		 var dataList = data.page.root;
        		 var province;
        		 var city;
        		 if(data.page.root.length > 0){
        			 $(dataList).each(function(index,item){
            			 province = getNotNullData(item.provinceDesc);
            			 city = getNotNullData(item.cityDesc);
            			 temp = item.chargeCardNum;
            			 html += '<tr>';
            			 html += getTdHtml(item.username, 15);
            			 html += '<td>'+getNotNullData(item.phone)+'</td>';
            			 html += '<td>'+getNotNullData(item.realName)+'</td>';
            			 html += '<td>'+getNotNullData(item.cardNo)+'</td>';
            			 html += '<td>'+item.sexDesc+'</td>';
            			 html += '<td>'+province+(province!="" && city!= ""?",":"")+city+'</td>';
            			 html += '<td>'+getNotNullData(item.plateNo)+'</td>';
            			 if(temp > 0){
            				 html += '<td><a href="javascript:;" onclick="cardNumClick(this);" infoId="'+item.id+'">'+temp+' >></a></td>'; 
            			 }else{
            				 html += '<td><span>'+temp+'</span></td>';  
            			 }
            			
            			 html += '<td>';
            			 if(temp >= maxChargeCard){
            				 html += '<span style="color:gray;" onclick=showInfo("已达开卡数量上限！！")>开卡</span>'; 
            			 }else{
            				 html += '<a href="person/registerCard.action?infoId='+item.id+'">开卡</a>'; 
            			 }
            			
            			 html += '</td>';
            			 html += '</tr>';
            		 });
        			 $("#noListMsg").remove();
            		 tbody.html(html);
            		 var currentPage = data.page.startRow/(data.page.endRow-data.page.startRow)+1;
            		 var totalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
            		 setPagingToolbarParams(data.page.totalProperty, totalPage,currentPage); 
        		 }else{
        			 if(!$("#noListMsg").attr("id")){
        				$("#userTableDiv").append('<p id="noListMsg" class="text-center" style="margin-top: 50px;">暂无用户信息</p>'); 
        			 }
        		 }
        	
        	 }else{
            	 showMsg(data.message, data.errormsg);
             }
         }
     });
}

















