package me.suanmiao.common.component;

import android.app.Application;
import android.content.Context;

import me.suanmiao.common.io.http.RequestManager;


/**
 * Created by suanmiao on 14-10-31.
 */
public abstract class BaseApplication extends Application {

  private static Context context;
  private static RequestManager requestManager;

  @Override
  public void onCreate() {
    super.onCreate();
    init();
  }

  private void init() {
    context = getApplicationContext();
    requestManager = initRequestManager();
  }

  protected abstract RequestManager initRequestManager();

  public static Context getAppContext() {
    return context;
  }

  public static RequestManager getRequestManager() {
    return requestManager;
  }

}
