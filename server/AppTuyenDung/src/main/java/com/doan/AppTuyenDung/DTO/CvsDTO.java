package com.doan.AppTuyenDung.DTO;

import lombok.Data;

@Data
public class CvsDTO {
    private Integer id;
    private String status;
    private Integer userId;
    private Integer postId;

    public CvsDTO() {
    }

    // Constructor
    public CvsDTO(Integer id, String status, Integer userId, Integer postId) {

        this.id = id;

        this.status = status;

        this.userId = userId;

        this.postId = postId;

    }
}
