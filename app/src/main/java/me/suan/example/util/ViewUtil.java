package me.suan.example.util;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import me.suan.example.component.SApplication;
import me.suan.example.ui.widget.STextView;

import java.util.List;

/**
 * Created by suanmiao on 14/12/4.
 */
public class ViewUtil {
    private ViewUtil() {

    }

    public static STextView createTagTextView() {
        Context context = SApplication.getAppContext();
        STextView result = new STextView(context);
        result.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                context.getResources().getDimensionPixelSize(me.suan.example.R.dimen.tag_text_size));
        result.setTextColor(context.getResources().getColor(me.suan.example.R.color.tag_text));
        result.setBackgroundResource(me.suan.example.R.drawable.bg_tag);
        int paddingHorizontal =
                context.getResources().getDimensionPixelSize(me.suan.example.R.dimen.tag_bg_padding_horizontal);
        int paddingVertical =
                context.getResources().getDimensionPixelSize(me.suan.example.R.dimen.tag_bg_padding_vertical);
        result.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
        result.setGravity(Gravity.CENTER);
        return result;
    }

    public static STextView createTipTextView(boolean warning) {
        Context context = SApplication.getAppContext();
        STextView result = new STextView(context);
        result.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                context.getResources().getDimensionPixelSize(me.suan.example.R.dimen.tip_text_size));
        result.setTextColor(context.getResources().getColor(me.suan.example.R.color.pure_white));
        result.setBackgroundResource(warning ? me.suan.example.R.drawable.bg_tip_warning : me.suan.example.R.drawable.bg_tip_normal);
        int paddingHorizontal =
                context.getResources().getDimensionPixelSize(me.suan.example.R.dimen.tip_bg_padding_horizontal);
        int paddingVertical =
                context.getResources().getDimensionPixelSize(me.suan.example.R.dimen.tip_bg_padding_vertical);
        result.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
        result.setGravity(Gravity.CENTER);
        return result;
    }

    public static void travelViewTree(View startView) {
        View currentView = startView;
        while (currentView.getParent() != null) {
            Log.e("SUAN", "current view" + currentView + "|" + currentView.getWidth() + "|" + currentView.getMeasuredWidth() + "|" + currentView.getLayoutParams().width);
            if (currentView.getParent() instanceof View) {
                currentView = (View) currentView.getParent();
            } else {
                break;
            }
        }
    }

    public static int getListViewScrollPosition(AbsListView listView) {
        if (listView.getChildCount() > 0) {
            View c = listView.getChildAt(0);
//            int scrolly = -c.getTop() + listView.getFirstVisiblePosition() * c.getHeight();
            int scrolly = -c.getTop();
            return scrolly;
        } else {
            return 0;
        }
    }

    public static void travelAll(View startView, List<View> exceptions, TravelActionListener actionListener) {
        View currentView = startView;
        //find rootView
        while (currentView.getParent() != null) {
            if (currentView.getParent() instanceof View) {
                currentView = (View) currentView.getParent();
            } else {
                break;
            }
        }
        travelChild(currentView, exceptions, actionListener);
    }

    public static void travelChild(View start, List<View> exceptions, TravelActionListener actionListener) {
        boolean exists = false;
        for(View view:exceptions){
            if(view.equals(start)){
                exists = true;
            }
        }
        if(!exists){
            actionListener.act(start);
        }
        if (start instanceof ViewGroup) {
            ViewGroup currentView = (ViewGroup)start;
            for (int i = 0; i < currentView.getChildCount(); i++) {
                travelChild(currentView.getChildAt(i),exceptions,actionListener);
            }
        }
    }

    public interface TravelActionListener {
        public void act(View act);
    }

}
