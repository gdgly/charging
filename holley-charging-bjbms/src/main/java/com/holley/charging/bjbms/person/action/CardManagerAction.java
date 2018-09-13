package com.holley.charging.bjbms.person.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;

import com.holley.charging.action.BaseAction;
import com.holley.charging.model.bus.BusCardRecharge;
import com.holley.charging.model.bus.BusChargeCard;
import com.holley.charging.model.bus.BusChargeCardExample;
import com.holley.charging.model.bus.BusPayment;
import com.holley.charging.model.bus.BusPaymentExample;
import com.holley.charging.model.bus.BusRebate;
import com.holley.charging.model.bus.BusRebateExample;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.model.bus.BusUserExample;
import com.holley.charging.model.bus.BusUserInfo;
import com.holley.charging.model.dcs.DcsChargerecord;
import com.holley.charging.model.dcs.DcsChargerecordExample;
import com.holley.charging.model.def.BadRecordModel;
import com.holley.charging.model.def.ChargeModel;
import com.holley.charging.service.bussiness.ChargingService;
import com.holley.charging.service.website.UserService;
import com.holley.common.cache.charging.CacheKeyProvide.KeySessionTypeEnum;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.Globals;
import com.holley.common.constants.LogOperatorEnum;
import com.holley.common.constants.LogTypeEnum;
import com.holley.common.constants.charge.ChargePayStatusEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.dataobject.WebUser;
import com.holley.common.util.DateUtil;
import com.holley.common.util.NumberUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.model.sys.SysCarBrand;
import com.holley.platform.util.CacheSysHolder;
import com.holley.platform.util.LogUtil;
import com.holley.platform.util.RoleUtil;
import com.holley.web.common.util.Validator;

public class CardManagerAction extends BaseAction {

    private UserService         userService;
    private ChargingService     chargingService;
    private Page                page;
    private Map<String, Object> map;
    private List<SysCarBrand>   modelList;                     // 车辆型号
    private static String       READ_CARDINFO = "readCardInfo"; // 读卡信息
    private static String       REGISTER_CARD = "registerCard"; // 开卡

