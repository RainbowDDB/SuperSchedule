package com.rainbow.superschedule.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.google.android.material.appbar.MaterialToolbar;
import com.rainbow.superschedule.R;

/**
 * SuperSchedule
 * Created By Rainbow on 2020/5/8.
 */
public class TitleToolbar extends RelativeLayout {

    private TextView mTitle;
    private MaterialToolbar mToolbar;

    public TitleToolbar(Context context) {
        this(context, null);
    }

    public TitleToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.toolbarStyle);
    }

    public TitleToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.title_toolbar, this, true);
        mToolbar = findViewById(R.id.my_toolbar);
        mTitle = findViewById(R.id.tv_toolbar_title);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TitleToolbar);

        String title = array.getString(R.styleable.TitleToolbar_title);
        int color = array.getColor(R.styleable.TitleToolbar_titleColor, Color.BLACK);
        int menuId = array.getResourceId(R.styleable.TitleToolbar_menu, 0);
        int navigationIconId = array.getResourceId(R.styleable.TitleToolbar_navigationIcon, 0);

        mToolbar.setTitle("");
        mTitle.setText(title);
        mTitle.setTextColor(color);
        if (menuId != 0) {
            // 当使用menu时，TitleToolbar不作为ActionBar使用，故inflateMenu会失效
            // setSupportActionBar(mToolbar.getToolbar());
            mToolbar.inflateMenu(menuId);
        }
        if (navigationIconId != 0) {
            mToolbar.setNavigationIcon(navigationIconId);
        }
        array.recycle();
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setTitleColor(@ColorInt int color) {
        mTitle.setTextColor(color);
    }

    public void setOnMenuItemClickListener(MaterialToolbar.OnMenuItemClickListener listener) {
        mToolbar.setOnMenuItemClickListener(listener);
    }

    public void setNavigationOnClickListener(OnClickListener listener) {
        mToolbar.setNavigationOnClickListener(listener);
    }

    public MaterialToolbar getToolbar() {
        return mToolbar;
    }
}
