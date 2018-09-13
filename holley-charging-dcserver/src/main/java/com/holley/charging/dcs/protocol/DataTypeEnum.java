package com.holley.charging.dcs.protocol;

import org.apache.commons.lang3.StringUtils;

public enum DataTypeEnum {
	YA_YG_ZONG(1),				//有功总电度
	YX_LINKCON(2),			//连接确认开关状态
	YX_AC_IN_OVER(3),		//交流输入过压告警
	YX_AC_IN_LOSS(4),		//交流输入欠压告警
	YX_I_OVER(5),			//充电电流过负荷告警
	YX_RELAY_OUT(6),		//输出继电器状态
	YX_PROTECT_TEMP(7),			//温度过高保护
	YX_PROTECT_SHORT(8),			//短路保护
	YX_PROTECT_LEAK(9),			//漏电保护
	YX_PROTECT_STOP(10),		//急停开关触发
	YX_BMS_ERROR(11),				//BMS通信异常
	YX_DC_OUT_OVER(12),			//直流输出过压告警
	YX_DC_OUT_LOSS(13),			//直流输出欠压告警
	YX_BAT_CUR_OVER(14),			//蓄电池充电过流告警
	YX_BAT_PNTEMP_OVER(15),				//蓄电池模块采样点过温告警
	YX_BAT_LINKED(16),				//是否连接电池
	YX_DEV_LINKED_ERR(17),				//充电机连接器故障
	YX_BMS_LOSS(18),				//BMS终止
	YX_DEV_TEMP_ERROR(19),				//充电机内部温度故障
	YC_WORKSTATUS(20),				//工作状态
	YC_AC_OUT_V(21),					//充电输出电压
	YC_AC_OUT_I(22),					//充电输出电流
	YC_TOTAL_TIME(23),					//累计充电时间
	YC_LEFT_TIME(24),					//预计剩余充电时间
	YC_VA_CUR(25),					//（当前）A相电压
	YC_VB_CUR(26),					//（当前）B相电压
	YC_VC_CUR(27),					//（当前）C相电压
	YC_IA_CUR(28),				//（当前）A相电流
	YC_IB_CUR(29),				//（当前）B相电流
	YC_IC_CUR(30),				//（当前）C相电流
	YC_OUT_V(31),			//充电机输出电压
	YC_OUT_I(32),			//充电机输出电流
	YC_IN_V(33),			//充电机输入电压
	YC_SOC(34),		//SOC
	YC_BAT_TEMP_LOW(35),		//电池组最低温度
	YC_BAT_TEMP_HIGH(36),		//电池组最高温度
	YC_BAT_SV_HIGH(37),			//单体电池最高电压
	YC_BAT_SV_LOW(38);			//单体电池最低电压
	
    private final int value;

    DataTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Short getShortValue() {
        Integer obj = value;
        return obj.shortValue();
    }

    /**
     * 通过传入的字符串匹配枚举，传入值
     * 
     * @param value
     * @return
     */
    public static DataTypeEnum getEnmuByValue(int value) {
        for (DataTypeEnum lowCommType : DataTypeEnum.values()) {
            if (value == lowCommType.getValue()) {
                return lowCommType;
            }
        }
        return null;
    }

    /**
     * 通过传入的字符串匹配枚举,传入名字
     * 
     * @param name
     * @return
     */
    public static DataTypeEnum getEnmuByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        for (DataTypeEnum lowCommType : DataTypeEnum.values()) {
            if (StringUtils.equals(name, lowCommType.toString())) {
                return lowCommType;
            }
        }
        return null;
    }
}
