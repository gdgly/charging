package com.holley.charging.serviceimpl.bussiness;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.holley.charging.dao.bus.BusChargeRuleMapper;
import com.holley.charging.dao.bus.BusPileApplyMapper;
import com.holley.charging.dao.bus.BusPileChargeruleMapper;
import com.holley.charging.dao.bus.BusPileModelMapper;
import com.holley.charging.dao.dcs.DcsAlarmEventsMapper;
import com.holley.charging.dao.dcs.DcsHisycMapper;
import com.holley.charging.dao.pob.PobChargingPileMapper;
import com.holley.charging.dao.pob.PobChargingStationMapper;
import com.holley.charging.dao.pob.PobChargingTempPileMapper;
import com.holley.charging.dao.pob.PobChargingTempStationMapper;
import com.holley.charging.model.bus.BusChargeRule;
import com.holley.charging.model.bus.BusChargeRuleExample;
import com.holley.charging.model.bus.BusPileApply;
import com.holley.charging.model.bus.BusPileChargerule;
import com.holley.charging.model.bus.BusPileChargeruleExample;
import com.holley.charging.model.bus.BusPileModel;
import com.holley.charging.model.bus.BusPileModelExample;
import com.holley.charging.model.dcs.DcsHisyc;
import com.holley.charging.model.dcs.DcsHisycExample;
import com.holley.charging.model.def.ChargeRuleModel;
import com.holley.charging.model.def.DcsAlarmEventsModel;
import com.holley.charging.model.def.PileTypeNumModel;
import com.holley.charging.model.pob.PobChargingPile;
import com.holley.charging.model.pob.PobChargingPileExample;
import com.holley.charging.model.pob.PobChargingStation;
import com.holley.charging.model.pob.PobChargingStationExample;
import com.holley.charging.model.pob.PobChargingTempPile;
import com.holley.charging.model.pob.PobChargingTempPileExample;
import com.holley.charging.model.pob.PobChargingTempStation;
import com.holley.charging.model.pob.PobChargingTempStationExample;
import com.holley.charging.service.bussiness.DeviceService;
import com.holley.common.cache.charging.CacheKeyProvide;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.Globals;
import com.holley.common.constants.charge.ChargePowerTypeEnum;
import com.holley.common.constants.charge.DeviceVerifyStatusEnum;
import com.holley.common.constants.charge.ImgTypeEnum;
import com.holley.common.constants.charge.RequestTypeEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.util.DateUtil;
import com.holley.web.common.util.ImageUtil;

public class DeviceServiceImpl implements DeviceService {

    private PobChargingStationMapper     pobChargingStationMapper;
    private PobChargingPileMapper        pobChargingPileMapper;
    private PobChargingTempStationMapper pobChargingTempStationMapper;
    private PobChargingTempPileMapper    pobChargingTempPileMapper;
    private BusChargeRuleMapper          busChargeRuleMapper;
    private DcsAlarmEventsMapper         dcsAlarmEventsMapper;
    private BusPileModelMapper           busPileModelMapper;
    private BusPileChargeruleMapper      busPileChargeruleMapper;
    private BusPileApplyMapper           busPileApplyMapper;
    private DcsHisycMapper               dcsHisycMapper;

    public List<PobChargingStation> selectStationByPage(Map<String, Object> param) {
        return pobChargingStationMapper.selectStationByPage(param);
    }

    public List<PobChargingPile> selectPileByPage(Map<String, Object> param) {
        return pobChargingPileMapper.selectPileByPage(param);
    }

    @Override
    public int insertPobChargingTempStationSelective(PobChargingTempStation pobChargingTempStation) {
        return pobChargingTempStationMapper.insertSelective(pobChargingTempStation);
    }

    @Override
    public int insertPobChargingTempPileSelective(PobChargingTempPile pobChargingTempPile) {
        return pobChargingTempPileMapper.insertSelective(pobChargingTempPile);
    }

