package me.suanmiao.ptrListview.header;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import me.suanmiao.ptrListview.IPullToRefresh;
import me.suanmiao.ptrListview.R;

/**
 * Created by suanmiao on 15/2/27.
 */
public class DefaultHeader implements IPTRHeader {

  private Context mContext;

  public int paddingTop = 0;
  public static final long RESET_TOTAL_DURATION = 300;
  private View headerLayout;
  private TextView ptrHeaderTipTextView;
  private ImageView ptrHeaderArrowImageView;
  private ImageView ptrHeaderCircleImageView;
  private RotateAnimation circleAnimation;
  private static final long ARROW_ANIMATION_DURATION = 300;

  public DefaultHeader(Context context) {
    mContext = context;
    initLayout();
  }

  private void initLayout() {
    LayoutInflater inflater =
        (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    headerLayout =  inflater.inflate(
        R.layout.ptr_header_layout, null);
    ptrHeaderTipTextView = (TextView) headerLayout
        .findViewById(R.id.ptr_header_text_tip);

    ptrHeaderArrowImageView = (ImageView) headerLayout
        .findViewById(R.id.ptr_header_arrow);
    ptrHeaderCircleImageView = (ImageView) headerLayout
        .findViewById(R.id.ptr_header_circle);

    circleAnimation = new RotateAnimation(0, 360,
        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
    circleAnimation.setInterpolator(new LinearInterpolator());
    circleAnimation.setDuration(500);
    circleAnimation.setRepeatCount(-1);
  }

  @Override
  public View getHeaderLayout() {
    return headerLayout;
  }

  @Override
  public void onPull(float progress, IPullToRefresh.REFRESH_STATE refreshState) {
    switch (refreshState) {
      case RELEASE_TO_REFRESH:
        paddingTop = (int) (-getHeaderTotalHeight() + getHeaderRefreshingHeight() * progress);
        ptrHeaderArrowImageView.setVisibility(View.VISIBLE);
        ptrHeaderCircleImageView.clearAnimation();
        ptrHeaderCircleImageView.setVisibility(View.GONE);
        ptrHeaderTipTextView.setVisibility(View.VISIBLE);
        ptrHeaderArrowImageView.animate().rotation(180).setDuration(ARROW_ANIMATION_DURATION)
            .start();
        ptrHeaderTipTextView
            .setText(mContext.getResources().getString(R.string.release_to_refresh));
        break;
      case PULL_TO_REFRESH:
        paddingTop = (int) (-getHeaderTotalHeight() + getHeaderRefreshingHeight() * progress);
        ptrHeaderCircleImageView.clearAnimation();
        ptrHeaderCircleImageView.setVisibility(View.GONE);
        ptrHeaderTipTextView.setVisibility(View.VISIBLE);
        ptrHeaderArrowImageView.clearAnimation();
        ptrHeaderArrowImageView.setVisibility(View.VISIBLE);
        // if the state comes from "release to refresh",
        // there should be a back animation for arrow
        ptrHeaderArrowImageView.animate().rotation(0).setDuration(ARROW_ANIMATION_DURATION).start();
        ptrHeaderTipTextView.setText(mContext.getResources().getString(R.string.pull_to_refresh));
        break;
      case REFRESHING:
        paddingTop = -(getHeaderTotalHeight() - getHeaderRefreshingHeight());
        ptrHeaderCircleImageView.setVisibility(View.VISIBLE);
        ptrHeaderCircleImageView.clearAnimation();
        ptrHeaderCircleImageView.startAnimation(circleAnimation);
        ptrHeaderArrowImageView.clearAnimation();
        ptrHeaderArrowImageView.setVisibility(View.GONE);
        ptrHeaderTipTextView.setText(mContext.getResources().getString(R.string.refreshing));
        break;
      case DONE:
        paddingTop = -getHeaderTotalHeight();
        ptrHeaderCircleImageView.setVisibility(View.GONE);
        ptrHeaderArrowImageView.clearAnimation();
        ptrHeaderArrowImageView.setImageResource(R.drawable.ptr_arrow);
        ptrHeaderTipTextView.setText(mContext.getResources().getString(R.string.pull_to_refresh));
        break;
    }
    setPadding(0, paddingTop, 0, 0);
  }

  private void setPadding(int l, int t, int r, int b) {
    headerLayout.setPadding(l, t, r, b);
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
  public int getHeaderTotalHeight() {
    return mContext.getResources().getDimensionPixelSize(R.dimen.default_header_height);
  }

  @Override
  public int getHeaderRefreshingHeight() {
    return mContext.getResources().getDimensionPixelSize(R.dimen.default_header_height);
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
}
