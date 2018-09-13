package com.holley.charging.app.user.action;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.holley.charging.action.BaseAction;
import com.holley.charging.app.util.AppTool;
import com.holley.charging.common.util.CacheChargeHolder;
import com.holley.charging.model.app.AppointmentInfo;
import com.holley.charging.model.app.BillInfo;
import com.holley.charging.model.app.CashFreeze;
import com.holley.charging.model.app.ChargePayInfo;
import com.holley.charging.model.app.ChargeRecordDetail;
import com.holley.charging.model.app.ChargeRecordIntro;
import com.holley.charging.model.app.PileDetailInfo;
import com.holley.charging.model.app.RongCloudUser;
import com.holley.charging.model.app.StationIntro;
import com.holley.charging.model.app.TradeRecord;
import com.holley.charging.model.bus.BusAccount;
import com.holley.charging.model.bus.BusAppointment;
import com.holley.charging.model.bus.BusAppointmentExample;
import com.holley.charging.model.bus.BusCash;
import com.holley.charging.model.bus.BusFavorites;
import com.holley.charging.model.bus.BusFavoritesExample;
import com.holley.charging.model.bus.BusGroupInfo;
import com.holley.charging.model.bus.BusPayment;
import com.holley.charging.model.bus.BusPaymentExample;
import com.holley.charging.model.bus.BusPileApply;
import com.holley.charging.model.bus.BusPileComment;
import com.holley.charging.model.bus.BusPileCommentExample;
import com.holley.charging.model.bus.BusPileModel;
import com.holley.charging.model.bus.BusSuggestion;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.model.bus.BusUserExample;
import com.holley.charging.model.bus.BusUserInfo;
import com.holley.charging.model.bus.BusUserReal;
import com.holley.charging.model.bus.BusUserRealExample;
import com.holley.charging.model.def.ChargeRuleModel;
import com.holley.charging.model.def.PileRunStatusModel;
import com.holley.charging.model.pob.PobChargingPile;
import com.holley.charging.model.pob.PobChargingStation;
import com.holley.charging.service.bussiness.AccountService;
import com.holley.charging.service.bussiness.AppointmentService;
import com.holley.charging.service.bussiness.ChargingService;
import com.holley.charging.service.bussiness.DeviceService;
import com.holley.charging.service.website.PobObjectService;
import com.holley.charging.service.website.UserService;
import com.holley.common.cache.charging.CacheKeyProvide.KeyMsgCodeTypeEnum;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.BillMarkTypeEnum;
import com.holley.common.constants.Globals;
import com.holley.common.constants.LogOperatorEnum;
import com.holley.common.constants.ResultBean;
import com.holley.common.constants.RoleTypeEnum;
import com.holley.common.constants.app.ErrorCodeConstants;
import com.holley.common.constants.charge.AccountStatusEnum;
import com.holley.common.constants.charge.AppointmentPayStatusEnum;
import com.holley.common.constants.charge.AppointmentStatusEnum;
import com.holley.common.constants.charge.CashStatusEnum;
import com.holley.common.constants.charge.CashVerifyStatusEnum;
import com.holley.common.constants.charge.CashWayEnum;
import com.holley.common.constants.charge.ChargeCurrentTypeEnum;
import com.holley.common.constants.charge.ChargeDealStatusEnum;
import com.holley.common.constants.charge.ChargeModeEnum;
import com.holley.common.constants.charge.ChargePayStatusEnum;
import com.holley.common.constants.charge.ChargePowerTypeEnum;
import com.holley.common.constants.charge.FundOperateTypeEnum;
import com.holley.common.constants.charge.ImgTypeEnum;
import com.holley.common.constants.charge.LinkTypeEnum;
import com.holley.common.constants.charge.PileApplyValidStatusEnum;
import com.holley.common.constants.charge.PileStatusEnum;
import com.holley.common.constants.charge.RealVerifyStatusEnum;
import com.holley.common.constants.charge.SuggestionStatusEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.dataobject.PileStatusBean;
import com.holley.common.util.DateUtil;
import com.holley.common.util.NumberUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.model.sys.SysDefArea;
import com.holley.platform.util.CacheSysHolder;
import com.holley.platform.util.LogUtil;
import com.holley.platform.util.RoleUtil;
import com.holley.web.common.util.ImageUtil;
import com.holley.web.common.util.Validator;

/**
 * 个人信息管理
 *
 * @author zhouli
 */
public class UserInfoAction extends BaseAction {

    private static final long  serialVersionUID = 1L;
    private UserService        userService;
    private AccountService     accountService;
    private AppointmentService appointmentService;
    private ChargingService    chargingService;
    private PobObjectService   pobObjectService;
    private DeviceService      deviceService;

    private String             userkey;
    private ResultBean         resultBean       = new ResultBean();

    private File               headimg;                            // 头像
    private File               front;                              // 身份证正面
    private File               pic;
    private List<File>         piclist;                            // 多图

    /**
     * 我的钱包：查询当前账户的余额和积分情况
     *
     * @return
     */
    public String account() {
        userkey = decrypt(this.getAttribute("userkey"));
        if (!NumberUtils.isNumber(userkey)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        Integer userId = Integer.valueOf(userkey);
        BusUser busUser = userService.selectUserByCache(userId);
        // 增加用户类型判断如果是集团子账户加载集团账户
        if (busUser.getGroupId() != null && busUser.getGroupId() > 0) {
            userId = busUser.getGroupId();
        }
        BusAccount account = accountService.selectAccoutByPrimaryKey(userId);// 如果集团子账户加载集团账户

        // 查询收入总额（已结算的充电、预约费用）
        if (account != null) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userid", userkey);
            BigDecimal totalin = accountService.selectUserBillTotalIn(params);
            account.setTotalin(totalin);
            account.setUserId(null);
            account.setStatus(null);
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("account", account);
        resultBean.setData(data);
        return SUCCESS;
    }

    /**
     * 收入明细(充电、预约)
     * 
     * @return
     */
    public String indetail() {
        userkey = decrypt(getAttribute("userkey"));
        String datatime = getAttribute("datatime");// 2016-05
        String pageindex = getAttribute("pageindex");

        if (!NumberUtils.isNumber(userkey) || AppTool.isNull(datatime, pageindex)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        Date startdate = DateUtil.ShortStrToDate(datatime + "-01");
        Date enddate = DateUtil.addMonths(startdate, 1);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userid", userkey);
        params.put("startdate", startdate);
        params.put("enddate", enddate);
        Page page = this.returnPage(Integer.valueOf(pageindex), limit);
        params.put(Globals.PAGE, page);
        List<TradeRecord> tradelist = accountService.selectBillDetailByParamByPage(params);
        if (tradelist == null || tradelist.size() == 0) {
            return SUCCESS;
        }
        // 设置描述
        for (TradeRecord record : tradelist) {
            record.setName(CacheChargeHolder.getStationPileNameById(record.getPileid()));
            if (record.getType() != null) {
                record.setTypedesc(BillMarkTypeEnum.getText(record.getType().intValue()));
            }
            record.setFee(NumberUtil.formateScale2(record.getFee()));
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("tradelist", tradelist);
        data.put("totoalcount", page.getTotalProperty());
        resultBean.setData(data);
        return SUCCESS;
    }

    /**
     * 提现申请
     * 
     * @return
     */
    public String cash() {
        userkey = decrypt(this.getAttribute("userkey"));
        String cashway = this.getAttribute("payway");// 1:支付宝，3：微信
        String accountinfo = this.getAttribute("accountinfo");
        String operatemoney = this.getAttribute("operatemoney");
        String validatecode = this.getAttribute("validatecode");

        if (AppTool.isNotNumber(userkey, cashway, operatemoney) || AppTool.isNull(accountinfo)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        if (StringUtil.isEmpty(validatecode)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("请输入短信验证码!");
            return SUCCESS;
        }

        String realpcode = ChargingCacheUtil.getMessageValidateCode(getSessionID(), KeyMsgCodeTypeEnum.CASH);
        if (realpcode == null || !realpcode.equals(validatecode)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_CASH_VALIDATEWORING);
            resultBean.setMessage("短信验证码不正确!");
            return SUCCESS;
        }

        CashWayEnum cashWayEnum = CashWayEnum.getEnmuByValue(Integer.valueOf(cashway));
        if (cashWayEnum == null) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_CASH_NOWAY);
            resultBean.setMessage("提现方式不支持!");
            return SUCCESS;
        }

        // 查看用户是否有欠费(预约费用和充电费用)
        if (!queryUnpaidFee(userkey)) {
            return SUCCESS;
        }

        // 查看当前账户可用余额
        BusAccount busAccount = accountService.selectAccoutByPrimaryKey(Integer.valueOf(userkey));
        if (busAccount.getStatus() != null && AccountStatusEnum.BLOCKED.getShortValue().equals(busAccount.getStatus())) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_CASH_BLOCKED);
            resultBean.setMessage("账户已被冻结!");
            return SUCCESS;
        }

