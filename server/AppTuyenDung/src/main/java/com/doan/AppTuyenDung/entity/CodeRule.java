package com.doan.AppTuyenDung.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "CodeRule")
public class CodeRule {
    @Id
    private String code;
    private String type;
    private String value;
    private String image;
    @JsonIgnore
    @OneToMany(mappedBy = "roleCode")
    private List<Account> accounts;
    
    public CodeRule(String code, String type, String value, String image, List<Account> accounts) {
        this.code = code;
        this.type = type;
        this.value = value;
        this.image = image;
        this.accounts = accounts;
    }
    public CodeRule() {
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

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
