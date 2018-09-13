package com.holley.charging.website.action;

import com.holley.charging.action.BaseAction;
import com.holley.common.constants.Globals;

public class ChargeDeviceAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    public String init() {
        this.getRequest().setAttribute(Globals.CURRENTMODULE, "chargedevice");
        return SUCCESS;
    }

    public boolean isSuccess() {
        return success;
    }
}
