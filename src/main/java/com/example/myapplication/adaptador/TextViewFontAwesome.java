package com.example.myapplication.adaptador;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TextViewFontAwesome extends androidx.appcompat.widget.AppCompatTextView {

    public TextViewFontAwesome(@NonNull Context context) {
        super (context);
        init();
    }

    public TextViewFontAwesome(@NonNull Context context, @Nullable AttributeSet attrs) {
        super (context, attrs);
        init();
    }

    public TextViewFontAwesome(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        Typeface tf = Typeface.createFromAsset(getContext ().getAssets(),"fonts/fa-solid-900.ttf");
        setTypeface(tf);
    }
}