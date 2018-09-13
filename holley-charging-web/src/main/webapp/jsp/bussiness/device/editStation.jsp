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
					<h3>修改设备基本信息</h3>
				</div>
				<!--        <div class="form-group">

       <div class="col-sm-offset-6 col-sm-6">
         <button id="saveStationBtn" type="button" class="btn btn-success">保存</button>
         <button id="testBtn" type="button" class="btn btn-success">打点</button>
      </div>
   </div> -->

				<div class="col-sm-offset-7 col-sm-2 text-right"
					style="margin-top: 21px;">
					<button disabled="disabled" id="saveStationBtn" type="button" class="btn btn-warning btn-sm">保存</button>
					<s:if test="#request.actionType == 'editValidStation'">
						<button onclick="href('deviceManager/searchValidDevice.action');"
							type="button" class="btn btn-warning btn-sm">返回</button>
					</s:if>
					<s:elseif test="#request.actionType == 'editNewStation'">
						<button
							onclick="href('deviceManager/searchUnValidDevice.action');"
							type="button" class="btn btn-warning btn-sm">返回</button>
					</s:elseif>
					<s:else>
						<button onclick="history.go(-1);" type="button"
							class="btn btn-warning btn-sm">返回</button>
					</s:else>
				</div>
			</div>
			<hr />
			<form class="form-horizontal" role="form" id="stationForm"
				enctype="multipart/form-data">
				<div class="form-group">
					<label for="stationName" class="col-sm-2 control-label"><span
						style="color: red;">*</span>充电点名称：</label>
					<div class="col-sm-4">
						<input maxlength="30" type="text" class="form-control" id="stationName"
							name="stationName"
							value="<s:property value='#request.pobChargingStation.stationName'/>" />
					</div>
								<label for="lng" class="col-sm-2 control-label"><span
						style="color: red;">*</span>经纬度：</label>
				<div class="col-sm-4">
					<div class="form-group">
						<div class="col-sm-5">
							<input  maxlength="30" type="text" class="form-control" id="lng" name="lng"
							value=<s:property value='#request.pobChargingStation.lng'/> />
							</div>
						<div class="col-sm-5">
							<input  maxlength="30" type="text" class="form-control" id="lat" name="lat"
							value=<s:property value='#request.pobChargingStation.lat'/> />
							</div>
						<div class="col-sm-2">
						
							<lable id="pointBtn" class="glyphicon glyphicon-map-marker"
							style="top:7px;cursor:pointer;font-size: 20px;" />
						</div>
					</div>
				
				</div>
					<%-- 
					<label for="lng" class="col-sm-2 control-label"><span
						style="color: red;">*</span>经纬度：</label>
					<div class="col-sm-3">
						<input type="text" class="form-control" id="lng" name="lng"
							value="<s:property value='#request.pobChargingStation.lng'/>" />
					</div>
					<div class="col-sm-1">
						<lable id="pointBtn" class="glyphicon glyphicon-map-marker"
							style="top:7px;cursor:pointer;font-size: 20px;" />
					</div> --%>
					
				</div>
				<div class="form-group">
					<label for="province" class="col-sm-2 control-label"><span
						style="color: red;">*</span>所在地区：</label>
					<div class="col-sm-2">
						<select id="province" name="province" class="form-control">
							<option value="0">请选择省份</option>
							<s:iterator value="#request.provinceList" status="statu"
								id="item">
								<s:if test="id==#request.pobChargingStation.province">
									<option selected="selected" value="<s:property value='id'/>"><s:property
											value="name" />
									</option>
								</s:if>
								<s:else>
								<option value="<s:property value='id'/>"><s:property
										value="name" />
								</option>
								</s:else>
								
							</s:iterator>
						</select>
					</div>

					<div class="col-sm-2">
						<select id="city" name="city" class="form-control">

							<s:if test="#request.cityList != null">
								<s:iterator value="#request.cityList" status="statu" id="item">
									<s:if test="id==#request.pobChargingStation.city">
										<option selected="selected" value="<s:property value='id'/>"><s:property
												value="name" />
										</option>
									</s:if>
									<s:else>
										<option value="<s:property value='id'/>"><s:property
											value="name" />
										</option>
									</s:else>
								
								</s:iterator>
							</s:if>
							<s:else>
								<option value="0">请先选择市区</option>
							</s:else>
						</select>
					</div>
						<label for="parkType" class="col-sm-2 control-label"><span
						style="color: red;">*</span>停车场类型：</label>
					<div class="col-sm-4">
						<s:iterator value="#request.parkTypeList" status="statu" id="item">
							<s:if test="value == #request.pobChargingStation.parkType">
								<label class="checkbox-inline"> <input type="radio"
									name="parkType" id="parkType"
									value="<s:property value='value'/>" checked> <s:property
										value='name' />
								</label>
							</s:if>
							<s:else>
								<label class="checkbox-inline"> <input type="radio"
									name="parkType" id="parkType"
									value="<s:property value='value'/>"> <s:property
										value='name' />
								</label>
							</s:else>
						</s:iterator>
					</div>
				</div>


				<div class="form-group">
					<label for="busMec" class="col-sm-2 control-label"><span
						style="color: red;">*</span>运营机构：</label>
					<div class="col-sm-4">
						<input disabled="disabled" id="busMec" name="busMec"
							class="form-control"
							value="<s:property value='#request.busBussinessInfo.busName'/>" />
					</div>
					<label for="busType" class="col-sm-2 control-label"><span
						style="color: red;">*</span>运营类型：</label>
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

					<label for="linkPhone" class="col-sm-2 control-label"><span
						style="color: red;">*</span>开放日：</label>
					<div class="col-sm-4">
						<select id="openDay" name="openDay"
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
					
					<label for="linkPhone" class="col-sm-2 control-label"><span
						style="color: red;">*</span>开放时间：</label>
					<div class="col-sm-4">
						<select id="openTime" name="openTime"
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
					<label for="editStationImg" class="col-sm-2 control-label"><span
						style="color: red;">*</span>图片上传：</label>
					<div class="col-sm-4">
						<s:if test="#request.pobChargingStation.img != null">
							<img id="showEditStationImg" alt="ss" class="img-thumbnail"
								style="height: 20%; width: 50%;"
								src="${imgUrl}${pobChargingStation.img}" />
						</s:if>
						<s:else>
							<img id="showEditStationImg" alt="ss" class="img-thumbnail"
								style="height: 20%; width: 50%;"
								src="${imgUrl}data/stationImg/stationDefault.jpg" />
						</s:else>
						<input type="file" name="img" id="editStationImg" />
					</div>
					<label for="linkPhone" class="col-sm-2 control-label"><span
						style="color: red;">*</span>联系电话：</label>
					<div class="col-sm-4">
						<input maxlength="20" type="text" class="form-control" id="linkPhone"
							name="linkPhone"
							value="<s:property value='#request.pobChargingStation.linkPhone'/>" />
					</div>
				</div>
				<div class="form-group">
					<label for="address" class="col-sm-2 control-label"><span
						style="color: red;">*</span>详细地址：</label>
					<div class="col-sm-4">
						<textarea maxlength="150" placeholder="详细地址最多填写150个字符..." type="text" class="form-control" id="address" rows="3"
							name="address"><s:property value='#request.pobChargingStation.address '/></textarea>
					</div>
					<label for="remark" class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-4">
						<textarea maxlength="150" placeholder="备注信息最多填写150个字符..." id="remark" name="remark" class="form-control" rows="3"><s:property
								value='#request.pobChargingStation.remark ' /></textarea>
					</div>
				</div>
				




			</form>
			<input type="hidden" id="actionType" name="actionType"
				value="${actionType}" /> <input type="hidden" id="stationId"
				name="stationId" value="${stationId}" />
		</div>
	</div>
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="pointMapModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 70%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>



					<div class="form-group">
						<label for="lngMap" class="col-sm-1 control-label">经度：</label>
						<div class="col-sm-2">
							<p type="text" class="form-control" id="lngMap"></p>
						</div>
						<label for="latMap" class="col-sm-1 control-label">纬度：</label>
						<div class="col-sm-2">
							<p type="text" class="form-control" id="latMap"></p>
						</div>
						<div class="col-sm-2">
							<input type="text" class="form-control" id="searchName"
								placeholder="请输入搜索地址" />
						</div>
						<div class="col-sm-2">
							<button class="btn btn-success" id="searchBtn">搜索</button>
						</div>
					</div>
				</div>

				<div class="modal-body" id="baiduMap"
					style="height: 70%; overflow: hidden; margin: 0; font-family:"微软雅黑";">

				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭
					</button>
					<button id="editLngLatBtn" type="button" class="btn btn-primary">
						提交更改</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal -->
	</div>
	<script type="text/javascript"
		src="//api.map.baidu.com/api?v=2.0&ak=A4749739227af1618f7b0d1b588c0e85"></script>
