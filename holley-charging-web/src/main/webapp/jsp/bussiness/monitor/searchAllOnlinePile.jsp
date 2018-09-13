<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	language="java" pageEncoding="UTF-8"%>
<%@include file="../../common/global/meta.jsp"%>
<%@include file="../../common/global/js.jsp"%>
<%@include file="../head.jsp"%>
<html lang="zh-CN">
<head>
<style type="text/css">
#onLinePileInfoDiv p{
padding-top: 8px;
}
</style>
</head>
<body>
	<div class="bg" style="margin-top: 70px;">
		<!--main-content-->
		<div class="container main-body tablePageSize" style="height: 92%;" id="onLinePileListDiv">
			<div class="row">
				<div class="col-sm-3">
					<h3>设备监控</h3>
				</div>
				<div class="col-sm-offset-7 col-sm-2 text-right"
					style="margin-top: 13px;">
					<s:if test="#request.allOnlinePileList != null && #request.allOnlinePileList.size > 0">
						<button id="flushStatusBtn" type="button" class="btn btn-warning btn-sm">
							<span class="glyphicon glyphicon-refresh"></span> 刷新
						</button>
					</s:if>
					<s:else>
						<button id="flushStatusBtn" disabled="disabled" type="button" class="btn btn-warning btn-sm">
							<span class="glyphicon glyphicon-refresh"></span> 刷新
						</button>
					</s:else>
						<button id="comeBackMonitorMapBtn" type="button" class="btn btn-warning btn-sm">
							<span class="glyphicon glyphicon-menu-left"></span> 返回
						</button>
				</div>
			</div>


			<!-- 监控列表 -->
			<s:if test="#request.allOnlinePileList != null && #request.allOnlinePileList.size > 0">
			<div id="onlinePileDiv">
				<div class="tableDivSize">
					<table class="table table-hover" id="onlineTable">
						<tbody>
							<s:iterator value="#request.allOnlinePileList" id="item"
								status="statu">
								<tr style="cursor:pointer;" onclick='showInfo(this);' >
									<td id="pilename"><s:property value="pilename" /></td>
									
									<td>通讯协议：
									<span id="comtype">
										<s:iterator value="#request.comTypeList" id="item2" status="statu2" >
											<s:if test="#item2.value == #item.comtype">
											<s:property value="#item2.name"/>
											</s:if>
										</s:iterator>
									</span>
									</td>
									<s:if test="outv != null && outv > 0">
									<td>电压：<span id="outv" style="color:red;"><s:property value="outv" /></span> V</td>
									</s:if>
									<s:else>
									<td>电压：<span id="outv" style="color:red;">0.0</span> V</td>
									</s:else>
									
									<s:if test="outi != null && outi > 0">
									<td>电流：<span id="outi" style="color:red;"><s:property value="outi" /></span> A</td>
									</s:if>
									<s:else>
									<td>电流：<span id="outi" style="color:red;">0.0</span> A</td>
									</s:else>
									<td>
										<s:iterator value="#request.pileTypeList" id="item2" status="statu2" >
											<s:if test="#item2.value == #item.piletype">
											<s:if test="#item.piletype == 1">
											<span id="piletype"><s:property value="#item2.name"/></span>
											</s:if>
											<s:elseif test="#item.piletype == 2">
											<span id="piletype"><s:property value="#item2.name"/></span>
											</s:elseif>
											<s:elseif test="#item.piletype == 3">
											<span id="piletype"><s:property value="#item2.name"/></span>
											</s:elseif>
											</s:if>
										</s:iterator>
										</td>
										<td>
											<s:iterator value="#request.chaWayList" id="item2" status="statu2" >
												<s:if test="#item2.value == #item.chaway">
													<span id="chaway"><s:property value="#item2.name"/></span>
												</s:if>
											</s:iterator>
										</td>
							<td>
								<s:if test="status == 1">
											<span id="status" status=<s:property value="status" /> style="color: #1ED538;"><s:property value="statusdisc" /></span>
										</s:if> 
									<s:elseif test="status == 2">
											<span id="status" status=<s:property value="status" /> style="color: #3EA2CC;"><s:property value="statusdisc" /></span>
									</s:elseif> 
									<s:elseif test="status == 3">
											<span id="status" status=<s:property value="status" /> style="color: #FF8D18;"><s:property value="statusdisc" /></span>
									</s:elseif> 
									<s:elseif test="status == 4">
											<span id="status" status=<s:property value="status" /> style="color: #6E6E6E;"><s:property value="statusdisc" /></span>
									</s:elseif> 
									<s:else>
											<span id="status" status=<s:property value="status" /> style="color: #6E6E6E;"><s:property value="statusdisc" /></span>
									</s:else>
							</td>
							<td>
									<lable stationId=<s:property value='stationid'/> onmouseenter="mouseIn(this);" onmouseleave="mouseOut(this);" onclick="showMap(this);" class="glyphicon glyphicon-map-marker" style="cursor:pointer;font-size: 20px;color:#858585;"></lable>
							</td>

									<td class="hide"><span id="pilecode"><s:property value="pilecode" /></span></td>
									<td class="hide" id="pilemodel"><s:property value="pilemodel" /></td>
									<td class="hide" id="comaddr"><s:property value="comaddr" /></td>
									<td class="hide" id="address"><s:property value="address" /></td>
									<td class="hide" id="username"><s:property value="username" /></td>
									<td class="hide" id="phone"><s:property value="phone" /></td>
									<td class="hide" id="updatetime"><s:date name="updatetime" format="yyyy-MM-dd hh:mm:ss" /></td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
				</div>
				
				
				<%-- <%@include file="../../common/global/pagingtoolbar.jsp" %>
 --%>
			</div>
			</s:if>
			<s:else>
			<div>
			<p class="text-center help-block" style="margin-top: 20%;">
			无信息
			</p>
			</div>
			</s:else>
		</div>
	<div class="container main-body tablePageSize hide" id="onLinePileInfoDiv" style="height: 92%;">
		<div class="row">
			<div class="col-sm-10">
			<h3 id="modelPileName"></h3>
			</div>
			<div class="col-sm-2 text-right" style="margin-top: 13px;">
			<button id="comeBackOnLinePileListBtn" type="button" class="btn btn-warning btn-sm">
						<span class="glyphicon glyphicon-menu-left"></span> 返回
			</button>
			</div>
		</div>
		<hr style="margin-top: 0px;"/>
