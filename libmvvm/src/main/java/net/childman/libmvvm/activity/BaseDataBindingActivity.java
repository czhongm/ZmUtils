package net.childman.libmvvm.activity;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import net.childman.libmvvm.BR;
import net.childman.libmvvm.R;
import net.childman.libmvvm.common.DataBindingUiAction;
import net.childman.libmvvm.dialog.ConfirmDialog;
import net.childman.libmvvm.viewmodel.BaseViewModel;

public abstract class BaseDataBindingActivity<T extends BaseViewModel,E extends ViewDataBinding> extends BaseActivity {
    protected T mViewModel;
    protected E mDataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(getViewModelClass());
        mDataBinding = DataBindingUtil.setContentView(this,getLayoutRes());
        mDataBinding.setVariable(BR.viewModel,mViewModel);
        mDataBinding.setLifecycleOwner(this);

        initData();
        initView();
        listenEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBinding.unbind();
        mDataBinding = null;
        mViewModel = null;
    }

    @Override
    protected void initUiAction() {
        mUiAction = new DataBindingUiAction(this);
    }

    /**
     * 获取布局Id
     * @return layoutRes
     */
    protected abstract @LayoutRes int getLayoutRes();

    /**
     * 获取ViewModel class
     * @return viewmodel class
     */
    protected abstract Class<? extends T> getViewModelClass();

    protected <K> K getViewModel(Class<K> tClass){
        if(tClass.isInstance(mViewModel)){
            return (K)mViewModel;
        }
        return null;
    }

    protected void initData() {

    }

    protected void initView() {
    }

    /**
     * 监听事件
     */
    protected void listenEvent(){
        if(mViewModel == null) return;
        mUiAction.listenEvent(mViewModel,this);
    }
}
