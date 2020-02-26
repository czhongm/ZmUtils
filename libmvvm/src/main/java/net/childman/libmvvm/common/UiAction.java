package net.childman.libmvvm.common;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.lifecycle.LifecycleOwner;
import net.childman.libmvvm.R;
import net.childman.libmvvm.dialog.LoadingDialog;
import net.childman.libmvvm.viewmodel.BaseViewModel;
import net.childman.libmvvm.widget.Toasty;

public class UiAction {
    private Context mContext;
    private Toast mToast;
    private LoadingDialog mLoadingDialog;
    private boolean mUseIcon;
    private int mToastLayoutRes;
    private int mLoadingLayoutRes;

    protected OnViewClickListener mOnViewClickListener;

    public OnViewClickListener getOnViewClickListener() {
        return mOnViewClickListener;
    }

    public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
        mOnViewClickListener = onViewClickListener;
    }

    public UiAction(Context context) {
        mContext = context;
        mToastLayoutRes = R.layout.toast;
        mLoadingLayoutRes = R.layout.dialog_loading;
    }

    private String getString(@StringRes int resId){
        return mContext.getString(resId);
    }

    public void setUseIcon(boolean useIcon) {
        mUseIcon = useIcon;
    }

    public void setToastLayoutRes(int toastLayoutRes) {
        mToastLayoutRes = toastLayoutRes;
        mToast = null;
    }

    public void setLoadingLayoutRes(int loadingLayoutRes) {
        mLoadingLayoutRes = loadingLayoutRes;
        mLoadingDialog = null;
    }

    public void showToast(int resId){
        showToast(getString(resId));
    }

    public void showToast(String msg){
        if(mUseIcon) {
            showToast(msg, R.drawable.succ);
        }else{
            showToast(msg,0);
        }
    }

    public void showToast(String msg, @DrawableRes int resId){
        if(TextUtils.isEmpty(msg)) return;
        if(mToast == null){
            mToast = Toasty.custom(mContext,mToastLayoutRes,msg,resId,Toast.LENGTH_SHORT);
            if(mToast!=null) mToast.show();
        }else{
            Toasty.showCustom(mContext,mToast,msg,resId,Toast.LENGTH_SHORT);
        }
    }

    public void showError(@StringRes int resId){
        showError(getString(resId));
    }

    public void showError(String msg){
        showToast(msg,R.drawable.error);
    }

    public void clearError(){
        if(mToast != null){
            mToast.cancel();
        }
    }

    public void showWarn(@StringRes int resId){
        showWarn(getString(resId));
    }

    public void showWarn(String msg){
        showToast(msg,R.drawable.warn);
    }

    public void showLoading(@StringRes int msgId){
        showLoading(getString(msgId));
    }

    public void showLoading(String msg){
        if(mLoadingDialog == null){
            mLoadingDialog = new LoadingDialog.Builder(mContext)
                    .setLayout(mLoadingLayoutRes)
                    .create();
        }
        mLoadingDialog.setMessage(msg);
        mLoadingDialog.show();
    }

    /**
     * 隐藏载入中对话框
     */
    public void hideLoading(){
        if(mLoadingDialog != null){
            mLoadingDialog.dismiss();
        }
    }

    public void listenEvent(BaseViewModel viewModel, LifecycleOwner owner) {
    }

    public interface OnViewClickListener{
        void onViewClick(int id);
    }
}
