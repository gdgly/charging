<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<%@include file="../../common/global/meta.jsp"%>
<%@include file="../../common/global/js.jsp"%>
<%@include file="../head.jsp"%>
<html lang="zh-CN">
<head>
<style type="text/css">
</style>
</head>
<body>
	<!--main-->
	<div class="bg">
		<!--main-content-->
		<div class="container main-body">
			<div class="row">
				<div class="col-sm-3">
					<h3>设备基本信息</h3>
				</div>
				<div class="col-sm-offset-7 col-sm-2 text-right"
					style="margin-top: 21px;">
					 <s:if test="#request.validOrUnValid == 'valid'">
					 <button onclick="href('deviceManager/searchValidDevice.action');" type="button"
							class="btn btn-warning btn-sm">返回</button>
					 </s:if>
					 <s:elseif test="#request.validOrUnValid == 'unValid'">
					 <button onclick="href('deviceManager/searchUnValidDevice.action');" type="button"
							class="btn btn-warning btn-sm">返回</button>
					 </s:elseif>
						
				</div>
			</div>
			<hr />
			<form class="form-horizontal" role="form" id="stationForm"
				enctype="multipart/form-data">
				<div class="form-group">
					<label for="stationName" class="col-sm-2 control-label">充电点名称：</label>
					<div class="col-sm-4">
						<input disabled="disabled" type="text" class="form-control" id="stationName"
							name="stationName"
							value="<s:property value='#request.pobChargingStation.stationName'/>" />
					</div>
					<label for="lng" class="col-sm-2 control-label">经纬度：</label>
					<div class="col-sm-4">
					<div class="form-group">
						<div class="col-sm-5">
							<input disabled="disabled" type="text" class="form-control" id="lng" name="lng"
							value=<s:property value='#request.pobChargingStation.lng'/> />
							</div>
						<div class="col-sm-5">
							<input disabled="disabled" type="text" class="form-control" id="lat" name="lat"
							value=<s:property value='#request.pobChargingStation.lat'/> />
							</div>
						<div class="col-sm-2">
						
							<lable id="pointBtn" class="glyphicon glyphicon-map-marker"
							style="top:7px;cursor:pointer;font-size: 20px;" />
						</div>
					</div>
				
				</div>
				</div>
				<div class="form-group">
					<label for="province" class="col-sm-2 control-label">所在地区：</label>
					<div class="col-sm-2">
						<select disabled="disabled" id="province" name="province" class="form-control">
							<option value="0">请选择省份</option>
							<s:iterator value="#request.provinceList" status="statu"
								id="item">
								<s:if test="id==#request.pobChargingStation.province">
									<option selected="selected" value="<s:property value='id'/>"><s:property
											value="name" />
									</option>
								</s:if>
								<option value="<s:property value='id'/>"><s:property
										value="name" />
								</option>
							</s:iterator>
						</select>
					</div>
					<div class="col-sm-2">
						<select disabled="disabled" id="city" name="city" class="form-control">

							<s:if test="#request.cityList != null">
								<s:iterator value="#request.cityList" status="statu" id="item">
									<s:if test="id==#request.pobChargingStation.city">
										<option selected="selected" value="<s:property value='id'/>"><s:property
												value="name" />
										</option>
									</s:if>
									<option value="<s:property value='id'/>"><s:property
											value="name" />
									</option>
								</s:iterator>
							</s:if>
							<s:else>
								<option value="0">请先选择市区</option>
							</s:else>
						</select>
					</div>
						<label for="parkType" class="col-sm-2 control-label">停车场类型：</label>
					<div class="col-sm-4">
						<s:iterator value="#request.parkTypeList" status="statu" id="item">
							<s:if test="value == #request.pobChargingStation.parkType">
								<label class="checkbox-inline"> <input type="radio" disabled="disabled"
									name="parkType" id="parkType"
									value="<s:property value='value'/>" checked> <s:property
										value='name' />
								</label>
							</s:if>
							<s:else>
								<label class="checkbox-inline"> <input type="radio" disabled="disabled"
									name="parkType" id="parkType"
									value="<s:property value='value'/>"> <s:property
										value='name' />
								</label>
							</s:else>
						</s:iterator>
					</div>
				</div>
				<div class="form-group">
					<label for="busMec" class="col-sm-2 control-label">运营机构：</label>
					<div class="col-sm-4">
						<input disabled="disabled" id="busMec" name="busMec"
							class="form-control"
							value="<s:property value='#request.busBussinessInfo.busName'/>" />
					</div>
					<label for="busType" class="col-sm-2 control-label">运营类型：</label>
					<div class="col-sm-4">
						<select disabled="disabled" id="busType" name="busType"
							class="form-control">
							<s:iterator value="#request.busTypeList" status="statu" id="item">
								<p>
									<s:property value='value' />
								</p>
								<s:if test="value == #request.currentUser.usertype.getValue()">
									<option selected="selected" value="<s:property value='value'/>"><s:property
											value="name" /></option>
								</s:if>
								<s:else>
									<option value="<s:property value='value'/>"><s:property
											value="name" /></option>
								</s:else>
							</s:iterator>
						</select>
					</div>
				</div>
								<div class="form-group">

					<label for="linkPhone" class="col-sm-2 control-label">开放日：</label>
					<div class="col-sm-4">
						<select disabled="disabled" id="openDay" name="openDay"
							class="form-control">
							<option value="0">请选择</option>
							<s:iterator value="#request.openDayList" status="statu" id="item">
							<s:if test="value == #request.pobChargingStation.openDay">
							<option selected="selected" value=<s:property value="value"/> ><s:property value="name"/></option>
							</s:if>
							<s:else>
							<option value=<s:property value="value"/> ><s:property value="name"/></option>
							</s:else>
							
							</s:iterator>
						</select>
					</div>
					
					<label for="linkPhone" class="col-sm-2 control-label">开放时间：</label>
					<div class="col-sm-4">
						<select disabled="disabled" id="openTime" name="openTime"
							class="form-control">
							<option value="0">请选择</option>
							<s:iterator value="#request.openTimeList" status="statu" id="item">
							<s:if test="value == #request.pobChargingStation.openTime">
							<option selected="selected" value=<s:property value="value"/> ><s:property value="name"/></option>
							</s:if>
							<s:else>
							<option value=<s:property value="value"/> ><s:property value="name"/></option>
							</s:else>
							</s:iterator>
						</select>
					</div>
				
				</div>

				<div class="form-group">
					<label for="editStationImg" class="col-sm-2 control-label">图片上传：</label>
					<div class="col-sm-4">
						<s:if test="#request.pobChargingStation.img != null">
							<img id="showEditStationImg" alt="ss" class="img-thumbnail"
								style="height: 20%; width: 100%;"
								src="${imgUrl}${pobChargingStation.img}" />
						</s:if>
						<s:else>
							<img id="showEditStationImg" alt="ss" class="img-thumbnail"
								style="height: 20%; width: 100%;"
								src="${imgUrl}data/stationImg/stationDefault.jpg" />
						</s:else>
					</div>
					<label for="linkPhone" class="col-sm-2 control-label">联系电话：</label>
					<div class="col-sm-4">
						<input disabled="disabled" type="text" class="form-control" id="linkPhone"
							name="linkPhone"
							value="<s:property value='#request.pobChargingStation.linkPhone'/>" />
					</div>
				</div>
				<div class="form-group">
					<label for="address" class="col-sm-2 control-label">详细地址：</label>
					<div class="col-sm-4">
						<textarea disabled="disabled" type="text" class="form-control" id="address" rows="3"
							name="address"><s:property value='#request.pobChargingStation.address '/></textarea>
					</div>
					<label for="validRemark" class="col-sm-2 control-label">审核备注：</label>
					<div class="col-sm-4">
						<textarea disabled="disabled" id="remark" name="remark" class="form-control" rows="3"><s:property
								value='#request.pobChargingStation.validRemark ' /></textarea>
					</div>
				</div>
			</form>
		</div>
	</div>
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="pointMapModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 880px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
				</div>
				<div class="modal-body" id="baiduMap"
					style="height: 70%; overflow: hidden; margin: 0; font-family:"微软雅黑";">

				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭
					</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal -->
	</div>
	<script type="text/javascript"
		src="http://api.map.baidu.com/api?v=2.0&ak=A4749739227af1618f7b0d1b588c0e85"></script>
