package me.suanmiao.common.ui.dialog;


import me.suanmiao.common.ui.dialog.animation.BaseEffect;

/**
 * Created by suanmiao on 14/12/12.
 */
public interface IDialog {

    public void show();

    public void dismiss();

    public void setShowEffect(BaseEffect effect);

    public void setDismissEffect(BaseEffect effect);

    /**
     * @param cancelable whether we can cancel the dialog by pressing back key
     */
    public void setCancelable(boolean cancelable);

    /**
     * @param moveable whether this dialog move when we flip on it
     */
    public void setMoveAble(boolean moveable);
}
