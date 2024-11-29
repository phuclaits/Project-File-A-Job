package com.doan.AppTuyenDung.DTO.CreatePostEmployerCompany;

import lombok.Data;

@Data
public class PostDTO {
    private int id;
    private String name;
    private String categoryJobCode;
    private String addressCode;
    private String salaryJobCode;
    private int amount;
    private String timeEnd;
    private String categoryJoblevelCode;
    private Integer userId;
    private String categoryWorktypeCode;
    private String experienceJobCode;
    private String genderPostCode;
    private String descriptionHTML;
    private String descriptionMarkdown;
    private int isHot;
}
