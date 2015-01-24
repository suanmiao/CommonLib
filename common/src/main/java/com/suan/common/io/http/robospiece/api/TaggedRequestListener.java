package com.suan.common.io.http.robospiece.api;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.suan.common.io.http.BaseRequest;
import com.suan.common.io.http.MRequestListener;
import com.suan.common.io.http.robospiece.RequestManager;

/**
 * Created by suanmiao on 14/12/6.
 */
public class TaggedRequestListener<T> implements RequestListener<T> {

  private Object tag;
  private BaseRequest request;
  private MRequestListener<T> listener;
  private RequestManager requestManager;

  public TaggedRequestListener(Object tag, BaseRequest request, RequestListener<T> listener,
      RequestManager requestManager) {
    this.tag = tag;
    this.requestManager = requestManager;
    this.request = request;
    this.listener = new MRequestListener<T>(listener);
  }

  public TaggedRequestListener(Object tag, BaseRequest request,
      MRequestListener.VolleyListener<T> listener1, RequestManager requestManager) {
    this.tag = tag;
    this.requestManager = requestManager;
    this.request = request;
    this.listener = new MRequestListener<T>(listener1);
  }

  @Override
  public void onRequestFailure(SpiceException spiceException) {
    requestManager.removeRequestFromTagList(tag, request);
    listener.onError(spiceException);
  }

  @Override
  public void onRequestSuccess(T o) {
    listener.onSuccess(o);
  }
}
