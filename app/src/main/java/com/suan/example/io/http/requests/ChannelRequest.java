package com.suan.example.io.http.requests;

import com.android.volley.Request;
import com.suan.common.io.http.BaseRequest;
import com.suan.common.io.http.volley.BaseXmlActionDelivery;
import com.suan.example.ui.mvc.Model.ChannelModel;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * Created by suanmiao on 15/1/19.
 */
public class ChannelRequest extends BaseRequest<ChannelModel> {

  public ChannelRequest(String url) {
    super(Request.Method.GET, url,null,null,new BaseXmlActionDelivery<ChannelModel>(ChannelModel.class) {
        @Override
        public Serializer getSerializer() {
            return new Persister();
        }
    });
  }

}
