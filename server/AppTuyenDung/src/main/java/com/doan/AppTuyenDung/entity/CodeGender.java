package com.doan.AppTuyenDung.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
@Entity

@Table(name = "CodeGender")
public class CodeGender {
    @Id
    private String code;
    private String type;
    private String value;
    private String image;
	@JsonIgnore
	@OneToMany(mappedBy = "genderCode")
    private List<User> users;

	public CodeGender(String code, String type, String value, String image, List<User> users) {
		this.code = code;
		this.type = type;
		this.value = value;
		this.image = image;
		this.users = users;
	}
	public CodeGender() {
	}
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
