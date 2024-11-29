package com.doan.AppTuyenDung.DTO.Request;

import java.util.Date;


import lombok.Data;

@Data
public class CompanyDTO {
	 private String name;
	 private String thumbnail;
	 private String coverImage;
	 private String descriptionHTML;
	 private String descriptionMarkdown;
	 private String website;
	 private String address;
	 private String phonenumber;
	 private Integer amountEmployer;
	 private String taxnumber;
	 private String statusCode;
	 private String file;
	 private Integer allowPost;
	 private Integer allowHotPost;
	 private Integer allowCvFree;
	 private Integer allowCV;
	 private String censorCode;
	 private int idUser;
	 private Date createdAt;
	 private Date updatedAt; 
}
