package com.holley.charging.job;

import com.holley.charging.common.util.CacheChargeHolder;

/**
 * 更新缓存任务
 * 
 * @author zdd
 */
public class UpdateCacheJob {

    public void execute() {
        CacheChargeHolder.reloadChargeStation();
        CacheChargeHolder.reloadChargePile();

    }
}