        BigDecimal usableMoney = busAccount.getUsableMoney();
        BigDecimal operateMoney = new BigDecimal(operatemoney);
        if (usableMoney == null || usableMoney.doubleValue() < operateMoney.doubleValue()) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_CASH_LACK);
            resultBean.setMessage("可用余额不足!");
            return SUCCESS;
        }
        BusCash cash = new BusCash();
        cash.setUserId(Integer.valueOf(userkey));
        cash.setMoney(operateMoney);
        cash.setAddTime(new Date());
        cash.setValidStatus(CashVerifyStatusEnum.VERIFYING.getShortValue());
        cash.setCashWay(cashWayEnum.getShortValue());
        cash.setAccountInfo(accountinfo);
        accountService.insertBusCashAndUpdateBusAccount(cash, busAccount);
        return SUCCESS;
    }

    /**
     * 用户是否存在未缴费记录
     *
     * @param userid
     * @return
     */
    private boolean queryUnpaidFee(String userid) {
        // 查看是否有未缴费的预约记录
        BusAppointmentExample aemp = new BusAppointmentExample();
        BusAppointmentExample.Criteria acr = aemp.createCriteria();
        acr.andUserIdEqualTo(Integer.valueOf(userid));
        acr.andPayStatusNotEqualTo(AppointmentPayStatusEnum.SUCCESS.getShortValue());
        acr.andAppStatusNotEqualTo(AppointmentStatusEnum.DEL.getShortValue());
        List<BusAppointment> appList = appointmentService.selectAppointmentByExample(aemp);
        Map<String, Object> data = new HashMap<String, Object>();
        if (appList != null && appList.size() > 0) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_UNPAIDFEE_APPO);// 20004: 用户有未缴清的预约费用
            resultBean.setMessage("用户有未缴清的预约费用!");
            BusAppointment record = appList.get(0);

            AppointmentInfo info = new AppointmentInfo();
            info.setAddtime(record.getAddTime());
            info.setAppfee(record.getAppFee());
            info.setApplen(record.getAppLen());
            // info.setAppstatus(record.getAppStatus());
            info.setEndtime(record.getEndTime());
            info.setId(record.getId());
            // info.setPaystatus(record.getPayStatus());
            info.setPileid(record.getPileId());
            info.setStarttime(record.getStartTime());

            PobChargingPile pile = CacheChargeHolder.getChargePileById(record.getPileId());
            if (pile != null) {
                info.setPilename(pile.getPileName());
                if (pile.getPileType() != null) {
                    info.setPiletypedesc(ChargePowerTypeEnum.getText(pile.getPileType().intValue()));
                }

                PobChargingStation station = CacheChargeHolder.getChargeStationById(pile.getStationId());
                if (station != null) {
                    info.setStationname(station.getStationName());
                }

                BusPileModel model = CacheChargeHolder.selectPileModelById(pile.getPileModel());
                if (model != null) {
                    info.setOutv(model.getOutV() == null ? "" : model.getOutV());
                }
            }
            data.put("appointment", info);
            resultBean.setData(data);
            return false;
        }
        // 查看是否有未缴费的充电记录
        BusPaymentExample pemp = new BusPaymentExample();
        BusPaymentExample.Criteria pcr = pemp.createCriteria();
        pcr.andUserIdEqualTo(Integer.valueOf(userid));
        pcr.andDealStatusEqualTo(ChargeDealStatusEnum.SUCCESS.getShortValue());
        pcr.andPayStatusNotEqualTo(ChargePayStatusEnum.SUCCESS.getShortValue());
        // pcr.andDealStatusNotEqualTo(ChargeDealStatusEnum.DEL.getShortValue());
        List<BusPayment> chargeList = chargingService.selectChargePaymentByExample(pemp);
        if (chargeList != null && chargeList.size() > 0) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_UNPAIDFEE_CHARGE);// 20005: 用户有未缴清的充电费用
            resultBean.setMessage("用户有未缴清的充电费用!");

            BusPayment record = chargeList.get(0);
            ChargePayInfo info = new ChargePayInfo();
            info.setChafee(record.getChaFee());
            info.setChalen(record.getChaLen());
            info.setChapower(record.getChaPower());
            info.setChargemodedesc(ChargeModeEnum.getDefaultText(record.getChaMode()));
            info.setId(record.getId());
            info.setParkfee(record.getParkFee());
            info.setPileid(record.getPileId());
            info.setServicefee(record.getServiceFee());
            info.setShouldmoney(record.getShouldMoney());
            info.setStarttime(record.getStartTime());

            data.put("payment", info);
            resultBean.setData(data);
            return false;
        }
        return true;
    }

    /**
     * 我的收藏
     *
     * @return
     */
    public String favorite() {
        userkey = decrypt(this.getAttribute("userkey"));
        String lng = this.getAttribute("lng");
        String lat = this.getAttribute("lat");
        String pageindex = getAttribute("pageindex");
        if (AppTool.isNotNumber(userkey, lng, lat)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004： 用户输入参数非法
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        Page page = this.returnPage(Integer.valueOf(pageindex), limit);
        params.put("userid", Integer.valueOf(userkey));
        params.put(Globals.PAGE, page);
        List<BusFavorites> favoritelist = accountService.selectUserFavoritesByPage(params);
        if (favoritelist == null || favoritelist.size() == 0) {
            return SUCCESS;
        }
        List<StationIntro> stationList = new ArrayList<StationIntro>();
        StationIntro station;
        PobChargingStation record;
        // 设置充电点的空闲桩数，交直流桩数
        Map<String, Integer> numMap;
        double lng1;
        double lat1;
        double lng2 = StringUtil.parseDouble(lng);
        double lat2 = StringUtil.parseDouble(lat);
        for (BusFavorites fa : favoritelist) {
            record = CacheChargeHolder.getChargeStationById(fa.getStationId());
            if (record == null) {
                continue;
            }
            lng1 = StringUtil.parseDouble(record.getLng());
            lat1 = StringUtil.parseDouble(record.getLat());
            station = new StationIntro();
            station.setStationid(record.getId());
            station.setStationname(record.getStationName());
            station.setLng(record.getLng());
            station.setLat(record.getLat());
            station.setAddress(record.getAddress());
            station.setImg(record.getImg());
            station.setLinkphone(record.getLinkPhone());
            station.setFastnum(record.getFastNum() == null ? 0 : record.getFastNum());
            station.setSlownum(record.getSlowNum() == null ? 0 : record.getSlowNum());
            station.setDistance(AppTool.getPointDistance(lng1, lat1, lng2, lat2));
            if (record.getOpenDay() != null) {
                station.setOpendaydesc(CacheSysHolder.getSysLinkName(LinkTypeEnum.OPEN_DAY.getValue(), record.getOpenDay().toString()));
            }
            if (record.getOpenTime() != null) {
                station.setOpentimedesc(CacheSysHolder.getSysLinkName(LinkTypeEnum.OPEN_TIME.getValue(), record.getOpenTime().toString()));
            }
            station.setScore(record.getScore());
            numMap = CacheChargeHolder.getPileNumByStationid(record.getId());
            station.setAcnum(numMap.get("acnum"));
            station.setDcnum(numMap.get("dcnum"));
            station.setIdlenum(numMap.get("idlenum"));
            station.setFavoriteid(fa.getId());
            stationList.add(station);
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("favoritelist", stationList);
        data.put("imgurl", RoleUtil.selectRuleByPrimaryKey(RoleUtil.IMG_URL));
        resultBean.setData(data);
        return SUCCESS;
    }

    /**
     * 添加充电点收藏
     * 
     * @return
     */
    public String favoriteadd() {
        userkey = decrypt(this.getAttribute("userkey"));
        String stationid = this.getAttribute("stationid");
        if (AppTool.isNotNumber(userkey, stationid)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        // 判断该充电点是否已收藏
        BusFavoritesExample emp = new BusFavoritesExample();
        BusFavoritesExample.Criteria cr = emp.createCriteria();
        cr.andUserIdEqualTo(Integer.valueOf(userkey));
        cr.andStationIdEqualTo(Integer.valueOf(stationid));
        List<BusFavorites> list = accountService.selectFavoritesByExample(emp);
        Map<String, Object> data = new HashMap<String, Object>();
        if (list == null || list.size() == 0) {
            BusFavorites record = new BusFavorites();
            record.setUserId(Integer.valueOf(userkey));
            record.setStationId(Integer.valueOf(stationid));
            record.setIsShow(WhetherEnum.YES.getShortValue());
            record.setAddTime(new Date());

            if (accountService.insertFavorites(record) > 0) {
                data.put("favoriteid", record.getId());
                resultBean.setData(data);
            } else {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);
                resultBean.setMessage("充电点收藏失败!");
            }
        } else {
            data.put("favoriteid", list.get(0).getId());
            resultBean.setData(data);
        }
        return SUCCESS;
    }

    /**
     * 我的收藏删除
     *
     * @return
     */
    public String favoritedel() {
        userkey = decrypt(this.getAttribute("userkey"));
        String favoriteid = this.getAttribute("favoriteid");
        if (AppTool.isNotNumber(userkey, favoriteid)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        if (accountService.deleteFavoritesByPK(Integer.valueOf(favoriteid)) <= 0) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);
            resultBean.setMessage("收藏删除失败!");
            return SUCCESS;
        }
        return SUCCESS;
    }

    /**
     * 账单查询 通过钱包账户产生的账单：充值、充电支出、预约支出、提现
     * 
     * @return
     */
    public String bill() {
        userkey = decrypt(this.getAttribute("userkey"));
        String pageindex = this.getAttribute("pageindex");
        String type = this.getAttribute("type");
        String datatime = this.getAttribute("datatime");
        if (!NumberUtils.isNumber(userkey) || AppTool.isNull(pageindex, datatime)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        Date starttime = DateUtil.ShortStrToDate(datatime + "-01");
        Date endtime = DateUtil.addMonths(starttime, 1);

        Page page = this.returnPage(Integer.valueOf(pageindex), limit);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userid", Integer.valueOf(userkey));
        params.put("startdate", starttime);
        params.put("enddate", endtime);
        params.put(Globals.PAGE, page);
        if (NumberUtils.isNumber(type)) {
            params.put("type", Short.valueOf(type));
        }
        List<BillInfo> billlist = accountService.selectAccountBillByPage(params);
        if (billlist == null || billlist.size() == 0) {
            return SUCCESS;
        }
        int operatetype;
        for (BillInfo record : billlist) {
            operatetype = record.getType().intValue();
            record.setTypedesc(FundOperateTypeEnum.getText(operatetype));
            if (FundOperateTypeEnum.RECHARGE.getValue() == operatetype) {// 充值
                record.setName("钱包充值");
            } else if (FundOperateTypeEnum.CHARGING.getValue() == operatetype || FundOperateTypeEnum.APPOINTMENT.getValue() == operatetype) {// 充电
                record.setName(CacheChargeHolder.getStationPileNameById(record.getId()));
            } else if (FundOperateTypeEnum.CASH.getValue() == operatetype) {// 体现
                record.setName("钱包提现");
            }
            record.setId(null);// 里面有userid，所以要清除，以免暴露
            record.setFee(NumberUtil.formateScale2(record.getFee()));
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("billlist", billlist);
        data.put("totoalcount", page.getTotalProperty());
        resultBean.setData(data);
        return SUCCESS;
    }

    /**
     * 预约管理
     *
     * @return
     */
    public String appointment() {
        userkey = decrypt(this.getAttribute("userkey"));
        String type = this.getAttribute("type");// 1:预约中，2：已完成并且已支付，3：未支付
        String pageindex = this.getAttribute("pageindex");

        if (AppTool.isNotNumber(userkey, type) || (!"1".equals(type) && AppTool.isNotNumber(pageindex))) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        Map<String, Object> data = new HashMap<String, Object>();
        if ("1".equals(type)) {// 预约中
            BusAppointmentExample emp = new BusAppointmentExample();
            BusAppointmentExample.Criteria cr = emp.createCriteria();
            cr.andUserIdEqualTo(Integer.valueOf(userkey));
            emp.setOrderByClause("START_TIME desc");
            cr.andAppStatusEqualTo(AppointmentStatusEnum.ORDERING.getShortValue());
            cr.andEndTimeGreaterThan(new Date());
            List<BusAppointment> list = appointmentService.selectAppointmentByExample(emp);
            if (list == null || list.size() == 0) {
                return SUCCESS;
            }
            AppointmentInfo info = getAppInfoListFrom(list).get(0);
            data.put("appointment", info);
            data.put("currenttime", System.currentTimeMillis());
            resultBean.setData(data);
        } else if ("2".equals(type)) {// 已完成并且已支付
            Page page = this.returnPage(Integer.valueOf(pageindex), limit);
            BusAppointmentExample emp = new BusAppointmentExample();
            BusAppointmentExample.Criteria cr = emp.createCriteria();
            emp.setOrderByClause("START_TIME desc");
            emp.setPage(page);
            cr.andUserIdEqualTo(Integer.valueOf(userkey));
            cr.andEndTimeLessThan(new Date());
            cr.andPayStatusEqualTo(AppointmentPayStatusEnum.SUCCESS.getShortValue());
            cr.andAppStatusNotEqualTo(AppointmentStatusEnum.DEL.getShortValue());

            List<Short> statusList = new ArrayList<Short>();
            statusList.add(AppointmentStatusEnum.CANCEL.getShortValue());
            statusList.add(AppointmentStatusEnum.RENEW.getShortValue());
            BusAppointmentExample.Criteria cr2 = emp.or();
            cr2.andUserIdEqualTo(Integer.valueOf(userkey));
            cr2.andPayStatusEqualTo(AppointmentPayStatusEnum.SUCCESS.getShortValue());
            cr2.andAppStatusIn(statusList);
            List<BusAppointment> list = appointmentService.selectAppointmentByExampleByPage(emp);

            if (list == null || list.size() == 0) {
                return SUCCESS;
            }
            List<AppointmentInfo> appointmentlist = getAppInfoListFrom(list);
            data.put("appointmentlist", appointmentlist);
            data.put("totoalcount", page.getTotalProperty());
            resultBean.setData(data);
        } else if ("3".equals(type)) {
            Page page = this.returnPage(Integer.valueOf(pageindex), limit);
            BusAppointmentExample emp = new BusAppointmentExample();
            BusAppointmentExample.Criteria cr = emp.createCriteria();
            emp.setOrderByClause("START_TIME desc");
            emp.setPage(page);
            cr.andUserIdEqualTo(Integer.valueOf(userkey));
            cr.andPayStatusNotEqualTo(AppointmentPayStatusEnum.SUCCESS.getShortValue());
            cr.andAppStatusNotEqualTo(AppointmentStatusEnum.DEL.getShortValue());
            List<BusAppointment> list = appointmentService.selectAppointmentByExampleByPage(emp);
            if (list == null || list.size() == 0) {
                return SUCCESS;
            }
            List<AppointmentInfo> appointmentlist = getAppInfoListFrom(list);
            data.put("appointmentlist", appointmentlist);
            data.put("totoalcount", page.getTotalProperty());
            resultBean.setData(data);
        }
        return SUCCESS;
    }

    private List<AppointmentInfo> getAppInfoListFrom(List<BusAppointment> list) {
        if (list == null || list.size() == 0) return null;
        List<AppointmentInfo> infoList = new ArrayList<AppointmentInfo>();
        AppointmentInfo info;
        for (BusAppointment record : list) {
            info = new AppointmentInfo();
            info.setId(record.getId());
            info.setPileid(record.getPileId());
            info.setAppfee(NumberUtil.formateScale2(record.getAppFee()));
            info.setApplen(record.getAppLen());
            info.setEndtime(record.getEndTime());
            info.setPaystatus(record.getPayStatus());
            info.setStarttime(record.getStartTime());
            info.setAppno(record.getAppNo());
            info.setAddtime(record.getAddTime());
            // 处理预约状态
            if (record.getAppStatus() != null) {
                AppointmentStatusEnum statusenum = AppointmentStatusEnum.getEnmuByValue(record.getAppStatus().intValue());
                if (statusenum == null) {
                    info.setAppstatusdesc("未知");
                } else {
                    if (AppointmentStatusEnum.ORDERING == statusenum && new Date().compareTo(record.getEndTime()) >= 0) {
                        info.setAppstatusdesc(AppointmentStatusEnum.OVERDUE.getText());
                    } else {
                        info.setAppstatusdesc(statusenum.getText());
                    }
                }
            }
            setRecordPileInfo(info);
            infoList.add(info);
        }
        return infoList;
    }

    /**
     * 设置预约记录里充电桩的相关信息
     * 
     * @param info
     */
    private void setRecordPileInfo(AppointmentInfo info) {
        PobChargingPile pile = CacheChargeHolder.getChargePileById(info.getPileid());
        if (pile != null) {
            info.setPilename(pile.getPileName());
            info.setPiletype(pile.getPileType());

            PobChargingStation station = CacheChargeHolder.getChargeStationById(pile.getStationId());
            if (station != null) {
                info.setAddress(station.getAddress());
                info.setStationname(station.getStationName());
                info.setLat(station.getLat());
                info.setLng(station.getLng());
            }

            BusPileModel model = CacheChargeHolder.selectPileModelById(pile.getPileModel());
            if (model != null) {
                info.setOutv(model.getOutV() == null ? "" : model.getOutV());
                info.setRatp(model.getRatP() == null ? "" : model.getRatP());
            }
        }
    }

    /**
     * 预约删除
     *
     * @return
     */
    public String appointmentdel() {
        userkey = decrypt(this.getAttribute("userkey"));
        String appointmentid = this.getAttribute("appointmentid");

        if (AppTool.isNotNumber(userkey, appointmentid)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        Integer id = Integer.valueOf(appointmentid);

        BusAppointment item = appointmentService.selectAppointmentByPK(id);
        if (item == null) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_QRY_NORECORD);
            resultBean.setMessage("预约记录不存在!");
            return SUCCESS;
        }
        if (!AppointmentPayStatusEnum.SUCCESS.getShortValue().equals(item.getPayStatus())) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_UNPAIDFEE_APPO);
            resultBean.setMessage("未支付的预约记录不能删除!");
            return SUCCESS;
        }

        BusAppointment record = new BusAppointment();
        record.setId(id);
        record.setAppStatus(AppointmentStatusEnum.DEL.getShortValue());
        if (appointmentService.updateAppointmentByPKSelective(record) <= 0) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);
            resultBean.setMessage("预约记录删除失败!");
            return SUCCESS;
        } else {
            LogUtil.recordDocumentlog(Integer.valueOf(userkey), LogOperatorEnum.MODIFY, null, "删除预约记录", "{id:" + id + ",appStatus:" + record.getAppStatus() + "}", true);
        }
        return SUCCESS;
    }

    /**
     * 我的充电
     *
     * @return
     */
    public String chargerecord() {
        userkey = decrypt(this.getAttribute("userkey"));
        String type = this.getAttribute("type");// 1:充电中，2：已完成，3：未完成（包括未支付或未上报充电记录）
        String pageindex = this.getAttribute("pageindex");

        if (AppTool.isNotNumber(userkey, type) || (!"1".equals(type) && !NumberUtils.isNumber(pageindex))) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法");
            return SUCCESS;
        }

        Map<String, Object> data = new HashMap<String, Object>();
        if ("1".equals(type)) {// 充电中
            BusPaymentExample emp = new BusPaymentExample();
            BusPaymentExample.Criteria cr = emp.createCriteria();
            cr.andUserIdEqualTo(Integer.valueOf(userkey));
            cr.andDealStatusEqualTo(ChargeDealStatusEnum.DEALING.getShortValue());
            cr.andEndTimeIsNull();
            emp.setOrderByClause("UPDATE_TIME DESC");
            List<BusPayment> list = chargingService.selectChargePaymentByExample(emp);
            if (list == null || list.size() == 0) {
                return SUCCESS;
            }
            PileStatusBean statusbean;
            for (BusPayment record : list) {
                statusbean = ChargingCacheUtil.getPileStatusBean(record.getPileId());
                if (statusbean == null) {
                    continue;
                }
                // || PileStatusEnum.FINISH == statusbean.getStatus()
                if ((PileStatusEnum.CHARGING == statusbean.getStatus()) && Integer.valueOf(userkey).equals(statusbean.getUserid())) {
                    AppTool.pileStatusValueFormat(statusbean);
                    data.put("statusbean", statusbean);
                    resultBean.setData(data);
                    return SUCCESS;
                }
            }
        } else if ("2".equals(type)) {// 已完成
            Page page = this.returnPage(Integer.valueOf(pageindex), limit);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userid", Integer.valueOf(userkey));
            params.put(Globals.PAGE, page);
            List<ChargeRecordIntro> list = chargingService.selectFinishedPaymentByPage(params);
            if (list == null || list.size() == 0) {
                return SUCCESS;
            }
            setChargeRecordPileInfo(list);
            data.put("chargerecordlist", list);
            data.put("totoalcount", page.getTotalProperty());
            resultBean.setData(data);
        } else if ("3".equals(type)) {// 未完成
            Page page = this.returnPage(Integer.valueOf(pageindex), limit);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userid", Integer.valueOf(userkey));
            params.put(Globals.PAGE, page);
            List<ChargeRecordIntro> list = chargingService.selectUnfinishedPaymentByPage(params);
            if (list == null || list.size() == 0) {
                return SUCCESS;
            }
            int total = page.getTotalProperty();
            // 判断未完成记录中是否有正在充电的记录
            PileStatusBean statusbean;
            ChargeRecordIntro charging = null;
            for (ChargeRecordIntro record : list) {
                if (record.getFee() == null) {
                    continue;
                }
                statusbean = ChargingCacheUtil.getPileStatusBean(record.getPileid());
                if (statusbean == null) {
                    continue;
                }
                // || PileStatusEnum.FINISH == statusbean.getStatus()
                if ((PileStatusEnum.CHARGING == statusbean.getStatus()) && Integer.valueOf(userkey).equals(statusbean.getUserid())) {
                    charging = record;
                    break;
                }
            }
            if (charging != null) {
                list.remove(charging);
                total = total - 1;
            }
            if (list == null || list.size() == 0) {
                return SUCCESS;
            }
            setChargeRecordPileInfo(list);
            data.put("chargerecordlist", list);
            data.put("totoalcount", total);
            resultBean.setData(data);
        }
        return SUCCESS;
    }

    private void setChargeRecordPileInfo(List<ChargeRecordIntro> list) {
        if (list == null || list.size() == 0) return;
        PobChargingPile pile;
        PobChargingStation station;
        for (ChargeRecordIntro record : list) {
            pile = CacheChargeHolder.getChargePileById(record.getPileid());
            String name = "";
            if (pile != null) {
                name = pile.getPileName();
                record.setPiletype(pile.getPileType());

                station = CacheChargeHolder.getChargeStationById(pile.getStationId());
                if (station != null) {
                    if (StringUtils.isNotEmpty(name)) {
                        name = station.getStationName() + name;
                    }
                }
                record.setPilename(name);
            }
        }
    }

    /**
     * 我的充电记录详情
     * 
     * @return
     */
    public String chargerecorddetail() {
        String chargerecordid = this.getAttribute("chargerecordid");
        if (!NumberUtils.isNumber(chargerecordid)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法");
            return SUCCESS;
        }
        BusPayment record = chargingService.selectPaymentByPK(Integer.valueOf(chargerecordid));
        PobChargingPile pile;
        PobChargingStation station;
        if (record != null) {
            ChargeRecordDetail detail = new ChargeRecordDetail();
            detail.setId(record.getId());
            detail.setPileid(record.getPileId());
            detail.setChalen(record.getChaLen());
            detail.setChapower(record.getChaPower());
            detail.setStarttime(record.getStartTime());
            detail.setEndtime(record.getEndTime());
            detail.setChafee(NumberUtil.formateScale2(record.getChaFee()));
            detail.setServicefee(NumberUtil.formateScale2(record.getServiceFee()));
            detail.setParkfee(NumberUtil.formateScale2(record.getParkFee()));
            detail.setShouldmoney(NumberUtil.formateScale2(record.getShouldMoney()));
            detail.setPaystatus(record.getPayStatus());
            detail.setChargemodedesc(ChargeModeEnum.getDefaultText(record.getChaMode()));
            detail.setTradeno(record.getTradeNo());

            pile = CacheChargeHolder.getChargePileById(record.getPileId());
            if (pile != null) {
                detail.setPilename(pile.getPileName());
                detail.setPiletype(pile.getPileType());

                station = CacheChargeHolder.getChargeStationById(pile.getStationId());
                if (station != null) {
                    detail.setStationname(station.getStationName());
                    if (station.getOpenDay() != null) {
                        detail.setOpenday(CacheSysHolder.getSysLinkName(LinkTypeEnum.OPEN_DAY.getValue(), station.getOpenDay().toString()));
                    }
                    if (station.getOpenTime() != null) {
                        detail.setOpentime(CacheSysHolder.getSysLinkName(LinkTypeEnum.OPEN_TIME.getValue(), station.getOpenTime().toString()));
                    }
                    detail.setAddress(station.getAddress());
                }
            }
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("chargerecorddetail", detail);
            resultBean.setData(data);
        }
        return SUCCESS;
    }

    /**
     * 查看充电记录评论
     * 
     * @return
     */
    public String chargerecordcomment() {
        userkey = decrypt(this.getAttribute("userkey"));
        String chargerecordid = this.getAttribute("chargerecordid");
        String commentid = this.getAttribute("commentid");
        if (AppTool.isNotNumber(userkey, chargerecordid, commentid)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法");
            return SUCCESS;
        }
        BusPileCommentExample emp = new BusPileCommentExample();
        BusPileCommentExample.Criteria cr = emp.createCriteria();
        cr.andIdEqualTo(Integer.valueOf(commentid));
        cr.andUserIdEqualTo(Integer.valueOf(userkey));
        cr.andChargerecordIdEqualTo(Integer.valueOf(chargerecordid));

        List<BusPileComment> list = pobObjectService.selectPileCommentByExample(emp);
        // BusPileComment record = pobObjectService.selectPileCommentByPK(Integer.valueOf(commentid));
        if (list != null && list.size() > 0) {
            Map<String, Object> data = new HashMap<String, Object>();
            BusPileComment record = list.get(0);

            BusUser user = userService.selectUserByCache(Integer.valueOf(userkey));
            if (user != null) {
                record.setUsername(user.getUsername());
                record.setHeadImg(user.getHeadImg());
            }

            record.setUserId(null);
            data.put("comment", record);
            data.put("imgurl", RoleUtil.selectRuleByPrimaryKey(RoleUtil.IMG_URL));
            resultBean.setData(data);
        }
        return SUCCESS;
    }

    /**
     * 充电记录删除
     *
     * @return
     */
    public String chargerecorddel() {
        userkey = decrypt(this.getAttribute("userkey"));
        String chargerecordid = this.getAttribute("chargerecordid");
        if (AppTool.isNotNumber(userkey, chargerecordid)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        Integer id = Integer.valueOf(chargerecordid);
        BusPayment item = chargingService.selectPaymentByPK(id);
        if (item == null) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_QRY_NORECORD);
            resultBean.setMessage("充电记录不存在!");
            return SUCCESS;
        }

        if (!ChargePayStatusEnum.SUCCESS.getShortValue().equals(item.getPayStatus())) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_UNPAIDFEE_CHARGE);
            resultBean.setMessage("未支付的充电记录不能删除!");
        }

        BusPayment record = new BusPayment();
        record.setId(Integer.valueOf(chargerecordid));
        record.setDealStatus(ChargeDealStatusEnum.DEL.getShortValue());
        if (chargingService.updatePaymentByPKSelective(record) <= 0) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);
            resultBean.setMessage("充电记录删除失败!");
        } else {
            LogUtil.recordDocumentlog(Integer.valueOf(userkey), LogOperatorEnum.MODIFY, null, "删除充电缴费记录", "{id:" + id + ",dealStatus:" + record.getDealStatus() + "}", true);
        }
        return SUCCESS;
    }

    /**
     * 我的信息
     *
     * @return
     */
    public String userinfo() {
    	userkey = decrypt(this.getAttribute("userkey"));
		userkey = "150";
		if (!NumberUtils.isNumber(userkey)) {
			resultBean.setSuccess(false);
			resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
			resultBean.setMessage("用户输入参数非法!");
			return SUCCESS;
		}
		BusUser user = userService.selectUserByCache(Integer.valueOf(userkey));
		if (user == null) {
			resultBean.setSuccess(false);
			resultBean.setErrorCode(ErrorCodeConstants.ERR_G_NOUSR);
			resultBean.setMessage("用户不存在!");
			return SUCCESS;
		}

		BusUserInfo userinfo = accountService.selectUserInfoByPrimaryKey(user.getInfoId());
		String imgurl = RoleUtil.selectRuleByPrimaryKey(RoleUtil.IMG_URL);
		if (userinfo != null) {
			if (user.getHeadImg() != null && imgurl != null) {
				userinfo.setHeadImg(imgurl + user.getHeadImg());
			}
			userinfo.setUsername(user.getUsername());
			userinfo.setPhone(user.getPhone());
			userinfo.setEmail(user.getEmail());
			if (userinfo.getPic() != null && imgurl != null) {
				userinfo.setPic(imgurl + userinfo.getPic());
			}
			// 判断是普通个人用户或集团子账户
			Integer groupId = user.getGroupId();
			if (groupId != null && groupId > 0) { // 集团子账户
				BusUser groupUser = userService.selectUserByCache(groupId);
				userinfo.setUserType(RoleTypeEnum.GROUP_SUB.getShortValue());
				// 返回集团账号名称
				BusGroupInfo groupInfo = userService.selectBusGroupInfoByPrimaryKey(groupUser.getInfoId());
				userinfo.setGroupName(groupInfo.getGroupName());
			} else { // 普通个人用户
				userinfo.setUserType(RoleTypeEnum.PERSON.getShortValue());
				// 查询用户实名状态
				BusUserReal userReal = userService.selectLatestUserRealByUserid(user.getId());
				// 0：未实名，1：审核中，2:审核通过，3：审核失败
				if (userReal == null) {
					userinfo.setRealStatus((short) 0);
				} else {
					userinfo.setRealStatus(userReal.getStatus());
				}
			}
		}
		// Map<String, Object> data = new HashMap<String, Object>();
		// data.put("userinfo", userinfo);
		resultBean.setData(userinfo);
		getResponse().setHeader("Cache-Control", "no-cache");
		return SUCCESS;
    }

    /**
     * 头像修改
     */
    public String headimgedit() {
        userkey = decrypt(this.getAttribute("userkey"));
        if (!NumberUtils.isNumber(userkey)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        if (headimg == null) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_IMG_EMPTYERR);// 1：上传图片为空
            resultBean.setMessage("上传图片为空!");
            return SUCCESS;
        }
        try {
            String id = userkey + "_" + System.currentTimeMillis();
            Map<String, Object> map = ImageUtil.uploadImg(headimg, id, ImgTypeEnum.HEAD_IMG, getDataPath());
            // Map<String, Object> map = ImageUtil.uploadImg(headimg, Integer.valueOf(userkey), ImgTypeEnum.HEAD_IMG,
            // getDataPath());
            if ("success".equals(map.get("msg"))) {
                String headImgPath = map.get("url").toString();
                BusUser user = new BusUser();
                user.setId(Integer.valueOf(userkey));
                user.setHeadImg(headImgPath);
                if (userService.updateUserByPKSelective(user) > 0) {
                    ChargingCacheUtil.removeUser(userkey);
                    LogUtil.recordDocumentlog(user.getId(), LogOperatorEnum.MODIFY, null, "修改头像", "", true);
                }

            } else {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_IMG_UPLOADERR);// 2：上传图片失败
                resultBean.setMessage("上传图片失败!");
                return SUCCESS;
            }
        } catch (Exception e) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);
            resultBean.setMessage("系统调用异常，请稍后重试!");
            e.printStackTrace();
        }
        return SUCCESS;
    }

    /**
     * 昵称修改
     */
    public String usernameedit() {
        userkey = decrypt(this.getAttribute("userkey"));
        String username = this.getAttribute("username");
        if (!NumberUtils.isNumber(userkey) || AppTool.isNull(username)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        BusUserExample emp = new BusUserExample();
        BusUserExample.Criteria cr = emp.createCriteria();
        cr.andUsernameEqualTo(username);
        List<BusUser> userList = userService.selectUserByExample(emp);
        if (userList != null && userList.size() > 0) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_USERNAME_REPEAT);// 1:昵称已经存在
            resultBean.setMessage("昵称已经存在!");
            return SUCCESS;
        }
        BusUser record = new BusUser();
        record.setId(Integer.valueOf(userkey));
        record.setUsername(username);
        if (userService.updateUserByPKSelective(record) > 0) {
            ChargingCacheUtil.removeUser(userkey);
            LogUtil.recordDocumentlog(record.getId(), LogOperatorEnum.MODIFY, null, "修改昵称", "{username:" + username + "}", true);
        } else {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);
            resultBean.setMessage("昵称修改失败!");
            return SUCCESS;
        }
        return SUCCESS;
    }

    /**
     * 性别修改
     */
    public String sexedit() {
        userkey = decrypt(this.getAttribute("userkey"));
        String sex = this.getAttribute("sex");
        if (AppTool.isNotNumber(userkey, sex)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        BusUser user = userService.selectUserByCache(Integer.valueOf(userkey));
        if (user == null) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_NOUSR);
            resultBean.setMessage("用户不存在!");
            return SUCCESS;
        }

        BusUserInfo record = new BusUserInfo();
        record.setId(user.getInfoId());
        record.setSex(Short.valueOf(sex));
        if (userService.updateUserInfoByPrimaryKeySelective(record) <= 0) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);
            resultBean.setMessage("性别修改失败!");
        } else {
            LogUtil.recordDocumentlog(user.getId(), LogOperatorEnum.MODIFY, null, "修改性别", "{sex:" + sex + "}", true);
        }
        return SUCCESS;
    }

    /**
     * 个性签名修改
     */
    public String signedit() {
        userkey = decrypt(this.getAttribute("userkey"));
        String sign = this.getAttribute("sign");
        if (!NumberUtils.isNumber(userkey)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        BusUser user = userService.selectUserByCache(Integer.valueOf(userkey));
        if (user == null) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_NOUSR);
            resultBean.setMessage("用户不存在!");
            return SUCCESS;
        }

        BusUserInfo record = new BusUserInfo();
        record.setId(user.getInfoId());
        record.setSign(sign);
        if (userService.updateUserInfoByPrimaryKeySelective(record) <= 0) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);
            resultBean.setMessage("个性签名修改失败!");
        } else {
            LogUtil.recordDocumentlog(user.getId(), LogOperatorEnum.MODIFY, null, "修改个性签名", "{sign:" + sign + "}", true);
        }
        return SUCCESS;
    }

    /**
     * 修改地区
     * 
     * @return
     */
    public String editarea() {
        userkey = decrypt(this.getAttribute("userkey"));
        String type = this.getAttribute("type");
        String province = this.getAttribute("province");
        String city = this.getAttribute("city");

        if (AppTool.isNotNumber(type, userkey)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        if (Integer.parseInt(type) == 2) {
            if (AppTool.isNotNumber(province, city)) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
                resultBean.setMessage("用户输入参数非法!");
                return SUCCESS;
            }
        }
        if (Integer.parseInt(type) == 1) {// 进入修改
            List<SysDefArea> provincelist = CacheSysHolder.getProvinceList();// 省列表
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("provincelist", provincelist);
            resultBean.setData(data);
        } else if (Integer.parseInt(type) == 2 && Integer.parseInt(province) > 0 && Integer.parseInt(city) > 0) {// 修改
            BusUser bususer = userService.selectUserByPrimaryKey(Integer.valueOf(userkey));
            BusUserInfo newBusUserInfo = new BusUserInfo();
            newBusUserInfo.setId(bususer.getInfoId());
            newBusUserInfo.setProvince(Integer.valueOf(province));
            newBusUserInfo.setCity(Integer.valueOf(city));
            if (userService.updateUserInfoByPrimaryKeySelective(newBusUserInfo) > 0) {
                ChargingCacheUtil.removeUser(userkey);
                LogUtil.recordDocumentlog(bususer.getId(), LogOperatorEnum.MODIFY, null, "修改地区", "{province:" + province + ",city:" + city + "}", true);
            } else {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);
                resultBean.setMessage("修改地区失败!");
                return SUCCESS;
            }
        } else {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        return SUCCESS;
    }

    /**
     * 根据省级ID返回市级列表信息
     * 
     * @return
     */
    public String returncitys() {
        userkey = decrypt(this.getAttribute("userkey"));
        String province = this.getAttribute("province");
        if (AppTool.isNotNumber(userkey, province)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        if (Integer.parseInt(province) > 0) {
            List<SysDefArea> citylist = CacheSysHolder.getCityListByPid(Integer.valueOf(province));// 市列表
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("citylist", citylist);
            resultBean.setData(citylist);
        } else {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("请选择省份!");
            return SUCCESS;
        }
        return SUCCESS;
    }

    /**
     * 实名认证
     */
    public String realnameauth() {
        userkey = decrypt(this.getAttribute("userkey"));
        String realname = this.getAttribute("realname");
        String cardno = this.getAttribute("cardno");
        if (!NumberUtils.isNumber(userkey) || AppTool.isNull(realname, cardno)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        if (front == null) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_IMG_EMPTYERR);// 1：上传图片为空
            resultBean.setMessage("上传图片为空!");
            return SUCCESS;
        }
        BusUserRealExample emp = new BusUserRealExample();
        BusUserRealExample.Criteria cr = emp.createCriteria();
        cr.andUserIdEqualTo(Integer.valueOf(userkey));
        cr.andStatusNotEqualTo(RealVerifyStatusEnum.FAILED.getShortValue());
        emp.setOrderByClause("STATUS DESC");
        List<BusUserReal> list = userService.selectUserRealByExample(emp);
        for (BusUserReal record : list) {
            if (RealVerifyStatusEnum.PASSED.getShortValue().equals(record.getStatus())) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_REAL_PASSED);// 4：已审核通过
                resultBean.setMessage("您已实名认证过!");
                return SUCCESS;
            } else if (RealVerifyStatusEnum.VERIFYING.getShortValue().equals(record.getStatus())) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_REAL_VERIFYING);// 3：实名认证审核中
                resultBean.setMessage("您的实名认证信息正在审核中,请耐心等待!");
                return SUCCESS;
            }
        }

        try {
            Date now = new Date();
            Map<String, Object> map = ImageUtil.uploadImg(front, now, ImgTypeEnum.USER_CARD_FRONT_TEMP, getDataPath());
            if ("success".equals(map.get("msg"))) {
                String frontPath = map.get("url").toString();
                BusUserReal record = new BusUserReal();
                record.setUserId(Integer.valueOf(userkey));
                record.setCardNum(cardno);
                record.setFront(frontPath);
                record.setRealName(realname);
                record.setAddTime(now);
                record.setStatus(RealVerifyStatusEnum.VERIFYING.getShortValue());

                userService.insertUserReal(record);
            } else {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_IMG_UPLOADERR);// 2：上传图片失败
                resultBean.setMessage("上传图片失败!");
                return SUCCESS;
            }
        } catch (Exception e) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);
            resultBean.setMessage("系统调用异常，请稍后重试!");
            e.printStackTrace();
        }
        return SUCCESS;
    }

    /**
     * 我的爱车添加
     *
     * @return
     */
    public String caradd() {
        userkey = decrypt(this.getAttribute("userkey"));
        if (!NumberUtils.isNumber(userkey)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        BusUser user = userService.selectUserByCache(Integer.valueOf(userkey));
        if (user == null) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_NOUSR);
            resultBean.setMessage("用户不存在!");
            return SUCCESS;
        }

        String brandid = this.getAttribute("brand");
        String modelid = this.getAttribute("model");
        String carno = this.getAttribute("carno");
        String vin = this.getAttribute("vin");

        BusUserInfo record = new BusUserInfo();
        if (NumberUtils.isNumber(brandid)) {
            record.setBrand(Integer.valueOf(brandid));
        }
        if (NumberUtils.isNumber(modelid)) {
            record.setModel(Integer.valueOf(modelid));
        }
        if (!StringUtils.isEmpty(carno)) {
            record.setPlateNo(carno);
        }
        if (!StringUtils.isEmpty(vin)) {
            record.setVin(vin);
        }
        if (pic != null) {
            try {
                Map<String, Object> map = ImageUtil.uploadImg(pic, Integer.valueOf(userkey), ImgTypeEnum.USER_DRIVING_LICENSE, getDataPath());
                if ("success".equals(map.get("msg"))) {
                    String picpath = map.get("url").toString();
                    record.setPic(picpath);
                } else {
                    resultBean.setSuccess(false);
                    resultBean.setErrorCode(ErrorCodeConstants.ERR_IMG_UPLOADERR);// 2：上传图片失败
                    resultBean.setMessage("上传图片失败!");
                    return SUCCESS;
                }
            } catch (Exception e) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);
                resultBean.setMessage("系统调用异常，请稍后重试!");
                e.printStackTrace();
            }
        }
        record.setId(user.getInfoId());
        int count = accountService.updateUserInfoByPKSelective(record);
        if (count <= 0) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);
            resultBean.setMessage("爱车添加失败!");
        }
        return SUCCESS;
    }

    /**
     * 我的设备
     *
     * @return
     */
    public String mypile() {
        userkey = decrypt(this.getAttribute("userkey"));
        if (!NumberUtils.isNumber(userkey)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        BusUser busUser = userService.selectUserByCache(Integer.valueOf(userkey));
        if (busUser == null) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_NOUSR);
            resultBean.setMessage("用户不存在!");
            return SUCCESS;
        }
        List<PileRunStatusModel> pilelist = CacheChargeHolder.getPileStatusListByInfoId(busUser.getInfoId(), UserTypeEnum.PERSON.getShortValue());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("pilelist", pilelist);
        resultBean.setData(data);
        return SUCCESS;
    }

    /**
     * 个人桩详情
     * 
     * @return
     */
    public String pileinfo() {
        String pileid = this.getAttribute("pileid");
        if (!NumberUtils.isNumber(pileid)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        PobChargingPile pile = pobObjectService.selectChargingPileByPK(Integer.valueOf(pileid));
        if (pile == null) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_NOPILE);// 20002: 无此充电桩
            resultBean.setMessage("无此充电桩!");
            return SUCCESS;
        }
        PileDetailInfo piledetail = new PileDetailInfo();
        piledetail.setPileid(pile.getId());
        piledetail.setPilename(pile.getPileName());
        piledetail.setChawaydesc(ChargeCurrentTypeEnum.getText(pile.getChaWay()));
        piledetail.setPiletypedesc(ChargePowerTypeEnum.getText(pile.getPileType()));

        PobChargingStation station = CacheChargeHolder.getChargeStationById(pile.getStationId());
        if (station != null && station.getStationName() != null) {
            piledetail.setStationname(station.getStationName());
        }

        // 地址
        String address = "";
        if (station != null && station.getAddress() != null) {
            address += station.getAddress();
        }
        if (pile.getAddress() != null) {
            address += pile.getAddress();
        }
        piledetail.setAddress(address);

        // 开放时间
        String opendaytime = "";
        if (station != null && station.getOpenDay() != null) {
            opendaytime += CacheSysHolder.getSysLinkName(LinkTypeEnum.OPEN_DAY.getValue(), station.getOpenDay().toString());
        }
        if (station != null && station.getOpenTime() != null) {
            if (StringUtil.isNotEmpty(opendaytime)) {
                opendaytime += " ";
            }
            opendaytime += CacheSysHolder.getSysLinkName(LinkTypeEnum.OPEN_TIME.getValue(), station.getOpenTime().toString());
        }
        piledetail.setOpentime(opendaytime);

        // 查询桩型号信息
        BusPileModel pileModel = CacheChargeHolder.selectPileModelById(pile.getPileModel());
        if (pileModel != null) {
            piledetail.setRatp(pileModel.getRatP() == null ? "" : pileModel.getRatP());
            piledetail.setOutv(pileModel.getOutV() == null ? "" : pileModel.getOutV());
        }
        // 查询费用信息:充电费、停车费、服务费
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pileId", pileid);
        params.put("status", WhetherEnum.YES.getShortValue());// 生效
        params.put("active", WhetherEnum.YES.getShortValue());// 已使用
        ChargeRuleModel ruleModel = deviceService.selectChargeRuleModelByMap(params);
        if (ruleModel != null) {
            if (Globals.CHARGE_RULE_SINGLE == ruleModel.getId().intValue()) {
                piledetail.setChargefee(NumberUtil.formateScale2Str(ruleModel.getChargeFee()));
            } else {
                String ruleDetail = "";
                if (ruleModel.getJianFee() != null) {
                    ruleDetail += "尖:" + NumberUtil.formateScale2Str(ruleModel.getJianFee()) + ";";
                }
                if (ruleModel.getFengFee() != null) {
                    ruleDetail += "峰:" + NumberUtil.formateScale2Str(ruleModel.getFengFee()) + ";";
                }
                if (ruleModel.getPingFee() != null) {
                    ruleDetail += "平:" + NumberUtil.formateScale2Str(ruleModel.getPingFee()) + ";";
                }
                if (ruleModel.getGuFee() != null) {
                    ruleDetail += "谷:" + NumberUtil.formateScale2Str(ruleModel.getGuFee()) + ";";
                }
                piledetail.setChargefee(ruleDetail);
            }
            piledetail.setParkfee(ruleModel.getParkFee() == null ? "" : NumberUtil.formateScale2Str(ruleModel.getParkFee()));
            piledetail.setServicefee(ruleModel.getServiceFee() == null ? "" : NumberUtil.formateScale2Str(ruleModel.getServiceFee()));
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("piledetailinfo", piledetail);
        resultBean.setData(data);
        return SUCCESS;
    }

    /**
     * 我的设备交易明细:充电记录，预约记录
     * 
     * @return
     */
    public String mypiletrade() {
        userkey = decrypt(this.getAttribute("userkey"));
        String datatime = this.getAttribute("datatime");// 2016-05
        String pageindex = this.getAttribute("pageindex");

        if (AppTool.isNotNumber(userkey, pageindex) || AppTool.isNull(datatime)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        Date startdate = DateUtil.ShortStrToDate(datatime + "-01");
        Date enddate = DateUtil.addMonths(startdate, 1);
        Date thismonth = DateUtil.getFirstDayOfMonth(new Date());

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startdate", startdate);
        params.put("enddate", enddate);

        Page page = this.returnPage(Integer.valueOf(pageindex), limit);
        params.put(Globals.PAGE, page);
        List<TradeRecord> tradelist;
        if (thismonth.after(startdate)) {// 上月及之前，查询已结算的资金详细表
            params.put("userid", Integer.valueOf(userkey));
            tradelist = accountService.selectBillDetailByParamByPage(params);
            // 设置描述
            if (tradelist == null || tradelist.size() == 0) {
                return SUCCESS;
            }
            for (TradeRecord record : tradelist) {
                record.setFee(NumberUtil.formateScale2(record.getFee()));
                record.setName(CacheChargeHolder.getStationPileNameById(record.getPileid()));
            }
        } else {// 这个月及之后，查询未结算的充电、预约记录表
            BusUser user = userService.selectUserByCache(Integer.valueOf(userkey));
            params.put("busmec", user.getInfoId());
            params.put("bustype", UserTypeEnum.PERSON.getShortValue());
            tradelist = chargingService.selectTradeDetailByParamByPage(params);
            String ratestr = RoleUtil.selectRuleByPrimaryKey(RoleUtil.RATE);
            // 结算
            if (tradelist == null || tradelist.size() == 0) {
                return SUCCESS;
            }
            if (NumberUtils.isNumber(ratestr)) {
                BigDecimal rate = new BigDecimal(ratestr);
                rate = BigDecimal.ONE.subtract(rate);
                BigDecimal fee = BigDecimal.ZERO;
                for (TradeRecord record : tradelist) {
                    if (record.getFee() != null) {
                        fee = record.getFee().multiply(rate);
                        record.setFee(NumberUtil.formateScale2(fee));
                    }
                }
            }
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("tradelist", tradelist);
        data.put("totoalcount", page.getTotalProperty());
        resultBean.setData(data);
        return SUCCESS;
    }

    /**
     * 个人桩代管申请
     * 
     * @return
     */
    public String pileapply() {
        userkey = decrypt(this.getAttribute("userkey"));
        String realname = this.getAttribute("realname");
        String phone = this.getAttribute("phone");
        String address = this.getAttribute("address");
        String remark = this.getAttribute("remark");
        if (!NumberUtils.isNumber(userkey) || AppTool.isNull(realname, phone, address)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004: 用户输入参数非法
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        if (Validator.isMobile(phone) || Validator.isTelephone(phone)) {
            BusPileApply record = new BusPileApply();
            record.setAddress(address);
            record.setAddtime(new Date());
            record.setPhone(phone);
            record.setRealName(realname);
            if (!StringUtils.isEmpty(remark)) {
                record.setRemark(remark);
            }
            record.setUserId(Integer.valueOf(userkey));
            record.setValidStatus(PileApplyValidStatusEnum.UNPROCESSED.getShortValue());
            if (deviceService.insertPileApply(record) <= 0) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);
                resultBean.setMessage("个人桩代管申请失败!");
            }
        } else {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_PILE_APPLY_PHONEERR);// 1:联系方式格式不正确
            resultBean.setMessage("联系方式格式不正确!");
            return SUCCESS;
        }
        return SUCCESS;
    }

    /**
     * 设备维护
     *
     * @return
     */
    public String pileedit() {
        return SUCCESS;
    }

    /**
     * 意见反馈
     *
     * @return
     */
    public String suggestion() {
        userkey = decrypt(this.getAttribute("userkey"));
        String content = this.getAttribute("content");

        if (!NumberUtils.isNumber(userkey) || AppTool.isNull(content)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004: 用户输入参数非法
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        BusSuggestion record = new BusSuggestion();
        record.setUserId(Integer.valueOf(userkey));
        record.setAddTime(new Date());
        record.setContent(content);
        record.setStatus(SuggestionStatusEnum.UNPROCESSED.getShortValue());
        // 处理图片
        String pics = "";
        if (piclist != null && piclist.size() > 0) {
            File f;
            String id;
            for (int i = 0; i < piclist.size(); i++) {
                f = piclist.get(i);
                if (f != null) {
                    try {
                        id = userkey + "_" + record.getAddTime().getTime() + "_" + (i + 1);
                        Map<String, Object> map = ImageUtil.uploadImg(f, id, ImgTypeEnum.USER_SUGGESTION, getDataPath());
                        if ("success".equals(map.get("msg"))) {
                            String picpath = map.get("url").toString();
                            pics += picpath + ",";
                        } else {
                            resultBean.setSuccess(false);
                            resultBean.setErrorCode(ErrorCodeConstants.ERR_IMG_UPLOADERR);// 2：上传图片失败
                            resultBean.setMessage("上传图片失败!");
                            return SUCCESS;
                        }
                    } catch (Exception e) {
                        resultBean.setSuccess(false);
                        resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);
                        resultBean.setMessage("系统调用异常，请稍后重试!");
                        e.printStackTrace();
                    }
                }
            }
            if (StringUtil.isNotEmpty(pics)) {
                pics = pics.substring(0, pics.length() - 1);
                record.setPic(pics);
            }
        }
        if (userService.insertSuggestion(record) <= 0) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);
            resultBean.setMessage("意见反馈失败!");
        }
        return SUCCESS;
    }

    /**
     * 冻结资金(提现冻结)
     * 
     * @return
     */
    public String freezefund() {
        userkey = decrypt(this.getAttribute("userkey"));
        String pageindex = this.getAttribute("pageindex");
        if (AppTool.isNotNumber(userkey, pageindex)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004: 用户输入参数非法
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userid", Integer.valueOf(userkey));
        Page page = this.returnPage(Integer.valueOf(pageindex), limit);
        params.put(Globals.PAGE, page);

        List<BusCash> list = accountService.selectCashFreezeMoneyByPage(params);
        if (list != null && list.size() > 0) {
            List<CashFreeze> cashFreezeList = new ArrayList<CashFreeze>();
            for (BusCash record : list) {
                CashFreeze cash = new CashFreeze();
                cash.setId(record.getId());
                cash.setAccountInfo(record.getAccountInfo());
                cash.setAddTime(record.getAddTime());
                cash.setMoney(NumberUtil.formateScale2Str(record.getMoney()));

                if (record.getValidStatus() != null) {
                    if (CashVerifyStatusEnum.PASSED.getShortValue().equals(record.getValidStatus()) && CashStatusEnum.WITHDRAWING.getShortValue().equals(record.getCashStatus())) {
                        cash.setValidStatusDesc(CashStatusEnum.WITHDRAWING.getText());// 提现中
                    } else {
                        cash.setValidStatusDesc(CashVerifyStatusEnum.getText(record.getValidStatus().intValue()));
                    }
                }
                if (record.getCashWay() != null) {
                    cash.setCashWayDesc(CashWayEnum.getText(record.getCashWay().intValue()));
                }
                cashFreezeList.add(cash);
            }
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("cashfreezelist", cashFreezeList);
            data.put("totoalcount", page.getTotalProperty());
            resultBean.setData(data);
        }
        return SUCCESS;
    }

    /**
     * 获取用户与融云相关的信息(用户昵称、头像)
     * 
     * @return
     */
    public String ronguserinfo() {
        userkey = decrypt(this.getAttribute("userkey"));
        if (!NumberUtils.isNumber(userkey)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        BusUser user = userService.selectUserByPrimaryKey(Integer.valueOf(userkey));
        if (user != null) {
            String imgurl = RoleUtil.selectRuleByPrimaryKey(RoleUtil.IMG_URL);
            RongCloudUser record = new RongCloudUser();
            record.setUserkey(encrypt(userkey));
            record.setUsername(user.getUsername());
            if (user.getHeadImg() != null) {
                record.setHeadimg(imgurl + user.getHeadImg());
            }
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("ronguserinfo", record);
            resultBean.setData(data);
        }
        return SUCCESS;
    }

    public ResultBean getResultBean() {
        return resultBean;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public void setAppointmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public void setChargingService(ChargingService chargingService) {
        this.chargingService = chargingService;
    }

    public void setPobObjectService(PobObjectService pobObjectService) {
        this.pobObjectService = pobObjectService;
    }

    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    public void setPic(File pic) {
        this.pic = pic;
    }

    public void setPiclist(List<File> piclist) {
        this.piclist = piclist;
    }

    public void setHeadimg(File headimg) {
        this.headimg = headimg;
    }

    public void setFront(File front) {
        this.front = front;
    }

}
