package com.rainbow.superschedule.delegate;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rainbow.superschedule.API;
import com.rainbow.superschedule.R;
import com.rainbow.superschedule.core.storage.LattePreference;
import com.rainbow.superschedule.core.ui.PermissionActivity;
import com.rainbow.superschedule.entity.ScheduleBean;
import com.rainbow.superschedule.entity.UploadResponseBean;
import com.rainbow.superschedule.receiver.AlarmReceiver;
import com.rainbow.superschedule.utils.DateUtils;
import com.rainbow.superschedule.widget.selector.DateSelector;
import com.rainbow.superschedule.widget.TitleToolbar;
import com.rainbow.superschedule.widget.selector.RepeatSelector;
import com.rainbow.superschedule.widget.selector.TimeSelector;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * SuperSchedule
 * Created By Rainbow on 2020/5/9.
 */
public class EditActivity extends PermissionActivity implements
        Toolbar.OnMenuItemClickListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "EditActivity";
    private static final int DEFAULT_COLOR = Color.parseColor("#8A000000");

    private TextView tvStartTime, tvEndTime, tvTips;
    private TextView meeting, outing, birthday, anniversary, others;
    private TextView selectResult;
    private Switch tipSwitch;
    private EditText titleEdt;
    private Button delete;
    private EditText locationEdt;

    private DateSelector startDateSelector, endDateSelector;
    private TimeSelector timeSelector;

    private boolean isTips;
    private int scheduleId;
    private String advancedMins;

    //    private int mins;
    private AlarmManager am;
    private PendingIntent pi;
