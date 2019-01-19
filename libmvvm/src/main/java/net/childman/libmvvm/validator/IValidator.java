package net.childman.libmvvm.validator;

import androidx.annotation.StringRes;

/**
 * 验证器接口
 */
public interface IValidator {
    @StringRes int getMsg();
    boolean isValid();
}
