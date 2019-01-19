package net.childman.libmvvm.common;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import net.childman.libmvvm.R;
import net.childman.libmvvm.viewmodel.BaseViewModel;

public class DataBindingUiAction extends UiAction {
    public DataBindingUiAction(Context context) {
        super(context);
    }
    private void fixMsgData(@Nullable BaseViewModel.MsgData msgData){
        if(msgData == null) return;
        if(msgData.getMsg() == null && msgData.getMsgResId()==0){
            msgData.setMsgResId(R.string.error_unknown);
        }
    }

    protected void toastAction(BaseViewModel.MsgData msgData){
        if(msgData == null) return;
        if(msgData.getMsg() != null){
            showToast(msgData.getMsg());
        }else{
            int msgId = msgData.getMsgResId();
            if(msgId == 0){
                msgId = R.string.error_unknown;
            }
            showToast(msgId);
        }
    }

    protected void errorAction(BaseViewModel.MsgData msgData){
        if(msgData == null) {
            return;
        }
        if(msgData.getMsg() != null){
            showWarn(msgData.getMsg());
        }else{
            int msgId = msgData.getMsgResId();
            if(msgId == 0){
                msgId = R.string.error_unknown;
            }
            showWarn(msgId);
        }
    }

    protected void loadingAction(BaseViewModel.MsgData msgData){
        if(msgData == null){
            hideLoading();
        }else{
            if(msgData.getMsg() != null){
                showLoading(msgData.getMsg());
            }else{
                int msgId = msgData.getMsgResId();
                if(msgId == 0){
                    msgId = R.string.error_unknown;
                }
                showLoading(msgId);
            }
        }
    }

    @Override
    public void listenEvent(BaseViewModel viewModel, LifecycleOwner owner){
        viewModel.getTipEvent().observe(owner, new Observer<BaseViewModel.MsgData>() {
            @Override
            public void onChanged(@Nullable BaseViewModel.MsgData msgData) {
                fixMsgData(msgData);
                toastAction(msgData);
            }
        });

        viewModel.getErrorEvent().observe(owner, new Observer<BaseViewModel.MsgData>() {
            @Override
            public void onChanged(@Nullable BaseViewModel.MsgData msgData) {
                fixMsgData(msgData);
                errorAction(msgData);
            }
        });

        viewModel.getUploadEvent().observe(owner, new Observer<BaseViewModel.MsgData>() {
            @Override
            public void onChanged(@Nullable BaseViewModel.MsgData msgData) {
                fixMsgData(msgData);
                loadingAction(msgData);
            }
        });
    }
}
