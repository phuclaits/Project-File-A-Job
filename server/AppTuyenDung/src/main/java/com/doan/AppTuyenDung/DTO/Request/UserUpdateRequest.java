package com.doan.AppTuyenDung.DTO.Request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private Integer id;
	private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String genderCode;
    private String image;
    private String dob;
}
