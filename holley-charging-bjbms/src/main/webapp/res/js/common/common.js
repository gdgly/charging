var maxImgSize = 4;//图片大小4M
/**
 * 回车直接搜索
 * @param keyWords搜索框元素数组列：[${'#keyword1'},${'#keyword2'}]
 * @param searchBtn搜索按钮
 */
function bindKey13(keyWords,searchBtn){
	$(keyWords).each(function(index,data){
		$(data).on("keydown",function(event){
			if(event.keyCode == 13){
				searchBtn.click();
				return false;
			}
		})
	})
}

/*$(keyWords).each(function(index,data){
	$(data).on("keydown",function(event){
		if(event.keyCode == 13){
			return false;
		}
	})
})*/
/**
 * 判断是否为空
 * @param str
 * @returns {Boolean}
 */
function isEmpty(str){
	if(!str){
		return true;
	}else if(str == undefined){
		return true;
	}else if($.trim(str).length <= 0){
		return true;
	}
	return false;
}
function getDefaultData(data,defaultData){
	if(data){
		return data+"";
	}
	else{
		return defaultData+"";
	}

}
function return2Num(num){
	if(num){
		if(num.indexOf(".") != -1){
			pre = num.substring(0,num.indexOf(".")+1);
			last = num.substring(num.indexOf(".")+1,num.length);
			if(last.length > 2){
				last = last.substring(0,2);
			}
			return parseFloat(pre+last);
		}else{
			return parseFloat(num);
		}
	}else{
		return 0;
	}
}
function mySubString(str,size){
	var temp;
	if(str && str.length > 0){
	if(str.length > size){
		temp = str.substring(0,size)+"...";
	}
	else{
		temp = str;
	}
	}else{
		temp = "暂无";
	}
	return temp;
}
/**
 * 删除左右两端的空格
 * @param str
 * @returns
 */
function trim(s){
	return s.replace(/(^s*)|(s*$)/g, "");
}
/**
 * 设置高度
 * @returns {Number}
 */
function setContentHeight(obj,opt){
	if(opt > 0){
		temp = $(window).height()-HEADER_HEIGHT-opt;
	}else{
		temp = $(window).height()-HEADER_HEIGHT;
	}
	obj.css("height", temp);   
}
/***
 * 初始化时间控件
 * @param searchStartTimeDate
 * @param searchEndTimeDate
 */
function initStartEndDate(startTimeDate,endTimeDate){
	startTimeDate.datetimepicker({
    	locale: 'zh-CN',
    	format: 'YYYY-MM-DD'
    });
	endTimeDate.datetimepicker({
    	locale: 'zh-CN',
    	format: 'YYYY-MM-DD',
    	 useCurrent: false //Important! See issue #1075
    });
	startTimeDate.on("dp.change",function (e) {
		date = new Date(e.date);
		day = date.getDate()+1;
		year = date.getFullYear();
		month = date.getMonth()+1;
		tempdate = year+"-"+month+"-"+day+" 00:00:00";
		if(e.date == null)return;
		endTimeDate.data("DateTimePicker").minDate(tempdate);
    });
	endTimeDate.on("dp.change",function (e) {
		if(e.date == null)return;
		startTimeDate.data("DateTimePicker").maxDate(e.date);
    });
}
function initStartEndDate2(startTimeDate,endTimeDate){
	startTimeDate.datetimepicker({
    	locale: 'zh-CN',
    	format: 'YYYY-MM-DD HH:mm:ss'
    });
	endTimeDate.datetimepicker({
    	locale: 'zh-CN',
    	format: 'YYYY-MM-DD HH:mm:ss',
    	 useCurrent: false //Important! See issue #1075
    });
	startTimeDate.on("dp.change",function (e) {
		temp = new Date(e.date);
		month = temp.getMonth()+1;
		day = temp.getDate();
		year = temp.getFullYear();
		
		now = new Date();
		nowMonth = now.getMonth()+1;
		nowDay = now.getDate();
		if(month != nowMonth || day != nowDay){
			$("#startDate").val(year+"-"+month+"-"+day+" 00:00:00");
		}
		
		if(e.date == null)return;
		endTimeDate.data("DateTimePicker").minDate(e.date);
    });
	endTimeDate.on("dp.change",function (e) {
		temp = new Date(e.date);
		month = temp.getMonth()+1;
		day = temp.getDate();
		year = temp.getFullYear();
		
		now = new Date();
		nowMonth = now.getMonth()+1;
		nowDay = now.getDate();
		if(month != nowMonth || day != nowDay){
			$("#endDate").val(year+"-"+month+"-"+day+" 00:00:00");
		}
		
		if(e.date == null)return;
		startTimeDate.data("DateTimePicker").maxDate(e.date);
    });
}


