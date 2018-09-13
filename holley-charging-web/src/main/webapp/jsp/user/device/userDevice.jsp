<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
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
 	<div class="box box-info" id="userValidStationPage">
	            <div class="box-header">
	             	<button id="goBackBtn" type="button" style="margin-left: 10px;" class="btn btn-info hide pull-right">返回</button>
	             	<button id="showUnVlaidDeviceBtn" type="button" style="margin-left: 10px;" class="btn btn-info pull-right">待审设备</button>
	            	<button id="addDeviceBtn" type="button" style="margin-left: 10px;" class="btn btn-info pull-right">添加设备</button>
		            <button id="searchBtn" type="button" style="margin-left: 10px;" class="btn btn-info pull-right">搜索</button>
		            <input placeholder="请输入充电点名称" style="width: 20%;" id="searchName" name="searchName" type='text' class="form-control pull-right" />
		           	<span id="exportBtn" title="导出" class="fa fa-file-excel-o inFeeColor" style="cursor: pointer;"></span>
		            <input type="hidden" id="stationId"/>
		            <input type="hidden" id="tempStationName"/>
		          </div>
            	<div class="box-body height400">
             	 <table class="table table-bordered table-hover"  id="userValidStationTable">
             	 <thead>
	                <tr>
	                  <th>充电点名称</th>
	                  <th>地址</th>
	                  <th>地图显示</th>
	                  <th>开放日</th>
	                  <th>开放时间</th>
	                  <th>桩数量(个)</th>
	                  <th>评分</th>
	                  <th>操作</th>
	                </tr>
                </thead>
                <tbody id="userValidStationBody">
                </tbody>
              </table>
              
               <table class="table table-bordered table-hover hide" id="userValidPileTable">
             	 <thead>
	                <tr>
	                  <th>充电桩名称</th>
	                  <th>桩编号</th>
	                  <th>状态</th>
	                  <th>充电类型</th>
	                  <th>充电方式</th>
	                  <th>通讯协议</th>
	                  <th>通讯地址</th>
	                  <th>支持预约</th>
	                  <th>支付方式</th>
	                  <th>操作</th>
	                </tr>
                </thead>
                <tbody id="userValidPileBody">
                </tbody>
              </table>
              
              </div>
              <div id="pageTool1">
              <%@include file="../../common/global/pagingtoolbar.jsp"%>
              </div>
               <div id="pageTool2" class="hide">
              <%@include file="../../common/global/pagingtoolbar2.jsp"%>
              </div>
              </div>
              <!-- /.tab-pane -->
         
          
          <!-- 添加点信息start -->
           <div class="box box-info hide" id="addUserStationPage">
            <div class="box-header with-border">
              <h3 class="box-title">添加设备</h3>
               <button id="goBackStationBtn" type="button" style="margin-left: 10px;" class="btn btn-info pull-right btn-sm">返回</button>
               <button id="saveUserStationBtn" type="button" style="margin-left: 10px;" class="btn btn-info pull-right btn-sm">保存</button>
               <button disabled="disabled" id="addUserPilePageBtn" type="button" class="btn btn-info pull-right btn-sm">添加桩</button>
            </div>
            <!-- /.box-header -->
            <!-- form start -->
            <div class="box-body height400">
            	<form class="form-horizontal" role="form" id="userStationForm" enctype="multipart/form-data">
				<div class="form-group">
					<label for="stationName" class="col-sm-2 control-label">
						<span style="color: red;">*</span>充电点名称：
					</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="stationName" placeholder="充电点名称" name="stationName" value="" maxlength="30"/>
					</div>
					<label for="lng" class="col-sm-2 control-label">
						<span style="color: red;">*</span>经纬度：
					</label>
				<div class="col-sm-4">
					<div class="form-group">
						<div class="col-sm-5">
							<input type="text" class="form-control" id="lng" name="lng" placeholder="经度"  maxlength="30"/>
						</div>
						<div class="col-sm-5">
							<input type="text" class="form-control" id="lat" name="lat" placeholder="纬度"  maxlength="30"/>
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
								<option value="<s:property value='id'/>">
									<s:property value="name" />
								</option>
							</s:iterator>
						</select>
					</div>

					<div class="col-sm-2">
						<select id="city" name="city" class="form-control">
							<option value="0">请先选择市区</option>
						</select>
					</div>
					<label for="parkType" class="col-sm-2 control-label">
						<span style="color: red;">*</span>停车场类型：
					</label>
					<div class="col-sm-4">
						<s:iterator value="#request.parkTypeList" status="statu" id="item">
							<s:if test="#statu.index == 0">
								<label class="checkbox-inline"> 
								<input type="radio" name="parkType" id="parkType" value="<s:property value='value'/>" checked> <s:property value='name' />
								</label>
							</s:if>
							<s:else>
								<label class="checkbox-inline"> 
								<input type="radio" name="parkType" id="parkType" value="<s:property value='value'/>"> <s:property value='name' />
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
						<select id="openDay" name="openDay"
							class="form-control">
							<option value="0">请选择</option>
							<s:iterator value="#request.openDayList" status="statu" id="item">
								<option value=<s:property value="value"/> ><s:property value="name"/></option>
							</s:iterator>
						</select>
					</div>
					
					<label for="linkPhone" class="col-sm-2 control-label">
						<span style="color: red;">*</span>开放时间：
					</label>
					<div class="col-sm-4">
						<select id="openTime" name="openTime"
							class="form-control">
							<option value="0">请选择</option>
							<s:iterator value="#request.openTimeList" status="statu" id="item">
								<option value=<s:property value="value"/> ><s:property value="name"/></option>
							</s:iterator>
						</select>
					</div>
				</div>
				<div class="form-group">
						<label for="addStationImg" class="col-sm-2 control-label">
							<span style="color: red;">*</span>图片上传：
						</label>
					<div class="col-sm-4">
							<img id="showAddStationImg" alt="Image preview" class="img-thumbnail" style="height: 30%; width: 50%;" src="${imgUrl}data/stationImg/stationDefault.jpg" /> 
							<input type="file" name="img" id="addStationImg" />
					</div>
					
					<label for="linkPhone" class="col-sm-2 control-label">
						<span style="color: red;">*</span>联系电话：
					</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="linkPhone" placeholder="联系电话" name="linkPhone" value="" maxlength="20"/>
					</div>
				</div>
				
				<div class="form-group">
					<label for="address" class="col-sm-2 control-label">
						<span style="color: red;">*</span>详细地址：
					</label>
					<div class="col-sm-4">
						<textarea type="text" class="form-control" id="address" rows="3" placeholder="详细地址..." name="address" value="" maxlength="150"></textarea>
					</div>
					<label for="remark" class="col-sm-2 control-label">备注：</label>
					<div class="col-sm-4">
						<textarea placeholder="备注信息..." id="remark" name="remark" class="form-control" rows="3" maxlength="150"></textarea>
					</div>
				</div>
			</form>
            </div>
          </div>
          <!-- 添加点信息end -->
           </div>
      </div>
   </div>
   
   	<!-- 模态框（Modal） -->
	<div class="modal fade" id="pointMapModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 80%;height: 70%;">
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
							<input type="text" class="form-control" id="searchMapName"
								placeholder="请输入搜索地址" />
						</div>
						<div class="col-sm-2">
							<button class="btn btn-info" id="searchMapBtn">搜索</button>
						</div>
					</div>
				</div>

				<div class="modal-body" id="baiduMap" style="height: 70%;">
				
				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭
					</button>
					<button id="editLngLatBtn" type="button" class="btn btn-info">
						提交更改</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal -->
	</div>
</section>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=A4749739227af1618f7b0d1b588c0e85"></script>
<script type="text/javascript">
var gobackStationId = "${gobackStationId}";
</script>
<script src="res/js/user/device/userDevice.js"></script>
