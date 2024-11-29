package com.doan.AppTuyenDung.DTO.Response;

import java.util.Date;
import lombok.Data;

@Data
public class PostResponse {
	private Integer id;
	private String statusCode;
    private String timeEnd;
    private String timePost;
    private String thumbnail;
    private Integer userId;
    private Integer isHot;
    private Date createdAt;
    private Date updatedAt;
    private postDetailResponse postDetailData;
    private UserResponse userPostData;
}