function initStartEndDate3(startTimeDate,endTimeDate){
	startTimeDate.datetimepicker({
    	locale: 'zh-CN',
    	format: 'YYYY-MM-DD HH:mm:ss'
    });
	endTimeDate.datetimepicker({
    	locale: 'zh-CN',
    	format: 'YYYY-MM-DD HH:mm:ss',
    	 useCurrent: false //Important! See issue #1075
    });
	startTimeDate.on("dp.change",function (e) {
		temp = new Date(e.date);
		month = temp.getMonth()+1;
		day = temp.getDate();
		year = temp.getFullYear();
		
		now = new Date();
		nowMonth = now.getMonth()+1;
		nowDay = now.getDate();
		if(month != nowMonth || day != nowDay){
			$("#startUpdateDate").val(year+"-"+month+"-"+day+" 00:00:00");
		}
		
		if(e.date == null)return;
		endTimeDate.data("DateTimePicker").minDate(e.date);
    });
	endTimeDate.on("dp.change",function (e) {
		temp = new Date(e.date);
		month = temp.getMonth()+1;
		day = temp.getDate();
		year = temp.getFullYear();
		
		now = new Date();
		nowMonth = now.getMonth()+1;
		nowDay = now.getDate();
		if(month != nowMonth || day != nowDay){
			$("#endUpdateDate").val(year+"-"+month+"-"+day+" 00:00:00");
		}
		
		if(e.date == null)return;
		startTimeDate.data("DateTimePicker").maxDate(e.date);
    });
}
/***
 * 初始化时间控件
 * @param searchStartTimeDate
 * @param searchEndTimeDate
 */
function initStartEndMonth(startTimeDate,endTimeDate,defaultStartDate,defaultEndDate){
	startTimeDate.datetimepicker({
    	locale: 'zh-CN',
    	format: 'YYYY-MM',
    	viewMode: 'months',
    	defaultDate: defaultStartDate
    });
	endTimeDate.datetimepicker({
    	locale: 'zh-CN',
    	format: 'YYYY-MM',
    	viewMode: 'months',
    	useCurrent: false, //Important! See issue #1075
    	defaultDate: defaultEndDate
    });
	startTimeDate.on("dp.change",function (e) {
		if(e.date == null)return;
		endTimeDate.data("DateTimePicker").minDate(e.date);
    });
	endTimeDate.on("dp.change",function (e) {
		if(e.date == null)return;
		startTimeDate.data("DateTimePicker").maxDate(e.date);
    });
}

/***
 * 初始化单个时间控件
 * @param oneTimeDate
 */
function initDate(datetime){
	datetime.datetimepicker({
    	locale: 'zh-CN',
    	format: 'YYYY-MM-DD'
    });
}

/***
 * 初始化单个时间控件
 * @param oneTimeDate
 */
