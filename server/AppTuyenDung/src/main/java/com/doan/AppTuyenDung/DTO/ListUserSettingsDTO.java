package com.doan.AppTuyenDung.DTO;

import com.doan.AppTuyenDung.entity.CodeExpType;
import com.doan.AppTuyenDung.entity.CodeJobType;
import com.doan.AppTuyenDung.entity.CodeProvince;
import com.doan.AppTuyenDung.entity.CodeSalaryType;
import com.doan.AppTuyenDung.entity.UserSetting;




public class ListUserSettingsDTO extends UserSetting {
    private Integer bonus;
    private String matchPercentage;
    private Integer userId;
    private String first_Name;
    private String last_Name;
    private String jobTypeValue;
    public ListUserSettingsDTO() {
        super();
        this.bonus = 0; 
        this.matchPercentage = "0%";
        
    }

    // Constructor to initialize UserSetting fields and bonus
    public ListUserSettingsDTO(Integer id, byte[] file, Boolean isFindJob, Boolean isTakeMail,
                          String addressCode, String categoryJobCode,
                          String experienceJobCode, String salaryJobCode, Integer bonus,Integer userId,
                          String first_Name,String last_Name,String jobTypeValue) {
        super(); 
        this.setId(id);
        this.setFile(file);
        this.setIsFindJob(isFindJob);
        this.setIsTakeMail(isTakeMail);
        this.setAddressCode(CodeProvince.fromString(addressCode)); 
        this.setCategoryJobCode(CodeJobType.fromString(categoryJobCode));
        this.setExperienceJobCode(CodeExpType.fromString(experienceJobCode));
        this.setSalaryJobCode(CodeSalaryType.fromString(salaryJobCode));
        this.bonus = bonus;
        this.matchPercentage = "0%";
        this.userId = userId;
        this.first_Name = first_Name;
        this.last_Name = last_Name;
        this.jobTypeValue = jobTypeValue;
    }
    
    // Getter and Setter for bonus
    public Integer getBonus() { return bonus; }
    public void setBonus(Integer bonus) { this.bonus = bonus; }
    public String getMatchPercentage() { return matchPercentage; }
    public void setMatchPercentage(String matchPercentage) { this.matchPercentage = matchPercentage; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getFirstName() { return first_Name; }
    public void setFirstName(String first_Name) { this.first_Name = first_Name; }

    public String getLastName() { return last_Name; }
    public void setLastName(String last_Name) { this.last_Name = last_Name; }

    public String getJobTypeValue() { return jobTypeValue; }
    public void setJobTypeValue(String jobTypeValue) { this.jobTypeValue = jobTypeValue; }
}
