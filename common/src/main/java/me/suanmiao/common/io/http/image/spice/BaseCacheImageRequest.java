package me.suanmiao.common.io.http.image.spice;

import me.suanmiao.common.component.BaseApplication;
import me.suanmiao.common.io.cache.CacheManager;
import me.suanmiao.common.io.http.image.ICommonRequest;
import me.suanmiao.common.io.http.robospiece.request.BaseOkhttpRequest;

/**
 * Created by suanmiao on 14/12/8.
 */
public abstract class BaseCacheImageRequest<T> extends BaseOkhttpRequest<T>{

    public BaseCacheImageRequest(Class<T> clazz) {
        super(clazz);
    }

    public CacheManager getCacheManager(){
        return BaseApplication.getRequestManager().getCacheManager();
    }

}
