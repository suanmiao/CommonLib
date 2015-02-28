package me.suanmiao.ptrListview.header;

import android.animation.ValueAnimator;
import android.content.Context;

/**
 * Created by suanmiao on 15/2/27.
 */
public abstract class AbstractNormalHeader implements IPTRHeader {

  private Context mContext;

  public int paddingTop = 0;
  public static final long RESET_TOTAL_DURATION = 300;

  public AbstractNormalHeader(Context context) {
    mContext = context;
  }

  protected void setPadding(int l, int t, int r, int b) {
    getHeaderLayout().setPadding(l, t, r, b);
  }

  @Override
  public void onPullCancel() {
    animatePaddingTop(-getHeaderTotalHeight());
  }

  @Override
  public void onRefreshStart() {
    animatePaddingTop(-(getHeaderTotalHeight() - getHeaderRefreshingHeight()));
  }

  @Override
  public void onInit() {
    setPadding(0, -getHeaderTotalHeight(), 0, 0);
  }

  @Override
  public int getHeaderCurrentPaddingTop() {
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

  public Context getContext() {
    return mContext;
  }
}
