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

    public UiAction(Context context) {
        mContext = context;
    }

    private String getString(@StringRes int resId){
        return mContext.getString(resId);
    }

    public void showToast(int resId){
        showToast(getString(resId));
    }

    public void showToast(String msg){
        showToast(msg,R.drawable.succ);
    }

    public void showToast(String msg, @DrawableRes int resId){
        if(TextUtils.isEmpty(msg)) return;
        if(mToast == null){
            mToast = Toasty.custom(mContext,msg,resId,Toast.LENGTH_SHORT);
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
        if(mLoadingDialog == null){
            mLoadingDialog = new LoadingDialog.Builder(mContext)
                    .create();
        }
        mLoadingDialog.setMessage(msgId);
        mLoadingDialog.show();
    }

    public void showLoading(String msg){
        if(mLoadingDialog == null){
            mLoadingDialog = new LoadingDialog.Builder(mContext)
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
}
