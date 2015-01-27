package me.suan.example.ui.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

/**
 * Created by suanmiao on 14/12/22.
 */
public class TipView extends RelativeLayout {

    private View tipContent;
    private static final long TIP_SHOW_DURATION = 300;
    public static final long TIP_STAY_DURATION_NORMAL = 2000L;
    public static final long TIP_STAY_DURATION_LONG = 3000L;
    private static final long TIP_DISMISS_DURATION = 200;

    private ObjectAnimator showAnimator, dismissAnimator;
    private Handler mHandler;
    private ShowCallback mShowCallback;
    private DismissCallback mDismissCallback;

    private static final String TRASLATIONY = "translationY";

    public TipView(Context context) {
        super(context);
        init();
    }

    public TipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setTipContent(View content) {
        this.tipContent = content;
        this.removeAllViews();
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        this.addView(content, layoutParams);
        content.setTranslationY(-getResources().getDimensionPixelSize(me.suan.example.R.dimen.tip_view_default_height));
        showAnimator = ObjectAnimator.ofFloat(content, TRANSLATION_Y, 0);
        showAnimator.setDuration(TIP_SHOW_DURATION);
        showAnimator.setInterpolator(new DecelerateInterpolator());
    }

    public View getTipContent() {
        return tipContent;
    }

    /**
     * stupid, all these could be done through AnimatorSet
     * @param delay
     */
    public void showTip(long delay) {
        if (this.tipContent != null) {
            this.tipContent.setVisibility(View.VISIBLE);
            removeAnimationCallback();
            mShowCallback = new ShowCallback();
            mHandler.postDelayed(mShowCallback, delay);
            mDismissCallback = new DismissCallback();
            mHandler.postDelayed(mDismissCallback, delay + TIP_STAY_DURATION_NORMAL + TIP_SHOW_DURATION);
        }
    }

    public void showTip(long delay, long tipStayDuration) {
        if (this.tipContent != null) {
            this.tipContent.setVisibility(View.VISIBLE);
            removeAnimationCallback();
            mShowCallback = new ShowCallback();
            mHandler.postDelayed(mShowCallback, delay);
            mDismissCallback = new DismissCallback();
            mHandler.postDelayed(mDismissCallback, delay + tipStayDuration + TIP_SHOW_DURATION);
        }
    }

    public void removeAnimationCallback() {
        if (mShowCallback != null) {
            mHandler.removeCallbacks(mShowCallback);
        }
        if (mDismissCallback != null) {
            mHandler.removeCallbacks(mDismissCallback);
        }
    }

    public class ShowCallback implements Runnable {

        @Override
        public void run() {
            showAnimator = ObjectAnimator.ofFloat(tipContent, TRANSLATION_Y, 0);
            showAnimator.setDuration(TIP_SHOW_DURATION);
            showAnimator.setInterpolator(new DecelerateInterpolator());
            showAnimator.start();
        }
    }

    public class DismissCallback implements Runnable {

        @Override
        public void run() {
            dismissAnimator = ObjectAnimator.ofFloat(tipContent,TRANSLATION_Y,-getResources().getDimensionPixelSize(me.suan.example.R.dimen.tip_view_default_height));
            dismissAnimator.setDuration(TIP_DISMISS_DURATION);
            dismissAnimator.setInterpolator(new AccelerateInterpolator());
            dismissAnimator.start();
            dismissAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    tipContent.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }

    private void init() {
        mHandler = new Handler();
    }
}
