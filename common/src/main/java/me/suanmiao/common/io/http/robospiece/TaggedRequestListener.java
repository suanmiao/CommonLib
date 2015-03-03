package me.suanmiao.common.io.http.robospiece;

import com.android.volley.VolleyError;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import me.suanmiao.common.io.http.RequestManager;
import me.suanmiao.common.io.http.volley.IVolleyListener;

/**
 * Created by suanmiao on 14/12/6.
 * universal listener for both Volley and RoboSpice
 */
public class TaggedRequestListener<T>
    implements RequestListener<T>, IVolleyListener<T> {

  private RequestListener<T> spiceListener;
  private IVolleyListener<T> volleyListener;
  private String hashTag;
  private RequestManager.RequestFinishListener mRequestFinishListener;

  public TaggedRequestListener(RequestListener<T> listener) {
    this.spiceListener = listener;
  }

  public TaggedRequestListener(
      IVolleyListener<T> listener1) {
    this.volleyListener = listener1;
  }

  public void mark(String hashTag, RequestManager.RequestFinishListener requestFinishListener) {
    this.hashTag = hashTag;
    this.mRequestFinishListener = requestFinishListener;
  }

  @Override
  public void onRequestFailure(SpiceException spiceException) {
    spiceListener.onRequestFailure(spiceException);
    if (mRequestFinishListener != null) {
      mRequestFinishListener.onFinish(hashTag);
      mRequestFinishListener = null;
    }
  }

  @Override
  public void onRequestSuccess(T o) {
    spiceListener.onRequestSuccess(o);
    if (mRequestFinishListener != null) {
      mRequestFinishListener.onFinish(hashTag);
      mRequestFinishListener = null;
    }
  }

  @Override
  public void onErrorResponse(VolleyError error) {
    volleyListener.onErrorResponse(error);
    if (mRequestFinishListener != null) {
      mRequestFinishListener.onFinish(hashTag);
      mRequestFinishListener = null;
    }
  }

  @Override
  public void onResponse(T response) {
    volleyListener.onResponse(response);
    if (mRequestFinishListener != null) {
      mRequestFinishListener.onFinish(hashTag);
      mRequestFinishListener = null;
    }
  }
}
