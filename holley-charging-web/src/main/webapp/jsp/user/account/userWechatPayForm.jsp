<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<section class="content-header">
      	<h1>充值</h1> 
	    <ol class="breadcrumb">
		     <li>
	      	  	<a href="javascript:;" onclick="frameReload();">
	      	 		<i class="fa fa-home"></i> 首页
	      	 	</a>
	      	 </li>
	        <li><i class="fa fa-dollar"></i> 账户中心</li>
	        <li class="active">充值跳转</li>
	    </ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
    	<div class="col-xs-12">
          <div class="box box-info">
            <div class="box-header" id="header1">
				<h3 class="box-title">您正在使用微信充值请扫描下方二维码进行支付操作...</h3>
				<button id="cancelWechatPayBtn" style="margin-left: 10px;" type="button" class="btn btn-info pull-right btn-sm">取消</button>
            </div>
             <div class="box-header hide" id="header2">
             <h3 class="box-title">充值成功正在跳转页面...</h3>
             </div>
            <div class="box-body text-center" id="body1">
            <img src="userAccount/createQRCode.action?rechargeMoney=${rechargeMoney}&rechargeWay=${rechargeWay}&out_trade_no=${out_trade_no}" style="width: 150px;">
              <p style="color: gray;">充值金额：<span class="feeColor">${rechargeMoney}</span>元</p>
              <input type="hidden" id="out_trade_no" value="${out_trade_no}"/>
              </div>
               <div class="box-body text-center hide" id="body2">
               <p>跳转倒计时：<span id="timeCount" style="color: red;font-size: 20;">5</span></p>
               </div>
              </div>
           </div>
      </div>
	</section>
	 <script src="res/js/user/account/userWechatPayForm.js"></script>
