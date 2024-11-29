package com.doan.AppTuyenDung.DTO.Response;

import java.util.Date;

import lombok.Data;

@Data
public class CvByPostResponse {
	Integer idCv;
	String lastName;
	String firstName;
	String phoneNumber;
	String status;
	Date createAt;
}
