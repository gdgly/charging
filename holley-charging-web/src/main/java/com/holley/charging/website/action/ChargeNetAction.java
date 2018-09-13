package com.holley.charging.website.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.util.CacheChargeHolder;
import com.holley.charging.model.def.PileRunStatusModel;
import com.holley.charging.model.pob.PobChargingStation;
import com.holley.charging.service.bussiness.DeviceService;
import com.holley.common.constants.Globals;
import com.holley.common.constants.charge.ChargeCurrentTypeEnum;
import com.holley.common.constants.charge.ChargePowerTypeEnum;
import com.holley.common.constants.charge.LinkTypeEnum;
import com.holley.common.dataobject.ComboxBean;
import com.holley.common.dataobject.Page;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.CacheSysHolder;

/****
 * 充电网络相关action
 * 
 * @author zdd
 */
public class ChargeNetAction extends BaseAction {

    private static final long        serialVersionUID = 1L;
    private DeviceService            deviceService;
    private List<PobChargingStation> stationList;
    private List<PileRunStatusModel> pileStatusList;
    private Page                     page;
    private Integer                  stationIdleNum;

    public String init() {
        List<ComboxBean> currentTypeList = ChargeCurrentTypeEnum.getBeanList();
        List<ComboxBean> powerTypeList = ChargePowerTypeEnum.getBeanList();

        this.getRequest().setAttribute(Globals.CURRENTMODULE, "chargenet");
        this.getRequest().setAttribute("currentTypeList", JSONArray.fromObject(currentTypeList).toString());
        this.getRequest().setAttribute("powerTypeList", JSONArray.fromObject(powerTypeList).toString());
        return SUCCESS;
    }

    /**
     * 根据地图显示范围查询充电点
     * 
     * @return
     */
    public String queryStationByRange() {
        String swlngstr = this.getParameter("swlng");// 西南经度
        String swlatstr = this.getParameter("swlat");// 西南纬度
        String nelngstr = this.getParameter("nelng");// 东北经度
        String nelatstr = this.getParameter("nelat");// 东北纬度
        String keyword = this.getParameter("keyword");// 东北纬度

        if (StringUtil.isEmpty(swlngstr) || StringUtil.isEmpty(swlatstr) || StringUtil.isEmpty(nelngstr) || StringUtil.isEmpty(nelatstr)) {
            this.success = false;
            return SUCCESS;
        }
        double swlng = StringUtil.parseDouble(swlngstr);
        double swlat = StringUtil.parseDouble(swlatstr);
        double nelng = StringUtil.parseDouble(nelngstr);
        double nelat = StringUtil.parseDouble(nelatstr);
        List<PobChargingStation> list = CacheChargeHolder.getChargeStationList();
        List<PobChargingStation> resultList = new ArrayList<PobChargingStation>();
        double lng;
        double lat;
        String stationname;
        String address;
        for (PobChargingStation record : list) {
            lng = StringUtil.parseDouble(record.getLng());
            lat = StringUtil.parseDouble(record.getLat());
            if (lng >= swlng && lng <= nelng && lat >= swlat && lat <= nelat) {
                if (StringUtil.isEmpty(keyword)) {
                    resultList.add(record);
                } else {
                    stationname = record.getStationName();
                    address = record.getAddress();
                    if ((stationname != null && stationname.indexOf(keyword) >= 0) || (address != null && address.indexOf(keyword) >= 0)) {
                        resultList.add(record);
                    }
                }
            }
        }
        stationList = resultList;
        this.success = true;
        return SUCCESS;
    }

    public String queryStationByRangeByPage() {
        String swlngstr = this.getParameter("swlng");// 西南经度
        String swlatstr = this.getParameter("swlat");// 西南纬度
        String nelngstr = this.getParameter("nelng");// 东北经度
        String nelatstr = this.getParameter("nelat");// 东北纬度
        String keyword = this.getParameter("keyword");// 东北纬度
        String pageIndex = this.getParameter("pageIndex");
        String pageLimit = this.getParameter("pageLimit");
        if (StringUtil.isEmpty(swlngstr) || StringUtil.isEmpty(swlatstr) || StringUtil.isEmpty(nelngstr) || StringUtil.isEmpty(nelatstr) || StringUtil.isEmpty(pageIndex)
            || StringUtil.isEmpty(pageLimit)) {
            this.success = false;
            return SUCCESS;
        }
        double swlng = StringUtil.parseDouble(swlngstr);
        double swlat = StringUtil.parseDouble(swlatstr);
        double nelng = StringUtil.parseDouble(nelngstr);
        double nelat = StringUtil.parseDouble(nelatstr);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("swlng", swlng);
        params.put("swlat", swlat);
        params.put("nelng", nelng);
        params.put("nelat", nelat);
        if (StringUtil.isNotEmpty(keyword)) {
            params.put("keyword", keyword);
        }
        int pageindex = Integer.valueOf(pageIndex);
        int pagelimit = Integer.valueOf(pageLimit);
        Page page = this.returnPage(pageindex, pagelimit);
        params.put(Globals.PAGE, page);
        List<PobChargingStation> list = deviceService.selectStationByParamByPage(params);
        if (list != null && list.size() > 0) {
            int rownum = pageindex == 1 ? 1 : ((pageindex - 1) * pagelimit + 1);
            for (PobChargingStation record : list) {
                record.setRownum(rownum);
                rownum++;
            }
        }
        page.setRoot(list);
        this.page = page;
        return SUCCESS;
    }

