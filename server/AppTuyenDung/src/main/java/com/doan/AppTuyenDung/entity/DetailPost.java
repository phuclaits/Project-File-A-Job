package com.doan.AppTuyenDung.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "detailposts")
public class DetailPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    @Column(columnDefinition = "LONGTEXT")
    private String descriptionHTML;
    @Column(columnDefinition = "LONGTEXT")
    private String descriptionMarkdown;
    @ManyToOne
    @JoinColumn(name = "CodeJobType", referencedColumnName = "code")
    private CodeJobType categoryJobCode;
    @ManyToOne
    @JoinColumn(name = "CodeAdressCode", referencedColumnName = "code")
    private CodeProvince addressCode;
    @ManyToOne
    @JoinColumn(name = "CodeSalaryType", referencedColumnName = "code")
    private CodeSalaryType salaryJobCode;

    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "CodeJobLevel", referencedColumnName = "code")
    private CodeJobLevel categoryJoblevelCode;
    
    @ManyToOne
    @JoinColumn(name = "CodeWorkType", referencedColumnName = "code")
    private CodeWorkType categoryWorktypeCode;

    @ManyToOne
    @JoinColumn(name = "CodeExpType", referencedColumnName = "code")
    private CodeExpType experienceJobCode;

    @ManyToOne
    @JoinColumn(name = "CodeGenderPost", referencedColumnName = "code")
    private CodeGenderPost genderPostCode;

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

	public String getDescriptionHTML() {
		return descriptionHTML;
	}

	public void setDescriptionHTML(String descriptionHTML) {
		this.descriptionHTML = descriptionHTML;
	}

	public String getDescriptionMarkdown() {
		return descriptionMarkdown;
	}

	public void setDescriptionMarkdown(String descriptionMarkdown) {
		this.descriptionMarkdown = descriptionMarkdown;
	}

	public CodeJobType getCategoryJobCode() {
		return categoryJobCode;
	}

	public void setCategoryJobCode(CodeJobType categoryJobCode) {
		this.categoryJobCode = categoryJobCode;
	}

	public CodeProvince getAddressCode() {
		return addressCode;
	}

	public void setAddressCode(CodeProvince addressCode) {
		this.addressCode = addressCode;
	}

	public CodeSalaryType getSalaryJobCode() {
		return salaryJobCode;
	}

	public void setSalaryJobCode(CodeSalaryType salaryJobCode) {
		this.salaryJobCode = salaryJobCode;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public CodeJobLevel getCategoryJoblevelCode() {
		return categoryJoblevelCode;
	}

	public void setCategoryJoblevelCode(CodeJobLevel categoryJoblevelCode) {
		this.categoryJoblevelCode = categoryJoblevelCode;
	}

	public CodeWorkType getCategoryWorktypeCode() {
		return categoryWorktypeCode;
	}

	public void setCategoryWorktypeCode(CodeWorkType categoryWorktypeCode) {
		this.categoryWorktypeCode = categoryWorktypeCode;
	}

	public CodeExpType getExperienceJobCode() {
		return experienceJobCode;
	}

	public void setExperienceJobCode(CodeExpType experienceJobCode) {
		this.experienceJobCode = experienceJobCode;
	}

	public CodeGenderPost getGenderPostCode() {
		return genderPostCode;
	}

	public void setGenderPostCode(CodeGenderPost genderPostCode) {
		this.genderPostCode = genderPostCode;
	}
   

    // Getters and Setters
    
}