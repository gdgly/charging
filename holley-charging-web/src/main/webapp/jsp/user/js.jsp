<%@ page contentType="text/html;charset=UTF-8"  language="java" pageEncoding="UTF-8" %>
<script src="//cdn.bootcss.com/jquery/2.2.1/jquery.js" charset="UTF-8"></script>
<script src="//cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js" type="text/javascript"></script>
<script src="res/userplugins/js/app.min.js"></script>
<script src="res/userplugins/js/demo.js"></script>
<script src="res/js/common/common.js"></script>
<script src="res/js/common/constants.js"></script>
<script src="res/js/common/xcConfirm.js" type="text/javascript"></script>
<script src="res/js/common/jquery-form.js" type="text/javascript"></script>

<script type="text/javascript">
var IMG_SRC = "${imgUrl}";
var WEB_ROOT = "${webroot}";
$(function(){
	$(".treeview-menu a").on("click",function(){
		$(".treeview-menu a").parent().removeClass("active");
		$(this).parent().addClass("active");
		$("#mainWindow").attr("src",$(this).attr("link"));
	});
})
</script>
