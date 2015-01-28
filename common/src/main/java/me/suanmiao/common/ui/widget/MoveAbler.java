package me.suanmiao.common.ui.widget;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by suanmiao on 14/12/13.
 */

public class MoveAbler implements View.OnTouchListener {
    private float startX, startY;
    private static final long RESET_ANIMATION_DURATION = 300;
    private static final float MOVE_SLOP = 200;

    public MoveAbler(){
    }

    @Override
    public boolean onTouch(View targetView, MotionEvent event) {
        float currentX = event.getX() + targetView.getTranslationX();
        float currentY = event.getY() + targetView.getTranslationY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = currentX;
                startY = currentY;

                break;
            case MotionEvent.ACTION_MOVE:
                targetView.setTranslationX(getEasedValue(currentX - startX));
                targetView.setTranslationY(getEasedValue(currentY - startY));

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                targetView.animate().translationX(0).translationY(0).setDuration(RESET_ANIMATION_DURATION).start();
                break;
        }
        return true;
    }


    private float getEasedValue(float value) {
        float fraction = (float) (Math.atan(value / MOVE_SLOP) / Math.PI * 2);
        return MOVE_SLOP * fraction;
    }

}

