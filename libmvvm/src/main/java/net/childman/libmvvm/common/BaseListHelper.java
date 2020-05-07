package net.childman.libmvvm.common;

import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import net.childman.libmvvm.R;
import net.childman.libmvvm.adapter.DataBindingAdapter;
import net.childman.libmvvm.viewmodel.BaseListViewModel;

import java.util.ArrayList;
import java.util.List;

public class BaseListHelper<T> implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener {
    private LifecycleOwner mLifecycleOwner;
    private DataBindingAdapter<T> mAdapter;
    private BaseListViewModel<T> mViewModel;
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
        mAdapter.bindToRecyclerView(recyclerView);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mViewModel.loadMore();
            }
        },recyclerView);
        mAdapter.disableLoadMoreIfNotFullPage();
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);

        if(mViewModel.getDataList().size()>0){
            setNewData(mViewModel.getDataList());
        }else{
            mAdapter.setEmptyView(R.layout.loading,recyclerView);
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

    public void notifyItemChanged(T t){
        int position = mAdapter.getData().indexOf(t);
        mAdapter.notifyItemChanged(position);
    }

    protected void setNewData(List<T> dataList){
        if(mRecyclerView!= null && dataList.size()==0){
            mAdapter.setEmptyView(mEmptyLayoutRes,mRecyclerView);
        }
        mAdapter.setNewData(dataList);
    }
    protected void appendData(List<T> dataList){
        mAdapter.addData(dataList);
        if(mRecyclerView != null && mAdapter.getItemCount()==0){
            mAdapter.setEmptyView(mEmptyLayoutRes,mRecyclerView);
        }
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

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
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
