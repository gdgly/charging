var param={};
$(document).ready(function(){
	initParams();
	queryPileList();
});
function delPile(obj){
	pileId = $(obj).attr("pileId");
	pileName = $(obj).attr("pileName");
	if(pileId > 0){
		opt={}
		opt.onOk=function(){
			param={};
			param.pileId = pileId;
			$.ajax({
				type:"POST",
				url:'device/pile_delPile.action',
				data:param,
				dataType:'json',
		        cache: false,
		        beforeSend:function(){$("#loading").removeClass("hide");},
		        complete:function(){
			    	  $("#loading").addClass("hide"); 
			      },
		        success: function(data){
		        	if("success" == data.message){
		        		opt={};
		        		opt.onOk=function(){
		        			$("#pileQueryBtn").click();
		        		}
		        		
		        		showInfo("删除设备“"+pileName+"”成功！",opt);
		        		
		        	}else{
		        		 showMsg(data.message, data.errormsg);
		        	}
		        }
			})
		}
		showConfirm("确定永久删除终端“"+pileName+"”？", opt)
	}else{
		showInfo("请选择充电桩！");
	}
}

function initParams(){
	initPagingToolbar(queryPileList);
	//初始化充电桩列表查询按钮
	$("#pileQueryBtn").on("click",function(){
		$("#stationIdForSelectModal").val(0);
		setPagingToolbarParams(0, 0, 1);
		queryPileList();
	});
	$("#pileType").on("change",function(){
		$("#pileQueryBtn").click();
	});
	$("#chaWay").on("change",function(){
		$("#pileQueryBtn").click();
	});
	$("#payWay").on("change",function(){
		$("#pileQueryBtn").click();
	});
	$("#pileStatus").on("change",function(){
		$("#pileQueryBtn").click();
	});
	$("#pileToType").on("change",function(){
		$("#pileQueryBtn").click();
	});
	$("#comType").on("change",function(){
		$("#pileQueryBtn").click();
	});
	$("#pileResetBtn").on("click",function(){
		resetForm($("#conditionFrom"));
		$("#pileQueryBtn").click();
	});
	//初始化充电桩列表导出按钮
	initExportBtn($("#pileExportBtn"),$("#conditionFrom"),$("#pileFileName").val(),$("#pileTable"));
	bindKey13([$("#keyword")],$("#pileQueryBtn"));//回车自动搜索
}

function queryPileList(stationId){
	param={};
	if(!isEmpty(stationId)){
		setPagingToolbarParams(0, 0, 1);
		param.keyword = $("#keyword").val("").val();
		param.piletype = $("#pileType").val(0).val();
		param.chaway = $("#chaWay").val(0).val();
		param.intftype = $("#intfType").val(0).val();
		param.payway = $("#payWay").val(-1).val();
		param.comtype = $("#comType").val(0).val();
		param.pilestatus = $("#pileStatus").val(0).val();
		param.stationIdForSelectModal = stationId;
		param.pileToType =  $("#pileToType").val(0).val();
		$("#stationIdForSelectModal").val(stationId);
	}else{
		param.keyword = $.trim($("#keyword").val());
		param.piletype = $("#pileType option:selected").val();
		param.chaway = $("#chaWay option:selected").val();
		param.intftype = $("#intfType option:selected").val();
		param.payway = $("#payWay option:selected").val();
		param.comtype = $("#comType option:selected").val();
		param.pilestatus = $("#pileStatus option:selected").val();
		param.stationIdForSelectModal = $("#stationIdForSelectModal").val();
		param.pileToType =  $("#pileToType").val();
	}
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	param.tm = new Date().getTime();
	
	var tbody = $("#pileTable").find("tbody");
	var html = '';
	$.ajax({
		type:"POST",
		url:'device/pile_queryPileList.action',
		data:param,
		dataType:'json',
        cache: false,
        beforeSend:function(){$("#loading").removeClass("hide");},
        complete:function(){
	    	  $("#loading").addClass("hide"); 
	      },
        success: function(data,options){
             if(data.page){
            	 tbody.empty();
            	 var dataList = data.page.root; 
            	 $(dataList).each(function(index,item){
            		 temp = item.busType;
            		 html += '<tr>';
            		/* html += '<td>'+item.id+'</td>';*/
            		 html += '<td>'+item.pileCode+'</td>';
            		 html += getTdHtml(item.pileName, 5);
            		 html += getTdHtml(item.stationName, 10);
            		 html += '<td>'+ getDefaultData(item.pileToTypeDesc,"--")+'</td>';
            		 html += '<td>'+item.pileTypeDesc+'</td>';
            		 html += '<td>'+item.chaWayDesc+'</td>';
            	/*	 html += '<td>'+item.intfTypeDesc+'</td>';*/
            		 html += '<td>'+item.payWayDesc+'</td>';
            		 html += '<td>'+item.comTypeDesc+'</td>';
            		 html += '<td>'+getNotNullData(item.comAddr)+'</td>';
            		/* html += '<td>'+item.isAppDesc+'</td>';*/
            		 html += '<td>'+item.statusDesc+'</td>';
            		 html += '<td>'+getNotNullData(item.updateTimeStr)+'</td>';
            		 html += '<td>';
            		 html += '<a class="a-blue" href="device/pileDetail.action?id='+item.id+'">详细</a>';
            		 if(userType==1){
            			 html += ' | ';
            			 html +='<a class="a-blue" href="device/editPile.action?pileId='+item.id+'">修改</a>'; 
            			 html += ' | ';
            			 html +='<a class="a-blue" href="javascript:;" onclick="delPile(this);" pileId="'+item.id+'" pileName="'+item.stationName+item.pileName+'">删除</a>'; 
            		 }
            		/* else{
            			 html +='<span style="color:gray;">修改</span>'; 
            		 }*/
            		 html +='</td>';
            	 });
            	 tbody.html(html);
            	 var currentPage = data.page.startRow/(data.page.endRow-data.page.startRow)+1;
            	 var totalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
            	setPagingToolbarParams(data.page.totalProperty, totalPage,currentPage);
             }else{
            	 showMsg(data.message, data.errormsg);
             }
         }
     });
}
