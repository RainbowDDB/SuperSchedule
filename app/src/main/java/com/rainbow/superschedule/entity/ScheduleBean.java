package com.rainbow.superschedule.entity;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * SuperSchedule
 * Created By Rainbow on 2020/5/9.
 */
public class ScheduleBean implements Serializable {

//    scheduleId: Number,
//    userId: Number,
//    isAlarm: Number, // 1: 是 0：否
//    advancedAlarmMintes: Number, // 提前多少分钟提醒
//    startTime: String, // Timestamp eg:2020-05-20 00:00:00
//    endTime: String,
//    location: String,
//    describtion: String,
//    remarks: String,
    private int scheduleId;

    private int userId;

    private int isAlarm;

    @SerializedName("advancedAlarmMintes")
    private int advancedAlarmMins;

    private String startTime;

    private String endTime;

    private String location;

    @SerializedName("describtion")
    private String description;

    private String remarks;

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIsAlarm() {
        return isAlarm;
    }

    public void setIsAlarm(int isAlarm) {
        this.isAlarm = isAlarm;
    }

    public int getAdvancedAlarmMins() {
        return advancedAlarmMins;
    }

    public void setAdvancedAlarmMins(int advancedAlarmMins) {
        this.advancedAlarmMins = advancedAlarmMins;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public ScheduleBean(int scheduleId, int userId, int isAlarm, int advancedAlarmMins, String startTime, String endTime, String location, String description, String remarks) {
        this.scheduleId = scheduleId;
        this.userId = userId;
        this.isAlarm = isAlarm;
        this.advancedAlarmMins = advancedAlarmMins;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.description = description;
        this.remarks = remarks;
    }

    @NotNull
    @Override
    public String toString() {
        return "{" +
                "scheduleId=" + scheduleId +
                ", userId=" + userId +
                ", isAlarm=" + isAlarm +
                ", advancedAlarmMins=" + advancedAlarmMins +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
