package com.doan.AppTuyenDung.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;
    private String lastName;
    private String email;
    private String address;
    @ManyToOne
    @JoinColumn(name = "CodeGender", referencedColumnName = "code")
    private CodeGender genderCode;
    private String image;
    private String dob;
    private Integer companyId;
	@JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Account> accounts;

    @OneToOne
    @JoinColumn(name = "companyId", insertable = false, updatable = false)
    private Company company;


    @OneToMany(mappedBy = "user")
    private Set<Cv> cvs;
	
    @OneToMany(mappedBy = "user")
    private Set<Note> notes;

    @OneToMany(mappedBy = "user")
    private Set<OrderPackageCv> orderPackageCvs;

    @OneToMany(mappedBy = "user")
    private Set<OrderPackage> orderPackages;
	
    @OneToMany(mappedBy = "user")
    private Set<UserSkill> userSkills;

    @OneToOne(mappedBy = "user")
    private UserSetting userSetting;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public CodeGender getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(CodeGender genderCode) {
		this.genderCode = genderCode;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Set<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Set<Cv> getCvs() {
		return cvs;
	}

	public void setCvs(Set<Cv> cvs) {
		this.cvs = cvs;
	}

	public Set<Note> getNotes() {
		return notes;
	}

	public void setNotes(Set<Note> notes) {
		this.notes = notes;
	}

	public Set<OrderPackageCv> getOrderPackageCvs() {
		return orderPackageCvs;
	}

	public void setOrderPackageCvs(Set<OrderPackageCv> orderPackageCvs) {
		this.orderPackageCvs = orderPackageCvs;
	}

	public Set<OrderPackage> getOrderPackages() {
		return orderPackages;
	}

	public void setOrderPackages(Set<OrderPackage> orderPackages) {
		this.orderPackages = orderPackages;
	}

	public Set<UserSkill> getUserSkills() {
		return userSkills;
	}

	public void setUserSkills(Set<UserSkill> userSkills) {
		this.userSkills = userSkills;
	}

	public UserSetting getUserSetting() {
		return userSetting;
	}

	public void setUserSetting(UserSetting userSetting) {
		this.userSetting = userSetting;
	}

   

    // Getters and Setters
}