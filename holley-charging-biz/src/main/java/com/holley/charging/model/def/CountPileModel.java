package com.holley.charging.model.def;

public class CountPileModel {

    private Integer totalFast; // 快充总数
    private Integer totalSlow; // 慢充总数

    public Integer getTotalFast() {
        return totalFast;
    }

    public void setTotalFast(Integer totalFast) {
        this.totalFast = totalFast;
    }

    public Integer getTotalSlow() {
        return totalSlow;
    }

    public void setTotalSlow(Integer totalSlow) {
        this.totalSlow = totalSlow;
    }
}
