package com.doan.AppTuyenDung.DTO.Request;

import com.doan.AppTuyenDung.entity.CodeGender;
import com.doan.AppTuyenDung.entity.CodeStatus;

import lombok.Data;

@Data
public class ProfileUserRequest {
	private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String gender;
    private String image;
    private String dob;
    private String phonenumber;
    private String status;
}
