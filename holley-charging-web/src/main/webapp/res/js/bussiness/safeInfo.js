/**
 * 安全信息
 */
var param={};
var goRealNameHref="bussiness/realName.action";
var goValidRealNameHref="bussiness/validRealNameInfo.action";
var realSafeLevel;

//----------图片动态显示-----------//






$(function(){
	function initInput(){
		$("#headImg").val("");
	}
	realSafeLevel = $("#realSafeLevel").val();
	if(realSafeLevel <= 2){
		$("#safeLevel").css("width","30%").addClass("progress-bar-danger");
		$("#warn").text("低").css("color","red");;
	}
	else if(realSafeLevel == 3){
		$("#safeLevel").css("width","60%").addClass("progress-bar-warning");
		$("#warn").text("中").css("color","orange");;
	}
	else if(realSafeLevel >= 4){
		$("#safeLevel").css("width","100%").addClass("progress-bar-success");
		$("#warn").text("高").css("color","green");;
	}

	initInput();	
	
	$("#goRealName").on("click",function(){
		href(goRealNameHref);
	});
	$("#goValid").on("click",function(){
		href(goValidRealNameHref);
	});


})