package com.holley.charging.dcs.database;

import java.security.InvalidKeyException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import com.alibaba.druid.pool.DruidDataSource;
import com.holley.common.security.RijndaelUtil;

public class DataSource extends DruidDataSource {

    String key = "@$DcS*&DB09^%12!Efw";

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        // TODO Auto-generated method stub
        return null;
    }

    // 密码解密
    public void setPassword(String password) {
        String pass;
        try {
            pass = RijndaelUtil.decodePassword(password);
            super.setPassword(pass);
        } catch (InvalidKeyException e) {
            super.setPassword(password);
        }
    }

    public String getPassword() {
        return this.password;
    }

    public void setUsername(String username) {
        String name;
        try {
            name = RijndaelUtil.decodePassword(username);
            super.setUsername(name);
        } catch (InvalidKeyException e) {
            super.setUsername(username);
        }

    }

    public String getUsername() {
        return this.username;
    }

}
