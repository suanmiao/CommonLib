package me.suanmiao.ptrListview.footer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import me.suanmiao.ptrListview.R;

/**
 * Created by suanmiao on 15/2/27.
 */
public class DefaultFooter implements IPTRFooter {

  private LinearLayout mFooterLayout;
  private ImageView mFooterCircleImageView;
  private int mFooterHeight;
  private Context mContext;
  private RotateAnimation circleAnimation;

  public DefaultFooter(Context context) {
    mContext = context;
    mFooterHeight = context.getResources().getDimensionPixelSize(R.dimen.footer_height);
    initLayout();
  }

  private void initLayout() {
    LayoutInflater inflater =
        (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    mFooterLayout = (LinearLayout) inflater.inflate(
        R.layout.ptr_loading_layout, null);
    mFooterCircleImageView = (ImageView) mFooterLayout
        .findViewById(R.id.ptr_footer_circle);
    mFooterLayout.setPadding(0, 0, 0, -getFooterHeight());

    circleAnimation = new RotateAnimation(0, 360,
        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
    circleAnimation.setInterpolator(new LinearInterpolator());
    circleAnimation.setDuration(500);
    circleAnimation.setRepeatCount(-1);

  }


  @Override
  public void onLoadStart() {
    mFooterLayout.setPadding(0, 0, 0, 0);
    mFooterCircleImageView.clearAnimation();
    mFooterCircleImageView.startAnimation(circleAnimation);
  }

  @Override
  public void onLoadComplete() {
    mFooterLayout.setPadding(0, 0, 0, -getFooterHeight());
    mFooterCircleImageView.clearAnimation();
  }

  @Override
  public View getFooterLayout() {
    return mFooterLayout;
  }

  @Override
  public int getFooterHeight() {
    return mFooterHeight;
  }
}
