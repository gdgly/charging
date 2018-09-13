var checkWechatPayStatusUrl = "userjson/account_checkWechatPayStatus.action";
var cancelWechatPayUrl = "userjson/account_cancelWechatPay.action";
var initUrl = "userAccount/initUserAccount.action";
var param = {};
var timer = 5;
function setTime(){
	$("#timeCount").text(timer);
	if(timer <= 0){
		 href(initUrl);
	}
	timer--;
}
function returnResult(){
	  clearInterval(t1);
	  href(initUrl);
	  /*$("#body1").addClass("hide");
	  $("#body2").removeClass("hide");
	  $("#header1").addClass("hide");
	  $("#header2").removeClass("hide");
	  setInterval("setTime()", 1000); */
}
function checkWechatPayStatus(){
	param.out_trade_no = $("#out_trade_no").val();
	if(isEmpty(param.out_trade_no)){
		opt={};
		opt.onOk = function(){href(initUrl);}
		opt.onClose = opt.onOk;
		showWarning("充值有误请重新操作！！",opt);
		return;
	}
	$.ajax({
	      url:checkWechatPayStatusUrl,// 跳转到 action  
	      data:param,  
	      type:'post',  
	      cache:false,  
	      dataType:'json', 
	      success:function(data){
	    	  if("success" == data.message){
	    		  returnResult();
	    	  }else if("error" == data.message){
	    		opt={};
	  			opt.onOk = function(){href(initUrl);}
	  			opt.onClose = opt.onOk;
	  			showWarning("充值有误请重新操作！！",opt);  
	    	  }
	      },
	      error : function() {  
	           showWarning("异常！");
	      } 
	});
}
var t1 = setInterval("checkWechatPayStatus()", 3000);



function cancelWechatPay(){
	$.ajax({
	      url:cancelWechatPayUrl,// 跳转到 action  
	      data:param,  
	      type:'post',  
	      cache:false,  
	      dataType:'json', 
	      success:function(data){
	    	  href(initUrl);
	      },
	      error : function() {  
	           showWarning("异常！");
	      } 
	});
}
$("#cancelWechatPayBtn").on("click",function(){
	opt={};
	opt.onOk = function(){
		  href(initUrl);
	}
	showConfirm("确定取消当前充值？",opt);	
});