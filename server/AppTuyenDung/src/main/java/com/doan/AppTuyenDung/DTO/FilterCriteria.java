package com.doan.AppTuyenDung.DTO;

public class FilterCriteria {
    private String salaryJobCode;
    private String categoryWorktypeCode;
    private String experienceJobCode;
    private String categoryJoblevelCode;
    private String categoryJobCode;
    private String addressCode;
    private String search;
    private Integer limit;
    private Integer offset;
    private Boolean isHot;
    public FilterCriteria() {
    }
    public FilterCriteria(String salaryJobCode, String categoryWorktypeCode, String experienceJobCode,
            String categoryJoblevelCode, String categoryJobCode, String addressCode, String search, Integer limit,
            Integer offset, Boolean isHot) {
        this.salaryJobCode = salaryJobCode;
        this.categoryWorktypeCode = categoryWorktypeCode;
        this.experienceJobCode = experienceJobCode;
        this.categoryJoblevelCode = categoryJoblevelCode;
        this.categoryJobCode = categoryJobCode;
        this.addressCode = addressCode;
        this.search = search;
        this.limit = limit;
        this.offset = offset;
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
    public Integer getLimit() {
        return limit;
    }
    public void setLimit(Integer limit) {
        this.limit = limit;
    }
    public Integer getOffset() {
        return offset;
    }
    public void setOffset(Integer offset) {
        this.offset = offset;
    }
    public Boolean getIsHot() {
        return isHot;
    }
    public void setIsHot(Boolean isHot) {
        this.isHot = isHot;
    }
}
