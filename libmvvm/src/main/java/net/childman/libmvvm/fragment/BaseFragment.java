package net.childman.libmvvm.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;

import com.trello.rxlifecycle4.components.support.RxFragment;

import net.childman.libmvvm.common.UiAction;

public class BaseFragment extends RxFragment {
    protected UiAction mUiAction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUiAction();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition transition = TransitionInflater.from(requireContext())
                    .inflateTransition(android.R.transition.move);
            setSharedElementEnterTransition(transition);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUiAction = null;
    }

    protected void initUiAction() {
        mUiAction = new UiAction(getContext());
    }
}
