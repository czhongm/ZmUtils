package net.childman.libmvvm.widget;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

public class SingleTextViewButton extends AppCompatTextView {
    public SingleTextViewButton(Context context) {
        super(context);
        init();
    }

    public SingleTextViewButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SingleTextViewButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Drawable[] drawables = getCompoundDrawables();
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        for(Drawable drawable : drawables) {
                            if(drawable != null) drawable.setColorFilter(0x33000000, PorterDuff.Mode.SRC_ATOP);
                        }
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        for(Drawable drawable : drawables) {
                            if(drawable != null) drawable.clearColorFilter();
                        }
                        invalidate();
                        break;
                }
                return false;
            }
        });
    }
}
