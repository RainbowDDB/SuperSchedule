package com.rainbow.superschedule.delegate;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.rainbow.superschedule.R;
import com.rainbow.superschedule.adapter.ScheduleListAdapter;
import com.rainbow.superschedule.core.storage.LattePreference;
import com.rainbow.superschedule.core.ui.PermissionActivity;
import com.rainbow.superschedule.entity.ScheduleBean;
import com.rainbow.superschedule.widget.TitleToolbar;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * SuperSchedule
 * Created By Rainbow on 2020/5/8.
 */
public class HistoryActivity extends PermissionActivity
        implements View.OnClickListener, CalendarView.OnCalendarSelectListener {

    private TextView tvDate;
    private RecyclerView recyclerView;
    private CalendarView calendarView;
    private List<ScheduleBean> scheduleList;
    private ScheduleListAdapter mAdapter;
    private TextView noneTip;

    private boolean isInited;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
        initRecyclerView();
    }

    private void initView() {
        TitleToolbar toolbar = $(R.id.toolbar);
        setSupportActionBar(toolbar.getToolbar());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        tvDate = $(R.id.tv_date);
        recyclerView = $(R.id.recyclerView);
        calendarView = $(R.id.calendar_view);
        noneTip = $(R.id.tv_none_tips);

        calendarView.setOnMonthChangeListener((year, month) ->
                tvDate.setText(MessageFormat.format("{0}年 {1}月", year, month)));
        calendarView.setOnCalendarSelectListener(this);
    }

    private void initData() {
        scheduleList = new ArrayList<>();
        int year = calendarView.getCurYear();
        int month = calendarView.getCurMonth();
        int day = calendarView.getCurDay();
        tvDate.setText(MessageFormat.format("{0}年{1}月", year, month));
        updateData(year, month, day);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isInited) {
            int year = calendarView.getCurYear();
            int month = calendarView.getCurMonth();
            int day = calendarView.getCurDay();
            updateData(year, month, day);
        }
        isInited = true;
    }

    private void initRecyclerView() {
        mAdapter = new ScheduleListAdapter(this, scheduleList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(mAdapter);
        if (scheduleList.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            noneTip.setVisibility(View.VISIBLE);
        }
        mAdapter.setOnItemClickListener(null);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_history;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        int year = calendar.getYear();
        int month = calendar.getMonth();
        int day = calendar.getDay();

        updateData(year, month, day);
        mAdapter.notifyDataSetChanged();
        if (scheduleList.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            noneTip.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noneTip.setVisibility(View.GONE);
        }
    }

    private Date getDate(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        try {
            return sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateData(int year, int month, int day) {
        JsonArray array = JsonParser
                .parseString(LattePreference.getCustomAppProfile("user-items-history"))
                .getAsJsonArray();
        scheduleList.clear();
        for (JsonElement e : array) {
            ScheduleBean b = new Gson().fromJson(e, ScheduleBean.class);
            Date d = getDate(b.getStartTime());
            if (d != null) {
                java.util.Calendar c = java.util.Calendar.getInstance();
                c.setTime(d);
                if (year == c.get(java.util.Calendar.YEAR)
                        && month == c.get(java.util.Calendar.MONTH) + 1
                        && day == c.get(java.util.Calendar.DAY_OF_MONTH)) {
                    scheduleList.add(b);
                }
            }
        }
    }
}
