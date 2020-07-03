package com.rainbow.superschedule.widget.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Created By Rainbow on 2019/4/30.
 */
@SuppressWarnings("unused")
public abstract class BaseRecyclerViewAdapter<T>
        extends RecyclerView.Adapter<RecyclerViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<T> mData;
    private int mLayoutId;

    private OnItemClickListener mListener;

    public BaseRecyclerViewAdapter(Context context, List<T> data, int layoutId) {
        this.mContext = context;
        this.mData = data;
        this.mLayoutId = layoutId;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
        view.setOnClickListener(this);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.itemView.setTag(position);
        T bean = mData.get(position);
        onBindData(holder, bean, position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    protected List<T> getData() {
        return mData;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemClick(this, v, (Integer) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    /**
     * 数据绑定，由实现类实现
     *
     * @param holder   The reference of the all view within the item.
     * @param bean     The data bean related to the position.
     * @param position The position to bind data.
     */
    protected abstract void onBindData(RecyclerViewHolder holder, T bean, int position);

    protected Context getContext() {
        return mContext;
    }

    /**
     * item点击监听器
     */
    public interface OnItemClickListener {
        /**
         * item点击回调
         *
         * @param adapter  The Adapter where the click happened.
         * @param v        The view that was clicked.
         * @param tag      The tag of the view in the adapter.
         */
        void onItemClick(RecyclerView.Adapter adapter, View v, int tag);
    }
}
