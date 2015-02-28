package me.suanmiao.ptrListview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import me.suanmiao.ptrListview.footer.DefaultFooter;
import me.suanmiao.ptrListview.footer.IPTRFooter;
import me.suanmiao.ptrListview.header.DefaultHeader;
import me.suanmiao.ptrListview.header.IPTRHeader;


/**
 * Created by lhk on 2/6/14.
 */
public class PtrListView extends ListView implements
    AbsListView.OnScrollListener, IPullToRefresh {

  private final static float ratio = 2.0f;
  private IPTRHeader mHeader;
  private IPTRFooter mFooter;

  /**
   * normal mode
   */
  private int startY;
  private boolean lastItemVisibile = false;

  // 用于保证startY的值在一个完整的touch事件中只被记录一次
  private boolean isRecorded;

  /**
   * continous mode
   */
  private float lastY;
  private float currentPullingY;


  private OnRefreshListener refreshListener;
  private OnLoadListener onLoadListener;
  private PullProgressListener progressListener;
  private OnScrollListener mScrollListener;

  private boolean catchMotionEvent;
  private boolean isLoading = false;
  private REFRESH_STATE refreshState;

  private boolean refreshEnable = true;
  private boolean loadEnable = true;
  private boolean continuousPulling = false;

  private static final int VISIBLE_SLOP = 30;

  public PtrListView(Context context) {
    super(context);
    init(context);
  }

  public PtrListView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public PtrListView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context);
  }

  private void init(Context context) {
    super.setOnScrollListener(this);
    // init state
    refreshState = REFRESH_STATE.DONE;
    // if catch the event
    catchMotionEvent = false;

    mFooter = new DefaultFooter(getContext());
    mHeader = new DefaultHeader(getContext());
    addHeaderView(mHeader.getHeaderLayout());

    addFooterView(mFooter.getFooterLayout());
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    if (continuousPulling) {
      return continuousTouch(ev);
    } else {
      return normalTouch(ev);
    }
  }

  private boolean continuousTouch(MotionEvent ev) {
    // Log.e("SUAN", ev.getAction() + " amount: " + ev.getPointerCount());
    if (catchMotionEvent && refreshEnable) {
      float currentY = ev.getY();
      if (ev.getPointerCount() >= 2 && ev.getY(1) < currentY) {
        currentY = ev.getY(1);
      }
      switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
          if (!isRecorded) {
            isRecorded = true;
          }
          lastY = currentY;// position of event start
          break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
          if (refreshState != REFRESH_STATE.REFRESHING && refreshState != REFRESH_STATE.LOADING) {
            if (refreshState == REFRESH_STATE.PULL_TO_REFRESH) {
              refreshState = REFRESH_STATE.DONE;
              mHeader.onPullCancel();
            }
            if (refreshState == REFRESH_STATE.RELEASE_TO_REFRESH) {
              refreshState = REFRESH_STATE.REFRESHING;
              mHeader.onRefreshStart();
              if (refreshListener != null) {
                refreshListener.onRefresh();
              }
            }
          }
          isRecorded = false;
          currentPullingY = 0;
          break;
        case MotionEvent.ACTION_MOVE:
          if (!isRecorded) {
            isRecorded = true;
            lastY = currentY;
          }
          float deltaY = currentY - lastY;
          currentPullingY += deltaY;
          switch (refreshState) {
            case RELEASE_TO_REFRESH:
              // ensure the section is always the first one
              setSelection(0);
              if (currentPullingY > 0) {
                if (currentPullingY / ratio < mHeader.getHeaderRefreshingHeight()) {
                  refreshState = REFRESH_STATE.PULL_TO_REFRESH;
                }
              } else {
                refreshState = REFRESH_STATE.DONE;
              }
              setPullProgress(currentPullingY / ratio
                  / (float) mHeader.getHeaderRefreshingHeight());
              break;
            case PULL_TO_REFRESH:
              setSelection(0);
              if (currentPullingY / ratio >= mHeader.getHeaderRefreshingHeight()) {
                // change state to rtr
                refreshState = REFRESH_STATE.RELEASE_TO_REFRESH;
              } else if (currentPullingY <= 0) {
                refreshState = REFRESH_STATE.DONE;
              }
              setPullProgress(currentPullingY / ratio
                  / (float) mHeader.getHeaderRefreshingHeight());
              break;
            case DONE:
              if (currentPullingY > 0) {
                refreshState = REFRESH_STATE.PULL_TO_REFRESH;
                setPullProgress((currentPullingY) / ratio
                    / (float) mHeader.getHeaderRefreshingHeight());
              }
              break;
          }
          break;

        default:
          break;
      }
      lastY = currentY;
    }
    try {
      return super.onTouchEvent(ev);
    } catch (Exception e) {
      return true;
    }
  }

  private boolean normalTouch(MotionEvent ev) {
    // Log.e("SUAN", ev.getAction() + " amount: " + ev.getPointerCount());
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
              mHeader.onPullCancel();
            }
            if (refreshState == REFRESH_STATE.RELEASE_TO_REFRESH) {
              refreshState = REFRESH_STATE.REFRESHING;
              mHeader.onRefreshStart();
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
                if ((tempY - startY) / ratio < mHeader.getHeaderRefreshingHeight()) {
                  refreshState = REFRESH_STATE.PULL_TO_REFRESH;
                }
              } else {
                refreshState = REFRESH_STATE.DONE;
              }
              setPullProgress((tempY - startY) / ratio
                  / (float) mHeader.getHeaderRefreshingHeight());
              break;
            case PULL_TO_REFRESH:
              setSelection(0);
              if ((tempY - startY) / ratio >= mHeader.getHeaderRefreshingHeight()) {
                // change state to rtr
                refreshState = REFRESH_STATE.RELEASE_TO_REFRESH;
              } else if (tempY - startY <= 0) {
                refreshState = REFRESH_STATE.DONE;
              }
              setPullProgress((tempY - startY) / ratio
                  / (float) mHeader.getHeaderRefreshingHeight());
              break;
            case DONE:
              if (tempY > startY) {
                refreshState = REFRESH_STATE.PULL_TO_REFRESH;
                setPullProgress((tempY - startY) / ratio
                    / (float) mHeader.getHeaderRefreshingHeight());
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

  public void setLoadEnable(boolean loadEnable) {
    this.loadEnable = loadEnable;
  }

  public void setHeaderLayout(IPTRHeader header) {
    if (this.mHeader != null) {
      removeHeaderView(this.mHeader.getHeaderLayout());
    }
    this.mHeader = header;
    mHeader.onInit();
    addHeaderView(header.getHeaderLayout(), null, false);
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
    mHeader.onPullCancel();
  }

  @Override
  public boolean isLoadEnable() {
    return loadEnable;
  }

  @Override
  public void onLoadStart() {
    isLoading = true;
    mFooter.onLoadStart();
  }

  @Override
  public void onLoadComplete() {
    isLoading = false;
    mFooter.onLoadComplete();
  }

  @Override
  public boolean isLoading() {
    return isLoading;
  }

  @Override
  public void setContinuousPulling(boolean continous) {
    this.continuousPulling = continous;
  }

  public void setAdapter(ListAdapter adapter) {
    super.setAdapter(adapter);
  }

  @Override
  public void onScrollStateChanged(AbsListView view, int scrollState) {
    if (scrollState == SCROLL_STATE_IDLE && lastItemVisibile && onLoadListener != null
        && itemTakeFullPage() && loadEnable) {
      onLoadListener.onLastItemVisible();
    }

    if (mScrollListener != null) {
      mScrollListener.onScrollStateChanged(view, scrollState);
    }
  }

  /**
   * to judge whether items take full page
   * if not ,even if last item is visible ,we should not call listener
   *
   * @return whether items take up full page
   */
  private boolean itemTakeFullPage() {
    if (getChildCount() > 0) {
      int totalHeight = 0;
      for (int i = 0; i < getChildCount(); i++) {
        View child = getChildAt(i);
        Rect childRect = new Rect();
        child.getDrawingRect(childRect);
        totalHeight += childRect.height();
      }
      Rect visibleRect = new Rect();
      getGlobalVisibleRect(visibleRect);
      return (visibleRect.bottom - visibleRect.top) - totalHeight < VISIBLE_SLOP;
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
    if (mHeader != null) {
      mHeader.onPull(progress, refreshState);
    }
  }

  public interface OnRefreshListener {
    public void onRefresh();
  }

  public interface OnLoadListener {
    public void onLastItemVisible();
  }

  public interface PullProgressListener {
    public void onPull(float progress, REFRESH_STATE state);
  }
}
