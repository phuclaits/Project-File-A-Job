package com.doan.AppTuyenDung.DTO.GetAllSkillAdmin;

public class SkillDTO {
    public String name;
    public String categoryJobCode;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryJobCode() {
        return this.categoryJobCode;
    }

    public void setCategoryJobCode(String categoryJobCode) {
        this.categoryJobCode = categoryJobCode;
    }
    public SkillDTO() {
    }
    public SkillDTO(String name, String categoryJobCode) {
        this.name = name;
        this.categoryJobCode = categoryJobCode;
    }
}
