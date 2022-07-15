package com.daliy.txtreader.utils;

import android.util.Log;

import com.daliy.txtreader.interfaces.ITxtReaderLoggerListener;
import com.daliy.txtreader.main.TxtConfig;


/**
 * Created by HP on 2017/11/15.
 */

public class ELogger {
    private static ITxtReaderLoggerListener l;
    public static void setLoggerListener(ITxtReaderLoggerListener l) {
        ELogger.l = l;
    }
    public static void log(String tag, String msg) {
        if(TxtConfig.DebugMode) {
            Log.e(tag, msg + "");
            if (l != null) {
                l.onLog(tag, msg + "");
            }
        }
    }

}
