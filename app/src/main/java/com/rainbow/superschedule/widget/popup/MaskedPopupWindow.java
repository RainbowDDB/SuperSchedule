package com.rainbow.superschedule.widget.popup;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;

import com.rainbow.superschedule.utils.UIHelper;

/**
 * 处理外部遮罩层透明度的PopupWindow
 * 使用方法：extends即可
 * PS：可能会有bug，希望后人完善下去
 *
 * Created By Rainbow on 2019/4/30.
 */
public class MaskedPopupWindow extends PopupWindow {
    public MaskedPopupWindow(Context context) {
        super(context);
    }

    public MaskedPopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaskedPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MaskedPopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MaskedPopupWindow() {
    }

    public MaskedPopupWindow(View contentView) {
        super(contentView);
    }

    public MaskedPopupWindow(int width, int height) {
        super(width, height);
    }

    public MaskedPopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public MaskedPopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    @Override
    public void dismiss() {
        // 恢复背景透明度
        Context context = getContentView().getContext();
        if (context != null) {
            UIHelper.setBackgroundAlpha(((Activity) context).getWindow(), 1.0f);
        }
        super.dismiss();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        changeAlpha();
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void showAsDropDown(View anchor) {
        changeAlpha();
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        changeAlpha();
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        changeAlpha();
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    private void changeAlpha() {
        // 改变背景透明度
        Context context = getContentView().getContext();
        if (context != null) {
            UIHelper.setBackgroundAlpha(((Activity) context).getWindow(), 0.7f);
        }
    }
}
