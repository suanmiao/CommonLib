package me.suanmiao.example.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import me.suanmiao.common.io.http.robospiece.api.BaseFormResult;
import me.suanmiao.example.ui.widget.TipView;

/**
 * Created by suanmiao on 14/12/24.
 */
public class ResponseStatusUtil {

    private ResponseStatusUtil() {
    }

    public static void processResponseStatus(final Context context, BaseFormResult result,TipView tipView) {
        switch (result.getStatus()) {
            case BaseFormResult.STATUS_NEED_TOKEN:
               break;
            case BaseFormResult.STATUS_INVALID_TOKEN:
               break;
            case BaseFormResult.STATUS_PERMISSION_DENIED:
                Toast.makeText(context, "no permission", Toast.LENGTH_LONG).show();
                break;
            default:
                if(tipView!=null&& !TextUtils.isEmpty(result.getDescription())){
//                    TipUtil.showWarningTip(tipView,result.getDescription());
                }
                break;
        }
    }

}
