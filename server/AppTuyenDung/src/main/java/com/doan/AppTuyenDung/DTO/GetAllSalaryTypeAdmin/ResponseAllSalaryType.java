package com.doan.AppTuyenDung.DTO.GetAllSalaryTypeAdmin;

public class ResponseAllSalaryType <T>{
    private int errCode;
    private String errMessage;
    private T data;


    
    public ResponseAllSalaryType(int errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public ResponseAllSalaryType() {
    }

    public ResponseAllSalaryType(int errCode, String errMessage, T data) {
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
