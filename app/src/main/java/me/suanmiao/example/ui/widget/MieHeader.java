package me.suanmiao.example.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import me.suanmiao.example.R;
import me.suanmiao.ptrlistview.IPullToRefresh;
import me.suanmiao.ptrlistview.header.AbstractNormalHeader;

/**
 * Created by suanmiao on 15/2/27.
 */
public class MieHeader extends AbstractNormalHeader {
  private View headerLayout;
  private View headIcon;
  private RotateAnimation circleAnimation;

  public MieHeader(Context context) {
    super(context);
    initLayout();
  }

  private void initLayout() {
    LayoutInflater inflater =
        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    headerLayout = inflater.inflate(
        R.layout.mie_header_layout, null);
    headIcon = headerLayout.findViewById(R.id.mie_head);

    circleAnimation = new RotateAnimation(0, 360,
        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
    circleAnimation.setInterpolator(new LinearInterpolator());
    circleAnimation.setDuration(500);
    circleAnimation.setRepeatCount(-1);
  }

  @Override
  public int getHeaderTotalHeight() {
    return getContext().getResources().getDimensionPixelSize(R.dimen.header_total_height);
  }

  @Override
  public int getHeaderRefreshingHeight() {
    return getContext().getResources().getDimensionPixelSize(R.dimen.header_refresh_height);
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
        headIcon.animate().rotation(180).setDuration(300)
            .start();
        break;
      case PULL_TO_REFRESH:
        paddingTop = (int) (-getHeaderTotalHeight() + getHeaderRefreshingHeight() * progress);
        headIcon.clearAnimation();
        headIcon.animate().rotation(0).setDuration(300).start();
        break;
      case REFRESHING:
        paddingTop = -(getHeaderTotalHeight() - getHeaderRefreshingHeight());
        headIcon.startAnimation(circleAnimation);
        break;
      case DONE:
        paddingTop = -getHeaderTotalHeight();
        headIcon.clearAnimation();
        break;
    }
    setPadding(0, paddingTop, 0, 0);
  }

  @Override
  protected void setPadding(int l, int t, int r, int b) {
    headerLayout.setPadding(l, t, r, b);
  }

}