    @Override
    public int updatePobChargingTempStationByPrimaryKeySelective(PobChargingTempStation record) {
        return pobChargingTempStationMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateChargingTempPileByPKSelective(PobChargingTempPile record) {
        return pobChargingTempPileMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int countPobChargingTempStationByExample(PobChargingTempStationExample example) {

        return pobChargingTempStationMapper.countByExample(example);
    }

    @Override
    public int countPobChargingStationByExample(PobChargingStationExample example) {

        return pobChargingStationMapper.countByExample(example);
    }

    @Override
    public PobChargingStation selectPobChargingStationByExample(PobChargingStationExample example) {
        List<PobChargingStation> list = null;
        if (example == null) {
            list = pobChargingStationMapper.selectByExample(null);
        } else {
            list = pobChargingStationMapper.selectByExample(example);
        }
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else return null;
    }

    @Override
    public List<PobChargingTempStation> selectPobChargingTempStationByExample(PobChargingTempStationExample example) {
        List<PobChargingTempStation> list = pobChargingTempStationMapper.selectByExample(example);
        return list;
    }

    @Override
    public List<PobChargingTempStation> selectTempStationByPage(Map<String, Object> param) {
        return pobChargingTempStationMapper.selectTempStationByPage(param);
    }

    @Override
    public List<PobChargingTempPile> selectTempPileByPage(Map<String, Object> param) {
        return pobChargingTempPileMapper.selectTempPileByPage(param);
    }

    @Override
    public List<PobChargingPile> selectPobChargingPileByExample(PobChargingPileExample example) {
        List<PobChargingPile> list = pobChargingPileMapper.selectByExample(example);
        return list;
    }

    @Override
    public List<PobChargingTempPile> selectPobChargingTempPileByExample(PobChargingTempPileExample example) {
        List<PobChargingTempPile> list = pobChargingTempPileMapper.selectByExample(example);
        return list;
    }

    @Override
    public PobChargingPile selectPileByMap(Map<String, Object> param) {
        return pobChargingPileMapper.selectPileByMap(param);
    }

    @Override
    public List<PobChargingPile> selectChargePileByPage(Map<String, Object> params) {
        return pobChargingPileMapper.selectChargePileByPage(params);
    }

    @Override
    public int updatePobChargingTempPileByPrimaryKeySelective(PobChargingTempPile record, PobChargingTempPileExample example) {
        return pobChargingTempPileMapper.updateByExampleSelective(record, example);
    }

    @Override
    public List<PobChargingTempStation> selectTempStationVerifyByPage(Map<String, Object> params) {
        return pobChargingTempStationMapper.selectTempStationVerifyByPage(params);
    }

    @Override
    public List<PobChargingTempPile> selectTempPileVerifyByPage(Map<String, Object> params) {
        return pobChargingTempPileMapper.selectTempPileVerifyByPage(params);
    }

    @Override
    public List<PobChargingStation> selectStationByParamByPage(Map<String, Object> param) {
        return pobChargingStationMapper.selectStationByParamByPage(param);
    }

    @Override
    public PobChargingTempStation selectTempStationByPrimaryKey(Integer id) {
        return pobChargingTempStationMapper.selectByPrimaryKey(id);
    }

    @Override
    public PobChargingTempPile selectTempPileByPrimaryKey(Integer id) {
        return pobChargingTempPileMapper.selectByPrimaryKey(id);
    }

    @Override
    public int insertChargingStation(PobChargingStation record) {
        return pobChargingStationMapper.insert(record);
    }

    @Override
    public int updateChargingStationByPK(PobChargingStation record) {
        return pobChargingStationMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateChargingStationByPKSelective(PobChargingStation record) {
        return pobChargingStationMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateTempPileByExampleSelective(PobChargingTempPile record, PobChargingTempPileExample example) {
        return pobChargingTempPileMapper.updateByExampleSelective(record, example);
    }

    @Override
    public String insertOrUpdateChargingStation(PobChargingTempStation tempStation, PobChargingStation station, DeviceVerifyStatusEnum statusEnum, String servletRealPath)
                                                                                                                                                                          throws Exception {
        String msg = "success";
        PobChargingTempStation tempRecord = new PobChargingTempStation();
        if (statusEnum.getValue() == DeviceVerifyStatusEnum.PASSED.getValue()) {
            // 把审核通过的信息更新到pob_charging_station表中
            if (tempStation.getRequestType().intValue() == RequestTypeEnum.ADDSTATIONPILE.getValue()) {
                pobChargingStationMapper.insert(station);
                Integer stationid = station.getId();

                // 更新充电点审核记录的realstationid
                tempRecord.setRealStationId(stationid);

                // 更新充电桩审核表中该充电点下充电桩的realstationid
                PobChargingTempPile record = new PobChargingTempPile();
                record.setRealStationId(stationid);
                PobChargingTempPileExample emp = new PobChargingTempPileExample();
                PobChargingTempPileExample.Criteria cr = emp.createCriteria();
                cr.andTempStationIdEqualTo(tempStation.getId());
                pobChargingTempPileMapper.updateByExampleSelective(record, emp);
            } else {
                pobChargingStationMapper.updateByPrimaryKeySelective(station);
            }
            if (tempStation.getImg() != null) {
                String imgPath = tempStation.getImg();
                imgPath = imgPath.replaceAll(Globals.IMG_DATA_FILE + "/", "");
                File file = new File(servletRealPath + "/" + imgPath);
                Map<String, Object> imgMap = ImageUtil.uploadImg(file, station.getId(), ImgTypeEnum.STATION_IMG, servletRealPath);
                msg = (String) imgMap.get("msg");
                if (msg.equals("success")) {
                    PobChargingStation cs = new PobChargingStation();
                    cs.setId(station.getId());
                    cs.setImg((String) imgMap.get("url"));
                    pobChargingStationMapper.updateByPrimaryKeySelective(cs);
                }
            }
            // 设置缓存里充电点的更新时间
            ChargingCacheUtil.setUpdateTime(CacheKeyProvide.KEY_STATION_UPDATETIME, station.getUpdateTime());
        }
        // 更新表pob_charging_temp_station的审核信息
        tempRecord.setId(tempStation.getId());
        tempRecord.setValidStatus(statusEnum.getShortValue());
        tempRecord.setValidTime(new Date());
        pobChargingTempStationMapper.updateByPrimaryKeySelective(tempRecord);
        return msg;
    }

    @Override
    public int insertChargingPile(PobChargingPile record) {
        return pobChargingPileMapper.insert(record);
    }

    @Override
    public int updateChargingPileByPK(PobChargingPile record) {
        return pobChargingPileMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateChargingPileByPKSelective(PobChargingPile record) {
        return pobChargingPileMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public boolean insertOrUpdateChargingPile(PobChargingTempPile tempPile, PobChargingPile pile, BusPileChargerule rule, DeviceVerifyStatusEnum statusEnum) throws Exception {
        int requestType = tempPile.getRequestType().intValue();
        if (statusEnum.getValue() == DeviceVerifyStatusEnum.PASSED.getValue()) {
            // 把审核通过的信息更新到pob_charging_station表中
            if (RequestTypeEnum.ADDSTATIONPILE.getValue() == requestType || RequestTypeEnum.ADDPILE.getValue() == requestType) {
                pobChargingPileMapper.insert(pile);
                Integer pileid = pile.getId();

                // 更新充电桩审核记录的realpileid
                tempPile.setRealPileId(pileid);
                rule.setPileId(pileid);
            } else {
                pobChargingPileMapper.updateByPrimaryKeySelective(pile);

                // 修改充电点，且有修改费用时，则更新费用中间表中的未到有效时间的记录改成无效
                if (rule != null) {
                    // 更新桩的费用规则中间表，过滤条件：未到有效时间，且状态为生效的记录
                    BusPileChargerule record = new BusPileChargerule();
                    record.setStatus(WhetherEnum.NO.getShortValue());

                    BusPileChargeruleExample emp = new BusPileChargeruleExample();
                    BusPileChargeruleExample.Criteria cr = emp.createCriteria();
                    cr.andPileIdEqualTo(pile.getId());
                    cr.andStatusEqualTo(WhetherEnum.YES.getShortValue());
                    if (!DateUtil.DateToShortStr(rule.getActiveTime()).equals(DateUtil.DateToShortStr(new Date()))) {
                        cr.andActiveTimeGreaterThan(new Date());
                    }
                    busPileChargeruleMapper.updateByExampleSelective(record, emp);
                }
            }
            // 插入桩的费用规则中间表
            if (rule != null) {
                busPileChargeruleMapper.insertSelective(rule);
            }
            // 更新充电点快桩、慢桩数量
            updateStationPileNum(pile.getStationId());
            // 更新缓存里充电桩的更新时间
            ChargingCacheUtil.setUpdateTime(CacheKeyProvide.KEY_PILE_UPDATETIME, pile.getUpdateTime());
        }
        pobChargingTempPileMapper.updateByPrimaryKeySelective(tempPile);
        return true;
    }

    /**
     * 更新充电点快充、慢充桩数量
     * 
     * @param stationid
     */
    private void updateStationPileNum(Integer stationid) {
        List<PileTypeNumModel> numList = pobChargingPileMapper.selectPileTypeNumByStationId(stationid);
        int slowNum = 0;
        int fastNum = 0;
        ChargePowerTypeEnum powerTypeEnum;
        for (PileTypeNumModel item : numList) {
            if (item.getPiletype() == null || item.getPilenum() == null) continue;
            powerTypeEnum = ChargePowerTypeEnum.getEnmuByValue(item.getPiletype().intValue());
            if (powerTypeEnum == null) continue;
            if (ChargePowerTypeEnum.SLOW.getValue() == powerTypeEnum.getValue()) {
                slowNum += item.getPilenum();
            } else {
                fastNum += item.getPilenum();
            }
        }
        PobChargingStation station = new PobChargingStation();
        station.setId(stationid);
        station.setSlowNum(slowNum);
        station.setFastNum(fastNum);
        station.setUpdateTime(new Date());
        if (pobChargingStationMapper.updateByPrimaryKeySelective(station) > 0) {
            // 设置缓存里充电点的更新时间
            ChargingCacheUtil.setUpdateTime(CacheKeyProvide.KEY_STATION_UPDATETIME, station.getUpdateTime());
        }
    }

    public int insertPileChargeruleSelective(BusPileChargerule record) {
        return busPileChargeruleMapper.insertSelective(record);
    }

    @Override
    public List<BusChargeRule> selectBusChargeRuleByExample(BusChargeRuleExample example) {
        return busChargeRuleMapper.selectByExample(example);
    }

    @Override
    public List<BusPileChargerule> selectBusPileChargeruleByExample(BusPileChargeruleExample emp) {
        return busPileChargeruleMapper.selectByExample(emp);
    }

    @Override
    public BusPileChargerule selectPileChargeRuleByParams(Map<String, Object> params) {
        return busPileChargeruleMapper.selectPileChargeRuleByParams(params);
    }

    @Override
    public Map<String, Object> insertAndUpdateTempStation(PobChargingTempStation tempStation, File img, String servletRealPath) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        PobChargingStationExample pobStationEmp = new PobChargingStationExample();
        PobChargingStationExample.Criteria pobStationCr = pobStationEmp.createCriteria();
        pobStationCr.andStationNameEqualTo(tempStation.getStationName());
        int count1 = pobChargingStationMapper.countByExample(pobStationEmp);// 对比正式充电点

        List<Short> values = new ArrayList<Short>();
        values.add(DeviceVerifyStatusEnum.UNVERIFIED.getShortValue());
        values.add(DeviceVerifyStatusEnum.VERIFYING.getShortValue());
        PobChargingTempStationExample pobTempStationEmp = new PobChargingTempStationExample();
        PobChargingTempStationExample.Criteria pobTempStationCr = pobTempStationEmp.createCriteria();
        pobTempStationCr.andStationNameEqualTo(tempStation.getStationName());
        pobTempStationCr.andValidStatusIn(values);
        int count2 = pobChargingTempStationMapper.countByExample(pobTempStationEmp);// 对比正在审核中或待审的点
        if ((count1 + count2) > 0) {
            map.put("msg", "充电点名称与其他充电点重复！！");
            return map;
        }
        pobChargingTempStationMapper.insertSelective(tempStation);
        int newStationId = tempStation.getId();
        if (newStationId > 0) {
            map = ImageUtil.uploadImg(img, tempStation.getId(), ImgTypeEnum.STATION_TEMP_IMG, servletRealPath);
            if ("success".equals(map.get("msg"))) {
                PobChargingTempStation newPobTempStation = new PobChargingTempStation();
                newPobTempStation.setImg(map.get("url").toString());
                newPobTempStation.setUpdateTime(new Date());
                newPobTempStation.setId(newStationId);
                pobChargingTempStationMapper.updateByPrimaryKeySelective(newPobTempStation);
                map.put("newStationId", newStationId);
            }

        } else {
            map.put("msg", "添加失败！！");
        }
        return map;
    }

    @Override
    public Map<String, Object> insertAndUpdateStation(PobChargingStation tempStation, File img, String servletRealPath) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", "success");
        PobChargingStationExample pobStationEmp = new PobChargingStationExample();
        PobChargingStationExample.Criteria pobStationCr = pobStationEmp.createCriteria();
        pobStationCr.andStationNameEqualTo(tempStation.getStationName());
        int count1 = pobChargingStationMapper.countByExample(pobStationEmp);// 对比正式充电点
        if ((count1) > 0) {
            map.put("msg", "充电点名称与其他充电点重复！！");
            return map;
        }
        pobChargingStationMapper.insertSelective(tempStation);
        int newStationId = tempStation.getId();
        if (newStationId > 0) {
            PobChargingStation newPobStation = new PobChargingStation();
            if (img != null) {
                map = ImageUtil.uploadImg(img, tempStation.getId(), ImgTypeEnum.STATION_IMG, servletRealPath);
                if ("success".equals(map.get("msg"))) {
                    newPobStation.setImg(map.get("url").toString());
                }
            }
            newPobStation.setUpdateTime(new Date());
            newPobStation.setId(newStationId);
            pobChargingStationMapper.updateByPrimaryKeySelective(newPobStation);
            map.put("newStationId", newStationId);
            // 设置缓存里充电点的更新时间
            ChargingCacheUtil.setUpdateTime(CacheKeyProvide.KEY_STATION_UPDATETIME, tempStation.getUpdateTime());

        } else {
            map.put("msg", "添加失败！！");
        }
        return map;
    }

    @Override
    public Map<String, Object> updatePobStation(PobChargingStation tempStation, PobChargingStation oldStation, File img, String servletRealPath) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", "success");
        if (img != null) {
            map = ImageUtil.uploadImg(img, tempStation.getId(), ImgTypeEnum.STATION_IMG, servletRealPath);
            if ("success".equals(map.get("msg"))) {
                tempStation.setImg(map.get("url").toString());
            }
        }
        int status = pobChargingStationMapper.updateByPrimaryKeySelective(tempStation);
        // if (!oldStation.getStationToType().equals(tempStation.getStationToType())) {
        // PobChargingPileExample pileEmp = new PobChargingPileExample();
        // PobChargingPileExample.Criteria pileCr = pileEmp.createCriteria();
        // pileCr.andStationIdEqualTo(oldStation.getId());
        // PobChargingPile newPile = new PobChargingPile();
        // newPile.setPileToType(tempStation.getStationToType());
        // newPile.setUpdateTime(tempStation.getUpdateTime());
        // status += pobChargingPileMapper.updateByExampleSelective(newPile, pileEmp);
        // ChargingCacheUtil.setUpdateTime(CacheKeyProvide.KEY_PILE_UPDATETIME, tempStation.getUpdateTime());
        // }
        if (status <= 0) {
            map.put("msg", "修改失败！！");
        } else {
            // 设置缓存里充电点的更新时间
            ChargingCacheUtil.setUpdateTime(CacheKeyProvide.KEY_STATION_UPDATETIME, tempStation.getUpdateTime());

        }
        return map;
    }

    @Override
    public Map<String, Object> insertAndUpdateTempStationForEdit(PobChargingTempStation oldEditTempStation, PobChargingTempStation tempStation, File img, String servletRealPath)
                                                                                                                                                                                 throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", "success");
        if (oldEditTempStation != null) {
            pobChargingTempStationMapper.updateByPrimaryKeySelective(oldEditTempStation);
        }
        pobChargingTempStationMapper.insertSelective(tempStation);
        int newStationId = tempStation.getId();
        if (img != null && newStationId > 0) {
            map = ImageUtil.uploadImg(img, tempStation.getId(), ImgTypeEnum.STATION_TEMP_IMG, servletRealPath);
            if ("success".equals(map.get("msg"))) {
                PobChargingTempStation newPobTempStation = new PobChargingTempStation();
                newPobTempStation.setImg(map.get("url").toString());
                newPobTempStation.setUpdateTime(new Date());
                newPobTempStation.setId(newStationId);
                pobChargingTempStationMapper.updateByPrimaryKeySelective(newPobTempStation);
                map.put("newStationId", newStationId);
            }
        }

        return map;
    }

    @Override
    public Map<String, Object> insertAndUpdateTempPile(PobChargingTempPile tempPile, File doc, String docType, String servletRealPath) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", "success");
        pobChargingTempPileMapper.insertSelective(tempPile);
        int newPileId = tempPile.getId();
        if (doc != null && newPileId > 0) {
            map = ImageUtil.uploadDoc(doc, newPileId, docType, servletRealPath);
            if ("success".equals(map.get("msg"))) {
                PobChargingTempPile newPobTempPile = new PobChargingTempPile();
                newPobTempPile.setDoc(map.get("url").toString());
                newPobTempPile.setUpdateTime(new Date());
                newPobTempPile.setId(newPileId);
                pobChargingTempPileMapper.updateByPrimaryKeySelective(newPobTempPile);
            }
        }
        return map;
    }

    @Override
    public Map<String, Object> insertAndUpdateTempPileForEdit(PobChargingTempPile oldEditTempPile, PobChargingTempPile tempPile, File doc, String docType, String servletRealPath)
                                                                                                                                                                                  throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", "success");
        if (oldEditTempPile != null) {
            pobChargingTempPileMapper.updateByPrimaryKeySelective(oldEditTempPile);
        }
        pobChargingTempPileMapper.insertSelective(tempPile);
        int newPileId = tempPile.getId();
        if (doc != null && newPileId > 0) {
            map = ImageUtil.uploadDoc(doc, newPileId, docType, servletRealPath);
            if ("success".equals(map.get("msg"))) {
                PobChargingTempPile newPobTempPile = new PobChargingTempPile();
                newPobTempPile.setDoc(map.get("url").toString());
                newPobTempPile.setUpdateTime(new Date());
                newPobTempPile.setId(newPileId);
                pobChargingTempPileMapper.updateByPrimaryKeySelective(newPobTempPile);
            }
        }
        return map;
    }

