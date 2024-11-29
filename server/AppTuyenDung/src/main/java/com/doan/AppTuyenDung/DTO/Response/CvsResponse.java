package com.doan.AppTuyenDung.DTO.Response;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class CvsResponse {

	private Integer idCv;
	private byte[] file;
    private Boolean isChecked;
    private String status;
    private Date interviewTime;
    private String description;
    private Date createdAtCv;
    private Date updatedAtCv;
    
    private Integer idPost;
    private String timeEnd;
    private String timePost;
    private Integer isHot;
    private Date createdAtPost;
    private Date updatedAtPost;
    
    private Integer idDetailPost;
    private String name;
    private String descriptionHTML;
    private String descriptionMarkdown;
    private Integer amount;
    
    private String codeJobType;
    private String valueJobType;
    
    private String codeWorkType;
    private String valueWorkType;
    
    private String codeSalaryType;
    private String valueSalaryType;
    
    private String codeJobLevel;
    private String valueJobLevel;
    
    private String codeGender;
    private String valueGender;
    
    private String codeProvince;
    private String valueProvince;
    
    private String codeExpType;
    private String valueExpType;
	public Integer getIdCv() {
		return idCv;
	}
	public void setIdCv(Integer idCv) {
		this.idCv = idCv;
	}
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
	public Boolean getIsChecked() {
		return isChecked;
	}
	public void setIsChecked(Boolean isChecked) {
		this.isChecked = isChecked;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getInterviewTime() {
		return interviewTime;
	}
	public void setInterviewTime(Date interviewTime) {
		this.interviewTime = interviewTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreatedAtCv() {
		return createdAtCv;
	}
	public void setCreatedAtCv(Date createdAtCv) {
		this.createdAtCv = createdAtCv;
	}
	public Date getUpdatedAtCv() {
		return updatedAtCv;
	}
	public void setUpdatedAtCv(Date updatedAtCv) {
		this.updatedAtCv = updatedAtCv;
	}
	public Integer getIdPost() {
		return idPost;
	}
	public void setIdPost(Integer idPost) {
		this.idPost = idPost;
	}
	public String getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}
	public String getTimePost() {
		return timePost;
	}
	public void setTimePost(String timePost) {
		this.timePost = timePost;
	}
	public Integer getIsHot() {
		return isHot;
	}
	public void setIsHot(Integer isHot) {
		this.isHot = isHot;
	}
	public Date getCreatedAtPost() {
		return createdAtPost;
	}
	public void setCreatedAtPost(Date createdAtPost) {
		this.createdAtPost = createdAtPost;
	}
	public Date getUpdatedAtPost() {
		return updatedAtPost;
	}
	public void setUpdatedAtPost(Date updatedAtPost) {
		this.updatedAtPost = updatedAtPost;
	}
	public Integer getIdDetailPost() {
		return idDetailPost;
	}
	public void setIdDetailPost(Integer idDetailPost) {
		this.idDetailPost = idDetailPost;
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
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getCodeJobType() {
		return codeJobType;
	}
	public void setCodeJobType(String codeJobType) {
		this.codeJobType = codeJobType;
	}
	public String getValueJobType() {
		return valueJobType;
	}
	public void setValueJobType(String valueJobType) {
		this.valueJobType = valueJobType;
	}
	public String getCodeWorkType() {
		return codeWorkType;
	}
	public void setCodeWorkType(String codeWorkType) {
		this.codeWorkType = codeWorkType;
	}
	public String getValueWorkType() {
		return valueWorkType;
	}
	public void setValueWorkType(String valueWorkType) {
		this.valueWorkType = valueWorkType;
	}
	public String getCodeSalaryType() {
		return codeSalaryType;
	}
	public void setCodeSalaryType(String codeSalaryType) {
		this.codeSalaryType = codeSalaryType;
	}
	public String getValueSalaryType() {
		return valueSalaryType;
	}
	public void setValueSalaryType(String valueSalaryType) {
		this.valueSalaryType = valueSalaryType;
	}
	public String getCodeJobLevel() {
		return codeJobLevel;
	}
	public void setCodeJobLevel(String codeJobLevel) {
		this.codeJobLevel = codeJobLevel;
	}
	public String getValueJobLevel() {
		return valueJobLevel;
	}
	public void setValueJobLevel(String valueJobLevel) {
		this.valueJobLevel = valueJobLevel;
	}
	public String getCodeGender() {
		return codeGender;
	}
	public void setCodeGender(String codeGender) {
		this.codeGender = codeGender;
	}
	public String getValueGender() {
		return valueGender;
	}
	public void setValueGender(String valueGender) {
		this.valueGender = valueGender;
	}
	public String getCodeProvince() {
		return codeProvince;
	}
	public void setCodeProvince(String codeProvince) {
		this.codeProvince = codeProvince;
	}
	public String getValueProvince() {
		return valueProvince;
	}
	public void setValueProvince(String valueProvince) {
		this.valueProvince = valueProvince;
	}
	public String getCodeExpType() {
		return codeExpType;
	}
	public void setCodeExpType(String codeExpType) {
		this.codeExpType = codeExpType;
	}
	public String getValueExpType() {
		return valueExpType;
	}
	public void setValueExpType(String valueExpType) {
		this.valueExpType = valueExpType;
	}
    
    
}
