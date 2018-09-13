<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8"%>
<%@include file="../common.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<section class="content-header">
      	<h1>充值</h1> 
	<!--     <ol class="breadcrumb">
		     <li>
	      	  	<a href="javascript:;" id="testBtn">
	      	 		<i class="fa fa-home"></i> 首页
	      	 	</a>
	      	 </li>
	        <li><i class="fa fa-dollar"></i> 账户中心</li>
	        <li class="active">充值跳转</li>
	    </ol> -->
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
    	<div class="col-xs-12">
          <div class="box box-info">
            <div class="box-header">
				<h3 class="box-title">充值跳转中...</h3>
						<!-- <button style="margin-left: 10px;" type="button" onclick="href('userAccount/initUserAccount.action');" class="btn btn-info pull-right btn-sm">返回</button> -->
            </div>
            <div class="box-body">
       			<form id="alipaysubmit" name="alipaysubmit" action="https://mapi.alipay.com/gateway.do?_input_charset=utf-8" method="get">
       			<input type="hidden" name="sign" value="${sign}"/>
       			<input type="hidden" name="body" value="${body}"/>
       			<input type="hidden" name="_input_charset" value="${_input_charset}"/>
       			<input type="hidden" name="subject" value="${subject}"/>
       			<input type="hidden" name="total_fee" value="${total_fee}"/>
       			<input type="hidden" name="sign_type" value="${sign_type}"/>
       			<input type="hidden" name="service" value="${service}"/>
       			<input type="hidden" name="notify_url" value="${notify_url}"/>
       			<input type="hidden" name="partner" value="${partner}"/>
       			<input type="hidden" name="seller_id" value="${seller_id}"/>
       			<input type="hidden" name="out_trade_no" value="${out_trade_no}"/>
       			<input type="hidden" name="payment_type" value="${payment_type}"/>
       			<input type="hidden" name="return_url" value="${return_url}"/>
       			<input type="hidden" name="show_url" value="${show_url}"/>
       			</form>
              </div>
              </div>
           </div>
      </div>
	</section>
<script type="text/javascript">
document.forms['alipaysubmit'].submit();
</script>

