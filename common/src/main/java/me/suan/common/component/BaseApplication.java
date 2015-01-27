package me.suan.common.component;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.squareup.okhttp.OkHttpClient;
import me.suan.common.io.http.robospiece.RequestManager;


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
    requestManager = new RequestManager(this);
    setExceptionHandler();
  }

  public abstract Class getRequestService();

  public abstract OkHttpClient getOkHttpClient();

  private void setExceptionHandler() {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        Thread.setDefaultUncaughtExceptionHandler(new SExceptionHandler(Thread
            .getDefaultUncaughtExceptionHandler()));
      }
    }, 200);
  }

  public static Context getAppContext() {
    return context;
  }

  public static RequestManager getRequestManager() {
    return requestManager;
  }

}
