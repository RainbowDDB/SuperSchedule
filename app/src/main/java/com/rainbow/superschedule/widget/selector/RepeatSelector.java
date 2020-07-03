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
import java.util.List;

/**
 * SuperSchedule
 * Created By Rainbow on 2020/6/22.
 */
public class RepeatSelector extends MaskedPopupWindow implements View.OnClickListener {

    private final ArrayList<String> REPEATS = new ArrayList<>();

    private OnConfirmClickListener listener;
    private Context mContext;
    private View view;
    private WheelView repeatWheel;

    private List<String> data = new ArrayList<>();

    @SuppressLint("InflateParams")
    public RepeatSelector(Context context, boolean repeated) {
        this.mContext = context;
        if (repeated) {
            this.data.add("重复");
        } else {
            this.data.add("不重复");
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            view = inflater.inflate(R.layout.widget_repeat_selector, null);
            setContentView(view);
        }

        init();
    }

    private void init() {
        setFocusable(true);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);

        REPEATS.add("重复");
        REPEATS.add("不重复");

        repeatWheel = view.findViewById(R.id.repeat_wheel_view);

        repeatWheel.setData(REPEATS);


        try {
            repeatWheel.setCenterItem(data.get(0));
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
                boolean repeated = repeatWheel.getCenterData().equals("重复");
                if (listener != null) {
                    listener.onConfirmClick(repeated);
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
        void onConfirmClick(boolean repeated);
    }
}
