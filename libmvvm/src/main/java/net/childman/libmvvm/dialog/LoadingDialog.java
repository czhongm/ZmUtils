package net.childman.libmvvm.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import net.childman.libmvvm.R;


public class LoadingDialog extends Dialog {
    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{

        private Context context;
        private String message;
        private boolean isShowMessage=true;
        private boolean isCancelable=false;
        private boolean isCancelOutside=false;


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

        public LoadingDialog create(){

            LayoutInflater inflater = LayoutInflater.from(context);
            View view=inflater.inflate(R.layout.dialog_loading,null);
            LoadingDialog loadingDailog=new LoadingDialog(context,R.style.MyDialogStyle);
            TextView msgText= view.findViewById(R.id.tv_prompt);
            if(isShowMessage){
                if(message != null) {
                    msgText.setText(message);
                }
            }else{
                msgText.setVisibility(View.GONE);
            }
            loadingDailog.setContentView(view);
            loadingDailog.setCancelable(isCancelable);
            loadingDailog.setCanceledOnTouchOutside(isCancelOutside);
            return loadingDailog;
        }
    }

    public void setMessage(String message){
        TextView msgText = this.findViewById(R.id.tv_prompt);
        msgText.setText(message);
    }

    public void setMessage(@StringRes int message){
        TextView msgText = this.findViewById(R.id.tv_prompt);
        msgText.setText(message);
    }
}
