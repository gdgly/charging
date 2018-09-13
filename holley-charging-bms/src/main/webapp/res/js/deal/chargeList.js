var param={};
$(document).ready(function(){
	initParams();
	queryList();
});

function initParams(){
	initStartEndDate($('#startDateDiv'),$('#endDateDiv'));
	initPagingToolbar(queryList);
	//初始化充电记录列表查询按钮
	$("#queryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryList();
	});
	//初始化充电记录列表导出按钮
	initExportBtn($("#exportBtn"),$("#conditionFrom"),$("#fileName").val());
}

function queryList(){
	param={};
	param.startdate =  $.trim($("#startDate").val());
	param.enddate =  $.trim($("#endDate").val());
	param.searchtradeno = $.trim($("#searchTradeno").val());
	param.searchuser = $.trim($("#searchUser").val());
	param.searchstation = $.trim($("#searchStation").val());
	param.dealstatus = $('#dealStatus option:selected').val();
	param.paystatus = $('#payStatus option:selected').val();
	param.billstatus = $('#billStatus option:selected').val();
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	param.tm = new Date().getTime();
	
	var tbody = $("#chargeTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'deal/charge_queryList.action',
		data:param,
		dataType:'json',
        cache: false,
        success: function(data,options){
             if(data.page){
            	 tbody.empty();
            	 var dataList = data.page.root;
            	 $(dataList).each(function(index,item){
            		 html += '<tr>';
            		 html += '<td>'+item.id+'</td>';
            		 html += '<td>'+item.tradeNo+'</td>';
            		 html += '<td>'+item.phone+'</td>';
            		 html += '<td>'+item.stationPileName+'</td>';
            		 html += '<td>'+item.startTimeDesc+'</td>';
            		 html += '<td class="text-right">'+getNotNullData(item.chaLen)+'</td>';
            		 html += '<td class="text-right">'+getNotNullData(item.chaPower)+'</td>';
            		 html += '<td class="text-right"><div class="color-green" onmouseenter="openDetail(this)" onmouseleave="closeDetail(this)"'+
            		 ' startTime='+item.startTime+' endTime='+item.endTime+
            		 ' chaLen='+item.chaLen+' chaPower='+item.chaPower+
            		 ' chaFee='+item.chaFee+' parkFee='+item.parkFee+' serviceFee='+item.serviceFee+
            		 ' shouldMoney='+getNotNullData(item.shouldMoney)+'>'+getNotNullData(item.shouldMoney)+'</div></td>';		
            		 html += '<td>'+item.dealStatusDesc+'</td>';		
            		 html += '<td>'+item.payStatusDesc+'</td>';		
            		 html += '<td>'+item.isBillDesc+'</td>';
            		 html += '<td>'+item.updateTimeDesc+'</td>';
            		 html += '<td>'
            		 html += '<a href="deal/chargeDetailInit.action?paymentid='+item.id+'">详细</a>';
            		 html += '</td>'
            	 });
            	 tbody.html(html);
            	 var currentPage = data.page.startRow/(data.page.endRow-data.page.startRow)+1;
            	 var totalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
            	 setPagingToolbarParams(data.page.totalProperty, totalPage, currentPage);
             }else{
            	 showMsg(data.message, data.errormsg);
             }
         }
     });
}

//显示详细信息
function openDetail(obj){
	$(obj).popover({ 
		html : true,
		title: function() {
			return "<p class='text-center' style='margin-bottom:1px;'>"+'费用明细'+"</p>";
		},
		content: function() {
			var html = '';
			html += '<form role="form" style="width:265px;margin-top:-10px;>';
			html += '<div class="form-group">';
			html += '<label class="col-sm-4 control-label">开始时间</label>';
			html += '<div class="col-sm-8 text-right"><p>'+getFormatDate($(obj).attr("startTime"))+'</p></div>';
			html += '</div>';
			html += '<div class="form-group">';
			html += '<label class="col-sm-4 control-label">结束时间</label>';
			html += '<div class="col-sm-8 text-right"><p>'+getFormatDate($(obj).attr("endTime"))+'</p></div>';
			html += '</div>';
			html += '<div class="form-group">';
			html += '<label class="col-sm-4 control-label">充电时长</label>';
			html += '<div class="col-sm-8 text-right"><p>'+$(obj).attr("chaLen")+' 分钟</p></div>';
			html += '</div>';
			html += '<div class="form-group">';
			html += '<label class="col-sm-4 control-label">充电电量</label>';
			html += '<div class="col-sm-8 text-right"><p>'+$(obj).attr("chaPower")+' 度</p></div>';
			html += '</div>';
			html += '<div class="form-group">';
			html += '<label class="col-sm-4 control-label">充电费用</label>';
			html += '<div class="col-sm-8 text-right"><p>'+$(obj).attr("chaFee")+' 元</p></div>';
			html += '</div>';
			html += '<div class="form-group">';
			html += '<label class="col-sm-4 control-label">停车费用</label>';
			html += '<div class="col-sm-8 text-right"><p>'+$(obj).attr("parkFee")+' 元</p></div>';
			html += '</div>';
			html += '<div class="form-group">';
			html += '<label class="col-sm-4 control-label">服务费用</label>';
			html += '<div class="col-sm-8 text-right"><p>'+$(obj).attr("serviceFee")+' 元</p></div>';
			html += '</div>';
			html += '<div class="form-group">';
			html += '<label class="col-sm-4 control-label">应缴费用</label>';
			html += '<div class="col-sm-8 text-right"><p>'+$(obj).attr("shouldMoney")+' 元</p></div>';
			html += '</div>';
			html += '</form>';
			return html;
		}
	});
	$(obj).popover("show");
}

//关闭详细信息
function closeDetail(obj){
	$(obj).popover("hide");
}