</body>
<script type="text/javascript">
setBowHeight1();
commomChangeImg("editStationImg","showEditStationImg");
	var map;
	var point;
	var defaultSize = 15;
	var marker;
/////////////////////////////////
	function removeDisabled(){
		$("#saveStationBtn").attr("disabled",false); 
	}
	
	function actionFun(obj,action){
		obj.on(action,removeDisabled);
	}
	function initChangeSaveBtn(){
		actionFun($("input[name='stationName']"),"keydown");
		actionFun($("input[name='linkPhone']"),"keydown");
		actionFun($("textarea[name='address']"),"keydown");
		actionFun($("textarea[name='remark']"),"keydown");
		actionFun($("input[name='img']"),"change");
		actionFun($("input[name='parkType']"),"change");
		actionFun($("select[name='province']"),"change");
		actionFun($("select[name='city']"),"change");
		actionFun($("select[name='openDay']"),"change");
		actionFun($("select[name='openTime']"),"change");
	}
///////////////////////////////////
	function cheack(station) {
		if (isEmpty(station.stationName)) {
			return "请输入充电点名称！！";
		} else if (isEmpty(station.lng)) {
			return "请输入经度！！";
		} else if (isEmpty(station.lat)) {
			return "请输入纬度！！";
		} else if (station.province == 0) {
			return "请选择省份！！";
		} else if (station.city == 0) {
			return "请选择市区！！";
		} else if(station.openDay <= 0){
			return "请选择开放日！！";
		}else if(station.openTime <= 0){
			return "请选择开放时间！！";
		}else if(!regBox.regMobile.test(station.linkPhone) && !regBox.regTel.test(station.linkPhone)){
			return  "请输入正确的座机号码电话或手机号码！！";
		} else if (isEmpty(station.address)) {
			return "请输入详细地址！！";
		}
		return "success";
	}

	// 百度地图API功能
	function myyFun(result) {
		var cityName = result.name;
		if (cityName) {
			map.centerAndZoom(cityName, defaultSize);
		} else {
			map.centerAndZoom("北京市", defaultSize);
		}

	}
	//地图智能搜索
	function setPlace(myValue) {
		map.clearOverlays(); //清除地图上所有覆盖物
		function myFun() {
			var pp = local.getResults().getPoi(0).point; //获取第一个智能搜索的结果
			map.centerAndZoom(pp, defaultSize);
			//map.addOverlay(new BMap.Marker(pp));    //添加标注
		}
		var local = new BMap.LocalSearch(map, { //智能搜索
			onSearchComplete : myFun
		});
		local.search(myValue);
	}

	function initMap(obj, lng, lat) {
		map = new BMap.Map(obj, {
			enableMapClick : false
		}); // 创建地图实例
		map.enableScrollWheelZoom();
		if (lng && lat) {
			point = new BMap.Point(lng, lat);
			map.centerAndZoom(point, defaultSize);
			marker = new BMap.Marker(point); // 创建标注
			map.addOverlay(marker); // 将标注添加到地图中
			marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
		} else {
			var myCity = new BMap.LocalCity();//定位当前城市
			myCity.get(myyFun); // 初始化地图,设置城市和地图级别。
		}

		//单击获取点击的经纬度
			map.addEventListener("click", function(e) {
			map.clearOverlays();
			var lng = e.point.lng;
			var lat = e.point.lat;
			$("#lngMap").text(lng);
			$("#latMap").text(lat);
			point = new BMap.Point(lng, lat);
			marker = new BMap.Marker(point);
			map.addOverlay(marker);
			marker.setAnimation(BMAP_ANIMATION_BOUNCE);
		});
	}
	function resetInput() {
		$("#lngMap").text($("#lng").val());
		$("#latMap").text($("#lat").val());
		$("#searchName").val("");
	}

	$(function() {
		var temLng;
		 initMap("baiduMap", $("#lng").val(), $("#lat").val());//初始化地图 
		//禁止修改经度
		$("#lng").on("focus", function() {
			temLng = $("#lng").val();
		});
		$("#lng").on("change", function() {
			$("#lng").val(temLng);
		});

		//禁止修改纬度
		$("#lat").on("focus", function() {
			temLng = $("#lat").val();
		});
		$("#lat").on("change", function() {
			$("#lat").val(temLng);
		});
		//打开地图事件
		$("#pointBtn").on("click", function() {
			map.clearOverlays();
			point = new BMap.Point($("#lng").val(), $("#lat").val());
			marker = new BMap.Marker(point);
			map.addOverlay(marker);
			marker.setAnimation(BMAP_ANIMATION_BOUNCE);
		 	window.setTimeout(function(){    
				  map.panTo(point);    
				 }, 500);  
			$("#pointMapModal").modal();
		});
		//提交打点信息事件
		$("#editLngLatBtn").on("click", function() {
			$("#lng").val($("#lngMap").text());
			$("#lat").val($("#latMap").text());
			$('#pointMapModal').modal("hide");
			removeDisabled();
		});
		//绑定地图搜索事件
		$("#searchBtn").on("click", function() {
			var searchName = $("#searchName").val();
			setPlace(searchName);
		});
		//地图框打开事件
		$('#pointMapModal').on('show.bs.modal', function() {
			resetInput();
		})
		//地图框关闭事件
		$('#pointMapModal').on('hidden.bs.modal', function() {
			resetInput();
		})
		
		var msg;
		var param = {};
		var editStationByAjaxUrl = "device_editStationByAjax.action";
		var finishUrlForValid = "deviceManager/searchValidDevice.action";
		var finishUrlForUnValid = "deviceManager/searchUnValidDevice.action";
		initArea($("#province"), $("#city"));//初始化省市
		resetForm($("#stationForm"));//重置表单
		initChangeSaveBtn();
		$("#saveStationBtn").on("click", function() {
			var station = getFormJson($("#stationForm"));
			msg = cheack(station);
			if ("success" != msg) {
				showWarning(msg)
			}
			else{
			param.pobStation = formDataToJsonString($("#stationForm"));
			param.actionType = $("#actionType").val();
			param.stationId = $("#stationId").val();
			$('#stationForm').ajaxSubmit({
				url : editStationByAjaxUrl,
				type : 'post',
				dataType : 'json',
				data : param,
				beforeSubmit:function(){$("#loading").removeClass("hide")},
				success : function(data) {
					$("#loading").addClass("hide");
					 if(data.userLoginStatus){
			    		  checkLoginStatus(data,true);
			    		  return;
			    	  }
					if ("success" == data.map.msg) {
						/* var opt={};
						opt.onOk = function(){
							if (param.actionType == "editValidStation") {
								href(finishUrlForValid);
							} else if (param.actionType == "editNewStation") {
								href(finishUrlForUnValid);
							} 
						}
						opt.onClose = opt.onOk; */
						showSuccess("修改成功！！")
					} else {
						showWarning(data.map.msg)
					}

				}
			});
			}
		});

	})
</script>
</html>

