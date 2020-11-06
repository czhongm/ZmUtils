package net.childman.libmvvm.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import net.childman.libmvvm.R;

public class PasswordEditText extends AppCompatEditText {
    private final static float ALPHA_ENABLED_DARK = .54f;
    private final static float ALPHA_DISABLED_DARK = .38f;
    private final static float ALPHA_ENABLED_LIGHT = 1f;
    private final static float ALPHA_DISABLED_LIGHT = .5f;
    private Drawable eye;
    private Drawable eyeWithStrike;
    private int alphaEnabled;
    private int alphaDisabled;
    private boolean visible = false;
    private boolean useStrikeThrough = false;
    private Typeface typeface;
    private boolean drawableClick;

    public PasswordEditText(Context context) {
        super(context);
        init(null);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.PasswordView,
                    0, 0);
            try {
                useStrikeThrough = a.getBoolean(R.styleable.PasswordView_useStrikeThrough, false);
                eye = a.getDrawable(R.styleable.PasswordView_showDrawable);
                eyeWithStrike = a.getDrawable(R.styleable.PasswordView_hideDrawable);
            } finally {
                a.recycle();
            }
        }

        int enabledColor = resolveAttr(android.R.attr.textColorPrimary);
        boolean isIconDark = isDark(enabledColor);
        alphaEnabled = (int) (255 * (isIconDark ? ALPHA_ENABLED_DARK : ALPHA_ENABLED_LIGHT));
        alphaDisabled = (int) (255 * (isIconDark ? ALPHA_DISABLED_DARK : ALPHA_DISABLED_LIGHT));
        if(eye == null) {
            eye = getToggleDrawable(getContext(), R.drawable.ic_show_password, enabledColor);
        }
        if(eyeWithStrike == null) {
            eyeWithStrike = getToggleDrawable(getContext(), R.drawable.ic_hidden_password, enabledColor);
            eyeWithStrike.setAlpha(alphaEnabled);
        }
        setup();
    }

    private @ColorInt int resolveAttr(@AttrRes int attrRes) {
        TypedValue ta = new TypedValue();
        getContext().getTheme().resolveAttribute(attrRes, ta, true);
        return ContextCompat.getColor(getContext(), ta.resourceId);
    }

    private Drawable getToggleDrawable(Context context, @DrawableRes int drawableResId, @ColorInt int tint) {
        // Make sure to mutate so that if there are multiple password fields, they can have
        // different visibilities.
        Drawable drawable = getVectorDrawableWithIntrinsicBounds(context, drawableResId).mutate();
        DrawableCompat.setTint(drawable, tint);
        return drawable;
    }

    private Drawable getVectorDrawableWithIntrinsicBounds(Context context, @DrawableRes int drawableResId) {
        Drawable drawable = getVectorDrawable(context, drawableResId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return drawable;
    }

    private Drawable getVectorDrawable(Context context, @DrawableRes int drawableResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.getDrawable(context, drawableResId);
        } else {
            return VectorDrawableCompat.create(context.getResources(), drawableResId, context.getTheme());
        }
    }

    protected void setup() {
        int start = getSelectionStart();
        int end = getSelectionEnd();
        setInputType(InputType.TYPE_CLASS_TEXT | (visible ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_TEXT_VARIATION_PASSWORD));
        setSelection(start, end);
        Drawable drawable = useStrikeThrough && !visible ? eyeWithStrike : eye;
        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawable, drawables[3]);
        eye.setAlpha(visible && !useStrikeThrough ? alphaEnabled : alphaDisabled);
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        int drawableRightX = getWidth() - getPaddingRight();
        int drawableLeftX = drawableRightX - getCompoundDrawables()[2].getBounds().width();
        boolean eyeClicked = event.getX() >= drawableLeftX && event.getX() <= drawableRightX;

        if (event.getAction() == MotionEvent.ACTION_DOWN && eyeClicked) {
            drawableClick = true;
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (eyeClicked && drawableClick) {
                drawableClick = false;
                visible = !visible;
                setup();
                invalidate();
                return true;
            } else {
                drawableClick = false;
            }
        }

        return super.onTouchEvent(event);
    }

    @Override public void setInputType(int type) {
        this.typeface = getTypeface();
        super.setInputType(type);
        setTypeface(typeface);
    }

    @Override public void setError(CharSequence error) {
        throw new RuntimeException("Please use TextInputLayout.setError() instead!");
    }

    @Override public void setError(CharSequence error, Drawable icon) {
        throw new RuntimeException("Please use TextInputLayout.setError() instead!");
    }

    public void setUseStrikeThrough(boolean useStrikeThrough) {
        this.useStrikeThrough = useStrikeThrough;
    }

    private static boolean isDark(float[] hsl) {
        return hsl[2] < 0.5f;
    }

    public static boolean isDark(@ColorInt int color) {
        float[] hsl = new float[3];
        ColorUtils.colorToHSL(color, hsl);
        return isDark(hsl);
    }
}
