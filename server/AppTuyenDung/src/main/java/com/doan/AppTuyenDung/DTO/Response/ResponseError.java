package com.doan.AppTuyenDung.DTO.Response;




public class ResponseError extends ResponseData {

    public ResponseError(int status, String message) {
        super(status, message);
    }
}