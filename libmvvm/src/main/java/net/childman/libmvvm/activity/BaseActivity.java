package net.childman.libmvvm.activity;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.ViewDataBinding;


import com.trello.rxlifecycle4.components.support.RxAppCompatActivity;

import net.childman.libmvvm.R;
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
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean isTranslucentStatus(){
        return getTypeValueBoolean(R.attr.isTranslucentStatus);
    }

    private int getStatusBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen","android");
        return resources.getDimensionPixelSize(resourceId);
    }

    private TypedArray getAttribute(int attr){
        TypedValue typedValue = new TypedValue();
        int[] attribute = new int[]{attr};
        return obtainStyledAttributes(typedValue.resourceId, attribute);
    }

    private boolean getTypeValueBoolean(int attr) {
        TypedArray array = getAttribute(attr);
        boolean statusFont = array.getBoolean(0, false);
        array.recycle();
        return statusFont;
    }

    private int getTypeValueColor(int attr) {
        TypedArray array = getAttribute(attr);
        int color = array.getColor(0, -1);
        array.recycle();
        return color;
    }

    private Drawable getTypeValueDrawable(int attr) {
        TypedArray array = getAttribute(attr);
        Drawable drawable = array.getDrawable(0);
        array.recycle();
        return drawable;
    }

    protected int getStatusBarColor() {
        int color = getTypeValueColor(R.attr.customStatusBarColor);
        if(color == -1) {
            return Color.TRANSPARENT;
        }else{
            return color;
        }
    }
    protected void initUiAction(){
        mUiAction = new UiAction(this);
    }

    protected void initStatusBar(){
        if(!isTranslucentStatus()) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Android5.0之后的沉浸式状态栏写法
            Window window = getWindow();
            View decorView = window.getDecorView();
            // 两个标志位要结合使用，表示让应用的主体内容占用系统状态栏的空间
            // 第三个标志位可让底部导航栏变透明View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            decorView.setSystemUiVisibility(option);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getStatusBarColor());
        } else {
            // Android4.4的沉浸式状态栏写法
            Window window = getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            // 底部导航栏也可以弄成透明的
            //int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            attributes.flags |= flagTranslucentStatus;
            //attributes.flags |= flagTranslucentNavigation;
            window.setAttributes(attributes);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getTypeValueBoolean(R.attr.useStatusDarkColor)) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    protected Toolbar initToolbar(int resId){
        initStatusBar();
        Toolbar toolbar = findViewById(resId);
        if(toolbar != null) setSupportActionBar(toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        initActionBar();
        return toolbar;
    }

    protected void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            Drawable drawable = getTypeValueDrawable(R.attr.customBackArrowIcon);
            if(drawable == null) {
                actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
            }else{
                actionBar.setHomeAsUpIndicator(drawable);
            }
        }
        setTitle(getTitle());
    }

    protected Toolbar initToolbar(ViewDataBinding binding, int resId){
        initStatusBar();
        Toolbar toolbar = binding.getRoot().findViewById(resId);
        mToolbarTitle = binding.getRoot().findViewById(R.id.toolbar_title);
        if(toolbar != null){
            setSupportActionBar(toolbar);
        }
        initActionBar();
        return toolbar;
    }

    @Override
    public void setTitle(CharSequence title) {
        if(mToolbarTitle!=null){
            super.setTitle("");
            mToolbarTitle.setText(title);
        }else{
            super.setTitle(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        if(mToolbarTitle!=null){
            super.setTitle("");
            mToolbarTitle.setText(titleId);
        }else {
            super.setTitle(titleId);
        }
    }

}
