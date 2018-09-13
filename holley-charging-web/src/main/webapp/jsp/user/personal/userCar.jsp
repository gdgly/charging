<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
    <!-- Content Header (Page header) -->
    <section class="content-header">
       <h1>我的爱车</h1> 
      	<ol class="breadcrumb">
      	 <li>
      	  	<a href="javascript:;" onclick="frameReload();">
      	 		<i class="fa fa-home"></i> 首页
      	 	</a>
      	 </li>
	        <li>
	        <i class="fa fa-user"></i> 个人中心
	        </li>
	        <li class="active">我的爱车</li>
      	</ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
       <div class="col-xs-12">
        <div class="box box-info">
            <div class="box-header with-border">
              <h3 class="box-title">爱车信息</h3>
               <button id="saveUserInfoBtn" type="button" class="btn btn-info pull-right" disabled="disabled">保存</button>
            </div>
            <!-- /.box-header -->
            <!-- form start -->
            <form class="form-horizontal" id="userCarForm">
              <div class="box-body">
                <div class="form-group">
                  <label for="plateNo" class="col-sm-offset-2 col-sm-2 control-label">车牌号 </label>
                  <div class="col-sm-4">
                    <input type="text" class="form-control" id="plateNo" name="plateNo" placeholder="车牌号" value="${busUserInfo.plateNo}" maxlength="20">
                  </div> 
                </div>
                 <div class="form-group">
                 <label for="vin" class="col-sm-offset-2 col-sm-2 control-label">车架号</label>
                  <div class="col-sm-4">
					<input type="text" class="form-control" id="vin" name="vin" placeholder="车架号" value="${busUserInfo.vin}" maxlength="30">                
                  </div> 
                 </div>
                <div class="form-group">
            			<label for="brand" class="col-sm-offset-2 col-sm-2 control-label">车品牌</label>
					<div class="col-sm-2">
						<select id="brand" name="brand" class="form-control">
							<option value="0">请选择车品牌</option>
							<s:iterator value="#request.carBrandList" status="statu"
								id="item">
								<s:if test="id==#request.busUserInfo.brand">
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
						<select id="model" name="model" class="form-control">
							<s:if test="#request.modelList != null">
								<s:iterator value="#request.modelList" status="statu" id="item">
									<s:if test="id==#request.busUserInfo.model">
										<option selected="selected" value="<s:property value='id'/>"><s:property
												value="name" />
										</option>
									</s:if>
									<s:else>
										<option value="<s:property value='id'/>"><s:property value="name" />
										</option>
									</s:else>
								</s:iterator>
							</s:if>
							<s:else>
								<option value="0">请先选择车品牌</option>
							</s:else>
						</select>
					</div>
                </div>
          		<div class="form-group">
                 <label for="showCarImg" class="col-sm-offset-2 col-sm-2 control-label">驾驶证图片</label>
                  <div class="col-sm-4">
                  <s:if test="#request.busUserInfo.pic != null">
                  <img id="showCarImg" alt="Image preview" class="img-thumbnail" style="height: 15%; width: 46%;" src="${imgUrl}${busUserInfo.pic}" /> 
                  </s:if>
                  <s:else>
                  <img id="showCarImg" alt="Image preview" class="img-thumbnail" style="height: 15%; width: 46%;" src="${imgUrl}data/stationImg/stationDefault.jpg" /> 
                  </s:else>
					   <input type="file" name="pic" id="addCarImg" />            
                  </div> 
                 </div>
              </div>
            </form>
          </div>
          </div>
      </div>
    </section>
<script src="res/js/user/personal/userCar.js"></script>
