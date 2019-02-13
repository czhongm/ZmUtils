package net.childman.libmvvm.viewmodel;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import androidx.annotation.StringRes;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import net.childman.libmvvm.BuildConfig;
import net.childman.libmvvm.model.HttpResult;
import net.childman.libmvvm.model.ServerResult;
import net.childman.libmvvm.utils.SingleLiveEvent;

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
    public class MsgData{
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

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private final SingleLiveEvent<MsgData> uploadEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<MsgData> errorEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<MsgData> tipEvent = new SingleLiveEvent<>();
    private MutableLiveData<Boolean> uploading = new MutableLiveData<>();
    private final SingleLiveEvent<Boolean> redirectLoginEvent = new SingleLiveEvent<>();

    public SingleLiveEvent<MsgData> getUploadEvent() {
        return uploadEvent;
    }

    public SingleLiveEvent<MsgData> getErrorEvent() {
        return errorEvent;
    }

    public SingleLiveEvent<MsgData> getTipEvent() {
        return tipEvent;
    }

    public SingleLiveEvent<Boolean> getRedirectLoginEvent() {
        return redirectLoginEvent;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(mCompositeDisposable != null) mCompositeDisposable.dispose();
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

    protected void addDisposable(Disposable disposable){
        mCompositeDisposable.add(disposable);
    }

    protected <T> CheckResultTransformer<T> applyCheckHttpResult(){
        return new CheckResultTransformer<>();
    }

    public class CheckResultTransformer<T> implements FlowableTransformer<HttpResult<T>, ServerResult<T>> {

        @Override
        public Publisher<ServerResult<T>> apply(Flowable<HttpResult<T>> upstream) {
            return upstream
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(new Predicate<HttpResult<T>>() {
                        @Override
                        public boolean test(HttpResult<T> tHttpResult) throws Exception {
                            if(tHttpResult.isSuccess()){
                                return true;
                            }else{
                                showError(tHttpResult.getMsg()); //显示错误信息
                                if(tHttpResult.getCode() == 700){
                                    //DONE:重定向到登录页
                                    redirectLoginEvent.setValue(true);
                                }
                                return false;
                            }
                        }
                    })
                    .map(new Function<HttpResult<T>, ServerResult<T>>() {
                        @Override
                        public ServerResult<T> apply(HttpResult<T> tHttpResult) throws Exception {
                            return tHttpResult.getServerResult();
                        }
                    });
        }
    }

    protected <T> UploadingTransformer<T> applyUploading(){
        return new UploadingTransformer<>();
    }

    public class UploadingTransformer<T> implements FlowableTransformer<T, T> {

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
