package net.childman.libmvvm.validator;

import androidx.lifecycle.LiveData;

public class SameValidator<T> extends BaseValidator<T> {
    private LiveData<T> compareData;
    public SameValidator(LiveData<T> data, LiveData<T> compareData, int errMsg) {
        super(data, errMsg);
        this.compareData = compareData;
    }

    @Override
    public boolean isValid() {
        if(mData == null) return false;
        if(compareData == null) return false;
        if(mData.getValue() == null) return false;
        if(compareData.getValue() == null) return false;
        return (mData.getValue().equals(compareData.getValue()));
    }
}
