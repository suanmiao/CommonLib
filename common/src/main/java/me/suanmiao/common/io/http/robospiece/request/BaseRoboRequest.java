package me.suanmiao.common.io.http.robospiece.request;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

/**
 * Created by suanmiao on 14/12/6.
 */
public abstract class BaseRoboRequest<T, V> extends RetrofitSpiceRequest<T, V> {
  public BaseRoboRequest(Class<T> clazz, Class<V> retrofitedInterfaceClass) {
    super(clazz, retrofitedInterfaceClass);
  }
}
