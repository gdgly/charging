var param={};
var html = '';
var queryRebateUrl = "sys/user_queryRebateList.action";//查询
var deleteRebateUrl = "sys/user_deleteRebate.action";//删除
var editRebateInitUrl = "sys/user_editRebateInit.action";//修改初始化
var addOrEditRebateUrl = "sys/user_addOrEditRebate.action";//修改添加
var opertypeid=0;
$(document).ready(function(){
	initParams();
	queryRebateList();
});
function initParams(){
	initPagingToolbar(queryRebateList);
	initOneBootStrapDate($("#startTimeDate"));//初始化时间插件
	initOneBootStrapDate($("#endTimeDate"));//初始化时间插件
	//初始化电桩型号列表查询按钮
	$("#queryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryRebateList();
	});
	$("#resetBtn").on("click",function(){
		resetForm($("#conditionFrom"));
		$("#queryBtn").click();
	});
	$("#backBtn").on("click",function(){
		opertypeid=0;
		$("#rebateId").val(0);
		$("#rebateListDiv").removeClass("hide");
		$("#editRebateDiv").addClass("hide");
	});
	$("#rebate").on("change",function(){
	var tempval=$(this).val();
	if(isNaN(tempval) || tempval < 1 || tempval >= 10){
		$(this).val("").focus();
	}else{
		$(this).val(parseFloat(Math.abs(tempval)).toFixed(1));
	}
	});

	$("#saveBtn").on("click",function(){
		addOrEditRebate();
	});
	//初始化电桩型号列表导出按钮
	initExportBtn($("#exportBtn"),$("#conditionFrom"),$("#fileName").val(),$("#rebateTable"));
	bindKey13([$("#keyword")],$("#queryBtn"));//回车自动搜索
}

