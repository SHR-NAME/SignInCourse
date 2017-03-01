package com.example.administrator.signincouse.model;

/**
 * Created by bangzhu on 2016/8/24.
 */
public class Sign {
    private String courseName;
    private String signTime;
    private int signCondition;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public int getSignCondition() {
        return signCondition;
    }

    public void setSignCondition(int signCondition) {
        this.signCondition = signCondition;
    }
}
