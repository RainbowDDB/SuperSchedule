package com.rainbow.superschedule.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * SuperSchedule
 * Created By Rainbow on 2020/6/29.
 */
public class UserItemBean {
    private String result;

    private String msg;

    private int more;

    @SerializedName("scheduleItems")
    private List<ScheduleBean> scheduleList;

    public UserItemBean(String result, String msg, int more, List<ScheduleBean> scheduleList) {
        this.result = result;
        this.msg = msg;
        this.more = more;
        this.scheduleList = scheduleList;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getMore() {
        return more;
    }

    public void setMore(int more) {
        this.more = more;
    }

    public List<ScheduleBean> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<ScheduleBean> scheduleList) {
        this.scheduleList = scheduleList;
    }
}
