
package net.childman.libmvvm.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.CheckResult;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import net.childman.libmvvm.R;

public class Toasty {
    private Toasty() {
        // avoiding instantiation
    }

    @CheckResult
    public static Toast normal(@NonNull Context context, @StringRes int message) {
        return custom(context, context.getString(message), R.drawable.warn, Toast.LENGTH_SHORT);
    }

    @CheckResult
    public static Toast normal(@NonNull Context context, String message) {
        return custom(context, message, R.drawable.warn, Toast.LENGTH_SHORT);
    }

    public static void showNormal(@NonNull Context context, @NonNull Toast toast, String message){
        showCustom(context,toast,message,R.drawable.warn,Toast.LENGTH_SHORT);
    }

    @CheckResult
    public static Toast warning(@NonNull Context context, @StringRes int message) {
        return custom(context, context.getString(message), R.drawable.warn, Toast.LENGTH_SHORT);
    }

    @CheckResult
    public static Toast warning(@NonNull Context context, String message) {
        return custom(context, message, R.drawable.warn, Toast.LENGTH_SHORT);
    }

    @CheckResult
    public static Toast success(@NonNull Context context, @StringRes int message) {
        return custom(context, context.getString(message), R.drawable.succ, Toast.LENGTH_SHORT);
    }

    @CheckResult
    public static Toast error(@NonNull Context context, @StringRes int message) {
        return custom(context, context.getString(message), R.drawable.error, Toast.LENGTH_SHORT);
    }

    @SuppressLint("ShowToast")
    @CheckResult
    public static Toast custom(@NonNull Context context, @NonNull CharSequence message, @DrawableRes int resIcon,
                               int duration) {
        final Toast currentToast = new Toast(context);
        currentToast.setDuration(duration);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(inflater == null) return null;
        final View toastLayout = inflater.inflate(R.layout.toast, null);
        final TextView toastTextView = toastLayout.findViewById(R.id.text);
        Drawable drawableFrame = ContextCompat.getDrawable(context,resIcon);
        if(drawableFrame == null) return null;
        drawableFrame.setBounds(0, 0, drawableFrame.getIntrinsicWidth(), (int) (drawableFrame.getMinimumHeight()));
        toastTextView.setText(message);
        toastTextView.setCompoundDrawables(null,drawableFrame,null,null);
        currentToast.setView(toastLayout);
        currentToast.setGravity(Gravity.CENTER,0,0);
        return currentToast;
    }

    public static void showCustom(@NonNull Context context, @NonNull Toast toast, @NonNull CharSequence message, @DrawableRes int resIcon,
                                  int duration) {
        final View toastLayout = toast.getView();
        final TextView toastTextView = toastLayout.findViewById(R.id.text);
        Drawable drawableFrame = ContextCompat.getDrawable(context,resIcon);
        if(drawableFrame == null) return;
        drawableFrame.setBounds(0, 0, drawableFrame.getIntrinsicWidth(), (int) (drawableFrame.getMinimumHeight()));
        toastTextView.setText(message);
        toastTextView.setCompoundDrawables(null,drawableFrame,null,null);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(duration);
        toast.show();
    }

}
