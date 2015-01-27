package me.suan.common.ui.dialog;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import me.suan.common.ui.dialog.animation.BaseEffect;
import me.suan.common.ui.widget.MoveAbler;


/**
 * Created by suanmiao on 14/12/12.
 */
public class BaseDialog extends Dialog implements IDialog {

    private boolean moveAble = true;
    private boolean canceledOnTouchOutside = false;
    private boolean cancelAble = true;
    private boolean showing = false;
    private ViewGroup decorView;
    private LinearLayout contentHolder;
    private View contentView;
    private WindowManager mWindowManager;
    private LayoutInflater inflater;

    private BaseEffect showEffect, dismissEffect;

    public BaseDialog(Context context) {
        super(context);
        init();
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        //remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        decorView = (ViewGroup) getWindow().getDecorView();
        //
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //make default background transparent
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        setupWindowParam();

        ViewGroup contentParent = (ViewGroup) decorView.findViewById(android.R.id.content);
        contentHolder = new LinearLayout(getContext());
        contentHolder.setGravity(Gravity.CENTER);
        contentHolder.setBackgroundResource(me.suan.common.R.color.dialog_mask);
        contentParent.addView(contentHolder, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setBackgroundDrawable(Drawable drawable){
        getWindow().setBackgroundDrawable(drawable);
    }

    public void setBackgroundDrawableResource(int resource){
        getWindow().setBackgroundDrawableResource(resource);
    }

    private void setupWindowParam() {
        if (getContext() instanceof Activity) {
            Activity originalActivity = (Activity) getContext();
            WindowManager.LayoutParams sourceLayoutParams = originalActivity.getWindow().getAttributes();
//            WindowManager.LayoutParams currentLayoutParam = getWindow().getAttributes();
//            currentLayoutParam.flags |= (sourceLayoutParams.flags | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            currentLayoutParam.flags |= (sourceLayoutParams.flags | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            currentLayoutParam.flags |= (sourceLayoutParams.flags | WindowManager.LayoutParams.FLAG_FULLSCREEN);
            WindowManager.LayoutParams currentLayoutParam = getWindow().getAttributes();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            currentLayoutParam.copyFrom(sourceLayoutParams);
            getWindow().setAttributes(currentLayoutParam);
        }
    }

    public void setCancelAble(boolean cancelAble) {
        this.cancelAble = cancelAble;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (cancelAble && keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (canceledOnTouchOutside && isOutOfBounds(event)) {
            dismiss();
        }
        return super.onTouchEvent(event);
    }

    public boolean isOutOfBounds(MotionEvent event) {
        if (contentView == null) {
            return true;
        }
        Rect contentRect = new Rect();
        contentView.getLocalVisibleRect(contentRect);
        return !contentRect.contains((int) event.getX(), (int) event.getY());
    }

    @Override
    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        canceledOnTouchOutside = cancel;
    }

    @Override
    public void setMoveAble(boolean moveAble) {
        if (this.moveAble && !moveAble) {
            this.contentView.setOnTouchListener(null);
        } else if (moveAble && contentView != null) {
            contentView.setOnTouchListener(new MoveAbler());
        }
        this.moveAble = moveAble;
    }

    @Override
    public void show() {
        if (!showing) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            try {
                mWindowManager.addView(decorView, layoutParams);
                if (showEffect != null) {
                    showEffect.setTargetView(contentView);
                    showEffect.start();
                }
                showing = true;
            } finally {
            }
        }
    }

    @Override
    public void dismiss() {
        if (showing) {
            if (dismissEffect != null) {
                dismissEffect.setTargetView(contentView);
                dismissEffect.addEffectListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        removeDecor();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        removeDecor();
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                dismissEffect.start();
                showing = false;
            } else {
                removeDecor();
            }
        }
    }

    private void removeDecor() {
        try {
            mWindowManager.removeView(decorView);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        showing = false;
    }

    @Override
    public void setShowEffect(BaseEffect effect) {
        this.showEffect = effect;
    }

    @Override
    public void setDismissEffect(BaseEffect effect) {
        this.dismissEffect = effect;
    }

    @Override
    public void setContentView(int layoutResID) {
        contentView = inflater.inflate(layoutResID, contentHolder, false);
        setContentView(contentView, contentView.getLayoutParams());
        if (moveAble) {
            contentView.setOnTouchListener(new MoveAbler());
        }
    }

    @Override
    public void setContentView(View view) {
        if (view.getLayoutParams() == null) {
            setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            setContentView(view, view.getLayoutParams());
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        contentView = view;
        contentHolder.removeAllViews();
        if (moveAble) {
            contentView.setOnTouchListener(new MoveAbler());
        }
        contentHolder.addView(view, params);
    }
}