function initMonth(datetime,defaultDate){
	datetime.datetimepicker({
    	locale: 'zh-CN',
    	format: 'YYYY-MM',
    	defaultDate:defaultDate
    });
}
function initOneBootStrapDate(oneTimeDate){
	
	oneTimeDate.datetimepicker({
		locale: 'zh-CN',
		format: 'YYYY/MM/DD'
	});
}
function initMinBootStrapDate(oneTimeDate,currentTime){
	oneTimeDate.datetimepicker({
		locale: 'zh-CN',
		format: 'YYYY/MM/DD',
		//defaultDate: new Date()
		minDate:currentTime
	});
}
//
//function addDays(date, amount){
//	if(!date)return null;
//	return date.setDate(date.getDate()+amount);
//}
//
function addMonths(date, amount){
	if(!date)return null;
	return date.setMonth(date.getMonth()+amount);
}

/**
 * 初始化分页条,绑定按钮事件
 * @param fn
 */
function initPagingToolbar(fn){
	//第一页
	$("#firstPage").on("click",function(){
		var pageIndex = 1;
		$("#currentPage").val(pageIndex);
		fn();
	});
	
	//上一页
	$("#prevPage").on("click",function(){
		var pageIndex = parseInt($("#currentPage").val())-1;
		$("#currentPage").val(pageIndex);
		fn();
	});
	
	//下一页
	$("#nextPage").on("click",function(){
		var pageIndex = parseInt($("#currentPage").val())+1;
		$("#currentPage").val(pageIndex);
		fn();
	});
	
	//最后一页
	$("#lastPage").on("click",function(){
		var pageIndex = $("#totalPage").text();
		$("#currentPage").val(pageIndex);
		fn();
	});
}

/**
 * 初始化分页条2,绑定按钮事件
 * @param fn
 */
function initPagingToolbar2(fn){
	//第一页
	$("#firstPage2").on("click",function(){
		var pageIndex = 1;
		$("#currentPage2").val(pageIndex);
		fn();
	});
	
	//上一页
	$("#prevPage2").on("click",function(){
		var pageIndex = parseInt($("#currentPage2").val())-1;
		$("#currentPage2").val(pageIndex);
		fn();
	});
	
	//下一页
	$("#nextPage2").on("click",function(){
		var pageIndex = parseInt($("#currentPage2").val())+1;
		$("#currentPage2").val(pageIndex);
		fn();
	});
	
	//最后一页
	$("#lastPage2").on("click",function(){
		var pageIndex = $("#totalPage2").text();
		$("#currentPage2").val(pageIndex);
		fn();
	});
}

/**
 * 初始化分页条2,绑定按钮事件
 * @param fn
 */
function initPagingToolbar3(fn){
	//第一页
	$("#firstPage3").on("click",function(){
		var pageIndex = 1;
		$("#currentPage3").val(pageIndex);
		fn();
	});
	
	//上一页
	$("#prevPage3").on("click",function(){
		var pageIndex = parseInt($("#currentPage3").val())-1;
		$("#currentPage3").val(pageIndex);
		fn();
	});
	
	//下一页
	$("#nextPage3").on("click",function(){
		var pageIndex = parseInt($("#currentPage3").val())+1;
		$("#currentPage3").val(pageIndex);
		fn();
	});
	
	//最后一页
	$("#lastPage3").on("click",function(){
		var pageIndex = $("#totalPage3").text();
		$("#currentPage3").val(pageIndex);
		fn();
	});
}

/**
 * 设置分页条的参数
 * @param totalProperty
 * @param totalPage
 */
function setPagingToolbarParams(totalProperty,totalPage,currentPage){
	 $("#currentPage").val(currentPage);
	 $("#totalProperty").text(totalProperty);
	 $("#totalPage").text(totalPage);
  	 if($("#currentPage").val() <= 1){
  		$("#prevPage").attr("disabled","disabled");
  		$("#firstPage").attr("disabled","disabled");
  	 }else{
  		$("#prevPage").removeAttr("disabled");
  		$("#firstPage").removeAttr("disabled");
  	 }
  	
  	 if($("#currentPage").val() >= totalPage){
  		$("#nextPage").attr("disabled","disabled");
  		$("#lastPage").attr("disabled","disabled");
  	 }else{
  		$("#nextPage").removeAttr("disabled");
  		$("#lastPage").removeAttr("disabled");
  	 }
}
/**
 * 设置分页条2的参数
 * @param totalProperty
 * @param totalPage
 */
