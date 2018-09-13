package com.holley.charging.dcs.constant;


public enum MediaTypeEnum {

	UNDEFINE(0, "undefine"),RS232(1, "RS232"), TCPCLIENT(2, "TcpClient"), TCPSERVER(3, "TcpServer"), UDPCLIENT(4, "UdpClient"), UDPSERVER(5, "UdpServer");

    private final int    value;
    private final String text;

    MediaTypeEnum(int value, String text) {
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
    	MediaTypeEnum devtype = getEnmuByValue(value);
        return devtype == null ? null : devtype.getText();
    }

    /**
     * 通过传入的字符串匹配枚举，传入值
     * 
     * @param value
     * @return
     */
    public static MediaTypeEnum getEnmuByValue(int value) {
        for (MediaTypeEnum devtype : MediaTypeEnum.values()) {
            if (value == devtype.getValue()) {
                return devtype;
            }
        }
        return null;
    }
}
