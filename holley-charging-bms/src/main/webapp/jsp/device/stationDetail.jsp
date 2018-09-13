<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>充电点详细信息</title>
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
	      <label class="col-sm-2 control-label">充电点名称</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargeStation.stationName"/></p>
	      </div>
	      <label class="col-sm-2 control-label">经纬度</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargeStation.lng"/>&nbsp;,&nbsp;<s:property value="#request.chargeStation.lat"/><span id="showMapBtn" class="glyphicon glyphicon-map-marker" style="top:3px;cursor:pointer;font-size: 17px;"></span></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">所在地区</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static">
	      		<s:property value="#request.chargeStation.provinceName"/>
	      		<s:if test="#request.chargeStation.provinceName != '' && #request.chargeStation.cityName != ''">&nbsp;,&nbsp;</s:if>
	      		<s:property value="#request.chargeStation.cityName"/>
	      	</p>
	      </div>
	      <label class="col-sm-2 control-label">详细地址</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargeStation.address"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">运营机构</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargeStation.busMecName"/></p>
	      </div>
	      <label class="col-sm-2 control-label">运营类型</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargeStation.busTypeDesc"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">开放日</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargeStation.openDayDesc"/></p>
	      </div>
	      <label class="col-sm-2 control-label">开放时间</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargeStation.openTimeDesc"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">停车场类型</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargeStation.parkTypeDesc"/></p>
	      </div>
	      <label class="col-sm-2 control-label">联系电话</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargeStation.linkPhone"/></p>
	      </div>
    	</div>
    	<%-- <div class="form-group">
    	 <label class="col-sm-2 control-label">停车是否收费</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargeStation.isParkFeeDesc"/></p>
	      </div>
	      <label class="col-sm-2 control-label">联系人</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargeStation.linkMan"/></p>
	      </div>
    	</div> --%>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">快桩数量</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargeStation.fastNum"/></p>
	      </div>
	      <label class="col-sm-2 control-label">慢桩数量</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargeStation.slowNum"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">更新时间</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargeStation.updateTimeStr"/></p>
	      </div>
	      <label class="col-sm-2 control-label">投运时间</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargeStation.operatTimeStr"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">图片</label>
	      <div class="col-sm-4">
	      	<s:if test="#request.chargeStation.img != null && #request.chargeStation.img != ''">
		      	<img class="img-thumbnail img-thumbnail-size" alt="" src="${imgUrl}<s:property value="#request.chargeStation.img"/>"
		      	     onmouseenter="openImg(this,'充电点')" onmouseleave="closeImg(this)">
	      	</s:if>
	      </div>
	      <label class="col-sm-2 control-label">备注</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargeStation.remark"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">评分</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargeStation.score"/></p>
	      </div>
	      <label class="col-sm-2 control-label">是否认证</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static">
	      		<s:if test="#request.chargeStation.isValidate == 1">是</s:if>
	      		<s:elseif test="#request.chargeStation.isValidate == 2">否</s:elseif>
	      	</p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">数据来源</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.chargeStation.dataSource"/></p>
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
							<p><s:property value="#request.chargeStation.lng"/></p>
						</div>
						<label for="latMap" class="col-sm-1 control-label">纬度：</label>
						<div class="col-sm-2">
							<p><s:property value="#request.chargeStation.lat"/></p>
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
  	var lng = <s:property value="#request.chargeStation.lng"/>;
  	var lat = <s:property value="#request.chargeStation.lat"/>;
  	var id = <s:property value="#request.chargeStation.id"/>;
  	var map;
  	
  	$(document).ready(function(){
  		initMap();
  		$("#showMapBtn").on("click",function(){
  			$("#pointMapModal").modal();
  			setTimeout(function(){    
  				  map.panTo(point);    
  			}, 500); 
  		});
  	});
  	
  	function initMap(){
//  		 百度地图API功能
  		map = new BMap.Map("mapContainer", {enableMapClick : false});
  		map.addControl(new BMap.NavigationControl());//添加缩放平移控件
  		map.addControl(new BMap.OverviewMapControl());//添加缩略图控件
  		map.enableScrollWheelZoom();//允许使用鼠标滚轮进行地图缩放
  		
  		point = new BMap.Point(lng , lat); // 创建点坐标
  		map.centerAndZoom(point, 19);
  		var marker = new BMap.Marker(point);  // 创建标注
  		map.addOverlay(marker);               // 将标注添加到地图中
  		marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
  		setTimeout(function(){    
  			  map.panTo(point);    
  		}, 500);
  	}
  </script>
 
</body>
</html>

