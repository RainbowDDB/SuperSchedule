package com.rainbow.superschedule.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainbow.superschedule.R;
import com.rainbow.superschedule.delegate.EditActivity;
import com.rainbow.superschedule.entity.ScheduleBean;
import com.rainbow.superschedule.widget.recycler.BaseRecyclerViewAdapter;
import com.rainbow.superschedule.widget.recycler.RecyclerViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * SuperSchedule
 * Created By Rainbow on 2020/5/9.
 */
public class ScheduleListAdapter extends BaseRecyclerViewAdapter<ScheduleBean> {

    public ScheduleListAdapter(Context context, List<ScheduleBean> data) {
        super(context, data, R.layout.item_schedule);
    }

    @Override
    protected void onBindData(RecyclerViewHolder holder, ScheduleBean bean, int position) {
        TextView timeAndRemarks = (TextView) holder.getView(R.id.tv_time_remarks);
        TextView description = (TextView) holder.getView(R.id.tv_description);
        ImageView ring = (ImageView) holder.getView(R.id.img_ring);

        String start = bean.getStartTime();
        String end = bean.getEndTime();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
//        sdf.f
        timeAndRemarks.setText(String.format("%s-%s %s",
                start.substring(start.indexOf(" ") + 1), end.substring(end.indexOf(" ") + 1), bean.getRemarks()));
        description.setText(bean.getDescription());
        ring.setVisibility(bean.getIsAlarm() == 1 ? View.VISIBLE : View.GONE);
    }
}
