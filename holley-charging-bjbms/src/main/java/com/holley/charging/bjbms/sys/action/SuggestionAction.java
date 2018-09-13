package com.holley.charging.bjbms.sys.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import com.holley.charging.action.BaseAction;
import com.holley.charging.model.bus.BusSuggestion;
import com.holley.charging.model.bus.BusSuggestionKey;
import com.holley.charging.service.website.UserService;
import com.holley.common.constants.Globals;
import com.holley.common.constants.LogOperatorEnum;
import com.holley.common.constants.charge.SuggestionStatusEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.util.DateUtil;
import com.holley.common.util.JsonUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.LogUtil;

import net.sf.json.JSONObject;

/**
 * 意见反馈相关ACTION
 * 
 * @author zdd
 */
public class SuggestionAction extends BaseAction {

    private static final long serialVersionUID = 1L;
    private UserService       userService;
    private Page              page;

    /**
     * 意见反馈列表初始化
     * 
     * @return
     */
    public String init() {
        this.getRequest().setAttribute("statusList", SuggestionStatusEnum.values());
        return SUCCESS;
    }

    /**
     * 分页查询意见反馈
     * 
     * @return
     */
    public String queryList() {
        String startdate = this.getParameter("startdate");
        String enddate = this.getParameter("enddate");
        String keyword = this.getParameter("keyword");
        String status = this.getParameter("status");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");

        if (StringUtil.isNotDigits(status, pageindex, pagelimit)) {
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
        if (StringUtil.isNotEmpty(status) && !"0".equals(status)) {
            params.put("status", Short.valueOf(status));
        }
        if (StringUtil.isNotEmpty(startdate)) {
            params.put("startdate", DateUtil.ShortStrToDate(startdate));
        }
        if (StringUtil.isNotEmpty(enddate)) {
            params.put("enddate", DateUtil.ShortStrToDate(enddate));
        }
        List<BusSuggestion> list = userService.selectSuggestionByPage(params);
        page.setRoot(list);
        this.page = page;
        return SUCCESS;
    }

    public String dealSuggestion() {
        String userid = getParameter("userid");
        String addtime = getParameter("addtime");
        if (!StringUtil.isDigits(userid) || StringUtil.isEmpty(addtime)) {
            this.success = false;
            this.message = "参数非法";
            return SUCCESS;
        }
        BusSuggestionKey key = new BusSuggestionKey();
        key.setUserId(Integer.valueOf(userid));
        key.setAddTime(DateUtil.LongStrToDate(addtime));
        BusSuggestion record = userService.selectSuggestionByPK(key);
        if (record == null) {
            this.success = false;
            this.message = "意见反馈记录不存在";
            return SUCCESS;
        }
        if (SuggestionStatusEnum.PROCESSED.getShortValue().equals(record.getStatus())) {
            this.success = false;
            this.message = "该意见反馈已处理，不能重复处理";
            return SUCCESS;
        }
        BusSuggestion newrecord = new BusSuggestion();
        newrecord.setUserId(record.getUserId());
        newrecord.setAddTime(record.getAddTime());
        newrecord.setStatus(SuggestionStatusEnum.PROCESSED.getShortValue());
        if (userService.updateSuggestionByPKSelective(newrecord) > 0) {
            String webuserid = getSessionBmsUserId();
            Integer userId = NumberUtils.isNumber(webuserid) ? Integer.valueOf(webuserid) : 0;
            String content = JSONObject.fromObject(newrecord, JsonUtil.returnJosnConfig(true, Date.class)).toString();
            LogUtil.recordDocumentlog(userId, LogOperatorEnum.MODIFY, getRemoteIP(), "意见反馈处理", content, true);
        }

        return SUCCESS;
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
