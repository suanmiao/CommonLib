package me.suanmiao.example.ui.mvvm;

import me.suanmiao.common.mvvm.UICallback;
import me.suanmiao.example.io.http.BaseFormResult;

/**
 * Created by suanmiao on 15/3/3.
 */
public interface SUICallback extends UICallback {
  public void notifyStatus(BaseFormResult baseFormResult);

}
