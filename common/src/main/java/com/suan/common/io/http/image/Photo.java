package com.suan.common.io.http.image;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.suan.common.component.BaseApplication;
import com.suan.common.io.http.CommonRequest;
import com.suan.common.io.http.CommonRequestListener;
import com.suan.common.io.http.image.spice.BlurPhotoSpiceRequest;
import com.suan.common.io.http.image.spice.PhotoSpiceRequest;
import com.suan.common.io.http.robospiece.ProgressListener;
import com.suan.common.io.http.robospiece.RequestManager;
import com.suan.common.util.TextUtil;

import java.io.IOException;

/**
 * Created by suanmiao on 14/12/7.
 */
public class Photo {

  public static final int INVALID_VALUE = -1;

  private int viewWidth = INVALID_VALUE;

  private int viewHeight = INVALID_VALUE;

  private String url;

  private Bitmap content;

  /**
   * about progress
   */
  private ProgressListener progressListener;

  private int contentLength;

  private static boolean saveTraffic = false;

  public static enum ContentState {
    DONE,
    LOADING,
    NONE
  }

  private ContentState contentState = ContentState.NONE;

  private CommonRequest request;

  private static RequestManager mRequestManager;

  public Photo(String url, int viewWidth, int viewHeight) {
    this.viewWidth = viewWidth;
    this.viewHeight = viewHeight;
    this.url = url;
    mRequestManager = BaseApplication.getRequestManager();
  }

  public int getViewWidth() {
    return viewWidth;
  }

  public int getViewHeight() {
    return viewHeight;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  public void setContent(Bitmap content) {
    this.content = content;
  }

  public Bitmap getContent() {
    return content;
  }

  public void setProgressListener(ProgressListener progressListener) {
    this.progressListener = progressListener;
  }

  public ProgressListener getProgressListener() {
    return progressListener;
  }

  public void setContentLength(int contentLength) {
    this.contentLength = contentLength;
  }

  public int getContentLength() {
    return contentLength;
  }

  public ContentState getLoadingState() {
    return contentState;
  }

  public void setContentState(ContentState contentState) {
    this.contentState = contentState;
  }

  public PhotoSpiceRequest newSpiceRequest() {
    return new PhotoSpiceRequest(this);
  }


  public BlurPhotoSpiceRequest newBlurSpiceRequest() {
    return new BlurPhotoSpiceRequest(this);
  }

  public CommonRequest getRequest() {
    return request;
  }

  public void setRequest(CommonRequest request) {
    this.request = request;
  }

  /**
   * get Photo object from view or create a new instance
   *
   * @param view the view to load image on
   * @param url target url to request image
   * @return
   */
  public static Photo getObject(ImageView view, String url) {
    /**
     * scheme :
     * when there is a Photo object in imageView's tag,just compare the url ,if not equal ,cancel
     * old and create a new request
     */
    if (view == null) {
      return null;
    }
    url = TextUtil.parseUrl(url);
    if (view.getTag() != null) {
      Photo result = (Photo) view.getTag();
      if (!TextUtils.equals(url, result.getUrl())) {
        if (result.getRequest() != null) {
          result.getRequest().cancel();
        }
        result.setContentState(ContentState.NONE);
        result.setUrl(url);
      }
      return result;
    } else {
      int width = 0, height = 0;
      if (view.getLayoutParams() != null) {
        width = view.getLayoutParams().width > 0 ? view.getLayoutParams().width : 0;
        height = view.getLayoutParams().height > 0 ? view.getLayoutParams().height : 0;
      }
      return new Photo(url, width, height);
    }
  }

  public static void loadScrollItemImg(final ImageView imageView, String url,
      int defaultResourceID, int scrollState, float scrollSpeed) {
    if (TextUtils.isEmpty(url)) {
      return;
    }
    url = TextUtil.parseUrl(url);

    final Photo photo = Photo.getObject(imageView, url);
    if (photo != null) {
      if (photo.getLoadingState() == ContentState.NONE) {
        photo.loadFromRamCache(mRequestManager, imageView, url);
        if (saveTraffic()) {
          return;
        }
        // no cache got
        if (photo.getLoadingState() == ContentState.NONE) {
          imageView.setImageResource(defaultResourceID);
          if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            photo.setContentState(ContentState.LOADING);
            switch (RequestManager.getExecuteMode()) {
              case ROBO_SPIECE:
                PhotoSpiceRequest spiceRequest = photo.newSpiceRequest();
                spiceRequest.setLoadOption(PhotoSpiceRequest.LoadOption.BOTH);
                CommonRequest request1 =
                    new CommonRequest.SpiceBuilder<Photo>().request(spiceRequest)
                        .build();
                photo.setRequest(request1);
                executeFullRequest(request1, photo, imageView);

                break;

              case VOLLEY:

                break;
            }
          }
        }
      }
      imageView.setTag(photo);
    }
  }

