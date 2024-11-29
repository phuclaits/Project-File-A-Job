package com.doan.AppTuyenDung.DTO;

import org.springframework.web.multipart.MultipartFile;

public class CreateCompanyRequest {
    private Integer userId;
    private String name;
    private MultipartFile thumbnail;
    private MultipartFile coverimage;
    
    private String descriptionHTML;
    private String descriptionMarkdown;
    private String website;
    private String address;
    private String phonenumber;
    private Integer amountEmployer;
    private String taxnumber;
    private String file;
    


    public CreateCompanyRequest() {
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public MultipartFile getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(MultipartFile thumbnail) {
        this.thumbnail = thumbnail;
    }
    public MultipartFile getCoverimage() {
        return coverimage;
    }
    public void setCoverimage(MultipartFile coverimage) {
        this.coverimage = coverimage;
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
    public String getWebsite() {
        return website;
    }
    public void setWebsite(String website) {
        this.website = website;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPhonenumber() {
        return phonenumber;
    }
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    public Integer getAmountEmployer() {
        return amountEmployer;
    }
    public void setAmountEmployer(Integer amountEmployer) {
        this.amountEmployer = amountEmployer;
    }
    public String getTaxnumber() {
        return taxnumber;
    }
    public void setTaxnumber(String taxnumber) {
        this.taxnumber = taxnumber;
    }
    public String getFile() {
        return file;
    }
    public void setFile(String file) {
        this.file = file;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    // Getters and Setters
}
