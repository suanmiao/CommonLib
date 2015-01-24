package com.suan.common.io.http.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.Map;

/**
 * Created by suanmiao on 15/1/19.
 */
public class FakeVolleyRequest<T> extends Request<T> {

  private IVolleyActionDelivery<T> volleyActionDelivery;
  private Response.Listener<T> resultListener;
  private Map<String, String> headers;
  private Map<String, String> postParams;

  public FakeVolleyRequest(int method, String url, Map<String, String> headers,
      Map<String, String> postParams,
      IVolleyActionDelivery<T> volleyActionDelivery,
      Response.Listener<T> resultListener,
      Response.ErrorListener errorListener) {
    super(method, url, errorListener);
    this.headers = headers;
    this.postParams = postParams;
    this.volleyActionDelivery = volleyActionDelivery;
    this.resultListener = resultListener;
  }

  @Override
  protected Response<T> parseNetworkResponse(NetworkResponse response) {
    return volleyActionDelivery.parseNetworkResponse(response);
  }

  @Override
  protected void deliverResponse(T response) {
    resultListener.onResponse(response);
  }

  @Override
  public Map<String, String> getHeaders() throws AuthFailureError {
    if (headers != null) {
      return headers;
    } else {
      return super.getHeaders();
    }
  }

  /**
   * 
   * @return params for POST request
   * @throws AuthFailureError
   */
  @Override
  protected Map<String, String> getParams() throws AuthFailureError {
    if (this.postParams != null) {
      return this.postParams;
    } else {
      return super.getParams();
    }
  }
}
