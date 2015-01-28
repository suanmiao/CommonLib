package me.suanmiao.common.io.http;

import com.octo.android.robospice.request.listener.RequestListener;
import me.suanmiao.common.io.http.volley.IVolleyListener;

/**
 * Created by suanmiao on 15/1/26.
 */
public abstract class VolleyCommonListener<T> extends CommonRequestListener<T> implements IVolleyListener<T> {
    @Override
    public RequestListener<T> getRoboRequestListener() {
        return null;
    }

    @Override
    public IVolleyListener<T> getVolleyListener() {
        return this;
    }

    @Override
    public ListenerType getListenerType() {
        return ListenerType.VOLLEY_LISTENER;
    }
}
