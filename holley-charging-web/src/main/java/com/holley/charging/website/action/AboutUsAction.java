package com.holley.charging.website.action;

import com.holley.charging.action.BaseAction;

public class AboutUsAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    public String aboutUsInit() {
        return SUCCESS;
    }

    public String contactUsInit() {
        return SUCCESS;
    }

    public boolean isSuccess() {
        return success;
    }
}
