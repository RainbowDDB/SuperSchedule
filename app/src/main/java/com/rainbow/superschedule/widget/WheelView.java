package com.rainbow.superschedule.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import androidx.annotation.Nullable;

import com.rainbow.superschedule.utils.UIHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 滚动选择器
 * 使用方法：声明变量后，setData即可使用
 * 可通过set方法设置各种属性
 *
 * Created By Rainbow on 2019/4/30.
 */
public class WheelView extends View {
    private static final int RATE = 120;  // 惯性滑动比率，rate越大，速率越快

    private Context mContext;

    private int showNumber = 5;           // 展示Item的个数
    private float textSize = 16;          // 文字的大小
    private boolean isCircle = false;     // 是否为环形

    private int width;                    // view的宽度
    private int height;                   // view的高度
    private int itemHeight;               // item的高度
    private int itemX;                    // item的X位置

    private int realHeight;               // 内容的实际高度
    private int minScrollY;               // 最小滚动高度
    private int maxScrollY;               // 最大滚动高度

    private float scrollY = 0;            // 滚动y坐标
    private int cacheNowItem = -1;        // 预设当前item的位置，负数表示不设定

    private Paint mPaint;                 // 文字层
    private Paint mShadowPaint;           // 遮罩层
    private Paint mBackgroundPaint;       // 背景层
    private int textColor = 0xFF000000;   // 文字颜色

    private int dataSize = 0;             // 数据量
    private ArrayList<String> mData;      // 数据

    private float lastY, downY;           // 上次操作的坐标以及按下时候的坐标
    private long downTime;                // 按下时的时间

    private OverScroller mScroller;       // 滚动控制器

    private boolean isStart = true;       // 是否是首次绘制的判断标志

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        // 解决onDraw方法不执行的问题
        setWillNotDraw(false);

        mScroller = new OverScroller(mContext);
        mData = new ArrayList<>();

        mPaint = new Paint();
        mPaint.setAntiAlias(true); // 抗锯齿
        mPaint.setTextSize(UIHelper.dip2px(mContext, textSize));
        mPaint.setTextAlign(Paint.Align.CENTER); // 居中
        mShadowPaint = new Paint();
        mBackgroundPaint = new Paint();

