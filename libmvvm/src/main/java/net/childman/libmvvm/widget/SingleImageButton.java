package net.childman.libmvvm.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import net.childman.libmvvm.R;

public class SingleImageButton extends AppCompatImageView {
    private int pressedColor = 0x33000000;

    public SingleImageButton(Context context) {
        super(context);
        init();
    }

    public SingleImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        obtainColor(attrs,0);
        init();
    }

    public SingleImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainColor(attrs,defStyleAttr);
        init();
    }

    private void obtainColor(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SingleImageButton, defStyleAttr, 0);
        pressedColor = a.getColor(R.styleable.SingleImageButton_pressedColor,0x33000000);
        a.recycle();
    }

    private void init() {
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        setColorFilter(pressedColor, PorterDuff.Mode.SRC_IN);
                        invalidate();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        getDrawable().clearColorFilter();
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        view.performClick();
                        clearColorFilter();
                        invalidate();
                        break;
                }
                return true;
            }
        });
    }
}
