package com.doan.AppTuyenDung.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
@Entity
@Table(name = "usersettings")
public class UserSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "CodeJobType", referencedColumnName = "code")
    private CodeJobType categoryJobCode;
    @ManyToOne
    @JoinColumn(name = "CodeSalaryType", referencedColumnName = "code")
    private CodeSalaryType salaryJobCode;

    @ManyToOne
    @JoinColumn(name = "CodeAdressCode", referencedColumnName = "code")
    private CodeProvince addressCode;
    
    @ManyToOne
    @JoinColumn(name = "CodeExpType", referencedColumnName = "code")
    private CodeExpType experienceJobCode;

    private Boolean isFindJob;
    private Boolean isTakeMail;
    @Column(columnDefinition = "LONGBLOB")
    private byte[] file;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CodeJobType getCategoryJobCode() {
		return categoryJobCode;
	}

	public void setCategoryJobCode(CodeJobType categoryJobCode) {
		this.categoryJobCode = categoryJobCode;
	}

	public CodeSalaryType getSalaryJobCode() {
		return salaryJobCode;
	}

	public void setSalaryJobCode(CodeSalaryType salaryJobCode) {
		this.salaryJobCode = salaryJobCode;
	}

	public CodeProvince getAddressCode() {
		return addressCode;
	}

	public void setAddressCode(CodeProvince addressCode) {
		this.addressCode = addressCode;
	}

	public CodeExpType getExperienceJobCode() {
		return experienceJobCode;
	}

	public void setExperienceJobCode(CodeExpType experienceJobCode) {
		this.experienceJobCode = experienceJobCode;
	}

	public Boolean getIsFindJob() {
		return isFindJob;
	}

	public void setIsFindJob(Boolean isFindJob) {
		this.isFindJob = isFindJob;
	}

	public Boolean getIsTakeMail() {
		return isTakeMail;
	}

	public void setIsTakeMail(Boolean isTakeMail) {
		this.isTakeMail = isTakeMail;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

    // Getters and Setters
    
}