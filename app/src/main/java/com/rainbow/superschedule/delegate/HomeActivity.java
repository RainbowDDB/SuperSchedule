package com.rainbow.superschedule.delegate;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rainbow.superschedule.API;
import com.rainbow.superschedule.adapter.SchedulePagerAdapter;
import com.rainbow.superschedule.core.storage.LattePreference;
import com.rainbow.superschedule.core.ui.PermissionActivity;
import com.rainbow.superschedule.R;
import com.rainbow.superschedule.entity.ScheduleBean;
import com.rainbow.superschedule.entity.UserItemBean;
import com.rainbow.superschedule.widget.TitleToolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends PermissionActivity implements
        Toolbar.OnMenuItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";

    private TitleToolbar mToolbar;
    private TabLayout mTabLayout;
    private FloatingActionButton mFab;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ViewPager mViewPager;
    private List<Fragment> fragments;
    private TextView account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_home;
    }

    private void initView() {
        mDrawerLayout = $(R.id.drawer_layout);
        mToolbar = $(R.id.toolbar);
        mTabLayout = $(R.id.tabs);
        mViewPager = $(R.id.view_pager);
        mFab = $(R.id.add_fab);

        mToolbar.setTitle("今日");
        mToolbar.setOnMenuItemClickListener(this);

        mToolbar.setNavigationOnClickListener(view -> {
            if (!mDrawerLayout.isDrawerOpen(mNavigationView)) {
                mDrawerLayout.openDrawer(mNavigationView);
            }
        });
        mFab.setOnClickListener(view -> start(EditActivity.class));

        mNavigationView = $(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        account = mNavigationView.getHeaderView(0).findViewById(R.id.tv_account);
        account.setText(LattePreference.getCustomAppProfile("account"));
        account.setOnClickListener((view) -> {
            start(AccountActivity.class);
        });

        String[] titles = {"日", "一", "二", "三", "四", "五", "六"};
        fragments = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            fragments.add(new ScheduleListFragment(i));
        }
        mViewPager.setAdapter(new SchedulePagerAdapter(getSupportFragmentManager(), fragments, titles));
        mTabLayout.setupWithViewPager(mViewPager);

        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        mViewPager.setCurrentItem(c.get(Calendar.DAY_OF_WEEK)-1);
    }

    private void initData() {
        API.getUserItems((response) -> {
            UserItemBean userItemBean = new Gson().fromJson(response, UserItemBean.class);
            saveLocal(userItemBean.getScheduleList());
            for (int i = 0; i < 7; i++) {
                ((ScheduleListFragment) fragments.get(i)).notifyDataChanged();
            }
        });
    }

    private void saveLocal(List<ScheduleBean> list){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        if (LattePreference.getCustomAppProfile("user-items").equals("")){
            LattePreference.addCustomAppProfile("user-items","[]");
        }
        if (LattePreference.getCustomAppProfile("user-items-history").equals("")){
            LattePreference.addCustomAppProfile("user-items-history","[]");
        }
        for (ScheduleBean bean:list) {
            // 保存到本地
            JsonArray array = JsonParser.parseString(LattePreference.getCustomAppProfile("user-items")).getAsJsonArray();
            List<ScheduleBean> scheduleList = new ArrayList<>();
            for (JsonElement e : array) {
                ScheduleBean b = new Gson().fromJson(e, ScheduleBean.class);
                if (b.getScheduleId() == bean.getScheduleId()) {
                    // id相同则后续请求成功后添加
                    continue;
                }
                scheduleList.add(b);
            }

            JsonArray historyArray = JsonParser.parseString(LattePreference.getCustomAppProfile("user-items-history")).getAsJsonArray();
            List<ScheduleBean> scheduleHistoryList = new ArrayList<>();
            for (JsonElement e : historyArray) {
                ScheduleBean b = new Gson().fromJson(e, ScheduleBean.class);
                if (b.getScheduleId() == bean.getScheduleId()) {
                    // id相同则后续请求成功后添加
                    continue;
                }
                scheduleHistoryList.add(b);
            }

            scheduleList.add(bean);
            scheduleHistoryList.add(bean);
            String userItems = new Gson().toJson(scheduleList);
            String userHistoryItems = new Gson().toJson(scheduleHistoryList);
            LattePreference.addCustomAppProfile("user-items", userItems);
            LattePreference.addCustomAppProfile("user-items-history", userHistoryItems);
        }
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_history:
                start(HistoryActivity.class);
//                API.uploadSchedule();
//                API.login("Rainbow", String.valueOf(123456));
                break;
            default:
                Log.i(TAG, String.valueOf(item.getItemId()));
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mNavigationView)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
