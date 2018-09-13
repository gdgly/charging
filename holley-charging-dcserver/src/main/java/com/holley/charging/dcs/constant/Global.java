package com.holley.charging.dcs.constant;

public class Global {

    public static String CONFIG_OUT_PATH      = "/config";
    public static String CONFIG_IN_PATH       = "config";
    public static String DEFAULT_LOCALE       = "en_US";

    public static int    MAX_NUM_DEV_THREAD   = 4;
    public static int    INTERVAL_DEV_THREAD  = 100;      // 毫秒
    public static int    MAX_NUM_CHAN_THREAD  = 4;
    public static int    INTERVAL_CHAN_THREAD = 100;      // 毫秒

    public static int    DIAL_RETRY_DELAY     = 60;

    public static int    CHAN_NUM_PERRTU      = 2;

    public static int    SIZE_RTU_BUFF        = 10240;

    public static int    MAX_CHANCMD_WAITTIME = 100;      // 毫秒

    public static int    TASK_PROFILE_CODE    = 2000;     // 召测方案明细中数据类型编码，对曲线、任务等非基本数据项的编码开始值

    public static int    MAX_MSG_COUNT        = 10240;    // rtu、channel 保存的报文数量

    public static int    INVALID_SCHEMEID     = 0;        // 无效任务

    public static byte   MAX_ASSIGNTIMES      = 1;        // 任务重试次数
    public static byte   MAX_SENDTIMES        = 1;        // 失败重发次数

}
