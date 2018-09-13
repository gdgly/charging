package com.holley.charging.bussiness.action;

import com.holley.charging.action.BaseAction;

/**
 * 权限相关ACTION
 * 
 * @author shencheng
 */
public class AuthAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    public String init() {

        return SUCCESS;
    }

    public boolean isSuccess() {
        return success;
    }

}
