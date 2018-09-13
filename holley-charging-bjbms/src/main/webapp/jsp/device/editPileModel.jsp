<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/global/top.jsp" %>
<html lang="zh-CN">
<head>
  <title>电桩型号新增/修改</title>
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
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>品牌名称</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="brand" name="brand" maxlength="25" value="<s:property value='#request.pileModel.brand'/>"
	      	       placeholder="如：Tellus TP-EVWA-220-3"/>
	      </div>
	      <label class="col-sm-2 control-label">产品编号</label>
	        <div class="col-sm-4">
	      		<input type="text" class="form-control" id="num" name="num" maxlength="25" value="<s:property value='#request.pileModel.num'/>"/>
	        </div>
    	</div>
    	<div class="form-group">
			<label class="col-sm-2 control-label"><span style="color: red;">*</span>充电方式</label>
	        <div class="col-sm-4">
		      	<select id="chaWay" name="chaWay" class="form-control">
			     	<s:iterator value="#request.currentTypeList" var="item" status="st">
			     		<s:if test="#request.pileModel.chaWay == #item.value">
			     			<option value="${item.value}" selected="selected">${item.text}</option>
			     		</s:if>
			     		<s:else>
				     		<option value="${item.value}">${item.text}</option>
			     		</s:else>
			     	</s:iterator>
			     </select>
	        </div>
	        <label class="col-sm-2 control-label"><span style="color: red;">*</span>充电类型</label>
	       <div class="col-sm-4">
		      	<select id="chaType" name="chaType" class="form-control">
			     	<s:iterator value="#request.powerTypeList" var="item" status="st">
			     		<s:if test="#request.pileModel.chaType == #item.value">
			     			<option value="${item.value}" selected="selected">${item.name}</option>
			     		</s:if>
			     		<s:else>
				     		<option value="${item.value}">${item.name}</option>
			     		</s:else>
			     	</s:iterator>
			     </select>
	        </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">是否智能</label>
	      <div class="col-sm-4">
	      	<s:iterator value="#request.whetherList" id="item" status="st">
				<s:if test="#request.pileModel.isIntelligent == value">
					<label class="checkbox-inline"> 
						<input type="radio" id="isIntelligent" name="isIntelligent" value='<s:property value="value"/>' checked><s:property value="text" />
					</label>
				</s:if>
				<s:else>
					<label class="checkbox-inline"> 
						<input type="radio" name="isIntelligent" id="isIntelligent" value='<s:property value="value"/>'><s:property value="text" />
					</label>
				</s:else>
			</s:iterator>
	      </div>
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>标准</label>
	         <div class="col-sm-4">
		      	<select id="standard" name="standard" class="form-control">
			     	<s:iterator value="#request.intfTypeList" var="item" status="st">
			     		<s:if test="#request.pileModel.standard == #item.value">
			     			<option value="${item.value}" selected="selected">${item.text}</option>
			     		</s:if>
			     		<s:else>
				     		<option value="${item.value}">${item.text}</option>
			     		</s:else>
			     	</s:iterator>
			     </select>
	        </div> 
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">输入电压</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="inV" name="inV" maxlength="25" value="<s:property value='#request.pileModel.inV'/>"
	      	       placeholder="如：220V"/>
	      </div>
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>输出电压</label>
	        <div class="col-sm-4">
	      		<input type="text" class="form-control" id="outV" name="outV" maxlength="25" value="<s:property value='#request.pileModel.outV'/>"
	      		       placeholder="如：220V"/>
	        </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">最大输出功率</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="maxP" name="maxP" maxlength="25" value="<s:property value='#request.pileModel.maxP'/>"
	      	       placeholder="如：7500W"/>
	      </div>
	      <label class="col-sm-2 control-label"><span style="color: red;">*</span>额定功率</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="ratP" name="ratP" maxlength="25" value="<s:property value='#request.pileModel.ratP'/>"
	      		   placeholder="如：7500W"/>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">外壳材质</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="hull" name="hull" maxlength="25" value="<s:property value='#request.pileModel.hull'/>"
	      		   placeholder="如：不锈钢"/>
	      </div>
	      <label class="col-sm-2 control-label">尺寸</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="size" name="size" maxlength="25" value="<s:property value='#request.pileModel.size'/>"
	      		   placeholder="如：446*230*150mm"/>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">防护等级</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="proLevel" name="proLevel" maxlength="25" value="<s:property value='#request.pileModel.proLevel'/>"
	      	       placeholder="如：IP54"/>
	      </div>
	      <label class="col-sm-2 control-label">缆线长度</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="lineLen" name="lineLen" maxlength="25" value="<s:property value='#request.pileModel.lineLen'/>"       
	      		   placeholder="如：2~3m"/>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">频率</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="rate" name="rate" maxlength="25" value="<s:property value='#request.pileModel.rate'/>"
	      		   placeholder="如：50Hz~+-1Hz"/>
	      </div>
	      <label class="col-sm-2 control-label">计量精度</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="meaAcc" name="meaAcc" maxlength="25" value="<s:property value='#request.pileModel.meaAcc'/>"
	      		   placeholder="如：2%"/>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">重量</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="weight" name="weight" maxlength="25" value="<s:property value='#request.pileModel.weight'/>"
	      		   placeholder="如：12kg"/>
	      </div>
	      <label class="col-sm-2 control-label">用户界面</label>
	        <div class="col-sm-4">
	      		<input type="text" class="form-control" id="window" name="window" maxlength="25" value="<s:property value='#request.pileModel.window'/>"
	      			   placeholder="如：LCD"/>
	        </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">工作温度</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="workTem" name="workTem" maxlength="25" value="<s:property value='#request.pileModel.workTem'/>"
	      		   placeholder="如：-25℃~+75℃"/>
	      </div>
	      <label class="col-sm-2 control-label">相对湿度</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="relaHum" name="relaHum" maxlength="25" value="<s:property value='#request.pileModel.relaHum'/>"
	      		    placeholder="如：5%~95%"/>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">海拔</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="altitude" name="altitude" maxlength="25" value="<s:property value='#request.pileModel.altitude'/>"
	      		   placeholder="如：≤2000"/>
	      </div>
	      <label class="col-sm-2 control-label">状态</label>
	        <div class="col-sm-4">
	      		<select id="status" name="status" class="form-control">
			     	<s:iterator value="#request.statusList" var="item" status="st">
			     		<s:if test="#request.pileModel.status == #item.value">
			     			<option value="${item.value}" selected="selected">${item.text}</option>
			     		</s:if>
			     		<s:else>
				     		<option value="${item.value}">${item.text}</option>
			     		</s:else>
			     	</s:iterator>
			     </select>
	        </div> 
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">安装方式</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="insMethod" name="insMethod" maxlength="25" value="<s:property value='#request.pileModel.insMethod'/>"
	      		   placeholder="如：落地式/壁挂式/便携式"/>
	      </div>
	      <label class="col-sm-2 control-label">工作标注</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="workSta" name="workSta" maxlength="25" value="<s:property value='#request.pileModel.workSta'/>"
	      		   placeholder="如：GB/T 20234.2-2011"/>
	      </div>
    	</div>
    	<div class="form-group">
	      <label class="col-sm-2 control-label">认证</label>
	      <div class="col-sm-4">
	      	<input type="text" class="form-control" id="identify" name="identify" maxlength="25" value="<s:property value='#request.pileModel.identify'/>"
	      			placeholder="如：Q/GDW485-2010"/>
	      </div>
    	</div>
    	<div class="form-group">
	      <div class="col-sm-offset-2 col-sm-10">
	         <button id="saveBtn" type="button" class="btn btn-primary">保存</button>
	         <button id="cancelBtn" type="button" class="btn btn-primary" onclick="gobackAndReload()">取消</button>
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

