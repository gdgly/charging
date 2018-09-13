<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>运营商审核信息</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
    <form class="form-horizontal" role="form">
    	<div class="form-group">
	      <label class="col-sm-2 control-label">充电站名称</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempStation.stationName"/></p>
	      </div>
	      <label class="col-sm-2 control-label">经纬度</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempStation.lng"/>&nbsp;,&nbsp;<s:property value="#request.tempStation.lat"/><span id="showMapBtn" class="glyphicon glyphicon-map-marker" style="top:3px;cursor:pointer;font-size: 17px;"></span></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">所在地区</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static">
	      		<s:property value="#request.tempStation.provinceName"/>
	      		<s:if test="#request.tempStation.provinceName != '' && #request.tempStation.cityName != ''">&nbsp;,&nbsp;</s:if>
	      		<s:property value="#request.tempStation.cityName"/></p>
	      </div>
	      <label class="col-sm-2 control-label">详细地址</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempStation.address"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">运营机构</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempStation.busMecName"/></p>
	      </div>
	      <label class="col-sm-2 control-label">运营类型</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempStation.busTypeDesc"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">开放日</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempStation.openDayDesc"/></p>
	      </div>
	      <label class="col-sm-2 control-label">开放时间</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempStation.openTimeDesc"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">停车场类型</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempStation.parkTypeDesc"/></p>
	      </div>
	      <label class="col-sm-2 control-label">联系电话</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempStation.linkPhone"/></p>
	      </div>
    	</div>
    	<%-- <div class="form-group">
    	  <label class="col-sm-2 control-label">停车是否收费</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempStation.isParkFeeDesc"/></p>
	      </div>
	      <label class="col-sm-2 control-label">联系人</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempStation.linkMan"/></p>
	      </div>
    	</div> --%>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">更新时间</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempStation.updateTimeStr"/></p>
	      </div>
	      <label class="col-sm-2 control-label">投运时间</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempStation.operatTimeStr"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">图片</label>
	      <div class="col-sm-4">
	      	<s:if test="#request.tempStation.img != null && #request.tempStation.img != ''">
		      	<img class="img-thumbnail img-thumbnail-size" alt="" src="${imgUrl}<s:property value="#request.tempStation.img"/>"
		      	     onmouseenter="openImg(this,'充电站')" onmouseleave="closeImg(this)">
	      	</s:if>
	      </div>
	      <label class="col-sm-2 control-label">备注</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempStation.remark"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">审核状态</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempStation.validStatusDesc"/></p>
	      </div>
	      <label class="col-sm-2 control-label">审核时间</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempStation.validTimeStr"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">审核备注</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.tempStation.validRemark"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <div class="col-sm-offset-2 col-sm-10">
	         <button id="backBtn" type="button" class="btn btn-primary" onclick="javascript:history.back(-1);">返回</button>
	      </div>
   		</div>
    </form>
</div>
  <!-- 模态框（Modal） -->
	<div class="modal fade" id="pointMapModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 70%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<div class="form-group">
						<label for="lngMap" class="col-sm-1 control-label">经度：</label>
						<div class="col-sm-2">
							<p><s:property value="#request.tempStation.lng"/></p>
						</div>
						<label for="latMap" class="col-sm-1 control-label">纬度：</label>
						<div class="col-sm-2">
							<p><s:property value="#request.tempStation.lat"/></p>
						</div>
					</div>
				</div>
				<div class="modal-body" id="mapContainer" style="height: 70%; overflow: hidden; margin: 0; font-family:'微软雅黑';">
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal -->
	</div>
  <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=ag2fYhtdY5BgQWqi6Xwsx0Pp"></script>
  <script type="text/javascript">
  	var lng = <s:property value="#request.tempStation.lng"/>;
  	var lat = <s:property value="#request.tempStation.lat"/>;
  	var id = <s:property value="#request.tempStation.id"/>;
  </script>
  <script src="res/js/device/stationVerify.js" type="text/javascript"></script>
</body>
</html>

