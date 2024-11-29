package com.doan.AppTuyenDung.DTO;
import java.util.Date;

public interface UserAccountDTO {
    Integer getId();
    String getFirstName();
    String getLastName();
    String getEmail();
    String getAddressUser();
    String getImage();
    String getGenderCodeValue();
    Integer getIdCompany();
    String getPhoneNumber();
    String getCodeStatusValue();
    String getCodeRoleValue();
    String getPassword();
    Date getCreatedAtUser();    
    String getDobUser();
    String getCodeRoleAccount();
}
