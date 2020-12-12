package net.childman.libmvvm.common;

import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;

import net.childman.libmvvm.R;
import net.childman.libmvvm.adapter.DataBindingAdapter;
import net.childman.libmvvm.viewmodel.BaseListViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BaseListHelper<T> implements OnItemClickListener, OnItemLongClickListener {
    private final LifecycleOwner mLifecycleOwner;
    protected DataBindingAdapter<T> mAdapter;
    protected final BaseListViewModel<T> mViewModel;
    private OnListDataChangeListener mOnListDataChangeListener;
    private OnItemEventListener<T> mOnItemEventListener;
    private int mEmptyLayoutRes;
    private RecyclerView mRecyclerView;

    public BaseListHelper(LifecycleOwner lifecycleOwner, BaseListViewModel<T> viewModel) {
        mLifecycleOwner = lifecycleOwner;
        mViewModel = viewModel;
    }

    private void initRecyclerView(RecyclerView recyclerView,@LayoutRes int itemLayoutRes, @LayoutRes int emtpyLayoutRes){
        mRecyclerView = recyclerView;
        mEmptyLayoutRes = emtpyLayoutRes;
        mAdapter = createAdapter(itemLayoutRes);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mViewModel.loadMore();
            }
        });
        mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        mAdapter.getLoadMoreModule().setAutoLoadMore(true);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);

        if(mViewModel.getDataList().size()>0){
            setNewData(mViewModel.getDataList());
        }else{
            mAdapter.setEmptyView(R.layout.loading);
            mViewModel.refresh();
        }
    }

    protected DataBindingAdapter<T> createAdapter(@LayoutRes int itemLayoutRes){
        return new DataBindingAdapter<>(itemLayoutRes, new ArrayList<>(),mViewModel);
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
                mAdapter.getLoadMoreModule().loadMoreComplete();
            }
        });

        mViewModel.getLoadMoreEndEvent().observe(mLifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                mAdapter.getLoadMoreModule().loadMoreEnd(true);
            }
        });

        mViewModel.getLoadMoreErrorEvent().observe(mLifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                mAdapter.getLoadMoreModule().loadMoreFail();
            }
        });

        mViewModel.getLoadMoreEnableEvent().observe(mLifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean!=null) mAdapter.getLoadMoreModule().setEnableLoadMore(aBoolean);
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

    public void notifyItemChanged(T t){
        int position = mAdapter.getData().indexOf(t);
        mAdapter.notifyItemChanged(position);
    }

    protected void setNewData(List<T> dataList){
        if(mRecyclerView!= null && dataList.size()==0){
            mAdapter.setEmptyView(mEmptyLayoutRes);
        }
        mAdapter.setNewData(dataList);
    }
    protected void appendData(List<T> dataList){
        mAdapter.addData(dataList);
        if(mRecyclerView != null && mAdapter.getItemCount()==0){
            mAdapter.setEmptyView(mEmptyLayoutRes);
        }
    }

    public void setOnItemEventListener(OnItemEventListener<T> onItemEventListener) {
        mOnItemEventListener = onItemEventListener;
    }

    @Override
    public void onItemClick(@NotNull BaseQuickAdapter adapter, @NotNull View view, int position) {
        if(mOnItemEventListener != null){
            T item = mAdapter.getItem(position);
            mOnItemEventListener.onItemClick(item);
        }
    }

    @Override
    public boolean onItemLongClick(@NotNull BaseQuickAdapter adapter, @NotNull View view, int position) {
        if(mOnItemEventListener != null){
            T item = mAdapter.getItem(position);
            return mOnItemEventListener.onItemLongClick(item);
        }
        return false;
    }

    public interface OnListDataChangeListener{
        void onListDataChanged();
    }

    public interface OnItemEventListener<T>{
        void onItemClick(T item);
        boolean onItemLongClick(T item);
    }

    public void setOnListDataChangeListener(OnListDataChangeListener onListDataChangeListener) {
        mOnListDataChangeListener = onListDataChangeListener;
    }

    public DataBindingAdapter<T> getAdapter(){
        return mAdapter;
    }
}
