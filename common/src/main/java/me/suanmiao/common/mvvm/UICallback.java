package me.suanmiao.common.mvvm;

/**
 * Created by suanmiao on 15/3/3.
 */
public interface UICallback {
  public void notifyUIChange();

  public void notifyException(Exception exception);

}
