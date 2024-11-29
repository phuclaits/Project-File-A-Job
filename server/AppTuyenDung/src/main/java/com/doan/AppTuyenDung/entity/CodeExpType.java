package com.doan.AppTuyenDung.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity

@Table(name = "CodeExpType")
public class CodeExpType {
    @Id
    private String code;
    private String type;
    private String value;
    private String image;
    @JsonIgnore
    @OneToMany(mappedBy = "experienceJobCode")
    private List<DetailPost> detailPosts;
    @JsonIgnore
    @OneToMany(mappedBy = "experienceJobCode")
    private List<UserSetting> userSettings;

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

    public List<DetailPost> getDetailPosts() {
        return detailPosts;
    }

    public void setDetailPosts(List<DetailPost> detailPosts) {
        this.detailPosts = detailPosts;
    }

    public List<UserSetting> getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(List<UserSetting> userSettings) {
        this.userSettings = userSettings;
    }

    public CodeExpType() {
    }

    public CodeExpType(String code, String type, String value, String image, List<DetailPost> detailPosts,
            List<UserSetting> userSettings) {
        this.code = code;
        this.type = type;
        this.value = value;
        this.image = image;
        this.detailPosts = detailPosts;
        this.userSettings = userSettings;
    }
    public static CodeExpType fromString(String code) {
        CodeExpType expType = new CodeExpType();
        expType.setCode(code);
        return expType;
    }

}
