package net.childman.libmvvm.validator;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;

public class NoEmptyValidator<T> extends BaseValidator<T> {
    private boolean needTrim = false;
    public NoEmptyValidator(LiveData<T> data, int errMsg) {
        super(data, errMsg);
    }
    public NoEmptyValidator(LiveData<T> data, int errMsg, boolean needTrim) {
        super(data, errMsg);
        this.needTrim = needTrim;
    }

    @Override
    public boolean isValid() {
        if(mData.getValue() == null) return false;
        if(mData.getValue() instanceof String){
            String s = (String)mData.getValue();
            if(needTrim){
                if(s == null) return false;
                return !TextUtils.isEmpty(s.trim());
            }else {
                return !TextUtils.isEmpty(s);
            }
        }
        return true;
    }
}
