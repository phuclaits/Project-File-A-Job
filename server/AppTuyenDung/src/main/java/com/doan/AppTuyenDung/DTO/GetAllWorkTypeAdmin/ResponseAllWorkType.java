package com.doan.AppTuyenDung.DTO.GetAllWorkTypeAdmin;

public class ResponseAllWorkType<T> {
    private int errCode;
    private String errMessage;
    private T data;


    
    public ResponseAllWorkType(int errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public ResponseAllWorkType() {
    }

    public ResponseAllWorkType(int errCode, String errMessage, T data) {
        this.errCode = errCode;
        this.errMessage = errMessage;
        this.data = data;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
