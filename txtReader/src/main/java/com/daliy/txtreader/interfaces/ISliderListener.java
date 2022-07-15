package com.daliy.txtreader.interfaces;


import com.daliy.txtreader.bean.TxtChar;

public interface ISliderListener {
    void onShowSlider(TxtChar txtChar);
    void onShowSlider(String CurrentSelectedText);
    void onReleaseSlider();
}
