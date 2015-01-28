package me.suanmiao.example.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by suanmiao on 14/12/12.
 */
public class MoveAbleView extends FrameLayout {

    private static String TAG = "SUANTOUCH";
    private static final long RESET_ANIMATION_DURATION = 300;
    private static final float MOVE_SLOP = 200;
    private boolean moveable;

    public MoveAbleView(Context context) {
        super(context);
    }

    public MoveAbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveAbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float startX, startY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (moveable) {
            float currentX = event.getX() + getTranslationX();
            float currentY = event.getY() + getTranslationY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = currentX;
                    startY = currentY;

                    break;
                case MotionEvent.ACTION_MOVE:
                    setTranslationX(getEasedValue(currentX - startX));
                    setTranslationY(getEasedValue(currentY - startY));

                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    this.animate().translationX(0).translationY(0).setDuration(RESET_ANIMATION_DURATION).start();
                    break;
            }
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    private float getEasedValue(float value) {
        float fraction = (float) (Math.atan(value / MOVE_SLOP) / Math.PI * 2);
        return MOVE_SLOP * fraction;
    }

    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }
}
