package net.childman.libmvvm.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import net.childman.libmvvm.validator.BaseValidator;

import java.util.ArrayList;
import java.util.List;

public class BaseFormViewModel extends BaseViewModel {
    private List<BaseValidator> mValidators = new ArrayList<>();
    public final MutableLiveData<Boolean> formIsValid = new MutableLiveData<>();
    private Observer mObserver = new Observer() {
        @Override
        public void onChanged(Object o) {
            formIsValid.setValue(checkFormValid());
        }
    };

    protected void addValidator(BaseValidator validator){
        mValidators.add(validator);
    }

    /**
     * 监听表单是否有效
     * @param owner lifecyclerOwner
     */
    public void listenFormIsValid(@NonNull LifecycleOwner owner){
        List<LiveData> dataList = new ArrayList<>();
        for(BaseValidator validator : mValidators){
            if(!dataList.contains(validator.getData())){
                LiveData data = validator.getData();
                //DONE: 这里的警告处理
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
        for (BaseValidator validator : mValidators) {
            if (!validator.isValid()) {
                return false;
            }
        }
        return true;
    }

    protected final boolean checkValid(LiveData... items){
        clearError();
        if(items.length == 0) {
            for (BaseValidator validator : mValidators) {
                if (!validator.isValid()) {
                    showError(validator.getMsg());
                    return false;
                }
            }
            return true;
        }else{
            for(LiveData data : items){
                for (BaseValidator validator : mValidators) {
                    if (validator.getData() == data && !validator.isValid()) {
                        showError(validator.getMsg());
                        return false;
                    }
                }
            }
            return true;
        }
    }
}
