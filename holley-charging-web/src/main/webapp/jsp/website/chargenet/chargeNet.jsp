<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../../common/global/top.jsp" %>
<html lang="zh-CN" style="height: 100%;">
<head>
  <title>充电网络</title>
  <%@include file="../../common/global/meta.jsp"%>
  <link rel="stylesheet" href="res/css/website/chargeNet.css" >
<style type="text/css">
</style>
</head>
<body id="body">
  <%@include file="../../common/global/header.jsp"%>
<!--main-->
<div id="content" class="container-fluid main">
    	<div class="row" style="height: 100%;">
	        <!--map-->
	        <div class="col-sm-9" style="height: 100%;">
	          <div style="height: 100%;" id="mapContainer">
	          </div>
	        </div>
	        <!--slide--->
	        <div class="col-sm-3">
	        	<div id="stationResult" style="padding-top: 20px;">
	                <form method="post" action="">
	                    <div class="input-group">
	                      <input class="form-control" type="text" name="keyword" id="keyword" placeholder="充电点名称/地址" style="border-radius: 0px;">
	                      <span class="input-group-addon" id="queryStationBtn" style="cursor:pointer; background-color: #0a94f2;border-color:#0a94f2;color: #ffffff;border-radius: 0px;">
	                          <span class="glyphicon glyphicon-search"></span>
	                      </span>
	                    </div>
	                </form>
	              	<ul class="list-group" id="stationListGrop" style="overflow-y: auto;">
	              	</ul>
	        	</div>
	          <div style="display: none;" id="pileResult">
	          	<button type="button" class="btn btn-default btn-lg" aria-label="Left Align" onclick="back()" style="border-style: none;">
				  <span class="glyphicon glyphicon-menu-left" aria-hidden="true"></span>
				</button>
	          	<ul class="list-group" id="pileListGrop" style="overflow-y: auto;">
	             </ul>
	          </div>
	         <!-- <div class="pull-right" style="position: absolute;top: 50%;right: 5px;width: 25px;height: 70px;">
	          	<span class="input-group-addon" id="filterPopover" style="color:#0a94f2;height: 80px;" title="筛选条件"  
			      data-container="body" data-toggle="popover" data-placement="left">筛<br>选
                         
                     </span>
	          </div> -->
	        </div>
    	</div>
  </div>

  <%@include file="../../common/global/js.jsp" %>
  <div class="hidden-xs">
	  <%@include file="../../common/global/footer.jsp"%>
  </div>
  <script type="text/javascript" src="//api.map.baidu.com/api?v=2.0&ak=ag2fYhtdY5BgQWqi6Xwsx0Pp"></script>
  <script type="text/javascript" src="//api.map.baidu.com/library/TextIconOverlay/1.2/src/TextIconOverlay_min.js"></script>
  <script type="text/javascript" src="//api.map.baidu.com/library/MarkerClusterer/1.2/src/MarkerClusterer_min.js"></script>
  <script type="text/javascript" src="res/js/website/chargenet/indexGisBaidu.js"></script>
  <script type="text/javascript">
  var currentTypeList = <s:property value="#request.currentTypeList" escape="false"/>;
  var powerTypeList = <s:property value="#request.powerTypeList" escape="false"/>;
  </script>
  <script src="res/js/website/chargenet/chargeNet.js" type="text/javascript"></script>
</body>
</html>