function setPagingToolbar2Params(totalProperty,totalPage,currentPage){
	$("#currentPage2").val(currentPage);
	 $("#totalProperty2").text(totalProperty);
	 $("#totalPage2").text(totalPage);
  	 if($("#currentPage2").val() <= 1){
  		$("#prevPage2").attr("disabled","disabled");
  		$("#firstPage2").attr("disabled","disabled");
  	 }else{
  		$("#prevPage2").removeAttr("disabled");
  		$("#firstPage2").removeAttr("disabled");
  	 }
  	
  	 if($("#currentPage2").val() >= totalPage){
  		$("#nextPage2").attr("disabled","disabled");
  		$("#lastPage2").attr("disabled","disabled");
  	 }else{
  		$("#nextPage2").removeAttr("disabled");
  		$("#lastPage2").removeAttr("disabled");
  	 }
}

/**
 * 设置分页条2的参数
 * @param totalProperty
 * @param totalPage
 */
function setPagingToolbar3Params(totalProperty,totalPage,currentPage){
	$("#currentPage3").val(currentPage);
	 $("#totalProperty3").text(totalProperty);
	 $("#totalPage3").text(totalPage);
  	 if($("#currentPage3").val() <= 1){
  		$("#prevPage3").attr("disabled","disabled");
  		$("#firstPage3").attr("disabled","disabled");
  	 }else{
  		$("#prevPage3").removeAttr("disabled");
  		$("#firstPage3").removeAttr("disabled");
  	 }
  	
  	 if($("#currentPage3").val() >= totalPage){
  		$("#nextPage3").attr("disabled","disabled");
  		$("#lastPage3").attr("disabled","disabled");
  	 }else{
  		$("#nextPage3").removeAttr("disabled");
  		$("#lastPage3").removeAttr("disabled");
  	 }
}

/**
 * 设置默认值
 * @param data
 * @returns
 */
function getNotNullData(data){
	if(data || data == 0){
		return data;
	}else{
		return "--";
	}	
}

/**
 * 设置数字默认值
 * @param data
 * @returns
 */
function getDefaultZeroData(data){
	if(data || data == 0){
		return data;
	}else{
		return "0";
	}	
}

/**
 * 获取有文字缩略的列
 * @param value
 * @param length
 */
function getTdHtml(value,length){
	var html = "";
	value = getNotNullData(value);
	if(value.length > length){
		 var newValue = value.replace(/"/g,"&quot;");//title里如果有双引号要替换成转义符，否则显示不完全
		 html += '<td data-toggle="tooltip" title="'+newValue+'">'+substring(value,length)+'</td>';
	 }else{
		 html += '<td>'+value+'</td>';
	 }
	return html;
}

/**
 * 获取有文字缩略的列
 * @param value
 * @param length
 */
function getTdHtml2(id,value,length){
	var html = "";
	value = getNotNullData(value);
	if(value.length > length){
		 var newValue = value.replace(/"/g,"&quot;");//title里如果有双引号要替换成转义符，否则显示不完全
		 html += '<td id="'+id+'" data-toggle="tooltip" title="'+newValue+'">'+substring(value,length)+'</td>';
	 }else{
		 html += '<td id="'+id+'">'+value+'</td>';
	 }
	return html;
}

function getFormatDate(date){
	str="";
	if(!date)return "";
	if(date.indexOf("T") != -1){
		str = date.replace("T"," ")
	}else{
		str = date;
	}
	return str;
}

function substring(str,length){
	if(!str)return "";
	var temp;
	if(str.length > length){
		temp = str.substring(0,length)+"...";
	}else{
		temp = str;
	}
	return temp;
}

/**
 * 重置表单
 * @param form
 */
function resetForm(form){
	form.get(0).reset();
}

/**
 * 封装表单数据
 * @param form
 * @returns {___anonymous120_121}
 */
function getFormJson(form) {
	var o = {};
	var a = form.serializeArray();
	$.each(a, function () {
		if (o[this.name] !== undefined) {
			if (!o[this.name].push) {
				o[this.name] = [o[this.name]];
			}

			o[this.name].push(this.value || '');
		} else {

			o[this.name] = this.value || '';
		}
	});

	return o;
}
/**
 * 表单数据转化为JSON字符串
 * @param formObj
 * @returns
 */
function formDataToJsonString(formObj){
	return JSON.stringify(getFormJson(formObj));
}

function objToJsonString(dataObj){
	return JSON.stringify(dataObj);
} 

function jsonstrToObj(jsonstr){
	return JSON.parse(jsonstr)
}

//弹出框
function showInfo(txt,opt){
	if(opt){
		window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.info,opt);
	}else{
		window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.info);
	}
	
}

