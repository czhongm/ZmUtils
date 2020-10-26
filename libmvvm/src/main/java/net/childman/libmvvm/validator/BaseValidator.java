package net.childman.libmvvm.validator;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.lifecycle.LiveData;

/**
 * 验证器基类
 */
public abstract class BaseValidator<T>{
    protected LiveData<T> mData;
    protected int mDestId;
    protected int mErrMsg;

    /**
     * 验证器构造
     * @param data 监听的数据
     * @param destId 目标id
     * @param errMsg 错误提示内容
     */
    public BaseValidator(LiveData<T> data, @IdRes int destId, @StringRes int errMsg) {
        mData = data;
        mDestId = destId;
        mErrMsg = errMsg;
    }

    /**
     * 监听的数据
     * @return 数据
     */
    public LiveData<T> getData() {
        return mData;
    }

    /**
     * 目标id
     * @return 目标id
     */
    public int getDestId() {
        return mDestId;
    }

    /**
     * 错误提示
     * @return 错误提示
     */
    public int getErrMsg() {
        return mErrMsg;
    }

    /**
     * 判断是否无效
     * @return 是否无效
     */
    public abstract boolean isInvalid();
}
