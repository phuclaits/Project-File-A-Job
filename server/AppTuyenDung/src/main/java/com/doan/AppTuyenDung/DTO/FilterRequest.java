package com.doan.AppTuyenDung.DTO;

import java.util.List;

import lombok.Data;

@Data
public class FilterRequest {
    private Integer limit; // Giá trị mặc định
    private Integer offset; // Giá trị mặc định
    private String categoryJobCode;
    private String experienceJobCode;
    private String salaryCode;
    private String provinceCode;
    private List<Integer> listSkills;
    private List<String> otherSkills;
    
}
