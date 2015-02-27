package me.suanmiao.ptrListview.footer;

import android.view.View;

/**
 * Created by suanmiao on 15/2/27.
 */
public interface IPTRFooter {

  public void onLoadStart();

  public void onLoadComplete();

  public View getFooterLayout();

  public int getFooterHeight();
}
