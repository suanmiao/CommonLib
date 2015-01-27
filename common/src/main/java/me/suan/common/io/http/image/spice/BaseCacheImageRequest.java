package me.suan.common.io.http.image.spice;

import me.suan.common.component.BaseApplication;
import me.suan.common.io.cache.CacheManager;
import me.suan.common.io.http.robospiece.request.BaseOkhttpRequest;

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
