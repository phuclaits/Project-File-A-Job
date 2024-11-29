package com.doan.AppTuyenDung.DTO;

import lombok.Data;

@Data
public class UpdatePostDTO {
    private Integer id;
    private String name;
    private String categoryJobCode;
    private String addressCode;
    private String salaryJobCode;
    private Integer amount;
    private String timeEnd;
    private String categoryJoblevelCode;
    private String categoryWorktypeCode;
    private String experienceJobCode;
    private String genderPostCode;
    private String descriptionHTML;
    private String descriptionMarkdown;
    private Integer userId;
}
