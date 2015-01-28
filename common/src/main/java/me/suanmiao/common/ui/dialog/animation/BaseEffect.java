package me.suanmiao.common.ui.dialog.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.View;

/**
 * Created by suanmiao on 14/12/16.
 */
public abstract class BaseEffect {
    /**
     * two steps:
     * 1.setup animation, init the type and object of the animation
     * 2.start animation
     *
     */
    private AnimatorSet animatorSet;
    private View targetView;
    private Animator.AnimatorListener listener;

    public BaseEffect() {
        this.animatorSet = new AnimatorSet();
    }

    public void setTargetView(View targetView) {
        this.targetView = targetView;
        initTargetView();
    }

    /**
     * setup initial dimens for target view
     */
    public void initTargetView() {
        float width = 0f, height = 0f;
        if (targetView.getLayoutParams() != null) {
            width = targetView.getLayoutParams().width > 0 ? targetView.getLayoutParams().width : 0;
            height = targetView.getLayoutParams().height > 0 ? targetView.getLayoutParams().height : 0;
        }
        if (width != 0 && height != 0) {
            this.targetView.setPivotX(width / 2f);
            this.targetView.setPivotY(height / 2f);
        }
    }

    public abstract AnimatorSet setupAnimation(View target);

    public void addEffectListener(Animator.AnimatorListener listener) {
        this.listener = listener;
        if (animatorSet != null) {
            animatorSet.addListener(listener);
        }
    }

    public void start() {
        animatorSet = setupAnimation(targetView);
        animatorSet.setTarget(targetView);
        if (listener != null) {
            animatorSet.addListener(listener);
        }
        animatorSet.start();
    }

    public AnimatorSet getAnimatorSet() {
        return animatorSet;
    }

}
