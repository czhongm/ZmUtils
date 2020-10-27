package net.childman.libmvvm.viewmodel;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import net.childman.libmvvm.utils.SingleLiveEvent;
import net.childman.libmvvm.validator.BaseValidator;

import java.util.ArrayList;
import java.util.List;

public class BaseFormViewModel extends BaseViewModel {
    private final List<BaseValidator<?>> mValidators = new ArrayList<>();
    public final MutableLiveData<Boolean> formIsValid = new MutableLiveData<>();
    public final SingleLiveEvent<FormErrorData> formErrorEvent = new SingleLiveEvent<>();
    public final MutableLiveData<Boolean> formChanged = new MutableLiveData<>();

    private final List<Pair<LiveData<?>,Object>> mListenChangeFields = new ArrayList<>();

    /**
     * 保存初始状态
     */
    public void saveInitState(@NonNull LifecycleOwner owner, LiveData<?>... items){
        for(Pair<LiveData<?>,Object> field : mListenChangeFields) {
            field.first.removeObserver(mChangeObserver);
        }
        mListenChangeFields.clear();
        for(LiveData<?> item : items){
            mListenChangeFields.add(new Pair<>(item,item.getValue()));
            item.observe(owner,mChangeObserver);
        }
    }

    protected boolean isRealTimeCheck(){
        return false;
    }

    private final Observer<Object> mChangeObserver = new Observer() {
        @Override
        public void onChanged(Object o) {
            checkChanged();
        }
    };

    private void checkChanged() {
        for(Pair<LiveData<?>,Object> field : mListenChangeFields){
            if(field.first.getValue()==null){
                if(field.second != null){
                    formChanged.setValue(true);
                    return;
                }
            }else if(!field.first.getValue().equals(field.second)){
                formChanged.setValue(true);
                return;
            }
        }
        formChanged.setValue(false);
    }

    private final Observer<Object> mObserver = new Observer() {
        @Override
        public void onChanged(Object o) {
            formErrorEvent.setValue(null); //输入的时候清空错误
            formIsValid.setValue(checkFormValid());
        }
    };

    public static class FormErrorData{
        private final int destId;
        private final int errMsg;

        public FormErrorData(int destId, int errMsg) {
            this.destId = destId;
            this.errMsg = errMsg;
        }

        public int getDestId() {
            return destId;
        }

        public int getErrMsg() {
            return errMsg;
        }
    }

    protected void addValidator(BaseValidator<?> validator){
        mValidators.add(validator);
    }

    /**
     * 监听表单是否有效
     * @param owner lifecyclerOwner
     */
    public void listenFormIsValid(@NonNull LifecycleOwner owner){
        List<LiveData<?>> dataList = new ArrayList<>();
        for(BaseValidator<?> validator : mValidators){
            if(!dataList.contains(validator.getData())){
                LiveData<?> data = validator.getData();
                data.observe(owner, mObserver);
                dataList.add(data);
            }
        }
    }

    protected void clearValidator(){
        mValidators.clear();
    }

    /**
     * 检查表单是否有效
     * @return 是否有效
     */
    protected boolean checkFormValid(){
        for (BaseValidator<?> validator : mValidators) {
            if (validator.isInvalid()) {
                if(isRealTimeCheck()){ //如果实时校验的话，弹出错误
                    formErrorEvent.setValue(new FormErrorData(validator.getDestId(),validator.getErrMsg()));
                }
                return false;
            }
        }
        return true;
    }

    protected final boolean checkValid(LiveData<?>... items){
        clearError();
        if(items.length == 0) {
            for (BaseValidator<?> validator : mValidators) {
                if (validator.isInvalid()) {
                    formErrorEvent.setValue(new FormErrorData(validator.getDestId(),validator.getErrMsg()));
                    return false;
                }
            }
        }else{
            for(LiveData<?> data : items){
                for (BaseValidator<?> validator : mValidators) {
                    if (validator.getData() == data && validator.isInvalid()) {
                        formErrorEvent.setValue(new FormErrorData(validator.getDestId(),validator.getErrMsg()));
                        return false;
                    }
                }
            }
        }
        formErrorEvent.setValue(null);
        return true;
    }
}
