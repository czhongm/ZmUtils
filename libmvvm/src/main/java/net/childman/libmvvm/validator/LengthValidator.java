package net.childman.libmvvm.validator;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;

/**
 * 长度验证器
 */
public class LengthValidator extends BaseValidator<String> {
    private int minLen=1;
    private int maxLen=Integer.MAX_VALUE;
    public LengthValidator(LiveData<String> data, int errMsg) {
        super(data, errMsg);
    }

    public LengthValidator(LiveData<String> data, int errMsg, int minLen) {
        super(data, errMsg);
        this.minLen = minLen;
    }

    public LengthValidator(LiveData<String> data, int errMsg, int minLen,int maxLen) {
        super(data, errMsg);
        this.minLen = minLen;
        this.maxLen = maxLen;
    }

    @Override
    public boolean isValid() {
        if(TextUtils.isEmpty(mData.getValue())) return false;
        int length = mData.getValue().length();
        return length>=minLen && length<=maxLen;
    }
}
