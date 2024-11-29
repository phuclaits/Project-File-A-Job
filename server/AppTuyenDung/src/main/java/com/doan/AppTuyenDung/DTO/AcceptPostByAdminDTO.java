package com.doan.AppTuyenDung.DTO;

import lombok.Data;

@Data
public class AcceptPostByAdminDTO {
    private Integer id;
    private String statusCode;
    private String note;
    private Integer userId;
}
