package com.daliy.txtreader.interfaces;

import com.daliy.txtreader.bean.TxtChar;

import java.util.List;

public interface IPage {
    ITxtLine getLine(int index);
    void addLine(ITxtLine line);
    void addLineTo(ITxtLine line, int index);
    void setLines(List<ITxtLine> lines);
    TxtChar getFirstChar();
    TxtChar getLastChar();
    ITxtLine getFirstLine();
    ITxtLine getLastLine();
    List<ITxtLine> getLines();
    ICursor<ITxtLine> getLineCursor();
    int getLineNum();
    boolean isFullPage();//是否满页了
    void setFullPage(boolean fullPage);
    int CurrentIndex();
    Boolean HasData();
}
