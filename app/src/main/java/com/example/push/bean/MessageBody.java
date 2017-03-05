package com.example.push.bean;

import java.io.Serializable;

/**
 * 消息体
 * Created by shihao on 2017/3/5.
 */

public class MessageBody implements Serializable {
    private String from;
    private String title;
    private String content;
    private String time;
    private String extra;

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

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
