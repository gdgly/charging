<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
<%@include file="../../common/global/meta.jsp"%>
<style type="text/css"> 
ul,li{margin:0;padding:0} 
#scrollDiv{width:300px;height:100px;min-height:25px;line-height:25px;border:#ccc 1px solid;overflow:hidden} 
#scrollDiv li{height:25px;padding-left:10px;} 
</style> 
</head>
<body>
<%@include file="../head.jsp"%>
<div class="bg">
 <!--main-content-->
<div class="container main-body">
  <div class="row">
  <div class="col-sm-2">
   <h3>设备监控</h3>
  </div>
   <div class="col-sm-offset-8 col-sm-2 text-right" style="margin-top: 21px;margin-bottom: 21px;">
   <button id="flushStatusBtn" type="button" class="btn btn-primary">
   <span class="glyphicon glyphicon-refresh"></span> 刷新</button>
  </div>
  </div>

<div id="scrollDiv"> 
<ul> 
<li>百度 www.baidu.com</li> 
<li>脚本之家 www.jb51.net</li> 
<li>这是公告标题的第三行</li> 
<li>这是公告标题的第四行</li> 
<li>这是公告标题的第五行</li> 
<li>这是公告标题的第六行</li> 
<li>这是公告标题的第七行</li> 
<li>这是公告标题的第八行</li> 
</ul> 
</div> 
        
 

</div>
</div>
  	<%@include file="../../common/global/js.jsp" %>
  	 <script src="res/js/bussiness/util.js" type="text/javascript"></script>
	<%@include file="../foot.jsp"%>
</body>
<script type="text/javascript">
//滚动插件 
(function($){ 
$.fn.extend({ 
Scroll:function(opt,callback){ 
//参数初始化 
if(!opt) var opt={}; 
var _this=this.eq(0).find("ul:first"); 
var lineH=_this.find("li:first").height(), //获取行高 
line=opt.line?parseInt(opt.line,10):parseInt(this.height()/lineH,10), //每次滚动的行数，默认为一屏，即父容器高度 
speed=opt.speed?parseInt(opt.speed,10):500, //卷动速度，数值越大，速度越慢（毫秒） 
timer=opt.timer?parseInt(opt.timer,10):3000; //滚动的时间间隔（毫秒） 
if(line==0) line=1; 
var upHeight=0-line*lineH; 
//滚动函数 
scrollUp=function(){ 
_this.animate({ 
marginTop:upHeight 
},speed,function(){ 
for(i=1;i<=line;i++){ 
_this.find("li:first").appendTo(_this); 
} 
_this.css({marginTop:0}); 
}); 
} 
//鼠标事件绑定 
_this.hover(function(){ 
clearInterval(timerID); 
},function(){ 
timerID=setInterval("scrollUp()",timer); 
}).mouseout(); 
} 
}) 
})(jQuery); 
$(document).ready(function(){ 
$("#scrollDiv").Scroll({line:4,speed:500,timer:3000}); 
}); 
</script>
</html>

