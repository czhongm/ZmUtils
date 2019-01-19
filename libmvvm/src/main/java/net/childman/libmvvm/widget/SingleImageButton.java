package net.childman.libmvvm.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

public class SingleImageButton extends AppCompatImageView {
    public SingleImageButton(Context context) {
        super(context);
        init();
    }

    public SingleImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SingleImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        getDrawable().setColorFilter(0x33000000, PorterDuff.Mode.SRC_ATOP);
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        getDrawable().clearColorFilter();
                        invalidate();
                        break;
                }
                return false;
            }
        });
    }
}
