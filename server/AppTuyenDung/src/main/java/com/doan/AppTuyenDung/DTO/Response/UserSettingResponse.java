package com.doan.AppTuyenDung.DTO.Response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSettingResponse {
	private int id;
    private CodeResponse categoryJobCode;
    private CodeResponse addressCode;
    private CodeResponse salaryJobCode;
    private CodeResponse experienceJobCode;
    private Boolean isTakeMail;
    private Boolean isFindJob;
    private Boolean isHiddenPercent;
    private String file;
    private int userId;
}
