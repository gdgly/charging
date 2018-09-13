package com.holley.charging.model.def;

import java.util.List;

import com.holley.charging.model.pob.PobChargingPile;
import com.holley.charging.model.pob.PobChargingStation;

public class StationPileListModel extends PobChargingStation {

    private List<PobChargingPile> pileList;

    public List<PobChargingPile> getPileList() {
        return pileList;
    }

    public void setPileList(List<PobChargingPile> pileList) {
        this.pileList = pileList;
    }

}
