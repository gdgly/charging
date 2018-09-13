<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
    <!-- Content Header (Page header) -->
    <section class="content-header">
       <h1>我的设备</h1> 
      <ol class="breadcrumb">
       	<li>
      	  	<a href="javascript:;" onclick="frameReload();">
      	 		<i class="fa fa-home"></i> 首页
      	 	</a>
      	 </li>
        <li><i class="fa fa-laptop"></i> 设备管理</li>
        <li class="active">我的设备</li>
      </ol>
    </section>
    <!-- Main content -->
<section class="content">
   <div class="row">
       <div class="col-xs-12">
        <div class="box box-info">
            <div class="box-header with-border">
              <h3 class="box-title">修改设备</h3>
					<s:if test="#request.actionType == 'editValidStation'">
						<button onclick="href('userDevice/initUserDevice.action');" type="button" style="margin-left: 10px;" class="btn btn-info btn-sm pull-right">
							返回
						</button>
					</s:if>
					<s:elseif test="#request.actionType == 'editNewStation'">
						<button onclick="href('userDevice/initUserUnDevice.action');" type="button" style="margin-left: 10px;" class="btn btn-info btn-sm pull-right">
							返回
						</button>
					</s:elseif>
					<s:else>
						<button onclick="history.go(-1);" type="button" style="margin-left: 10px;" class="btn btn-info btn-sm pull-right">
							返回
						</button>
					</s:else>
						<button disabled="disabled" id="saveStationBtn" type="button" class="btn btn-info btn-sm pull-right">
							保存
						</button>
            </div>
            <!-- /.box-header -->
            <!-- form start -->
           <div class="box-body">
            <form class="form-horizontal" role="form" id="stationForm" enctype="multipart/form-data">
				<div class="form-group">
					<label for="stationName" class="col-sm-2 control-label">
						<span style="color: red;">*</span>充电点名称：
					</label>
					<div class="col-sm-4">
						<input type="text" maxlength="30" class="form-control" id="stationName" name="stationName" value="<s:property value='#request.pobChargingStation.stationName'/>" />
					</div>
					<label for="lng" class="col-sm-2 control-label">
						<span style="color: red;">*</span>经纬度：
					</label>
				<div class="col-sm-4">
					<div class="form-group">
						<div class="col-sm-5">
							<input type="text" maxlength="30" class="form-control" id="lng" name="lng" value=<s:property value='#request.pobChargingStation.lng'/> />
						</div>
						<div class="col-sm-5">
							<input type="text" maxlength="30" class="form-control" id="lat" name="lat" value=<s:property value='#request.pobChargingStation.lat'/> />
						</div>
						<div class="col-sm-2">
							<lable id="pointBtn" class="glyphicon glyphicon-map-marker" style="top:7px;cursor:pointer;font-size: 20px;" />
						</div>
					</div>
				
				</div>
					
				</div>
				<div class="form-group">
					<label for="province" class="col-sm-2 control-label">
						<span style="color: red;">*</span>所在地区：
					</label>
					<div class="col-sm-2">
						<select id="province" name="province" class="form-control">
							<option value="0">请选择省份</option>
							<s:iterator value="#request.provinceList" status="statu" id="item">
								<s:if test="id==#request.pobChargingStation.province">
									<option selected="selected" value="<s:property value='id'/>">
										<s:property value="name" />
									</option>
								</s:if>
								<s:else>
									<option value="<s:property value='id'/>">
										<s:property value="name" />
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
										<option selected="selected" value="<s:property value='id'/>">
											<s:property value="name" />
										</option>
									</s:if>
									<s:else>
										<option value="<s:property value='id'/>">
											<s:property value="name" />
										</option>
									</s:else>
								</s:iterator>
							</s:if>
							<s:else>
								<option value="0">请先选择市区</option>
							</s:else>
						</select>
					</div>
						<label for="parkType" class="col-sm-2 control-label">
							<span style="color: red;">*</span>停车场类型：
						</label>
					<div class="col-sm-4">
						<s:iterator value="#request.parkTypeList" status="statu" id="item">
							<s:if test="value == #request.pobChargingStation.parkType">
								<label class="checkbox-inline"> 
									<input type="radio" name="parkType" id="parkType" value="<s:property value='value'/>" checked> 
									<s:property value='name' />
								</label>
							</s:if>
							<s:else>
								<label class="checkbox-inline"> 
									<input type="radio" name="parkType" id="parkType" value="<s:property value='value'/>"> 
									<s:property value='name' />
								</label>
							</s:else>
						</s:iterator>
					</div>
				</div>
				<div class="form-group">
					<label for="linkPhone" class="col-sm-2 control-label">
						<span style="color: red;">*</span>开放日：
					</label>
					<div class="col-sm-4">
						<select id="openDay" name="openDay" class="form-control">
								<option value="0">
									请选择
								</option>
							<s:iterator value="#request.openDayList" status="statu" id="item">
							<s:if test="value == #request.pobChargingStation.openDay">
								<option selected="selected" value=<s:property value="value"/> >
									<s:property value="name"/>
								</option>
							</s:if>
							<s:else>
								<option value=<s:property value="value"/> >
									<s:property value="name"/>
								</option>
							</s:else>
							</s:iterator>
						</select>
					</div>
					
					<label for="linkPhone" class="col-sm-2 control-label">
						<span style="color: red;">*</span>开放时间：
					</label>
					<div class="col-sm-4">
						<select id="openTime" name="openTime" class="form-control">
							<option value="0">
								请选择
							</option>
							<s:iterator value="#request.openTimeList" status="statu" id="item">
							<s:if test="value == #request.pobChargingStation.openTime">
							<option selected="selected" value=<s:property value="value"/> >
								<s:property value="name"/>
							</option>
							</s:if>
							<s:else>
							<option value=<s:property value="value"/> >
								<s:property value="name"/>
							</option>
							</s:else>
							</s:iterator>
						</select>
					</div>
				</div>

				<div class="form-group">
					<label for="editStationImg" class="col-sm-2 control-label">
						<span style="color: red;">*</span>图片上传：
					</label>
					<div class="col-sm-4">
						<s:if test="#request.pobChargingStation.img != null">
							<img id="showEditStationImg" alt="ss" class="img-thumbnail" style="height: 15%; width: 50%;" src="${imgUrl}${pobChargingStation.img}" />
						</s:if>
						<s:else>
							<img id="showEditStationImg" alt="ss" class="img-thumbnail" style="height: 20%; width: 50%;" src="${imgUrl}data/stationImg/stationDefault.jpg" />
						</s:else>
							<input type="file" name="img" id="editStationImg" />
					</div>
					<label for="linkPhone" class="col-sm-2 control-label">
						<span style="color: red;">*</span>联系电话：
					</label>
					<div class="col-sm-4">
						<input type="text" maxlength="20" class="form-control" id="linkPhone" name="linkPhone" value="<s:property value='#request.pobChargingStation.linkPhone'/>" />
					</div>
				</div>
				<div class="form-group">
					<label for="address" class="col-sm-2 control-label">
						<span style="color: red;">*</span>详细地址：
					</label>
					<div class="col-sm-4">
						<textarea type="text" maxlength="150" class="form-control" id="address" rows="3" name="address"><s:property value='#request.pobChargingStation.address '/>
						</textarea>
					</div>
					<label for="remark" class="col-sm-2 control-label">
						备注：
					</label>
					<div class="col-sm-4">
						<textarea placeholder="备注信息..." maxlength="150" id="remark" name="remark" class="form-control" rows="3"><s:property value='#request.pobChargingStation.remark ' /></textarea>
					</div>
				</div>
			</form>
				<input type="hidden" id="actionType" name="actionType" value="${actionType}" /> 
				<input type="hidden" id="stationId" name="stationId" value="${stationId}" />
          </div>
         </div>
        </div>
      </div>
</section>
<!-- 模态框（Modal） -->
	<div class="modal fade" id="pointMapModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 80%;height: 70%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
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
							<input type="text" class="form-control" id="searchName" placeholder="请输入搜索地址" />
						</div>
						<div class="col-sm-2">
							<button class="btn btn-info" id="searchBtn">搜索</button>
						</div>
					</div>
				</div>

				<div class="modal-body" id="baiduMap" style="height: 70%; overflow: hidden; margin: 0; font-family:"微软雅黑";">

				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">
						关闭
					</button>
					<button id="editLngLatBtn" type="button" class="btn btn-info">
						提交更改
					</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal -->
	</div>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=A4749739227af1618f7b0d1b588c0e85"></script>
<script src="res/js/user/device/editUserStation.js"></script>
