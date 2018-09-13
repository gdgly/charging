package com.holley.charging.bms.person.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.holley.charging.action.BaseAction;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.model.bus.BusUserReal;
import com.holley.charging.model.bus.BusUserRealKey;
import com.holley.charging.service.website.UserService;
import com.holley.common.constants.Globals;
import com.holley.common.constants.charge.NoticeTypeEnum;
import com.holley.common.constants.charge.RealVerifyStatusEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.util.DateUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.MsgUtil;

/**
 * 个人实名
 * 
 * @author zdd
 */
public class UserRealVerifyAction extends BaseAction {

    private final static Logger logger           = Logger.getLogger(UserRealVerifyAction.class);
    private static final long   serialVersionUID = 1L;
    private UserService         userService;
    private Page                page;

    /**
     * 个人实名认证记录列表初始化
     * 
     * @return
     */
    public String init() {
        RealVerifyStatusEnum[] realVerifyStatusList = RealVerifyStatusEnum.values();

        this.getRequest().setAttribute("realVerifyStatusList", realVerifyStatusList);
        this.getRequest().setAttribute("verifyStatus", 0);
        return SUCCESS;
    }

    /**
     * 查询个人实名审核记录列表
     * 
     * @return
     */
    public String queryRealVerifyList() {
        String startdate = this.getParameter("startdate");
        String enddate = this.getParameter("enddate");
        String status = this.getParameter("status");
        String keyword = this.getParameter("keyword");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");

        Map<String, Object> params = new HashMap<String, Object>();

        if (StringUtil.isNotEmpty(startdate)) {
            params.put("startDate", DateUtil.ShortStrToDate(startdate));
        }
        if (StringUtil.isNotEmpty(enddate)) {
            params.put("endDate", DateUtil.ShortStrToDate(enddate));
        }
        if (StringUtil.isNotEmpty(status) && !status.equals("0")) {
            params.put("status", Short.valueOf(status));
        }
        if (StringUtil.isNotEmpty(keyword)) {
            params.put("keyword", keyword);
        }

        int pageIndex = 1;
        int pageLimit = Globals.PAGE_LIMIT;
        if (StringUtil.isNotEmpty(pageindex)) {
            pageIndex = Integer.valueOf(pageindex);
        }
        if (StringUtil.isNotEmpty(pagelimit)) {
            pageLimit = Integer.valueOf(pagelimit);
        }
        Page page = this.returnPage(pageIndex, pageLimit);
        params.put(Globals.PAGE, page);

        List<BusUserReal> list = userService.selectUserRealByPage(params);
        for (BusUserReal record : list) {
            if (record.getAddTime() != null) {
                record.setAddTimeStr(DateUtil.DateToLongStr(record.getAddTime()));
            }
            if (record.getStatus() != null) {
                record.setStatusDesc(RealVerifyStatusEnum.getText(record.getStatus().intValue()));
            }

        }
        page.setRoot(list);
        this.page = page;
        return SUCCESS;
    }

    /**
     * 用户实名审核初始化
     * 
     * @return
     */
    public String realInfoInit() {
        String userId = this.getParameter("userid");
        String addTimeStr = this.getParameter("addTimeStr");
        if (StringUtil.isEmpty(userId)) {
            this.message = "用户Id为空.";
            return SUCCESS;
        }
        if (StringUtil.isEmpty(addTimeStr)) {
            this.message = "提交时间为空.";
            return SUCCESS;
        }
        BusUserRealKey key = new BusUserRealKey();
        key.setUserId(Integer.valueOf(userId));
        key.setAddTime(DateUtil.LongStrToDate(addTimeStr));
        BusUserReal userReal = userService.selectUserRealByPK(key);
        if (userReal != null) {
            userReal.setAddTimeStr(DateUtil.DateToLongStr(userReal.getAddTime()));
            if (userReal.getStatus() != null) {
                userReal.setStatusDesc(RealVerifyStatusEnum.getText(userReal.getStatus()));
            }
            BusUser user = userService.selectUserByCache(Integer.valueOf(userId));
            if (user != null) {
                userReal.setUsername(user.getUsername());
                userReal.setPhone(user.getPhone());
            }
        }
        this.getRequest().setAttribute("userReal", userReal);
        this.getRequest().setAttribute("statusList", RealVerifyStatusEnum.getVerifyResultList());
        this.getRequest().setAttribute("noticeTypeList", NoticeTypeEnum.getMessageAndSms());
        return SUCCESS;
    }

