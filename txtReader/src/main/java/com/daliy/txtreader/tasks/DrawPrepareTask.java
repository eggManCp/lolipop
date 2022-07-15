package com.daliy.txtreader.tasks;

import android.content.Context;
import android.graphics.Color;

import com.daliy.txtreader.interfaces.ILoadListener;
import com.daliy.txtreader.interfaces.ITxtTask;
import com.daliy.txtreader.main.PaintContext;
import com.daliy.txtreader.main.TxtConfig;
import com.daliy.txtreader.main.TxtReaderContext;
import com.daliy.txtreader.utils.ELogger;


public class DrawPrepareTask implements ITxtTask {
    private String tag = "DrawPrepareTask";

    @Override
    public void Run(ILoadListener callBack, TxtReaderContext readerContext) {
        callBack.onMessage("start do DrawPrepare");
        ELogger.log(tag, "do DrawPrepare");
        initPainContext(readerContext.context,readerContext.getPaintContext(), readerContext.getTxtConfig());
        readerContext.getPaintContext().textPaint.setColor(Color.WHITE);
        ITxtTask txtTask = new BitmapProduceTask();
        txtTask.Run(callBack, readerContext);
    }

    private void initPainContext(Context context, PaintContext paintContext, TxtConfig txtConfig) {
        TxtConfigInitTask.initPainContext(context,paintContext, txtConfig);
    }
}
