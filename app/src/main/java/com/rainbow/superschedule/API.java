package com.rainbow.superschedule;

import android.util.Log;

import com.google.gson.Gson;
import com.rainbow.superschedule.core.net.RestClient;
import com.rainbow.superschedule.core.net.callback.IError;
import com.rainbow.superschedule.core.net.callback.IFailure;
import com.rainbow.superschedule.core.net.callback.ISuccess;
import com.rainbow.superschedule.core.storage.LattePreference;
import com.rainbow.superschedule.entity.UserItemBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.WeakHashMap;

/**
 * SuperSchedule
 * Created By Rainbow on 2020/5/5.
 */
public class API {
    public static void uploadSchedule(int scheduleId,
                                      boolean isAlarm,
                                      int advancedAlarmMins,
                                      Date startTime,
                                      Date endTime,
                                      String location,
                                      String description,
                                      String remarks,
                                      ISuccess success) {
        WeakHashMap<String, Object> params = new WeakHashMap<>();
        params.put("userId", Integer.parseInt(LattePreference.getCustomAppProfile("user-id")));
        if (scheduleId != -1) {
            params.put("scheduleId", scheduleId);
        }
        params.put("isAlarm", isAlarm ? 1 : 0);
        params.put("advancedAlarmMintes", advancedAlarmMins);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        String s = sdf.format(startTime);
        params.put("startTime", sdf.format(startTime));
        params.put("endTime", sdf.format(endTime));

        params.put("location", location);
        params.put("describtion", description);
        params.put("remarks", remarks);
        new RestClient.Builder()
                .url("daylist/uploadScheduleItem")
                .params(params)
                .success(success)
                .error((code, msg) -> Log.e("upload", code + ":" + msg))
                .failure(() -> {

                })
                .build()
                .post();
    }

    public static void getUserItems(ISuccess success) {
        int page = 0;
        new RestClient.Builder()
                .url("daylist/getUserItems")
                .param("userId", LattePreference.getCustomAppProfile("user-id"))
                .param("page", String.valueOf(page))
                .success(success)
                .error((code, msg) -> {

                })
                .failure(() -> {

                })
                .build()
                .get();
    }

    public static void getUserInfo() {
        new RestClient.Builder()
                .url("daylist/getUserInformation")
                .param("userId", LattePreference.getCustomAppProfile("user-id"))
                .success((response -> Log.d("getInfo", response)))
                .error((code, msg) -> Log.e("getInfo", code + ":" + msg))
                .failure(() -> {

                })
                .build()
                .get();
    }

    public static void modifyUserInfo(String name, int sex, int age, String account, String password) {
        WeakHashMap<String, Object> params = new WeakHashMap<>();
        params.put("userId", LattePreference.getCustomAppProfile("user-id"));
        if (name != null) {
            params.put("name", name);
        }
        if (sex != -1) {
            params.put("sex", sex);
        }
        if (age != -1) {
            params.put("age", age);
        }
        if (account != null) {
            params.put("account", account);
        }
        if (password != null) {
            params.put("cipher", password);
        }
        new RestClient.Builder()
                .url("daylist/modifyUserInformation")
                .params(params)
                .success((response -> Log.d("modifyInfo", response)))
                .error((code, msg) -> Log.e("modifyInfo", code + ":" + msg))
                .failure(() -> {

                })
                .build()
                .post();
    }

    public static void modifyPassword(String password) {
        modifyUserInfo(null, -1, -1, null, password);
    }

    public static void deleteScheduleItem(int scheduleId,ISuccess success) {
        WeakHashMap<String, Object> params = new WeakHashMap<>();
        params.put("userId", LattePreference.getCustomAppProfile("user-id"));
        params.put("scheduleId", scheduleId);

        new RestClient.Builder()
                .url("daylist/deleteScheduleItem")
                .params(params)
                .success(success)
                .error((code, msg) -> Log.e("deleteItem", code + ":" + msg))
                .failure(() -> {

                })
                .build()
                .post();
    }

    public static void register(String account, String password, ISuccess success, IError error) {
        new RestClient.Builder()
                .url("daylist/register")
                .param("account", account)
                .param("cipher", password)
                .success(success)
                .error(error)
                .failure(() -> {

                })
                .build()
                .post();
    }

    public static void login(String account, String password, ISuccess success, IError error) {
        new RestClient.Builder()
                .url("daylist/login")
                .param("account", account)
                .param("cipher", password)
                .success(success)
                .error(error)
                .failure(() -> {

                })
                .build()
                .post();
    }

    public static void login(String userId, ISuccess success, IError error) {
        new RestClient.Builder()
                .url("daylist/login")
                .param("userId", userId)
                .success(success)
                .error(error)
                .failure(() -> {

                })
                .build()
                .post();
    }
}
