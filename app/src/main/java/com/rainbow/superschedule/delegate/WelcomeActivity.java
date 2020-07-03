package com.rainbow.superschedule.delegate;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.rainbow.superschedule.API;
import com.rainbow.superschedule.R;
import com.rainbow.superschedule.core.storage.LattePreference;
import com.rainbow.superschedule.core.ui.PermissionActivity;
import com.rainbow.superschedule.utils.TimerHelper;

/**
 * SuperSchedule
 * Created By Rainbow on 2020/7/1.
 */
public class WelcomeActivity extends PermissionActivity {

    private static final String TAG = "WelcomeActivity";
    private TimerHelper mTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTimer = new TimerHelper() {
            @Override
            public void run() {
                // 如果是首次进入
                if (!LattePreference.getAppFlag("entered")) {
                    LattePreference.setAppFlag("entered", true);
                    WelcomeActivity.this.start(AccountActivity.class);
                    finish();
                } else {
                    String userId = LattePreference.getCustomAppProfile("user-id");
                    if (userId == null) {
                        WelcomeActivity.this.start(AccountActivity.class);
                        finish();
                    } else {
                        API.login(userId, (response -> {
                            WelcomeActivity.this.start(HomeActivity.class);
                            finish();
                        }), (code, msg) -> {
                            Log.e("login", code + ":" + msg);
                            if (code == 401) {
                                WelcomeActivity.this.start(AccountActivity.class);
                            }
//                            finish();
                        });
                    }
                }
            }
        };
        mTimer.start(2000);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_welcome;
    }
}
