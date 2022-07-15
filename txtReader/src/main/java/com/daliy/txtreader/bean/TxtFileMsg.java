package com.daliy.txtreader.bean;


public class TxtFileMsg {
    public String FilePath;
    public String FileName;
    public String HexName;
    public long FileSize;
    public int  CurrentParagraphIndex;
    public int  CurrentCharIndex;
    public int PreParagraphIndex =-1;
    public int PreCharIndex = -1;
    public String FileCode;

}
