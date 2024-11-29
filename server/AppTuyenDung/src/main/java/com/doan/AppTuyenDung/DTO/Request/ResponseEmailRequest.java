package com.doan.AppTuyenDung.DTO.Request;

import lombok.Data;

@Data
public class ResponseEmailRequest {
	private String userEmail;
    private String companyEmail;
    private String jobTitle;
    private String userName;
    private String statusApproved;
}
