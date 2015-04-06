package me.suanmiao.example.ui.mvvm.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.InjectView;
import me.suanmiao.common.mvvm.view.BaseView;
import me.suanmiao.example.R;

/**
 * Created by suanmiao on 14-12-3.
 */
public class MainArticleNormalItemView extends BaseView {

  @InjectView(R.id.text_item_main_article_title)
  public TextView titleText;
  @InjectView(R.id.text_item_main_article_fav_num)
  public TextView favNum;
  @InjectView(R.id.text_item_main_article_comment_num)
  public TextView commentNum;
  @InjectView(R.id.text_item_main_article_header)
  public TextView textHeader;
  @InjectView(R.id.layout_item_main_article_normal_tag)
  public LinearLayout tagLayout;
  @InjectView(R.id.img_main_article_normal)
  public ImageView titleImg;

  /**
   * @param context context to inflate layout
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
  public void afterInjected() {}
}
