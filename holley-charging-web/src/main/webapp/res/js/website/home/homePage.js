$(document).ready(function(){
	$('#content').fullpage({
		verticalCentered:false
	});
	$('.carousel').carousel({
	     interval: 2000
	    });
});

function openImg(obj,codetype){
	$(obj).popover({ 
		html : true,
		title: function() {
			var str = "";
			if(codetype == "ios"){
				str = "IOS客户端";
			}else if(codetype == "andriod"){
				str = "Andriod客户端";
			}else{
				str = "关注51充电";
			}
		 return str;
		},
		content: function() {
			var html = "";
			html += '<div id="appcode_popover">'; 
			html += '<div class="thumbnail" style="width: 200px;height: 200px;">';
			html += '<img alt="" src="'+$(obj).attr("src")+'">';
		 	html += '</div></div>';
		 return html;
		}
	});
	 $(obj).popover("show");
}

function closeImg(obj){
	$(obj).popover("hide");
}

//$(function () {
//    $("[data-toggle='tooltip']").tooltip({delay:{show:0,hide:500}});
//});





