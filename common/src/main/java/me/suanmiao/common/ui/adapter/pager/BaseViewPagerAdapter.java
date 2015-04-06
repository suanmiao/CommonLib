package me.suanmiao.common.ui.adapter.pager;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import me.suanmiao.common.mvvm.model.BaseModel;
import me.suanmiao.common.mvvm.viewmodel.BaseViewModel;

/**
 * Created by suanmiao on 14/12/4.
 */
public abstract class BaseViewPagerAdapter<T extends BaseModel> extends BasePagerAdapter {

    private SparseArray<View> contentArray;

    public BaseViewPagerAdapter() {
        this.contentArray = new SparseArray<>();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BaseViewModel baseViewModel;
        View contentView = contentArray.get(position);
        if (contentView == null) {
            baseViewModel = newViewModel(position, container);
            contentView = baseViewModel.getItemView().getContentView();
            contentView.setTag(baseViewModel);
        } else {
            baseViewModel = (BaseViewModel) contentView.getTag();
        }
        baseViewModel.bind(position,getItem(position), 0, 0);
        container.removeView(contentView);
        container.addView(contentView);
       return contentView;
    }

    public abstract BaseViewModel newViewModel(int i, ViewGroup container);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == ((View) o);
    }
}