    public String searchCardInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = "success";
        String cardNo = getParameter("cardNo");
        String type = getParameter("type");
        if (!StringUtil.isEmpty(cardNo) && !StringUtil.isEmpty(type)) {
            BusChargeCardExample cardEmp = new BusChargeCardExample();
            BusChargeCardExample.Criteria cardCr = cardEmp.createCriteria();
            cardCr.andCardNoEqualTo(cardNo);
            List<BusChargeCard> list = userService.selectChargeCardByExample(cardEmp);
            if (READ_CARDINFO.equals(type)) {
                if (list != null && list.size() > 0) {
                    BusChargeCard cardInfo = list.get(0);
                    map.put("cardInfo", cardInfo);
                } else {
                    msg = "充电卡[" + cardNo + "]还未开卡！！";
                }
            } else if (REGISTER_CARD.equals(type)) {
                if (list != null && list.size() > 0) {
                    msg = "充电卡[" + cardNo + "]已经开卡,请更换新的充电卡并刷新！！";
                }
            }

        } else {
            msg = "无充电卡信息！！";
        }
        map.put("msg", msg);
        this.map = map;
        return SUCCESS;
    }

    /**
     * 查询充电卡充值记录
     * 
     * @return
     */
    public String cardRechargeListInit() {
        return SUCCESS;
    }

    /**
     * 查询充电卡充值记录AJAX
     * 
     * @return
     */
    public String showCardRechargeList() {
        WebUser user = getBmsWebuser();
        String keyword = this.getParameter("keyword");
        int pageindex = getParamInt("pageindex");
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(keyword)) {
            params.put("keyword", StringUtil.trim(keyword));
        }
        if (UserTypeEnum.COMPANY == user.getUsertype()) {
            params.put("groupId", user.getUserId());
        }
        Page page = this.returnPage(pageindex, limit);
        params.put(Globals.PAGE, page);
        List<BusCardRecharge> cardRechargeList = userService.selectCardRechargeByPage(params);
        page.setRoot(cardRechargeList);
        this.page = page;
        return SUCCESS;
    }

    /**
     * 清灰色记录
     * 
     * @return
     */
    public String cleanBadRecord() {
        String chargeCardNo = getParameter("chargeCardNo");
        String payNo = getParameter("payNo");
        String totalShouldMoney = getParameter("totalShouldMoney");
        if (StringUtil.isEmpty(totalShouldMoney)) {
            this.message = "应付金额有误！！";
            return SUCCESS;
        }
        BigDecimal totalShouldMoneyD = new BigDecimal(totalShouldMoney);
        // String usableMoney = getParameter("usableMoney");
        Map<String, Object> map = checkCleanBadRecord(chargeCardNo, payNo);
        if ("success".equals(map.get("msg"))) {
            DcsChargerecord newRecord = (DcsChargerecord) map.get("newRecord");
            BusPayment newPay = (BusPayment) map.get("newPay");
            BusChargeCard chargeCard = (BusChargeCard) map.get("chargeCard");
            // chargingService.updatePaymentByPKSelective(newPay);
            // chargingService.updateDcsChargerecordByPrimaryKeySelective(newRecord);

            BusChargeCard updateChargeCard = new BusChargeCard();
            updateChargeCard.setId(chargeCard.getId());
            updateChargeCard.setUsableMoney(NumberUtil.sub(chargeCard.getUsableMoney(), totalShouldMoneyD));
            updateChargeCard.setUpdateTime(new Date());
            updateChargeCard.setFreezeMoney(chargeCard.getFreezeMoney());
            updateChargeCard.setUserId(chargeCard.getUserId());
            userService.updateCleanBadRecord(newRecord, newPay, updateChargeCard, totalShouldMoneyD);

        }
        this.message = (String) map.get("msg");
        return SUCCESS;
    }

    private Map<String, Object> checkCleanBadRecord(String chargeCardNo, String badChargeId) {
        String msg = "success";
        Map<String, Object> map = new HashMap<String, Object>();
        // String[] badChargeIds = null;
        // List<Integer> badChargeIdList = null;
        if (StringUtil.isEmpty(chargeCardNo)) {
            msg = "充电卡信息有误！！";
        } else if (StringUtil.isEmpty(badChargeId)) {
            msg = "扣款记录有误！！";
        }

        if ("success".equals(msg)) {
            BusChargeCardExample cardEmp = new BusChargeCardExample();
            BusChargeCardExample.Criteria cardCr = cardEmp.createCriteria();
            cardCr.andCardNoEqualTo(chargeCardNo);
            List<BusChargeCard> list = userService.selectChargeCardByExample(cardEmp);

            if (list == null || list.size() <= 0) {
                msg = "充电卡[" + chargeCardNo + "]未注册！！";
                map.put("msg", msg);
                return map;
            }
            BusChargeCard chargeCard = list.get(0);
            map.put("chargeCard", chargeCard);
            DcsChargerecordExample recordEmp = new DcsChargerecordExample();
            DcsChargerecordExample.Criteria recordCr = recordEmp.createCriteria();
            recordCr.andPayNoEqualTo(badChargeId);
            recordCr.andCardNoEqualTo(chargeCardNo);
            List<DcsChargerecord> dcsChargerecordList = chargingService.selectDcsChargerecordByExample(recordEmp);

            if (dcsChargerecordList == null || dcsChargerecordList.size() <= 0) {
                msg = "灰记录号[" + badChargeId + "]不存在！！";
                map.put("msg", msg);
                return map;
            }
            DcsChargerecord r = dcsChargerecordList.get(0);
            if (r.getPayStatus() == 1) {// 1成功其他未支付
                msg = "灰记录号[" + badChargeId + "]已经支付成功！！";
                map.put("msg", msg);
                return map;
            }

            BusPaymentExample payEmp = new BusPaymentExample();
            BusPaymentExample.Criteria payCr = payEmp.createCriteria();
            payCr.andTradeNoEqualTo(r.getTradeNo());
            List<BusPayment> paymentList = chargingService.selectChargePaymentByExample(payEmp);
            if (paymentList == null || paymentList.size() <= 0) {
                msg = "交易序号[" + r.getTradeNo() + "]不存在！！";
                map.put("msg", msg);
                return map;
            }
            BusPayment pay = paymentList.get(0);
            if (ChargePayStatusEnum.SUCCESS.getShortValue().equals(pay.getPayStatus())) {
                msg = "交易序号[" + r.getTradeNo() + "]已经支付成功！！";
                map.put("msg", msg);
                return map;
            }

            DcsChargerecord newRecord = new DcsChargerecord();
            newRecord.setId(r.getId());
            newRecord.setPayStatus(new Short("1"));

            BusPayment newPay = new BusPayment();
            newPay.setId(pay.getId());
            newPay.setPayStatus(ChargePayStatusEnum.SUCCESS.getShortValue());

            map.put("newRecord", newRecord);
            map.put("newPay", newPay);
        }
        map.put("msg", msg);
        return map;
    }

    /**
     * 查看灰色记录
     * 
     * @return
     */
    public String showBadChargeList() {
        String chargeCardNo = getParameter("chargeCardNo");// 充电卡卡号
        String badChargeIds = getParameter("badChargeIds");// 灰色充电记录号[XX,XX]形式
        String okArray = getParameter("okArray");
        String badArray = getParameter("badArray");
        Map<String, Object> map = checkShowBadCharge(chargeCardNo, okArray, badArray);
        Map<String, Object> param = new HashMap<String, Object>();
        if ("success".equals(map.get("msg"))) {
            List<String> payNoList = (List<String>) map.get("payNoList");
            List<BadRecordModel> badRecordList = (List<BadRecordModel>) map.get("badRecordList");
            param.put("list", payNoList);
            param.put("cardNo", chargeCardNo);
            List<ChargeModel> badChargeList = chargingService.selectBadCardPaymentByMap(param);
            for (ChargeModel model : badChargeList) {
                for (BadRecordModel recordModel : badRecordList) {
                    if (recordModel.getPayNo().equals(model.getPayNo())) {
                        model.setBadRecordId(recordModel.getBadRecordId());
                        model.setFreeze(recordModel.getFreeze());
                        break;
                    }
                }
            }
            map.put("badChargeList", badChargeList);

        }
        this.map = map;
        return SUCCESS;
    }

    private Map<String, Object> checkShowBadCharge(String chargeCardNo, String okArray, String badArray) {
        String msg = "success";
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> payNoList = new ArrayList<String>();
        List<BadRecordModel> badRecordList = new ArrayList<BadRecordModel>();
        BadRecordModel badRecord = null;
        JSONObject jsonObj = null;
        Object recordId = null;
        String recordIdStr = null;
        Object freeze = null;
        String freezeStr = null;
        Object payNo = null;
        String payNoStr = null;
        if (StringUtil.isEmpty(chargeCardNo)) {
            msg = "充电卡号为空！！";
        } else if (StringUtil.isEmpty(okArray) && StringUtil.isEmpty(badArray)) {
            msg = "无灰色记录！！";
        } else {
            BusChargeCardExample cardEmp = new BusChargeCardExample();
            BusChargeCardExample.Criteria cardCr = cardEmp.createCriteria();
            cardCr.andCardNoEqualTo(chargeCardNo);
            int count = userService.countChargeCardByExample(cardEmp);
            if (count <= 0) {
                msg = "该充电卡[" + chargeCardNo + "]未注册或未开卡！！";
            }
        }
        if ("success".equals(msg)) {
            if (!StringUtil.isEmpty(okArray)) {
                JSONArray okList = JSONArray.fromObject(okArray);
                for (Object obj : okList) {
                    badRecord = new BadRecordModel();
                    jsonObj = JSONObject.fromObject(obj);

                    recordId = jsonObj.get("recordId");
                    recordIdStr = recordId == null ? "" : recordId.toString();
                    badRecord.setBadRecordId(recordIdStr);

                    freeze = jsonObj.get("freeze");
                    freezeStr = freeze == null ? "" : freeze.toString();
                    badRecord.setFreeze(freezeStr);

                    payNo = jsonObj.get("payNo");
                    payNoStr = payNo == null ? "" : payNo.toString();
                    badRecord.setPayNo(payNoStr);

                    badRecordList.add(badRecord);
                    payNoList.add(badRecord.getPayNo());
                }
            }
            if (!StringUtil.isEmpty(badArray)) {
                JSONArray badList = JSONArray.fromObject(badArray);
                for (Object obj : badList) {
                    jsonObj = JSONObject.fromObject(obj);
                    badRecord = new BadRecordModel();

                    recordId = jsonObj.get("recordId");
                    recordIdStr = recordId == null ? "" : recordId.toString();
                    badRecord.setBadRecordId(recordIdStr);

                    freeze = jsonObj.get("freeze");
                    freezeStr = freeze == null ? "" : freeze.toString();
                    badRecord.setFreeze(freezeStr);

                    payNo = jsonObj.get("payNo");
                    payNoStr = payNo == null ? "" : payNo.toString();
                    badRecord.setPayNo(payNoStr);

                    badRecordList.add(badRecord);
                    payNoList.add(badRecord.getPayNo());
                }
            }
            /*
             * String[] ids = badChargeIds.split(","); List<String> badIds = new ArrayList<String>(); for (String id :
             * ids) { badIds.add(id); } map.put("list", badIds); map.put("cardNo", chargeCardNo); List<ChargeModel>
             * badChargeList = chargingService.selectBadCardPaymentByMap(map); map.put("badChargeList", badChargeList);
             */
        }
        map.put("msg", msg);
        map.put("payNoList", payNoList);
        map.put("badRecordList", badRecordList);
        return map;
    }

    /**
     * 修改充电卡密码
     * 
     * @return
     */
    public String changeCardPwd() {
        String chargeCardNo = getParameter("chargeCardNo");// 充电卡卡号
        String newPassword = getParameter("newPassword");// 新密码
        message = checkChangeCardPwd(chargeCardNo, newPassword);
        if ("success".equals(message)) {
            BusChargeCard chargeCard = new BusChargeCard();
            chargeCard.setPassword(newPassword);
            BusChargeCardExample cardEmp = new BusChargeCardExample();
            BusChargeCardExample.Criteria cardCr = cardEmp.createCriteria();
            cardCr.andCardNoEqualTo(chargeCardNo);
            userService.updateChargeCardByExampleSelective(chargeCard, cardEmp);
            // 修改充电卡密码操作日志
            LogUtil.recordOperationLog(NumberUtils.toInt(getSessionBmsUserId()), LogTypeEnum.RUN_RECORD, LogOperatorEnum.ADD, getRemoteIP(), "修改充电卡密码", "[" + chargeCardNo + "]");
        }
        return SUCCESS;
    }

    private String checkChangeCardPwd(String chargeCardNo, String newPassword) {
        String msg = "success";
        if (StringUtil.isEmpty(chargeCardNo)) {
            msg = "充电卡号为空！！";
        } else if (StringUtil.isEmpty(newPassword) || newPassword.length() < 6) {
            msg = "充电卡密码必须大于或等于6位！！";
        } else {
            BusChargeCardExample cardEmp = new BusChargeCardExample();
            BusChargeCardExample.Criteria cardCr = cardEmp.createCriteria();
            cardCr.andCardNoEqualTo(chargeCardNo);
            int count = userService.countChargeCardByExample(cardEmp);
            if (count <= 0) {
                msg = "该充电卡[" + chargeCardNo + "]不存在或未开卡！！";
            }
        }
        return msg;
    }

    /**
     * 充电卡充值
     * 
     * @return
     */
    public String cardRecharge() {
        String chargeCardNo = getParameter("chargeCardNo");// 充电卡卡号
        String rechargeMoney = getParameter("rechargeMoney");// 充值金额
        message = checkCardRecharge(chargeCardNo, rechargeMoney);
        if ("success".equals(message)) {
            BusChargeCardExample cardEmp = new BusChargeCardExample();
            BusChargeCardExample.Criteria cardCr = cardEmp.createCriteria();
            cardCr.andCardNoEqualTo(chargeCardNo);
            List<BusChargeCard> cardList = userService.selectChargeCardByExample(cardEmp);
            BusChargeCard card = cardList.get(0);
            BusCardRecharge cardRecharge = new BusCardRecharge();
            cardRecharge.setAddIp(getRemoteIP());
            cardRecharge.setAddTime(new Date());
            cardRecharge.setCardInfoId(card.getId());
            cardRecharge.setMoney(new BigDecimal(rechargeMoney));
            cardRecharge.setTradeNo(DateUtil.DateToLong14Str(new Date()) + StringUtil.AddjustLength(card.getUserId().toString(), 6, "0"));
            cardRecharge.setWorker(NumberUtils.toInt(getSessionBmsUserId()));
            // 更新充电卡余额信息
            BusChargeCard updateChargeCard = new BusChargeCard();
            updateChargeCard.setId(card.getId());
            updateChargeCard.setUsableMoney(NumberUtil.add(card.getUsableMoney(), cardRecharge.getMoney()));
            updateChargeCard.setFreezeMoney(card.getFreezeMoney());
            updateChargeCard.setUpdateTime(new Date());
            updateChargeCard.setUserId(card.getUserId());
            userService.insertAndUpdateCardRecharge(cardRecharge, updateChargeCard);
        }
        return SUCCESS;
    }

    private String checkCardRecharge(String chargeCardNo, String rechargeMoney) {
        String msg = "success";
        double rechargeMoneyd = NumberUtils.toDouble(rechargeMoney);
        if (StringUtil.isEmpty(chargeCardNo)) {
            msg = "充电卡有误！！";
        } else if (rechargeMoneyd <= 0) {
            msg = "充值金额有误！！";
        } else {
            BusChargeCardExample cardEmp = new BusChargeCardExample();
            BusChargeCardExample.Criteria cardCr = cardEmp.createCriteria();
            cardCr.andCardNoEqualTo(chargeCardNo);
            int count = userService.countChargeCardByExample(cardEmp);
            if (count <= 0) {
                msg = "该充电卡[" + chargeCardNo + "]不存在或未开卡！！";
            }
        }
        return msg;
    }

    /**
     * 充电卡信息初始化
     * 
     * @return
     */
    public String cardInfoInit() {
        getRequest().setAttribute("currentTime", DateUtil.DateToLong14Str(new Date()));
        return SUCCESS;
    }

    /**
     * 进入充电卡业务办理
     * 
     * @return
     */
    public String cardManagerInit() {
        int maxChargeCard = NumberUtils.toInt(RoleUtil.selectRuleByPrimaryKey(RoleUtil.MAX_CHARGECARD_LIMIT));
        maxChargeCard = maxChargeCard > 0 ? maxChargeCard : Globals.MAX_CHAEGECARD_LIMIT;
        getRequest().setAttribute("maxChargeCard", maxChargeCard);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userType", UserTypeEnum.COMPANY.getValue());
        param.put("isLock", WhetherEnum.NO.getValue());
        List<BusUser> companyList = userService.selectBusUserByMap(param);
        this.getRequest().setAttribute("companyList", companyList);// 子公司
        return SUCCESS;
    }

    public String userInfoList() {
        WebUser user = getBmsWebuser();
        String keyword = this.getParameter("keyword");
        int groupId = getParamInt("groupId");
        int pageindex = getParamInt("pageindex");
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(keyword)) {
            params.put("keyword", StringUtil.trim(keyword));
        }
        if (UserTypeEnum.COMPANY == user.getUsertype()) {
            groupId = user.getUserId();
        }
        if (groupId > 0) {
            params.put("groupId", groupId);
        }
        Page page = this.returnPage(pageindex, limit);
        params.put(Globals.PAGE, page);
        List<BusUserInfo> cardInfoList = userService.selectUserInfoForCardByPage(params);
        page.setRoot(cardInfoList);
        this.page = page;
        return SUCCESS;
    }

    /**
     * 开户初始化
     * 
     * @return
     */
    public String registerUserInit() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userType", UserTypeEnum.COMPANY.getValue());
        param.put("isLock", WhetherEnum.NO.getValue());
        List<BusUser> companyList = userService.selectBusUserByMap(param);
        this.getRequest().setAttribute("companyList", companyList);// 子公司
        getRequest().setAttribute("provinceList", CacheSysHolder.getProvinceList());
        getRequest().setAttribute("carBrandList", CacheSysHolder.getCarBrandList());
        BusRebateExample rebateEmp = new BusRebateExample();
        BusRebateExample.Criteria rebateCr = rebateEmp.createCriteria();
        rebateCr.andEndTimeGreaterThan(DateUtil.StrToDate(DateUtil.DateToShortStr(new Date()), DateUtil.TIME_SHORT));
        List<BusRebate> rebateList = userService.selectRebateByExample(rebateEmp);
        this.getRequest().setAttribute("rebateList", rebateList);
        return SUCCESS;
    }

    /**
     * 开户
     * 
     * @return
     */
    public String doRegisterUser() {
        String userObjStr = getParameter("userObjStr");
        Map<String, Object> map = checkCarAndCard(userObjStr);
        if ("success".equals(map.get("msg"))) {
            BusUser user = (BusUser) map.get("userObj");
            BusUserInfo userInfo = (BusUserInfo) map.get("userInfoObj");
            BusUserExample userEmp = new BusUserExample();
            BusUserExample.Criteria userCr = userEmp.createCriteria();
            userCr.andPhoneEqualTo(user.getPhone());
            userCr.andUserTypeEqualTo(UserTypeEnum.PERSON.getShortValue());
            List<BusUser> userList = userService.selectUserByExample(userEmp);
            if (userList != null && userList.size() > 0) {
                map.put("msg", "该手机号码已被注册！！");
            } else {
                try {
                    int userInfoId = userService.insertCardUser(user, userInfo);
                    map.put("newInfoId", userInfoId);
                    // 添加充电卡用户操作日志
                    LogUtil.recordOperationLog(NumberUtils.toInt(getSessionBmsUserId()), LogTypeEnum.RUN_RECORD, LogOperatorEnum.ADD, getRemoteIP(), "添加充电卡用户",
                                               "[" + user.getPhone() + "]");
                } catch (Exception e) {
                    map.put("msg", "开户失败！！");
                }
            }
        }
        this.map = map;
        return SUCCESS;
    }

    /**
     * 请求车型号
     * 
     * @return
     */
    public String requestCarModel() {
        int brand = this.getParamInt("brand");
        if (brand > 0) {
            modelList = CacheSysHolder.getCarModelListByPid(brand);
        }
        return SUCCESS;
    }

    /**
     * 开卡初始化
     * 
     * @return
     */
    public String registerCardInit() {
        WebUser bmsUser = ChargingCacheUtil.getSession(getSessionBmsUserId(), KeySessionTypeEnum.BMS, null);
        int infoId = getParamInt("infoId");
        if (infoId > 0) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("infoId", infoId);
            param.put("userType", UserTypeEnum.PERSON.getShortValue());
            List<BusUser> list = userService.selectBusUserByMap(param);
            if (list != null && list.size() > 0) {
                BusUser user = list.get(0);
                BusUserInfo userInfo = userService.selectUserInfoByPrimaryKey(user.getInfoId());
                getRequest().setAttribute("user", user);// 开卡用户
                getRequest().setAttribute("userInfo", userInfo);// 开卡用户信息
                getRequest().setAttribute("worker", bmsUser);// 操作员用户
                getRequest().setAttribute("password", Globals.DEFAULT_PASSWORD);// 默认充电卡开卡密码可修改
                getRequest().setAttribute("worker", bmsUser);// 操作员用户
                getRequest().setAttribute("currentTime", DateUtil.DateToLong14Str(new Date()));
                getRequest().setAttribute("startTime", DateUtil.DateToShortStr(new Date()));
                String cardActiveTimeStr = RoleUtil.selectRuleByPrimaryKey(RoleUtil.CARD_ACTIVE_TIME);
                int cardActiveTime = cardActiveTimeStr == null ? 3 : NumberUtils.toInt(cardActiveTimeStr);
                getRequest().setAttribute("endTime", DateUtil.DateToShortStr(DateUtil.getADDYear(new Date(), cardActiveTime)));
            } else {
                return MEMBER;
            }
        } else {
            return MEMBER;
        }
        return SUCCESS;
    }

    /**
     * 开卡
     * 
     * @return
     */
    public String doRegisterCard() {
        String cardObjStr = getParameter("cardObjStr");
        map = checkCardInfo(cardObjStr);
        if ("success".equals(map.get("msg"))) {
            BusChargeCard chargeCard = (BusChargeCard) map.get("cardInfoObj");
            chargeCard.setUpdateTime(new Date());
            int newCardId = userService.insertChargeCardSelective(chargeCard);
            // 添加充电卡操作日志
            LogUtil.recordOperationLog(NumberUtils.toInt(getSessionBmsUserId()), LogTypeEnum.RUN_RECORD, LogOperatorEnum.ADD, getRemoteIP(), "添加充电卡", "[" + chargeCard.getCardNo()
                                                                                                                                                      + "]");
            map.put("newCardId", newCardId);
        }
        return SUCCESS;
    }

    private Map<String, Object> checkCardInfo(String cardInfoStr) {
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = "success";
        BusChargeCard cardInfoObj = null;
        try {
            cardInfoObj = JsonToBean(cardInfoStr, BusChargeCard.class);
            Date startTime = cardInfoObj.getStartTime();
            Date endTime = cardInfoObj.getEndTime();
            if (cardInfoObj.getUserId() == null || cardInfoObj.getUserId() <= 0) {
                msg = "非法操作！！";
            } else if (StringUtil.isEmpty(cardInfoObj.getBusNo())) {
                msg = "发卡运营商编号不能为空！！";
            } else if (StringUtil.isEmpty(cardInfoObj.getCardNo())) {
                msg = "充电卡卡号不能为空！！";
            } else if (cardInfoObj.getApplicationType() <= 0) {
                msg = "请选择应用类型标识！！";
            } else if (cardInfoObj.getCardType() <= 0) {
                msg = "请选择卡类型标识！！";
            } else if (StringUtil.isEmpty(cardInfoObj.getPassword())) {
                msg = "充电卡密码不能为空！！";
            } else if (startTime == null) {
                msg = "请选择充点卡启用日期！！";
            } else if (cardInfoObj.getWorker() <= 0) {
                msg = "职工标识不能为空！！";
            } else if (endTime == null) {
                msg = "请选择充点卡有效日期！！";
            } else if (startTime.getTime() >= endTime.getTime()) {
                msg = "充电卡有效日期必须大于启用日期！！";
            }
            if ("success".equals(msg)) {
                // 卡数量上限判断
                // BusChargeCardExample cardEmp = new BusChargeCardExample();
                // BusChargeCardExample.Criteria cardCr = cardEmp.createCriteria();
                // cardCr.andUserIdEqualTo(cardInfoObj.getUserId());
                // int countCard = userService.countChargeCardByExample(cardEmp);
                // int maxChargeCard =
                // NumberUtils.toInt(RoleUtil.selectRuleByPrimaryKey(RoleUtil.MAX_CHARGECARD_LIMIT));
                // maxChargeCard = maxChargeCard > 0 ? maxChargeCard : Globals.MAX_CHAEGECARD_LIMIT;
                // 卡号重复判断
                BusChargeCardExample cardEmp2 = new BusChargeCardExample();
                BusChargeCardExample.Criteria cardCr2 = cardEmp2.createCriteria();
                cardCr2.andCardNoEqualTo(cardInfoObj.getCardNo());
                int countCard2 = userService.countChargeCardByExample(cardEmp2);

                // if (countCard >= maxChargeCard) {
                // msg = "添加充电卡数量已达上限！！";
                // } else
                if (countCard2 > 0) {
                    msg = "充电卡[" + cardInfoObj.getCardNo() + "]已经开卡，请更换新的充电卡！！";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "开卡失败！！";
        }
        if ("success".equals(msg)) {
            map.put("cardInfoObj", cardInfoObj);
        }
        map.put("msg", msg);
        return map;
    }

    /**
     * 用户充电卡列表
     * 
     * @return
     */
    public String showCardList() {
        int infoId = getParamInt("infoId");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String msg = "success";
        if (infoId > 0) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("infoId", infoId);
            param.put("userType", UserTypeEnum.PERSON.getShortValue());
            List<BusUser> list = userService.selectBusUserByMap(param);
            if (list != null && list.size() > 0) {
                BusUser user = list.get(0);
                BusChargeCardExample cardEmp = new BusChargeCardExample();
                BusChargeCardExample.Criteria cardCr = cardEmp.createCriteria();
                cardCr.andUserIdEqualTo(user.getId());
                List<BusChargeCard> cardList = userService.selectChargeCardByExample(cardEmp);
                resultMap.put("cardList", cardList);
            } else {
                msg = "请选择用户！！";
            }
        } else {
            msg = "请选择用户！！";
        }
        resultMap.put("msg", msg);
        map = resultMap;
        return SUCCESS;
    }

    // 检查车辆信息和用户信息
    private Map<String, Object> checkCarAndCard(String userObjStr) {
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = "success";
        try {
            BusUserInfo userInfo = JsonToBean(userObjStr, BusUserInfo.class);
            BusUser user = JsonToBean(userObjStr, BusUser.class);
            if (!Validator.isChinese(userInfo.getRealName())) {
                msg = "请输入正确的姓名！！";
            } else if (!Validator.isMobile(user.getPhone())) {
                msg = "请输入正确的11位手机号码！！";
            } else if (userInfo.getProvince() <= 0) {
                msg = "请选择省份！！";
            } else if (userInfo.getCity() <= 0) {
                msg = "请选择市区！！";
            } else if (StringUtil.isNotEmpty(user.getEmail()) && !Validator.isEmail(user.getEmail())) {
                msg = "请输入正确的邮箱！！";
            } else if (StringUtil.isNotEmpty(userInfo.getCardNo()) && !Validator.isIDCard(userInfo.getCardNo())) {
                msg = "请输入正确的身份证号码！！";
            } else if (StringUtil.isEmpty(userInfo.getPlateNo())) {
                msg = "请输入车牌号！！";
            } else if (userInfo.getBrand() <= 0) {
                msg = "请选择车品牌！！";
            } else if (userInfo.getModel() <= 0) {
                msg = "请选择车型号！！";
            } else if (StringUtil.isEmpty(userInfo.getVin())) {
                msg = "请输入车架号！！";
            }
            if ("success".equals(msg)) {
                map.put("userObj", user);
                map.put("userInfoObj", userInfo);
            }
        } catch (Exception e) {
            msg = "开户失败！！";
        }
        map.put("msg", msg);
        return map;
    }

    // set get

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Page getPage() {
        return page;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public List<SysCarBrand> getModelList() {
        return modelList;
    }

    public void setChargingService(ChargingService chargingService) {
        this.chargingService = chargingService;
    }
}
