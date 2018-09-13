<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="common.jsp"%>
	<section class="content-header">
	  <s:if test="#request.webUser.usertype.value == 3">
	    <h1 id="">个人主页</h1> 
	  </s:if>
	  <s:else>
	  <h1 id="">主页</h1> 
	  </s:else>
    </section>
    <!-- Main content -->
<section class="content">
   <div class="row">
        <!-- /.col -->
        <s:if test="#request.webUser.roleType == 3">
        	<div class="col-lg-4">
        </s:if>
   		<s:else>
   		<div class="col-lg-6">
   		</s:else>
		<div class="box box-info">
	        <div class="box-header with-border">
	        <s:if test="#request.webUser.roleType == 3">
	          <h3 class="box-title">我的钱包</h3>
	        </s:if>
	        <s:else>
	        <h3 class="box-title">账户金额</h3>
	        </s:else>
	        </div>
	        <div class="box-body">
	         	<p>余额（元）：<span class="feeColor">${usableMoney}</span></p>
	         	<p>
	         	<span>支出（元）：<span class="outFeeColor">-${totalOut}</span></span>
	         	<s:if test="#request.webUser.roleType == 3">
	         	<span style="padding-left:11%;">收入（元）：<span style="color:#A2CD5A;">+${totalIn}</span></span>
	         	</s:if>
	         	</p>
	        </div>
	        <!-- /.box-body -->
	        <s:if test="#request.webUser.roleType !=6 ">
		        <div class="box-footer">
		          <button id="rechargeBtn" onclick="href('userAccount/initUserRecharge.action');" type="button" class="btn btn-info btn-sm">充值</button>
		        </div>
        		<!-- /.box-footer-->
	        </s:if>
	  
      	</div>
     </div>
        <s:if test="#request.webUser.roleType == 3">
        	<div class="col-lg-4">
        </s:if>
   		<s:else>
   		<div class="col-lg-6">
   		</s:else>
	 		<div class="box box-info">
		        <div class="box-header with-border">
		          <h3 class="box-title">消费金额</h3>
		        </div>
		        <div class="box-body">
		         	<p>本月消费（元）：<span class="outFeeColor" style="font-size:18px;">-${totalOutForMonth}</span></p>
		         	<p><span>充电（元）：<span  class="outFeeColor">-${totalChaOutForMonth}</span></span><span style="padding-left:11%;">预约（元）：<span  class="outFeeColor">-${appOutForMonth}</span></span></p>
		        </div>
		        <!-- /.box-body -->
		         <s:if test="#request.webUser.roleType !=6 ">
		          		<div class="box-footer">
		          			<button type="button" onclick="href('userAccount/initUserBill.action');" class="btn btn-info btn-sm">查看详细</button>
		        		</div>
	       			 <!-- /.box-footer-->
		         </s:if>
		       
	      	</div>
        </div>
         <s:if test="#request.webUser.roleType == 3">
              <div class="col-lg-4">
	     		<div class="box box-info" style="height: 177">
		        <div class="box-header with-border">
		          <h3 class="box-title">收益金额</h3>
		        </div>
		        <s:if test="#request.deviceCount > 0">
		        	        <div class="box-body">
		        	<p>本月收入（元）：<span  class="inFeeColor" style="font-size:18px;">+${totalInForMonth}</span></p>
		         	<p>
		         	<span>充电（元）：<span  class="inFeeColor">+${totalChaInForMonth}</span></span>
		         	<span style="padding-left:11%;">预约（元）：<span class="inFeeColor">+${totalAppInForMonth}</span></span>
		         	</p>
		        </div>
			    <div class="box-footer">
			          <button type="button" onclick="href('userTransaction/initUserDeviceBill.action');" class="btn btn-info btn-sm">查看详细</button>
			    </div>
		        </s:if>
				<s:else>
					<div class="box-body">
				    	<button type="button" class="btn btn-default" style="margin-left: 40%;margin-top: 5%;" onclick="href('userDevice/initUserDevice.action');">
							<span class="glyphicon glyphicon-plus"></span>添加设备
						</button>
					</div>
		    	</s:else>

	      	</div>
        </div>
         </s:if>

<div class="col-xs-12">
          <div class="box box-info">
            <div class="box-header">
              <h3 class="box-title">消费明细</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body height400">
              <table class="table table-bordered table-hover">
         <!--        <thead>
                <tr>
                  <th>充电点名称</th>
                  <th>地址</th>
                  <th>评分</th>
                  <th>快充桩（个）</th>
                  <th>慢充桩（个）</th>
                  <th>操作</th>
                </tr>
                </thead> -->
                <tbody id="userConsumeBody">
                <!-- <tr>
                <td>华立科技</td>
                  <td>留下五常大道222号</td>
                    <td>5</td>
                      <td>3</td>
                        <td>3</td>
                          <td>详细</td>
                </tr> -->
                </tbody>
              </table>
              </div>
                 <%@include file="../common/global/pagingtoolbar.jsp"%>
              </div>
           </div>
     </div>
    </section>
<script src="res/js/user/userHome.js"></script>
