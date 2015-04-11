package me.suanmiao.common.ui.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import java.util.List;

import me.suanmiao.common.component.BaseApplication;
import me.suanmiao.common.io.http.RequestManager;
import me.suanmiao.common.mvvm.UICallback;
import me.suanmiao.common.mvvm.model.BaseModel;
import me.suanmiao.common.mvvm.viewmodel.BaseViewModel;

/**
 * Created by suanmiao on 14-12-3.
 */
public abstract class BaseListAdapter<T extends BaseModel> extends BaseAdapter
    implements
    AbsListView.OnScrollListener {
  private UICallback mUICallback;
  protected RequestManager requestManager;
  private Context mContext;

  private int currentScrollState = SCROLL_STATE_IDLE;

  private ListParser parser;

  public BaseListAdapter(Context context) {
    this.requestManager = BaseApplication.getRequestManager();
    this.mContext = context;
  }

  public void setUICallback(UICallback mUICallback) {
    this.mUICallback = mUICallback;
  }

  public UICallback getUICallback() {
    return mUICallback;
  }

  public Context getContext() {
    return mContext;
  }

  protected List<T> dataList;

  public void setData(List<T> content) {
    this.dataList = content;
    if (parser != null) {
      this.dataList = parser.parseData(this.dataList);
    }
    notifyDataSetChanged();
  }

  public void addData(List<T> content) {
    if (this.dataList != null) {
      this.dataList.addAll(content);
    } else {
      this.dataList = content;
    }
    if (parser != null) {
      this.dataList = parser.parseData(this.dataList);
    }
    notifyDataSetChanged();
  }

  public void insertData(int index, T t) {
    dataList.add(index, t);
    notifyDataSetChanged();
  }

  public void removeData(int index) {
    dataList.remove(index);
    notifyDataSetChanged();
  }

  public void parseList() {
    if (parser != null) {
      this.dataList = parser.parseData(this.dataList);
    }
  }

  public void setParser(ListParser parser) {
    this.parser = parser;
  }

  public List<T> getData() {
    return dataList;
  }

  @Override
  public int getCount() {
    return dataList == null ? 0 : dataList.size();
  }

  @Override
  public T getItem(int i) {
    return dataList == null ? null : dataList.get(i);
  }

  @Override
  public long getItemId(int i) {
    return 0;
  }

  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    BaseViewModel baseViewModel;
    if (view == null) {
      baseViewModel = newViewModel(i, viewGroup);
      view = baseViewModel.getItemView().getContentView();
      view.setTag(baseViewModel);
    } else {
      baseViewModel = (BaseViewModel) view.getTag();
    }
    baseViewModel.bind(i, getItem(i), currentScrollState, getSpeed());
    return view;
  }

  public abstract BaseViewModel newViewModel(int i, ViewGroup container);

  @Override
  public abstract int getViewTypeCount();

  @Override
  public int getItemViewType(int position) {
    return dataList == null ? 0 : dataList.get(position).getViewType();
  }

  private static final int POSITION_INVALID = -1;

  private int lastVisibleItem = POSITION_INVALID;

  public static final float SPEED_INVALID = -1;

  protected float speed = SPEED_INVALID;

  private long lastTime = -1;

  @Override
  public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
      int totalItemCount) {
    if (lastVisibleItem != POSITION_INVALID) {
      float deltaTime = (System.currentTimeMillis() - lastTime);
      speed = (firstVisibleItem - lastVisibleItem) / deltaTime;
    }
    lastVisibleItem = firstVisibleItem;
    lastTime = System.currentTimeMillis();
  }

  public float getSpeed() {
    return speed;
  }

  @Override
  public void onScrollStateChanged(AbsListView view, int scrollState) {
    currentScrollState = scrollState;
    if (scrollState == SCROLL_STATE_IDLE) {
      for (int i = 0; i < view.getChildCount(); i++) {
        View child = view.getChildAt(i);
        if (child.getTag() != null) {
          ((BaseViewModel) child.getTag()).idleReload();
        }
      }
    }
  }

  public interface ListParser<T> {

    public List<T> parseData(List<T> dataList);

  }

}
