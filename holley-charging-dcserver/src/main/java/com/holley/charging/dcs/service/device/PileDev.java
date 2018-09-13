package com.holley.charging.dcs.service.device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.holley.charging.dcs.dao.model.PileFeeModel;
import com.holley.charging.dcs.dao.model.PobPile;

public class PileDev extends PobPile {

    // 一桩多枪标识
    private boolean                   multiInterface = false;
    // multiInterface=true时生效
    private HashMap<Integer, PileDev> interfaceMap   = null;
    private short                     pubAddress     = 0;                            // 站地址
    List<PileFeeModel>                pfmList        = new ArrayList<PileFeeModel>();

    public PileDev() {

    }

    public PileDev(PobPile pile) {
        this();
        BeanUtils.copyProperties(pile, this);
    }

    public short getPubAddress() {
        return pubAddress;
    }

    public void setPubAddress(short pubAddress) {
        this.pubAddress = pubAddress;
    }

    public List<PileFeeModel> getPfmList() {
        return pfmList;
    }

    public void setPfmList(List<PileFeeModel> pfmList) {
        this.pfmList = pfmList;
    }

}
