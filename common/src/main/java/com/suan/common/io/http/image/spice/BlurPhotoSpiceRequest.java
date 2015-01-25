package com.suan.common.io.http.image.spice;

import android.graphics.Bitmap;

import com.suan.common.component.BaseApplication;
import com.suan.common.io.http.image.Photo;
import com.suan.common.ui.blur.Blur;

import java.io.IOException;

/**
 * Created by suanmiao on 14/12/7.
 */
public class BlurPhotoSpiceRequest extends PhotoSpiceRequest {

    public static final String BLUR_SUFFIX = "_blur";

    public BlurPhotoSpiceRequest(Photo photo) {
        super(photo);
    }

    public BlurPhotoSpiceRequest(Photo photo, LoadOption option) {
        super(photo, option);
    }

    @Override
    public Photo loadDataFromNetwork() throws IOException {
        switch (loadOption) {
            case ONLY_FROM_CACHE: {
                Bitmap cacheContent = getCacheManager().get(photo.getUrl() + BLUR_SUFFIX);
                if (cacheContent != null) {
                    photo.setContent(cacheContent);
                } else {
                    cacheContent = getCacheManager().get(photo.getUrl());
                    if (cacheContent != null) {
                        Bitmap blurBitmap = Blur.apply(BaseApplication.getAppContext(), cacheContent);
                        photo.setContent(blurBitmap);
                        getCacheManager().put(photo.getUrl() + BLUR_SUFFIX, blurBitmap, true);
                    }
                }
            }
            break;
            case ONLY_FROM_NETWORK: {
                Bitmap networkContent = getBitmapFromNetwork();
                if (shouldCache && networkContent != null) {
                    Bitmap blurBitmap = Blur.apply(BaseApplication.getAppContext(), networkContent);
                    getCacheManager().put(photo.getUrl(), networkContent, true);
                    getCacheManager().put(photo.getUrl() + BLUR_SUFFIX, blurBitmap, true);
                    photo.setContent(blurBitmap);
                }
            }
            break;
            case BOTH: {
                Bitmap content = getCacheManager().get(photo.getUrl() + BLUR_SUFFIX);
                if (content == null) {
                    content = getCacheManager().get(photo.getUrl());
                    if (content != null) {
                        Bitmap blurBitmap = Blur.apply(BaseApplication.getAppContext(), content);
                        getCacheManager().put(photo.getUrl() + BLUR_SUFFIX, blurBitmap, true);
                        photo.setContent(blurBitmap);
                    } else {
                        Bitmap networkContent = getBitmapFromNetwork();
                        if (shouldCache && networkContent != null) {
                            Bitmap blurBitmap = Blur.apply(BaseApplication.getAppContext(), networkContent);
                            getCacheManager().put(photo.getUrl(), networkContent, true);
                            getCacheManager().put(photo.getUrl() + BLUR_SUFFIX, blurBitmap, true);
                            photo.setContent(blurBitmap);
                        }
                    }
                }
            }
            break;
        }
        return photo;
    }

}
