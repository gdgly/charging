<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
<%@include file="../../common/global/bootStrapDateUtil.jsp"%>
	<section class="content-header">
      	<h1>设备监控</h1> 
	    <ol class="breadcrumb">
		     <li>
	      	  	<a href="javascript:;" onclick="frameReload();">
	      	 		<i class="fa fa-home"></i> 首页
	      	 	</a>
	      	 </li>
	        <li><i class="fa fa-laptop"></i> 我的设备</li>
	        <li class="active">设备监控</li>
	    </ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
    	<div class="col-xs-12">
          <div class="box box-info" id="onLinePileListDiv">
            <div class="box-header">
            <button id="comeBackMonitorMapBtn" type="button" style="margin-left: 10px;" class="btn btn-info btn-sm pull-right">
							<span class="glyphicon glyphicon-menu-left"></span> 返回
						</button>
           			<s:if test="#request.allOnlinePileList != null && #request.allOnlinePileList.size > 0">
						<button id="flushStatusBtn" type="button" class="btn btn-info btn-sm pull-right">
							<span class="glyphicon glyphicon-refresh"></span> 刷新
						</button>
					</s:if>
					<s:else>
						<button id="flushStatusBtn" disabled="disabled" type="button" class="btn btn-info btn-sm pull-right">
							<span class="glyphicon glyphicon-refresh"></span> 刷新
						</button>
					</s:else>
						
            </div>
            <!-- /.box-header -->
            <div class="box-body height400">
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
              </div>
              
              <!-- 监控详细 -->
              <div class="box box-info hide" id="onLinePileInfoDiv">
            <div class="box-header  with-border">
            <h3 class="box-title" id="modelPileName"></h3>
            	<button id="comeBackOnLinePileListBtn" type="button" class="btn btn-info pull-right btn-sm">
						<span class="glyphicon glyphicon-menu-left"></span> 返回
				</button>
            </div>
             <div class="box-body height400">
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
           </div>
      </div>
	</section>
<script src="res/js/user/device/userOnlineDevice.js"></script>

