package com.daliy.txtreader.tasks;

import com.daliy.txtreader.interfaces.IChapter;
import com.daliy.txtreader.interfaces.ILoadListener;
import com.daliy.txtreader.interfaces.IParagraphData;
import com.daliy.txtreader.interfaces.ITxtTask;
import com.daliy.txtreader.main.ParagraphData;
import com.daliy.txtreader.main.TxtReaderContext;
import com.daliy.txtreader.utils.ELogger;

import java.util.ArrayList;
import java.util.List;



public class TextLoader {
    private final String tag = "FileDataLoadTask";

    public void load(String text, TxtReaderContext readerContext, ILoadListener callBack) {
        IParagraphData paragraphData = new ParagraphData();
        List<IChapter> chapter = new ArrayList<>();
        callBack.onMessage("start read text");
        ELogger.log(tag, "start read text");
        paragraphData.addParagraph(text + "");
        readerContext.setParagraphData(paragraphData);
        readerContext.setChapters(chapter);
        ITxtTask txtTask = new TxtConfigInitTask();
        txtTask.Run(callBack, readerContext);
    }
}