//    private long time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        scheduleId = -1;
        initData();
        initWheelView();

        initAlarm();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ScheduleBean schedule = (ScheduleBean) bundle.getSerializable("schedule");
            if (schedule != null) {
                advancedMins = String.valueOf(schedule.getAdvancedAlarmMins());
                scheduleId = schedule.getScheduleId();
                isTips = (schedule.getIsAlarm() == 1);
                advancedMins = String.valueOf(schedule.getAdvancedAlarmMins());
                tvTips.setText(MessageFormat.format("提前{0}分钟", advancedMins));
                tvStartTime.setText(schedule.getStartTime());
                tvEndTime.setText(schedule.getEndTime());
                tipSwitch.setChecked(isTips);
                titleEdt.setText(schedule.getDescription());
                locationEdt.setText(schedule.getLocation());
                switch (schedule.getRemarks()) {
                    case "会议":
                        changeLabel(meeting);
                        break;
                    case "出行":
                        changeLabel(outing);
                        break;
                    case "生日":
                        changeLabel(birthday);
                        break;
                    case "纪念日":
                        changeLabel(anniversary);
                        break;
                    case "其他":
                        changeLabel(others);
                        break;
                }
            }
        } else {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, 12);
            c.set(Calendar.MINUTE, 0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            tvStartTime.setText(sdf.format(c.getTime()));
            c.set(Calendar.HOUR_OF_DAY, 13);
            tvEndTime.setText(sdf.format(c.getTime()));
        }
    }

    private void initView() {
        TitleToolbar toolbar = $(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        toolbar.setOnMenuItemClickListener(this);

        meeting = $(R.id.tv_meeting);
        outing = $(R.id.tv_outing);
        birthday = $(R.id.tv_birthday);
        anniversary = $(R.id.tv_anniversary);
        others = $(R.id.tv_others);
        tipSwitch = $(R.id.sw_tip_status);
        titleEdt = $(R.id.edt_title);
        LinearLayout time = $(R.id.ll_time);
        LinearLayout tips = $(R.id.ll_tips);
        tvStartTime = $(R.id.tv_start_time);
        tvEndTime = $(R.id.tv_end_time);
        locationEdt = $(R.id.edt_location);
        tvTips = $(R.id.tv_tips);
        delete = $(R.id.btn_delete);

        // 默认选中会议
        selectResult = meeting;

        meeting.setOnClickListener(this);
        outing.setOnClickListener(this);
        birthday.setOnClickListener(this);
        anniversary.setOnClickListener(this);
        others.setOnClickListener(this);
        time.setOnClickListener(this);
        tips.setOnClickListener(this);
        tipSwitch.setOnCheckedChangeListener(this);
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    private void initWheelView() {
        String[] startTimes = DateUtils.parseTime(tvStartTime.getText().toString());
        if (startTimes.length != 5) {
            throw new RuntimeException("time form is wrong!");
        }
        List<String> defaultStartTime = Arrays.asList(startTimes);
        startDateSelector = new DateSelector(this, defaultStartTime);
        startDateSelector.setOnConfirmClickListener((year, month, day, hour, min) -> {
            tvStartTime.setText(String.format("%s-%s-%s %s:%s", year, month, day, hour, min));
        });

        String[] endTimes = DateUtils.parseTime(tvEndTime.getText().toString());
        if (endTimes.length != 5) {
            throw new RuntimeException("time form is wrong!");
        }
        List<String> defaultEndTime = Arrays.asList(endTimes);
        endDateSelector = new DateSelector(this, defaultEndTime);
        endDateSelector.setOnConfirmClickListener((year, month, day, hour, min) -> {
            tvEndTime.setText(String.format("%s-%s-%s %s:%s", year, month, day, hour, min));
        });

        String text = tvTips.getText().toString();
        advancedMins = text.substring(text.indexOf("提前") + 2, text.indexOf("分钟"));
        timeSelector = new TimeSelector(this, advancedMins);
        timeSelector.setOnConfirmClickListener(((min) -> {
            advancedMins = min;
            tvTips.setText(String.format("提前%s分钟", min));
        }));
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_edit;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_save:
//                Log.d(TAG, "fuck");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
                try {
                    final Date start = sdf.parse(tvStartTime.getText().toString());
                    final Date end = sdf.parse(tvEndTime.getText().toString());
                    final String remarks = selectResult.getText().toString();
                    final String description = titleEdt.getText().toString();
                    final String location = locationEdt.getText().toString();

                    if (isTips) {
                        cancelAlarm();
                        setAlarm();
                    } else {
                        cancelAlarm();
                    }
                    API.uploadSchedule(scheduleId, isTips,
                            Integer.parseInt(advancedMins), start, end, location, description, remarks,
                            (response -> {
                                UploadResponseBean bean = new Gson().fromJson(response, UploadResponseBean.class);
                                if (bean.getStatus() == 1) {
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

                                    ScheduleBean schedule = new ScheduleBean(bean.getScheduleId(),
                                            Integer.parseInt(LattePreference.getCustomAppProfile("user-id")),
                                            isTips ? 1 : 0, Integer.parseInt(advancedMins),
                                            sdf.format(start), sdf.format(end), location, description, remarks);
                                    scheduleList.add(schedule);
                                    scheduleHistoryList.add(schedule);
                                    String userItems = new Gson().toJson(scheduleList);
                                    String userHistoryItems = new Gson().toJson(scheduleHistoryList);
                                    LattePreference.addCustomAppProfile("user-items", userItems);
                                    LattePreference.addCustomAppProfile("user-items-history", userHistoryItems);
                                    finish();
                                } else {
                                    Toast.makeText(EditActivity.this, "上传失败，请重试", Toast.LENGTH_SHORT).show();
                                }
                            }));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        return false;
    }

    private void initAlarm() {
        pi = PendingIntent.getBroadcast(this, 0, getMsgIntent(), 0);
//        time = System.currentTimeMillis();
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    private Intent getMsgIntent() {
        // AlarmReceiver 为广播在下面代码中
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setAction(AlarmReceiver.BC_ACTION);
        intent.putExtra("msg", "闹钟开启");
        return intent;
    }

    private void setAlarm() {
        //android Api的改变不同版本中设 置有所不同
        if (Build.VERSION.SDK_INT < 19) {
            am.set(AlarmManager.RTC_WAKEUP, getTimeDiff(), pi);
        } else {
            am.setExact(AlarmManager.RTC_WAKEUP, getTimeDiff(), pi);
        }
    }

    public long getTimeDiff() {
        // 设置时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        Calendar ca = Calendar.getInstance();
        try {
            ca.setTime(sdf.parse(tvStartTime.getText().toString()));
            ca.add(Calendar.MINUTE, -Integer.parseInt(advancedMins));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ca.getTimeInMillis();
    }

    //取消定时任务的执行
    private void cancelAlarm() {
        am.cancel(pi);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_meeting:
                changeLabel(meeting);
                break;
            case R.id.tv_outing:
                changeLabel(outing);
                break;
            case R.id.tv_birthday:
                changeLabel(birthday);
                break;
            case R.id.tv_anniversary:
                changeLabel(anniversary);
                break;
            case R.id.tv_others:
                changeLabel(others);
                break;
            case R.id.tv_start_time:
                startDateSelector.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.tv_end_time:
                endDateSelector.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.ll_tips:
                timeSelector.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.btn_delete:
                API.deleteScheduleItem(scheduleId, (response) -> {
                    JsonArray array = JsonParser.parseString(LattePreference.getCustomAppProfile("user-items")).getAsJsonArray();
                    List<ScheduleBean> scheduleList = new ArrayList<>();
                    for (JsonElement e : array) {
                        ScheduleBean b = new Gson().fromJson(e, ScheduleBean.class);
                        if (b.getScheduleId() != scheduleId) {
                            // 删除指定id的数据
                            scheduleList.add(b);
                        }
                    }
                    // 重新保存
                    String userItems = new Gson().toJson(scheduleList);
                    LattePreference.addCustomAppProfile("user-items", userItems);
                    finish();
                });
                break;
            default:
                break;
        }
    }

    private void changeLabel(final TextView select) {
        if (selectResult == select) {
            return;
        }
        select.setBackgroundResource(R.drawable.label_corner_selected);
        select.setTextColor(Color.WHITE);
        selectResult.setBackgroundResource(R.drawable.label_corner_unselected);
        selectResult.setTextColor(DEFAULT_COLOR);
        selectResult = select;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        isTips = b;
    }
}
