package net.childman.libmvvm.validator;

import androidx.lifecycle.LiveData;

/**
 * 长度验证器
 */
public class LengthValidator extends BaseValidator<String> {
    private int minLen=1;
    private int maxLen=Integer.MAX_VALUE;
    public LengthValidator(LiveData<String> data, int destId, int errMsg) {
        super(data, destId, errMsg);
    }

    public LengthValidator(LiveData<String> data, int destId, int errMsg, int minLen) {
        super(data, destId, errMsg);
        this.minLen = minLen;
    }

    public LengthValidator(LiveData<String> data, int destId, int errMsg, int minLen,int maxLen) {
        super(data, destId, errMsg);
        this.minLen = minLen;
        this.maxLen = maxLen;
    }

    @Override
    public boolean isInvalid() {
        String value = mData.getValue();
        if(value == null || value.length()==0) return true;
        return value.length()<minLen || value.length()>maxLen;
    }
}
