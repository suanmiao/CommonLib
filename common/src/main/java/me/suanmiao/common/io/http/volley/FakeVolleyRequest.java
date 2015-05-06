package me.suanmiao.common.io.http.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import me.suanmiao.common.io.http.exception.CommonParamException;
import me.suanmiao.common.io.http.exception.CommonRequestException;
import me.suanmiao.common.io.http.robospiece.TaggedRequestListener;

/**
 * Created by suanmiao on 15/1/19.
 */
public class FakeVolleyRequest<T> extends Request<T> {

  private IVolleyActionDelivery<T> volleyActionDelivery;
  private TaggedRequestListener<T> taggedRequestListener;
  private Map<String, String> headers;
  private Map<String, String> params;

  public FakeVolleyRequest(int method, String url, Map<String, String> headers,
      Map<String, String> params,
      IVolleyActionDelivery<T> volleyActionDelivery,
      TaggedRequestListener<T> taggedRequestListener) throws CommonRequestException {
    super(method, url, taggedRequestListener);
    checkNull("url", url);
    checkNull("action delivery", volleyActionDelivery);
    this.headers = headers;
    this.params = params;
    this.volleyActionDelivery = volleyActionDelivery;
    this.taggedRequestListener = taggedRequestListener;
  }

  private void checkNull(String paramName, Object param) throws CommonRequestException {
    if (param == null) {
      throw new CommonParamException("param " + paramName + " is null");
    }
  }

  @Override
  protected Response<T> parseNetworkResponse(NetworkResponse response) {
    return volleyActionDelivery.parseNetworkResponse(response);
  }

  @Override
  protected void deliverResponse(T response) {
    taggedRequestListener.onResponse(response);
  }

  @Override
  public void deliverError(VolleyError error) {
    super.deliverError(error);
    taggedRequestListener.onErrorResponse(error);
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
    if (this.params != null) {
      return this.params;
    } else {
      return super.getParams();
    }
  }

  @Override
  public String getUrl() {
    if (getMethod() == Method.GET) {
      return getEncodedGETUrl(super.getUrl(), this.params);
    } else {
      return super.getUrl();
    }
  }

  private String getEncodedGETUrl(String url, Map<String, String> params) {
    if (params == null) {
      return url;
    }
    ArrayList<Map.Entry<String, String>> entryArrayList = new ArrayList<>();
    entryArrayList.addAll(params.entrySet());
    for (int i = 0; i < entryArrayList.size(); i++) {
      Map.Entry<String, String> entry = entryArrayList.get(i);
      String key = entry.getKey();
      String value = entry.getValue();
      try {
        value = URLEncoder.encode(value, "UTF-8");
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        key = URLEncoder.encode(key, "UTF-8");
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (i == 0) {
        url += ("?" + key + "=" + value);
      } else {
        url += ("&" + key + "=" + value);
      }
    }
    return url;
  }

}
