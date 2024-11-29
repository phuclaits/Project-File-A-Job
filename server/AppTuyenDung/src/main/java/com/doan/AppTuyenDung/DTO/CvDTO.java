package com.doan.AppTuyenDung.DTO;

public class CvDTO {
    private Integer userId;
    private byte[] file;
    private Integer postId;
    private String description;

    public CvDTO(Integer userId, byte[] file, Integer postId, String description) {
        this.userId = userId;
        this.file = file;
        this.postId = postId;
        this.description = description;
    }

    public CvDTO() {
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public byte[] getFile() {
        return this.file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public Integer getPostId() {
        return this.postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
}