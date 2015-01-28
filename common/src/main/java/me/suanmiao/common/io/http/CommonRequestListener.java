package me.suanmiao.common.io.http;

import com.octo.android.robospice.request.listener.RequestListener;
import me.suanmiao.common.io.http.volley.IVolleyListener;

/**
 * Created by suanmiao on 15/1/18.
 * Universal listener for a request
 */
public abstract class CommonRequestListener<T> {

  public enum ListenerType {
    ROBO_LISTENER,
    VOLLEY_LISTENER
  }

  private ListenerType mListenerType;

  public abstract RequestListener<T> getRoboRequestListener();
  public abstract IVolleyListener<T> getVolleyListener();

  public abstract ListenerType getListenerType();

}
