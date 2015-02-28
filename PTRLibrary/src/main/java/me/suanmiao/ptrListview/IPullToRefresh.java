package me.suanmiao.ptrListview;

/**
 * Created by suanmiao on 14-11-3.
 */
public interface IPullToRefresh {
  static enum REFRESH_STATE {
    RELEASE_TO_REFRESH,
    PULL_TO_REFRESH,
    REFRESHING,
    DONE,
    LOADING
  }

  public REFRESH_STATE getRefreshState();

  public boolean isRefreshEnable();

  public void setRefreshEnable(boolean enable);

  /**
   * called when refresh start
   */
  public void onRefreshStart();


  /**
   * called when refresh complete
   */
  public void onRefreshComplete();

  public boolean isLoadEnable();

  public void setLoadEnable(boolean enable);

  public void onLoadStart();

  public void onLoadComplete();

  public boolean isLoading();

  public void setContinuousPulling(boolean continous);
}
