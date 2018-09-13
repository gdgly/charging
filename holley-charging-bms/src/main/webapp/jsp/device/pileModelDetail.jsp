<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>电桩型号详细</title>
  <%@include file="../common/global/meta.jsp"%>
  <%@include file="../common/global/js.jsp" %>
<style type="text/css">
</style>
</head>
<body>
  <%@include file="../common/global/header.jsp"%>
  <!--main-content-->
  <div class="container">
  	<form id="pileModelForm" class="form-horizontal" role="form">
		<div class="form-group">
	      <label class="col-sm-2 control-label">品牌名称</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.pileModel.brand"/></p>
	      </div>
	      <label class="col-sm-2 control-label">产品编号</label>
	        <div class="col-sm-4">
	        	<p class="form-control-static"><s:property value="#request.pileModel.num"/></p>
	        </div>
    	</div>
    	<div class="form-group">
			<label class="col-sm-2 control-label">充电方式</label>
	        <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.pileModel.chaWayDesc"/></p>
	        </div>
	        <label class="col-sm-2 control-label">充电类型</label>
	        <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.pileModel.chaTypeDesc"/></p>
	        </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">是否智能</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.pileModel.isIntelligentDesc"/></p>
	      </div>
	      <label class="col-sm-2 control-label">标准</label>
	        <div class="col-sm-4">
		      	<p class="form-control-static"><s:property value="#request.pileModel.standardDesc"/></p>
	        </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">输入电压</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.pileModel.inV"/></p>
	      </div>
	      <label class="col-sm-2 control-label">输出电压</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.pileModel.outV"/></p>
	        </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">最大输出功率</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.pileModel.maxP"/></p>
	      </div>
	      <label class="col-sm-2 control-label">额定功率</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.pileModel.ratP"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">外壳材质</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.pileModel.hull"/></p>
	      </div>
	      <label class="col-sm-2 control-label">尺寸</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.pileModel.size"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">防护等级</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.pileModel.proLevel"/></p>
	      </div>
	      <label class="col-sm-2 control-label">缆线长度</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.pileModel.lineLen"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">频率</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.pileModel.rate"/></p>
	      </div>
	      <label class="col-sm-2 control-label">计量精度</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.pileModel.meaAcc"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">重量</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.pileModel.weight"/></p>
	      </div>
	      <label class="col-sm-2 control-label">用户界面</label>
	        <div class="col-sm-4">
	        	<p class="form-control-static"><s:property value="#request.pileModel.window"/></p>
	        </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">工作温度</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.pileModel.workTem"/></p>
	      </div>
	      <label class="col-sm-2 control-label">相对湿度</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.pileModel.relaHum"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">海拔</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.pileModel.altitude"/></p>
	      </div>
	      <label class="col-sm-2 control-label">状态</label>
	        <div class="col-sm-4">
	      		<p class="form-control-static"><s:property value="#request.pileModel.statusDesc"/></p>
	        </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">安装方式</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.pileModel.insMethod"/></p>
	      </div>
	      <label class="col-sm-2 control-label">工作标注</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.pileModel.workSta"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">认证</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.pileModel.identify"/></p>
	      </div>
	      <label class="col-sm-2 control-label">更新时间</label>
	      <div class="col-sm-4">
	      	<p class="form-control-static"><s:property value="#request.pileModel.updateTimeStr"/></p>
	      </div>
    	</div>
    	<div class="form-group">
	      <div class="col-sm-offset-2 col-sm-10">
	         <button  type="button" class="btn btn-primary" onclick="gobackAndReload()">返回</button>
	      </div>
   		</div>
    </form>
  </div>
  
  <script type="text/javascript">
  	var requestType = <s:property value='#request.requestType'/>
  	var modelid = "<s:property value='#request.pileModel.id'/>";
  </script>
  <script src="res/js/device/editPileModel.js" type="text/javascript"></script>
</body>
</html>

