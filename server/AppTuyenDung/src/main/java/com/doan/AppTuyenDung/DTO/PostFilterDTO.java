package com.doan.AppTuyenDung.DTO;
import java.util.Date;
public interface PostFilterDTO {
    Integer getId();
    String getPostName();
    String getFullName();
    String getTimeEnd();
    String getStatusCode();
    String getStatusValue();
    Date getUpdateTime();
    String getGenderPostCode();
}
