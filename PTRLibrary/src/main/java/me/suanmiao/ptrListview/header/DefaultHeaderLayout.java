package me.suanmiao.ptrListview.header;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.suanmiao.ptrListview.IPullToRefresh;
import me.suanmiao.ptrListview.R;

/**
 * Created by suanmiao on 14-11-3.
 */
public class DefaultHeaderLayout extends AbstractHeaderLayout {

  private LinearLayout headerView;
  private TextView ptrHeaderTipTextView;
  private ImageView ptrHeaderArrowImageView;
  private ImageView ptrHeaderCircleImageView;
  private RotateAnimation circleAnimation;
  private static final long ARROW_ANIMATION_DURATION = 300;

  public DefaultHeaderLayout(Context context) {
    super(context);
  }

  public DefaultHeaderLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public DefaultHeaderLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  public void init(Context context) {
    LayoutInflater inflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    headerView = (LinearLayout) inflater.inflate(
        R.layout.ptr_header_layout, null);
    ptrHeaderTipTextView = (TextView) headerView
        .findViewById(R.id.ptr_header_text_tip);

    ptrHeaderArrowImageView = (ImageView) headerView
        .findViewById(R.id.ptr_header_arrow);
    ptrHeaderCircleImageView = (ImageView) headerView
        .findViewById(R.id.ptr_header_circle);

    circleAnimation = new RotateAnimation(0, 360,
        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
    circleAnimation.setInterpolator(new LinearInterpolator());
    circleAnimation.setDuration(500);
    circleAnimation.setRepeatCount(-1);

    this.addView(headerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT));
  }

  @Override
  public void onPull(float progress, IPullToRefresh.REFRESH_STATE refreshState) {
    switch (refreshState) {
      case RELEASE_TO_REFRESH:
        paddingTop = (int) (-getHeaderTotalHeight() + getHeaderRefreshingHeight() * progress);
        ptrHeaderArrowImageView.setVisibility(VISIBLE);
        ptrHeaderCircleImageView.clearAnimation();
        ptrHeaderCircleImageView.setVisibility(GONE);
        ptrHeaderTipTextView.setVisibility(VISIBLE);
        ptrHeaderArrowImageView.animate().rotation(180).setDuration(ARROW_ANIMATION_DURATION)
            .start();
        ptrHeaderTipTextView.setText(getResources().getString(R.string.release_to_refresh));
        break;
      case PULL_TO_REFRESH:
        paddingTop = (int) (-getHeaderTotalHeight() + getHeaderRefreshingHeight() * progress);
        ptrHeaderCircleImageView.clearAnimation();
        ptrHeaderCircleImageView.setVisibility(GONE);
        ptrHeaderTipTextView.setVisibility(VISIBLE);
        ptrHeaderArrowImageView.clearAnimation();
        ptrHeaderArrowImageView.setVisibility(VISIBLE);
        // if the state comes from "release to refresh",
        // there should be a back animation for arrow
        ptrHeaderArrowImageView.animate().rotation(0).setDuration(ARROW_ANIMATION_DURATION).start();
        ptrHeaderTipTextView.setText(getResources().getString(R.string.pull_to_refresh));
        break;
      case REFRESHING:
        paddingTop = -(getHeaderTotalHeight() - getHeaderRefreshingHeight());
        ptrHeaderCircleImageView.setVisibility(VISIBLE);
        ptrHeaderCircleImageView.clearAnimation();
        ptrHeaderCircleImageView.startAnimation(circleAnimation);
        ptrHeaderArrowImageView.clearAnimation();
        ptrHeaderArrowImageView.setVisibility(GONE);
        ptrHeaderTipTextView.setText(getResources().getString(R.string.refreshing));
        break;
      case DONE:
        paddingTop = -getHeaderTotalHeight();
        ptrHeaderCircleImageView.setVisibility(GONE);
        ptrHeaderArrowImageView.clearAnimation();
        ptrHeaderArrowImageView.setImageResource(R.drawable.ptr_arrow);
        ptrHeaderTipTextView.setText(getResources().getString(R.string.pull_to_refresh));
        break;
    }
    setPadding(0, paddingTop, 0, 0);
//    Log.e("SUAN", "padding top " + paddingTop);
  }

  @Override
  public int getHeaderTotalHeight() {
    return getResources().getDimensionPixelSize(R.dimen.default_header_height);
  }

  @Override
  public int getHeaderRefreshingHeight() {
    return getResources().getDimensionPixelSize(R.dimen.default_header_height);
  }

  @Override
  public int getHeaderPaddingTop() {
    return paddingTop;
  }

}
