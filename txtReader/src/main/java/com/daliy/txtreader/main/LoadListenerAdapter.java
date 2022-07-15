package com.daliy.txtreader.main;


import com.daliy.txtreader.bean.TxtMsg;
import com.daliy.txtreader.interfaces.ILoadListener;

public class LoadListenerAdapter implements ILoadListener {
    @Override
    public void onSuccess() {
    }

    @Override
    public void onFail(TxtMsg txtMsg) {
    }

    @Override
    public void onMessage(String message) {
    }
}