function showConfirm(txt,opt){
	if(opt){
		window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.confirm,opt);
	}else{
		window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.confirm);
	}
}

function showWarning(txt,opt){
	if(opt){
		window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.warning,opt);
	}else{
		window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.warning);
	}
}

function showError(txt,opt){
	if(opt){
		window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.error,opt);
	}else{
		window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.error);
	}
}

function showSuccess(txt,opt){
	if(opt){
		window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.success,opt);
	}else{
		window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.success);
	}
}

function showCustom(txt,option){
	window.wxc.xcConfirm(txt, "custom", option);
}

function showDefault(txt){
	window.wxc.xcConfirm(txt);
}

function encodeHtml(s){
    return (typeof s != "string") ? s :
        s.replace(this.REGX_HTML_ENCODE,
                  function($0){
                      var c = $0.charCodeAt(0), r = ["&#"];
                      c = (c == 0x20) ? 0xA0 : c;
                      r.push(c); r.push(";");
                      return r.join("");
                  });
};

function reload() {
	//location.reload() 
	window.location.href = location.href;
}

function goback(){
	history.back();//返回 
}

function gobackAndReload(){
//	history.back();//返回 
//	var bow = getBrowser();
//	if("IE" == bow){
//		location.reload();//刷新
//	}
	self.location=document.referrer;
}

function href(url) {
	window.location.href=WEB_ROOT+"/"+url;
}

function openImg(obj,title){
	$(obj).popover({ 
		html : true,
		title: function() {
		 return title;
		},
		content: function() {
			var html = "";
			html += '<div id="appcode_popover">'; 
			html += '<div style="width: 300px;height: 300px;">';
			html += '<img class="img-thumbnail" alt="" src="'+$(obj).attr("src")+'" style="width: 300px;height: 300px;">';
		 	html += '</div></div>';
		 return html;
		}
	});
	 $(obj).popover("show");
}

function closeImg(obj){
	$(obj).popover("hide");
}

//当前浏览器
function getBrowser(){  
    var browserName=navigator.userAgent.toLowerCase(); 
    if(/msie/i.test(browserName) && !/opera/.test(browserName)){  
        return "IE";  
    }else if(/firefox/i.test(browserName)){  
        return "Firefox";  
    }else if(/chrome/i.test(browserName) && /webkit/i.test(browserName) && /mozilla/i.test(browserName)){  
    	return"Chrome";  
    }else if(/opera/i.test(browserName)){  
    	return "Opera";  
    }else if(/webkit/i.test(browserName) &&!(/chrome/i.test(browserName) && /webkit/i.test(browserName) && /mozilla/i.test(browserName))){  
    	return "Safari";  
    }else{  
    	return "unKnow";  
    }  
}

