package com.doan.AppTuyenDung.DTO.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
	private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String addressUser;
    private CodeResponse genderCodeValue;
    private String image;
    private String dobUser;
    private Integer companyId;
    private UserSettingResponse userSettingData;
}