  public static void loadImg(final ImageView imageView, String url, int defaultResourceID) {
    final Photo photo = Photo.getObject(imageView, url);
    if (photo != null) {
      if (photo.getLoadingState() == ContentState.NONE) {
        photo.loadFromRamCache(mRequestManager, imageView, url);
        if (photo.getLoadingState() == ContentState.NONE) {
          imageView.setImageResource(defaultResourceID);
          photo.setContentState(ContentState.LOADING);

          switch (RequestManager.getExecuteMode()) {
            case ROBO_SPIECE:
              PhotoSpiceRequest spiceRequest = photo.newSpiceRequest();
              if (saveTraffic) {
                spiceRequest.setLoadOption(PhotoSpiceRequest.LoadOption.ONLY_FROM_CACHE);
              } else {
                spiceRequest.setLoadOption(PhotoSpiceRequest.LoadOption.BOTH);
              }
              CommonRequest request1 =
                  new CommonRequest.SpiceBuilder<Photo>().request(spiceRequest)
                      .build();
              photo.setRequest(request1);
              executeFullRequest(request1, photo, imageView);

              break;

            case VOLLEY:

              break;
          }
        }
      }
      imageView.setTag(photo);
    }
  }

  public static void reloadImg(final ImageView imageView) {
    if (imageView != null && imageView.getTag() != null) {
      final Photo photo = (Photo) imageView.getTag();
      if (photo.getLoadingState() == ContentState.NONE) {
        photo.setContentState(ContentState.LOADING);
        switch (RequestManager.getExecuteMode()) {
          case ROBO_SPIECE:
            PhotoSpiceRequest spiceRequest = photo.newSpiceRequest();
            if (saveTraffic) {
              spiceRequest.setLoadOption(PhotoSpiceRequest.LoadOption.ONLY_FROM_CACHE);
            } else {
              spiceRequest.setLoadOption(PhotoSpiceRequest.LoadOption.BOTH);
            }
            CommonRequest request1 =
                new CommonRequest.SpiceBuilder<Photo>().request(spiceRequest)
                    .build();
            photo.setRequest(request1);
            executeFullRequest(request1, photo, imageView);
            break;

          case VOLLEY:

            break;
        }
        imageView.setTag(photo);
      } else {
        if (photo.getRequest() != null) {
          photo.getRequest().cancel();
        }
      }
    }
  }

  public static void loadBlurImg(final ImageView imageView, String url, int defaultResourceID) {
    final Photo photo = Photo.getObject(imageView, url);
    if (photo != null) {
      if (photo.getLoadingState() == ContentState.NONE) {
        photo.loadFromRamCache(mRequestManager, imageView, url + BlurPhotoSpiceRequest.BLUR_SUFFIX);
        if (photo.getLoadingState() == ContentState.NONE) {
          imageView.setImageResource(defaultResourceID);
          photo.setContentState(ContentState.LOADING);
          switch (RequestManager.getExecuteMode()) {
            case ROBO_SPIECE:
              PhotoSpiceRequest spiceRequest = photo.newBlurSpiceRequest();
              if (saveTraffic) {
                spiceRequest.setLoadOption(PhotoSpiceRequest.LoadOption.ONLY_FROM_CACHE);
              } else {
                spiceRequest.setLoadOption(PhotoSpiceRequest.LoadOption.BOTH);
              }
              CommonRequest request1 =
                  new CommonRequest.SpiceBuilder<Photo>().request(spiceRequest)
                      .build();
              photo.setRequest(request1);
              executeFullRequest(request1, photo, imageView);
              break;

            case VOLLEY:

              break;
          }
        }
      }
      imageView.setTag(photo);
    }
  }

