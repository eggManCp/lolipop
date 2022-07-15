package com.daliy.txtreader.interfaces;


public interface IChapter {

    int getIndex();

    int getStartParagraphIndex();

    int getEndParagraphIndex();

    int getStartCharIndex();

    int getEndCharIndex();

    int getStartIndex();

    String getTitle();

    void setStartParagraphIndex(int index);

    void setEndParagraphIndex(int index);

}
