package com.suan.ptrListview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.suan.ptrListview.header.AbstractHeaderLayout;
import com.suan.ptrListview.header.DefaultHeaderLayout;


/**
 * Created by lhk on 2/6/14.
 */
public class PtrListview extends ListView implements
        AbsListView.OnScrollListener, IPullToRefresh {

    private final static float ratio = 2.0f;
    private AbstractHeaderLayout headerLayout;

    private RotateAnimation circleAnimation;
    /*
     * about footer view
     */
    private LinearLayout footerLayout;
    private ImageView footerCircleImageView;
    private int footerHeight;

    private int startY;
    private boolean isLoading = false;
    private REFRESH_STATE refreshState;
    private boolean lastItemVisibile = false;

    // 用于保证startY的值在一个完整的touch事件中只被记录一次
    private boolean isRecorded;

    private OnRefreshListener refreshListener;
    private OnLoadListener onLoadListener;
    private PullProgressListener progressListener;
    private OnScrollListener mScrollListener;

    private boolean catchMotionEvent;

    /*
     * outer variable
     */
    private boolean refreshEnable = true;

    private static final int VISIBLIE_SLOP = 30;

    public PtrListview(Context context) {
        super(context);
        init(context);
    }

    public PtrListview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PtrListview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        super.setOnScrollListener(this);

        circleAnimation = new RotateAnimation(0, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        circleAnimation.setInterpolator(new LinearInterpolator());
        circleAnimation.setDuration(500);
        circleAnimation.setRepeatCount(-1);

        // init state
        refreshState = REFRESH_STATE.DONE;
        // if catch the event
        catchMotionEvent = false;

        setHeaderLayout(new DefaultHeaderLayout(context));

    /*
     * about footer layout
     */
        footerLayout = (LinearLayout) inflater.inflate(
                R.layout.ptr_loading_layout, null);
        footerCircleImageView = (ImageView) footerLayout
                .findViewById(R.id.ptr_footer_circle);
        footerHeight = getResources().getDimensionPixelSize(R.dimen.footer_height);
        footerLayout.setPadding(0, 0, 0, -footerHeight);
        // footerLayout.setVisibility(View.INVISIBLE);
        addFooterView(footerLayout);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (catchMotionEvent && refreshEnable) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!isRecorded) {
                        isRecorded = true;
                    }
                    // find the issue ,should set start y out of record
                    // else some event will not be recorded
                    startY = (int) ev.getY();// position of event start
                    break;
                case MotionEvent.ACTION_UP:
                    if (refreshState != REFRESH_STATE.REFRESHING && refreshState != REFRESH_STATE.LOADING) {
                        if (refreshState == REFRESH_STATE.PULL_TO_REFRESH) {
                            refreshState = REFRESH_STATE.DONE;
                            headerLayout.animatePaddingTop(-headerLayout.getHeaderTotalHeight());
                        }
                        if (refreshState == REFRESH_STATE.RELEASE_TO_REFRESH) {
                            refreshState = REFRESH_STATE.REFRESHING;
                            headerLayout.animatePaddingTop(-(headerLayout.getHeaderTotalHeight() - headerLayout
                                    .getHeaderRefreshingHeight()));
                            if (refreshListener != null) {
                                refreshListener.onRefresh();
                            }
                        }
                    }
                    isRecorded = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int tempY = (int) ev.getY();
                    if (!isRecorded) {
                        isRecorded = true;
                        startY = tempY;
                    }
                    switch (refreshState) {
                        case RELEASE_TO_REFRESH:
                            // ensure the section is always the first one
                            setSelection(0);
                            if ((tempY - startY) > 0) {
                                if ((tempY - startY) / ratio < headerLayout.getHeaderRefreshingHeight()) {
                                    refreshState = REFRESH_STATE.PULL_TO_REFRESH;
                                }
                            } else {
                                refreshState = REFRESH_STATE.DONE;
                            }
                            setPullProgress((tempY - startY) / ratio
                                    / (float) headerLayout.getHeaderRefreshingHeight());
                            break;
                        case PULL_TO_REFRESH:
                            setSelection(0);
                            if ((tempY - startY) / ratio >= headerLayout.getHeaderRefreshingHeight()) {
                                // change state to rtr
                                refreshState = REFRESH_STATE.RELEASE_TO_REFRESH;
                            } else if (tempY - startY <= 0) {
                                refreshState = REFRESH_STATE.DONE;
                            }
                            setPullProgress((tempY - startY) / ratio
                                    / (float) headerLayout.getHeaderRefreshingHeight());
                            break;
                        case DONE:
                            if (tempY > startY) {
                                refreshState = REFRESH_STATE.PULL_TO_REFRESH;
                                setPullProgress((tempY - startY) / ratio
                                        / (float) headerLayout.getHeaderRefreshingHeight());
                            }
                            break;
                    }
                    break;

                default:
                    break;
            }
        }
        try {
            return super.onTouchEvent(ev);
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        } catch (IndexOutOfBoundsException e) {
            // samsung error
        }
    }

    public void setonRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
        catchMotionEvent = true;
    }

    @Override
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mScrollListener = onScrollListener;
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    public void setProgressListener(PullProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public void setRefreshEnable(boolean refreshEnable) {
        this.refreshEnable = refreshEnable;
    }

    public void setHeaderLayout(AbstractHeaderLayout headerLayout) {
        if (this.headerLayout != null) {
            removeHeaderView(this.headerLayout);
        }
        this.headerLayout = headerLayout;
        headerLayout.setPadding(0, -headerLayout.getHeaderTotalHeight(), 0, 0);
        addHeaderView(headerLayout, null, false);
    }

    @Override
    public REFRESH_STATE getRefreshState() {
        return refreshState;
    }

    @Override
    public boolean isRefreshEnable() {
        return refreshEnable;
    }

    @Override
    public void onRefreshStart() {
        refreshState = REFRESH_STATE.REFRESHING;
        setPullProgress(0);
    }

    @Override
    public void onRefreshComplete() {
        refreshState = REFRESH_STATE.DONE;
        headerLayout.animatePaddingTop(-headerLayout.getHeaderTotalHeight());
    }

    @Override
    public void onLoadStart() {
        isLoading = true;
        footerLayout.setPadding(0, 0, 0, 0);
        footerCircleImageView.clearAnimation();
        footerCircleImageView.startAnimation(circleAnimation);
    }

    @Override
    public void onLoadComplete() {
        isLoading = false;
        footerLayout.setPadding(0, 0, 0, -footerHeight);
        footerCircleImageView.clearAnimation();
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE && lastItemVisibile && onLoadListener != null && itemTakeFullPage()) {
            onLoadListener.onLastVisibleItem();
        }

        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    /**
     * to judge whether items take full page
     * if not ,even if last item is visible ,we should not call listener
     *
     * @return
     */
    private boolean itemTakeFullPage() {
        if (getChildCount() > 0) {
            int totalHeight = 0;
            for(int i = 0;i<getChildCount();i++){
                View child = getChildAt(i);
                Rect childRect = new Rect();
                child.getDrawingRect(childRect);
                totalHeight+=childRect.height();
            }
            Rect visibleRect = new Rect();
            getGlobalVisibleRect(visibleRect);
            return visibleRect.bottom - totalHeight < VISIBLIE_SLOP;
        }
        return false;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // when list is empty
        catchMotionEvent = firstVisibleItem == 0;

        lastItemVisibile = firstVisibleItem + visibleItemCount >= totalItemCount;
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
                    totalItemCount);
        }
    }

    public void setPullProgress(float progress) {
        if (progressListener != null) {
            progressListener.onPull(progress, refreshState);
        }
        if (headerLayout != null) {
            headerLayout.onPull(progress, refreshState);
            headerLayout.invalidate();
        }
    }

    public interface OnRefreshListener {
        public void onRefresh();
    }

    public interface OnLoadListener {
        public void onLastVisibleItem();
    }

    public interface PullProgressListener {
        public void onPull(float progress, REFRESH_STATE state);
    }
}
