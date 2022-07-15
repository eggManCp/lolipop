package com.daliy.txtreader.bean;

import android.graphics.Color;

public class NumChar extends TxtChar {
    public static  int DefaultTextColor = Color.parseColor("#45a1cf");
    public NumChar(char aChar) {
        super(aChar);
    }
    @Override
    public int getTextColor() {
        return DefaultTextColor;
    }

    @Override
    public int getCharType() {
        return Char_Num;
    }
}
