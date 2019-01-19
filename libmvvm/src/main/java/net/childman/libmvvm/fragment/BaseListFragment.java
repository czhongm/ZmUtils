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
    protected void listenEvent() {
        super.listenEvent();
        mBaseListHelper.listenEvent();
    }

    @Override
    protected void initView() {
        super.initView();
        mBaseListHelper = new BaseListHelper<>(this,mViewModel);
        mBaseListHelper.initView(getRecyclerView(),getItemViewRes(),getEmptyViewRes());
    }

    protected abstract @NonNull RecyclerView getRecyclerView();
    protected abstract @LayoutRes int getItemViewRes();
    protected @LayoutRes int getEmptyViewRes(){
        return R.layout.empty;
    }
}
