package me.suan.common.ui.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import me.suan.common.R;
import me.suan.common.util.helper.SystemHelper;


/**
 * Created by suanmiao on 14-12-1.
 */
public abstract class BaseToolbarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            initTranslucent();
        }
    }

    protected abstract void afterInjected();

    protected abstract int getContentViewId();

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initTranslucent() {
        ViewGroup contentView =
                (ViewGroup) findViewById(android.R.id.content);

        contentView.setPadding(contentView.getPaddingLeft(),
                contentView.getPaddingTop() + SystemHelper.getStatusBarHeight(),
                contentView.getPaddingRight(), contentView.getPaddingBottom());

        View bgView = new View(BaseToolbarActivity.this);
        bgView.setBackgroundColor(getResources().getColor(R.color.gray_d8_20));
        contentView.addView(bgView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                SystemHelper.getStatusBarHeight()));
    }

}
