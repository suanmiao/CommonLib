package me.suanmiao.common.io.http;

import com.octo.android.robospice.request.listener.RequestListener;
import me.suanmiao.common.io.http.volley.IVolleyListener;

/**
 * Created by suanmiao on 15/1/26.
 */
public abstract class SpiceCommonListener<T> extends CommonRequestListener<T>
    implements
      RequestListener<T> {
  @Override
  public RequestListener<T> getRoboRequestListener() {
    return this;
  }

  @Override
  public IVolleyListener<T> getVolleyListener() {
    return null;
  }

  @Override
  public ListenerType getListenerType() {
    return ListenerType.ROBO_LISTENER;
  }
}