        // 保证显示的size是奇数，上下对称
        if (showNumber % 2 == 0) {
            showNumber += 1;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        measureSize();
        // 绘制背景层
        canvas.drawRect(0, 0, width, height, mBackgroundPaint);
        // 如果设置了当前选中
        if (cacheNowItem >= 0) {
            scrollY = -(cacheNowItem - (showNumber - 1) / 2) * itemHeight;
            cacheNowItem = -1;
        }
        // 绘制数据的起始位置
        int startItemPos = (int) -scrollY / itemHeight;
        mPaint.setColor(textColor);
        for (int i = startItemPos, j = 0; i < startItemPos + showNumber + 2; j++, i++) {
            float topY = j * itemHeight + scrollY % itemHeight;
            // 绘制数据
            if (i >= 0 && i < dataSize) {
                canvas.drawText(mData.get(i), itemX, getBaseLine(mPaint, topY, itemHeight), mPaint);
            } else {
                // 补充环形绘制
                if (isCircle) {
                    int pos = i % dataSize;
                    canvas.drawText(mData.get(pos < 0 ? pos + dataSize : pos), itemX, getBaseLine(mPaint, topY, itemHeight), mPaint);
                }
            }
        }
        // 绘制遮罩层
        canvas.drawRect(0, 0, width, height, mShadowPaint);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            // 开始触摸
            case MotionEvent.ACTION_DOWN:
                downTime = System.currentTimeMillis();
                downY = event.getRawY();
                lastY = downY;
                performClick();
                break;
            // 正在移动
            case MotionEvent.ACTION_MOVE:
                float y = event.getRawY();
                float dy = y - lastY;
                pretendScrollY(dy);
                lastY = y;
                break;
            // 松开手指
            case MotionEvent.ACTION_UP:
                checkStateAndPosition();
                invalidate();
                break;
        }
        return true;
    }

    // 初始化各种尺寸和画笔
    private void measureSize() {
        // isStart 判断初始化一次就好
        if (isStart) {
            width = getWidth();
            itemX = width / 2;
            height = getHeight();
            // 高度平均分配
            itemHeight = (height - getPaddingTop() - getPaddingBottom()) / showNumber;
            realHeight = dataSize * itemHeight;
            minScrollY = -(getRealHeight() - (showNumber + 1) / 2 * itemHeight);
            maxScrollY = (showNumber - 1) / 2 * itemHeight;
            // 中心Item的高度
            float centerItemHeight = itemHeight;
            // 中心Item的上边距位置
            float centerItemTop = (height - getPaddingTop() - getPaddingBottom()) / 2
                    + getPaddingTop() - centerItemHeight / 2;
            // 中心Item的下边距位置
            float centerItemBottom = (height - getPaddingTop() - getPaddingBottom()) / 2
                    + getPaddingTop() + centerItemHeight / 2;
            // 遮罩层线性渐变
            Shader maskShader = new LinearGradient(0, 0, 0, height,
                    new int[]{0xFFFFFFFF, 0xAAFFFFFF, 0x00FFFFFF, 0x00FFFFFF, 0xAAFFFFFF, 0xFFFFFFFF},
                    new float[]{0.0f, centerItemTop / height, centerItemTop / height,
                            centerItemBottom / height, centerItemBottom / height, 1.0f}, Shader.TileMode.REPEAT);
            mShadowPaint.setShader(maskShader);
            // 背景层线性渐变
            Shader backgroundShader = new LinearGradient(0, 0, 0, height,
                    new int[]{0xFFFFFFFF, 0xAAFFFFFF, 0xFFD7F2FB, 0xFFD7F2FB, 0xAAFFFFFF, 0xFFFFFFFF},
                    new float[]{0.0f, centerItemTop / height, centerItemTop / height, centerItemBottom / height, centerItemBottom / height, 1.0f}, Shader.TileMode.REPEAT);
            mBackgroundPaint.setShader(backgroundShader);
            // 将isStart设置为false
            isStart = false;
        }
    }

    @Override
    public void computeScroll() {
        // scroller的滚动是否完成
        if (mScroller.computeScrollOffset()) {
            scrollY = mScroller.getCurrY();
            invalidate();
        }
        super.computeScroll();
    }

    // 获取item的实际高度
    private int getRealHeight() {
        if (realHeight == 0) {
            realHeight = dataSize * itemHeight;
        }
        return realHeight;
    }

    // 获取基线的y坐标
    private float getBaseLine(Paint paint, float top, float height) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        return (2 * top + height - fontMetrics.bottom - fontMetrics.top) / 2;
    }

    // 获取到该位置的基线y坐标
    private float getBaseLine(int position) {
        return getBaseLine(mPaint, itemHeight * position, itemHeight);
    }

    // 预置y轴滚动
    private void pretendScrollY(float dy) {
        scrollY += dy;
        invalidate();
    }

    // 检查位置和状态
    private void checkStateAndPosition() {
        // 上拉超出
        if (!isCircle && scrollY < -(getRealHeight() - (showNumber + 1) / 2 * itemHeight)) {
            mScroller.startScroll(0, (int) scrollY,
                    0, (showNumber + 1) / 2 * itemHeight - getRealHeight() - (int) scrollY, 400);
        }
        // 下拉超出
        else if (!isCircle && scrollY > (showNumber - 1) / 2 * itemHeight) {
            mScroller.startScroll(0, (int) scrollY,
                    0, (showNumber - 1) / 2 * itemHeight - (int) scrollY, 400);
        } else {
            long endTime = System.currentTimeMillis();
            // 超出滑动时间或者不足滑动距离
            if (endTime - downTime > 250 || Math.abs(lastY - downY) < itemHeight / 2) {
                // 不足一个Item高度的部分
                int dy = (int) scrollY % itemHeight;
                // 如果偏移大于item的一半
                if (Math.abs(dy) > itemHeight / 2) {
                    if (scrollY < 0) {
                        mScroller.startScroll(0, (int) scrollY, 0, -itemHeight - dy);
                    } else {
                        mScroller.startScroll(0, (int) scrollY, 0, itemHeight - dy);
                    }
                } else {
                    mScroller.startScroll(0, (int) scrollY, 0, -dy);
                }
            } else {
                // 滑动距离，和手指滑动距离成正比，和滑动时间成反比
                int finalY = (int) ((scrollY + RATE * (lastY - downY) / (endTime - downTime))) / itemHeight * itemHeight;
                if (!isCircle) {
                    if (finalY < minScrollY) {
                        finalY = minScrollY;
                    } else if (finalY > maxScrollY) {
                        finalY = maxScrollY;
                    }
                }
                mScroller.startScroll(0, (int) scrollY, 0, (int) (finalY - scrollY), 400);
            }
        }
    }

    // 获取中心选中的值在data中的位置
    private int getCenterPosition() {
        if (cacheNowItem >= 0) {
            return cacheNowItem;
        }
        // 对于初始化时未调用setCenterPosition或setCenterItem方法的需要，手动计算
        else {
            // 根据scrollY的值，判断是否在滚动，并强制停止滚动，选中的值调整到应该的位置
            // 不足一个Item高度的部分
            int dy = (int) scrollY % itemHeight;
            // 如果偏移大于item的一半
            if (Math.abs(dy) > itemHeight / 2) {
                if (scrollY < 0) {
                    scrollY = scrollY - itemHeight - dy;
                } else {
                    scrollY = scrollY + itemHeight - dy;
                }
            } else {
                scrollY = scrollY - dy;
            }
            mScroller.forceFinished(true);
            invalidate();
            int nowChecked;
            // 若是环形还需进一步判断
            if (!isCircle) {
                if (scrollY < minScrollY) {
                    nowChecked = dataSize - 1;
                } else if (scrollY > maxScrollY) {
                    nowChecked = 0;
                } else {
                    nowChecked = (int) (-scrollY / itemHeight + (showNumber - 1) / 2);
                }
            } else {
                // 滚轮时，重置scrollY位置，使它出现在限定范围的等效位置
                // 以minScroll为相对0点，进行调整
                if (scrollY < minScrollY || scrollY >= maxScrollY) {
                    int mid = (int) ((scrollY - minScrollY) % realHeight);
                    if (mid < 0) {
                        mid += realHeight;
                    }
                    scrollY = mid + minScrollY;
                }
                nowChecked = (int) (-scrollY / itemHeight + (showNumber - 1) / 2);
            }
            return nowChecked;
        }
    }

    // 设置当前Item的位置
    public void setCenterPosition(int position) throws IOException {
        if (position >= 0 && position < dataSize) {
            cacheNowItem = position;
        } else {
            throw new IOException("数组越位！");
        }
    }

    // 清除数据
    public void clearData() {
        mData.clear();
        invalidate();
    }

    // 设置是否可循环
    public void setIsCircle(boolean isCircle) {
        this.isCircle = isCircle;
    }

    // 设置TextColor
    public void setTextColor(int color) {
        this.textColor = color;
    }

    // 设置TextSize
    public void setTextSize(float textSize) {
        this.textSize = textSize;
        mPaint.setTextSize(UIHelper.dip2px(mContext, textSize));
    }

    // 通知重新绘制
    public void notifyDataSetChanged() {
        isStart = true;
        invalidate();
    }

    // 设置数据
    public void setData(String[] data) {
        mData.addAll(Arrays.asList(data));
        dataSize = mData.size();
    }

    public void setData(ArrayList<String> data) {
        mData = data;
        dataSize = mData.size();
    }

    // 设置显示的数目
    public void setShowNumber(int showNumber) {
        if (showNumber % 2 == 0) {
            this.showNumber = showNumber + 1;
        } else {
            this.showNumber = showNumber;
        }
    }

    // 设置选中内容
    public void setCenterItem(String itemData) throws IOException {
        int size = mData.size();
        for (int i = 0; i < size; i++) {
            if (itemData.equals(mData.get(i))) {
                cacheNowItem = i;
                invalidate();
                return;
            }
        }
        throw new IOException("未找到需选择的项目！");
    }

    // 获取选中内容
    public String getCenterData() {
        return dataSize > 0 ? mData.get(getCenterPosition()) : null;
    }
}
