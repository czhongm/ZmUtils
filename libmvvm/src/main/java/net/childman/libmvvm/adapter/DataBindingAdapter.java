package net.childman.libmvvm.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import net.childman.libmvvm.BR;

public class DataBindingAdapter<T> extends BaseQuickAdapter<T,DataBindingAdapter.ViewHolder> {
    protected ViewModel mViewModel;

    public DataBindingAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public DataBindingAdapter(int layoutResId, @Nullable List<T> data, ViewModel viewModel) {
        super(layoutResId, data);
        mViewModel = viewModel;
    }

    @Override
    protected void convert(ViewHolder helper, T item) {
        ViewDataBinding viewDataBinding = helper.getDataBinding();
        viewDataBinding.setVariable(BR.item,item);
        viewDataBinding.setVariable(BR.viewModel,mViewModel);
        viewDataBinding.executePendingBindings();
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
        if (binding == null) {
            return super.getItemView(layoutResId, parent);
        }
        View view = binding.getRoot();
        view.setTag(com.chad.library.R.id.BaseQuickAdapter_databinding_support, binding);
        return view;
    }


    public static class ViewHolder extends BaseViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
        public ViewDataBinding getDataBinding(){
            return (ViewDataBinding)itemView.getTag(com.chad.library.R.id.BaseQuickAdapter_databinding_support);
        }
    }
}
