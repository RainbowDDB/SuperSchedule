package com.rainbow.superschedule.core;

import android.app.Application;
import android.content.Context;

/**
 * SuperSchedule
 * Created By Rainbow on 2020/6/29.
 */
public class App extends Application {

    private static Context context;

    public static Context getContext(){
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
