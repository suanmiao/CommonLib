package me.suan.ptrListview.header;

import me.suan.ptrListview.PtrListview;

/**
 * Created by suanmiao on 14-11-3.
 */
public interface IPTRHeader extends PtrListview.PullProgressListener {

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

  public int getHeaderPaddingTop();

}
