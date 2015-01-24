package com.suan.example.io.http.image;

import com.octo.android.robospice.request.listener.RequestListener;

/**
 * Created by suanmiao on 14/12/7.
 */
public interface BaseImageRequestListener extends RequestListener{

    public void onProgress(int progress);

}
