package com.suan.common.io.http.image.spice;

import android.graphics.Bitmap;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.suan.common.io.http.image.Photo;
import com.suan.common.util.BitmapUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by suanmiao on 14/12/7.
 */
public class PhotoSpiceRequest extends BaseCacheImageRequest<Photo> {

    protected static final String KEY_CONTENT_LENGTH = "Content-Length";

    protected Photo photo;

    protected boolean shouldCache = true;

    protected Photo.LoadOption loadOption = Photo.LoadOption.BOTH;

    public PhotoSpiceRequest(Photo photo) {
        super(Photo.class);
        this.photo = photo;
    }

    public PhotoSpiceRequest(Photo photo, Photo.LoadOption option) {
        super(Photo.class);
        this.photo = photo;
        this.loadOption = option;
    }

    @Override
    public Photo loadDataFromNetwork() throws IOException {
        switch (loadOption) {
            case ONLY_FROM_CACHE: {
                Bitmap cacheContent = getCacheManager().get(photo.getUrl());
                photo.setContent(cacheContent);
            }
            break;
            case ONLY_FROM_NETWORK: {
                Bitmap networkContent = getBitmapFromNetwork();
                if (shouldCache && networkContent != null) {
                    getCacheManager().put(photo.getUrl(), networkContent, true);
                }
                photo.setContent(networkContent);
            }
            break;
            case BOTH: {
                Bitmap content = getCacheManager().get(photo.getUrl());
                if (content == null) {
                    content = getBitmapFromNetwork();
                    if (shouldCache && content != null) {
                        getCacheManager().put(photo.getUrl(), content, true);
                    }
                }
                photo.setContent(content);
            }
            break;
        }
        return photo;
    }


    protected Bitmap getBitmapFromNetwork() throws IOException {
        Request request = new Request.Builder()
                .url(photo.getUrl())
                .build();
        Response response = getOkHttpClient().newCall(request).execute();
        /**
         * get content length
         */
        String contentLength = response.header(KEY_CONTENT_LENGTH, "0");
        photo.setContentLength(Integer.parseInt(contentLength));

        InputStream in = response.body().byteStream();
        return BitmapUtil.decodePhoto(in, photo);
    }

    public boolean isShouldCache() {
        return shouldCache;
    }

    public void setShouldCache(boolean shouldCache) {
        this.shouldCache = shouldCache;
    }

    public Photo.LoadOption getLoadOption() {
        return loadOption;
    }

    public void setLoadOption(Photo.LoadOption loadOption) {
        this.loadOption = loadOption;
    }
}