  public static void loadScrollItemBlurImg(final ImageView imageView, String url,
      int defaultResourceID, int scrollState, float scrollSpeed) {
    if (TextUtils.isEmpty(url)) {
      return;
    }
    url = TextUtil.parseUrl(url);

    final Photo photo = Photo.getObject(imageView, url);
    if (photo != null) {
      if (photo.getLoadingState() == ContentState.NONE) {
        photo.loadFromRamCache(mRequestManager, imageView, url + BlurPhotoSpiceRequest.BLUR_SUFFIX);
        if (photo.getLoadingState() == ContentState.NONE) {
          imageView.setImageResource(defaultResourceID);
        }
        if (saveTraffic()) {
          return;
        }
        if (photo.getLoadingState() == ContentState.NONE
            && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
          photo.setContentState(ContentState.LOADING);
          switch (RequestManager.getExecuteMode()) {
            case ROBO_SPIECE:
              PhotoSpiceRequest spiceRequest = photo.newBlurSpiceRequest();
              if (saveTraffic) {
                spiceRequest.setLoadOption(PhotoSpiceRequest.LoadOption.ONLY_FROM_CACHE);
              } else {
                spiceRequest.setLoadOption(PhotoSpiceRequest.LoadOption.BOTH);
              }
              CommonRequest request1 =
                  new CommonRequest.SpiceBuilder<Photo>().request(spiceRequest)
                      .build();
              photo.setRequest(request1);
              executeFullRequest(request1, photo, imageView);
              break;

            case VOLLEY:

              break;
          }
        }
      }
      imageView.setTag(photo);
    }
  }

  public static void reloadBlurImg(final ImageView imageView) {
    if (imageView != null && imageView.getTag() != null) {
      final Photo photo = (Photo) imageView.getTag();
      if (photo.getLoadingState() == ContentState.NONE) {
        photo.setContentState(ContentState.LOADING);
        switch (RequestManager.getExecuteMode()) {
          case ROBO_SPIECE:
            PhotoSpiceRequest spiceRequest = photo.newBlurSpiceRequest();
            if (saveTraffic) {
              spiceRequest.setLoadOption(PhotoSpiceRequest.LoadOption.ONLY_FROM_CACHE);
            } else {
              spiceRequest.setLoadOption(PhotoSpiceRequest.LoadOption.BOTH);
            }
            CommonRequest request1 =
                new CommonRequest.SpiceBuilder<Photo>().request(spiceRequest)
                    .build();
            photo.setRequest(request1);
            executeFullRequest(request1, photo, imageView);
            break;

          case VOLLEY:

            break;
        }
        imageView.setTag(photo);
      } else {
        if (photo.getRequest() != null) {
          photo.getRequest().cancel();
        }
      }
    }
  }

  public static void executeFullRequest(CommonRequest request, final Photo photo,
      final ImageView imageView) {
    switch (RequestManager.getExecuteMode()) {
      case ROBO_SPIECE:
        mRequestManager.executeRequest(request, new CommonRequestListener<Photo>(
            new RequestListener<Photo>() {
              @Override
              public void onRequestFailure(SpiceException spiceException) {
                photo.setContentState(ContentState.NONE);
              }

              @Override
              public void onRequestSuccess(Photo photo) {
                if (photo.getContent() != null) {
                  photo.setContentState(ContentState.DONE);
                  imageView.setImageBitmap(photo.getContent());
                }
              }
            }), imageView);

        break;
      case VOLLEY:
        mRequestManager.executeRequest(request, new CommonRequestListener(
            new CommonRequestListener.VolleyListener<Photo>() {
              @Override
              public void onErrorResponse(VolleyError error) {
                photo.setContentState(ContentState.NONE);
              }

              @Override
              public void onResponse(Photo photo) {
                if (photo.getContent() != null) {
                  photo.setContentState(ContentState.DONE);
                  imageView.setImageBitmap(photo.getContent());
                }
              }
            }), imageView);

        break;
    }
  }

  public void loadFromRamCache(RequestManager requestManager, ImageView imageView, String url) {
    if (TextUtils.isEmpty(url)) {
      return;
    }
    url = TextUtil.parseUrl(url);
    this.contentState = ContentState.NONE;
    try {
      Bitmap result = requestManager.getCacheManager().get(url);
      if (result != null) {
        imageView.setImageBitmap(result);
        this.contentState = ContentState.DONE;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static boolean saveTraffic() {
    return saveTraffic;
  }

  public static void setSaveTraffic(boolean saveTraffic) {
    Photo.saveTraffic = saveTraffic;
  }

}
