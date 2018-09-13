<%--
  头部、全文检索、导航栏
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
  <header>
	<div class="navbar navbar-default navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				 <button class="navbar-toggle collapsed" type="button" data-toggle="collapse" data-target=".navbar-collapse">
		            <span class="sr-only">Toggle navigation</span>
		            <span class="icon-bar"></span>
		            <span class="icon-bar"></span>
		            <span class="icon-bar"></span>
		          </button>
		          <a class="navbar-brand" style="color: #fff;text-decoration: none;padding-top: 9px;" href="#">
				 	<img alt="" src="${imgUrl}res/img/home/logo.png">
				 </a>
			</div>
			<div class="collapse navbar-collapse">
				<ul id="menu" class="nav navbar-nav">
				<!-- 
					<li class="<s:if test="#request.currentModule=='devicemanage'">active nav-current</s:if>">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">设备管理<span class="caret"></span></a>
						<ul class="dropdown-menu">
				            <li><a href="device/stationVerifyList.action">充电点审核</a></li>
				            <li><a href="device/pileVerifyList.action">充电桩审核</a></li>
				        </ul>
					</li>
					<li class="<s:if test="#request.currentModule=='chargenet'">active nav-current</s:if>">
						<a href="chargenet/chargenet.action">运行管理</a>
					</li>
					<li class="<s:if test="#request.currentModule=='news'">active nav-current</s:if>">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">交易管理<span class="caret"></span></a>
						<ul class="dropdown-menu">
				            <li><a href="deal/cashVerifyList.action">提现审核记录</a></li>
				            <li><a href="deal/cashTransferList.action">提现转账记录</a></li>
				        </ul>
					</li>
					<li class="dropdown <s:if test="#request.currentModule=='busmanage'">active nav-current</s:if>">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">运营商管理<span class="caret"></span></a>
						<ul class="dropdown-menu">
				            <li><a href="business/busRealVerifyList.action">实名审核记录</a></li>
				            <li><a href="business/busRealTransferList.action">实名转账记录</a></li>
				        </ul>
					</li>
					<li class="<s:if test="#request.currentpage=='news'">active nav-current</s:if>">
						<a href="news/news.action">账户管理</a>
					</li>
					 -->
				</ul>
				<form class="navbar-form navbar-right form-horizontal" style="padding-top: 15px;padding-bottom: 15px;">
					<div style="float: left;">
						<span class="glyphicon glyphicon-user" style="font-size:18px;"></span>
						您好,${webUser.userName}  |  
						<a id="modifyPwdBtn" href="javascript:;">
							<span style="color:#f49447;">修改</span>
						</a>
						|
						<a href="<%=request.getContextPath()%>/user/logout.action">
							<span style="color:#f49447;">退出</span>
						</a>
					</div>
				</form>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var modules = ${userModule};
		var buttons = ${userModuleButton};
	</script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/res/js/common/header.js"></script>
</header>
<div class="container" style="margin-top: 50px;">
	<ol id="header_breadcrumb" class="breadcrumb" style="margin-top: 10px;margin-bottom: 10px;">
  		<!-- <li class="active">设备管理</li>
  		<li><a href="device/deviceVerifyList.action">充电点审核记录</a></li>
		<li class="active">充电点审核</li> -->
 	</ol>
</div>
<!-- 模态框（Modal） -->
<div class="modal fade" id="modifyPwdModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h4 class="modal-title" id="myModalLabel">修改密码</h4>
         </div>
         <div class="modal-body">
		 	<form class="form-horizontal" role="form" id="pwdform"  enctype="multipart/form-data">
				<div class="form-group">
			      <label class="col-sm-3 control-label">旧密码</label>
			      <div class="col-sm-9">
			      	<input type="password" class="form-control" id="oldpwd" onblur="ispassword(this);" placeholder="请输入旧密码" maxlength="16">
			      </div>
		    	</div>
		    	<div class="form-group">
			      <label class="col-sm-3 control-label">新密码</label>
			      <div class="col-sm-9">
			      	<input type="password" class="form-control" id="newpwd" onblur="ispassword(this);" placeholder="请输入新密码" maxlength="16">
			      </div>
		    	</div>
		    	<div class="form-group">
			      <label class="col-sm-3 control-label">重新输入新密码</label>
			      <div class="col-sm-9">
			      	<input type="password" class="form-control" id="repeatpwd" onblur="ispassword(this);" placeholder="请再一次输入新密码" maxlength="16">
			      </div>
		    	</div>
			</form>
         </div>
         <div class="modal-footer">
            <button id="savePwdBtn" type="button" class="btn btn-primary">确定</button>
            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
         </div>
      </div><!-- /.modal-content -->
</div><!-- /.modal -->
</div>

