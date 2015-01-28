package me.suanmiao.common.ui.dialog.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by suanmiao on 14/12/31.
 */
public class FadeInEffect extends BaseEffect {

    @Override
    public AnimatorSet setupAnimation(View target) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(ObjectAnimator.ofFloat(target, "scale", 0.8f, 1f).setDuration(400)).with(ObjectAnimator.ofFloat(target, "alpha", 0.8f, 1f).setDuration(400));

        return animatorSet;
    }
}
