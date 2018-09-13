package com.holley.charging.dcs.service.channel;

public enum LinkStatus {
	MODEMINIT,
	MODEMINIT_SEND,
	/*
	 * 设备端口正确打开，但未执行与RTU的绑定
	 */
	IDEL,
	/*
	 * 逻辑连接
	 */
	LOGICLINK,
	/*
	 * 等待物理连接
	 */
	WAITPHYLINK,
	/*
	 * 物理连接状态,有了绑定的RTU，并通道建立了
	 */
	PHYLINK,
	/*
	 * 准备断开
	 */
	NEEDDISCONNECT,
	/*
	 * 发送+++
	 */
	WAITDISPHYLINK,
	/*
	 * 转义序列成功
	 */
	WAITDISPHYLINK_OK,
	/*
	 * 发送挂断命令 
	 */
	SENDDISPHYLINK,
	/*
	 * 挂断命令成功
	 */
	SENDDISPHYLINK_OK,
	WAITPHYLINK_ERR
}
