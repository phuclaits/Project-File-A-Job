package com.doan.AppTuyenDung.DTO.Response;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CvStatusResponse {
	Integer id;
	Integer userId;
	Integer postId;
	Integer detailPostId;
	String lastName;
	String firstName;
	String email;
	String status;
	Boolean isChecked;
	java.util.Date interviewTime;
	String description;
	byte[] file;
	String FilePDF;
}
