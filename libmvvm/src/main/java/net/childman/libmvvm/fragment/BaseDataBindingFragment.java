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
import androidx.lifecycle.ViewModelProvider;
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
                mViewModel = new ViewModelProvider(requireActivity()).get(getViewModelClass());
            }else{
                mViewModel = new ViewModelProvider(this).get(getViewModelClass());
            }
            mDataBinding.setVariable(BR.viewModel, mViewModel);
            mDataBinding.setLifecycleOwner(this);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
        listenEvent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRootView = null;
        mDataBinding.unbind();
        mDataBinding = null;
        mViewModel = null;
    }

    protected void initData() {

    }

    protected void initView() {

    }

    protected T getViewModel(Class<T> tClass){
        if(tClass.isInstance(mViewModel)){
            return (T)mViewModel;
        }
        return null;
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
