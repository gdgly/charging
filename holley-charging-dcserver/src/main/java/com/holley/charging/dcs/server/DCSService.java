package com.holley.charging.dcs.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.holley.charging.dcs.constant.Global;
import com.holley.charging.dcs.thread.MainThread;

public class DCSService {

    static Log         logger = LogFactory.getLog(DCSService.class.getName());
    private MainThread mainThread;

    /**
     * @param args
     */
    public static void main(String[] args) {

        // 加载程序启动参数
        if (args != null && args.length > 1) {
            String runmode = args[0];

        }

        DCSService server = new DCSService();
        server.init();
        server.start();

        // 命令行监听
        // CommandUI ui = new CommandUI(server);
        // ui.startUI();

        // IDataBaseService dataBaseService = (IDataBaseService)SpringSupport.springHandle.getBean("dataBaseService");
        // List<ObjDevice> list = dataBaseService.loadDevice();
    }

    public void init() {
        // 初始化log4j
        JarUtil ju = new JarUtil(DCSService.class);// + "/classes"
        PropertyConfigurator.configure(ju.getJarPath() + Global.CONFIG_OUT_PATH + "/log4j.properties");

        // 加载HES配置文件
        String os = System.getProperty("os.name");
        System.out.println(os);
        if (DCSConfig.sysConfig == null) {
            String path = Global.CONFIG_OUT_PATH + "/dcs-config.xml";
            if (os != null && os.startsWith("Windows")) {
                path = ju.getJarPath() + "/classes" + path;
            }
            ApplicationContext ctx = new FileSystemXmlApplicationContext(path);
            DCSConfig.sysConfig = (DCSConfig) ctx.getBean("dcsconfig");
        }

        // 初始化spring
        if (SpringSupport.springHandle == null) {
            SpringSupport.initHandle();
        }
    }

    public void start() {
        // 启动管理线程
        mainThread = new MainThread(this);
        mainThread.start();
    }

    public void exit() {
        if (mainThread != null) {
            mainThread.exitThread();
        }
    }

    public MainThread getMainThread() {
        return mainThread;
    }
}
