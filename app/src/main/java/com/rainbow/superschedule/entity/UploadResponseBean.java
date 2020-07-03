package com.rainbow.superschedule.entity;

/**
 * SuperSchedule
 * Created By Rainbow on 2020/7/1.
 */
public class UploadResponseBean {
    private int status;

    private String msg;

    private int scheduleId;

    public UploadResponseBean(int status, String msg, int scheduleId) {
        this.status = status;
        this.msg = msg;
        this.scheduleId = scheduleId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }
}
