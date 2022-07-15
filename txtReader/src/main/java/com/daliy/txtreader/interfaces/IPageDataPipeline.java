package com.daliy.txtreader.interfaces;

public interface IPageDataPipeline {
    IPage getPageStartFromProgress(int paragraphIndex, int charIndex);
    IPage getPageEndToProgress(int paragraphIndex, int charIndex);
}
