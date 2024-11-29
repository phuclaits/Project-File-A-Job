package com.doan.AppTuyenDung.DTO;

import lombok.Data;

@Data
public class CreateUserByEmployeerDTO {
    private String phonenumber;
    private String lastName;
    private String firstName;
    private String password;
    private String image;
    private String address;
    private String genderCode;
    private String dob;
    private Integer companyId;
    private String email;
    private String roleCode;
}
