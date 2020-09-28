package net.childman.libmvvm.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.trello.rxlifecycle4.components.support.RxFragment;

import net.childman.libmvvm.common.UiAction;

public class BaseFragment extends RxFragment {
    protected UiAction mUiAction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUiAction();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUiAction = null;
    }

    protected void initUiAction(){
        mUiAction = new UiAction(getContext());
    }
}
