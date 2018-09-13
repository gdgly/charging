package com.holley.charging.service.website;

import java.util.List;
import java.util.Map;

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

/**
 * pob对象服务
 *
 * @author zhouli
 */
public interface PobObjectService {

    // --充电点
    int insertChargingStation(PobChargingStation record);

    List<PobChargingStation> selectChargingStationByExample(PobChargingStationExample example);

    PobChargingStation selectChargingStationByPK(Integer id);

    int updateChargingStationBKSelective(PobChargingStation record);

    int updateChargingStationScore(Map<String, Object> params);

    List<Integer> selectRecentChargeStation(Integer userid);

    List<PobChargingStation> selectHasPileStationByPage(Map<String, Object> params);

    List<PobChargingStation> selectChargeStationByPage(Map<String, Object> params);

    // ---充电桩
    int insertChargingPile(PobChargingPile record);

    List<PobChargingPile> selectChargingPileByExample(PobChargingPileExample example);

    int updateChargingPileByPKSelective(PobChargingPile record);

    PobChargingPile selectChargingPileByPK(Integer id);

    List<PobChargingPile> selectChargingPileByParam(Map<String, Object> param);

    List<PobChargingPile> selectPileAndFeeByParam(Map<String, Object> params);

    // ---充电桩评论

    int insertPlieComment(BusPileComment record);

    List<BusPileComment> selectPileCommentByExample(BusPileCommentExample example);

    List<BusPileComment> selectStationCommentByPage(Map<String, Object> params);

    int deletePileCommentByExample(BusPileCommentExample example);

    BusPileComment selectPileCommentByPK(Integer id);

    // --新闻资讯

    List<PobNews> selectNewsByPage(Map<String, Object> params);

    PobNews selectNewsByPrimaryKey(Integer id);

    int updateNewsByPrimaryKeySelective(PobNews record);

    // 充电点费用
    List<StationFee> selectStationFee();

    // --服务点
    int updateRepairPointByPK(BusRepairPoint record);

    int updateRepairPointByPKSelective(BusRepairPoint record);

    int insertRepairPointSelective(BusRepairPoint record);

    List<BusRepairPoint> selectRepairPointByExample(BusRepairPointExample example);

    List<BusRepairPoint> selectRepairPointByPage(Map<String, Object> params);

    BusRepairPoint selectRepairPointByPK(Integer id);

}
