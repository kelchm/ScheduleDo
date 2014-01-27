package com.kelchm.scheduledo.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by bperin
 */
public class ButtonAwesome extends Button {

    public ButtonAwesome(Context context) {
        super(context);
        init();

    }

    public ButtonAwesome(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ButtonAwesome(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/fontawesome-webfont.ttf");
        setTypeface(font);
    }
}