function showMsg(message,errormsg){
	if(!isEmpty(message)){
		if("relogin" == message){
			var opt={};
			opt.onOk = function(){
				location.reload();
			}
			showInfo("登录超时，请重新登录！",opt);
		}else{
			showInfo(message);
		}
	}else if(!isEmpty(errormsg)){
		showError(errormsg);
	}
}
//充电桩状态显示的颜色
function getPileStatusColor(status){
	var color = null;
	 if(status == PILE_RUNSTATUS_IDLE){//空闲
		 color = "green";
	 }else if(status == PILE_RUNSTATUS_APPOINTMENT){//预约中
		 color = "#ff8c00";
	 }else if(status == PILE_RUNSTATUS_CHARGING){//充电中
		 color = "#7ec0ee";
	 }else if(status == PILE_RUNSTATUS_OFFLINE){//离线
		 color = "#6e6e6e";
	 }else if(status == PILE_RUNSTATUS_FAULT){//故障
		 color = "red";
	 }else if(status == PILE_RUNSTATUS_UNKNOW){//忙碌中
		 color = "#cdcdcd";
	 }
	 return color;
}

//获取通知方式
function getNoticeTypes(){
	var noticetypeArray = new Array();
	$("input[name='noticeType']:checked").each(function(){ 
		noticetypeArray.push($(this).val());
	}); 
	return noticetypeArray.join(",");
}
/**
 * 查询车品牌型号
 * @param brand
 * @param model
 */
function initCarBrand(brand,model){
	var requestModelsUrl = "person/cardManager_requestCarModel.action";
	var param={};
	brand.on("change",function(){
		param.brand = brand.val();
		if(param.brand == 0){
			model.empty().append("<option value='0'>请先选择车品牌</option>");;
		}
		else if(param.brand > 0){
			$.post(requestModelsUrl,param,function(data){
				model.empty();
				$(data.modelList).each(function(index,value){
					model.append("<option value="+value.id+">"+value.name+"</option>");
				});
			});
		}
		else{
			alert("请选车品牌！！");
		}
	});
}

function initArea(province,city){
	var requestCitysUrl = "device/station_queryCitys.action";
	var param={};
	//省市级联
	province.on("change",function(){
		param.province = province.val();
		if(param.province == 0){
			city.empty().append("<option value='0'>请选择市区</option>");
		}else if(param.province > 0){
			$.post(requestCitysUrl,param,function(data){
				city.empty();
				if(data.citys){
					city.append("<option value='0'>请选择市区</option>");
					$(data.citys).each(function(index,value){
						city.append("<option value="+value.id+">"+value.name+"</option>");
					});
				}else{
					showMsg(data.message, data.errormsg);
				}
			});
		}else{
			showInfo("请选择省份");
		}
	});
}
//检查图片大小格式
function checkImg(img,maxSize){
	var domObj = img.get(0).files[0];
	var path = img.val();
	var size;
	if(!path){
		return "请选择上传图片！！";
	}
	var end = path.substring(path.lastIndexOf(".")+1);
	end = end.toLowerCase();

	if(end != "jpg" && end != "png" && end != "gif"){
		return "上传图片必须jpg或png或gif格式！！";
	}
	else if(!domObj){
		return "请选择上传图片！！";
	}
	else{
		size = domObj.size;
		if(maxSize){
			if(size/(1000*1024) > maxSize){
				return "选择上传的文件不能大于"+maxSize+"M！！";
			}
		}
	}
	return "success";
}
function commomChangeImg(ImgfileId,srcImgId){
	/* rFilter = /^(?:image\/bmp|image\/cis\-cod|image\/gif|image\/ief|image\/jpeg|image\/jpeg|image\/jpeg|image\/pipeg|image\/png|image\/svg\+xml|image\/tiff|image\/x\-cmu\-raster|image\/x\-cmx|image\/x\-icon|image\/x\-portable\-anymap|image\/x\-portable\-bitmap|image\/x\-portable\-graymap|image\/x\-portable\-pixmap|image\/x\-rgb|image\/x\-xbitmap|image\/x\-xpixmap|image\/x\-xwindowdump)$/i; */
	var reader = new FileReader();
		reader.onload = function (oFREvent) {
		document.getElementById(srcImgId).src = oFREvent.target.result;
	};
	$("#"+ImgfileId).on("change",function(){
		msg = checkImg($(this), maxImgSize);
		if ("success" != msg) {
			showWarning(msg);
			$(this).val("");
		} else {
			var oFile = $(this).get(0).files[0];
			reader.readAsDataURL(oFile);
		}
	});
	$("#"+srcImgId).hover(function(){
		$(this).popover({
			html : true,
			title: function() {
				return "<p class='text-center' style='margin-bottom:1px;'>图片</p>";
			},
			content: function() {
				return "<img style='width:250px;' src='"+$(this).attr("src")+"'>";
			}
		});
		$(this).popover("show");
		},function(){
			$(this).popover("hide");
			});
}
/**
 * 初始化导出按钮
 */