</body>
<script type="text/javascript">
setBowHeight1();
commomChangeImg("editStationImg","showEditStationImg");//初始化图片修改
	var map;
	var point;
	var defaultSize = 15;

	// 百度地图API功能
	function myyFun(result) {
		var cityName = result.name;
		if (cityName) {
			map.centerAndZoom(cityName, defaultSize);
		} else {
			map.centerAndZoom("北京市", defaultSize);
		}

	}


	function initMap(obj, lng, lat) {
		map = new BMap.Map(obj, {
			enableMapClick : false
		}); // 创建地图实例
		map.enableScrollWheelZoom();
		if (lng && lat) {
			point = new BMap.Point(lng, lat);
			map.centerAndZoom(point, defaultSize);
			var marker = new BMap.Marker(point); // 创建标注
			map.addOverlay(marker); // 将标注添加到地图中
			marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
	
		} else {
			var myCity = new BMap.LocalCity();//定位当前城市
			myCity.get(myyFun); // 初始化地图,设置城市和地图级别。
		}

	}

	$(function() {
		initMap("baiduMap", $("#lng").val(), $("#lat").val());//初始化地图
		//打开地图事件
		$("#pointBtn").on("click", function() {
			window.setTimeout(function(){    
				  map.panTo(point);   
				 }, 500); 
			$("#pointMapModal").modal();
		});
	})
</script>
</html>

