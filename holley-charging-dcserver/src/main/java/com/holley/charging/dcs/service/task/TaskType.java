package com.holley.charging.dcs.service.task;

public enum TaskType {
	
	/*
	 * 定时采集
	 */
	CYCLEREAD(1),
	/*
	 * 漏点补抄
	 */
	MISSINGREAD(2),
	/*
	 * 人工采集
	 */
	MANUALREAD(3),
	/*
	 * 随机召唤
	 */
	RANDOMREAD(4),
	
	/*
	 * 主动上报
	 */
	REPORT(5);
	
	int value=1;
	TaskType(int value) {
        this.value = value;
    }
	
	public int getValue() {
        return value;
    }
	
	/**
     * 通过传入的字符串匹配枚举，传入值
     * 
     * @param value
     * @return
     */
    public static TaskType getEnmuByValue(int value) {
        for (TaskType taskType : TaskType.values()) {
            if (value == taskType.getValue()) {
                return taskType;
            }
        }
        return null;
    }
}
