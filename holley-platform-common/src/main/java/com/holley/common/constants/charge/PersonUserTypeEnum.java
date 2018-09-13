package com.holley.common.constants.charge;

/**
 * 个人用户类型 <br>
 */
public enum PersonUserTypeEnum {
    SYS_PERSON(1, "个人（平台）"), WX_PERSON(2, "个人（微信）");

    private final int    value;
    private final String text;

    PersonUserTypeEnum(int value, String text) {
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
        PersonUserTypeEnum task = getEnmuByValue(value);
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
    public static PersonUserTypeEnum getEnmuByValue(int value) {
        for (PersonUserTypeEnum record : PersonUserTypeEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }

}
