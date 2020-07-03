package com.rainbow.superschedule.entity;

/**
 * SuperSchedule
 * Created By Rainbow on 2020/7/1.
 */
public class AccountResponseBean {
//    result: String,
//    msg: String,
//    userId: Number
    private String result;

    private String msg;

    private int userId;

    public AccountResponseBean(String result, String msg, int userId) {
        this.result = result;
        this.msg = msg;
        this.userId = userId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
