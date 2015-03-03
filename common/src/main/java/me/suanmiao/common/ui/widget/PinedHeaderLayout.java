package me.suanmiao.common.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by suanmiao on 14-10-3.
 * function for this class:
 * a scroll container for two or more child ,and the header will be pined when scroll up to a distance
 *
 * difficulty I cannot overcome
 * 1.cannot know whether child can scroll down , this is important when header is pined
 */
public class PinedHeaderLayout extends ViewGroup {

    private int headerHeight;
    private View headerChild;
    private int stickyHeight;
    private int pinHeaderPaddingTop;
    private boolean pined = false;

    // vertical
    private int availableHeight;
    private int maxHeight = 0;


    public PinedHeaderLayout(Context context) {
        this(context, null);
    }

    public PinedHeaderLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PinedHeaderLayout(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        initAttr(context, attributeSet, defStyle);
    }

    private void initAttr(Context context, AttributeSet attributeSet, int defStyle) {
//    TypedArray a =
//        context.obtainStyledAttributes(attributeSet, R.styleable.FlowLayout, defStyle, 0);
//    try {
//
//   } finally {
//      a.recycle();
//    }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
//        if (!pined) {
//            return true;
//        } else {
//            return super.onInterceptTouchEvent(ev);
//        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private float lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getX();
        float currentY = event.getY();
        float deltaY = currentY - lastY;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (!pined) {
                    if (getScrollY() >= headerHeight) {
                        pined = true;
                        scrollTo(0, headerHeight);
                    } else {
                        scrollBy(0, -(int) deltaY);
                    }
                } else {
                    if (getScrollY() < headerHeight) {
                        pined = false;
                    }
                    if (pined) {
                        dispatchToChild(event);
                    } else {
                        scrollBy(0, -(int) deltaY);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                break;

        }
        lastY = currentY;
        return true;
//       return super.onTouchEvent(event);
    }

    private void dispatchToChild(MotionEvent event) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            Rect childRect = new Rect();
            child.getLocalVisibleRect(childRect);
            MotionEvent movedEvent = MotionEvent.obtain(event);
            int offsetY = getScrollY() + childRect.top;
            movedEvent.setLocation(movedEvent.getX(), movedEvent.getY() - offsetY);
            headerChild.onTouchEvent(movedEvent);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureVertically(widthMeasureSpec, heightMeasureSpec);
    }

    private void measureVertically(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        availableHeight = heightSize - getPaddingTop() - getPaddingBottom();

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParam lp = (LayoutParam) child.getLayoutParams();

            int childWidthMode = MeasureSpec.EXACTLY;
            int childHeightMode = MeasureSpec.EXACTLY;

            int childWidthSize = lp.width;
            int childHeightSize = lp.height;

            if (widthMode == MeasureSpec.UNSPECIFIED && childWidthSize == 0) {
                childWidthMode = MeasureSpec.UNSPECIFIED;
            }
            if (childWidthSize == LayoutParams.MATCH_PARENT) {
                childWidthSize = widthSize;
            }

            /**
             * measure height
             */
            if (lp.height == LayoutParams.MATCH_PARENT) {
                childHeightSize = availableHeight;
            } else if (lp.height == LayoutParams.WRAP_CONTENT) {
                child.measure(MeasureSpec.makeMeasureSpec(childWidthSize, childWidthMode),
                        MeasureSpec.makeMeasureSpec(childHeightSize, childHeightMode));
                childWidthSize = child.getMeasuredWidth();
                childHeightSize = child.getMeasuredHeight();
            }
            if (i != 0) {
                availableHeight = 0;
            } else {
                headerHeight = childHeightSize;
                headerChild = child;
            }

            /**
             * measure width
             */
            if (lp.width == LayoutParams.MATCH_PARENT) {
                childWidthSize = widthSize;
            } else if (lp.width == LayoutParams.WRAP_CONTENT) {
                child.measure(MeasureSpec.makeMeasureSpec(childWidthSize, childWidthMode),
                        MeasureSpec.makeMeasureSpec(childHeightSize, childHeightMode));
                childWidthSize = child.getMeasuredWidth();
                childHeightSize = child.getMeasuredHeight();
            }

            if (lp.height != LayoutParams.WRAP_CONTENT || lp.width != LayoutParams.WRAP_CONTENT) {
                child.measure(MeasureSpec.makeMeasureSpec(childWidthSize, childWidthMode),
                        MeasureSpec.makeMeasureSpec(childHeightSize, childHeightMode));
                childWidthSize = child.getMeasuredWidth();
                childHeightSize = child.getMeasuredHeight();
            }
        }

        int currentRowHeight = 0;
        int totalWidth = getPaddingLeft();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParam lp = (LayoutParam) child.getLayoutParams();
            int childHeight = child.getMeasuredHeight();
            int childWidth = child.getMeasuredWidth();
            lp.top = currentRowHeight + lp.topMargin;
            lp.left = lp.leftMargin;
//            Log.e("SUAN", "measure child " + i + "|" + lp.top + "|" + lp.left + "|" + childWidth + "|" + childHeight);

            currentRowHeight += (lp.topMargin + childHeight + lp.bottomMargin);
            totalWidth = Math.max(totalWidth, lp.leftMargin + childWidth + lp.rightMargin);
        }

        int measuredWidth = (widthMode == MeasureSpec.UNSPECIFIED || widthMode == MeasureSpec.AT_MOST)
                ? totalWidth +
                getPaddingRight()
                : widthSize;
        int measuredHeight = (heightMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.AT_MOST)
                ? maxHeight +
                getPaddingLeft()
                +
                getPaddingBottom()
                : heightSize;
//        Log.e("SUAN", "measure dimens " + measuredWidth + "|" + measuredHeight + "|");

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChild(changed, l, t, r, b);
    }

    private void layoutChild(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            LayoutParam lp = (LayoutParam) child.getLayoutParams();

            int left = lp.left;
            int top = lp.top;
            child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
        }
    }

    @Override
    protected LayoutParam generateDefaultLayoutParams() {
        return new LayoutParam(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected LayoutParam generateLayoutParams(LayoutParams p) {
        return new LayoutParam(p);
    }

    @Override
    public LayoutParam generateLayoutParams(AttributeSet attrs) {
        return new LayoutParam(getContext(), attrs);
    }

    public static class LayoutParam extends MarginLayoutParams {

        public static final int LINE_NUM_INVALID = Integer.MIN_VALUE;
        public float weight = -1;
        public int lineNum = LINE_NUM_INVALID;
        public int horizontalSpacing = 0;
        public int verticalSpacing = 0;
        public int left = -1;
        public int top = -1;

        public LayoutParam(int width, int height) {
            super(width, height);
        }

        public LayoutParam(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParam(LayoutParams source) {
            super(source);
        }
    }

}
