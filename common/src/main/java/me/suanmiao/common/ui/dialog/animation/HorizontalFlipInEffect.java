package me.suanmiao.common.ui.dialog.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by suanmiao on 14/12/31.
 */
public class HorizontalFlipInEffect extends BaseEffect {

    @Override
    public AnimatorSet setupAnimation(final View target) {
//        target.setPivotX(target.getWidth() / 2);
//        target.setPivotY(target.getHeight() / 2);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(target, "rotationY", -90, 0).setDuration(400)
        );
        return animatorSet;
    }
}
