package com.rainbow.superschedule.widget.selector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainbow.superschedule.R;
import com.rainbow.superschedule.widget.WheelView;
import com.rainbow.superschedule.widget.popup.MaskedPopupWindow;

import java.io.IOException;
import java.util.ArrayList;

/**
 * SuperSchedule
 * Created By Rainbow on 2020/6/22.
 */
public class TimeSelector extends MaskedPopupWindow implements View.OnClickListener {

    private final ArrayList<String> MINS = new ArrayList<>();

    private OnConfirmClickListener listener;
    private Context mContext;
    private View view;
    private WheelView minWheel;

    private String data;

    @SuppressLint("InflateParams")
    public TimeSelector(Context context, String data) {
        this.mContext = context;
        this.data = data;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            view = inflater.inflate(R.layout.widget_time_selector, null);
            setContentView(view);
        }

        init();
    }

    private void init() {
        setFocusable(true);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);

        for (int i = 0; i < 60; i++) {
            MINS.add(String.valueOf(i < 10 ? "0" + i : i));
        }

        minWheel = view.findViewById(R.id.min_wheel_view);

        minWheel.setData(MINS);
        minWheel.setIsCircle(true);

        try {
            minWheel.setCenterItem(data);
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
                String min = minWheel.getCenterData();
                if (listener != null) {
                    listener.onConfirmClick(min);
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    public void setOnConfirmClickListener(OnConfirmClickListener listener) {
        this.listener = listener;
    }

    public interface OnConfirmClickListener {
        void onConfirmClick(String min);
    }
}
