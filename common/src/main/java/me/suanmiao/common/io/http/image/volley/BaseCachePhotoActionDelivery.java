package me.suanmiao.common.io.http.image.volley;

import me.suanmiao.common.component.BaseApplication;
import me.suanmiao.common.io.cache.CacheManager;
import me.suanmiao.common.io.http.image.Photo;
import me.suanmiao.common.io.http.volley.IVolleyActionDelivery;

/**
 * Created by suanmiao on 15/1/26.
 */
public abstract class BaseCachePhotoActionDelivery implements IVolleyActionDelivery<Photo>{

    public CacheManager getCacheManager(){
        return BaseApplication.getRequestManager().getCacheManager();
    }
}
