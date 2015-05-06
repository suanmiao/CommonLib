package me.suanmiao.common.io.http.volley;

import com.android.volley.Request;

import me.suanmiao.common.io.http.image.Photo;
import me.suanmiao.common.io.http.image.volley.PhotoActionDelivery;

/**
 * Created by suanmiao on 15/5/1.
 */
public class PhotoVolleyRequest extends BaseVolleyRequest<Photo> {

  private Photo mPhoto;
  private PhotoActionDelivery mPhotoActionDelivery;

  public PhotoVolleyRequest(Photo photo) {
    this.mPhoto = photo;
    mPhotoActionDelivery = new PhotoActionDelivery(photo);
  }

  public Photo getPhoto() {
    return mPhoto;
  }

  @Override
  public int getRequestMethod() {
    return Request.Method.GET;
  }

  @Override
  public String getUrl() {
    return mPhoto.getUrl();
  }

  @Override
  public IVolleyActionDelivery<Photo> getActionDelivery() {
    return mPhotoActionDelivery;
  }

}
