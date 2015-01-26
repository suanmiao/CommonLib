package com.suan.common.io.http.image.volley;

import com.suan.common.component.BaseApplication;
import com.suan.common.io.cache.CacheManager;
import com.suan.common.io.http.image.Photo;
import com.suan.common.io.http.volley.IVolleyActionDelivery;

/**
 * Created by suanmiao on 15/1/26.
 */
public abstract class BaseCachePhotoActionDelivery implements IVolleyActionDelivery<Photo>{

    public CacheManager getCacheManager(){
        return BaseApplication.getRequestManager().getCacheManager();
    }
}
