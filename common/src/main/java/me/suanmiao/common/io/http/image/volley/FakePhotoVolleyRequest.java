package me.suanmiao.common.io.http.image.volley;

import java.util.Map;

import me.suanmiao.common.io.http.exception.CommonRequestException;
import me.suanmiao.common.io.http.image.Photo;
import me.suanmiao.common.io.http.robospiece.TaggedRequestListener;
import me.suanmiao.common.io.http.volley.FakeVolleyRequest;
import me.suanmiao.common.io.http.volley.IVolleyActionDelivery;

/**
 * Created by suanmiao on 15/4/29.
 */
public class FakePhotoVolleyRequest<T> extends FakeVolleyRequest<T> {

  private Photo mPhoto;

  public FakePhotoVolleyRequest(int method, String url, Map<String, String> headers,
      Map<String, String> postParams, IVolleyActionDelivery<T> volleyActionDelivery,
      TaggedRequestListener<T> taggedRequestListener, Photo photo)
      throws CommonRequestException {
    super(method, url, headers, postParams, volleyActionDelivery, taggedRequestListener);
    this.mPhoto = photo;
  }

  public Photo getPhoto() {
    return mPhoto;
  }
}
