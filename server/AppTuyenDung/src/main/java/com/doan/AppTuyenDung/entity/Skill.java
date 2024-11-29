package com.doan.AppTuyenDung.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "skills")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String categoryJobCode;
	@JsonIgnore
    @OneToMany(mappedBy = "skill")
    private Set<UserSkill> userSkills;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategoryJobCode() {
		return categoryJobCode;
	}

	public void setCategoryJobCode(String categoryJobCode) {
		this.categoryJobCode = categoryJobCode;
	}

	public Set<UserSkill> getUserSkills() {
		return userSkills;
	}

	public void setUserSkills(Set<UserSkill> userSkills) {
		this.userSkills = userSkills;
	}

    // Getters and Setters
}