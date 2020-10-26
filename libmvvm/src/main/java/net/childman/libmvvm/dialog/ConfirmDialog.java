package net.childman.libmvvm.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import net.childman.libmvvm.R;


public class ConfirmDialog extends Dialog {

    public ConfirmDialog(@NonNull Context context) {
        super(context);
    }

    public ConfirmDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private final Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;
        private boolean cancelable = true;
        private int layoutRes = R.layout.dialog_confirm;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setLayout(@LayoutRes int layoutRes){
            this.layoutRes = layoutRes;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public ConfirmDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater == null) return null;

            final ConfirmDialog dialog = new ConfirmDialog(context, R.style.MyDialogStyle);
            View layout = inflater.inflate(layoutRes, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView tvTitle = layout.findViewById(R.id.title);
            TextView tvMessage = layout.findViewById(R.id.message);
            TextView tvOkBtn = layout.findViewById(R.id.btn_ok);
            TextView tvCancelBtn = layout.findViewById(R.id.btn_cancel);
            LinearLayout lyContent = layout.findViewById(R.id.dialog_content);
            View verLine = layout.findViewById(R.id.ver_line);

            tvTitle.setText(title);
            if (positiveButtonText != null) {
                tvOkBtn.setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    tvOkBtn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            } else {
                tvOkBtn.setVisibility(View.GONE);
                verLine.setVisibility(View.GONE);
            }

            if (negativeButtonText != null) {
                tvCancelBtn.setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    tvCancelBtn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            negativeButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                tvCancelBtn.setVisibility(View.GONE);
                verLine.setVisibility(View.GONE);
            }
            if (message != null) {
                tvMessage.setText(message);
            } else if (contentView != null) {
                lyContent.removeAllViews();
                lyContent.addView(
                        contentView, new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
            }
            dialog.setContentView(layout);
            dialog.setCancelable(cancelable);
            dialog.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    if(negativeButtonClickListener != null){
                        negativeButtonClickListener.onClick(dialog,
                                DialogInterface.BUTTON_NEGATIVE);
                    }
                }
            });
            return dialog;
        }

    }

//    @Override
//    public void show() {
//        super.show();
//
//        Window window = this.getWindow();
//        if (window == null) return;
//        WindowManager windowManager = window.getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        Point pt = new Point();
//        display.getSize(pt);
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.width = (int) (pt.x * 0.75); //设置宽度
//        window.setAttributes(lp);
//    }
}
