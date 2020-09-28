package net.childman.libmvvm.validator;

import androidx.lifecycle.LiveData;

import net.childman.libmvvm.utils.CommonUtils;

/**
 * 手机号码验证器
 */
public class MobileValidator extends BaseValidator<String> {
    public MobileValidator(LiveData<String> data, int errMsg) {
        super(data, errMsg);
    }

    @Override
    public boolean isValid() {
        return CommonUtils.isChinaPhoneLegal(mData.getValue());
    }
}
