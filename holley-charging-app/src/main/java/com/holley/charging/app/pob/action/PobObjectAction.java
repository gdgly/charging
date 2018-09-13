package com.holley.charging.app.pob.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import com.holley.charging.action.BaseAction;
import com.holley.charging.app.util.AppTool;
import com.holley.charging.app.util.AppTool.StationSortTypeEnum;
import com.holley.charging.common.util.CacheChargeHolder;
import com.holley.charging.model.app.PileStatusInfo;
import com.holley.charging.model.app.StationFee;
import com.holley.charging.model.app.StationIntro;
import com.holley.charging.model.app.StationMarker;
import com.holley.charging.model.bus.BusFavorites;
import com.holley.charging.model.bus.BusFavoritesExample;
import com.holley.charging.model.bus.BusPileComment;
import com.holley.charging.model.bus.BusPileModel;
import com.holley.charging.model.bus.BusRepairPoint;
import com.holley.charging.model.def.PileRunStatusModel;
import com.holley.charging.model.pob.PobChargingPile;
import com.holley.charging.model.pob.PobChargingStation;
import com.holley.charging.model.pob.PobChargingStationExample;
import com.holley.charging.service.bussiness.AccountService;
import com.holley.charging.service.website.PobObjectService;
import com.holley.common.cache.charging.CacheKeyProvide;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.Globals;
import com.holley.common.constants.ResultBean;
import com.holley.common.constants.app.ErrorCodeConstants;
import com.holley.common.constants.charge.ImgTypeEnum;
import com.holley.common.constants.charge.LinkTypeEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.dataobject.PileStatusBean;
import com.holley.common.security.SecurityUtil;
import com.holley.common.util.DateUtil;
import com.holley.common.util.RandomUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.CacheSysHolder;
import com.holley.platform.util.RoleUtil;
import com.holley.web.common.util.ImageUtil;

/**
 * 档案对象
 * 
 * @author zhouli
 */
public class PobObjectAction extends BaseAction {

    private static final long serialVersionUID  = 1L;
    private PobObjectService  pobObjectService;
    private AccountService    accountService;

    private ResultBean        resultBean        = new ResultBean();
    private String            userkey;
    private List<File>        piclist;
    private Page              page;
    private static String     STATION_SHARE_URL = "/pob/stationshare.htm"; // 充电点分享地址
    private static String     ABOUTUS_URL       = "/pob/usinfo.htm";      // 关于我们链接地址

    /**
     * 生成充电点充电桩JSON加密key
     *
     * @return
     */
    public String securitykey() throws Exception {
        String securitykey = RandomUtil.getRandomStr(16);
        String uuid = this.getAttribute("uuid");
        ChargingCacheUtil.setSecurityKeySession(securitykey, uuid);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("securitykey", securitykey);
        return SUCCESS;
    }

