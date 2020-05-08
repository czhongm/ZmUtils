package net.childman.libmvvm.fragment;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import net.childman.libmvvm.R;
import net.childman.libmvvm.common.BaseListHelper;
import net.childman.libmvvm.viewmodel.BaseListViewModel;


public abstract class BaseListFragment<T,E extends ViewDataBinding> extends BaseDataBindingFragment<BaseListViewModel<T>,E> {
    protected BaseListHelper<T> mBaseListHelper;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBaseListHelper = null;
    }

    @Override
    protected void listenEvent() {
        super.listenEvent();
        mBaseListHelper.listenEvent();
    }

    @Override
    protected void initView() {
        super.initView();
        mBaseListHelper = createHelper();
        mBaseListHelper.initView(getRecyclerView(),getItemViewRes(),getEmptyViewRes());
    }

    protected BaseListHelper<T> createHelper(){
        return new BaseListHelper<>(getViewLifecycleOwner(),mViewModel);
    }

    protected abstract @NonNull RecyclerView getRecyclerView();
    protected abstract @LayoutRes int getItemViewRes();
    protected @LayoutRes int getEmptyViewRes(){
        return R.layout.empty;
    }
}
