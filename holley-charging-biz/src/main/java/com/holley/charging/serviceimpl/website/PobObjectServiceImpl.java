package com.holley.charging.serviceimpl.website;

import java.util.List;
import java.util.Map;

import com.holley.charging.dao.bus.BusPileChargeruleMapper;
import com.holley.charging.dao.bus.BusPileCommentMapper;
import com.holley.charging.dao.bus.BusRepairPointMapper;
import com.holley.charging.dao.pob.PobChargingPileMapper;
import com.holley.charging.dao.pob.PobChargingStationMapper;
import com.holley.charging.dao.pob.PobNewsMapper;
import com.holley.charging.model.app.StationFee;
import com.holley.charging.model.bus.BusPileComment;
import com.holley.charging.model.bus.BusPileCommentExample;
import com.holley.charging.model.bus.BusRepairPoint;
import com.holley.charging.model.bus.BusRepairPointExample;
import com.holley.charging.model.pob.PobChargingPile;
import com.holley.charging.model.pob.PobChargingPileExample;
import com.holley.charging.model.pob.PobChargingStation;
import com.holley.charging.model.pob.PobChargingStationExample;
import com.holley.charging.model.pob.PobNews;
import com.holley.charging.service.website.PobObjectService;

public class PobObjectServiceImpl implements PobObjectService {

    private PobChargingStationMapper pobChargingStationMapper;
    private PobChargingPileMapper    pobChargingPileMapper;
    private BusPileCommentMapper     busPileCommentMapper;
    private PobNewsMapper            pobNewsMapper;
    private BusPileChargeruleMapper  busPileChargeruleMapper;
    private BusRepairPointMapper     busRepairPointMapper;

    @Override
    public int insertChargingStation(PobChargingStation record) {
        return pobChargingStationMapper.insert(record);
    }

    @Override
    public List<PobChargingStation> selectChargingStationByExample(PobChargingStationExample example) {
        return pobChargingStationMapper.selectByExample(example);
    }

    @Override
    public PobChargingStation selectChargingStationByPK(Integer id) {
        return pobChargingStationMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateChargingStationBKSelective(PobChargingStation record) {
        return pobChargingStationMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateChargingStationScore(Map<String, Object> params) {
        return pobChargingStationMapper.updateChargingStationScore(params);
    }

    @Override
    public List<Integer> selectRecentChargeStation(Integer userid) {
        return pobChargingStationMapper.selectRecentChargeStation(userid);
    }

    @Override
    public List<PobChargingStation> selectHasPileStationByPage(Map<String, Object> params) {
        return pobChargingStationMapper.selectHasPileStationByPage(params);
    }

    @Override
    public List<PobChargingStation> selectChargeStationByPage(Map<String, Object> params) {
        return pobChargingStationMapper.selectChargeStationByPage(params);
    }

    @Override
    public int insertChargingPile(PobChargingPile record) {
        return pobChargingPileMapper.insert(record);
    }

    @Override
    public List<PobChargingPile> selectChargingPileByExample(PobChargingPileExample example) {
        return pobChargingPileMapper.selectByExample(example);
    }

    @Override
    public int updateChargingPileByPKSelective(PobChargingPile record) {
        return pobChargingPileMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public PobChargingPile selectChargingPileByPK(Integer id) {
        return pobChargingPileMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<PobChargingPile> selectPileAndFeeByParam(Map<String, Object> params) {
        return pobChargingPileMapper.selectPileAndFeeByParam(params);
    }

    // 充电桩评论
    @Override
    public int insertPlieComment(BusPileComment record) {
        return busPileCommentMapper.insertSelective(record);
    }

    @Override
    public List<BusPileComment> selectPileCommentByExample(BusPileCommentExample example) {
        return busPileCommentMapper.selectByExample(example);
    }

    @Override
    public List<BusPileComment> selectStationCommentByPage(Map<String, Object> params) {
        return busPileCommentMapper.selectStationCommentByPage(params);
    }

    @Override
    public int deletePileCommentByExample(BusPileCommentExample example) {
        return busPileCommentMapper.deleteByExample(example);
    }

    @Override
    public BusPileComment selectPileCommentByPK(Integer id) {
        return busPileCommentMapper.selectByPrimaryKey(id);
    }

    // 新闻资讯
    @Override
    public List<PobNews> selectNewsByPage(Map<String, Object> params) {
        return pobNewsMapper.selectNewsByPage(params);
    }

    @Override
    public PobNews selectNewsByPrimaryKey(Integer id) {
        return pobNewsMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateNewsByPrimaryKeySelective(PobNews record) {
        return pobNewsMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<PobChargingPile> selectChargingPileByParam(Map<String, Object> param) {
        return pobChargingPileMapper.selectByParam(param);
    }

    @Override
    public List<StationFee> selectStationFee() {
        return busPileChargeruleMapper.selectStationFee();
    }

    @Override
    public int updateRepairPointByPK(BusRepairPoint record) {
        return busRepairPointMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateRepairPointByPKSelective(BusRepairPoint record) {
        return busRepairPointMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int insertRepairPointSelective(BusRepairPoint record) {
        return busRepairPointMapper.insertSelective(record);
    }

    @Override
    public List<BusRepairPoint> selectRepairPointByExample(BusRepairPointExample example) {
        return busRepairPointMapper.selectByExample(example);
    }

    @Override
    public List<BusRepairPoint> selectRepairPointByPage(Map<String, Object> params) {
        return busRepairPointMapper.selectRepairPointByPage(params);
    }

    @Override
    public BusRepairPoint selectRepairPointByPK(Integer id) {
        return busRepairPointMapper.selectByPrimaryKey(id);
    }

    // ---------------get and set-------------------------

    public void setPobChargingStationMapper(PobChargingStationMapper pobChargingStationMapper) {
        this.pobChargingStationMapper = pobChargingStationMapper;
    }

    public void setPobChargingPileMapper(PobChargingPileMapper pobChargingPileMapper) {
        this.pobChargingPileMapper = pobChargingPileMapper;
    }

    public void setBusPileCommentMapper(BusPileCommentMapper busPileCommentMapper) {
        this.busPileCommentMapper = busPileCommentMapper;
    }

    public void setPobNewsMapper(PobNewsMapper pobNewsMapper) {
        this.pobNewsMapper = pobNewsMapper;
    }

    public void setBusPileChargeruleMapper(BusPileChargeruleMapper busPileChargeruleMapper) {
        this.busPileChargeruleMapper = busPileChargeruleMapper;
    }

    public void setBusRepairPointMapper(BusRepairPointMapper busRepairPointMapper) {
        this.busRepairPointMapper = busRepairPointMapper;
    }

}
