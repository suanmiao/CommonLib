package me.suan.common.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by suanmiao on 14-12-2.
 */
public class SViewPager extends ViewPager {

    public SViewPager(Context context) {
        super(context);
        init();
    }

    public SViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
    }

    public boolean isParentViewPager() {
        View currentView = this;
        while (currentView.getParent() != null) {
            if (currentView.getParent() instanceof ViewPager) {
                return true;
            }
            currentView = (View) currentView.getParent();
        }
        return false;
    }

}
