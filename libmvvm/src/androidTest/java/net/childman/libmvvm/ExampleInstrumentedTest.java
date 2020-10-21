package net.childman.libmvvm;

import android.content.Context;
import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import net.childman.libmvvm.utils.CommonUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private Context mContext = ApplicationProvider.getApplicationContext();
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("net.childman.libmvvm.test", appContext.getPackageName());
    }

    @Test
    public void deviceIdTest(){
        assertThat(CommonUtils.getDeviceId(mContext)).isEqualTo("111111");
    }

}
