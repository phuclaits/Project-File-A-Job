package com.doan.AppTuyenDung.DTO;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CompanyRequest {
    private String id;
    private String name;
    private String phonenumber;
    private String address;
    private String descriptionHTML;
    private String descriptionMarkdown;
    private String website;
    private String taxnumber;
    private int amountEmployer;
    private String file;
    private MultipartFile thumbnail;
    private MultipartFile coverimage;
    
}
