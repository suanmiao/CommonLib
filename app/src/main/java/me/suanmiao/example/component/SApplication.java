package me.suanmiao.example.component;

import me.suanmiao.common.component.BaseApplication;
import me.suanmiao.common.io.http.RequestManager;

/**
 * Created by suanmiao on 14-10-31.
 */
public class SApplication extends BaseApplication {

  @Override
  protected RequestManager initRequestManager() {
    return new RequestManager(this,".commonExample");
  }
}
