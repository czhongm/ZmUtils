package net.childman.libmvvm.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import net.childman.libmvvm.R;

public class ProgressDialog extends Dialog {
    private ProgressBar mProgressBar;
    private TextView mMsgView;
    private TextView mSubMsgView;

    public ProgressDialog(@NonNull Context context) {
        super(context);
    }

    public ProgressDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{

        private Context context;
        private String message;
        private boolean isShowMessage=true;
        private boolean isCancelable=false;
        private boolean isCancelOutside=false;
        private DialogInterface.OnCancelListener mOnCancelListener;


        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置提示信息
         * @param message
         * @return
         */

        public Builder setMessage(String message){
            this.message=message;
            return this;
        }

        public Builder setMessage(@StringRes int message){
            this.message=context.getString(message);
            return this;
        }

        /**
         * 设置是否显示提示信息
         * @param isShowMessage
         * @return
         */
        public Builder setShowMessage(boolean isShowMessage){
            this.isShowMessage=isShowMessage;
            return this;
        }

        /**
         * 设置是否可以按返回键取消
         * @param isCancelable
         * @return
         */

        public Builder setCancelable(boolean isCancelable){
            this.isCancelable=isCancelable;
            return this;
        }

        /**
         * 设置是否可以取消
         * @param isCancelOutside
         * @return
         */
        public Builder setCancelOutside(boolean isCancelOutside){
            this.isCancelOutside=isCancelOutside;
            return this;
        }

        public Builder setOnCancelListener(DialogInterface.OnCancelListener listener){
            mOnCancelListener = listener;
            return this;
        }

        public ProgressDialog create(){

            LayoutInflater inflater = LayoutInflater.from(context);
            View view=inflater.inflate(R.layout.dialog_progress,null);
            ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyDialogStyle);
            TextView msgText= view.findViewById(R.id.title);
            if(isShowMessage){
                if(message != null) {
                    msgText.setText(message);
                }
            }else{
                msgText.setVisibility(View.GONE);
            }
            progressDialog.setContentView(view);
            progressDialog.setCancelable(isCancelable);
            progressDialog.setCanceledOnTouchOutside(isCancelOutside);
            if(mOnCancelListener != null) progressDialog.setOnCancelListener(mOnCancelListener);
            return progressDialog;
        }
    }

    public void setMessage(String message){
        if(mMsgView == null) {
            mMsgView = findViewById(R.id.title);
        }
        if(mMsgView != null) mMsgView.setText(message);
    }

    public void setMessage(@StringRes int message){
        if(mMsgView == null) {
            mMsgView = findViewById(R.id.title);
        }
        if(mMsgView != null) mMsgView.setText(message);
    }

    public void setSubTitle(String msg){
        if(mSubMsgView == null){
            mSubMsgView = findViewById(R.id.sub_title);
        }
        if(mSubMsgView != null) mSubMsgView.setText(msg);
    }

    public ProgressBar getProgressBar(){
        if(mProgressBar == null) {
            mProgressBar = findViewById(R.id.progress_bar);
        }
        return mProgressBar;
    }

    @Override
    public void show() {
        super.show();

        Window window = getWindow();
        if(window != null) {
            window.setGravity(Gravity.CENTER);
            window.getDecorView().setPadding(50, 0, 50, 0);

            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }
    }
}