<form class="form-horizontal" role="form">
	<div class="form-group">
		<label for="modelPileCode" class="col-sm-2 control-label">电桩编号：</label>
		<div class="col-sm-4">
			<p type="text" id="modelPileCode" ></p>
		</div>
		<label for="modelPileModel" class="col-sm-2 control-label">电桩型号：</label>
		<div class="col-sm-4">
			<p type="text" id="modelPileModel" ></p>
		</div>
	</div>
	<div class="form-group">
		<label for="modelChaWay" class="col-sm-2 control-label">充电方式：</label>
		<div class="col-sm-4">
			<p type="text" id="modelChaWay" ></p>
		</div>
		<label for="modelPileType" class="col-sm-2 control-label">电桩类型：</label>
		<div class="col-sm-4">
			<p type="text" id="modelPileType" ></p>
		</div>
	</div>
	<div class="form-group">
		<label for="modelComType" class="col-sm-2 control-label">通讯协议：</label>
		<div class="col-sm-4">
			<p type="text" id="modelComType" ></p>
		</div>
		<label for="modelComAddr" class="col-sm-2 control-label">通讯地址：</label>
		<div class="col-sm-4">
			<p type="text" id="modelComAddr" ></p>
		</div>
	</div>
	<div class="form-group">
		<label for="modelOutV" class="col-sm-2 control-label">输出电压：</label>
		<div class="col-sm-4">
			<p><span style="color:red;" type="text" id="modelOutV"></span> V</p>
		</div>
		<label for="modelOutA" class="col-sm-2 control-label">输出电流：</label>
		<div class="col-sm-4">
			<p><span style="color:red;" type="text" id="modelOutA"></span> A</p>
		</div>
	</div>
	<div class="form-group">
	<label for="modelStatus" class="col-sm-2 control-label">电桩状态：</label>
		<div class="col-sm-4">
			<p type="text" id="modelStatus" ></p>
		</div>
		<label for="modelUpdateTime" class="col-sm-2 control-label">更新时间：</label>
		<div class="col-sm-4">
			<p type="text" id="modelUpdateTime" ></p>
		</div>
	</div>
	<div class="form-group">
		<label for="modelAddress" class="col-sm-2 control-label">电桩地址：</label>
		<div class="col-sm-4">
			<p type="text" id="modelAddress" ></p>
		</div>
	</div>
