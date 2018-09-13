package com.holley.charging.model.def;

import com.holley.platform.model.sys.SysModuledef;

public class SysModuledefModel extends SysModuledef {

    private String elementId;
    private String topUrl;

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getTopUrl() {
        return topUrl;
    }

    public void setTopUrl(String topUrl) {
        this.topUrl = topUrl;
    }
}
