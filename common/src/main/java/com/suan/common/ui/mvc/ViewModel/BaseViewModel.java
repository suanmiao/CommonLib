package com.suan.common.ui.mvc.ViewModel;

import android.content.Context;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.suan.common.component.BaseApplication;
import com.suan.common.io.http.BaseRequest;
import com.suan.common.io.http.MRequestListener;
import com.suan.common.io.http.robospiece.RequestManager;
import com.suan.common.io.http.robospiece.api.BaseFormResult;
import com.suan.common.ui.mvc.Model.BaseModel;
import com.suan.common.ui.mvc.View.BaseView;

/**
 * Created by suanmiao on 14-12-3.
 */
public abstract class BaseViewModel {

    private static final String TAG = "SUANVIEWMMODEL";

    protected RequestManager mRequestManager;

    protected BaseView baseView;

    protected Context mContext;
    private UIChangeListener uiChangeListener;

    public BaseViewModel(BaseView view, Context context, UIChangeListener uiChangeListener) {
        this.baseView = view;
        this.mRequestManager = BaseApplication.getRequestManager();
        this.mContext = context;
        this.uiChangeListener = uiChangeListener;
    }

    public void notifyUIChange() {
        if (this.uiChangeListener != null) {
            uiChangeListener.notifyUIChange();
        }
    }

    public void notifyException(SpiceException exception) {
        if (this.uiChangeListener != null) {
            uiChangeListener.notifyException(exception);
        }
    }

    public void notifyStatus(BaseFormResult result) {
        if (this.uiChangeListener != null) {
            uiChangeListener.notifyStatus(result);
        }
    }

    public abstract void bind(int index, BaseModel baseModel, int scrollState, float scrollSpeed);

    public abstract void idleReload();

    public BaseView getView() {
        return baseView;
    }

    public void executeRequest(BaseRequest request, MRequestListener listener) {
        mRequestManager.executeRequest(request, listener, this);
    }

    public void cancelRequest() {
        mRequestManager.cancelRequest(this);
    }

    public interface UIChangeListener {
        public void notifyUIChange();
        public void notifyException(SpiceException exception);
        public void notifyStatus(BaseFormResult baseFormResult);
    }
}
