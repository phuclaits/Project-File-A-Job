package com.doan.AppTuyenDung.DTO.Request;

import lombok.Data;

@Data
public class ChangePasswordForgotPassword {
    public String phonenumber;
	public String newPassword;
}
