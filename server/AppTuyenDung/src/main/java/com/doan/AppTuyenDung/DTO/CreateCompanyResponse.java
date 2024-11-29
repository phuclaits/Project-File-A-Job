package com.doan.AppTuyenDung.DTO;

public class CreateCompanyResponse {
    private int errCode;
    private String errMessage;
    private Integer companyId;

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

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public CreateCompanyResponse() {
    }

    public CreateCompanyResponse(int errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public CreateCompanyResponse(int errCode, String errMessage, Integer companyId) {
        this.errCode = errCode;
        this.errMessage = errMessage;
        this.companyId = companyId;
    }
}
