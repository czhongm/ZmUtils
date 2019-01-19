package net.childman.libmvvm.validator;

import androidx.annotation.StringRes;
import androidx.lifecycle.LiveData;

/**
 * 字符串验证器基类
 */
public abstract class BaseValidator<T> implements IValidator{
    protected LiveData<T> mData;
    protected int mErrMsg;

    public BaseValidator(LiveData<T> data, @StringRes int errMsg) {
        mData = data;
        mErrMsg = errMsg;
    }

    public LiveData<T> getData() {
        return mData;
    }

    @Override
    public int getMsg() {
        return mErrMsg;
    }

    @Override
    public abstract boolean isValid();
}
