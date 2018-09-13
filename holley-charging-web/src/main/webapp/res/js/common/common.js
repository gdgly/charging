
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
//设置运营商窗口高度 start
function setBowHeight1(){
	var bow = getBrowser();
	var defaultHeight = 0;
	if(bow != "unKnow"){
		if("Firefox" == bow){
			defaultHeight = 643;
		}
		else if("Chrome" == bow){
			defaultHeight = 651;
		}
		if(defaultHeight && defaultHeight > 0){
			setBgHeight(defaultHeight);//动态设置窗口高度	
		}
	}
}
function setBowHeight2(){
	var bow = getBrowser();
	var defaultHeight = 0;
	if(bow != "unKnow"){
		if("Firefox" == bow){
			defaultHeight = 693;
		}
		else if("Chrome" == bow){
			defaultHeight = 715;
		}
		if(defaultHeight && defaultHeight > 0){
			setBgHeight(defaultHeight);//动态设置窗口高度	
		}
		var temp = getWindowHeight();
		if(temp && temp > 0){
			if(temp < defaultHeight){
				temp = defaultHeight;
			}
			$("#mapDiv").css("height",temp);
		}
		
	}
}
function setBowHeight3(){
	var bow = getBrowser();
	var defaultHeight = 0;
	if(bow != "unKnow"){
		if("Firefox" == bow){
			defaultHeight = 693;
		}
		else if("Chrome" == bow){
			defaultHeight = 729;
		}
		if(defaultHeight && defaultHeight > 0){
			setBgHeight(defaultHeight);//动态设置窗口高度	
		}
		
	}
}
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
function setBgHeight(defaultHeight){
	var temp = getWindowHeight();
	if(temp && temp > 0){
		if(temp < defaultHeight){
			temp = defaultHeight;
		}
		$(".bg").css("height",temp);
	}
}


function getWindowHeight(){
	return $(window).height()-120;
}
//设置运营商窗口高度 end
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
/**
 * 判断对象是否为空
 * @param obj
 * @returns boolean
 */
