package net.childman.libmvvm.fragment;

import android.os.Bundle;

import com.trello.rxlifecycle3.components.support.RxFragment;

import androidx.annotation.Nullable;
import net.childman.libmvvm.common.UiAction;

public class BaseFragment extends RxFragment {
    protected UiAction mUiAction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUiAction();
    }
    protected void initUiAction(){
        mUiAction = new UiAction(getContext());
    }
}
