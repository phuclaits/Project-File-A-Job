package com.doan.AppTuyenDung.DTO.Response;

import lombok.Data;

@Data
public class PostJobTypeCountDTO {
    private String categoryJobCode;
    private Integer amount;
    private String value;
    private String code;
    private String image;

    public String getCategoryJobCode() {
        return this.categoryJobCode;
    }

    public void setCategoryJobCode(String categoryJobCode) {
        this.categoryJobCode = categoryJobCode;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public PostJobTypeCountDTO() {
    }
    public PostJobTypeCountDTO(String categoryJobCode, Integer amount, String value, String code, String image) {
        this.categoryJobCode = categoryJobCode;
        this.amount = amount;
        this.value = value;
        this.code = code;
        this.image = image;
    }
}