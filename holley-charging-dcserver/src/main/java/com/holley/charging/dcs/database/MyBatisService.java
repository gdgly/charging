package com.holley.charging.dcs.database;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.holley.charging.dcs.dao.mapper.AccountMapper;
import com.holley.charging.dcs.dao.mapper.AlarmEventsMapper;
import com.holley.charging.dcs.dao.mapper.AppointmentRecMapper;
import com.holley.charging.dcs.dao.mapper.ChargeRecordMapper;
import com.holley.charging.dcs.dao.mapper.DcsBaseDataTypeMapper;
import com.holley.charging.dcs.dao.mapper.DcsHisYaMapper;
import com.holley.charging.dcs.dao.mapper.DcsHisYcMapper;
import com.holley.charging.dcs.dao.mapper.DcsHisYxMapper;
import com.holley.charging.dcs.dao.mapper.FeeModelMapper;
import com.holley.charging.dcs.dao.mapper.PayMentRecMapper;
import com.holley.charging.dcs.dao.mapper.PileFeeModelMapper;
import com.holley.charging.dcs.dao.mapper.PobPileMapper;
import com.holley.charging.dcs.dao.mapper.UserMapper;
import com.holley.charging.dcs.dao.model.Account;
import com.holley.charging.dcs.dao.model.AppointmentRec;
import com.holley.charging.dcs.dao.model.AppointmentRecExample;
import com.holley.charging.dcs.dao.model.ChargeRecord;
import com.holley.charging.dcs.dao.model.DcsHisYa;
import com.holley.charging.dcs.dao.model.DcsHisYc;
import com.holley.charging.dcs.dao.model.DcsHisYx;
import com.holley.charging.dcs.dao.model.FeeModel;
import com.holley.charging.dcs.dao.model.PayMentRec;
import com.holley.charging.dcs.dao.model.PileFeeModel;
import com.holley.charging.dcs.dao.model.PileFeeModelExample;
import com.holley.charging.dcs.dao.model.PobPile;
import com.holley.charging.dcs.dao.model.PobPileExample;
import com.holley.charging.dcs.dao.model.User;
import com.holley.charging.dcs.dao.model.UserExample;
import com.holley.common.constants.charge.UserTypeEnum;

public class MyBatisService implements IDataBaseService {

    @Autowired
    PobPileMapper              pobPileMapper;

    @Autowired
    AlarmEventsMapper          alarmEventsMapper;

    @Autowired
    ChargeRecordMapper         chargeRecordMapper;

    @Autowired
    DcsHisYxMapper             dcsHisYxMapper;

    @Autowired
    DcsHisYcMapper             dcsHisYcMapper;

    @Autowired
    DcsHisYaMapper             dcsHisYaMapper;

    @Autowired
    DcsBaseDataTypeMapper      dcsBaseDataTypeMapper;

    @Autowired
    FeeModelMapper             feeModelMapper;

    @Autowired
    PileFeeModelMapper         pileFeeModelMapper;

    @Autowired
    PayMentRecMapper           payMentRecMapper;

    @Autowired
    UserMapper                 userMapper;

    @Autowired
    AppointmentRecMapper       appointmentRecMapper;

    @Autowired
    AccountMapper              accountMapper;

    HashMap<Integer, FeeModel> feeModelMap = null;

    public void initDataBase() {
        initFeeModel();

    }

