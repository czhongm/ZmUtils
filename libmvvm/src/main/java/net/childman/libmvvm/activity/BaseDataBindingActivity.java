package net.childman.libmvvm.activity;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import net.childman.libmvvm.BR;
import net.childman.libmvvm.R;
import net.childman.libmvvm.common.DataBindingUiAction;
import net.childman.libmvvm.dialog.ConfirmDialog;
import net.childman.libmvvm.viewmodel.BaseViewModel;

public abstract class BaseDataBindingActivity<T extends BaseViewModel,E extends ViewDataBinding> extends BaseActivity {
    protected T mViewModel;
    protected E mDataBinding;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(getViewModelClass());
        mDataBinding = DataBindingUtil.setContentView(this,getLayoutRes());
        mDataBinding.setVariable(BR.viewModel,mViewModel);
        mDataBinding.setLifecycleOwner(this);

        initData();
        initView();
        listenEvent();
    }

    @Override
    protected void initUiAction() {
        mUiAction = new DataBindingUiAction(this);
    }

    /**
     * 获取布局Id
     * @return layoutRes
     */
    protected abstract @LayoutRes int getLayoutRes();

    /**
     * 获取ViewModel class
     * @return viewmodel class
     */
    protected abstract Class<? extends T> getViewModelClass();


    protected void initData() {

    }

    protected void initView() {
        initToolbar(mDataBinding, R.id.toolbar);
    }

    /**
     * 监听事件
     */
    protected void listenEvent(){
        if(mViewModel == null) return;
        mUiAction.listenEvent(mViewModel,this);

        mViewModel.getRedirectLoginEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                gotoLogin();
            }
        });
    }

    /**
     * 去到重新登陆
     */
    protected void gotoLogin() {
//        Intent intent = new Intent(this,LoginActivity.class);
//        startActivity(intent);
//        ActivityCollector.finishAll();
    }

    @Override
    public void onBackPressed() {
        if(mViewModel.isChanged()){ //如果有变动
            ConfirmDialog dialog = new ConfirmDialog.Builder(this)
                    .setTitle(R.string.not_save_title)
                    .setMessage(R.string.not_save_msg)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
            dialog.show();
        }else {
            super.onBackPressed();
        }
    }
}
