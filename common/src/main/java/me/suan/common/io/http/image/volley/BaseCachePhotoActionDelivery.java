package me.suan.common.io.http.image.volley;

import me.suan.common.component.BaseApplication;
import me.suan.common.io.cache.CacheManager;
import me.suan.common.io.http.image.Photo;
import me.suan.common.io.http.volley.IVolleyActionDelivery;

/**
 * Created by suanmiao on 15/1/26.
 */
public abstract class BaseCachePhotoActionDelivery implements IVolleyActionDelivery<Photo>{

    public CacheManager getCacheManager(){
        return BaseApplication.getRequestManager().getCacheManager();
    }
}
