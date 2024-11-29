package com.doan.AppTuyenDung.DTO;
import java.util.Date;
public interface CompanyGetListDTO {
    String getId();
    String getName();
    String getThumbnail();
    String getCoverimage();
    String getDescriptionHTML();
    String getDescriptionMarkdown();
    String getWebsite();
    String getAddress();
    String getPhonenumber();
    String getAmountEmployer();
    String getTaxnumber();
    String getStatusCode();
    String getUserId();
    String getCensorCode();
    byte[] getFile();
    String getAllowPost();
    String getAllowHotPost();
    String getAllowCvFree();
    String getallowCV();
    Date getCreatedAt();
    Date getUpdatedAt();
}
