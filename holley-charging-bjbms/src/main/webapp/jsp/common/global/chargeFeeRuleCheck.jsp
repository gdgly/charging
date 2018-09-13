<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>

<!-- 模态框（Modal） -->
<div class="modal fade" id="chargeFeeRuleCheckModal" tabindex="-1" role="dialog" aria-labelledby="chargeFeeRuleCheckModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title" id="chargeFeeRuleCheckModalLabel">
					计费模型下发
				</h4>
			</div>
			<div class="modal-body">
		<caption>
			<form class="form-horizontal" role="form" id="chargeFeeRuleCheckModalForm">
			 <div class="form-group">
			 		 <label for="feeRule" class="col-sm-3 control-label">计费模型：</label>
			 		<div class="col-sm-6">
								<select id="feeRule" name="feeRule" class="form-control">
									<s:iterator value="#request.chargeRuleList" id="item"
										status="statu">
										<option value="<s:property value='id'/>" >
											<s:property value='name' />
										</option>
									</s:iterator>
								</select>
								</div>
			</div>
				<div id="chargeFeeDiv" class="form-group">
					<label class="col-sm-3 control-label">单一电费(元)：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="chargeFee"
							name="chargeFee" placeholder="保留小数点后4位" maxlength="10"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label"><span style="color: red;">*</span>服务费(元)：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="seviceFee"
							name="seviceFee" placeholder="保留小数点后4位" maxlength="10"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label"><span style="color: red;">*</span>停车费(元)：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="parkFee"
							name="parkFee" placeholder="保留小数点后4位" maxlength="10"/>
					</div>
				</div>
				<div class="form-group">
					<label for="activeTime" class="col-sm-3 control-label"><span style="color: red;">*</span>启用时间：</label>
					<div class="col-sm-6">
						<div class='input-group date' id='activeTimeDiv'>
								<input id="activeTime" name="activeTime" type='text'
									class="form-control" placeholder="费用规则启用时间"/> <span class="input-group-addon"><span
									class="glyphicon glyphicon-calendar"></span> </span>
						</div>
					</div>
				</div>
					<div class="form-group">
					<label class="col-sm-3 control-label">描述：</label>
					<div class="col-sm-7">
					<textarea class="form-control" rows="2" disabled="disabled" id="chargeRuleDesc" ></textarea>
					</div>
					</div>
					<div class="form-group">
					<label class="col-sm-3 control-label">下发充电站：</label>
					<div class="col-sm-7">
					<textarea class="form-control" rows="3" disabled="disabled" id="issuedStation" ></textarea>
					</div>
					</div>
		</form>
	</caption>
			</div>
			<div class="modal-footer">
				 <div class="form-group pull-left">
				 <p><span style="color: red;">注意:</span>计费模型为单一电价时需要填写金额，选择多费率模板则不需要填写。</p>
				 </div>
			 <div class="form-group pull-right">
				
				<button type="button" class="btn btn-primary" id="doIssuedBtn">
					确定
				</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭
				</button>
				</div>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>