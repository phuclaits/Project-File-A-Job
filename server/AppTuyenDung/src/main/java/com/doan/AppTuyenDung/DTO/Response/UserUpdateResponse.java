package com.doan.AppTuyenDung.DTO.Response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class UserUpdateResponse {
	private Integer id;
    private String lastName;
    private String firstName;
    private String image;
    private String email;
    private String codeRoleAccount;
    private String addressUser;
    private Integer idCompany;
    private String phoneNumber;
    private String genderCodeValue;
    private String codeStatusValue;
    private String codeRoleValue;
    private String createdAtUser;
    private String dobUser;
}
