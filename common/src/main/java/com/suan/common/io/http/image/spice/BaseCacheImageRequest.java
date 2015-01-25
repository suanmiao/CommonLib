package com.suan.common.io.http.image.spice;

import com.suan.common.component.BaseApplication;
import com.suan.common.io.cache.CacheManager;
import com.suan.common.io.http.robospiece.request.BaseOkhttpRequest;

/**
 * Created by suanmiao on 14/12/8.
 */
public abstract class BaseCacheImageRequest<T> extends BaseOkhttpRequest<T> {

    public BaseCacheImageRequest(Class<T> clazz) {
        super(clazz);
    }

    public CacheManager getCacheManager(){
        return BaseApplication.getRequestManager().getCacheManager();
    }

}
