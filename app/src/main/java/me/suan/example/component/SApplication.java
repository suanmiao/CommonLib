package me.suan.example.component;

import android.os.Handler;

import com.squareup.okhttp.OkHttpClient;
import me.suan.common.component.BaseApplication;
import me.suan.example.io.http.api.WaceRequestService;

/**
 * Created by suanmiao on 14-10-31.
 */
public class SApplication extends BaseApplication {

  @Override
  public Class getRequestService() {
    return WaceRequestService.class;
  }

  @Override
  public OkHttpClient getOkHttpClient() {
    return WaceRequestService.getOkHttpClient();
  }

  private void setExceptionHandler() {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        Thread.setDefaultUncaughtExceptionHandler(new SExceptionHandler(Thread
            .getDefaultUncaughtExceptionHandler()));
      }
    }, 200);
  }

}
