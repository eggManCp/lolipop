package com.daliy.txtreader.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.BatteryManager;
import android.util.AttributeSet;
import android.view.View;

import com.bifan.txtreaderlib.R;

/**
 * @author FengZhongChan
 * @date 2022/6/29 10:49
 */
public class BatteryView extends View {
    private int mPower = 100;
    private int orientation;
    private int width;
    private int height;
    private int mColor;
    private int mBatteryNum = 0;
    private Activity mActivity;

    private int strokeColor = Color.parseColor("#858582");

    private BatteryLevelReceiver receiver;

    public BatteryView(Context context) {
        super(context);
    }

    public BatteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Battery);
        mColor = typedArray.getColor(R.styleable.Battery_batteryColor, 0xFFFFFFFF);
        orientation = typedArray.getInt(R.styleable.Battery_batteryOrientation, 0);
        mPower = typedArray.getInt(R.styleable.Battery_batteryPower, 100);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        /**
         * recycle() :官方的解释是：回收TypedArray，以便后面重用。在调用这个函数后，你就不能再使用这个TypedArray。
         * 在TypedArray后调用recycle主要是为了缓存。当recycle被调用后，这就说明这个对象从现在可以被重用了。
         *TypedArray 内部持有部分数组，它们缓存在Resources类中的静态字段中，这样就不用每次使用前都需要分配内存。
         */
        typedArray.recycle();
    }

    public void registerBattery(Activity activity) {
        mActivity = activity;
        receiver = new BatteryLevelReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mActivity.registerReceiver(receiver, filter);
    }

    public void releaseReceiver() {
        mActivity.unregisterReceiver(receiver);
        mActivity = null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //对View上的內容进行测量后得到的View內容占据的宽度
        width = getMeasuredWidth();
        //对View上的內容进行测量后得到的View內容占据的高度
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //判断电池方向    horizontal: 0   vertical: 1
        if (orientation == 0) {
            drawHorizontalBattery(canvas);
        } else {
            drawVerticalBattery(canvas);
        }
    }

    /**
     * 绘制水平电池
     *
     * @param canvas
     */
    private void drawHorizontalBattery(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        paint.setColor(mColor);
        paint.setStyle(Paint.Style.STROKE);

        float strokeWidth = width / 20.f;
        float strokeWidth_2 = strokeWidth / 2;
        paint.setStrokeWidth(strokeWidth);
        RectF r1 = new RectF(strokeWidth_2, strokeWidth_2, width - strokeWidth - strokeWidth_2, height - strokeWidth_2);
        //设置外边框颜色为黑色
        paint.setColor(strokeColor);
        canvas.drawRoundRect(r1, 8f, 8f, paint);

        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.FILL);
        //画电池头
        RectF r3 = new RectF(width - strokeWidth, height * 0.25f, width, height * 0.75f);
        //设置电池头颜色为黑色
        paint.setColor(strokeColor);
        canvas.drawRect(r3, paint);

        //画电池内矩形电量
        float offset = (width - strokeWidth * 2) * mPower / 100.f;
        RectF r2 = new RectF(strokeWidth, strokeWidth, offset, height - strokeWidth);
        //根据电池电量决定电池内矩形电量颜色
        paint.setColor(Color.parseColor("#33000000"));
        canvas.drawRoundRect(r2, 8f, 8f, paint);

        if (mBatteryNum > 0) {
            Paint textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(20f);
            canvas.drawText(mBatteryNum + "%", offset / 10, height / 2 + 8, textPaint);
        }
    }

    /**
     * 绘制垂直电池
     *
     * @param canvas
     */
    private void drawVerticalBattery(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(mColor);
        paint.setStyle(Paint.Style.STROKE);
        float strokeWidth = height / 20.0f;
        float strokeWidth2 = strokeWidth / 2;
        paint.setStrokeWidth(strokeWidth);
        int headHeight = (int) (strokeWidth + 0.5f);
        RectF rect = new RectF(strokeWidth2, headHeight + strokeWidth2, width - strokeWidth2, height - strokeWidth2);
        canvas.drawRect(rect, paint);
        paint.setStrokeWidth(0);
        float topOffset = (height - headHeight - strokeWidth) * (100 - mPower) / 100.0f;
        RectF rect2 = new RectF(strokeWidth, headHeight + strokeWidth + topOffset, width - strokeWidth, height - strokeWidth);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect2, paint);
        RectF headRect = new RectF(width / 4.0f, 0, width * 0.75f, headHeight);
        canvas.drawRect(headRect, paint);
    }

    /**
     * 设置电池电量
     *
     * @param power
     */
    public void setPower(int power) {
        this.mPower = power;
        if (mPower < 0 || mPower > 100) {
            mPower = 100;
        }
        invalidate();//刷新VIEW
    }

    /**
     * 设置电池颜色
     *
     * @param color
     */
    public void setColor(int color) {
        this.mColor = color;
        invalidate();
    }

    private void changeBatterNum(int num) {
        mBatteryNum = num;
        mPower = num;
        invalidate();
    }

    /**
     * 获取电池电量
     *
     * @return
     */
    public int getPower() {
        return mPower;
    }

    private class BatteryLevelReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //获取当前电量
            int rawLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            //获取总电量
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int level = -1;
            if (rawLevel >= 0 && scale > 0) {
                level = (rawLevel * 100) / scale;
            }
            if (level > 0 && level <= 100) {
                changeBatterNum(level);
            }
        }
    }
}