</form>
		</div>
	</div>
</body>
<script type="text/javascript">
setBowHeight3();
function showMap(obj){
	stationId = $(obj).attr("stationId");
	if(stationId && stationId > 0){
		href("monitor/monitorMap.action?stationId="+stationId);
	}
}
function mouseIn(obj){
	$(obj).parent().parent().attr("onclick","");
	$(obj).css('color','#0a94f2');
}
function mouseOut(obj){
	$(obj).parent().parent().attr("onclick","showInfo(this)");
	$(obj).css('color','#858585');
}
function showInfo(obj){
	pileName = $(obj).find("[id=pilename]").text();
	pileCode = $(obj).find("[id=pilecode]").text();
	pileType = $(obj).find("[id=piletype]").text();
	statusDesc = $(obj).find("[id=status]").text();
	status =  $(obj).find("[id=status]").attr("status");
	comType = $(obj).find("[id=comtype]").text();
	comAddr = $(obj).find("[id=comaddr]").text();
	chaWay = $(obj).find("[id=chaway]").text();
	pileModel = $(obj).find("[id=pilemodel]").text();
	outV = $(obj).find("[id=outv]").text();
	outA = $(obj).find("[id=outi]").text();
	updateTime = $(obj).find("[id=updatetime]").text();
	address = $(obj).find("[id=address]").text();
	$("#model"+"PileName").text(pileName);
	$("#model"+"PileCode").text(pileCode);
	$("#model"+"PileType").text(pileType);
	if(status == 1){
		$("#model"+"Status").css("color","#1ED538");
	}else if(status == 2){
		$("#model"+"Status").css("color","#3EA2CC");
	}else if(status == 3){
		$("#model"+"Status").css("color","#FF8D18");
	}else if(status == 4){
		$("#model"+"Status").css("color","#6E6E6E");
	}else{
		$("#model"+"Status").css("color","#6E6E6E");
	}
	$("#model"+"Status").text(statusDesc);
	$("#model"+"ComType").text(comType);
	$("#model"+"ComAddr").text(comAddr);
	$("#model"+"ChaWay").text(chaWay);
	$("#model"+"PileModel").text(pileModel);
	$("#model"+"OutV").text(outV);
	$("#model"+"OutA").text(outA);
	$("#model"+"UpdateTime").text(updateTime);
	$("#model"+"Address").text(address);
 $("#onLinePileListDiv").addClass("hide");
$("#onLinePileInfoDiv").removeClass("hide"); 
}

$(function(){
	$("#comeBackOnLinePileListBtn").on("click",function(){
		$("#onLinePileInfoDiv").addClass("hide");
		$("#onLinePileListDiv").removeClass("hide");
	});
	$("#comeBackMonitorMapBtn").on("click",function(){
		href("monitor/monitorMap.action");
	});
	$("#flushStatusBtn").on("click",function(){
		reload();
	});
});
</script>
</html>

