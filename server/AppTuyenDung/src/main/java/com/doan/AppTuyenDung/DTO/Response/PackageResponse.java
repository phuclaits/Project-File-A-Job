package com.doan.AppTuyenDung.DTO.Response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PackageResponse {
	private Integer id;
    private String name;
    private String value;
    private Double price;
    private Boolean isHot;
    private Boolean isActive;
}
