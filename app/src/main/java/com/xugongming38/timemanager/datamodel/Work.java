package com.xugongming38.timemanager.datamodel;

import org.litepal.crud.DataSupport;

import java.util.Date;


/**
 * Created by dell on 2018/4/19.
 */

public class Work extends DataSupport {
    private int id;
    //用户名
    private String username;
    //任务名称
    private String title;
    //状态 -1已完成 0 正在进行  1 未开始
    private int state;
    //任务内容
    private String content;
    //任务优先级0-100
    private int important;
    //最后期限
    private String deadLine;
    //建立时间
    private String setTime;
    //评论
    private Comment comment;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public String getSetTime() {
        return setTime;
    }

    public void setSetTime(String setTime) {
        this.setTime = setTime;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
