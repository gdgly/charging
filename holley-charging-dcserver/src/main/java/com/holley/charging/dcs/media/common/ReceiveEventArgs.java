package com.holley.charging.dcs.media.common;

/** 
 Argument class for IMedia data received events.
*/
public class ReceiveEventArgs
{
    private Object privateData;
    private String privateSenderInfo;
    
    /** 
     Constructor
    */
    public ReceiveEventArgs()
    {

    }

    /** 
     Constructor
    */
    public ReceiveEventArgs(Object data, String senderInfo)
    {
        setData(data);
        setSenderInfo(senderInfo);
    }

    /** 
     Pointer to the (safe array) byte buffer containing the data, 
     which is received from the device.
    */
    public final Object getData()
    {
        return privateData;
    }
    public final void setData(Object value)
    {
        privateData = value;
    }
    /** 
     Media depend sender information.
    */
    public final String getSenderInfo()
    {
        return privateSenderInfo;
    }
    public final void setSenderInfo(String value)
    {
        privateSenderInfo = value;
    }
}