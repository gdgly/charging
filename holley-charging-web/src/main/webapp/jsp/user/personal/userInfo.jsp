<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
	<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
	<section class="content-header">
       <h1 id=""><s:if test="#request.webUser.usertype.value == 3">个人资料</s:if><s:else>集团信息</s:else></h1> 
      <ol class="breadcrumb">
         <li>
      	  	<a href="javascript:;" onclick="frameReload();">
      	 		<i class="fa fa-home"></i> 首页
      	 	</a>
      	 </li>
        <li>
        <i class="fa fa-user"></i>  <s:if test="#request.webUser.usertype.value == 3">个人中心</s:if><s:else>集团中心</s:else>
        </li>
         <li class="active"><s:if test="#request.webUser.usertype.value == 3">个人资料</s:if><s:else>集团信息</s:else></li>
      </ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
      <div class="col-xs-6">
      
      <s:if test="#request.webUser.usertype.value == 3">
      <div class="box box-info" style="height:660px;">
            <div class="box-header with-border">
              <h3 class="box-title">基本信息</h3>
              <button id="saveUserInfoBtn" type="button" class="btn btn-info pull-right" disabled="disabled">保存</button>
            </div> 
            <!-- /.box-header -->
            <!-- form start -->
            <form class="form-horizontal" id="userInfoForm" enctype="multipart/form-data">
              <div class="box-body">
               <div class="form-group">
                  <label for="headImg" class="col-sm-offset-1 col-sm-2 control-label">头像</label>
                  <div class="col-sm-6 text-center">
                  <img id="showHeadImg" title="点击修改头像" class="img-circle" style="height: 100px;width: 100px;cursor: pointer;" src="${imgUrl}${webUser.headImg}"/>
                    <input style="display: none;" type="file" id="addHeadImg" name="headImg"/>
                  </div> 
                </div>
                <s:if test="#request.webUser.roleType == 6">
                 <div class="form-group">
                  <label for="groupName" class="col-sm-offset-1 col-sm-2 control-label">所属集团</label>
                  <div class="col-sm-6">
                    <input type="text" disabled="disabled" class="form-control" id="groupName" name="groupName" value="${groupName}" maxlength="20">
                  </div> 
                </div>
                </s:if>
                <div class="form-group">
                  <label for="username" class="col-sm-offset-1 col-sm-2 control-label">昵称</label>
                  <div class="col-sm-6">
                    <input type="text" class="form-control" id="username" name="username" placeholder="昵称" value="${webUser.userName}" maxlength="20">
                  </div> 
                   <div class="col-sm-1">
                    <button id="editUsernameBtn" type="button" class="btn btn-info pull-right btn-sm" disabled="disabled">修改</button>
                   </div>
                </div>
                       <div class="form-group">
                   		<label for="sex" class="col-sm-offset-1 col-sm-2 control-label">性别</label>
                  		<div class="col-sm-8" id="sex">
                  		<s:if test="#request.busUserInfo.sex == 1">
                  				<label class="checkbox-inline"> <input type="radio" name="sex"  value='1' checked> 
                    			 男
								</label>
                  		</s:if>
                  		<s:else>
                  				<label class="checkbox-inline"> <input type="radio" name="sex"  value='1'> 
                    			 男
								</label>
                  		</s:else>
                    	<s:if test="#request.busUserInfo.sex == 2">
                    			<label class="checkbox-inline"> <input type="radio" name="sex"  value='2' checked> 
								 女
								</label>
                    	</s:if>
						<s:else>
						        <label class="checkbox-inline"> <input type="radio" name="sex"  value='2'> 
								 女
								</label>
						</s:else>
                  		</div>
                </div>
                <div class="form-group">
                  <label for="qq" class="col-sm-offset-1 col-sm-2 control-label">QQ</label>
                  <div class="col-sm-6">
                    <input type="text" class="form-control" id="qq" name="qq" placeholder="QQ" value="${busUserInfo.qq}" maxlength="20">
                  </div> 
                </div>
                 <div class="form-group">
                 <label for="qq" class="col-sm-offset-1 col-sm-2 control-label">生日</label>
                  <div class="col-sm-6">
                	<div class='input-group date' id='datepicker'>
							<input id="birthday" name="birthday" type='text' value="${busUserInfo.birthdayDesc}" class="form-control" /> 
							<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span> </span>
					</div>
                
                  </div> 
                 </div>
                <div class="form-group">
            			<label for="province" class="col-sm-offset-1 col-sm-2 control-label">地区</label>
					<div class="col-sm-3">
						<select id="province" name="province" class="form-control">
							<option value="0">请选择省份</option>
							<s:iterator value="#request.provinceList" status="statu"
								id="item">
								<s:if test="id==#request.busUserInfo.province">
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

					<div class="col-sm-3">
						<select id="city" name="city" class="form-control">
							<s:if test="#request.cityList != null">
								<s:iterator value="#request.cityList" status="statu" id="item">
									<s:if test="id==#request.busUserInfo.city">
										<option selected="selected" value="<s:property value='id'/>"><s:property
												value="name" />
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
								<option value="0">请选择市区</option>
							</s:else>
						</select>
					</div>
                </div>
         
                <div class="form-group">
                  <label for="sign" class="col-sm-offset-1 col-sm-2 control-label">签名</label>
                  <div class="col-sm-6">
                  	<textarea type="text" class="form-control" id="sign" rows="3" placeholder="不能超过100个字符..." name="sign" maxlength="100">${busUserInfo.sign}</textarea>
                  </div> 
                </div>
              </div>
            </form>
          </div>
          </s:if>
          <s:else>
           <div class="box box-info" style="height:460px;">
            <div class="box-header with-border">
              <h3 class="box-title">基本信息</h3>
              <!-- <button id="" type="button" class="btn btn-info pull-right" disabled="disabled">保存</button> -->
            </div> 
              <form class="form-horizontal" id="groupInfoForm" enctype="multipart/form-data">
              <div class="box-body">
               <div class="form-group">
                   <label class="col-sm-offset-1 col-sm-2 control-label">集团名称:</label>
                  <div class="col-sm-8">
                  <input type='text' value="${groupInfo.groupName}" class="form-control" disabled="disabled"/> 
                  </div> 
               </div>
               <div class="form-group">
                   <label class="col-sm-offset-1 col-sm-2 control-label">集团规模:</label>
                  <div class="col-sm-8">
                  <select class="form-control" disabled="disabled">
                  <s:iterator id="item" value="#request.scaleTypeList">
                  <s:if test="#request.groupInfo.scale == #item.value">
                  <option selected="selected"><s:property value="#item.name"/></option>
                  </s:if>
                  </s:iterator>
                  </select>
                  </div> 
               </div>
               <div class="form-group">
                   <label for="username" class="col-sm-offset-1 col-sm-2 control-label">主营业务:</label>
                  <div class="col-sm-8">
                  <textarea rows="3" disabled="disabled" class="form-control">${groupInfo.domain}</textarea>
                  </div> 
               </div>
               <div class="form-group">
                   <label for="username" class="col-sm-offset-1 col-sm-2 control-label">所在省市:</label>
                  <div class="col-sm-4">
                  <select class="form-control" disabled="disabled">
	                 <s:iterator id="item1" value="#request.provinceList">
	                 <s:if test="#item1.id == #request.groupInfo.province">
	                 <option><s:property value="#item1.name"/></option>
	                 </s:if>
	                 </s:iterator>
                  </select>
                  </div> 
                    <div class="col-sm-4">
                  <select class="form-control" disabled="disabled">
                 <s:iterator id="item2" value="#request.cityList">
                 <s:if test="#item2.id == #request.groupInfo.city">
                 <option><s:property value="#item2.name"/></option>
                 </s:if>
                 </s:iterator>
                  </select>
                  </div>
               </div>
               <div class="form-group">
                   <label class="col-sm-offset-1 col-sm-2 control-label">详细地址:</label>
                  <div class="col-sm-8">
                   	<textarea rows="3" disabled="disabled" class="form-control">${groupInfo.address}</textarea>
                  </div> 
               </div>
               <div class="form-group">
                   <label class="col-sm-offset-1 col-sm-2 control-label">联系电话:</label>
                  <div class="col-sm-8">
                   	<input type='text' value="${groupInfo.phone}" class="form-control" disabled="disabled"/> 
                  </div>
               </div>
               </div>
               </form>
            </div>
          </s:else>
          </div>
          
          <!-- 安全设置 -->
           <div class="col-xs-6">
      <div class="box box-info" style="height: 460px;">
      		<div class="box-header with-border">
              <h3 class="box-title">安全设置</h3>
            </div>
           <form class="form-horizontal" role="form">
				<input type="hidden" id="realSafeLevel" value="${safeLevel}" />
				<div class="form-group">
					<label for="safeLevel" class="col-sm-2 control-label">账户安全：</label>
					<div class="col-sm-7" style="padding-top: 9px;">
						<div class="progress" style="width: 97%;">
							<div id="safeLevel" class="progress-bar" role="progressbar"
								aria-valuenow="60" aria-valuemin="0" aria-valuemax="100">
							</div>
						</div>
					</div>
					<div class="col-sm-3 text-center" style="padding-top: 9px;">
						安全级别：<span id="warn">低</span>
					</div>

				</div>
				<%-- <hr class="dashed"/>
   <div class="form-group">
      <label for="account" class="col-sm-2 control-label">登录账号：</label>
      <div class="col-sm-8" >
     	<p id="account" class="text-center">
     	  ${currentUser.username}（
      		<s:if test="#request.currentUser.realStatus == 1">
  			未认证，<a href="${webroot}/bussiness/realName.action">点击实名认证</a>）
			</s:if>
			<s:else>
			已认证）
			</s:else>
      	</p>
      </div>
   </div> --%>

				<hr class="dashed" style="border-top: 4px double #00c0ef;">
				<div class="form-group">
					<label for="password" class="col-sm-2 control-label">登录密码：</label>
					<div class="col-sm-7">
						<p class="help-block fontSize15">
							安全性能高的密码可以使账户更安全，建议您定期更换密码，设置一个包含字母、符号或数字长度超过6位的密码</p>
					</div>
					<div class="col-sm-3">
						<p id="password" class="text-center">
							<s:if test="#request.currentUser.password != null">
								<img
									src="${imgUrl}res/img/bussiness/steup.png"
									class="marginBottom3" />
								<span class="success">已设置</span> | <a href="userAdmin/validChangePwd.action">修改</a>
							</s:if>
						</p>
					</div>
				</div>
				<%--     <hr class="dashed">
    <div class="form-group">
         <label for="payPassword" class="col-sm-3 control-label">交易密码：</label>
            <div class="col-sm-6">
       <p class="help-block fontSize15">
       	安全性能高的密码可以使账户更安全，建议您定期更换密码，设置一个包含字母、符号或数字中至少两项目长度超过6 位的密码。
       </p>
      </div>
      <div class="col-sm-3">
     	<p id="payPassword" class="text-center">
			<s:if test="#request.currentUser.payPassword != null">
				<img src="res/img/bussiness/steup.png" class="marginBottom3"/> <span class="success">已设置</span> | <a href="${webroot}/bussiness/validChangePayPwd.action">修改</a>
			</s:if>
      	</p>
      </div>
   </div> --%>
			<%-- 	<hr class="dashed">
				<div class="form-group">
					<label for="phone" class="col-sm-2 control-label">手机修改：</label>
					<div class="col-sm-7">
						<p class="help-block fontSize15">
							您已绑定了手机<span style="color: orange;">"${currentUser.phone}"</span>。[您的手机为安全手机，可以找回密码；为保障您的帐户安全，如果换绑手机，五天之内只能操作一次]
						</p>
					</div>
					<div class="col-sm-3">
						<p id="phone" class="text-center">
							<s:if test="#request.currentUser.phoneStatus == 2">
								<img
									src="${imgUrl}res/img/bussiness/steup.png"
									class="marginBottom3" />
								<span class="success">已设置</span> | <a href="userAdmin/validChangePhone.action">修改</a>
							</s:if>
						</p>
					</div>
				</div> --%>
				<hr class="dashed">
				<div class="form-group">
					<label for="email" class="col-sm-2 control-label">邮箱修改：</label>
					<div class="col-sm-7">
						<p class="help-block fontSize15">
							<s:if test="#request.currentUser.emailStatus == 2">
         						您已绑定了邮箱<span style="color: orange;">"${currentUser.email}"</span>[您的邮箱为安全邮箱，可以接收信息]
       						</s:if>
							<s:else>
             					 邮箱绑定后可用来接收各类系统、营销、服务通知。
       						</s:else>
						</p>
					</div>
					<div class="col-sm-3">
						<p id="email" class="text-center">
							<s:if test="#request.currentUser.emailStatus == 2">
								<img
									src="${imgUrl}res/img/bussiness/steup.png"
									class="marginBottom3">
								<span class="success">已设置</span> | <a href="userAdmin/validChangeEmail.action">修改</a>
							</s:if>
							<s:else>
								<img
									src="${imgUrl}res/img/bussiness/no_setup.png"
									class="marginBottom3" />
								<span class="warn">未设置</span>  | <a href="userAdmin/validChangeEmail.action">设置</a>
							</s:else>
						</p>
					</div>
				</div>
			</form>
      </div>
       <s:if test="#request.webUser.roleType == 3">
      <div class="box box-info" style="height: 180px;">
      		<div class="box-header with-border">
              <h3 class="box-title">实名认证</h3>
            </div>
            <div class="col-sm-3" style="margin-top:15px;">
            <!-- <span class="fa fa-credit-card" style="font-size: 55px;"></span> -->
				<img style="width:70px;" src="${imgUrl}res/img/mark/identify.png" />
			</div>
			<div class="col-sm-5 text-center" style="margin-top: 20px;">
				<s:if test="#request.currentUser.realStatus == 2">
				<p class="help-block fontSize15">${busUserInfo.realNameDesc}</p>
					<p class="help-block fontSize15">${busUserInfo.cardNoDesc}</p>
				</s:if>
				<s:elseif test="#request.userRealInfo !=null && #request.userRealInfo.status == 1">
					<p class="help-block fontSize15">
 							管理员审核中。。。
					</p>
				</s:elseif>
				<s:else>
					<p class="help-block fontSize15">未认证</p>
				</s:else>
			</div>
					<div class="col-sm-offset-1 col-sm-3 text-center" style="margin-top: 35px;">
				<s:if test="#request.currentUser.realStatus != 2">
					<s:if test="#request.userRealInfo !=null && #request.userRealInfo.status != 1">
						<button id="goRealName" type="button" class="btn btn-info pull-right">去认证</button>
					</s:if>
					<s:elseif test="#request.userRealInfo ==null">
					  <button id="goRealName" type="button" class="btn btn-info pull-right">去认证</button>
					</s:elseif>
				</s:if>
				<s:else>
					<img src="${imgUrl}res/img/bussiness/steup.png" class="marginBottom3" />
					<span class="success">已认证</span>
				</s:else>
			</div>
            </div>
            </s:if>
      </div>
      </div>
    </section>
<script src="res/js/user/personal/userInfo.js"></script>
