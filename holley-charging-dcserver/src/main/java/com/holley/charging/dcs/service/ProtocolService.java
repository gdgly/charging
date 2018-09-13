package com.holley.charging.dcs.service;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.holley.charging.dcs.protocol.IProtocol;
import com.holley.common.util.StringUtil;

public class ProtocolService extends BaseService {

    private String                    protocolList;
    private HashMap<Short, IProtocol> protocolMap = null;

    static Log                        logger      = LogFactory.getLog(ProtocolService.class.getName());

    public synchronized IProtocol createProtocol(Short id) {
        if (id == null) {
            return null;
        }
        IProtocol protocol = null;
        if (protocolList != null && protocolList.length() > 0) {
            protocolList = StringUtil.replaceBlank(protocolList);
            String protocols[] = protocolList.split(",");
            if (protocols == null) {
                return null;
            }
            for (int i = 0; i < protocols.length; i++) {
                String ids[] = protocols[i].split(":");
                if (ids == null || ids.length != 2) {
                    continue;
                }
                if (!id.equals(Short.parseShort(ids[0]))) {
                    continue;
                }
                try {
                    protocol = (IProtocol) Class.forName(ids[1]).newInstance();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    logger.error(e.getStackTrace());
                    return null;
                }
                return protocol;
            }
        }
        return null;
    }

    public synchronized IProtocol getProtocol(Short id) {
        if (id == null) {
            return null;
        }
        IProtocol protocol = null;
        if (protocolMap == null) {
            protocolMap = new HashMap<Short, IProtocol>();
        } else if (protocolMap.containsKey(id)) {
            return protocolMap.get(id);
        }

        if (protocolList != null && protocolList.length() > 0) {
            protocolList = StringUtil.replaceBlank(protocolList);
            String protocols[] = protocolList.split(",");
            if (protocols == null) {
                return null;
            }
            for (int i = 0; i < protocols.length; i++) {
                String ids[] = protocols[i].split(":");
                if (ids == null || ids.length != 2) {
                    continue;
                }
                if (!id.equals(Short.parseShort(ids[0]))) {
                    continue;
                }
                try {
                    protocol = (IProtocol) Class.forName(ids[1]).newInstance();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    logger.error(e.getStackTrace());
                    return null;
                }
                return protocol;
            }
        }
        return null;
    }

    public String getProtocolList() {
        return protocolList;
    }

    public void setProtocolList(String protocolList) {
        this.protocolList = protocolList;
    }
}
