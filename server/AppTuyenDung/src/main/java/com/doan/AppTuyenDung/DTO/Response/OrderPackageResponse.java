package com.doan.AppTuyenDung.DTO.Response;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderPackageResponse {
	private Integer id;
    private Double currentPrice;
    private Integer amount;
    private Date createdAt;
    private Date updatedAt;
    private UserResponse userData;
    private PackageResponse packageData;
    private PackageCvResponse packageCvData;
}
