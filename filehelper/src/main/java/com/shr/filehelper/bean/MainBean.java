package com.shr.filehelper.bean;

import java.io.Serializable;

/**
 * 实体类
 */

public class MainBean extends BaseResponse<MainBean> implements Serializable {

    private int id;
    private String name;
    private String atime;
    private String path;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAtime() {
        return atime;
    }

    public void setAtime(String atime) {
        this.atime = atime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
