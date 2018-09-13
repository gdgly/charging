$(document).ready(function(){
	fixHeight();
});

//设置content的高度
function fixHeight() {
    $("#main").css("margin-top", ($("#content").height()-$("#main").height())/2);
   
}

function loadAndroid(){
//	alert("下载Android客户端");
}

function loadIOS(){
//	alert("下载IOS客户端");
}