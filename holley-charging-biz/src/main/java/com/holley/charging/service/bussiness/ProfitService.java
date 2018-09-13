package com.holley.charging.service.bussiness;

import java.util.List;
import java.util.Map;

import com.holley.charging.model.def.ProfitModel;

public interface ProfitService {

    /**
     * 按年，月，季度统计预约金额
     * 
     * @param param
     * @return
     */
    List<ProfitModel> createAppProfit(Map<String, Object> param);

    /**
     * 获取年，月，季度的服务费，停车费，充电费统计数据
     * 
     * @param param
     * @return
     */
    List<ProfitModel> createYMQProfit(Map<String, Object> param);

    /**
     * 按条件统计充电桩收益排行
     * 
     * @param param
     * @return
     */
    List<ProfitModel> countProfit(Map<String, Object> param);

    /**
     * 统计预约次数
     * 
     * @param param
     * @return
     */
    int countAppiontment(Map<String, Object> param);

    /**
     * 统计充电次数
     * 
     * @param param
     * @return
     */
    int countCharge(Map<String, Object> param);
}
