var param={};
var html = '';
var queryChargeRuleUrl = "device/pileModel_queryChargeRule.action";//查询
var deleteChargeRuleUrl = "device/pileModel_deleteChargeRule.action";//删除
var editChargeRuleInitUrl = "device/pileModel_editChargeRuleInit.action";//修改初始化
var addOrEditChargeRuleUrl = "device/pileModel_addOrEditChargeRule.action";//修改添加

var opertypeid=0;
$(document).ready(function(){
	initParams();
	queryChargeRuleList();
});
function initParams(){
	initPagingToolbar(queryChargeRuleList);
	//初始化电桩型号列表查询按钮
	$("#queryBtn").on("click",function(){
		setPagingToolbarParams(0, 0, 1);
		queryChargeRuleList();
	});
	$("#resetBtn").on("click",function(){
		resetForm($("#conditionFrom"));
		$("#queryBtn").click();
	});
	$("#backBtn").on("click",function(){
		$("#saveBtn").attr("disabled",true);
		opertypeid=0;
		$("#chargeRuleId").val(0);
		$("#chargeRuleListDiv").removeClass("hide");
		$("#editChargeRuleDiv").addClass("hide");
	});
	$("#chargeRuleForm").find("input[name!='name']").on("change",function(){
	var tempval=$(this).val();
	if(isNaN(tempval)){
		$(this).val("").focus();
	}else{
		$(this).val(parseFloat(Math.abs(tempval)).toFixed(4));
	}
	});
	$("#chargeRuleForm").find("input").on("input",function(){
		$("#saveBtn").attr("disabled",false);
	});
	$("#remark").on("input",function(){
		$("#saveBtn").attr("disabled",false);
	});
	$("#saveBtn").on("click",function(){
		addOrEditChargeRule();
	});
	//初始化电桩型号列表导出按钮
	initExportBtn($("#exportBtn"),$("#conditionFrom"),$("#fileName").val(),$("#chargeRuleTable"));
	bindKey13([$("#keyword")],$("#queryBtn"));//回车自动搜索
}

function queryChargeRuleList(){
	param={};
	html="";
	param.keyword = $.trim($("#keyword").val());
	param.pageindex = $.trim($("#currentPage").val());
	param.pagelimit = PAGE_LIMIT;
	var tbody = $("#chargeRuleBody");
	$.ajax({
		type:"POST",
		url:queryChargeRuleUrl,
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
        			 html += '<td>'+item.name+'</td>';
        			 html += '<td>'+getNotNullData(item.jianFee)+'</td>';
        			 html += '<td>'+getNotNullData(item.fengFee)+'</td>';
        			 html += '<td>'+getNotNullData(item.pingFee)+'</td>';
        			 html += '<td>'+getNotNullData(item.guFee)+'</td>';
        			 html += '<td>'+getNotNullData(item.remark)+'</td>';
        			 html += '<td>'+getNotNullData(item.addTimeDesc)+'</td>';
        			 html += '<td>';
    			     html += '<a  style="cursor: pointer;" onclick="editChargeRuleInit(this);" chargeRuleId="'+item.id+'" data-toggle="tooltip" title="修改">修改</a>';
        			 html += ' | ';
        			 html += '<a  style="cursor: pointer;" onclick="deletChargeRule(this);" chargeRuleId="'+item.id+'" name="'+item.name+'" data-toggle="tooltip" title="删除">删除</a>';
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
	addOrEditChargeRuleInit(REQUEST_TYPE_ADD);
});
function editChargeRuleInit(obj){
	
	addOrEditChargeRuleInit(REQUEST_TYPE_EDIT,$(obj).attr("chargeRuleId"));
}

function deletChargeRule(obj){
	param.chargeRuleId = $(obj).attr("chargeRuleId");
	var option = {
			title: "系统提示",
			btn: window.wxc.xcConfirm.btnEnum.okcancel,
			onOk: function(){
				
				$.ajax({
					type:"POST",
					url:deleteChargeRuleUrl,
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
function addOrEditChargeRuleInit(opertype,chargeRuleId){
	resetForm($("#chargeRuleForm"));
	
	if(REQUEST_TYPE_ADD == opertype){
		opertypeid=REQUEST_TYPE_ADD;
		$("#chargeRuleListDiv").addClass("hide");
		$("#editChargeRuleDiv").removeClass("hide");
	}else if(REQUEST_TYPE_EDIT == opertype){
		opertypeid=REQUEST_TYPE_EDIT;
		if(chargeRuleId <= 0){
			showMsg("参数非法");
			return;
		}
		param={};
		param.chargeRuleId = chargeRuleId;
		$.ajax({
			type:"POST",
			url:editChargeRuleInitUrl,
			data:param,
			dataType:'json',
	        cache: false,
	        beforeSend:showLoading,
	        complete:hideLoading,
	        success: function(data){
	        	if(data.success){
	        	var chargeRule = data.chargeRule;
	        		$("#chargeRuleId").val(chargeRule.id);
	        		$("#name").val(chargeRule.name);
	        		$("#jianFee").val(chargeRule.jianFee);
	        		$("#fengFee").val(chargeRule.fengFee);
	        		$("#pingFee").val(chargeRule.pingFee);
	        		$("#guFee").val(chargeRule.guFee);
	        		$("#remark").val(chargeRule.remark);
	        		$("#chargeRuleListDiv").addClass("hide");
	        		$("#editChargeRuleDiv").removeClass("hide");
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
function addOrEditChargeRule(){
	param = {};
	param.opertype = opertypeid;
	if(param.opertype <= 0){
		showMsg("参数非法");
		return false;
	}
	var tempchargeRule = getFormJson($("#chargeRuleForm"));
	if(!checkchargeRuleData(tempchargeRule)){
		return false;
	}
	if(REQUEST_TYPE_EDIT == param.opertype){
		tempchargeRule.id=$("#chargeRuleId").val();
		if(isEmpty(tempchargeRule.id) || tempchargeRule.id <= 0){
			showMsg("参数非法");
			return false;
		}
	}
	param.chargeRuleJson=objToJsonString(tempchargeRule);
	$.ajax({
		type:"POST",
		url:addOrEditChargeRuleUrl,
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

function checkchargeRuleData(chargeRuleData){
	if(isEmpty(chargeRuleData.name)){
		showMsg("计费模型名称不能为空");
		return false;
	}else if(isEmpty(chargeRuleData.jianFee)){
		showMsg("尖费用不能为空");
		return false;
	}else if(isEmpty(chargeRuleData.fengFee)){
		showMsg("峰费用不能为空");
		return false;
	}else if(isEmpty(chargeRuleData.pingFee)){
		showMsg("平费用不能为空");
		return false;
	}else if(isEmpty(chargeRuleData.guFee)){
		showMsg("谷费用不能为空");
		return false;
	}
	return true;
}


