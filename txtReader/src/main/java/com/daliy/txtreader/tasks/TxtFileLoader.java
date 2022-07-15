package com.daliy.txtreader.tasks;


import com.daliy.txtreader.bean.FileReadRecordBean;
import com.daliy.txtreader.bean.TxtFileMsg;
import com.daliy.txtreader.bean.TxtMsg;
import com.daliy.txtreader.interfaces.ILoadListener;
import com.daliy.txtreader.main.FileReadRecordDB;
import com.daliy.txtreader.main.TxtReaderContext;
import com.daliy.txtreader.utils.ELogger;
import com.daliy.txtreader.utils.FileCharsetDetector;
import com.daliy.txtreader.utils.FileUtil;

import java.io.File;


public class TxtFileLoader {
    private String tag = "TxtFileLoader";
    private FileDataLoadTask mFileDataLoadTask;

    public void load(String filePath, TxtReaderContext readerContext, ILoadListener loadListener) {
        load(filePath, null, readerContext, loadListener);
    }

    public void load(String filePath, String fileName, TxtReaderContext readerContext, ILoadListener loadListener) {
        onStop();
        if (!FileUtil.FileExist(filePath)) {
            loadListener.onFail(TxtMsg.FileNoExist);
            return;
        }
        loadListener.onMessage("initFile start");
        initFile(filePath, fileName, readerContext);
        ELogger.log(tag, "initFile done");
        loadListener.onMessage("initFile done");
        mFileDataLoadTask = new FileDataLoadTask();
        mFileDataLoadTask.Run(loadListener, readerContext);

    }

    private void initFile(String filePath, String fileName, TxtReaderContext readerContext) {
        File file = new File(filePath);
        TxtFileMsg fileMsg = new TxtFileMsg();
        fileMsg.FileSize = file.getTotalSpace();
        fileMsg.FilePath = filePath;
        file.setLastModified(System.currentTimeMillis());
        fileMsg.FileCode = new FileCharsetDetector().getCharset(new File(filePath));

        fileMsg.CurrentParagraphIndex = 0;
        fileMsg.PreParagraphIndex = 0;
        fileMsg.PreCharIndex = 0;
        if (fileName == null || fileName.trim().length() == 0) {
            fileName = file.getName();
        }
        fileMsg.FileName = fileName;

        //获取之前的播放进度记录
        FileReadRecordDB readRecordDB = new FileReadRecordDB(readerContext.getContext());
        readRecordDB.createTable();
        try {
            FileReadRecordBean r = readRecordDB.getRecordByHashName(FileUtil.getMD5Checksum(filePath));
            if (r != null) {
                fileMsg.PreParagraphIndex = r.paragraphIndex;
                fileMsg.PreCharIndex = r.chartIndex;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        readRecordDB.createTable();
        readerContext.setFileMsg(fileMsg);
    }

    public void onStop() {
        if (mFileDataLoadTask != null) {
            mFileDataLoadTask.onStop();
        }
    }
}
