package com.daliy.txtreader.interfaces;


import com.daliy.txtreader.bean.TxtChar;

public interface ITextSelectListener {
    void onTextChanging(TxtChar firstSelectedChar, TxtChar lastSelectedChar);
    void onTextChanging(String selectText);
    void onTextSelected(String selectText);
}
