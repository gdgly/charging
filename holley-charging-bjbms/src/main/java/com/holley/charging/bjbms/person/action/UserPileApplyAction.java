package com.holley.charging.bjbms.person.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import com.holley.charging.action.BaseAction;
import com.holley.charging.model.bus.BusPileApply;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.service.bussiness.DeviceService;
import com.holley.charging.service.website.UserService;
import com.holley.common.constants.Globals;
import com.holley.common.constants.LogOperatorEnum;
import com.holley.common.constants.charge.PileApplyValidStatusEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.util.DateUtil;
import com.holley.common.util.JsonUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.LogUtil;

import net.sf.json.JSONObject;

/**
 * 个人桩代管相关ACTION
 * 
 * @author zdd
 */
public class UserPileApplyAction extends BaseAction {

    private static final long serialVersionUID = 1L;
    private DeviceService     deviceService;
    private UserService       userService;
    private Page              page;

    /**
     * 个人桩代管申请列表初始化
     * 
     * @return
     */
    public String init() {
        this.getRequest().setAttribute("statusList", PileApplyValidStatusEnum.values());
        return SUCCESS;
    }

    public String verifyInit() {
        String id = getParameter("id");
        String requestype = getParameter("requestype");
        if (!NumberUtils.isNumber(id) && StringUtil.isEmpty(requestype)) {
            this.getRequest().setAttribute("msg", "参数非法");
            return "msg";
        }
        BusPileApply record = deviceService.selectPileApplyByPK(Integer.valueOf(id));
        if (record != null && record.getUserId() != null) {
            BusUser user = userService.selectBusUserByPrimaryKey(record.getUserId());
            if (user != null) {
                record.setUsername(user.getUsername());
                record.setUserphone(user.getPhone());
            }
        }
        this.getRequest().setAttribute("pileApply", record);
        this.getRequest().setAttribute("statusList", PileApplyValidStatusEnum.getVerifyResultList());
        this.getRequest().setAttribute("requestType", requestype);
        return SUCCESS;
    }

    /**
     * 分页查询个人桩代管申请
     * 
     * @return
     */
    public String queryList() {
        String startdate = this.getParameter("startdate");
        String enddate = this.getParameter("enddate");
        String keyword = this.getParameter("keyword");
        String validstatus = this.getParameter("validstatus");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");

        if (StringUtil.isNotDigits(validstatus, pageindex, pagelimit)) {
            this.success = false;
            this.message = "参数格式不正确.";
            return SUCCESS;
        }

        Page page = this.returnPage(Integer.valueOf(pageindex), Integer.valueOf(pagelimit));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(Globals.PAGE, page);
        if (StringUtil.isNotEmpty(keyword)) {
            params.put("keyword", StringUtil.trim(keyword));
        }
        if (StringUtil.isNotEmpty(validstatus) && !"0".equals(validstatus)) {
            params.put("validstatus", Short.valueOf(validstatus));
        }
        if (StringUtil.isNotEmpty(startdate)) {
            params.put("startdate", DateUtil.ShortStrToDate(startdate));
        }
        if (StringUtil.isNotEmpty(enddate)) {
            params.put("enddate", DateUtil.ShortStrToDate(enddate));
        }
        List<BusPileApply> list = deviceService.selectPileApplyByParams(params);
        page.setRoot(list);
        this.page = page;
        return SUCCESS;
    }

    /**
     * 审核个人桩代管申请
     * 
     * @return
     */
    public String applyVerify() {
        String id = this.getParameter("id");
        String validstatus = this.getParameter("validstatus");
        String validremark = this.getParameter("validremark");
        if (StringUtil.isNotNumber(id, validstatus)) {
            this.success = false;
            this.message = "参数非法";
            return SUCCESS;
        }
        BusPileApply record = deviceService.selectPileApplyByPK(Integer.valueOf(id));
        if (record == null) {
            this.success = false;
            this.message = "桩代管申请不存在！";
            return SUCCESS;
        }
        if (PileApplyValidStatusEnum.PROCESSED.getShortValue().equals(record.getValidStatus())) {
            this.success = false;
            this.message = "桩代管申请已审核,不能重复审核！";
            return SUCCESS;
        }
        PileApplyValidStatusEnum statusEnum = PileApplyValidStatusEnum.getEnmuByValue(Integer.parseInt(validstatus));
        if (PileApplyValidStatusEnum.PROCESSED != statusEnum) {
            this.success = false;
            this.message = "桩代管申请的审核状态不不符合！";
            return SUCCESS;
        }
        BusPileApply apply = new BusPileApply();
        apply.setId(record.getId());
        apply.setValidStatus(statusEnum.getShortValue());
        apply.setValidTime(new Date());
        if (StringUtil.isNotEmpty(validremark)) {
            apply.setValidRemark(validremark);
        }
        if (deviceService.updatePileApplyByPKSelective(apply) > 0) {
            // 插入操作日志
            String webuserid = getSessionBmsUserId();
            Integer userid = NumberUtils.isNumber(webuserid) ? Integer.valueOf(webuserid) : 0;
            String content = JSONObject.fromObject(apply, JsonUtil.returnJosnConfig(true, Date.class)).toString();
            LogUtil.recordDocumentlog(userid, LogOperatorEnum.MODIFY, getRemoteIP(), "个人桩代管审核", content, true);
        }
        return SUCCESS;
    }

    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Page getPage() {
        return page;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

}
