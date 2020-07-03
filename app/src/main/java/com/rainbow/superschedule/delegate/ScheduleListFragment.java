package com.rainbow.superschedule.delegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rainbow.superschedule.R;
import com.rainbow.superschedule.adapter.ScheduleListAdapter;
import com.rainbow.superschedule.core.storage.LattePreference;
import com.rainbow.superschedule.entity.ScheduleBean;
import com.rainbow.superschedule.widget.recycler.BaseRecyclerViewAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * SuperSchedule
 * Created By Rainbow on 2020/5/8.
 */
public class ScheduleListFragment extends Fragment {

    private View mRootView;
    private TextView mScheduleText;
    private RecyclerView mRecyclerView;
    private ScheduleListAdapter mAdapter;
    private TextView noneTip;

    private List<ScheduleBean> scheduleList;
    private int mDay;
    private boolean isInit;

    public ScheduleListFragment(int day) {
        super();
        mDay = day;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.schedule_list_fragment, container, false);
        initView();
        return mRootView;
    }

    private void initView() {
        mScheduleText = mRootView.findViewById(R.id.tv_schedule_text);
        mRecyclerView = mRootView.findViewById(R.id.tv_schedule);
        noneTip = mRootView.findViewById(R.id.tv_none_tips);


        scheduleList = new ArrayList<>();
        isInit = true;

        updateData();
//        for (ScheduleBean schedule : scheduleList) {
//            Calendar c = Calendar.getInstance();
//            c.setTime(new Date());
//            int s = c.get(Calendar.DAY_OF_WEEK);
//            c.add(Calendar.DAY_OF_WEEK, mDay + 1 - s);
//            // 获取此页应该的日期日程
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH);
//            int day = c.get(Calendar.DAY_OF_MONTH);
//
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
//            try {
//                c.setTime(sdf.parse(schedule.getStartTime()));
//                // 移除多余项
//                if (year == c.get(Calendar.YEAR)
//                        && month == c.get(Calendar.MONTH)
//                        && day == c.get(Calendar.DAY_OF_MONTH)) {
//                    Log.d("test", "test");
//                    scheduleRealList.add(schedule);
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
        mAdapter = new ScheduleListAdapter(getContext(), scheduleList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
//        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        if (scheduleList.size() == 0) {
            mScheduleText.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            noneTip.setVisibility(View.VISIBLE);
        }
        mAdapter.setOnItemClickListener((adapter, v, tag) -> {
            // 点击此view
            Intent intent = new Intent(getContext(), EditActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("schedule", scheduleList.get(tag));
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        notifyDataChanged();
    }

    public void notifyDataChanged() {
        // 如果并未初始化
        if (!isInit) {
            return;
        }
        updateData();
        if (scheduleList.size() != 0) {
            mScheduleText.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            noneTip.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        } else {
            mScheduleText.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            noneTip.setVisibility(View.VISIBLE);
        }
    }

    private void updateData() {
        String schedule = LattePreference.getCustomAppProfile("user-items");
        if (schedule != null && !schedule.equals("")) {
            scheduleList.clear();
            JsonArray scheduleArray = JsonParser.parseString(schedule).getAsJsonArray();

            if (scheduleArray != null) {
                for (JsonElement element : scheduleArray) {
                    ScheduleBean scheduleBean = new Gson().fromJson(element, ScheduleBean.class);
                    // 判断是否需要添加
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date());
                    int s = c.get(Calendar.DAY_OF_WEEK);
                    c.add(Calendar.DAY_OF_WEEK, mDay + 1 - s);
                    // 获取此页应该的日期日程
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
                    try {
                        c.setTime(sdf.parse(scheduleBean.getStartTime()));
                        // 移除多余项
                        if (year == c.get(Calendar.YEAR)
                                && month == c.get(Calendar.MONTH)
                                && day == c.get(Calendar.DAY_OF_MONTH)) {
                            Log.d("test", "test");
//                            scheduleRealList.add(schedule);
                            scheduleList.add(scheduleBean);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
