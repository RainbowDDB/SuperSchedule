package com.rainbow.superschedule.delegate;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.rainbow.superschedule.API;
import com.rainbow.superschedule.R;
import com.rainbow.superschedule.core.storage.LattePreference;
import com.rainbow.superschedule.core.ui.PermissionActivity;
import com.rainbow.superschedule.entity.AccountResponseBean;

/**
 * SuperSchedule
 * Created By Rainbow on 2020/6/29.
 */
public class AccountActivity extends PermissionActivity implements View.OnClickListener {
    private static final String TAG = "AccountActivity";

    private EditText account;
    private EditText password;
    private Button login, register;
    private TextView errMsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        account = $(R.id.edt_account);
        password = $(R.id.edt_password);
        login = $(R.id.btn_login);
        register = $(R.id.btn_register);
        errMsg = $(R.id.tv_error_msg);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_account;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login(account.getText().toString(), password.getText().toString());
                break;
            case R.id.btn_register:
                String ac = account.getText().toString();
                String ps = password.getText().toString();
                API.register(ac, ps,
                        response -> {
                            login(ac, ps);
                        },
                        (code, msg) -> {
                            Log.e(TAG, code + ":" + msg);
                        });
                break;
            default:
                break;
        }
    }

    private void login(String account, String password) {
        API.login(account, password, (loginResponse -> {
            Toast.makeText(AccountActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            AccountResponseBean accountResponse = new Gson().fromJson(loginResponse, AccountResponseBean.class);
            LattePreference.addCustomAppProfile("user-id", String.valueOf(accountResponse.getUserId()));
            LattePreference.addCustomAppProfile("account", account);
            start(HomeActivity.class);
        }), (code, msg) -> {
            Log.e(TAG, code + ":" + msg);
            if (code == 400) {
                errMsg.setText("请检查账户名是否为十位数字，密码为8-20位数字密码");
            } else {
                errMsg.setText(code + ":" + msg);
            }
        });
    }
}
