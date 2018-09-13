<%--
  头部、全文检索、导航栏
--%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
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
		          <a class="navbar-brand" style="color: #fff;text-decoration: none;padding-top: 9px;" href="${webroot}/run/deviceMap.action" >
				 	<img alt="" src="${imgUrl}res/img/home/loginbj.png">
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
					<s:if test="#request.webUser.usertype.value != 9">
					<a id="modifyPwdBtn" href="javascript:;">
							<span style="color:#f49447;">修改</span>
						</a>
						|
					</s:if>
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




<!-- 模态框（Modal） -->
<div class="modal fade" id="selectStationModal" tabindex="-1" role="dialog" aria-labelledby="selectStationModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title" id="selectStationModalLabel">
					充电站帮助
				</h4>
			</div>
			<div class="modal-body">
				<table class="table  table-bordered" id="selectStationTable">
	<caption>
	<form class="form-inline" role="form">
	 <div class="form-group">
		<input id="selectStationModalKeyword"  type="text" class="form-control" style="width: 240px;" placeholder="请输入充电站名称"/>
		<select id="selectStationModalStationToType" name="selectStationModalStationToType" class="form-control">
			<option value=0>站类型</option>
			<option value=1>汽车站</option>
			<option value=2>自行车站</option>
		</select>
	</div>
	 <div class="form-group pull-right">
			<button class="btn btn-primary" type="button" id="selectStationModalSearchBtn">查询</button>
			</div>
	</form>
	</caption>
	<thead>
		<tr>
			<th>编号</th>
			<th>名称</th>
			<th>站类型</th>
		</tr>
	</thead>
	<tbody>
	</tbody>
</table>
<%@include file="./pagingtoolbar3.jsp" %>
			</div>
			<div class="modal-footer">
				 <button type="button" class="btn btn-default hide" id="cleanSelectBtn">清除
				</button>
				<button type="button" class="btn btn-primary" id="selectStationModalOkBtn">
					确定
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>
<style>
.selectStationColor{
background-color: #86cb86;
}
.selectStationCursor{
cursor: pointer;
}
</style>
<script type="text/javascript">
var userType= '${webUser.usertype.value}';
var selectStationId;
var stationName;
var selectStationIds;
var stationNames;
var queryStationListForModalUrl = "device/station_queryStationListForModal.action";
function checkStation(obj){
	if((typeof(isSelects) != "undefined") && isSelects){
		$(obj).addClass("selectStationColor");	
	}else{
		$(obj).parent().find("tr").each(function(index,data){
			$(data).removeClass("selectStationColor");
		});
		$(obj).addClass("selectStationColor");	
	}
	
}

function reqStationListForModal(){
	html="";
	param={};
	param.pageindex = $.trim($("#currentPage3").val());
	param.searchStationName = $.trim($("#selectStationModalKeyword").val());
	param.stationToType = $("#selectStationModalStationToType").val();
	var tbody = $("#selectStationTable").find("tbody");
		$.ajax({
			type:"POST",
			url:queryStationListForModalUrl,
			data:param,
			dataType:'json',
	        cache: false,
	        success: function(data){
	             if(data.page){
	            	 tbody.empty();
	            	 var dataList = data.page.root; 
	            	 $(dataList).each(function(index,item){
	            		 html += '<tr class="selectStationCursor" onclick="checkStation(this);" stationId="'+item.id+'" stationName="'+item.stationName+'">';
	            		 html += '<td>'+item.id+'</td>';
	            		 html += '<td>'+item.stationName+'</td>';
	            		 html += '<td>'+item.stationToTypeDesc+'站</td>';
	            		 html += '</tr>';
	            	 });
	            	 tbody.html(html);
	            	 var currentPage = data.page.startRow/(data.page.endRow-data.page.startRow)+1;
	            	 var totalPage = Math.ceil(data.page.totalProperty/(data.page.endRow-data.page.startRow));
	            	 setPagingToolbar3Params(data.page.totalProperty, totalPage,currentPage);
	            	$('#selectStationModal').modal('show');
	             }else{
	            	 showMsg(data.message, data.errormsg);
	             }
	         }
	     });
}
$(function(){
	initPagingToolbar3(reqStationListForModal);
	$("#selectStationModalOkBtn").on("click",function(){
		selectStationId="";
		stationName="";
		selectStationIds=[];
		stationNames=[];
		$("#selectStationTable").find('tr').each(function(index,data){
			if($(data).hasClass("selectStationColor")){
				selectStationIds.push($(data).attr("stationId"));
				stationNames.push($(data).attr("stationName"));
				selectStationId = $(data).attr("stationId");
				stationName =  $(data).attr("stationName");
			}
		});
		if(!isEmpty(selectStationId)){
			$('#selectStationModal').modal('hide');
			if(typeof(queryStationList)!="undefined"){ 
				queryStationList(selectStationId);
			}else if(typeof(queryPileList)!="undefined"){
				queryPileList(selectStationId);
			}else if(typeof(querySingleStation)!="undefined"){
				querySingleStation(selectStationId);
			}else if(typeof(queryList)!="undefined"){
				queryList(selectStationIds,stationNames);
			}
			else{
				showInfo("请求数据失败！！");
			}
			
		}else{
			showInfo("请选择一行数据！！");
		}
	
		//$('#selectStationModal').modal('hide')
	});
	
	$("#selectStationModalBtn").on("click",function(){
		setPagingToolbar3Params(0,0,1);
		$("#selectStationModalKeyword").val("");
		$("#selectStationModalStationToType").val(0);
		reqStationListForModal();
	});
	
	$("#selectStationModalSearchBtn").on("click",function(){
		setPagingToolbar3Params(0,0,1);
		reqStationListForModal();
	});
	if((typeof(isSelects) != "undefined") && isSelects){
		$("#cleanSelectBtn").removeClass("hide");
		$("#cleanSelectBtn").on("click",function(){
			$("#selectStationTable").find("tbody").find("tr").removeClass("selectStationColor");
		});
	}
	
})

</script>
