package com.doan.AppTuyenDung.DTO.Response;


import lombok.*;

import java.io.Serializable;

@Data
public class postDetailResponse {
    private Integer id;
    private String name;
    private String descriptionHTML;
    private String descriptionMarkdown;
    private Integer amount;
    private CodeResponse jobTypePostData;
    private CodeResponse workTypePostData;
    private CodeResponse salaryTypePostData;
    private CodeResponse jobLevelPostData;
    private CodeResponse genderPostData;
    private CodeResponse provincePostData;
    private CodeResponse expTypePostData;
}
