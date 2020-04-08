package net.childman.libmvvm.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import net.childman.libmvvm.R;

public class SingleTextViewButton extends AppCompatTextView {
    private int pressedColor = 0x33000000;

    public SingleTextViewButton(Context context) {
        super(context);
        init();
    }

    public SingleTextViewButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        obtainColor(attrs,0);
        init();
    }

    public SingleTextViewButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainColor(attrs,defStyleAttr);
        init();
    }

    private void obtainColor(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SingleTextViewButton, defStyleAttr, 0);
        pressedColor = a.getColor(R.styleable.SingleTextViewButton_pressedColor,0x33000000);
        a.recycle();
    }

    private void init() {
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Drawable[] drawables = getCompoundDrawables();
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        for(Drawable drawable : drawables) {
                            if(drawable != null) drawable.mutate().setColorFilter(pressedColor, PorterDuff.Mode.SRC_IN);
                        }
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        view.performClick();
                    case MotionEvent.ACTION_CANCEL:
                        for(Drawable drawable : drawables) {
                            if(drawable != null) drawable.clearColorFilter();
                        }
                        invalidate();
                        break;
                }
                return true;
            }
        });
    }
}
