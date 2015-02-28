package me.suanmiao.example.io.http.requests;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.simpleframework.xml.Serializer;

import me.suanmiao.common.io.http.volley.IVolleyActionDelivery;

/**
 * Created by suanmiao on 15/1/17.
 */
public abstract class BaseXmlActionDelivery<T> implements IVolleyActionDelivery<T> {
  private Class<T> mClazz;

  public BaseXmlActionDelivery(Class<T> clazz) {
    this.mClazz = clazz;
  }

  @Override
  public Response<T> parseNetworkResponse(NetworkResponse response) {
    try {
      String data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
      return Response.success(getSerializer().read(mClazz, data),
          HttpHeaderParser.parseCacheHeaders(response));
    } catch (Exception e) {
      return Response.error(new ParseError());
    }
  }

  public abstract Serializer getSerializer();

}
