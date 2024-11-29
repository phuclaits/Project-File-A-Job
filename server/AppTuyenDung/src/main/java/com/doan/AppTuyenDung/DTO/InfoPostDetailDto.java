package com.doan.AppTuyenDung.DTO;

public interface InfoPostDetailDto {
    Integer getId();
    String getTimePostValue();
    String getTimeEndValue();
    String getName();
    String getDescriptionHTMLValue();
    String getDescriptionMarkdownValue();
    String getCategoryJobCodeValue();
    String getCategoryJobLevelCodeValue();
    String getCategoryWorktypeCodeValue();
    String getExperienceJobCodeValue();
    String getGenderPostCodeValue();
    String getAdressCodeValue();
    String getSalaryCodeValue();
    String getNameCompanyValue();
    String getAddressCompanyValue();
    String getCoverImageCompanyValue();
    String getPhoneCompanyValue();
    String getTaxNumberValue();
    String getThumbnailCompanyValue();
    String getWebsiteCompanyValue();
    String getEmployerCompanyValue();
    

    // Phát sinh
    String getCategoryJobCode();
    String getExperienceJobCode();
    String getCategoryJobLevelCode();
    String getCategoryWorktypeCode();
    String getSalaryCode();
    String getAdressCode();
    String getGenderPostCode();
    Integer getAmount();
    String getEmailCompany();


}
