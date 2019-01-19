package net.childman.libmvvm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;
import net.childman.libmvvm.BR;
import net.childman.libmvvm.common.DataBindingUiAction;
import net.childman.libmvvm.viewmodel.BaseViewModel;

public abstract class BaseDataBindingFragment<T extends BaseViewModel,E extends ViewDataBinding> extends BaseFragment {
    protected T mViewModel;
    protected E mDataBinding;
    protected View mRootView;

    @Override
    protected void initUiAction() {
        mUiAction = new DataBindingUiAction(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mRootView != null){
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (null != parent) {
                parent.removeView(mRootView);
            }
        }else {
            mDataBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);
            mRootView = mDataBinding.getRoot();
            if(useActivityViewModel() && this.getActivity()!=null){
                mViewModel = ViewModelProviders.of(this.getActivity()).get(getViewModelClass());
            }else{
                mViewModel = ViewModelProviders.of(this).get(getViewModelClass());
            }
            mDataBinding.setVariable(BR.viewModel, mViewModel);
            mDataBinding.setLifecycleOwner(this);
            initData();
            initView();
            listenEvent();
        }
        return mRootView;
    }

    protected void initData() {

    }

    protected void initView() {

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

    /**
     * 监听事件
     */
    protected void listenEvent(){
        if(mViewModel == null) return;
        mUiAction.listenEvent(mViewModel,this);
    }

    protected boolean useActivityViewModel(){
        return true;
    }
}
