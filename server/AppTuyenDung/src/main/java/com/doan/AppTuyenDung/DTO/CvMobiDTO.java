package com.doan.AppTuyenDung.DTO;

import lombok.Data;

@Data
public class CvMobiDTO {
    private Integer userId;
    private byte[] file;
    private Integer postId;
    private String description;
    private String fileBase64;

    
}