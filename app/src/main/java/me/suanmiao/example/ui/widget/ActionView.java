package me.suanmiao.example.ui.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import me.suanmiao.example.R;

/**
 * Created by suanmiao on 14/12/23.
 */
public class ActionView extends RelativeLayout {
    private ImageView iconView;
    private static final long TWIST_ANIMATION_DURATION = 300l;
    private static final long LOADING_ANIMATION_DURATION = 500l;
    private static final long RESET_ANIMATION_DURATION = 500l;
    private ObjectAnimator twistAnimator, loadingAnimation;
    private int iconSize;
    private int bgSize;
    private static final float DISMISS_ALPHA = 0.3f;
    private boolean loading = false;

    private OnClickListener onClickListener;
    private int MOVE_SLOP;
    private float startX, startY;
    private float lastX, lastY;
    private long downTime;
    private static final long CLICK_TIME_THRESHOLD = 500l;
    private boolean actionStart = false;

    private OnActionListener mOnActionListener;

    public ActionView(Context context) {
        super(context);
        init();
    }

    public ActionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ActionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        iconView = new ImageView(getContext());
        iconView.setImageResource(R.drawable.ic_rotate_center);
        iconView.setScaleType(ImageView.ScaleType.FIT_XY);
        this.setBackgroundResource(R.drawable.bg_action_view);

        removeAllViews();
        iconSize = getResources().getDimensionPixelSize(R.dimen.default_action_icon_size);
        bgSize = getResources().getDimensionPixelSize(R.dimen.default_action_bg_size);
        MOVE_SLOP = getResources().getDimensionPixelSize(R.dimen.default_move_slop);
        LayoutParams layoutParams = new LayoutParams(iconSize, iconSize);
        iconView.setLayoutParams(layoutParams);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        addView(iconView, layoutParams);
    }

    public void translateIndex() {
        if (twistAnimator != null) {
            twistAnimator.cancel();
        }
        this.setPivotY(getHeight() / 2);
        this.setPivotX(getWidth() / 2);
        twistAnimator = ObjectAnimator.ofFloat(iconView, "rotationY", 0, 360).setDuration(TWIST_ANIMATION_DURATION);
        twistAnimator.setInterpolator(new LinearInterpolator());
        twistAnimator.start();
    }

    public void startLoading() {
        if (loading) {
            return;
        }
        loading = true;
        if (loadingAnimation != null) {
            loadingAnimation.cancel();
        }
//        iconView.setPivotX(radius);
//        iconView.setPivotY(radius);
        iconView.setImageResource(R.drawable.ic_rotate_center_loading);
        loadingAnimation = ObjectAnimator.ofFloat(iconView, "rotation", 0f, 360f).setDuration(LOADING_ANIMATION_DURATION);
        loadingAnimation.setInterpolator(new LinearInterpolator());
        loadingAnimation.addListener(loadingAnimatorListener);
        loadingAnimation.start();
    }

    private Animator.AnimatorListener loadingAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (loading) {
                iconView.setImageResource(R.drawable.ic_rotate_center_loading);
                loadingAnimation = ObjectAnimator.ofFloat(iconView, "rotation", 0f, 360f).setDuration(LOADING_ANIMATION_DURATION);
                loadingAnimation.setInterpolator(new LinearInterpolator());
                loadingAnimation.addListener(loadingAnimatorListener);
                loadingAnimation.start();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    public void stopLoading() {
        if (!loading) {
            return;
        }
        loading = false;
        if (loadingAnimation != null) {
            iconView.setRotation(0f);
            iconView.setImageResource(R.drawable.ic_rotate_center);
            loadingAnimation.removeAllListeners();
            loadingAnimation.cancel();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getRawX();
        float currentY = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downTime = System.currentTimeMillis();
                startX = currentX;
                startY = currentY;
                if (mOnActionListener != null) {
                    mOnActionListener.onPress();
                }
                break;
            case MotionEvent.ACTION_MOVE: {
                float moveX = currentX - startX;
                float moveY = currentY - startY;

                if (Math.abs(moveX) > MOVE_SLOP || Math.abs(moveY) > MOVE_SLOP) {
                    float deltaX = currentX - lastX;
                    float deltaY = currentY - lastY;
                    setTranslationX(getTranslationX() + deltaX);
                    setTranslationY(getTranslationY() + deltaY);
                    if (!actionStart && mOnActionListener != null) {
                        mOnActionListener.onActionStart();
                    }
                    actionStart = true;
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                float moveX = currentX - startX;
                float moveY = currentY - startY;

                if (Math.abs(moveX) < MOVE_SLOP && Math.abs(moveY) < MOVE_SLOP && System.currentTimeMillis() - downTime < CLICK_TIME_THRESHOLD && onClickListener != null) {
                    onClickListener.onClick(this);
                }
                resetPosition();
                if (mOnActionListener != null) {
                    mOnActionListener.onRelease(event.getRawX(), event.getRawY());
                }
                actionStart = false;
            }
            case MotionEvent.ACTION_CANCEL: {
                resetPosition();
                if (mOnActionListener != null) {
                    mOnActionListener.onCancel();
                }
                actionStart = false;
                break;
            }
        }
        lastX = currentX;
        lastY = currentY;
        return true;
    }

    private void resetPosition() {
        this.animate().translationY(0).translationX(0).setDuration(RESET_ANIMATION_DURATION).start();
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnActionListener(OnActionListener mOnActionListener) {
        this.mOnActionListener = mOnActionListener;
    }

    public interface OnActionListener {

        public void onPress();

        public void onCancel();

        public void onActionStart();

        public void onRelease(float rawX, float rawY);
    }
}
