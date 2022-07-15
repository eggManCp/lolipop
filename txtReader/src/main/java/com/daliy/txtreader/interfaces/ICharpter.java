package com.daliy.txtreader.interfaces;


public interface ICharpter {
    int getParagraphNum();
    int getStartParagraphIndex();
    int getEndParagraphIndex();
    String getTitle();
    void setStartParagraphIndex(int index);
    void setEndParagraphIndex(int index);
}
