package com.daliy.txtreader.interfaces;

import com.daliy.txtreader.main.TxtReaderContext;

public interface ITxtTask {
    void Run(ILoadListener callBack, TxtReaderContext readerContext);
}
