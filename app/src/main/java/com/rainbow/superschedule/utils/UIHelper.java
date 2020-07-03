package com.rainbow.superschedule.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

/**
 * Created By Rainbow on 2019/4/30.
 */
@SuppressWarnings("unused")
public class UIHelper {

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }


    /**
     * 获取屏幕的真实高度，去除底部虚拟按键的高度
     */
    public static int getRealHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display;
        if (windowManager != null) {
            display = windowManager.getDefaultDisplay();
            DisplayMetrics dm = new DisplayMetrics();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealMetrics(dm);
            } else {
                display.getMetrics(dm);
            }
            return dm.heightPixels;
        } else {
            throw new NullPointerException("WindowManager.getDefaultDisplay() is null.");
        }
    }

    /**
     * 获取虚拟按键高度
     */
    public static int getVirtualBarHeight(Context context) {
        return getScreenHeight(context) - getRealHeight(context);
    }

    /**
     * 虚拟按键是否存在
     */
    public static boolean isVirtualBarExist(Context context) {
        return getVirtualBarHeight(context) != 0;
    }

    /**
     * dp转px
     *
     * @param ctx     上下文
     * @param dpValue dp值
     * @return px值
     */
    public static int dip2px(Context ctx, float dpValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    // 使状态栏变为透明，从而实现沉浸式状态栏
    public static void setImmersiveStatusBar(Window window) {
        View decorView = window.getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    // 设置状态栏颜色,请勿与setAndroidNativeLightStatusBar一起使用
    public static void setStatusBarColor(Window window, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    // 监听软键盘状态从而改变ScrollView高度
    // 解决状态栏透明导致的ScrollView滚动失效的bug
    public static void setScrollViewHeight(View decorView, ScrollView scrollView) {
        Rect rect = new Rect();
        decorView.getWindowVisibleDisplayFrame(rect);
        int screenHeight = decorView.getRootView().getHeight();
        // 计算软键盘占有的高度  = 屏幕高度 - 视图可见高度
        int heightDifference = screenHeight - rect.bottom;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) scrollView.getLayoutParams();
        // 设置ScrollView的marginBottom的值为软键盘占有的高度即可
        layoutParams.setMargins(0, 0, 0, heightDifference);
        scrollView.requestLayout();
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        // 获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            // 根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 改变状态栏字体颜色为黑
     */
    public static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }

    /**
     * 根据坐标获取相对应的子控件<br>
     * 在Activity使用
     *
     * @return 目标View
     */
    public static View getViewAtActivity(Activity activity, int x, int y) {
        // 从Activity里获取容器
        View root = activity.getWindow().getDecorView();
        return findViewByXY(root, x, y);
    }

    private static View findViewByXY(View view, int x, int y) {
        View targetView = null;
        if (view instanceof ViewGroup) {
            // 父容器,遍历子控件
            ViewGroup v = (ViewGroup) view;
            for (int i = 0; i < v.getChildCount(); i++) {
                targetView = findViewByXY(v.getChildAt(i), x, y);
                if (targetView != null) {
                    break;
                }
            }
        } else {
            targetView = getTouchTarget(view, x, y);
        }
        return targetView;

    }

    private static View getTouchTarget(View view, int x, int y) {
        View targetView = null;
        // 判断view是否可以聚焦
        ArrayList<View> TouchableViews = view.getTouchables();
        for (View child : TouchableViews) {
            if (isTouchPointInView(child, x, y)) {
                targetView = child;
                break;
            }
        }
        return targetView;
    }

    private static boolean isTouchPointInView(View view, int x, int y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        return view.isClickable() && y >= top && y <= bottom && x >= left && x <= right;
    }

    /**
     * 根据坐标获取相对应的子控件<br>
     * 在重写ViewGroup使用
     */
    public View getViewAtViewGroup(View v, int x, int y) {
        return findViewByXY(v, x, y);
    }

    /**
     * 设置背景透明色
     */
    public static void setBackgroundAlpha(Window window, float bgAlpha) {
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        window.setAttributes(lp);
    }
}