function isEmptyObj(obj){
	return $.isEmptyObject(obj);
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
 * 获取除去header和footer以外的高度
 * @returns {Number}
 */
function getContentHeight(){
	return $(window).height()-HEADER_HEIGHT-FOOTER_HEIGHT;
}

function getBasepath(){
	var local = window.location;  
    var contextPath = local.pathname.split("/")[1];  
    var basePath = local.protocol+"//"+local.host+"/"+contextPath+"/";  
    return basePath;
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

function goSearch(){	
	var sv = $("#searchMainInput").val();
	if (sv== null || sv == ""){
		alert("请输入查询内容");
	}else{
		$("#searchMainForm").submit();
	}
}

function goSearch2(){	
	var sv = $("#searchMainInput2").val();
	if (sv== null || sv == ""){
		alert("请输入查询内容");
	}else{
		$("#searchMainForm2").submit();
	}
}
/**
 * 初始化导出按钮
 */
function initExportBtn(exportBtn,url1,url2,fileName1,fileName2,value1,value2){
	exportBtn.on("click",function(){
		stationId = $("#stationId").val();
		opt={};
		opt.onOk = function(){
			if(stationId > 0 && url1){
				temp = url1+"?isExport=1&stationId="+stationId;
				if(fileName1){
					temp = temp +"&fileName="+fileName1;	
				}
				if(!isEmpty($("#"+value1).val())){
					temp = temp + "&"+value1+"="+$("#"+value1).val();
				}
				if(!isEmpty($("#"+value2).val())){
					temp = temp + "&"+value2+"="+$("#"+value2).val();
				}
				href(temp);
			}else if(url2){
				temp = url2+"?isExport=1";
				if(fileName2){
					temp = temp +"&fileName="+fileName2;	
				}
				if(!isEmpty($("#"+value1).val())){
					temp = temp + "&"+value1+"="+$("#"+value1).val();
				}
				if(!isEmpty($("#"+value2).val())){
					temp = temp + "&"+value2+"="+$("#"+value2).val();
				}
				href(temp);
			}
		}
		showConfirm("确定要导出Excel？",opt);
	});
}
/**
 * 初始化分页条,绑定按钮事件
 * @param fn
 */
function initPagingToolbar(fn){
	$("#currentPage").val("1");
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
	//下一页alert();
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
	$("#currentPage2").val("1");
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
function initPagingToolbar3(appfn,chafn){
	$("#currentPage3").val("1");
	//第一页
	$("#firstPage3").on("click",function(){
		temp = $("#isAppOrCha");
		var pageIndex = 1;
		$("#currentPage3").val(pageIndex);
		if("app" == temp.val()){
			appfn();
		}else if("cha" == temp.val()){
			chafn();
		}
	});
	
	//上一页
	$("#prevPage3").on("click",function(){
		temp = $("#isAppOrCha");
		var pageIndex = parseInt($("#currentPage3").val())-1;
		$("#currentPage3").val(pageIndex);
		if("app" == temp.val()){
			appfn();
		}else if("cha" == temp.val()){
			chafn();
		}
	});
	
	//下一页
	$("#nextPage3").on("click",function(){
		temp = $("#isAppOrCha");
		var pageIndex = parseInt($("#currentPage3").val())+1;
		$("#currentPage3").val(pageIndex);
		if("app" == temp.val()){
			appfn();
		}else if("cha" == temp.val()){
			chafn();
		}
	});
	
	//最后一页
	$("#lastPage3").on("click",function(){
		temp = $("#isAppOrCha");
		var pageIndex = $("#totalPage3").text();
		$("#currentPage3").val(pageIndex);
		if("app" == temp.val()){
			appfn();
		}else if("cha" == temp.val()){
			chafn();
		}
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
 * 设置分页条3的参数
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
var searchKeyName;
function searchPileByStationId(obj,initPileBody){
	$("#stationId").val($(obj).attr("stationId"));
	$("#addStationBtn").attr("stationId",$(obj).attr("stationId"));
	//$("#currentPage").val("1");
	$("#currentPage2").val("1");
	searchKeyName = $("#searchKeyName").val();
	$("#searchKeyName").val("");
	$("#searchKeyNameDiv").removeClass("col-sm-offset-4");
	$("#datetimeInput").val("");
	$("#datetime").removeClass("hide");
	initPileBody();
	$("#comeBack").removeClass("hide");
	$("#pileDiv").removeClass("hide");
	$("#stationDiv").addClass("hide");
}
function backPileByStationId(stationId,initPileBody){
	$("#stationId").val(stationId);
	$("#addStationBtn").attr("stationId",stationId);
	//$("#currentPage").val("1");
	$("#currentPage2").val("1");
	searchKeyName = $("#searchKeyName").val();
	$("#searchKeyName").val("");
	$("#searchKeyNameDiv").removeClass("col-sm-offset-4");
	$("#datetimeInput").val("");
	$("#datetime").removeClass("hide");
	initPileBody();
	$("#comeBack").removeClass("hide");
	$("#pileDiv").removeClass("hide");
	$("#stationDiv").addClass("hide");
}
function comeback(){
	if(searchKeyName){
		$("#searchKeyName").val(searchKeyName);
	}else{
		$("#searchKeyName").val("");
	}
	$("#addStationBtn").attr("stationId","0");
	$("#searchKeyNameDiv").addClass("col-sm-offset-4");
	$("#datetime").addClass("hide");
	$("#stationId").val("");
	$("#comeBack").addClass("hide");
	$("#pileDiv").addClass("hide");
	//initStationBody();
	$("#stationDiv").removeClass("hide");
}
function publicSearchFun(initStationBody,initPileBody){

	$("#searchKeyNameBtn").on("click",function(){
		var stationId = $("#stationId").val();
		$("#currentPage").val("1");
		$("#currentPage2").val("1");
	/*	if(!$("#searchKeyName").val().trim()){
			showWarning("请输入搜索关键字！！");
			return false;
		}*/
		 if(stationId && stationId > 0){
			initPileBody();
		}
		else{
			initStationBody();
		}
	});
}
function stationAndPileBtn(initStationBody,initPileBody,initFn){
	$("#searchStationBtn").on("click",function(){
		initFn();
		$("#searchType").val("station");
		$("#currentPage").val("1");
		$("#currentPage2").val("1");
		initStationBody();
		$(this).addClass("active");
		$("#searchPileBtn").removeClass("active");
		$("#pileDiv").addClass("hide");
		$("#stationDiv").removeClass("hide");
	});
	$("#searchPileBtn").on("click",function(){
		initFn();
		$("#searchType").val("pile");
		$("#currentPage").val("1");
		$("#currentPage2").val("1");
		initPileBody();
		$(this).addClass("active");
		$("#searchStationBtn").removeClass("active");
		$("#pileDiv").removeClass("hide");
		$("#stationDiv").addClass("hide");
	});
}




//util.js
var YYYY;
var MM;
var DD;
var hh;
var mm;
var ss;
var dateTime;
var maxImgSize = 4;//图片大小4M
var maxDocSize = 10;//文件大小10M
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

function initBootStrapDate(searchStartTimeDate,searchEndTimeDate){
	searchStartTimeDate.datetimepicker({
		locale: 'zh-CN',
		format: 'YYYY/MM/DD'
	});
	searchEndTimeDate.datetimepicker({
		locale: 'zh-CN',
		format: 'YYYY/MM/DD'
	});
	searchStartTimeDate.on("dp.change",function (e) {
		searchEndTimeDate.data("DateTimePicker").minDate(e.date);
	});
	searchEndTimeDate.on("dp.change",function (e) {
		searchStartTimeDate.data("DateTimePicker").maxDate(e.date);
	});
}
function initOneBootStrapDate(oneTimeDate){
	
	oneTimeDate.datetimepicker({
		locale: 'zh-CN',
		format: 'YYYY/MM/DD'
	});
}
function initOneBootStrapDateDef(oneTimeDate,formatStr){
	oneTimeDate.datetimepicker({
		locale: 'zh-CN',
		format: formatStr
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
function initOneBootStrapDate2(oneTimeDate){
	oneTimeDate.datetimepicker({
		locale: 'zh-CN',
		viewMode: 'years',
		format: 'YYYY/MM'
	});
}
function initOneBootStrapDate3(oneTimeDate){
	oneTimeDate.datetimepicker({
		locale: 'zh-CN',
		defaultDate: new Date(),
		viewMode: 'years',
		format: 'YYYY'
	});
}

function GetDateStr(AddDayCount) {
    var dd = new Date();
    dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
   return dd;
}
function initArea(province,city){
	var requestCitysUrl = "bussiness_requestCitys.action";
	var param={};
	//省市级联
	province.on("change",function(){
		param.province = province.val();
		if(param.province == 0){
			city.empty().append("<option value='0'>请先选择省份</option>");;
		}
		else if(param.province > 0){
			$.post(requestCitysUrl,param,function(data){
				city.empty();
				$(data.citys).each(function(index,value){
					city.append("<option value="+value.id+">"+value.name+"</option>");
				});
			});
		}
		else{
			alert("请选择省份！！");
		}
	});
}
function initCarBrand(brand,model){
	var requestModelsUrl = "userjson/personal_requestCarModel.action";
	var param={};
	//省市级联
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
function getFormatDate(date){
	str="";
	if(!date)return "";
	if(date.indexOf("T") != -1){
		str = date.replace("T"," ")
	}else{
		str = date;
	}
	return str;
	/*dateTime = new Date(date);
	var str="";
	YYYY = dateTime.getFullYear();
	MM = dateTime.getMonth()+1;
	DD = dateTime.getDate();
	hh = dateTime.getHours();
	mm = dateTime.getMinutes();
	ss = dateTime.getSeconds();

	if(MM < 10){
		MM = "0"+MM;
	}
	if(DD < 10){
		DD = "0"+DD;
	}
	if(hh < 10){
		hh = "0"+hh;
	}
	if(mm < 10){
		mm = "0"+mm;
	}
	if(ss < 10){
		ss = "0"+ss;
	}
	str = str+YYYY+"-"+MM+"-"+DD+" "+hh+":"+mm+":"+ss
	return str;*/
}
/**
 * 设置默认值
 * @param data
 * @returns
 */
function getNotNullData(data){
	if(!data){
		return "";
	}
	else{
		return data;
	}	

}
function getDefaultData(data,defaultData){
	if(data){
		return data;
	}
	else{
		return defaultData;
	}

}
/*function trim(str){ //删除左右两端的空格
	return str.replace(/(^\s*)|(\s*$)/g, "");
}*/
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
 * 判断是否为空
 * @param str
 * @returns {Boolean}
 */
/*function isEmpty(str){
	if(!str){
		return true;
	}
	else if(trim(str).length <= 0){
		return true;
	}
	return false;
}*/
/**
 * 重置表单
 * @param form
 */
function resetForm(form){
	form.get(0).reset();
}
//检查上传文件类型，大小
function checkDoc(doc,maxSize){
	var domObj = doc.get(0).files[0];
	var path = doc.val();
	var size;
	if(!path){
		return "请选择上传资料！！";
	}
	var end = path.substring(path.lastIndexOf(".")+1);
	end = end.toLowerCase();

	if(end != "doc" && end != "pdf"){
		return "上传图片必须doc或pdf格式！！";
	}
	else if(!domObj){
		return "请选择上传资料！！";
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
function commomChangeDoc(docFileId){

	$("#"+docFileId).on("change",function(){
		msg = checkDoc($(this), maxDocSize);
		if ("success" != msg) {
			showWarning(msg);
			$(this).val("");
		}
	});
}
function reload() {
	location.reload() 
}
function href(url) {
	window.location.href=WEB_ROOT+"/"+url;
}
function frameHref(url) {
	window.parent.href(url);
	/*var b = window.parent.document.getElementById("mainWindow");
	$(b).attr("src",WEB_ROOT+"/"+url);*/
}
function frameReload(param) {
	if(param){
		frameHref('userAdmin/home.action?direction='+param);
	}else{
		frameHref('userAdmin/home.action');
	}
	//window.parent.reload();
}
function getParentElementById(id){
	el = window.parent.document.getElementById(id);
	return $(el);
}
String.prototype.endWith=function(str){
	var reg=new RegExp(str+"$");
	return reg.test(this);
	}
function goUserMenu1(elementId){
	m = getParentElementById(elementId);
	if(!m.parent().hasClass("active")){
		m.parent().addClass('active');
	}
}
function goUserMenu2(elementId){
	m = getParentElementById(elementId);
	m.parent().parent().find("li").each(function(index,value){
		id = $(value).find("a").attr("id");
		if(id == elementId){
		}else{
			$(value).removeClass('active');
		}
	});
   temp = elementId;
   setTimeout('goUserMenu1("'+temp+'")',600); 
}
function goUserMainMenu(elementId){
    temp = getParentElementById(elementId);
    temp = temp.parent().parent().prev("a")
	if(!temp.parent().hasClass("active")){
		temp.get(0).click();
	}  
}
function goUserMenu(menuName){
    goUserMainMenu(menuName);
    goUserMenu2(menuName);
}
function checkLoginStatus(data,isBussiness){
	 msg="登录超时，请重新登录！！";
	  if(data.sessionMsg){
		  msg = data.sessionMsg;
	  }
	    opt={};
		opt.onOk = function(){
			if(isBussiness){
				reload();
			}else{
				frameReload();
			}
			}
		opt.onClose = opt.onOk;
		showWarning(msg,opt);	
}
//alert 相关
function showInfo(txt){
	window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.info);
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
/*
function loadImageFile(FileObj,Reader) {
	if (FileObj.files.length === 0) { return; }
	var oFile = FileObj.files[0];
	if (!rFilter.test(oFile.type)) 
	{ 
		alert("必须选择图片!"); 
		$(FileObj).val("");
		return; 

	}
	Reader.readAsDataURL(oFile);
}*/
