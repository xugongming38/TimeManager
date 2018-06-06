package com.xugongming38.timemanager.datamodel;

import org.litepal.crud.DataSupport;

/**
 * Created by dell on 2018/4/21.
 */

public class User extends DataSupport{
    private int id;
    private  String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
