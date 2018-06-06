package com.xugongming38.timemanager.datamodel;

import org.litepal.crud.DataSupport;

import java.sql.Date;

/**
 * Created by dell on 2018/4/19.
 */

public class Comment extends DataSupport {
    private int id;
    private String feeling;
    private Date doneTime;

    public String getFeeling() {
        return feeling;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    public Date getDoneTime() {
        return doneTime;
    }

    public void setDoneTime(Date doneTime) {
        this.doneTime = doneTime;
    }
}
