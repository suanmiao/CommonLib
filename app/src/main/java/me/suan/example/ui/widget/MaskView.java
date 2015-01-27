package me.suan.example.ui.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import me.suan.example.R;
import me.suan.example.util.TextUtil;

/**
 * Created by suanmiao on 14/12/30.
 */
public class MaskView extends View {

    private float currentRaidus;
    private float maxRaidus;
    private float minRaidus;

    private float hintTextSize;
    private String hint;

    private boolean waving = false;
    private boolean spreading = true;

    private ValueAnimator wavingAnimation;
    private static final long LOADING_ANIMATION_DURATION = 500l;

    private int maskColor;
    private int hintTextColor;

    public MaskView(Context context) {
        super(context);
        init();
    }

    public MaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        maxRaidus = getResources().getDimensionPixelSize(R.dimen.default_max_hole_radius);
        minRaidus = getResources().getDimensionPixelSize(R.dimen.default_min_hole_radius);
        hintTextSize = getResources().getDimensionPixelSize(R.dimen.default_hint_size);
        maskColor = getResources().getColor(R.color.article_comment_hint_mask);
        hintTextColor = getResources().getColor(R.color.pure_white);
        hint = "comment hint";
    }

    public void startWaving() {
        if (waving) {
            return;
        }
        waving = true;
        if (wavingAnimation != null) {
            wavingAnimation.cancel();
        }
        wavingAnimation = ValueAnimator.ofFloat(0, 1);
        wavingAnimation.setDuration(LOADING_ANIMATION_DURATION).setInterpolator(new AccelerateInterpolator());
        wavingAnimation.addUpdateListener(updateListener);
        wavingAnimation.addListener(loadingAnimatorListener);
        wavingAnimation.start();
    }

    private ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float value = (Float) animation.getAnimatedValue();
            currentRaidus = (maxRaidus - minRaidus) * value + minRaidus;
            invalidate();
        }
    };

    private Animator.AnimatorListener loadingAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (waving) {
                spreading = !spreading;
                if (spreading) {
                    wavingAnimation = ValueAnimator.ofFloat(0, 1);
                    wavingAnimation.setInterpolator(new DecelerateInterpolator());
                } else {
                    wavingAnimation = ValueAnimator.ofFloat(1, 0);
                    wavingAnimation.setInterpolator(new AccelerateInterpolator());
                }
                wavingAnimation.addUpdateListener(updateListener);
                wavingAnimation.addListener(loadingAnimatorListener);
                wavingAnimation.start();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    public void stopWaving() {
        if (!waving) {
            return;
        }
        waving = false;
        if (wavingAnimation != null) {
            wavingAnimation.removeAllListeners();
            wavingAnimation.cancel();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawMask(canvas);
    }

    private void drawMask(Canvas canvas) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        Path mPath = new Path();
        Paint mPaint = new Paint();
        mPaint.setColor(maskColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mPath.moveTo(0, 0);
        mPath.lineTo(getWidth(), 0);
        mPath.lineTo(getWidth(), getHeight());
        mPath.lineTo(0, getHeight());
        mPath.close();

        mPath.addCircle(centerX, centerY, currentRaidus, Path.Direction.CCW);
        mPath.close();
        canvas.drawPath(mPath, mPaint);


        //draw hint
        float textWidth = TextUtil.getTextWidth(mPaint, hint, hintTextSize);

        float textPositionY = centerY - maxRaidus - hintTextSize * 2;
        mPaint.setTextSize(hintTextSize);
        mPaint.setColor(hintTextColor);
        canvas.drawText(hint, centerX - textWidth / 2, textPositionY, mPaint);
    }

    public Rect getTriggerRect() {
        Rect globalRect = new Rect();
        getGlobalVisibleRect(globalRect);
        return new Rect(getWidth() / 2 - (int) maxRaidus+globalRect.left, getHeight() / 2 - (int) maxRaidus+globalRect.top, getWidth() / 2 + (int) maxRaidus+globalRect.left, getHeight() / 2 + (int) maxRaidus+globalRect.top);
    }
}
