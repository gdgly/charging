package com.holley.charging.model.bus;

import com.holley.common.constants.charge.SuggestionStatusEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.util.DateUtil;

public class BusSuggestion extends BusSuggestionKey {

    private String content;

    private String pic;

    private Short  status;

    private String username;
    private Short  usertype;
    private String phone;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic == null ? null : pic.trim();
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Short getUsertype() {
        return usertype;
    }

    public void setUsertype(Short usertype) {
        this.usertype = usertype;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddTimeDesc() {
        if (getAddTime() == null) return "";
        return DateUtil.DateToLongStr(getAddTime());
    }

    public String getStatusDesc() {
        if (status == null) return "";
        String desc = SuggestionStatusEnum.getText(status.intValue());
        return desc == null ? "" : desc;
    }

    public String getUsertypeDesc() {
        if (usertype == null) return "";
        return UserTypeEnum.getText(usertype.intValue());
    }

}
