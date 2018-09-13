package com.holley.charging.dcs.protocol;

import org.apache.commons.lang3.StringUtils;

public enum BizCmdTypeEnum {
    STARTCHARGE(0), STOPCHARGE(1), STARTAPPOINTMENT(0), STOPAPPOINTMENT(1);

    private final int value;

    BizCmdTypeEnum(int value) {
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
    public static BizCmdTypeEnum getEnmuByValue(int value) {
        for (BizCmdTypeEnum lowCommType : BizCmdTypeEnum.values()) {
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
    public static BizCmdTypeEnum getEnmuByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        for (BizCmdTypeEnum lowCommType : BizCmdTypeEnum.values()) {
            if (StringUtils.equals(name, lowCommType.toString())) {
                return lowCommType;
            }
        }
        return null;
    }
}
