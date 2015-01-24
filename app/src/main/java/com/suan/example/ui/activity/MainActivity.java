package com.suan.example.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.suan.example.R;
import com.suan.example.ui.adapter.pager.FragmentPagerAdapter;
import com.suan.example.ui.fragment.ArticleListFragment;
import com.suan.example.ui.widget.SViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;


public class MainActivity extends BaseToolbarActivity {

    public static final int INDEX_ARTICLE_LIST = 0;
    public static final int INDEX_SQUARE = 1;
    public static final int INDEX_ME = 2;
    @InjectView(R.id.ic_main_actionbar_article)
    ImageView icMainActionbarArticle;
    @InjectView(R.id.ic_main_actionbar_square)
    ImageView icMainActionbarSquare;
    @InjectView(R.id.ic_main_actionbar_me)
    ImageView icMainActionbarMe;
    @InjectView(R.id.toolbar_base)
    Toolbar toolbarMain;
    @InjectView(R.id.viewpager_main)
    SViewPager viewpagerMain;

    @Override
    protected void afterInjected() {
        initWidgets();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    private void initWidgets() {
        setupActionBar();
        icMainActionbarArticle.setSelected(true);

        List<Fragment> contentFragmentList = new ArrayList<Fragment>();
        contentFragmentList.add(new ArticleListFragment());
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), contentFragmentList);
        viewpagerMain.setAdapter(fragmentPagerAdapter);
        viewpagerMain.setOffscreenPageLimit(3);

        viewpagerMain.setOnPageChangeListener(pageChangeListener);

        icMainActionbarArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem(INDEX_ARTICLE_LIST);
            }
        });
        icMainActionbarSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem(INDEX_SQUARE);
            }
        });
        icMainActionbarMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem(INDEX_ME);
            }
        });
    }

    private void selectItem(int index) {
        switch (index) {
            case INDEX_ARTICLE_LIST:
                icMainActionbarArticle.setSelected(true);
                icMainActionbarSquare.setSelected(false);
                icMainActionbarMe.setSelected(false);
                break;
            case INDEX_SQUARE:
                icMainActionbarArticle.setSelected(false);
                icMainActionbarSquare.setSelected(true);
                icMainActionbarMe.setSelected(false);
                break;
            case INDEX_ME:
                icMainActionbarArticle.setSelected(false);
                icMainActionbarSquare.setSelected(false);
                icMainActionbarMe.setSelected(true);
                break;
        }
        if (viewpagerMain.getCurrentItem() != index) {
            viewpagerMain.setCurrentItem(index);
        }
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i2) {
        }

        @Override
        public void onPageSelected(int i) {
            selectItem(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    };

    private void setupActionBar() {
        setSupportActionBar(toolbarMain);
    }

}
