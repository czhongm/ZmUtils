package net.childman.libmvvm.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.trello.rxlifecycle4.components.support.RxAppCompatActivity;

import net.childman.libmvvm.common.UiAction;


/**
 * Activity 基类
 * Created by czm on 18-3-7.
 */

public abstract class BaseActivity extends RxAppCompatActivity {
    protected TextView mToolbarTitle;
    protected UiAction mUiAction;

    static {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUiAction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mToolbarTitle = null;
        mUiAction = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(useHomeArrow()) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean useHomeArrow(){
        return true;
    }

    protected void initUiAction(){
        mUiAction = new UiAction(this);
    }
}