    public String detailInit() {
        String userId = this.getParameter("userid");
        String addTimeStr = this.getParameter("addTimeStr");
        if (StringUtil.isEmpty(userId)) {
            this.message = "用户Id为空.";
            return SUCCESS;
        }
        if (StringUtil.isEmpty(addTimeStr)) {
            this.message = "提交时间为空.";
            return SUCCESS;
        }
        BusUserRealKey key = new BusUserRealKey();
        key.setUserId(Integer.valueOf(userId));
        key.setAddTime(DateUtil.LongStrToDate(addTimeStr));
        BusUserReal userReal = userService.selectUserRealByPK(key);
        if (userReal != null) {
            userReal.setAddTimeStr(DateUtil.DateToLongStr(userReal.getAddTime()));
            if (userReal.getStatus() != null) {
                userReal.setStatusDesc(RealVerifyStatusEnum.getText(userReal.getStatus()));
            }
            BusUser user = userService.selectUserByCache(Integer.valueOf(userId));
            if (user != null) {
                userReal.setUsername(user.getUsername());
                userReal.setPhone(user.getPhone());
            }
        }
        this.getRequest().setAttribute("userReal", userReal);
        return SUCCESS;
    }

    /**
     * 实名审核
     * 
     * @return
     * @throws Exception
     * @throws NumberFormatException
     */
    public String realVerify() throws NumberFormatException, Exception {
        String userid = this.getParameter("userid");
        String addtimestr = this.getParameter("addtimestr");
        String validstatus = this.getParameter("validstatus");
        String remark = this.getParameter("remark");
        String noticetypes = this.getParameter("noticetypes");

        if (!NumberUtils.isNumber(userid)) {
            this.message = "用户Id格式不正确.";
            this.success = false;
            return SUCCESS;
        }

        if (StringUtil.isEmpty(addtimestr)) {
            this.message = "提交时间不能为空.";
            this.success = false;
            return SUCCESS;
        }

        if (StringUtil.isEmpty(validstatus)) {
            this.message = "审核状态不能为空.";
            this.success = false;
            return SUCCESS;
        }

        RealVerifyStatusEnum statusEnum = RealVerifyStatusEnum.getEnmuByValue(Integer.valueOf(validstatus));
        if (statusEnum == null) {
            this.success = false;
            this.message = "审核状态不符合.";
            return SUCCESS;
        }

        BusUserRealKey key = new BusUserRealKey();
        key.setUserId(Integer.valueOf(userid));
        key.setAddTime(DateUtil.LongStrToDate(addtimestr));
        BusUserReal userReal = userService.selectUserRealByPK(key);
        if (userReal == null) {
            this.success = false;
            this.message = "个人实名记录不存在！";
            return SUCCESS;
        }
        if (!RealVerifyStatusEnum.VERIFYING.getShortValue().equals(userReal.getStatus())) {
            this.success = false;
            this.message = "个人实名信息已审核，不能重复审核.";
            return SUCCESS;
        }

        if (RealVerifyStatusEnum.FAILED == statusEnum) {
            if (StringUtil.isNotEmpty(remark)) {
                userReal.setRemark(remark);
            } else {
                this.success = false;
                this.message = "请填写审核失败原因！";
                return SUCCESS;
            }
        }

        String msg = "";
        try {
            msg = userService.updateUserRealAndInfo(userReal, statusEnum);
        } catch (Exception e) {
            msg = "实名审核失败!";
            e.printStackTrace();
        }
        if ("success".equals(msg)) {
            this.success = true;
            // 发送短信和站内信通知用户
            sendValidResult(Integer.valueOf(userid), statusEnum, remark, noticetypes);
        } else {
            this.success = false;
            this.message = msg;
        }
        return SUCCESS;
    }

    /**
     * @param userid
     * @param statusEnum
     * @param validRemark
     * @param noticetypes<br>
     * 短信模板内容:尊敬的用户：您好！${content}审核${result}，请注意查看。
     */
    private void sendValidResult(Integer userid, RealVerifyStatusEnum statusEnum, String validRemark, String noticetypes) {
        if (StringUtil.isEmpty(noticetypes)) {
            return;
        }
        String[] noticetypeArray = noticetypes.split(",");
        NoticeTypeEnum typeenum;
        String result = "";
        if (RealVerifyStatusEnum.FAILED == statusEnum) {
            if (StringUtil.isNotEmpty(validRemark)) {
                result = "不通过（原因：" + validRemark + "）";
            } else {
                result = "不通过";
            }
        } else {
            result = "通过";
        }
        for (String type : noticetypeArray) {
            typeenum = NoticeTypeEnum.getEnmuByValue(Integer.parseInt(type));
            if (NoticeTypeEnum.MESSAGE == typeenum) {// 站内信发送
                String content = "尊敬的用户：您好！个人实名审核" + result + "，请注意查看。";
                MsgUtil.sendMessage(userid, content, "实名审核");
            } else if (NoticeTypeEnum.SMS == typeenum) {// 短信发送
                BusUser user = userService.selectUserByPrimaryKey(userid);
                if (user != null) {
                    MsgUtil.sendVerifySMS(user.getPhone(), "个人实名", result);
                    logger.info("-----【个人实名审核，短信通知】-----phone=" + user.getPhone() + ",content=" + result);
                }
            }
        }
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Page getPage() {
        return page;
    }

}
