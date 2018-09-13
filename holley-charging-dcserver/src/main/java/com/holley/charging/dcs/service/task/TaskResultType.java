package com.holley.charging.dcs.service.task;

public enum TaskResultType {
	/*
	 * 未处理
	 */
	NOTDEAL,		
	/*
	 * 无后续数据，且正确完成
	 */
	EXEOK,
	/*
	 * 规约解释命令错误
	 */
	ERROR_EXPLAIN,
	/*
	 * 同一帧有后续数据
	 */
	CALLNEXT,
	/*
	 * 任务完成一部分，继续本任务下发
	 */
	CONTINUE,
	/*
	 * 连续召唤3次无响应
	 */
	NOANSWER,
	/*
	 * 组帧错误
	 */
	ERROR_MAKEFRAME,
	/*
	 * 手动挂断通道错误
	 */
	ERROR_DISCONNECT,
	/*
	 * 终端暂停接收
	 */
	RTUPAUSE,
	/*
	 * 数据接收延迟
	 */
	DATADELAY,
	/*
	 * 其他错误
	 */
	ERROR_NODEFINE,
	/*
	 * 任务清除
	 */
	TASKCLEAR,
	/*
	 * 被其他高优先级任务打断
	 */
	TASKBREAK,
	/*
	 * 无通道，或故障
	 */
	NOCHAN,
	/*
	 * 无通道，或故障
	 */
	NOCANUSECHAN,
	/*
	 * 规定连接次数，无法连通	
	 */
	OVERLINK,
	/*
	 * 通道长时间故障，无法打开
	 */
	CHAN_TIMEOUT,
	/*
	 * 答非所问，如主动上报
	 */
	NOTMYECHO
}
