package com.doan.AppTuyenDung.DTO.Request;


import java.util.Date;
import java.util.List;

import com.doan.AppTuyenDung.entity.CodeGender;
import com.doan.AppTuyenDung.entity.CodeRule;
import com.doan.AppTuyenDung.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {
	private int statusCode;
	private String error;
	private String message;
	private String token;
	private String refreshToken;
	private String expirationTime;
	private String phonenumber;
	private String roleCode;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String genderCode;
	private String image;
	@JsonIgnore
	private User User;
	@JsonIgnore
	private List<User> UsersList;
}
