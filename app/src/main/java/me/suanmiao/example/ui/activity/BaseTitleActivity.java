package me.suanmiao.example.ui.activity;

import android.os.Bundle;

import me.suanmiao.common.ui.activity.BaseActivity;

/**
 * Created by suanmiao on 14-12-1.
 */
public abstract class BaseTitleActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    protected abstract void afterInjected();

    protected abstract int getContentViewId();

    private void setupActionBar(){
//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayUseLogoEnabled(false);
//        actionBar.setDisplayShowCustomEnabled(true);
    }


}
