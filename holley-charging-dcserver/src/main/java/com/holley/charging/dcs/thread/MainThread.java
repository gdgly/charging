package com.holley.charging.dcs.thread;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.holley.charging.dcs.dao.model.PileFeeModel;
import com.holley.charging.dcs.dao.model.PobPile;
import com.holley.charging.dcs.database.IDataBaseService;
import com.holley.charging.dcs.media.MediaPara;
import com.holley.charging.dcs.server.DCSConfig;
import com.holley.charging.dcs.server.DCSService;
import com.holley.charging.dcs.server.SpringSupport;
import com.holley.charging.dcs.service.ChannelManagerService;
import com.holley.charging.dcs.service.PileManagerService;
import com.holley.charging.dcs.service.ProtocolService;
import com.holley.common.cache.RedisUtil;
import com.holley.common.cache.charging.CacheKeyProvide;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.jms.MessageSendService;
import com.holley.common.util.StringUtil;

public class MainThread extends BaseThread {

    private IDataBaseService      dataBaseService;
    private PileManagerService    pileManagerService;
    private ChannelManagerService channelService;
    private ProtocolService       protocolService;
    private MessageSendService    messageSendService;

    private DCSService            server;

    private ServerMonitorThread[] serverMonitorThread = null;
    private MQThread              mqThread            = null;
    static Log                    logger              = LogFactory.getLog(MainThread.class.getName());

    public MainThread(DCSService server) {
        this.server = server;

        dataBaseService = (IDataBaseService) SpringSupport.springHandle.getBean("dataBaseService");
        pileManagerService = (PileManagerService) SpringSupport.springHandle.getBean("pileManagerService");
        channelService = (ChannelManagerService) SpringSupport.springHandle.getBean("channelManagerService");
        protocolService = (ProtocolService) SpringSupport.springHandle.getBean("protocolService");
        messageSendService = (MessageSendService) SpringSupport.springHandle.getBean("messageSendService");

        channelService.setMainThread(this);
        pileManagerService.setMainThread(this);
        /*
         * deviceService.setMainThread(this); channelService.setMainThread(this); taskService = (TaskService)
         * SpringSupport.springHandle.getBean("taskService"); dictionaryService = (DictionaryService)
         * SpringSupport.springHandle.getBean("dictionaryService");
         */
    }

    public MessageSendService getMessageSendService() {
        return messageSendService;
    }

    public void setMessageSendService(MessageSendService messageSendService) {
        this.messageSendService = messageSendService;
    }

    public ProtocolService getProtocolService() {
        return protocolService;
    }

    public IDataBaseService getDataBaseService() {
        return dataBaseService;
    }

    public PileManagerService getPileManagerService() {
        return pileManagerService;
    }

    public ChannelManagerService getChannelService() {
        return channelService;
    }

    @Override
    public void run() {
        // 加载数据库参数
        loadDataBase();

        // mqThread = new MQThread(this);
        // mqThread.start();

        // 启动服务端监听通讯管理线程池
        List<MediaPara> list = DCSConfig.sysConfig.getServerMediaList();
        int threadNum = list.size();
        if (list != null && threadNum != 0) {

            serverMonitorThread = new ServerMonitorThread[threadNum];
            for (int i = 0; i < list.size(); i++) {
                serverMonitorThread[i] = new ServerMonitorThread(this);
                serverMonitorThread[i].setPara(list.get(i));
                serverMonitorThread[i].start();
            }
        }
        Date lastLoadDBTime = Calendar.getInstance().getTime();
        int k = 1; // 计数器
        // 线程维系
        while (!bStopThread) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.info(e.getMessage());
            }
            k++;
            if (k < 0) k = 1;
            if (k % (30) == 0) { // 30秒，参数更新
                try {
                    // ADD BY SC START
                    if (StringUtil.isNotEmpty(RedisUtil.getString("updateFeeModel"))) {
                        dataBaseService.reLoadFeeModel();
                        RedisUtil.delKey("updateFeeModel");
                    }
                    // ADD BY SC END
                    Date pileUpdateTime = ChargingCacheUtil.getUpdateTime(CacheKeyProvide.KEY_PILE_UPDATETIME);
                    // Calendar now = Calendar.getInstance();
                    // now.set(Calendar.HOUR_OF_DAY, 8);
                    // lastLoadDBTime = now.getTime();
                    // logger.info("KEY_PILE_UPDATETIME=" + DateUtil.DateToLongStr(pileUpdateTime) + ";lastLoadDBTime="
                    // +
                    // DateUtil.DateToLongStr(lastLoadDBTime));
                    if (pileUpdateTime != null && lastLoadDBTime.before(pileUpdateTime)) {
                        // 重新加载充电桩信息
                        List<PobPile> pileList = dataBaseService.reLoadPobPiles(lastLoadDBTime);
                        pileManagerService.onLoadPile(pileList);
                        logger.info("发现充电桩参数修改！ 修改数量=" + pileList.size());
                    }
                    Date pileFeeModelUpdateTime = ChargingCacheUtil.getUpdateTime(CacheKeyProvide.KEY_PILE_CHARGE_RULE_UPDATETIME);
                    if (pileFeeModelUpdateTime != null && lastLoadDBTime.before(pileFeeModelUpdateTime)) {
                        // 重新加载充电桩计费模型信息
                        List<PileFeeModel> pileFeeModelList = dataBaseService.reLoadPileFeeModel(lastLoadDBTime);
                        pileManagerService.onLoadPileFeeModel(pileFeeModelList);
                    }
                    if (pileUpdateTime != null || pileFeeModelUpdateTime != null) {
                        if (pileUpdateTime == null) {
                            lastLoadDBTime = pileFeeModelUpdateTime;
                        } else if (pileFeeModelUpdateTime == null) {
                            lastLoadDBTime = pileUpdateTime;
                        } else {
                            lastLoadDBTime = pileUpdateTime.after(pileFeeModelUpdateTime) ? pileUpdateTime : pileFeeModelUpdateTime;
                        }
                    }
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }

            }
            try {
                // 监听服务
                for (int i = 0; i < threadNum; i++) {
                    if (serverMonitorThread[i] != null) {
                        if (serverMonitorThread[i].getState().equals(Thread.State.TERMINATED)) {
                            ServerMonitorThread temp = serverMonitorThread[i];
                            serverMonitorThread[i] = new ServerMonitorThread(this);
                            serverMonitorThread[i].setPara(temp.getPara());
                            serverMonitorThread[i].start();
                        }
                    }
                }
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
            try {
                // 设备管理定时服务
                if (pileManagerService != null) {
                    pileManagerService.onTimer();
                }
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
            try {
                if (k % (3600) == 0) { // 1小时，通道清理
                    if (channelService != null) {
                        channelService.onTimer();
                    }
                }
            } catch (Exception e) {
                logger.info(e.getMessage());
            }

        }

        logger.info("MainThread exit!");
    }

    protected void loadDataBase() {

        // 加载充电桩信息
        List<PobPile> pileList = dataBaseService.loadPobPiles();
        pileManagerService.onLoadPile(pileList);
    }

    public void exitThread() {
        super.exitThread();
    }

    public boolean isActiveStatus() {
        // TODO Auto-generated method stub
        return true;
    }

}
