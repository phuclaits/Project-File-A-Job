package com.doan.AppTuyenDung.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
	UNCATEGORIZED_EXCEPTION(404, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least 12 characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    NOTEXISTCOMPANY(1009, "Công ty không tồn tại", HttpStatus.BAD_REQUEST),
    LISTISNULL(1010, "Danh sách rỗng", HttpStatus.BAD_REQUEST),
    ERRORCLOUD(1011, "Lỗi đường truyền lên Cloud!", HttpStatus.BAD_REQUEST),
    ERROROTP(1012, "Lỗi khi xác thực số điện thoại!", HttpStatus.BAD_REQUEST),
    CV_NOT_FIT(1013, "Lỗi Cv không phù hợp!", HttpStatus.BAD_REQUEST),
    COMPANY_NOT_FIT(1013, "Lỗi Công ty không phù hợp!", HttpStatus.BAD_REQUEST)
    ;
	private int code;
    private String message;
    private final HttpStatusCode statusCode;
    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public HttpStatusCode getStatusCode() {
		return statusCode;
	}
    
    
}