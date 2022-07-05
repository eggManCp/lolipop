package com.bifan.txtreaderlib.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bifan.txtreaderlib.main.TxtReaderView;

/**
 * @author FengZhongChan
 * @date 2022/7/5 15:44
 */
public class ScreenStatusReceiver extends BroadcastReceiver {
    private final TxtReaderView mReaderView;
    private final String SCREEN_OFF = "android.intent.action.SCREEN_OFF";

    public ScreenStatusReceiver(TxtReaderView view) {
        mReaderView = view;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SCREEN_OFF.equals(intent.getAction())) {
            mReaderView.saveCurrentProgress();
        }
    }
}