    public String queryStationIdleNum() {
        String stationid = this.getParameter("stationid");
        if (StringUtil.isEmpty(stationid)) {
            this.success = false;
            this.message = "请选择一个充电点!";
            return SUCCESS;
        }
        this.stationIdleNum = CacheChargeHolder.getPileIdleNumByStationid(Integer.valueOf(stationid));
        return SUCCESS;
    }

    /**
     * 根据关键字查询充电站
     * 
     * @return
     */
    public String queryStation() {
        String keyword = this.getParameter("keyword");
        String swlngstr = this.getParameter("swlng");// 西南经度
        String swlatstr = this.getParameter("swlat");// 西南纬度
        String nelngstr = this.getParameter("nelng");// 东北经度
        String nelatstr = this.getParameter("nelat");// 东北纬度

        List<PobChargingStation> list = CacheChargeHolder.getChargeStationList();
        List<PobChargingStation> resultList = new ArrayList<PobChargingStation>();
        // 如果有搜索关键字，则只根据关键字查询，否则根据可是区域经纬度查询
        if (StringUtil.isEmpty(keyword)) {
            double swlng = StringUtil.parseDouble(swlngstr);
            double swlat = StringUtil.parseDouble(swlatstr);
            double nelng = StringUtil.parseDouble(nelngstr);
            double nelat = StringUtil.parseDouble(nelatstr);
            double lng;
            double lat;
            for (PobChargingStation record : list) {
                lng = StringUtil.parseDouble(record.getLng());
                lat = StringUtil.parseDouble(record.getLat());
                if (lng >= swlng && lng <= nelng && lat >= swlat && lat <= nelat) {
                    resultList.add(record);
                }
            }
        } else {
            String stationname;
            String address;
            for (PobChargingStation record : list) {
                stationname = record.getStationName();
                address = record.getAddress();
                if ((stationname != null && stationname.indexOf(keyword) >= 0) || (address != null && address.indexOf(keyword) >= 0)) {
                    resultList.add(record);
                }
            }
        }
        stationList = resultList;
        this.success = true;
        return SUCCESS;
    }

    public String queryStationByPage() {
        String keyword = this.getParameter("keyword");
        String swlngstr = this.getParameter("swlng");// 西南经度
        String swlatstr = this.getParameter("swlat");// 西南纬度
        String nelngstr = this.getParameter("nelng");// 东北经度
        String nelatstr = this.getParameter("nelat");// 东北纬度
        String pageIndex = this.getParameter("pageIndex");
        String pageLimit = this.getParameter("pageLimit");

        Map<String, Object> params = new HashMap<String, Object>();

        // 如果有搜索关键字，则只根据关键字查询，否则根据可是区域经纬度查询
        if (StringUtil.isEmpty(keyword)) {
            double swlng = StringUtil.parseDouble(swlngstr);
            double swlat = StringUtil.parseDouble(swlatstr);
            double nelng = StringUtil.parseDouble(nelngstr);
            double nelat = StringUtil.parseDouble(nelatstr);
            params.put("swlng", swlng);
            params.put("swlat", swlat);
            params.put("nelng", nelng);
            params.put("nelat", nelat);
        } else {
            params.put("keyword", keyword);
        }

        int pageindex = Integer.valueOf(pageIndex);
        int pagelimit = Integer.valueOf(pageLimit);
        Page page = this.returnPage(pageindex, pagelimit);
        params.put(Globals.PAGE, page);
        List<PobChargingStation> list = deviceService.selectStationByParamByPage(params);
        if (list != null && list.size() > 0) {
            int rownum = pageindex == 1 ? 1 : ((pageindex - 1) * pagelimit + 1);
            for (PobChargingStation record : list) {
                record.setRownum(rownum);
                rownum++;
            }
        }
        page.setRoot(list);
        this.page = page;
        this.success = true;
        return SUCCESS;
    }

    /**
     * 根据关键字查询充电站
     * 
     * @return
     */
    public String queryPileStatus() {
        String stationid = this.getParameter("stationid");
        if (StringUtil.isEmpty(stationid)) {
            this.success = false;
            this.message = "请选择一个充电点!";
            return SUCCESS;
        }

        pileStatusList = CacheChargeHolder.getPileStatusListByStationid(Integer.valueOf(stationid));
        return SUCCESS;
    }

    private String getPayWay(String payWay) {
        String desc = "";
        if (payWay != null) {
            String[] ways = payWay.split(",");
            String temp = "";
            for (String way : ways) {
                temp = CacheSysHolder.getSysLinkName(LinkTypeEnum.PAY_WAY.getValue(), way);
                if (StringUtil.isNotEmpty(temp)) {
                    desc += " " + temp + " ,";
                }
            }
            if (desc.length() > 0) {
                desc = desc.substring(0, desc.length() - 1);
            }
        }
        return desc;
    }

    public boolean isSuccess() {
        return success;
    }

    public Page getPage() {
        return page;
    }

    public Integer getStationIdleNum() {
        return stationIdleNum;
    }

    public List<PobChargingStation> getStationList() {
        return stationList;
    }

    public List<PileRunStatusModel> getPileStatusList() {
        return pileStatusList;
    }

    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

}