    @Override
    public List<DcsAlarmEventsModel> selectDcsAlarmEventsModelByPage(Map<String, Object> param) {
        return dcsAlarmEventsMapper.selectDcsAlarmEventsModelByPage(param);
    }

    @Override
    public List<BusPileModel> selectPileModelByExample(BusPileModelExample example) {
        return busPileModelMapper.selectByExample(example);
    }

    @Override
    public List<BusPileModel> selectPileModelByPage(Map<String, Object> params) {
        return busPileModelMapper.selectPileModelByPage(params);
    }

    @Override
    public BusPileModel selectPileModelByPK(Integer id) {
        return busPileModelMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updatePileModelByPKSelective(BusPileModel record) {
        return busPileModelMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int insertPileModelSelective(BusPileModel record) {
        return busPileModelMapper.insertSelective(record);
    }

    @Override
    public ChargeRuleModel selectChargeRuleModelByMap(Map<String, Object> param) {
        return busChargeRuleMapper.selectChargeRuleModelByMap(param);
    }

    @Override
    public int insertPileApply(BusPileApply record) {
        return busPileApplyMapper.insert(record);
    }

    @Override
    public int insertPileApplySelective(BusPileApply record) {
        return busPileApplyMapper.insertSelective(record);
    }

    @Override
    public int updatePileApplyByPKSelective(BusPileApply record) {
        return busPileApplyMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public BusPileApply selectPileApplyByPK(Integer id) {
        return busPileApplyMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<BusPileApply> selectPileApplyByParams(Map<String, Object> params) {
        return busPileApplyMapper.selectPileApplyByParams(params);
    }

    @Override
    public List<PobChargingStation> selectStationByExample(PobChargingStationExample example) {
        return pobChargingStationMapper.selectByExample(example);
    }

    @Override
    public List<PobChargingPile> exportPile(Map<String, Object> param) {
        return this.pobChargingPileMapper.exportPile(param);
    }

    @Override
    public List<DcsAlarmEventsModel> exportAlarmEvents(Map<String, Object> param) {
        return dcsAlarmEventsMapper.exportAlarmEvents(param);
    }

    @Override
    public PobChargingStation selectChargeStationByPrimaryKey(Integer id) {

        return pobChargingStationMapper.selectByPrimaryKey(id);
    }

    @Override
    public PobChargingPile selectPileByPrimaryKey(Integer id) {
        return pobChargingPileMapper.selectByPrimaryKey(id);
    }

    @Override
    public int countPobChargingPileByExample(PobChargingPileExample example) {
        return pobChargingPileMapper.countByExample(example);
    }

    @Override
    public int countPobChargingTeampPileByExample(PobChargingTempPileExample example) {
        return pobChargingTempPileMapper.countByExample(example);
    }

    @Override
    public Map<String, Object> insertAndUpdatePile(PobChargingTempPile tempPile) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        PobChargingStation station = pobChargingStationMapper.selectByPrimaryKey(tempPile.getRealStationId());
        Integer fastNo = station.getFastNum() == null ? 0 : station.getFastNum();
        Integer slowNo = station.getSlowNum() == null ? 0 : station.getSlowNum();
        map.put("msg", "success");
        PobChargingPile newPile = new PobChargingPile();
        BusPileChargerule newRule = new BusPileChargerule();

        BusPileModel model = busPileModelMapper.selectByPrimaryKey(tempPile.getPileModel());
        tempPile.setIntfType(model.getStandard());// 设置接口类型
        BeanUtils.copyProperties(newPile, tempPile);
        BeanUtils.copyProperties(newRule, tempPile);
        newPile.setStationId(tempPile.getRealStationId());
        newRule.setChargeruleId(tempPile.getFeeRule());
        newPile.setUpdateTime(tempPile.getUpdateTime());
        newRule.setAddTime(tempPile.getUpdateTime());
        pobChargingPileMapper.insertSelective(newPile);
        int pileId = newPile.getId();
        if (pileId > 0) {
            newRule.setPileId(pileId);
            busPileChargeruleMapper.insertSelective(newRule);
            PobChargingStation newStation = new PobChargingStation();
            newStation.setId(tempPile.getRealStationId());
            newStation.setUpdateTime(tempPile.getUpdateTime());
            short pileType = tempPile.getPileType().shortValue();
            if ((ChargePowerTypeEnum.FAST.getShortValue().shortValue() == pileType) || ChargePowerTypeEnum.OVERSPEED.getShortValue().shortValue() == pileType) {
                newStation.setFastNum(++fastNo);
            } else if (ChargePowerTypeEnum.SLOW.getShortValue().shortValue() == tempPile.getPileType().shortValue()) {
                newStation.setSlowNum(++slowNo);
            }
            pobChargingStationMapper.updateByPrimaryKeySelective(newStation);// 更新快慢桩数量
            // 设置缓存里充电点的更新时间
            ChargingCacheUtil.setUpdateTime(CacheKeyProvide.KEY_PILE_UPDATETIME, tempPile.getUpdateTime());
            ChargingCacheUtil.setUpdateTime(CacheKeyProvide.KEY_STATION_UPDATETIME, tempPile.getUpdateTime());
            ChargingCacheUtil.setUpdateTime(CacheKeyProvide.KEY_PILE_CHARGE_RULE_UPDATETIME, tempPile.getUpdateTime());
        }

        return map;
    }

    @Override
    public Map<String, Object> updatePile(PobChargingTempPile tempPile) throws Exception {
        int stationId = tempPile.getRealStationId();
        int pileId = tempPile.getRealPileId();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", "success");
        PobChargingPile pile = pobChargingPileMapper.selectByPrimaryKey(pileId);
        PobChargingPile newPile = new PobChargingPile();
        BeanUtils.copyProperties(newPile, tempPile);
        newPile.setId(pileId);
        newPile.setUpdateTime(tempPile.getUpdateTime());
        if (tempPile.getFeeRule() > 0) {
            BusPileChargerule newRule = new BusPileChargerule();
            BeanUtils.copyProperties(newRule, tempPile);
            newRule.setPileId(pileId);
            newRule.setChargeruleId(tempPile.getFeeRule());
            newRule.setAddTime(tempPile.getUpdateTime());
            map.put("pileid", pileId);
            map.put("status", 1);
            map.put("isactive", 2);
            BusPileChargerule rule = busPileChargeruleMapper.selectPileChargeRuleByParams(map);
            if (rule != null) {
                int ruleId = rule.getChargeruleId();
                int newRuleId = newRule.getChargeruleId();
                Date activeTime = rule.getActiveTime();
                Date newActiveTime = newRule.getActiveTime();
                BigDecimal ruleChargeFee = rule.getChargeFee();
                BigDecimal newRuleChargeFee = newRule.getChargeFee() == null ? BigDecimal.ZERO : newRule.getChargeFee();
                BigDecimal ruleParkFee = rule.getParkFee();
                BigDecimal newRuleParkFee = newRule.getParkFee();
                BigDecimal ruleServiceFee = rule.getServiceFee();
                BigDecimal newRuleServiceFee = newRule.getServiceFee();

                if (ruleId != newRuleId || !activeTime.equals(newActiveTime) || !(ruleChargeFee.compareTo(newRuleChargeFee) == 0) || !(ruleParkFee.compareTo(newRuleParkFee) == 0)
                    || !(ruleServiceFee.compareTo(newRuleServiceFee) == 0)) {
                    rule.setStatus((short) 2);
                    busPileChargeruleMapper.updateByPrimaryKeySelective(rule);
                    busPileChargeruleMapper.insertSelective(newRule);
                }
            } else {
                busPileChargeruleMapper.insertSelective(newRule);
            }
            ChargingCacheUtil.setUpdateTime(CacheKeyProvide.KEY_PILE_CHARGE_RULE_UPDATETIME, tempPile.getUpdateTime());
        } else {
            newPile.setFeeRule(null);
            newPile.setChargeFee(null);
            newPile.setParkFee(null);
            newPile.setServiceFee(null);
        }
        // 快慢充类型修改需改变充电点快慢充数量
        if (pile.getPileType().shortValue() != tempPile.getPileType().shortValue()) {
            short pileType = tempPile.getPileType().shortValue();
            PobChargingStation station = pobChargingStationMapper.selectByPrimaryKey(stationId);
            PobChargingStation newStation = new PobChargingStation();
            newStation.setId(stationId);
            newStation.setUpdateTime(tempPile.getUpdateTime());
            if (pileType == ChargePowerTypeEnum.FAST.getShortValue().shortValue() || pileType == ChargePowerTypeEnum.OVERSPEED.getShortValue().shortValue()) {
                newStation.setFastNum(station.getFastNum() + 1);
                newStation.setSlowNum(station.getSlowNum() - 1);
            } else if (pileType == ChargePowerTypeEnum.SLOW.getShortValue().shortValue()) {
                newStation.setFastNum(station.getFastNum() - 1);
                newStation.setSlowNum(station.getSlowNum() + 1);
            }
            pobChargingStationMapper.updateByPrimaryKeySelective(newStation);
            // 设置缓存里充电点的更新时间
            ChargingCacheUtil.setUpdateTime(CacheKeyProvide.KEY_STATION_UPDATETIME, tempPile.getUpdateTime());
        }
        ChargingCacheUtil.setUpdateTime(CacheKeyProvide.KEY_PILE_UPDATETIME, tempPile.getUpdateTime());
        pobChargingPileMapper.updateByPrimaryKeySelective(newPile);

        return map;
    }

    @Override
    public List<DcsHisyc> selectDcsHisycByExample(DcsHisycExample example) {
        return dcsHisycMapper.selectByExample(example);
    }

    // set
    public void setPobChargingStationMapper(PobChargingStationMapper pobChargingStationMapper) {
        this.pobChargingStationMapper = pobChargingStationMapper;
    }

    public void setPobChargingPileMapper(PobChargingPileMapper pobChargingPileMapper) {
        this.pobChargingPileMapper = pobChargingPileMapper;
    }

    public void setPobChargingTempStationMapper(PobChargingTempStationMapper pobChargingTempStationMapper) {
        this.pobChargingTempStationMapper = pobChargingTempStationMapper;
    }

    public void setPobChargingTempPileMapper(PobChargingTempPileMapper pobChargingTempPileMapper) {
        this.pobChargingTempPileMapper = pobChargingTempPileMapper;
    }

    public void setBusChargeRuleMapper(BusChargeRuleMapper busChargeRuleMapper) {
        this.busChargeRuleMapper = busChargeRuleMapper;
    }

    public void setDcsAlarmEventsMapper(DcsAlarmEventsMapper dcsAlarmEventsMapper) {
        this.dcsAlarmEventsMapper = dcsAlarmEventsMapper;
    }

    public void setBusPileModelMapper(BusPileModelMapper busPileModelMapper) {
        this.busPileModelMapper = busPileModelMapper;
    }

    public void setBusPileChargeruleMapper(BusPileChargeruleMapper busPileChargeruleMapper) {
        this.busPileChargeruleMapper = busPileChargeruleMapper;
    }

    public void setBusPileApplyMapper(BusPileApplyMapper busPileApplyMapper) {
        this.busPileApplyMapper = busPileApplyMapper;
    }

    public void setDcsHisycMapper(DcsHisycMapper dcsHisycMapper) {
        this.dcsHisycMapper = dcsHisycMapper;
    }

    @Override
    public int deletePileByPrimaryKey(Integer id) {
        return pobChargingPileMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteAndUpdatePile(int pileId) {
        int result = 0;
        PobChargingPile pile = pobChargingPileMapper.selectByPrimaryKey(pileId);
        PobChargingStation station = pobChargingStationMapper.selectByPrimaryKey(pile.getStationId());
        PobChargingStation newStation = new PobChargingStation();
        newStation.setId(station.getId());
        if (ChargePowerTypeEnum.FAST.getShortValue().equals(pile.getPileType())) {// 快充
            newStation.setFastNum(station.getFastNum() - 1);
        } else if (ChargePowerTypeEnum.SLOW.getShortValue().equals(pile.getPileType())) {
            newStation.setSlowNum(station.getSlowNum() - 1);
        }
        newStation.setUpdateTime(new Date());
        result += pobChargingPileMapper.deleteByPrimaryKey(pileId);
        result += pobChargingStationMapper.updateByPrimaryKeySelective(newStation);
        ChargingCacheUtil.setDelPile(newStation.getId().toString());
        ChargingCacheUtil.setUpdateTime(CacheKeyProvide.KEY_STATION_UPDATETIME, newStation.getUpdateTime());
        ChargingCacheUtil.setUpdateTime(CacheKeyProvide.KEY_PILE_UPDATETIME, newStation.getUpdateTime());
        // 插入日志

        return result;
    }

    @Override
    public List<BusChargeRule> selectChargeRuleByPage(Map<String, Object> param) {
        return busChargeRuleMapper.selectChargeRuleByPage(param);
    }

    @Override
    public int insertChargeRuleSelective(BusChargeRule record) {
        return busChargeRuleMapper.insertSelective(record);
    }

    @Override
    public int updateChargeRuleByPrimaryKeySelective(BusChargeRule record) {
        return busChargeRuleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int deleteChargeRuleByPrimaryKey(Integer id) {
        return busChargeRuleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public BusChargeRule selectChargeRuleByPrimaryKey(Integer id) {
        return busChargeRuleMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updatePileChargeruleByExampleSelective(BusPileChargerule record, BusPileChargeruleExample example) {
        return busPileChargeruleMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int insertPileChargeRuleBatch(List<BusPileChargerule> list) {
        return busPileChargeruleMapper.insertPileChargeRuleBatch(list);
    }

    @Override
    public void insertAndUpdatePileChargeRuleBatch(List<Integer> stationIds, BusPileChargerule pileChargerule) throws Exception {

        PobChargingPileExample emp = new PobChargingPileExample();
        PobChargingPileExample.Criteria cr = emp.createCriteria();
        cr.andStationIdIn(stationIds);
        List<PobChargingPile> pileList = selectPobChargingPileByExample(emp);
        List<Integer> pileIdList = returnPileIds(pileList);
        if (pileIdList.size() > 0) {
            Calendar a = Calendar.getInstance();
            a.set(Calendar.HOUR_OF_DAY, 0);
            a.set(Calendar.MINUTE, 0);
            a.set(Calendar.SECOND, 0);
            a.set(Calendar.MILLISECOND, 0);
            BusPileChargerule rule = new BusPileChargerule();
            rule.setStatus((short) 2);// 设置状态过期
            BusPileChargeruleExample pileChargeruleEmp = new BusPileChargeruleExample();
            BusPileChargeruleExample.Criteria pileChargeruleCr = pileChargeruleEmp.createCriteria();
            pileChargeruleCr.andPileIdIn(pileIdList);
            pileChargeruleCr.andActiveTimeGreaterThan(a.getTime());
            pileChargeruleCr.andStatusEqualTo((short) 1);
            updatePileChargeruleByExampleSelective(rule, pileChargeruleEmp);
            List<BusPileChargerule> tempInsetbatch = new ArrayList<BusPileChargerule>();
            for (int pileId : pileIdList) {
                BusPileChargerule temp = new BusPileChargerule();
                BeanUtils.copyProperties(temp, pileChargerule);
                temp.setPileId(pileId);
                tempInsetbatch.add(temp);
            }
            int count = insertPileChargeRuleBatch(tempInsetbatch);
            if (count > 0) {
                ChargingCacheUtil.setUpdateTime(CacheKeyProvide.KEY_PILE_CHARGE_RULE_UPDATETIME, pileChargerule.getAddTime());// 鐠愬湱宸兼穱顔芥暭閿涘本鍧婇崝锟�
            }
        }

    }

    private List<Integer> returnPileIds(List<PobChargingPile> pileList) {
        List<Integer> list = new ArrayList<Integer>();
        if (pileList != null && pileList.size() > 0) {
            for (PobChargingPile pile : pileList) {
                list.add(pile.getId());
            }
        }
        return list;
    }

}
