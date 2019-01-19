package net.childman.libmvvm.common;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import net.childman.libmvvm.adapter.DataBindingAdapter;
import net.childman.libmvvm.viewmodel.BaseListViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

public class BaseListHelper<T> implements BaseQuickAdapter.OnItemClickListener {
    private LifecycleOwner mLifecycleOwner;
    private DataBindingAdapter<T> mAdapter;
    private BaseListViewModel<T> mViewModel;
    private OnListDataChangeListener mOnListDataChangeListener;
    private OnItemEventListener<T> mOnItemEventListener;

    public BaseListHelper(LifecycleOwner lifecycleOwner, BaseListViewModel<T> viewModel) {
        mLifecycleOwner = lifecycleOwner;
        mViewModel = viewModel;
    }

    private void initRecyclerView(RecyclerView recyclerView,@LayoutRes int itemLayoutRes, @LayoutRes int emtpyLayoutRes){
        mAdapter = new DataBindingAdapter<>(itemLayoutRes, new ArrayList<>(),mViewModel);
        mAdapter.bindToRecyclerView(recyclerView);
        mAdapter.setEmptyView(emtpyLayoutRes,recyclerView);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mViewModel.loadMore();
            }
        },recyclerView);
        mAdapter.disableLoadMoreIfNotFullPage();
        mAdapter.setOnItemClickListener(this);

        if(mViewModel.getDataList().size()>0){
            setNewData(mViewModel.getDataList());
        }else{
            mViewModel.refresh();
        }
    }

    public void initView(@NonNull RecyclerView recyclerView, @LayoutRes int itemLayoutRes, @LayoutRes int emptyLayoutRes) {
        initRecyclerView(recyclerView,itemLayoutRes,emptyLayoutRes);
    }

    public void listenEvent(){
        mViewModel.getLoadEvent().observe(mLifecycleOwner, new Observer<List<T>>() {
            @Override
            public void onChanged(@Nullable List<T> alertInfos) {
                if(alertInfos == null){
                    alertInfos = new ArrayList<>();
                }
                setNewData(alertInfos);
                if(mOnListDataChangeListener != null) mOnListDataChangeListener.onListDataChanged();
            }
        });

        mViewModel.getLoadMoreEvent().observe(mLifecycleOwner, new Observer<List<T>>() {
            @Override
            public void onChanged(@Nullable List<T> alertInfos) {
                if(alertInfos!=null) {
                    appendData(alertInfos);
                    if(mOnListDataChangeListener != null) mOnListDataChangeListener.onListDataChanged();
                }
            }
        });

        mViewModel.getLoadMoreCompleteEvent().observe(mLifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                mAdapter.loadMoreComplete();
            }
        });

        mViewModel.getLoadMoreEndEvent().observe(mLifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                mAdapter.loadMoreEnd(true);
            }
        });

        mViewModel.getLoadMoreErrorEvent().observe(mLifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                mAdapter.loadMoreFail();
            }
        });

        mViewModel.getLoadMoreEnableEvent().observe(mLifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean!=null) mAdapter.setEnableLoadMore(aBoolean);
            }
        });
        mViewModel.getDeleteSuccEvent().observe(mLifecycleOwner, new Observer<T>() {
            @Override
            public void onChanged(@Nullable T t) {
                onDeleteSuccess(t);
            }
        });
    }

    private void onDeleteSuccess(T t) {
        int position = mAdapter.getData().indexOf(t);
        mAdapter.remove(position);
    }

    public void notifyDataSetChanged(){
        mAdapter.notifyDataSetChanged();
    }

    protected void setNewData(List<T> dataList){
        mAdapter.setNewData(dataList);
    }
    protected void appendData(List<T> dataList){
        mAdapter.addData(dataList);
    }

    public void setOnItemEventListener(OnItemEventListener<T> onItemEventListener) {
        mOnItemEventListener = onItemEventListener;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if(mOnItemEventListener != null){
            T item = mAdapter.getItem(position);
            mOnItemEventListener.onItemClick(item);
        }
    }

    public interface OnListDataChangeListener{
        void onListDataChanged();
    }

    public interface OnItemEventListener<T>{
        void onItemClick(T item);
    }

    public void setOnListDataChangeListener(OnListDataChangeListener onListDataChangeListener) {
        mOnListDataChangeListener = onListDataChangeListener;
    }
}
