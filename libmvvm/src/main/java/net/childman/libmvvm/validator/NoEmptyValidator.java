package net.childman.libmvvm.validator;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoEmptyValidator<T> extends BaseValidator<T> {
    private boolean needTrim = false;
    public NoEmptyValidator(LiveData<T> data, int destId, int errMsg) {
        super(data, destId, errMsg);
    }
    public NoEmptyValidator(LiveData<T> data, int destId, int errMsg, boolean needTrim) {
        super(data, destId, errMsg);
        this.needTrim = needTrim;
    }

    @Override
    public boolean isInvalid() {
        T value = mData.getValue();
        if(value == null) return true;
        if(value instanceof String){
            String s = (String)value;
            if(needTrim){
                s = s.trim();
            }
            return TextUtils.isEmpty(s);
        }else if(value instanceof List){
            List<?> list = (List<?>) mData.getValue();
            return list.isEmpty();
        }
        return false;
    }
}
