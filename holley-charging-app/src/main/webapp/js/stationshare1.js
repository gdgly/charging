var pageindex = 1;//当前页
var pagecount = 0;//总页数
var pagelimit = 10;
$(document).ready(function(){
	refresher.init({
        id: "wrapper",
        pullDownAction: refresh,
        pullUpAction: load
    });
	queryList();
});

function refresh() {
	pageindex = 1;
	queryList();
}

function load(){
	pageindex ++;
	if(pageindex < 1) pageindex = 1;
	if(pageindex > pagecount) {
		pageindex = pagecount;
		wrapper.refresh();
		return;
	}
	queryList();
}

function queryList(){
	var stationid = trim($("#stationid").text());
	 $.ajax({
	        type: "POST",
	        url: 'pob/sharepilecomment.htm',
	        data: {stationid:stationid,pageindex:pageindex,pagelimit:pagelimit,tm:new Date().getTime()},
	        dataType:'json',
	        cache: false,
	        success: function(data,options){
	            if(data.resultBean.success){
	            	var page = data.page;
	            	var list = page.root;
	            	pagecount = Math.floor(page.totalProperty/pagelimit) + (page.totalProperty%pagelimit>0?1:0);
	            	$("#commenttotal").text(page.totalProperty+'条评价');
	            	if(list != null && list.length > 0){
	            		createList(list);
	            	}
	            	wrapper.refresh();
	            }
	        }
	    });
}


//创建评论
function createList(list){
	var html = '';
	$.each(list, function(i, item){
		  html += '<li>';
		  html += '<div class="list-top f-cb">';
		  html += '<div class="img fl">';
		  html += '<img src="'+imgurl+item.headImg+'" alt="">';
		  html += '</div>';
		  html += '<div class="text fl">';
		  html += '<span>'+item.username+'</span>';
		  html += '<div class="xingx">';
		  var num1 = (item.score > 50 ? 50 : item.score) / 10;
		  for(var k=0;k<num1;k++){
			  html += '<i class="n1"></i>';
		  }
		  var num2 = 5 - num1;
		  for(var j=0;j<num2;j++){
			  html += '<i class="n2"></i>';
		  }
		  html += '<b>'+num1+'.0</b>';
		  html += '</div>';
		  html += '</div>';
		  html += '<div class="time fr">'+item.addTime.split("T")[0]+'</div>';
		  html += '</div>';
		  html += '<div class="list-text f-cb">';
		  html += '<p>'+item.content+'</p>';
		  if(item.path != null && item.path.length > 0){
			  var imgArray = item.path.split(",");
			  for(var t=0;t<imgArray.length;t++){
				  html += '<div class="img">';
				  html += '<img src="'+imgurl+imgArray[t]+'" alt="">';
				  html += '</div>';
			  }
		  }
		  html += '</div>';
		  html += '</li>';
	});
	if(pageindex == 1){
		$("#commentul").html('');
		$("#commentul").append(html); 
	}else{
		$("#commentul").append(html); 
	}
}

function trim(str){ //删除左右两端的空格
	return str.replace(/(^\s*)|(\s*$)/g, "");
}

function download(){
	/*
	 * 智能机浏览器版本信息:
	 *
	 */
	 var browser = {
	 	versions: function() {
			 var u = navigator.userAgent, app = navigator.appVersion;
			 return {//移动终端浏览器版本信息 
				 mobile: !!u.match(/AppleWebKit.*Mobile.*/) || !!u.match(/AppleWebKit/), //是否为移动终端
				 ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
				 android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
				 iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //是否为iPhone或者QQHD浏览器
				 iPad: u.indexOf('iPad') > -1, //是否iPad
				 webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
	 		};
	 	}(),
	 	language: (navigator.browserLanguage || navigator.language).toLowerCase()
	 }

	 if (browser.versions.ios || browser.versions.iPhone || browser.versions.iPad) {
	 	/* window.location="https://itunes.apple.com/xxx"; */
	 	window.location="http://www.baidu.com";
	 }else if (browser.versions.android) {
	 	window.location="http://xxx/xxx.apk";
	 }
}