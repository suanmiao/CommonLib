package me.suanmiao.example.io.http.requests;

import com.android.volley.Request;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import me.suanmiao.common.io.http.volley.BaseVolleyRequest;
import me.suanmiao.common.io.http.volley.IVolleyActionDelivery;
import me.suanmiao.example.ui.mvvm.model.ChannelModel;

/**
 * Created by suanmiao on 15/1/19.
 */
public class ChannelRequest extends BaseVolleyRequest<ChannelModel> {

  private String url;

  public ChannelRequest(String url) {
    this.url = url;
  }

  @Override
  public int getRequestMethod() {
    return Request.Method.GET;
  }

  @Override
  public String getUrl() {
    return this.url;
  }

  @Override
  public IVolleyActionDelivery<ChannelModel> getActionDelivery() {
    return new BaseXmlActionDelivery<ChannelModel>(ChannelModel.class) {
      @Override
      public Serializer getSerializer() {
        return new Persister();
      }
    };
  }
}
