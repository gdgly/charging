package com.holley.charging.dcs.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.holley.charging.dcs.constant.Global;

public class SpringSupport {

    public static ApplicationContext springHandle = null;

    public static void initHandle() {
        if (springHandle == null) {

            // 初始化Spring
            springHandle = new ClassPathXmlApplicationContext(new String[] { Global.CONFIG_IN_PATH + "/spring/spring-common.xml",
                                                                             Global.CONFIG_IN_PATH + "/spring/spring-service.xml", Global.CONFIG_IN_PATH + "/spring/spring-dao.xml",
                                                                             Global.CONFIG_IN_PATH + "/spring/spring-activemq.xml" });
        }
    }
}
