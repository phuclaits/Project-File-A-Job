package com.doan.AppTuyenDung.DTO.Request;

import java.util.List;

import lombok.Data;

@Data
public class UserSettingDTO {
	private Integer idUser;
    private String categoryJobCode;
    private String salaryJobCode;
    private String addressCode;
    private String experienceJobCode;
    private Boolean isFindJob;
    private Boolean isTakeMail;
    private byte[] file;
    private List<Integer> listSkills;
}