    public boolean saveData(Object data) {
        if (data == null) {
            return true;
        }
        if (data instanceof Object[]) {
            // 待完善代码，批量存储
            for (int i = 0; i < ((Object[]) data).length; i++) {
                saveData(((Object[]) data)[i]);
            }
        } else {
            try {
                if (data instanceof ChargeRecord) {
                    chargeRecordMapper.insertOrUpdateByPrimaryKeySelective((ChargeRecord) data);
                } else if (data instanceof DcsHisYx) {
                    dcsHisYxMapper.insert((DcsHisYx) data);
                } else if (data instanceof DcsHisYc) {
                    dcsHisYcMapper.insert((DcsHisYc) data);
                } else if (data instanceof DcsHisYa) {
                    dcsHisYaMapper.insert((DcsHisYa) data);
                } else if (data instanceof PayMentRec) {
                    payMentRecMapper.insertOrUpdateByPrimaryKeySelective((PayMentRec) data);
                } else if (data instanceof AppointmentRec) {
                    AppointmentRec rec = (AppointmentRec) data;
                    if (rec.getId() != null && rec.getUserId() != null && rec.getPileId() != null) {
                        AppointmentRecExample example = null;
                        example = new AppointmentRecExample();
                        AppointmentRecExample.Criteria cr = example.createCriteria();
                        cr.andIdEqualTo(rec.getId());
                        cr.andUserIdEqualTo(rec.getUserId());
                        cr.andPileIdEqualTo(rec.getPileId());
                        appointmentRecMapper.updateByExampleSelective(rec, example);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
        return true;
    }

    @Override
    public List<PobPile> loadPobPiles() {
        List<PobPile> pileList = pobPileMapper.selectByExample(null);
        if (pileList == null || pileList.size() == 0) {
            return pileList;
        }
        if (feeModelMap == null) {
            initFeeModel();
        }

        return pileList;
    }

    @Override
    public List<PobPile> reLoadPobPiles(Date freshTime) {
        PobPileExample example = new PobPileExample();
        PobPileExample.Criteria cr = example.createCriteria();
        cr.andUpdateTimeGreaterThan(freshTime);
        return pobPileMapper.selectByExample(example);
    }

    public List<Integer> selectDataType(Integer typeClass) {
        return dcsBaseDataTypeMapper.selectByTypeClass(typeClass);
    }

    private void initFeeModel() {
        if (feeModelMap == null) {
            feeModelMap = new HashMap<Integer, FeeModel>();
            List<FeeModel> list = feeModelMapper.selectByExample(null);
            for (int i = 0; i < list.size(); i++) {
                feeModelMap.put(list.get(i).getId(), list.get(i));
            }

        }
    }

    @Override
    public List<PileFeeModel> loadPileFeeModel(Integer pileID) {
        PileFeeModelExample example = null;
        if (pileID != null) {
            example = new PileFeeModelExample();
            PileFeeModelExample.Criteria cr = example.createCriteria();
            cr.andPileIdEqualTo(pileID);
            cr.andStatusEqualTo((short) 1);
            example.setOrderByClause("ACTIVE_TIME desc");
        }
        if (feeModelMap == null) {
            initFeeModel();
        }
        List<PileFeeModel> list = pileFeeModelMapper.selectByExample(example);
        if (list == null) {
            return null;
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).getChargeruleId() != 1) { // 1为单一费率
                if (feeModelMap.containsKey(list.get(i).getChargeruleId())) {
                    list.get(i).setFeeModel(feeModelMap.get(list.get(i).getChargeruleId()));
                } else { // 模型找不到了
                    list.remove(i);
                }
            }
        }
        return list;
    }

    @Override
    public User getUserByPhone(String phoneNum) {
        if (phoneNum == null) {
            return null;
        }
        UserExample example = new UserExample();
        UserExample.Criteria cr = example.createCriteria();
        cr.andPhoneEqualTo(phoneNum.trim());
        cr.andUserTypeEqualTo(UserTypeEnum.PERSON.getShortValue());
        List<User> list = userMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public User getUserByCpuCardID(String cardNum) {
        if (cardNum == null) {
            return null;
        }
        HashMap<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("cardMum", cardNum);
        List<User> list = userMapper.selectByCardNum(paraMap);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<PileFeeModel> reLoadPileFeeModel(Date freshTime) {
        if (freshTime == null) {
            return null;
        }
        PileFeeModelExample example = null;
        example = new PileFeeModelExample();
        PileFeeModelExample.Criteria cr = example.createCriteria();
        cr.andStatusEqualTo((short) 1);
        cr.andAddTimeGreaterThan(freshTime);
        example.setOrderByClause("ACTIVE_TIME desc");
        if (feeModelMap == null) {
            initFeeModel();
        }
        List<PileFeeModel> list = pileFeeModelMapper.selectByExample(example);
        if (list == null) {
            return null;
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).getChargeruleId() != 1) { // 1为单一费率
                if (feeModelMap.containsKey(list.get(i).getChargeruleId())) {
                    list.get(i).setFeeModel(feeModelMap.get(list.get(i).getChargeruleId()));
                } else { // 模型找不到了
                    list.remove(i);
                }
            }
        }
        return list;
    }

    @Override
    public Account getAccountByCardID(String cardID) {
        return accountMapper.selectByCardID(cardID);
    }

    @Override
    public void reLoadFeeModel() {
        if (feeModelMap != null) {
            feeModelMap.clear();
            List<FeeModel> list = feeModelMapper.selectByExample(null);
            for (int i = 0; i < list.size(); i++) {
                feeModelMap.put(list.get(i).getId(), list.get(i));
            }
        }
    }

}
