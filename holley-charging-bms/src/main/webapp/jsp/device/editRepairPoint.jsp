<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>服务点新增/修改</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
  	<form id="repairPointForm" class="form-horizontal" role="form">
		<div class="form-group">
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>服务点名称</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="name" name="name" maxlength="25" value="<s:property value='#request.repairPoint.name'/>"
	      	       placeholder="请输入服务点名称"/>
	      </div>
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>服务点地址</label>
	        <div class="col-sm-4">
	      		<input type="text" class="form-control" id="address" name="address" maxlength="200" value="<s:property value='#request.repairPoint.address'/>"
	      		placeholder="请输入服务点地址"/>
	        </div>
    	</div>
    	<div class="form-group">
			<label class="col-sm-2 control-label"><span style="color: red;">*</span>是否显示</label>
	        <div class="col-sm-4">
		      	<select id="isShow" name="isShow" class="form-control">
			     	<s:iterator value="#request.showStatusList" var="item" status="st">
			     		<s:if test="#request.repairPoint.isShow == #item.value">
			     			<option value="${item.value}" selected="selected">${item.text}</option>
			     		</s:if>
			     		<s:else>
				     		<option value="${item.value}">${item.text}</option>
			     		</s:else>
			     	</s:iterator>
			     </select>
	        </div>
	        <label class="col-sm-2 control-label"><span style="color: red;">*</span>经纬度</label>
	        <div class="col-sm-4">
	      		<div class="form-group" style="margin-bottom: 0;">
						<div class="col-sm-5">
							<input type="text" class="form-control" id="lng" name="lng" value=<s:property value='#request.repairPoint.lng'/> />
						</div>
						<div class="col-sm-5">
							<input type="text" class="form-control" id="lat" name="lat" value=<s:property value='#request.repairPoint.lat'/> />
						</div>
						<div class="col-sm-2">
							<label id="markPointBtn" style="padding-top:5px;cursor:pointer;font-size: 20px;">
								<span class="glyphicon glyphicon-map-marker"></span>
							</label>
						</div>
				</div>
	        </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>座机号码</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="tel" name="tel" maxlength="20" value="<s:property value='#request.repairPoint.tel'/>"
	      	placeholder="请输入座机号码"/>
	      </div>
	      <label class="col-sm-2 control-label">手机号码</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="phone" name="phone" maxlength="phone" value="<s:property value='#request.repairPoint.phone'/>"
	      	placeholder="请输入手机号码"/>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">营业时间</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="workTime" name="workTime" maxlength="phone" value="<s:property value='#request.repairPoint.workTime'/>"/>
	      </div>
    	</div>
    	<div class="form-group">
	      <div class="col-sm-offset-2 col-sm-10">
	         <button id="saveBtn" type="button" class="btn btn-primary">保存</button>
	         <button id="cancelBtn" type="button" class="btn btn-primary" onclick="gobackAndReload()">取消</button>
	      </div>
   		</div>
    </form>
  </div>
  <!-- 模态框（Modal） -->
	<div class="modal fade" id="pointMapModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 70%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<div class="form-group">
						<label for="lngMap" class="col-sm-1 control-label">经度：</label>
						<div class="col-sm-2">
							<input type="text" id="lngMap" class="form-control" disabled="disabled"/>
						</div>
						<label for="latMap" class="col-sm-1 control-label">纬度：</label>
						<div class="col-sm-2">
							<input type="text" id="latMap" class="form-control" disabled="disabled"/>
						</div>
						<div class="col-sm-2">
							<input type="text" class="form-control" id="searchName" placeholder="请输入搜索地址" />
						</div>
						<div class="col-sm-2">
							<button class="btn btn-success" id="searchBtn">搜索</button>
						</div>
					</div>
				</div>
				<div class="modal-body" id="baiduMap" style="height: 70%; overflow: hidden; margin: 0; font-family:'微软雅黑';">
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="editLngLatBtn" type="button" class="btn btn-primary">保存</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal -->
	</div>
  <script type="text/javascript">
  	var requestType = <s:property value='#request.requestType'/>
  	var repairpointid = "<s:property value='#request.repairPoint.id'/>";
  </script>
  <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=A4749739227af1618f7b0d1b588c0e85"></script>
  <script src="res/js/device/editRepairPoint.js" type="text/javascript"></script>
</body>
</html>

