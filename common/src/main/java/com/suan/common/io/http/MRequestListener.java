package com.suan.common.io.http;

import com.android.volley.Response;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * Created by suanmiao on 15/1/18.
 */
public class MRequestListener<T> {

  RequestListener<T> roboRequestListener;

  Response.Listener<T> volleyListener;
  Response.ErrorListener errorListener;

  public enum ListenerType {
    ROBO_LISTENER,
    VOLLEY_LISTENER
  }

  private ListenerType mListenerType;

  public MRequestListener(RequestListener<T> requestListener) {
    this.roboRequestListener = requestListener;
    this.mListenerType = ListenerType.ROBO_LISTENER;
  }

  public MRequestListener(VolleyListener volleyListener) {
    this.volleyListener = volleyListener;
    this.errorListener = volleyListener;
    this.mListenerType = ListenerType.VOLLEY_LISTENER;
  }

  public RequestListener<T> getRoboRequestListener() {
    return roboRequestListener;
  }

  public Response.Listener<T> getVolleyListener() {
    return volleyListener;
  }

  public Response.ErrorListener getErrorListener() {
    return errorListener;
  }

  public ListenerType getListenerType() {
    return mListenerType;
  }

  public void onSuccess(T o) {
    switch (mListenerType) {
      case ROBO_LISTENER:
        this.roboRequestListener.onRequestSuccess(o);
        break;
      case VOLLEY_LISTENER:
        this.volleyListener.onResponse(o);
        break;
    }
  }

  public void onError(SpiceException spiceException) {
    this.roboRequestListener.onRequestFailure(spiceException);
  }

  public interface VolleyListener<T> extends Response.ErrorListener, Response.Listener<T> {

  }
}
