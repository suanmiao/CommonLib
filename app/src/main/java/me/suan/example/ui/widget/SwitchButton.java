package me.suan.example.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by suanmiao on 14/12/27.
 */
public class SwitchButton extends View {

    private int baselineHeight;
    private int indicatorWidth;
    private int indicatorHeight;
    private int baselinePaddingHorizontal;

    private Drawable normalIndicatorDrawable;
    private Drawable normalPressedIndicatorDrawable;
    private Drawable checkedIndicatorDrawable;
    private Drawable checkedPressedIndicatorDrawable;
    private Drawable baselineDrawable;

    private float currentX;
    private boolean pressed = false;
    private boolean switchOn = false;

    private static final int MOVE_SLOP = 6;
    private float startX, startY;
    private float lastX, lastY;
    private long downTime;
    private static final long CLICK_TIME_THRESHOLD = 500l;

    private OnClickListener onClickListener;

    private OnSwitchChangeListener onSwitchChangeListener;

    public SwitchButton(Context context) {
        super(context);
        init();
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        indicatorWidth = getResources().getDimensionPixelSize(me.suan.example.R.dimen.default_switch_indicator_width);
        indicatorHeight = getResources().getDimensionPixelSize(me.suan.example.R.dimen.default_switch_indicator_height);
        baselineHeight = getResources().getDimensionPixelSize(me.suan.example.R.dimen.default_switch_baseline_height);
        baselinePaddingHorizontal = getResources().getDimensionPixelSize(me.suan.example.R.dimen.default_switch_baseline_padding);

        normalIndicatorDrawable = getResources().getDrawable(me.suan.example.R.drawable.ic_button_indicator_normal);
        normalPressedIndicatorDrawable = getResources().getDrawable(me.suan.example.R.drawable.ic_button_indicator_pressed);
        checkedIndicatorDrawable = getResources().getDrawable(me.suan.example.R.drawable.ic_button_indicator_checked_normal);
        checkedPressedIndicatorDrawable = getResources().getDrawable(me.suan.example.R.drawable.ic_button_indicator_checked_pressed);
        baselineDrawable = getResources().getDrawable(me.suan.example.R.drawable.ic_switch_baseline);
    }

    public void setSwitchOn(boolean switchOn) {
        this.switchOn = switchOn;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgressBar(canvas);
        drawNode(canvas);
    }

    private void drawProgressBar(Canvas canvas) {
        int length = (getWidth() - baselinePaddingHorizontal * 2);
        int startX = (getWidth() - length) / 2;
        int centerY = getHeight() / 2;
        Rect baselineRect = new Rect(startX, centerY - baselineHeight / 2, startX + length, centerY + baselineHeight / 2);
        baselineDrawable.setBounds(baselineRect);
        baselineDrawable.draw(canvas);
    }

    private void drawNode(Canvas canvas) {
        Drawable target = null;
        if (pressed) {
            if (currentX < getWidth() / 2) {
                target = normalPressedIndicatorDrawable;
            } else {
                target = checkedPressedIndicatorDrawable;
            }
        } else {
            if (switchOn) {
                target = normalIndicatorDrawable;
            } else {
                target = checkedIndicatorDrawable;
            }
        }
//        if (currentX < getWidth() / 2) {
//            if (pressed) {
//                target = normalPressedIndicatorDrawable;
//            } else {
//                target = normalIndicatorDrawable;
//            }
//        } else {
//            if (pressed) {
//                target = checkedPressedIndicatorDrawable;
//            } else {
//                target = checkedIndicatorDrawable;
//            }
//        }
        int rightBoundary = getWidth() - baselinePaddingHorizontal - (indicatorWidth / 2 - baselineHeight / 2);
        int leftBoundary = baselinePaddingHorizontal + (indicatorWidth / 2 - baselineHeight / 2);
        int centerY = getHeight() / 2;
        if (pressed) {
            int drawCenterX = (int) currentX;
            if (drawCenterX < leftBoundary) {
                drawCenterX = leftBoundary;
            } else if (drawCenterX > rightBoundary) {
                drawCenterX = rightBoundary;
            }
            Rect targetRect = new Rect(drawCenterX - indicatorWidth / 2, centerY - indicatorHeight / 2, drawCenterX + indicatorWidth / 2, centerY + indicatorHeight / 2);
            target.setBounds(targetRect);
            target.draw(canvas);
        } else {
            int drawCenterX = switchOn ? leftBoundary : rightBoundary;
            Rect targetRect = new Rect(drawCenterX - indicatorWidth / 2, centerY - indicatorHeight / 2, drawCenterX + indicatorWidth / 2, centerY + indicatorHeight / 2);
            target.setBounds(targetRect);
            target.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getX();
        float currentY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downTime = System.currentTimeMillis();
                startX = currentX;
                startY = currentY;
                pressed = true;
                break;
            case MotionEvent.ACTION_MOVE: {
                float moveX = currentX - startX;
                float moveY = currentY - startY;

                if (Math.abs(moveX) > MOVE_SLOP || Math.abs(moveY) > MOVE_SLOP) {
                    float deltaX = currentX - lastX;
//                    int rightBoundary = getWidth() - baselinePaddingHorizontal - (indicatorWidth / 2 - baselineHeight / 2);
//                    int leftBoundary = baselinePaddingHorizontal + (indicatorWidth / 2 - baselineHeight / 2);
//                    float willX = this.currentX + deltaX;
                    this.currentX += deltaX;
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                float moveX = currentX - startX;
                float moveY = currentY - startY;

                if (Math.abs(moveX) < MOVE_SLOP && Math.abs(moveY) < MOVE_SLOP && System.currentTimeMillis() - downTime < CLICK_TIME_THRESHOLD) {
                    switchOn = !switchOn;
                    if (onSwitchChangeListener != null) {
                        onSwitchChangeListener.onSwitchChange(switchOn);
                    }
                    if (onClickListener != null) {
                        onClickListener.onClick(this);
                    }
                } else {
                    if (this.currentX > getWidth() / 2) {
                        switchOn = false;
                    } else {
                        switchOn = true;
                    }
                    if (onSwitchChangeListener != null) {
                        onSwitchChangeListener.onSwitchChange(switchOn);
                    }
                }
                pressed = false;
            }
            case MotionEvent.ACTION_CANCEL: {
                pressed = false;
                break;
            }
        }

        invalidate();
        lastX = currentX;
        lastY = currentY;
        return true;
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnSwitchChangeListener(OnSwitchChangeListener onSwitchChangeListener) {
        this.onSwitchChangeListener = onSwitchChangeListener;
    }

    public interface OnSwitchChangeListener {
        public void onSwitchChange(boolean switchOn);
    }

}
