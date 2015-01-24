package com.suan.common.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.suan.common.component.BaseApplication;
import com.suan.common.io.http.BaseRequest;
import com.suan.common.io.http.MRequestListener;
import com.suan.common.io.http.robospiece.RequestManager;

import butterknife.ButterKnife;

/**
 * Created by suanmiao on 14-10-31.
 * add record on lifecycle of that activity
 */
public abstract class BaseActivity extends ActionBarActivity {

    public RequestManager mRequestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        mRequestManager = BaseApplication.getRequestManager();
        ButterKnife.inject(this);
        afterInjected();
   }

    protected abstract void afterInjected();

    protected abstract int getContentViewId();


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRequestManager.cancelRequest(this);
    }

    protected void executeRequest(BaseRequest request, MRequestListener requestListener) {
        mRequestManager.executeRequest(request, requestListener, this);
    }

}
