package com.iec.dwx.timer.Views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

import com.iec.dwx.timer.R;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * 自定义时钟控件
 * Created by Flying SnowBean on 2015/10/5.
 */
public class ClockView extends View {
    private final String TAG = ClockView.class.getSimpleName();

    private static final int STATE_DIAL_ANIM = 0;
    private static final int STATE_OUTER_ANIM = 1;

    private int mState = STATE_DIAL_ANIM;

    private Calendar mCalendar;

    private int mMinute;
    private int mHour;
    private int mSecond;

    private Drawable mDial;
    private Drawable mInnerCircle;
    private Drawable mPointHour;
    private Drawable mPointMinute;
    private Drawable mPointSecond;

    private int mWidth;
    private int mHeight;


    private boolean mAttached;

    private float mScale = 2;

    private ClockHandler mHandler;
    //监听时间变化的广播接收者，时间改变，改变时钟指针
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "BroadReceiver->onReceive");

            if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                String tz = intent.getStringExtra("time-zone");
                mCalendar.setTimeZone(TimeZone.getTimeZone(tz));
                mCalendar = Calendar.getInstance();
            }

            mCalendar = Calendar.getInstance();
            mHour = mCalendar.get(Calendar.HOUR);
            mMinute = mCalendar.get(Calendar.MINUTE);
            mSecond = mCalendar.get(Calendar.SECOND);
            invalidate();
        }
    };

    public ClockView(Context context) {
        super(context);
        init(context);
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        post(() -> startDialAnim());
    }

    private void startDialAnim() {
        float scale = (float) mWidth / mDial.getIntrinsicWidth();
        //Log.d(TAG,"scale->"+scale+"mHeight->"+mHeight+",mDial->Height->"+mDial.getIntrinsicHeight());
        Animator anim = ObjectAnimator.ofFloat(this, "scale", 0, scale).setDuration(1000);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mState = STATE_OUTER_ANIM;
                startPointAnim();
            }
        });
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }

    private void startPointAnim() {
        Animator minuteAnim = ObjectAnimator.ofInt(this, "minute", 0, mCalendar.get(Calendar.MINUTE)).setDuration(800);
        Animator hourAnim = ObjectAnimator.ofInt(this, "hour", 0, mCalendar.get(Calendar.HOUR)).setDuration(800);
        Animator secondAnim = ObjectAnimator.ofInt(this, "second", 0, mCalendar.get(Calendar.SECOND)).setDuration(800);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(minuteAnim, hourAnim, secondAnim);
        set.setInterpolator(new AccelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                refreshSecond();
            }
        });
        set.start();
    }


    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        final Resources res = context.getResources();
        mDial = res.getDrawable(R.drawable.dial);
        mInnerCircle = res.getDrawable(R.drawable.inner_circle);
        mPointHour = res.getDrawable(R.drawable.point_hour);
        mPointMinute = res.getDrawable(R.drawable.point_minute);
        mPointSecond = res.getDrawable(R.drawable.point_second);

        mCalendar = Calendar.getInstance();

        mHandler = new ClockHandler(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!mAttached) {
            mAttached = true;
            IntentFilter filter = new IntentFilter();

            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);

            getContext().registerReceiver(mIntentReceiver, filter, null, mHandler);
            Log.d(TAG, "onAttachToWindow");
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            getContext().unregisterReceiver(mIntentReceiver);
            mAttached = false;
            Log.d(TAG, "onDetachToWindow");
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHeight = getHeight();

        //mScale = mWidth / mDial.getIntrinsicWidth();
        //Log.d(TAG,"Scale->"+mScale);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mState == STATE_DIAL_ANIM) {
            drawDial(canvas);
        } else if (mState == STATE_OUTER_ANIM) {
            drawDial(canvas);
            drawPointHour(canvas);
            drawPointMinute(canvas);
            drawPointSecond(canvas);
            drawInnerCircle(canvas);
        }
    }


    private void drawPointSecond(Canvas canvas) {
        final Drawable second = mPointSecond;
        int w = second.getIntrinsicWidth();
        int h = second.getIntrinsicHeight();
        second.setBounds((mWidth - w) / 2, (mHeight - h) / 2, (mWidth + w) / 2, (mHeight + h) / 2);

        canvas.save();
//        Log.d(TAG, "mSecond->" + mSecond);
        canvas.rotate(mSecond / 60.0f * 360.0f, mWidth / 2, mHeight / 2);
        second.draw(canvas);
        canvas.restore();
    }


    private void drawPointMinute(Canvas canvas) {
        final Drawable minute = mPointMinute;
        int w = minute.getIntrinsicWidth();
        int h = minute.getIntrinsicHeight();
//        Log.d(TAG, "minute_point:" + "width->" + w + ",height->" + h);
        minute.setBounds((mWidth - w) / 2, (mHeight - h) / 2, (mWidth + w) / 2, (mHeight + h) / 2);

        canvas.save();
        canvas.rotate(mMinute / 60.0f * 360.0f, mWidth / 2, mHeight / 2);
        minute.draw(canvas);
        canvas.restore();
    }

    private void drawPointHour(Canvas canvas) {
        final Drawable hour = mPointHour;
        int w = hour.getIntrinsicWidth();
        int h = hour.getIntrinsicHeight();
//        Log.d(TAG, "hour_point:" + "width->" + w + ",height->" + h);
        hour.setBounds((mWidth - w) / 2, (mHeight - h) / 2, (mWidth + w) / 2, (mHeight + h) / 2);

        canvas.save();
        canvas.rotate(mHour / 12.0f * 360.0f, mWidth / 2, mHeight / 2);
        hour.draw(canvas);
        canvas.restore();
    }

    private void drawInnerCircle(Canvas canvas) {
        final Drawable inner = mInnerCircle;
        int w = inner.getIntrinsicWidth();
        int h = inner.getIntrinsicHeight();
//        Log.d(TAG, "inner:" + "width->" + w + ",height->" + h);
        inner.setBounds((mWidth - w) / 2, (mHeight - h) / 2, (mWidth + w) / 2, (mHeight + h) / 2);
        inner.draw(canvas);
    }

    private void drawDial(Canvas canvas) {
        final Drawable dial = mDial;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();
//        Log.d(TAG, "dial:" + "width->" + w + ",height->" + h + ",scale->" + mScale);
        dial.setBounds((mWidth - w) / 2, (mHeight - h) / 2, (mWidth + w) / 2, (mHeight + h) / 2);
        canvas.save();
        canvas.scale(mScale, mScale, mWidth * 0.5f, mHeight * 0.5f);
        dial.draw(canvas);
    }

    public void setScale(float scale) {
        mScale = scale;
        invalidate();
    }

    public void setHour(int hour) {
        mHour = hour;
        invalidate();
    }

    public void setMinute(int minute) {
        mMinute = minute;
        invalidate();
    }

    public void setSecond(int second) {
        mSecond = second;
        invalidate();
    }

    private void refreshSecond() {
        mHandler.sendEmptyMessageDelayed(0, 1000);
        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mSecond = mCalendar.get(Calendar.SECOND);
        invalidate();
    }

    private static class ClockHandler extends Handler {
        private WeakReference<ClockView> mClockViewWeakReference;

        public ClockHandler(ClockView clockView) {
            mClockViewWeakReference = new WeakReference<>(clockView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mClockViewWeakReference.get() != null)
                mClockViewWeakReference.get().refreshSecond();
        }
    }
}
