package com.suan.example.util;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.suan.example.R;
import com.suan.example.component.SApplication;
import com.suan.example.io.http.HttpExceptionHelper;
import com.suan.example.ui.widget.TipView;

/**
 * Created by suanmiao on 14/12/23.
 */
public class TipUtil {
    private TipUtil() {
    }

    public static void showRefreshTip(TipView tipView, boolean showToast, int size) {
        Context context = SApplication.getAppContext();
        String content = String.format("refresh %d item", size);
        if (showToast) {
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
        }
        View tipContent = tipView.getTipContent();
        if (tipContent != null && tipContent instanceof TextView) {
            TextView tipText = (TextView) tipContent;
            tipText.setBackgroundResource(R.drawable.bg_tip_normal);
            tipText.setText(content);
            tipView.showTip(0);
        } else {
            tipView.removeAllViews();
            TextView tipTextView = ViewUtil.createTipTextView(false);
            tipTextView.setText(content);
            tipView.setTipContent(tipTextView);
            tipView.showTip(0);
        }
    }

    public static void showTip(Context context,TipView tipView,String content,boolean showToast) {
        if (showToast) {
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
        }
        View tipContent = tipView.getTipContent();
        if (tipContent != null && tipContent instanceof TextView) {
            TextView tipText = (TextView) tipContent;
            tipText.setBackgroundResource(R.drawable.bg_tip_normal);
            tipText.setText(content);
            tipView.showTip(0);
        } else {
            tipView.removeAllViews();
            TextView tipTextView = ViewUtil.createTipTextView(false);
            tipTextView.setText(content);
            tipView.setTipContent(tipTextView);
            tipView.showTip(0);
        }
    }

    public static void showLoadToast(int size) {
        Context context = SApplication.getAppContext();
        String content = String.format("load %d item", size);
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static void showWarningTip(TipView tipView, String content) {
        View tipContent = tipView.getTipContent();
        if (tipContent != null && tipContent instanceof TextView) {
            TextView tipText = (TextView) tipContent;
            tipText.setBackgroundResource(R.drawable.bg_tip_warning);
            tipText.setText(content);
            tipView.showTip(0, TipView.TIP_STAY_DURATION_LONG);
        } else {
            tipView.removeAllViews();
            TextView tipTextView = ViewUtil.createTipTextView(true);
            tipTextView.setText(content);
            tipView.setTipContent(tipTextView);
            tipView.showTip(0, TipView.TIP_STAY_DURATION_LONG);
        }
    }

    public static void processException(TipView tipView, SpiceException exception) {
        String content = HttpExceptionHelper.getExceptionDescription(exception);
        showWarningTip(tipView, content);
    }

}



