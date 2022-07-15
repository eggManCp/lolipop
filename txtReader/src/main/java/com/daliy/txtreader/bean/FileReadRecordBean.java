package com.daliy.txtreader.bean;

public class FileReadRecordBean {
    public String fileHashName;
    public int id;
    public String fileName;
    public String filePath;
    public int paragraphIndex;
    public int chartIndex;

    @Override
    public String toString() {
        return "FileReadRecordBean{" +
                "fileHashName='" + fileHashName + '\'' +
                ", id=" + id +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", paragraphIndex='" + paragraphIndex + '\'' +
                ", chartIndex='" + chartIndex + '\'' +
                '}';
    }
}
