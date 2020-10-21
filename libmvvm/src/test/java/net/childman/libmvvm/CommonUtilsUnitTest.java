package net.childman.libmvvm;

import android.content.Context;


import androidx.test.core.app.ApplicationProvider;

import net.childman.libmvvm.utils.CommonUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static com.google.common.truth.Truth.assertThat;
@RunWith(RobolectricTestRunner.class)
public class CommonUtilsUnitTest {

    private Context mContext = ApplicationProvider.getApplicationContext();

    @Test
    public void getDoubleStringTest(){
        assertThat(CommonUtils.getDoubleStr(12.0)).isEqualTo("12");
        assertThat(CommonUtils.getDoubleStr(12.00)).isEqualTo("12");
        assertThat(CommonUtils.getDoubleStr(12.01)).isEqualTo("12.01");
        assertThat(CommonUtils.getDoubleStr(0.00)).isEqualTo("0");
    }

    @Test
    public void isChinaPhoneLegalTest(){
        assertThat(CommonUtils.isChinaPhoneLegal("13071873322")).isTrue();
        assertThat(CommonUtils.isChinaPhoneLegal("10071873322")).isFalse();
    }

    @Test
    public void deviceIdTest(){
        assertThat(CommonUtils.getDeviceId(mContext)).isEqualTo("111111");
    }
}
