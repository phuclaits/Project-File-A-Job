package com.doan.AppTuyenDung.Repositories.criteria;

public class FilterData {
    public String salaryJobCode;
    public String categoryWorktypeCode;
    public String experienceJobCode;
    public String categoryJoblevelCode;
    public String categoryJobCode;
    public String addressCode;
    public String search;
    public Integer isHot;
    public FilterData() {
    }
    public FilterData(String salaryJobCode, String categoryWorktypeCode, String experienceJobCode,
            String categoryJoblevelCode, String categoryJobCode, String addressCode, String search, Integer isHot) {
        this.salaryJobCode = salaryJobCode;
        this.categoryWorktypeCode = categoryWorktypeCode;
        this.experienceJobCode = experienceJobCode;
        this.categoryJoblevelCode = categoryJoblevelCode;
        this.categoryJobCode = categoryJobCode;
        this.addressCode = addressCode;
        this.search = search;
        this.isHot = isHot;
    }
    public String getSalaryJobCode() {
        return salaryJobCode;
    }
    public void setSalaryJobCode(String salaryJobCode) {
        this.salaryJobCode = salaryJobCode;
    }
    public String getCategoryWorktypeCode() {
        return categoryWorktypeCode;
    }
    public void setCategoryWorktypeCode(String categoryWorktypeCode) {
        this.categoryWorktypeCode = categoryWorktypeCode;
    }
    public String getExperienceJobCode() {
        return experienceJobCode;
    }
    public void setExperienceJobCode(String experienceJobCode) {
        this.experienceJobCode = experienceJobCode;
    }
    public String getCategoryJoblevelCode() {
        return categoryJoblevelCode;
    }
    public void setCategoryJoblevelCode(String categoryJoblevelCode) {
        this.categoryJoblevelCode = categoryJoblevelCode;
    }
    public String getCategoryJobCode() {
        return categoryJobCode;
    }
    public void setCategoryJobCode(String categoryJobCode) {
        this.categoryJobCode = categoryJobCode;
    }
    public String getAddressCode() {
        return addressCode;
    }
    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }
    public String getSearch() {
        return search;
    }
    public void setSearch(String search) {
        this.search = search;
    }
    public Integer getIsHot() {
        return isHot;
    }
    public void setIsHot(Integer isHot) {
        this.isHot = isHot;
    }
}
