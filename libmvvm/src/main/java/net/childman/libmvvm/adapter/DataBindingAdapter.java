package net.childman.libmvvm.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import net.childman.libmvvm.BR;

import org.jetbrains.annotations.NotNull;

public class DataBindingAdapter<T> extends BaseQuickAdapter<T,BaseViewHolder> implements LoadMoreModule {
    protected ViewModel mViewModel;

    public DataBindingAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public DataBindingAdapter(int layoutResId, @Nullable List<T> data, ViewModel viewModel) {
        super(layoutResId, data);
        mViewModel = viewModel;
    }

    public void notifyItemChanged(T item){
        int index = getData().indexOf(item);
        if(index != -1){
            notifyItemChanged(index + getHeaderLayoutCount());
        }
    }

    @Override
    protected void onItemViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, T item) {
        ViewDataBinding viewDataBinding = baseViewHolder.getBinding();
        if(viewDataBinding != null) {
            viewDataBinding.setVariable(BR.item, item);
            viewDataBinding.setVariable(BR.viewModel, mViewModel);
            viewDataBinding.executePendingBindings();
        }
    }
}
