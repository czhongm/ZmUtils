package net.childman.libmvvm.viewmodel;

import android.view.View;

import androidx.annotation.StringRes;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.childman.libmvvm.BuildConfig;
import net.childman.libmvvm.utils.SingleLiveEvent;

import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableTransformer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * 基础ViewModel类
 *
 * 一些大部分要用的功能函数
 */
public class BaseViewModel extends ViewModel {

    private boolean changed = false;

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    /**
     * 设置初始状态
     */
    public void updateInitState(){
        changed = false;
    }

    /**
     * 提示/错误/loading 数据类
     */
    public static class MsgData{
        private @StringRes int msgResId;
        private String msg;

        MsgData(String msg) {
            this.msg = msg;
        }

        MsgData(int resId) {
            this.msgResId = resId;
        }

        public int getMsgResId() {
            return msgResId;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsgResId(int msgResId) {
            this.msgResId = msgResId;
        }
    }

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private final SingleLiveEvent<MsgData> uploadEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<MsgData> errorEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<MsgData> tipEvent = new SingleLiveEvent<>();
    private final MutableLiveData<Boolean> uploading = new MutableLiveData<>();
    private final SingleLiveEvent<Integer> clickEvent = new SingleLiveEvent<>();

    public SingleLiveEvent<MsgData> getUploadEvent() {
        return uploadEvent;
    }

    public SingleLiveEvent<MsgData> getErrorEvent() {
        return errorEvent;
    }

    public SingleLiveEvent<MsgData> getTipEvent() {
        return tipEvent;
    }

    public SingleLiveEvent<Integer> getClickEvent() {
        return clickEvent;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposable.dispose();
    }

    protected void showError(String error){
        errorEvent.setValue(new MsgData(error));
    }

    protected void showError(@StringRes int stringId){
        errorEvent.setValue(new MsgData(stringId));
    }

    protected void showTip(String tip){
        tipEvent.setValue(new MsgData(tip));
    }

    protected void showTip(@StringRes int stringId){
        tipEvent.setValue(new MsgData(stringId));
    }

    protected void hideLoading(){
        uploadEvent.setValue(null);
    }

    protected void showLoading(String loadingText){
        uploadEvent.setValue(new MsgData(loadingText));
    }

    protected void showLoading(@StringRes int stringId){
        uploadEvent.setValue(new MsgData(stringId));
    }

    protected void clearError(){
        errorEvent.setValue(null);
    }

    /**
     * 通用的错误捕捉方法
     */
    protected Consumer<Throwable> mThrowableConsumer = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            showError(throwable.getMessage());
            if(BuildConfig.DEBUG) {
                throwable.printStackTrace();
            }
        }
    };

    /**
     * 通用的点击事件
     * @param view view
     */
    public void onViewClick(View view){
        clickEvent.setValue(view.getId());
    }

    protected void addDisposable(Disposable disposable){
        mCompositeDisposable.add(disposable);
    }

    protected <T> UploadingTransformer<T> applyUploading(){
        return new UploadingTransformer<>();
    }

    public class UploadingTransformer<T> implements FlowableTransformer<T, T> {

        @NotNull
        @Override
        public Publisher<T> apply(Flowable<T> upstream) {
            return upstream
                    .doOnSubscribe(new Consumer<Subscription>() {
                        @Override
                        public void accept(Subscription subscription) throws Exception {
                            uploading.setValue(true);
                        }
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            uploading.setValue(false);
                        }
                    });
        }
    }

}
