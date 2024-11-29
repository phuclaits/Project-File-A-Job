package com.doan.AppTuyenDung.DTO.Response;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class CompanyResponse {
	private Integer id;
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
	private int userId;
	private String censorCode;
	private String file;
	private Integer allowPost;
	private Integer allowHotPost;
	private Integer allowCvFree;
	private Integer allowCV;
	private Date createdAt;
	private Date updatedAt;
	private CodeResponse censorData;
	private List<PostResponse> postData;
	public List<PostResponse> getPostData() {
        return postData;
    }
}
