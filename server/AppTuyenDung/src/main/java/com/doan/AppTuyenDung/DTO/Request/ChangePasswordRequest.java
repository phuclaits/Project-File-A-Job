package com.doan.AppTuyenDung.DTO.Request;

public class ChangePasswordRequest {
	public Integer id;
	public String oldpassword;
	public String password;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOldpassword() {
		return oldpassword;
	}

	public void setOldpassword(String oldpassword) {
		this.oldpassword = oldpassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ChangePasswordRequest(Integer id, String oldpassword, String password) {
		this.id = id;
		this.password = password;
	}

	public ChangePasswordRequest() {
	}

	// Getters and Setters

}