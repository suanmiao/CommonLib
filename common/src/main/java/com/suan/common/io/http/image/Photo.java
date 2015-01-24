package com.suan.common.io.http.image;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;
import com.suan.common.component.BaseApplication;
import com.suan.common.io.http.BaseRequest;
import com.suan.common.io.http.MRequestListener;
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

  private PhotoRequest request;

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

  public PhotoRequest newRequest() {
    return new PhotoRequest(this);
  }

  public BlurPhotoRequest newBlurRequest() {
    return new BlurPhotoRequest(this);
  }

  public PhotoRequest getRequest() {
    return request;
  }

  public void setRequest(PhotoRequest request) {
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
    /**
     * load img
     * set photo as tag
     * when scrolling ,cancel previous request ,just load cached bitmap
     * when idle >> load img from network
     *
     * actions to do in this process:
     * 1.set default img
     * 2.load from cache
     * 3.load from network
     *
     * things can be optimized:
     * 1.load img just from cache ,not through cache request
     * 2.do not set default img for cached img
     * 3.(maybe) do nothing when fast scrolling
     *
     * strategy to request:
     * image loading state is record and judged from Photo Object
     * 1.state none || url not equal >>　new request
     *
     * >> 1.scrolling >> load from disk
     * >> 2.idle >> load from network
     */
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
            PhotoRequest request = photo.newRequest();
            request.setLoadOption(PhotoRequest.LoadOption.BOTH);

            photo.setRequest(request);
            photo.setContentState(ContentState.LOADING);
            executeRequest(request, new RequestListener<Photo>() {
              @Override
              public void onRequestFailure(SpiceException spiceException) {
                photo.setContentState(ContentState.NONE);
              }

              @Override
              public void onRequestSuccess(Photo photo) {
                if (photo.getContent() != null) {
                  photo.setContentState(ContentState.DONE);
                  imageView.setImageBitmap(photo.getContent());
                } else {
                  photo.setContentState(ContentState.NONE);
                }
              }
            }, imageView);
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
          PhotoRequest request = photo.newRequest();
          if (saveTraffic()) {
            request.setLoadOption(PhotoRequest.LoadOption.ONLY_FROM_CACHE);
          } else {
            request.setLoadOption(PhotoRequest.LoadOption.BOTH);
          }
          photo.setRequest(request);
          photo.setContentState(ContentState.LOADING);
          executeRequest(request, new RequestListener<Photo>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
              photo.setContentState(ContentState.NONE);
            }

            @Override
            public void onRequestSuccess(Photo photo) {
              if (photo.getContent() != null) {
                photo.setContentState(ContentState.DONE);
                imageView.setImageBitmap(photo.getContent());
              } else {
                photo.setContentState(ContentState.NONE);
              }
            }
          }, imageView);
        }
      }
      imageView.setTag(photo);
    }
  }

  public static void reloadImg(final ImageView imageView) {
    if (imageView != null && imageView.getTag() != null) {
      final Photo photo = (Photo) imageView.getTag();
      if (photo.getLoadingState() == ContentState.NONE) {
        PhotoRequest request = photo.newRequest();
        if (saveTraffic()) {
          request.setLoadOption(PhotoRequest.LoadOption.ONLY_FROM_CACHE);
        } else {
          request.setLoadOption(PhotoRequest.LoadOption.BOTH);
        }
        photo.setRequest(request);
        photo.setContentState(ContentState.LOADING);
        imageView.setTag(photo);
        executeRequest(request, new RequestListener<Photo>() {
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
        }, imageView);
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
        photo.loadFromRamCache(mRequestManager, imageView, url + BlurPhotoRequest.BLUR_SUFFIX);
        if (photo.getLoadingState() == ContentState.NONE) {
          imageView.setImageResource(defaultResourceID);
          BlurPhotoRequest request = photo.newBlurRequest();
          if (saveTraffic()) {
            request.setLoadOption(PhotoRequest.LoadOption.ONLY_FROM_CACHE);
          } else {
            request.setLoadOption(PhotoRequest.LoadOption.BOTH);
          }
          photo.setRequest(request);
          photo.setContentState(ContentState.LOADING);
          executeRequest(request, new RequestListener<Photo>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
              photo.setContentState(ContentState.NONE);
            }

            @Override
            public void onRequestSuccess(Photo photo) {
              if (photo.getContent() != null) {
                photo.setContentState(ContentState.DONE);
                imageView.setImageBitmap(photo.getContent());
              } else {
                photo.setContentState(ContentState.NONE);
              }
            }
          }, imageView);
        }
      }
      imageView.setTag(photo);
    }
  }

  public static void loadScrollItemBlurImg(final ImageView imageView, String url,
      int defaultResourceID, int scrollState, float scrollSpeed) {
    /**
     * load img
     * set photo as tag
     * when scrolling ,cancel previous request ,just load cached bitmap
     * when idle >> load img from network
     *
     * actions to do in this process:
     * 1.set default img
     * 2.load from cache
     * 3.load from network
     *
     * things can be optimized:
     * 1.load img just from cache ,not through cache request
     * 2.do not set default img for cached img
     * 3.(maybe) do nothing when fast scrolling
     *
     * strategy to request:
     * image loading state is record and judged from Photo Object
     * 1.state none || url not equal >>　new request
     *
     * >> 1.scrolling >> load from disk
     * >> 2.idle >> load from network
     */
    if (TextUtils.isEmpty(url)) {
      return;
    }
    url = TextUtil.parseUrl(url);

    final Photo photo = Photo.getObject(imageView, url);
    if (photo != null) {
      if (photo.getLoadingState() == ContentState.NONE) {
        photo.loadFromRamCache(mRequestManager, imageView, url + BlurPhotoRequest.BLUR_SUFFIX);
        if (photo.getLoadingState() == ContentState.NONE) {
          imageView.setImageResource(defaultResourceID);
        }
        if (saveTraffic()) {
          return;
        }
        if (photo.getLoadingState() == ContentState.NONE
            && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
          BlurPhotoRequest request = photo.newBlurRequest();
          request.setLoadOption(PhotoRequest.LoadOption.BOTH);

          photo.setRequest(request);
          photo.setContentState(ContentState.LOADING);
          executeRequest(request, new RequestListener<Photo>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
              photo.setContentState(ContentState.NONE);
            }

            @Override
            public void onRequestSuccess(Photo photo) {
              if (photo.getContent() != null) {
                photo.setContentState(ContentState.DONE);
                imageView.setImageBitmap(photo.getContent());
              } else {
                photo.setContentState(ContentState.NONE);
              }
            }
          }, imageView);

        }


      }
      imageView.setTag(photo);
    }
  }

  public static void reloadBlurImg(final ImageView imageView) {
    if (imageView != null && imageView.getTag() != null) {
      final Photo photo = (Photo) imageView.getTag();
      if (photo.getLoadingState() == ContentState.NONE) {
        PhotoRequest request = photo.newBlurRequest();
        if (saveTraffic()) {
          request.setLoadOption(PhotoRequest.LoadOption.ONLY_FROM_CACHE);
        } else {
          request.setLoadOption(PhotoRequest.LoadOption.BOTH);
        }
        photo.setRequest(request);
        photo.setContentState(ContentState.LOADING);
        imageView.setTag(photo);
        executeRequest(request, new RequestListener<Photo>() {
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
        }, imageView);
      } else {
        if (photo.getRequest() != null) {
          photo.getRequest().cancel();
        }
      }
    }
  }

  public static <T> void executeRequest(SpiceRequest<T> request, RequestListener<T> listener,
      Object tag) {
    mRequestManager.executeRequest(new BaseRequest.SpiceBuilder<T>().request(request).build(),
        new MRequestListener<T>(listener), tag);
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
