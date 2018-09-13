package com.holley.common.constants;

/**
 * 是否激活<br>
 */
public enum IsActiveEnum {
    ACTIVE(1, "激活"), UNACTIVE(2, "失效");

    private final int    value;
    private final String text;

    IsActiveEnum(int value, String text) {
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
        IsActiveEnum task = getEnmuByValue(value);
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
    public static IsActiveEnum getEnmuByValue(int value) {
        for (IsActiveEnum record : IsActiveEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }
}