    /**
     * 充电点
     * 
     * @return
     */
    public String chargestation() {
        String lasttime = this.getAttribute("lasttime");
        String uuid = this.getAttribute("uuid");

        String securitykey = ChargingCacheUtil.getSecurityKeySession(uuid);

        // 加入测试bean
        // setTestBean();
        if (securitykey != null) {
            List<PobChargingStation> chargestationlists;
            PobChargingStationExample csexample = new PobChargingStationExample();
            PobChargingStationExample.Criteria cscr = csexample.createCriteria();
            if (lasttime != null && !lasttime.equals("")) {
                cscr.andUpdateTimeGreaterThan(DateUtil.LongStrToDate(lasttime));
                chargestationlists = pobObjectService.selectChargingStationByExample(csexample);
            } else {
                chargestationlists = pobObjectService.selectChargingStationByExample(csexample);
            }
            String chargestationlist = null;
            if (chargestationlists != null) {
                chargestationlist = SecurityUtil.encrypt(chargestationlists.toString(), securitykey);
            }
            lasttime = DateUtil.DateToLongStr(new Date());
            securitykey = null;
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("chargestationlist", chargestationlist);
            data.put("lasttime", lasttime);
            resultBean.setData(data);
        } else {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_KEYTIMEOUT);
            resultBean.setMessage("秘钥已过期,需重新申请");
            lasttime = null;
        }
        return SUCCESS;
    }

    /**
     * 充电桩
     * 
     * @return
     */
    public String piles() {
        String lasttime = this.getAttribute("lasttime");
        String stationid = this.getAttribute("stationid");
        String uuid = this.getAttribute("uuid");
        String securitykey = ChargingCacheUtil.getSecurityKeySession(uuid);
        if (securitykey != null) {
            Map<String, Object> params = new HashMap<String, Object>();
            if (stationid != null) {
                params.put("stationid", Integer.valueOf(stationid));
            }
            if (lasttime != null && !lasttime.equals("")) {
                params.put("updatetime", DateUtil.LongStrToDate(lasttime));
            }
            List<PobChargingPile> pilelists = pobObjectService.selectPileAndFeeByParam(params);
            String pilelist = null;
            if (pilelists != null) {
                pilelist = SecurityUtil.encrypt(pilelists.toString(), securitykey);
            }
            lasttime = DateUtil.DateToLongStr(new Date());
            securitykey = null;
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("pilelist", pilelist);
            data.put("lasttime", lasttime);
            resultBean.setData(data);
        } else {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_KEYTIMEOUT);
            resultBean.setMessage("秘钥已过期,需重新申请");
            lasttime = null;
        }
        return SUCCESS;
    }

    /**
     * 服务点
     * 
     * @return
     */
    public String repairpoint() {
        String keyword = this.getAttribute("keyword");
        String lng = this.getAttribute("lng");
        String lat = this.getAttribute("lat");

        if (AppTool.isNotNumber(lng, lat)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        if (!StringUtils.isEmpty(keyword)) {
            keyword = StringUtils.trim(keyword);
        }

        double lng1;
        double lat1;
        double lng2 = StringUtil.parseDouble(lng);
        double lat2 = StringUtil.parseDouble(lat);
        List<BusRepairPoint> list = CacheChargeHolder.getRepairPointList();
        if (list == null || list.size() == 0) {
            return SUCCESS;
        }

        List<BusRepairPoint> tempList = new ArrayList<BusRepairPoint>();
        BusRepairPoint repairpoint;
        for (BusRepairPoint record : list) {
            if (!StringUtils.isEmpty(keyword)) {
                if (record.getName().indexOf(keyword) < 0 && record.getAddress().indexOf(keyword) < 0) {
                    continue;
                }
            }
            lng1 = StringUtil.parseDouble(record.getLng());
            lat1 = StringUtil.parseDouble(record.getLat());
            repairpoint = new BusRepairPoint();
            repairpoint.setAddress(record.getAddress());
            repairpoint.setAddTime(record.getAddTime());
            repairpoint.setDistance(AppTool.getPointDistance(lng1, lat1, lng2, lat2));
            repairpoint.setId(record.getId());
            repairpoint.setIsShow(record.getIsShow());
            repairpoint.setLat(record.getLat());
            repairpoint.setLng(record.getLng());
            repairpoint.setName(record.getName());
            repairpoint.setPhone(record.getPhone());
            repairpoint.setTel(record.getTel());
            repairpoint.setWorkTime(record.getWorkTime());
            tempList.add(repairpoint);
        }

        // 按距离排序
        Collections.sort(tempList, new Comparator<BusRepairPoint>() {

            public int compare(BusRepairPoint obj1, BusRepairPoint obj2) {
                return Double.valueOf(obj1.getDistance()).compareTo(Double.valueOf(obj2.getDistance()));
            }
        });

        // 取最近100条数据
        List<BusRepairPoint> repairpointlist;
        if (list.size() > Globals.STATION_SHOW_LIMIT) {
            repairpointlist = tempList.subList(0, Globals.STATION_SHOW_LIMIT);
        } else {
            repairpointlist = tempList;
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("repairpointlist", repairpointlist);
        resultBean.setData(data);
        return SUCCESS;
    }

    /**
     * 查看评论
     * 
     * @return
     */
    public String pilecomment() {
        String stationid = this.getAttribute("stationid");
        String pageindex = this.getAttribute("pageindex");

        if (AppTool.isNotNumber(stationid, pageindex)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("stationid", Integer.valueOf(stationid));

        Page page = this.returnPage(Integer.valueOf(pageindex), limit);
        params.put(Globals.PAGE, page);
        List<BusPileComment> commentlist = pobObjectService.selectStationCommentByPage(params);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("commentlist", commentlist);
        data.put("totoalcount", page.getTotalProperty());
        data.put("imgurl", RoleUtil.selectRuleByPrimaryKey(RoleUtil.IMG_URL));
        resultBean.setData(data);
        return SUCCESS;
    }

    /**
     * 发表评论
     * 
     * @return
     */
    public String commentadd() {
        userkey = decrypt(this.getAttribute("userkey"));
        String pileid = this.getAttribute("pileid");
        String chargerecordid = this.getAttribute("chargerecordid");
        String content = this.getAttribute("content");
        String score = this.getAttribute("score");

        if (AppTool.isNotNumber(userkey, pileid, chargerecordid, score)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004: 用户输入参数非法
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        Integer recordid = Integer.valueOf(chargerecordid);

        BusPileComment bpcomment = new BusPileComment();
        bpcomment.setPileId(Integer.valueOf(pileid));
        bpcomment.setChargerecordId(recordid);
        if (!StringUtils.isEmpty(content)) {
            bpcomment.setContent(content);
        }
        bpcomment.setScore(Short.valueOf(score));
        bpcomment.setUserId(Integer.valueOf(userkey));
        bpcomment.setPid(0);
        bpcomment.setAddTime(new Date());

        // 处理图片
        String pics = "";
        if (piclist != null && piclist.size() > 0) {
            File f;
            String id;
            for (int i = 0; i < piclist.size(); i++) {
                f = piclist.get(i);
                if (f != null) {
                    try {
                        id = pileid + "_" + bpcomment.getAddTime().getTime() + "_" + (i + 1);
                        Map<String, Object> map = ImageUtil.uploadImg(f, id, ImgTypeEnum.PILE_COMMENT, getDataPath());
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
                bpcomment.setPath(pics);
            }
        }

        if (pobObjectService.insertPlieComment(bpcomment) > 0) {
            PobChargingPile pile = CacheChargeHolder.getChargePileById(Integer.valueOf(pileid));
            if (pile != null && pile.getStationId() != null) {
                // 更新充电点评论
                Map<String, Object> params = new HashMap<String, Object>();
                Date updatetime = new Date();
                params.put("stationid", pile.getStationId());
                params.put("updatetime", updatetime);
                if (pobObjectService.updateChargingStationScore(params) > 0) {
                    // 更新缓存
                    ChargingCacheUtil.setUpdateTime(CacheKeyProvide.KEY_STATION_UPDATETIME, updatetime);
                }
            }
        } else {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);
            resultBean.setMessage("发表评论失败!");
        }
        return SUCCESS;

    }

    /**
     * 根据地图边界范围查询充电点
     * 
     * @return
     */
    public String qrystationbyrange() {
        String swlngstr = this.getAttribute("swlng");// 西南经度
        String swlatstr = this.getAttribute("swlat");// 西南纬度
        String nelngstr = this.getAttribute("nelng");// 东北经度
        String nelatstr = this.getAttribute("nelat");// 东北纬度
        if (AppTool.isNotNumber(swlngstr, swlatstr, nelngstr, nelatstr)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
        }
        List<StationMarker> resultList = new ArrayList<StationMarker>();
        double swlng = StringUtil.parseDouble(swlngstr);
        double swlat = StringUtil.parseDouble(swlatstr);
        double nelng = StringUtil.parseDouble(nelngstr);
        double nelat = StringUtil.parseDouble(nelatstr);
        List<PobChargingStation> list = CacheChargeHolder.getStationByRange(swlng, swlat, nelng, nelat);
        int idleNum = 0;
        StationMarker marker;
        if (list == null || list.size() == 0) {
            return SUCCESS;
        }
        for (PobChargingStation record : list) {
            idleNum = CacheChargeHolder.getPileIdleNumByStationid(record.getId());
            marker = new StationMarker();
            marker.setStationid(record.getId());
            marker.setLat(record.getLat());
            marker.setLng(record.getLng());
            marker.setFastnum(record.getFastNum() == null ? 0 : record.getFastNum());
            marker.setSlownum(record.getSlowNum() == null ? 0 : record.getSlowNum());
            marker.setIdlenum(idleNum);
            resultList.add(marker);
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("markerlist", resultList);// 地图显示的充电点
        resultBean.setData(data);
        return SUCCESS;
    }

    /**
     * 充电点简介
     * 
     * @return
     */
    public String stationintro() {
        String stationid = this.getAttribute("stationid");
        String lng = this.getAttribute("lng");
        String lat = this.getAttribute("lat");
        if (AppTool.isNotNumber(stationid, lng, lat)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        PobChargingStation station = CacheChargeHolder.getChargeStationById(Integer.valueOf(stationid));
        if (station == null) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_NOPILE);
            resultBean.setMessage("无此充电点!");
            return SUCCESS;
        }
        int idlenum = CacheChargeHolder.getPileIdleNumByStationid(Integer.valueOf(stationid));

        double lng1 = StringUtil.parseDouble(station.getLng());
        double lat1 = StringUtil.parseDouble(station.getLat());
        double lng2 = StringUtil.parseDouble(lng);
        double lat2 = StringUtil.parseDouble(lat);

        StationIntro record = new StationIntro();
        record.setStationid(station.getId());
        record.setStationname(station.getStationName());
        record.setLng(station.getLng());
        record.setLat(station.getLat());
        record.setAddress(station.getAddress());
        record.setLinkphone(station.getLinkPhone());
        record.setScore(station.getScore());
        if (station.getImg() != null) {
            record.setImg(RoleUtil.selectRuleByPrimaryKey(RoleUtil.IMG_URL) + station.getImg());
        }
        record.setDistance(AppTool.getPointDistance(lng1, lat1, lng2, lat2));
        record.setFastnum(station.getFastNum() == null ? 0 : station.getFastNum());
        record.setSlownum(station.getSlowNum() == null ? 0 : station.getSlowNum());
        record.setIdlenum(idlenum);
        if (station.getOpenDay() != null) {
            record.setOpendaydesc(CacheSysHolder.getSysLinkName(LinkTypeEnum.OPEN_DAY.getValue(), station.getOpenDay().toString()));
        }
        if (station.getOpenTime() != null) {
            record.setOpentimedesc(CacheSysHolder.getSysLinkName(LinkTypeEnum.OPEN_TIME.getValue(), station.getOpenTime().toString()));
        }

        if (this.getAttribute("userkey") != null) {
            // 查看是否已收藏
            userkey = decrypt(this.getAttribute("userkey"));
            if (NumberUtils.isNumber(userkey)) {
                BusFavoritesExample emp = new BusFavoritesExample();
                BusFavoritesExample.Criteria cr = emp.createCriteria();
                cr.andUserIdEqualTo(Integer.valueOf(userkey));
                cr.andStationIdEqualTo(Integer.valueOf(stationid));
                List<BusFavorites> favoriteList = accountService.selectFavoritesByExample(emp);
                if (favoriteList != null && favoriteList.size() > 0) {
                    record.setFavoriteid(favoriteList.get(0).getId());
                }
            }
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("stationintro", record);// 充电点简介信息
        resultBean.setData(data);
        return SUCCESS;
    }

    /**
     * 搜索充电点（支持地图关键字搜索和附近搜索）
     * 
     * @return
     */
    public String searchstation() {
        String type = this.getAttribute("type");// 1:综合，2：评价，3：价格，4:距离
        String keyword = this.getAttribute("keyword");
        String lng = this.getAttribute("lng");
        String lat = this.getAttribute("lat");

        if (AppTool.isNotNumber(type, lng, lat)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        if (!StringUtils.isEmpty(keyword)) {
            keyword = StringUtils.trim(keyword);
        }
        double lng1;
        double lat1;
        double lng2 = StringUtil.parseDouble(lng);
        double lat2 = StringUtil.parseDouble(lat);
        List<PobChargingStation> list = CacheChargeHolder.getChargeStationList();
        if (list == null || list.size() == 0) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_NOPILE);
            resultBean.setMessage("无充电点!");
            return SUCCESS;
        }

        List<StationIntro> tempList = new ArrayList<StationIntro>();
        StationIntro station;
        for (PobChargingStation record : list) {
            if (!StringUtils.isEmpty(keyword)) {
                if (record.getStationName().indexOf(keyword) < 0 && record.getAddress().indexOf(keyword) < 0) {
                    continue;
                }
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
            station.setScore(record.getScore() == null ? (short) 0 : record.getScore());
            tempList.add(station);
        }

        if (tempList == null || tempList.size() == 0) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_NOPILE);
            resultBean.setMessage("无充电点!");
            return SUCCESS;
        }

        // 按距离排序取最近的100个充电点
        List<StationIntro> stationList = stationSortByType(StationSortTypeEnum.BY_DISTANCE, tempList);

        StationSortTypeEnum sorttype = StationSortTypeEnum.getEnmuByValue(Integer.parseInt(type));
        // 按价格排序时，则设置价格，充电点的价格=服务费的价格+停车费的价格
        if (StationSortTypeEnum.BY_FEE == sorttype) {
            List<StationFee> feeList = pobObjectService.selectStationFee();
            if (feeList != null && feeList.size() > 0) {
                Map<Integer, Double> feeMap = new HashMap<Integer, Double>();
                for (StationFee record : feeList) {
                    feeMap.put(record.getStationid(), record.getFee());
                }
                Double fee;
                for (StationIntro record : stationList) {
                    fee = feeMap.get(record.getStationid());
                    record.setFee(fee == null ? 0 : fee);
                }
            }
        }

        if (StationSortTypeEnum.BY_SCORE == sorttype || StationSortTypeEnum.BY_FEE == sorttype) {
            // 按排序类型进行排序
            stationList = stationSortByType(sorttype, stationList);
        }

        // 设置充电点的空闲桩数，交直流桩数
        Map<String, Integer> numMap;
        for (StationIntro record : stationList) {
            numMap = CacheChargeHolder.getPileNumByStationid(record.getStationid());
            record.setAcnum(numMap.get("acnum"));
            record.setDcnum(numMap.get("dcnum"));
            record.setIdlenum(numMap.get("idlenum"));
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("stationlist", stationList);
        data.put("imgurl", RoleUtil.selectRuleByPrimaryKey(RoleUtil.IMG_URL));
        resultBean.setData(data);
        return SUCCESS;
    }

    /**
     * 根据排序类型进行排序
     * 
     * @param type 1:综合，2：评价，3：价格，4距离
     * @param list
     * @return
     */
    private List<StationIntro> stationSortByType(StationSortTypeEnum type, List<StationIntro> list) {
        if (list == null || list.size() == 0) return null;
        List<StationIntro> resultList;

        if (StationSortTypeEnum.BY_SCORE == type) {// 按评价排序
            Collections.sort(list, new Comparator<StationIntro>() {

                public int compare(StationIntro obj1, StationIntro obj2) {
                    return obj2.getScore().compareTo(obj1.getScore());
                }
            });
        } else if (StationSortTypeEnum.BY_FEE == type) {// 按价格排序
            Collections.sort(list, new Comparator<StationIntro>() {

                public int compare(StationIntro obj1, StationIntro obj2) {
                    return Double.valueOf(obj1.getFee()).compareTo(Double.valueOf(obj2.getFee()));
                }
            });
        } else {// 按距离排序
            Collections.sort(list, new Comparator<StationIntro>() {

                public int compare(StationIntro obj1, StationIntro obj2) {
                    return Double.valueOf(obj1.getDistance()).compareTo(Double.valueOf(obj2.getDistance()));
                }
            });
        }

        if (list.size() > Globals.STATION_SHOW_LIMIT)

        {
            resultList = list.subList(0, Globals.STATION_SHOW_LIMIT);
        } else {
            resultList = list;
        }
        return resultList;
    }

    /**
     * 请求充电桩当前状态
     * 
     * @return
     */
    public String pilestatus() {
        userkey = decrypt(this.getAttribute("userkey"));
        String pileid = this.getAttribute("pileid");

        if (AppTool.isNotNumber(userkey, pileid)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004: 用户输入参数非法
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        PileStatusBean runstatus = ChargingCacheUtil.getPileStatusBean(Integer.valueOf(pileid));
        if (runstatus == null) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_NOPILE);// 20002：无此充电桩
            resultBean.setMessage("无此充电桩!");
            return SUCCESS;
        }
        System.out.println("充电状态：" + runstatus.getStatus() + "，预约状态：" + runstatus.getAppstatus() + ",userid=" + runstatus.getUserid() + ",tradeno=" + runstatus.getTradeno());
        AppTool.pileStatusValueFormat(runstatus);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("pilestatus", runstatus);
        resultBean.setData(data);
        return SUCCESS;
    }

    /**
     * 请求某个充电点下充电桩的当前状态
     *
     * @return
     */
    public String chargestationstatus() {
        String stationid = this.getAttribute("stationid");
        if (!NumberUtils.isNumber(stationid)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004： 用户输入参数非法
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        Map<String, Object> data = new HashMap<String, Object>();
        // 查看该充电点是否已收藏
        if (this.getAttribute("userkey") != null) {
            userkey = decrypt(this.getAttribute("userkey"));
            if (NumberUtils.isNumber(userkey)) {
                BusFavoritesExample emp = new BusFavoritesExample();
                BusFavoritesExample.Criteria cr = emp.createCriteria();
                cr.andUserIdEqualTo(Integer.valueOf(userkey));
                cr.andStationIdEqualTo(Integer.valueOf(stationid));
                List<BusFavorites> list = accountService.selectFavoritesByExample(emp);
                Integer favoriteid = null;
                if (list != null && list.size() > 0) {
                    favoriteid = list.get(0).getId();
                }
                data.put("favoriteid", favoriteid);
            }
        }

        // 充电点分享地址
        String shareurl = RoleUtil.selectRuleByPrimaryKey(RoleUtil.APP_URL) + getRequest().getContextPath();
        shareurl = shareurl + STATION_SHARE_URL + "?stationid=" + encrypt(stationid);
        data.put("stationshareurl", shareurl);

        // 充电桩状态
        List<PobChargingPile> pileList = CacheChargeHolder.getChargePileListByStationid(Integer.valueOf(stationid));
        if (pileList != null && pileList.size() > 0) {
            List<PileStatusInfo> itemList = new ArrayList<PileStatusInfo>();
            PileStatusInfo item;
            BusPileModel pilemodel;
            PileRunStatusModel tempstatus;
            for (PobChargingPile record : pileList) {
                item = new PileStatusInfo();
                item.setPileid(record.getId());
                item.setPilename(record.getPileName());
                item.setPiletype(record.getPileType());
                if (record.getPileModel() != null) {
                    pilemodel = CacheChargeHolder.selectPileModelById(record.getPileModel());
                    if (pilemodel != null) {
                        item.setOutv(pilemodel.getOutV() == null ? "" : pilemodel.getOutV());
                    }
                }
                tempstatus = CacheChargeHolder.getPileStatusById(record.getId());
                if (tempstatus != null) {
                    item.setStatus(tempstatus.getStatus());
                    item.setStatusdesc(tempstatus.getStatusdisc());
                }
                itemList.add(item);
            }

            data.put("pilestatuslist", itemList);
        }
        resultBean.setData(data);
        return SUCCESS;
    }

    /**
     * 最近充电的充电点
     * 
     * @return
     */
    public String recentchargestation() {
        userkey = decrypt(this.getAttribute("userkey"));
        String lng = this.getAttribute("lng");
        String lat = this.getAttribute("lat");
        if (AppTool.isNotNumber(userkey, lng, lat)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004： 用户输入参数非法
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        List<Integer> list = pobObjectService.selectRecentChargeStation(Integer.valueOf(userkey));
        if (list != null && list.size() > 0) {
            PobChargingStation record;
            Map<String, Integer> numMap;
            double lng1;
            double lat1;
            double lng2 = StringUtil.parseDouble(lng);
            double lat2 = StringUtil.parseDouble(lat);
            List<StationIntro> stationList = new ArrayList<StationIntro>();
            for (Integer stationid : list) {
                record = CacheChargeHolder.getChargeStationById(stationid);
                if (record == null) {
                    continue;
                }
                lng1 = StringUtil.parseDouble(record.getLng());
                lat1 = StringUtil.parseDouble(record.getLat());

                StationIntro station = new StationIntro();
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

                // 设置充电点的空闲桩数，交直流桩数
                numMap = CacheChargeHolder.getPileNumByStationid(station.getStationid());
                station.setAcnum(numMap.get("acnum"));
                station.setDcnum(numMap.get("dcnum"));
                station.setIdlenum(numMap.get("idlenum"));
                stationList.add(station);
            }
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("recentstationlist", stationList);
            data.put("imgurl", RoleUtil.selectRuleByPrimaryKey(RoleUtil.IMG_URL));
            resultBean.setData(data);
        }
        return SUCCESS;
    }

    /**
     * 充电点分享
     * 
     * @return
     */
    public String stationshare() {
        String stationid = getParameter("stationid");
        if (StringUtil.isEmpty(stationid)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法");
            return SUCCESS;
        }
        stationid = decrypt(stationid);
        if (AppTool.isNotNumber(stationid)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法");
            return SUCCESS;
        }
        PobChargingStation record = CacheChargeHolder.getChargeStationById(Integer.valueOf(stationid));
        if (record != null) {
            StationIntro station = new StationIntro();
            station.setStationid(record.getId());
            station.setStationname(record.getStationName());
            station.setLng(record.getLng());
            station.setLat(record.getLat());
            station.setAddress(record.getAddress());
            station.setImg(record.getImg());
            station.setLinkphone(record.getLinkPhone());
            station.setFastnum(record.getFastNum() == null ? 0 : record.getFastNum());
            station.setSlownum(record.getSlowNum() == null ? 0 : record.getSlowNum());
            if (record.getOpenDay() != null) {
                station.setOpendaydesc(CacheSysHolder.getSysLinkName(LinkTypeEnum.OPEN_DAY.getValue(), record.getOpenDay().toString()));
            }
            if (record.getOpenTime() != null) {
                station.setOpentimedesc(CacheSysHolder.getSysLinkName(LinkTypeEnum.OPEN_TIME.getValue(), record.getOpenTime().toString()));
            }
            station.setScore(record.getScore());

            // 设置充电点的空闲桩数，交直流桩数
            Map<String, Integer> numMap = CacheChargeHolder.getPileNumByStationid(station.getStationid());
            station.setAcnum(numMap.get("acnum"));
            station.setDcnum(numMap.get("dcnum"));
            station.setIdlenum(numMap.get("idlenum"));
            this.getRequest().setAttribute("station", station);
        }
        this.getRequest().setAttribute("imgurl", RoleUtil.selectRuleByPrimaryKey(RoleUtil.IMG_URL));
        this.getRequest().setAttribute("iosurl", RoleUtil.selectRuleByPrimaryKey(RoleUtil.IOS_UPDATE_URL));
        this.getRequest().setAttribute("androidurl", RoleUtil.selectRuleByPrimaryKey(RoleUtil.APK_UPDATE_URL));
        return SUCCESS;
    }

    /**
     * 充电点分享页评论
     * 
     * @return
     */
    public String sharepilecomment() {
        String stationid = this.getParameter("stationid");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");

        if (AppTool.isNotNumber(stationid, pageindex, pagelimit)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("stationid", Integer.valueOf(stationid));

        Page page = this.returnPage(Integer.valueOf(pageindex), Integer.valueOf(pagelimit));
        params.put(Globals.PAGE, page);
        List<BusPileComment> commentlist = pobObjectService.selectStationCommentByPage(params);
        page.setRoot(commentlist);
        this.page = page;
        return SUCCESS;
    }

    /**
     * 关于我们
     * 
     * @return
     */
    public String aboutus() {
        Map<String, Object> data = new HashMap<String, Object>();
        String aboutusurl = RoleUtil.selectRuleByPrimaryKey(RoleUtil.APP_URL) + getRequest().getContextPath();
        data.put("aboutusurl", aboutusurl + ABOUTUS_URL);
        resultBean.setData(data);
        return SUCCESS;
    }

    public String usinfo() {
        return SUCCESS;
    }

    public void setPobObjectService(PobObjectService pobObjectService) {
        this.pobObjectService = pobObjectService;
    }

    public ResultBean getResultBean() {
        return resultBean;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setPiclist(List<File> piclist) {
        this.piclist = piclist;
    }

    public Page getPage() {
        return page;
    }

}
