package com.doan.AppTuyenDung.DTO.Request;

import lombok.Data;

@Data
public class ApplyEmailRequest {
	private String userEmail;
    private String companyEmail;
    private String jobTitle;
    private String userName;
}
