package com.suan.example.ui.adapter.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by suanmiao on 14-12-2.
 */
public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

  private List<Fragment> contentFragmentList;

  public FragmentPagerAdapter(FragmentManager fm, List<Fragment> contentFragmentList) {
    super(fm);
    this.contentFragmentList = contentFragmentList;
  }

  @Override
  public Fragment getItem(int i) {
    return contentFragmentList.get(i);
  }

  @Override
  public int getCount() {
    return contentFragmentList==null?0:contentFragmentList.size();
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    return super.instantiateItem(container, position);
  }

}
