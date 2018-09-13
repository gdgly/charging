package com.holley.charging.main;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CalcServer {

    private final static Logger      logger         = Logger.getLogger(CalcServer.class);
    public static String             CONFIG_IN_PATH = "config";
    public static ApplicationContext springHandle   = null;

    public static void init() {
        JarUtil ju = new JarUtil(CalcServer.class);
        PropertyConfigurator.configure(ju.getJarPath() + "/classes/" + CONFIG_IN_PATH + "/log4j.properties");
        springHandle = new ClassPathXmlApplicationContext(new String[] { CONFIG_IN_PATH + "/spring/applicationContext.xml", CONFIG_IN_PATH + "/spring/spring-bus-dao.xml",
                CONFIG_IN_PATH + "/spring/spring-job.xml" });
    }

    public static void main(String[] args) {
        init();
    }
}
