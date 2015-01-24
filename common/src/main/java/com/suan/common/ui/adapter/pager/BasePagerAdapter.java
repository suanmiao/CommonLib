package com.suan.common.ui.adapter.pager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suan.common.component.BaseApplication;
import com.suan.common.io.http.robospiece.RequestManager;
import com.suan.common.ui.mvc.Model.BaseModel;

import java.util.List;

/**
 * Created by suanmiao on 14/12/4.
 */
public abstract class BasePagerAdapter<T extends BaseModel> extends PagerAdapter {

    protected List<T> dataList;
    protected LayoutInflater inflater;
    protected RequestManager requestManager;

    public BasePagerAdapter() {
        inflater =
                (LayoutInflater) BaseApplication.getAppContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
        requestManager = BaseApplication.getRequestManager();
    }

    public void setData(List<T> dataList) {
        this.dataList = dataList;
    }

    public List<T> getData() {
        return dataList;
    }

    public T getItem(int i) {
        return dataList == null ? null : dataList.get(i);
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public abstract Object instantiateItem(ViewGroup container, int position);

    @Override
    public abstract void destroyItem(ViewGroup container, int position, Object object);

    @Override
    public abstract boolean isViewFromObject(View view, Object o);

}
