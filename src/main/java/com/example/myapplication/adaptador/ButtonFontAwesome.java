package com.example.myapplication.adaptador;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ButtonFontAwesome extends androidx.appcompat.widget.AppCompatButton {
    public ButtonFontAwesome(@NonNull Context context) {
        super (context);
        init();
    }

    public ButtonFontAwesome(@NonNull Context context, @Nullable AttributeSet attrs) {
        super (context, attrs);
        init();
    }

    public ButtonFontAwesome(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        Typeface tf = Typeface.createFromAsset(getContext ().getAssets(),"fonts/FontAwesome.ttf");
        setTypeface(tf);
    }
}
