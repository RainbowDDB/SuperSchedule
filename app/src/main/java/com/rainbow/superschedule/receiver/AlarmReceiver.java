package com.rainbow.superschedule.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * SuperSchedule
 * Created By Rainbow on 2020/7/2.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";
    public static final String BC_ACTION="com.rainbow.schedule.action.BC_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra("msg");
        Log.i(TAG, "get Receiver msg :" + msg);
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
//        showConfirmDialog(context);
    }
}
