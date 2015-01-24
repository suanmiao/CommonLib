package com.suan.example.ui.mvc.View;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suan.common.ui.mvc.View.BaseView;
import com.suan.example.R;

/**
 * Created by suanmiao on 14-12-3.
 */
public class MainArticleNormalItemView extends BaseView {

    public TextView titleText, favNum, commentNum, textHeader;
    public LinearLayout tagLayout;
    public ImageView titleImg;

    /**
     * @param context   context to inflate layout
     * @param contanier parent viewGroup for inflated layout
     */
    public MainArticleNormalItemView(Context context, ViewGroup contanier) {
        super(context, contanier);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_main_article_normal;
    }

    @Override
    public void findView() {
        textHeader = findViewById(R.id.text_item_main_article_header, TextView.class);
        titleText = findViewById(R.id.text_item_main_article_title, TextView.class);
        favNum = findViewById(R.id.text_item_main_article_fav_num, TextView.class);
        commentNum = findViewById(R.id.text_item_main_article_comment_num, TextView.class);
        titleImg = findViewById(R.id.img_main_article_normal, ImageView.class);
        tagLayout = findViewById(R.id.layout_item_main_article_normal_tag, LinearLayout.class);
    }
}
