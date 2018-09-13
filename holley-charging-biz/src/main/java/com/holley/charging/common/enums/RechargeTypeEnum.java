package com.holley.charging.common.enums;

/**
 * 充值|提现状<br>
 */
public enum RechargeTypeEnum {
    RECHARGE(1, "充值"), CASH(2, "提现");

    private final int    value;
    private final String text;

    RechargeTypeEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public static String getText(int value) {
        RechargeTypeEnum task = getEnmuByValue(value);
        return task == null ? null : task.getText();
    }

    public Short getShortValue() {
        Integer obj = value;
        return obj.shortValue();
    }

    /**
     * 通过传入的值匹配枚举
     * 
     * @param value
     * @return
     */
    public static RechargeTypeEnum getEnmuByValue(int value) {
        for (RechargeTypeEnum record : RechargeTypeEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }

}
