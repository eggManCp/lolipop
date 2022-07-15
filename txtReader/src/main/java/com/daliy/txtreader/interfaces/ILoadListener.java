package com.daliy.txtreader.interfaces;

import com.daliy.txtreader.bean.TxtMsg;

public interface ILoadListener {
    void onSuccess();
    void onFail(TxtMsg txtMsg);
    void onMessage(String message);
}
