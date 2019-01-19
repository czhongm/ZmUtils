package net.childman.libmvvm.viewmodel;


import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import net.childman.libmvvm.model.HttpResult;
import net.childman.libmvvm.model.ServerResult;
import net.childman.libmvvm.utils.SingleLiveEvent;

public abstract class BaseListViewModel<T> extends BaseViewModel {
    protected int mPageSize = 15;
    protected int mTotalNum;
    private List<T> mDataList = new ArrayList<>();
    public final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    protected final MutableLiveData<List<T>> loadEvent = new MutableLiveData<>();
    private final MutableLiveData<List<T>> loadMoreEvent = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadMoreEndEvent = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadMoreCompleteEvent = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadMoreErrorEvent = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadMoreEnableEvent = new MutableLiveData<>();

    protected final SingleLiveEvent<Boolean> addEvent = new SingleLiveEvent<>();
    protected final SingleLiveEvent<T> editEvent = new SingleLiveEvent<>();
    protected final SingleLiveEvent<T> deleteEvent = new SingleLiveEvent<>();
    protected final SingleLiveEvent<T> deleteSuccEvent = new SingleLiveEvent<>();

    private Disposable mRefreshDisposable;
    private int mCurrentPage;

    public MutableLiveData<List<T>> getLoadEvent() {
        return loadEvent;
    }

    public MutableLiveData<List<T>> getLoadMoreEvent() {
        return loadMoreEvent;
    }

    public MutableLiveData<Boolean> getLoadMoreEndEvent() {
        return loadMoreEndEvent;
    }

    public MutableLiveData<Boolean> getLoadMoreCompleteEvent() {
        return loadMoreCompleteEvent;
    }

    public MutableLiveData<Boolean> getLoadMoreErrorEvent() {
        return loadMoreErrorEvent;
    }

    public MutableLiveData<Boolean> getLoadMoreEnableEvent() {
        return loadMoreEnableEvent;
    }

    public SingleLiveEvent<Boolean> getAddEvent() {
        return addEvent;
    }

    public SingleLiveEvent<T> getEditEvent() {
        return editEvent;
    }

    public SingleLiveEvent<T> getDeleteEvent() {
        return deleteEvent;
    }

    public SingleLiveEvent<T> getDeleteSuccEvent() {
        return deleteSuccEvent;
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public int getTotalNum() {
        return mTotalNum;
    }

    protected boolean isSinglePage() {
        return false;
    }

    /**
     * 刷新
     */
    public void refresh() {
        if (mRefreshDisposable != null && !mRefreshDisposable.isDisposed()) return;
        Flowable<HttpResult<List<T>>> flowable = fetchList(1, mPageSize);
        if(flowable == null) return;
        mRefreshDisposable = flowable
                .subscribeOn(Schedulers.io())
                .compose(this.<List<T>>applyCheckHttpResult())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        isLoading.setValue(true);
                        loadMoreEnableEvent.setValue(false); //禁止loadMore
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        isLoading.setValue(false);
                        if(!isSinglePage()) loadMoreEnableEvent.setValue(true); //重新开启loadMore
                    }
                })
                .subscribe(new Consumer<ServerResult<List<T>>>() {
                    @Override
                    public void accept(ServerResult<List<T>> listServerResult) throws Exception {
                        getExtraData(listServerResult);
                        mTotalNum = listServerResult.getCount();
                        mDataList.clear();
                        List<T> dataList = listServerResult.getData();
                        mDataList.addAll(dataList);
                        loadEvent.setValue(dataList);
                        if(mTotalNum == 0){ //如果获取不到总数的话
                            if(dataList.size()<mPageSize){ //小于分页数，表示结束了
                                loadMoreEndEvent.setValue(true);
                            }else{
                                loadMoreCompleteEvent.setValue(true);
                            }
                            mTotalNum = dataList.size();
                        }else {
                            if (isSinglePage() || mDataList.size() >= mTotalNum) {
                                loadMoreEndEvent.setValue(true);
                            } else {
                                loadMoreCompleteEvent.setValue(true);
                            }
                        }
                        mCurrentPage = 1;
                    }
                }, mThrowableConsumer);
        addDisposable(mRefreshDisposable);
    }

//    /**
//     * 附加处理数据
//     * @param data
//     * @return
//     */
//    protected Collection<? extends T> processData(List<T> data) {
//        return null;
//    }

    /**
     * 留出接口给获取附加信息
     * @param listServerResult 数据
     */
    protected void getExtraData(ServerResult<List<T>> listServerResult){
        //do nothing
    }

    /**
     * 加载更多
     */
    public void loadMore() {
        if(isSinglePage()){
            loadMoreEndEvent.setValue(true);
            return; //如果是单页，则忽略加载更多
        }
        if (mRefreshDisposable != null && !mRefreshDisposable.isDisposed()) {
            loadMoreErrorEvent.setValue(true);
            return;
        }
        Flowable<HttpResult<List<T>>> flowable = fetchList(mCurrentPage + 1, mPageSize);
        if(flowable == null) return;
        mRefreshDisposable = flowable
                .compose(this.<List<T>>applyCheckHttpResult())
                .subscribe(new Consumer<ServerResult<List<T>>>() {
                    @Override
                    public void accept(ServerResult<List<T>> listServerResult) throws Exception {
                        getExtraData(listServerResult);
                        mTotalNum = listServerResult.getCount();
                        List<T> dataList = listServerResult.getData();
                        loadMoreEvent.setValue(dataList);
                        mDataList.addAll(dataList);
                        if(mTotalNum == 0){ //如果获取不到总数的话
                            if(dataList.size()<mPageSize){ //小于分页数，表示结束了
                                loadMoreEndEvent.setValue(true);
                            }else{
                                loadMoreCompleteEvent.setValue(true);
                            }
                            mTotalNum = mDataList.size();
                        }else {
                            if (isSinglePage() || mDataList.size() >= mTotalNum) {
                                loadMoreEndEvent.setValue(true); //
                            } else {
                                loadMoreCompleteEvent.setValue(true);
                            }
                        }
                        mCurrentPage++;
                    }
                }, mThrowableConsumer);
        addDisposable(mRefreshDisposable);
    }

    protected abstract Flowable<HttpResult<List<T>>> fetchList(int page, int limit);

    protected Flowable<HttpResult<String>> deleteMethod(T item) {
        return null;
    }

    public void onDelete(T item) {
        deleteEvent.setValue(item);
    }

    public void onEdit(T item) {
        editEvent.setValue(item);
    }

    public void onAdd() {
        addEvent.setValue(true);
    }

    public void delete(final T item) {
        Flowable<HttpResult<String>> delMethod = deleteMethod(item);
        if (delMethod == null) return;
        Disposable disposable = delMethod
                .compose(this.<String>applyCheckHttpResult())
                .compose(this.<ServerResult<String>>applyUploading())
                .subscribe(new Consumer<ServerResult<String>>() {
                    @Override
                    public void accept(ServerResult<String> stringServerResult) throws Exception {
                        deleteSuccEvent.setValue(item);
                        mTotalNum--; //???这里需要么
                    }
                }, mThrowableConsumer);
        addDisposable(disposable);
    }
}
