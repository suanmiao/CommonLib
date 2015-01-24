package com.suan.ptrListview.header;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.suan.ptrListview.IPullToRefresh;

/**
 * Created by suanmiao on 14-11-3.
 * notes:
 * the super class must be LinearLayout here
 * if I use FrameLayout,the setPadding(0,negative value,0,0),it does not work
 */
public abstract class AbstractHeaderLayout extends LinearLayout implements IPTRHeader {

  public int paddingTop = 0;
  public static final long RESET_TOTAL_DURATION = 300;

  public AbstractHeaderLayout(Context context) {
    super(context);
    init(context);
  }

  public AbstractHeaderLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public AbstractHeaderLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context);
  }

  public abstract void init(Context context);

  @Override
  public abstract void onPull(float progress, IPullToRefresh.REFRESH_STATE refreshState);

  @Override
  public int getHeaderPaddingTop() {
    return paddingTop;
  }

  public void animatePaddingTop(int to) {
    ValueAnimator resetAnimator =
        ValueAnimator.ofInt(paddingTop, to).setDuration(RESET_TOTAL_DURATION);
    resetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        paddingTop = (Integer) animation.getAnimatedValue();
        setPadding(0, paddingTop, 0, 0);
      }
    });
    resetAnimator.start();
  }
}