function queryRebateList(){
	param={};
	html="";
	param.keyword = $.trim($("#keyword").val());
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	var tbody = $("#rebateBody");
	$.ajax({
		type:"POST",
		url:queryRebateUrl,
		data:param,
		dataType:'json',
        cache: false,
        beforeSend:showLoading,
        complete:hideLoading,
        success: function(data){
        	 if(data.page){
        		 tbody.empty();
        		 var dataList = data.page.root;
        		 $(dataList).each(function(index,item){
        			 html += '<tr>';
        			 html += '<td>'+item.rebateDesc+'</td>';
        			 html += '<td>'+getNotNullData(item.rebate)+'</td>';
        			 html += '<td>'+getNotNullData(item.startTimeDesc)+'</td>';
        			 html += '<td>'+getNotNullData(item.endTimeDesc)+'</td>';
        			 html += '<td>'+getNotNullData(item.addTimeDesc)+'</td>';
        			 html += '<td>'+getNotNullData(item.isActiveDesc)+'</td>';
        			 html += '<td>';
        			 if(item.isActive){
        				   html += '<span  style="cursor: pointer;color:grey;" >修改</span>'; 
        			 }else{
        				 html += '<a  style="cursor: pointer;" onclick="editRebateInit(this);" rebateId="'+item.id+'" data-toggle="tooltip" title="修改">修改</a>'; 
        			 }
    			     html += ' | ';
        			 html += '<a  style="cursor: pointer;" onclick="deletRebate(this);" rebateId="'+item.id+'" name="'+item.rebateDesc+'" data-toggle="tooltip" title="删除">删除</a>';
        			 html += '</td>';
        			 html += '</tr>';
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

//新增按钮点击事件
$("#addBtn").on("click",function(){
	addOrEditRebateInit(REQUEST_TYPE_ADD);
});
function editRebateInit(obj){
	addOrEditRebateInit(REQUEST_TYPE_EDIT,$(obj).attr("rebateId"));
}

function deletRebate(obj){
	param.rebateId = $(obj).attr("rebateId");
	var option = {
			title: "系统提示",
			btn: window.wxc.xcConfirm.btnEnum.okcancel,
			onOk: function(){
				
				$.ajax({
					type:"POST",
					url:deleteRebateUrl,
					data:param,
					dataType:'json',
			        cache: false,
			        beforeSend:showLoading,
			        complete:hideLoading,
			        success: function(data){
			        	if(data.success){
			        		var opt={
			        				onOk:reload,
			        				onClose:reload
			        		}
			        		showInfo("删除成功", opt)
						}else{
							showMsg(data.message, data.errormsg);
						}
			         }
			     });
			}
		}
		showConfirm("确定删除'"+$(obj).attr("name")+"'吗?",option);
}
/**
 * 初始化添加修改界面
 * @param opertype
 */
function addOrEditRebateInit(opertype,rebateId){
	resetForm($("#rebateForm"));
	if(REQUEST_TYPE_ADD == opertype){
		opertypeid=REQUEST_TYPE_ADD;
		$("#rebateListDiv").addClass("hide");
		$("#editRebateDiv").removeClass("hide");
	}else if(REQUEST_TYPE_EDIT == opertype){
		opertypeid=REQUEST_TYPE_EDIT;
		if(rebateId <= 0){
			showMsg("参数非法");
			return;
		}
		param={};
		param.rebateId = rebateId;
		$.ajax({
			type:"POST",
			url:editRebateInitUrl,
			data:param,
			dataType:'json',
	        cache: false,
	        beforeSend:showLoading,
	        complete:hideLoading,
	        success: function(data){
	        	if(data.success){
	        	var rebateBean = data.rebateBean;
	        		$("#rebateId").val(rebateBean.id);//id
	        		$("#rebateDesc").val(rebateBean.rebateDesc);//名称
	        		$("#rebate").val(rebateBean.rebate);//折扣
	        		$("#startTimeDate").data("DateTimePicker").date(rebateBean.startTime);
	        		$("#endTimeDate").data("DateTimePicker").date(rebateBean.endTime);
	        		//时间
	        		$("#rebateListDiv").addClass("hide");
	        		$("#editRebateDiv").removeClass("hide");
				}else{
					showMsg(data.message, data.errormsg);
				}
	         }
	     });
	}
	
}
/**
 * 添加修改主方法
 * @param opertype
 */
function addOrEditRebate(){
	param = {};
	param.opertype = opertypeid;
	if(param.opertype <= 0){
		showMsg("参数非法");
		return false;
	}
	var tempRebate = getFormJson($("#rebateForm"));
	if(!checkRebateData(tempRebate)){
		return false;
	}
	if(REQUEST_TYPE_EDIT == param.opertype){
		tempRebate.id=$("#rebateId").val();
		if(isEmpty(tempRebate.id) || tempRebate.id <= 0){
			showMsg("参数非法");
			return false;
		}
	}
	param.rebateJson=objToJsonString(tempRebate);
	$.ajax({
		type:"POST",
		url:addOrEditRebateUrl,
		data:param,
		dataType:'json',
        cache: false,
        beforeSend:showLoading,
        complete:hideLoading,
        success: function(data){
        	if(data.success){
        		var opt={
        				onOk:reload,
        				onClose:reload
        		}
        		if(REQUEST_TYPE_EDIT == opertypeid){
        			showInfo("修改成功", opt);
        		}else if(REQUEST_TYPE_ADD == opertypeid){
        			showInfo("添加成功", opt);
        		}
			}else{
				showMsg(data.message, data.errormsg);
			}
         }
     });
}

function checkRebateData(rebateData){
	if(isEmpty(rebateData.rebateDesc)){
		showMsg("优惠方案名称不能为空");
		return false;
	}else if(isEmpty(rebateData.rebate)){
		showMsg("折扣必须填写>=1或<10之间的数字");
		return false;
	}else if(rebateData.rebate < 1 || rebateData.rebate >= 10){
		showMsg("折扣必须填写>=1或<10之间的数字");
		return false;
	}else if(isEmpty(rebateData.startTime)){
		showMsg("优惠开始时间不能为空");
		return false;
	}else if(isEmpty(rebateData.endTime)){
		showMsg("优惠结束时间不能为空");
		return false;
	}else if(rebateData.startTime == rebateData.endTime|| new Date(rebateData.startTime) > new Date(rebateData.endTime)){
		showMsg("优惠结束时间必须大于优惠开始时间");
		return false;
	}
	return true;
}


