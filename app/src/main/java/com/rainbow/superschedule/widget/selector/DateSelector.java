package com.rainbow.superschedule.widget.selector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rainbow.superschedule.R;
import com.rainbow.superschedule.utils.DateUtils;
import com.rainbow.superschedule.widget.WheelView;
import com.rainbow.superschedule.widget.popup.MaskedPopupWindow;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By Rainbow on 2019/4/30.
 */
public class DateSelector extends MaskedPopupWindow implements View.OnClickListener {

    private final ArrayList<String> YEARS = new ArrayList<>();
    private final ArrayList<String> MONTHS = new ArrayList<>();
    private final ArrayList<String> DAYS = new ArrayList<>();
    private final ArrayList<String> HOURS = new ArrayList<>();
    private final ArrayList<String> MINS = new ArrayList<>();

    private OnConfirmClickListener listener;
    private Context mContext;
    private View view;
    private WheelView yearWheel;
    private WheelView monthWheel;
    private WheelView dayWheel;
    private WheelView hourWheel;
    private WheelView minWheel;

    private List<String> data = new ArrayList<>();

    @SuppressLint("InflateParams")
    public DateSelector(Context context, @NotNull List<String> data) {
        this.mContext = context;
        if (data.size() == 0) {
            this.data.add("2000");
            this.data.add("1");
            this.data.add("1");
            this.data.add("0");
            this.data.add("00");
        } else if (data.size() == 5) {
            this.data = data;
        } else {
            throw new RuntimeException("size is not 5, please check data.size()");
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            view = inflater.inflate(R.layout.widget_date_selector, null);
            setContentView(view);
        }

        init();
    }

    private void init() {
        setFocusable(true);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);

        for (int i = 0; i < DateUtils.getYear() - 1950 + 50; i++) {
            YEARS.add(String.valueOf(i + 1951));
        }
        for (int i = 0; i < 12; i++) {
            MONTHS.add(String.valueOf(i + 1 < 10 ? "0" + (i + 1) : (i + 1)));
        }
        for (int i = 0; i < 31; i++) {
            DAYS.add(String.valueOf( i + 1 < 10 ? "0" + (i + 1) : (i + 1)));
        }
        for (int i = 0; i < 24; i++) {
            HOURS.add(String.valueOf(i < 10 ? "0" + i : i));
        }
        for (int i = 0; i < 60; i++) {
            MINS.add(String.valueOf(i < 10 ? "0" + i : i));
        }

        yearWheel = view.findViewById(R.id.year_wheel_view);
        monthWheel = view.findViewById(R.id.month_wheel_view);
        dayWheel = view.findViewById(R.id.day_wheel_view);
        hourWheel = view.findViewById(R.id.hour_wheel_view);
        minWheel = view.findViewById(R.id.min_wheel_view);

        yearWheel.setData(YEARS);
        monthWheel.setData(MONTHS);
        dayWheel.setData(DAYS);
        hourWheel.setData(HOURS);
        minWheel.setData(MINS);
        yearWheel.setIsCircle(false);
        monthWheel.setIsCircle(true);
        dayWheel.setIsCircle(true);
        hourWheel.setIsCircle(true);
        minWheel.setIsCircle(true);

        try {
            yearWheel.setCenterItem(data.get(0));
            monthWheel.setCenterItem(data.get(1));
            dayWheel.setCenterItem(data.get(2));
            hourWheel.setCenterItem(data.get(3));
            minWheel.setCenterItem(data.get(4));
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView cancel = view.findViewById(R.id.tv_cancel);
        TextView confirm = view.findViewById(R.id.tv_confirm);

        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_confirm:
                String year = yearWheel.getCenterData();
                String month = monthWheel.getCenterData();
                String day = dayWheel.getCenterData();
                String hour = hourWheel.getCenterData();
                String min = minWheel.getCenterData();
                if (Integer.parseInt(day) >
                        DateUtils.getDaysOfMonth(Integer.parseInt(year), Integer.parseInt(month))) {
                    Toast.makeText(mContext, "请检查日期哦~", Toast.LENGTH_SHORT).show();
                } else {
                    if (listener != null) {
                        listener.onConfirmClick(year, month, day, hour, min);
                    }
                    dismiss();
                }
                break;
            default:
                break;
        }
    }

    public void setOnConfirmClickListener(OnConfirmClickListener listener) {
        this.listener = listener;
    }

    public interface OnConfirmClickListener {
        void onConfirmClick(String year, String month, String day, String hour, String min);
    }
}
