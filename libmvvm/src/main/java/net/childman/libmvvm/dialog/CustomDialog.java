package net.childman.libmvvm.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.childman.libmvvm.R;

public class CustomDialog extends Dialog {
    public CustomDialog(@NonNull Context context) {
        super(context);
    }

    public CustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private final Context context;
        private boolean cancelable = true;
        private int layoutRes;
        private OnCancelListener mOnCancelListener;
        private OnBindView mOnBindView;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setLayout(@LayoutRes int layoutRes){
            this.layoutRes = layoutRes;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            mOnCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnBindView(OnBindView onBindView) {
            mOnBindView = onBindView;
            return this;
        }

        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater == null) return null;

            final CustomDialog dialog = new CustomDialog(context, R.style.MyDialogStyle);
            View layout = inflater.inflate(layoutRes, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            dialog.setCancelable(cancelable);
            dialog.setOnCancelListener(mOnCancelListener);
            if(mOnBindView!=null){
                mOnBindView.onBind(dialog,layout);
            }
            return dialog;
        }

    }

    public interface OnBindView {
        void onBind(CustomDialog dialog, View v);
    }
}
