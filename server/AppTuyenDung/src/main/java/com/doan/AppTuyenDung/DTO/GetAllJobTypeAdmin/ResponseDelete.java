package com.doan.AppTuyenDung.DTO.GetAllJobTypeAdmin;

public class ResponseDelete {
    private int errCode;
    private String errMessage;

    public ResponseDelete() {
    }

    public ResponseDelete(int errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    // Getters và Setters
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
}
