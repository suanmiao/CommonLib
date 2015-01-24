package com.suan.example.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by suanmiao on 14/12/17.
 */
public class ArticleWebView extends WebView {
    public ArticleWebView(Context context) {
        super(context);
    }

    public ArticleWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArticleWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        requestDisallowInterceptTouchEvent(true);
//        return super.onTouchEvent(event);
//    }


}
