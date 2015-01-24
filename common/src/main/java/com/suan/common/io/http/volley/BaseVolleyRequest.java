package com.suan.common.io.http.volley;

import com.suan.common.io.http.BaseRequest;

/**
 * Created by suanmiao on 15/1/19.
 */
public class BaseVolleyRequest<T> extends BaseRequest<T> {
  public BaseVolleyRequest(int method, String url, IVolleyActionDelivery<T> volleyActionDelivery) {
    super(method, url,null,null, volleyActionDelivery);
  }

  @Override
  public void cancel() {
      super.cancel();
  }

  @Override
  public void cancel(boolean interrupt) {

  }
}
