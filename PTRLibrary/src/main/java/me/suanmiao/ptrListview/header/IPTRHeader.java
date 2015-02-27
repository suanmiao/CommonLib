package me.suanmiao.ptrListview.header;

import android.view.View;

import me.suanmiao.ptrListview.IPullToRefresh;
import me.suanmiao.ptrListview.PtrListView;

/**
 * Created by suanmiao on 14-11-3.
 */
public interface IPTRHeader extends PtrListView.PullProgressListener {

  /**
   * 
   * @return the total height of header ,include all the space(height) that header content display
   *         for example ,you can pull header in any distance ,but the header will have a exactly
   *         display area
   *         this is the height of area
   */
  public int getHeaderTotalHeight();

  /**
   * 
   * @return the height of header when list view is in refreshing mode
   */
  public int getHeaderRefreshingHeight();

  public int getHeaderCurrentPaddingTop();

  public View getHeaderLayout();

  public void onPull(float progress, IPullToRefresh.REFRESH_STATE refreshState);

  public void onPullCancel();

  public void onRefreshStart();

  public void onInit();
}
