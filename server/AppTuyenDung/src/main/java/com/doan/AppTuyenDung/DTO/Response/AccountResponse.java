package com.doan.AppTuyenDung.DTO.Response;


import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class AccountResponse {
	 private int id;

	    private String phoneNumber;
	    private String codeStatusValue;
	    private int userId;
	    private Date createdAtUser;
	    private Date updatedAtUser;
	    private CodeResponse codeRoleAccount;
	    private UserResponse userAccountData;
		
	    private List<SkillIdRespones> listSkills;
}