function initExportBtn(exportBtn,from,title,table){
/*	exportBtn.on("keydown", function(e){
		alert("do")
		if(e.keyCode == 13) {
			alert(13)
			return;
		}
	})*/
	
	exportBtn.on("click",function(){
		temp = $("#totalProperty").text();
		if(temp <= 0){
			showInfo("导出数据为空！！");
			return;
		}
//		if(table){
//			if(!table.find("tbody").find("tr").html()){
//				showInfo("导出数据为空！！");
//				return;
//			}	
//		}
		
		exportBtn.blur(); 
		exportBtn.attr("disabled",true)
		var option = {
				title: "系统提示",
				btn: window.wxc.xcConfirm.btnEnum.okcancel,
				onOk: function(){
					exportBtn.attr("disabled",false);
					from.submit();
				},
				onClose:function(){
					exportBtn.attr("disabled",false);
				},
				onCancel:function(){
					exportBtn.attr("disabled",false);
				}
			}
			showConfirm("确定导出'"+title+"'吗?",option);
		//showInfo("确定导出'"+title+"'吗?");
	});
}
var regBox = {
		regEmail : /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/,//邮箱
		regName : /^[a-z0-9_-]{3,16}$/,//用户名
		regMobile : /^(((13[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/,//手机
		regTel : /^0[\d]{2,3}-[\d]{7,8}$/,
		//身份证正则表达式(15位)
		regCard15 : /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/,
		//身份证正则表达式(18位)
		regCard18 : /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/,
		regNum:/^[0-9]*$/,
		//中文
		regCn:/^[\u4e00-\u9fa5]+$/
}

function replaceAll(str,str1,str2){
	 return str.replace(new RegExp(str1,"gm"),str2);
}
function loading(isLoad){
	if(isLoad){
		$("#loading").removeClass("hide");
	}else{
		$("#loading").addClass("hide");
	}
}
/**
 * 计算功率
 */
function getRate(outI,outV){
	if(outI> 0 && outV > 0){
		return (outI*outV).toFixed(2);
	}else{
		return "--";
	}
}

/**
 * 计算soc电池余量百分比
 */
function getSoc(soc){
	if(soc > 0){
		return (soc*100).toFixed(0);
	}else{
		return "--";
	}
}

function showLoading(){
	$("#loading").removeClass("hide");
}

function hideLoading(){
	$("#loading").addClass("hide");
}

function initWebsocket(){
	var webSocket;  
    if ('WebSocket' in window) {  
        webSocket = new WebSocket("ws://localhost:8080/ws/websocket");  
    } else if ('MozWebSocket' in window) {  
        webSocket = new MozWebSocket("ws://localhost:8080/websocket");  
    } else {  
        alert("js");  
        webSocket = new SockJS("http://localhost:8080/Origami/sockjs/webSocketServer");  
    }  
    webSocket.onerror = function(event) {  
      alert("error")
    };  
   
    webSocket.onopen = function(event) {  
    	 alert("open")
    };  
   
    webSocket.onmessage = function(event) {  
    	 alert("onmessage")
    };  
}