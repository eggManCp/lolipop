package com.lolipop.reader.model;

/**
 * @author FengZhongChan
 * @date 2022/7/7 14:27
 */
public class ResultModel {
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean isSuccess() {
        return code == 0;
    }
}
