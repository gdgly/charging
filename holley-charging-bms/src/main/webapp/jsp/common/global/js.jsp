<%--js放在页面底部、此处是全局js--%>
<%@ page contentType="text/html;charset=UTF-8"  language="java" pageEncoding="UTF-8" %>

<script src="//cdn.bootcss.com/jquery/2.2.1/jquery.js" charset="UTF-8"></script>
<script src="//cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js" type="text/javascript"></script>
<script src="//cdn.bootcss.com/less.js/2.6.0/less.min.js" type="text/javascript"></script>

<script src="<%=request.getContextPath()%>/res/js/common/moment-with-locales.js"></script>
<script src="<%=request.getContextPath()%>/res/js/common/bootstrap-datetimepicker.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/res/js/common/jquery.goup.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/res/js/common/jquery-form.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/res/js/common/xcConfirm.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/res/js/common/constants.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/res/js/common/enum-constants.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/res/js/common/common.js" type="text/javascript"></script>

<!-- 在IE下测试，placeholder不被支持,所以引入jquery.placeholder.js，并加入以下代码 -->
<!--[if lte IE 9]>
<script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
<script src="//cdn.bootcss.com/html5shiv/r29/html5.js"></script>
<script src="//cdn.bootcss.com/jquery-placeholder/2.3.1/jquery.placeholder.min.js"></script>
<script type="text/javascript">
    $(function () {
        $('input, textarea').placeholder();
    });
</script>
<![endif]-->

