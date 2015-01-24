package com.suan.example.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by suanmiao on 14/12/18.
 */
public class SScrollView extends ScrollView {
    private ScrollGestureListener gestureListener;
    private float lastY;
    private static final float SCROLL_SLOP = 1f;
    private static final float VELOCITY_SLOP = 60f;
    private VelocityTracker mVelocityTracker;
    private static final int MAX_VELOCITY = 1000;

    public SScrollView(Context context) {
        super(context);
    }

    public SScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        initVelocityTrackerIfNotExists();
        acquireVelocityTracker(ev);
        final VelocityTracker verTracker = mVelocityTracker;
        float currentX = ev.getX();
        float currentY = ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                if (gestureListener != null) {
                    verTracker.computeCurrentVelocity(1000, MAX_VELOCITY);
                    float velocityY = verTracker.getYVelocity();
                    if (currentY - lastY > SCROLL_SLOP&&velocityY>VELOCITY_SLOP) {
                        gestureListener.onScrollDown();
                    } else if (currentY - lastY < -SCROLL_SLOP&&velocityY<-VELOCITY_SLOP) {
                        gestureListener.onScrollUp();
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();

                break;
        }
        lastY = currentY;
        return super.onTouchEvent(ev);
    }

    /**
     * @param event 向VelocityTracker添加MotionEvent
     * @see android.view.VelocityTracker#obtain()
     * @see android.view.VelocityTracker#addMovement(android.view.MotionEvent)
     */
    private void acquireVelocityTracker(final MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    public void setGestureListener(ScrollGestureListener listener) {
        this.gestureListener = listener;
    }

    public interface ScrollGestureListener {

        public void onScrollDown();

        public void onScrollUp();

    }

    @Override
    public void requestChildFocus(View child, View focused) {
        //prevent webView from scrolling when viewPager scroll
        if(focused instanceof ArticleWebView){
            return;
        }
        super.requestChildFocus(child, focused);
    }

}
