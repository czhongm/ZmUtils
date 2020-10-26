package net.childman.libmvvm.validator;

import androidx.lifecycle.LiveData;

public class SameValidator<T> extends BaseValidator<T> {
    private final LiveData<T> compareData;
    public SameValidator(LiveData<T> data, LiveData<T> compareData,int destId, int errMsg) {
        super(data,destId,errMsg);
        this.compareData = compareData;
    }

    @Override
    public boolean isInvalid() {
        if(mData == null || compareData == null || mData.getValue()==null || compareData.getValue()==null) return true;
        return (!mData.getValue().equals(compareData.getValue()));
    }
}
