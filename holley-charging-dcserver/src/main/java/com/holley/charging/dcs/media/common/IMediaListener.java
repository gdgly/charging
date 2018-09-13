package com.holley.charging.dcs.media.common;


public interface IMediaListener 
{
    void onReceived(Object sender, ReceiveEventArgs e);    


    void onActive(Object media, boolean active);
}